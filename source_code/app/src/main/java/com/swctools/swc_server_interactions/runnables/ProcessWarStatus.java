package com.swctools.swc_server_interactions.runnables;

import android.content.ContentValues;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

import com.swctools.activity_modules.main.models.PlayerDAO;
import com.swctools.activity_modules.war_room.processing_models.WarRoomData_CurrentlyDefending;
import com.swctools.activity_modules.war_room.processing_models.WarRoomData_Outposts;
import com.swctools.activity_modules.war_room.processing_models.WarRoomData_WarParticipant;
import com.swctools.common.enums.ApplicationMessageTemplates;
import com.swctools.common.enums.BundleKeys;
import com.swctools.common.enums.ScreenCommands.DeployableTypes;
import com.swctools.common.helpers.BundleHelper;
import com.swctools.common.helpers.WarDataHelper;
import com.swctools.common.model_list_providers.GameUnitConversionListProvider;
import com.swctools.common.models.player_models.DonatedTroops;
import com.swctools.common.models.player_models.MapBuildings;
import com.swctools.common.models.player_models.TacticalCapItem;
import com.swctools.common.models.player_models.Troop;
import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;
import com.swctools.swc_server_interactions.PlayerLoginSession;
import com.swctools.swc_server_interactions.SWCMessage;
import com.swctools.swc_server_interactions.results.SWCDefaultResponse;
import com.swctools.swc_server_interactions.results.SWCDefaultResultData;
import com.swctools.swc_server_interactions.results.SWCGetPublicGuildResponseData;
import com.swctools.swc_server_interactions.results.SWCGetWarStatusResultJson;
import com.swctools.swc_server_interactions.results.SWCGuildWarPartRespData;
import com.swctools.swc_server_interactions.results.SWCVisitResult;
import com.swctools.swc_server_interactions.results.models.WarHistory;
import com.swctools.swc_server_interactions.swc_commands.Cmd_GetWarParticipant;
import com.swctools.swc_server_interactions.swc_commands.Cmd_GuildWarStatus;
import com.swctools.swc_server_interactions.threads.SWC_Interaction_Thread;
import com.swctools.util.MethodResult;
import com.swctools.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;

import javax.json.JsonObject;
import javax.json.JsonValue;

public class ProcessWarStatus extends SWC_Runnable_Base implements Runnable {
    private static final String TAG = "ProcessWarStatus";

    //Constants for messages:
    private static final String NO_ACTIVE_WAR_ID = "No active war found!";
    private static final String NO_GUILD_ID = "Your player is not in a squad!!";

    private String playerId;
    private Context context;


    public ProcessWarStatus(String playerId, Context context, Handler swc_interaction_handler) {
        super(swc_interaction_handler);
        this.playerId = playerId;
        this.context = context;
        this.swc_interaction_handler = swc_interaction_handler;
    }

    @Override
    public void run() {
        MethodResult methodResult = GetWarRoomData(playerId, context);
//        MethodResult methodResult = new MethodResult(true, "4d9dd498-72a9-11ea-b79d-1e79da990f2c");
        Message msg = Message.obtain();


        /*DEBUG - RETURN A WAR ID TO SAVE CONSTANTLY HAMMERING THE SERVER DURING TESTING:

         */
        msg.what = SWC_Interaction_Thread.SWC_Interaction_Handler.GET_WAR_ROOM_DATA;
        msg.obj = methodResult;
        swc_interaction_handler.sendMessage(msg);
    }

    private MethodResult GetWarRoomData(String playerId, Context mContext) {
//        PlayerLoginSession warBotLoginSession = new PlayerLoginSession(appConfig.getBot2ID(), appConfig.getBot2Secret(), appConfig.getVisitorDeviceId(), false);
        PlayerDAO playerDAO = new PlayerDAO(playerId, mContext);
        PlayerLoginSession playerLoginSession = new PlayerLoginSession(playerId, playerDAO.getPlayerSecret(), playerDAO.getDeviceId(), true);
        sendProgressUpdateToUI("Logging in...");
        if (!playerLoginSession.login(true, mContext).success) {
            return playerLoginSession.getLoginResult();
        }
        //Logged in... now visit player and get their guild data...

        try {
            SWCVisitResult swcVisitResult = visitPlayer(playerId, mContext, playerLoginSession);
            //get guild:
            if (!StringUtil.isStringNotNull(swcVisitResult.mplayerModel().guildInfo().getString("guildId"))) {
                return new MethodResult(false, NO_GUILD_ID);
            }
            String myGuildId = swcVisitResult.mplayerModel().guildInfo().getString("guildId");
            SWCGetPublicGuildResponseData guildResponseData = getSwcGetPublicGuildResponseData(mContext, playerLoginSession, myGuildId);
            MethodResult checkWarID = checkWarId(guildResponseData);

            if (!checkWarID.success) {
                return checkWarID;
            }
            String warId = guildResponseData.getcurrentWarId();
            //1st, login with player.
            playerLoginSession.login(true, mContext);
            if (!playerLoginSession.isLoggedIn()) {
                return playerLoginSession.getLoginResult();
            }
            sendProgressUpdateToUI("Getting War Data...");
            //2nd, get the war participant:
            MethodResult getWarPart = getWarParticipantData(playerLoginSession, mContext);
            if (!getWarPart.success) {//Something went wrong with getting war participant data. Quit and return result.
                return getWarPart;
            }
            SWCGuildWarPartRespData swcGuildWarPartRespData = (SWCGuildWarPartRespData) getWarPart.getResultObject();
            //wait a bit... seem to get timestamp errors otherwise!
            SystemClock.sleep(1500);
            //Now get the war status..
            MethodResult warStatusResult = getWarStatus(playerLoginSession, warId, mContext);
            if (!warStatusResult.success) {
                return warStatusResult;
            }
            SWCGetWarStatusResultJson swcGetWarStatusResultJson = (SWCGetWarStatusResultJson) warStatusResult.getResultObject();
            //Now get the rival squad's data.
            //MethodResult rivalSquadData = /


            Cmd_GuildWarStatus cmd_guildWarStatus = new Cmd_GuildWarStatus(playerLoginSession.newRequestId(), playerId, warId, playerLoginSession.getLoginTime());
            SWCDefaultResponse getWarStatusResponse = new SWCMessage(playerLoginSession.getAuthKey(), true, playerLoginSession.getLoginTime(), cmd_guildWarStatus, "processWarStatus|guild.war.status", mContext).getSwcMessageResponse();
            SWCDefaultResultData guildWarStatusData = new SWCDefaultResultData(getWarStatusResponse.getResponseDataByRequestId(playerLoginSession.getRequestId()));
            if (guildWarStatusData.isSuccess()) {
                String key = String.format(ApplicationMessageTemplates.VISIT_KEY.getemplateString(), Cmd_GuildWarStatus.COMMAND, playerId);
                BundleHelper bundleHelper = new BundleHelper(key, guildWarStatusData.getResult().toString());
                bundleHelper.commit(mContext);
                //now go get the rival squad's data
                String rivalId = guildWarStatusData.getResult().getJsonObject("rival").getString("guildId");
                SWCGetPublicGuildResponseData rivalData = getSwcGetPublicGuildResponseData(context, playerLoginSession, rivalId);
                key = String.format(ApplicationMessageTemplates.VISIT_KEY.getemplateString(), BundleKeys.GUILD_ID, rivalId);
                bundleHelper = new BundleHelper(key, rivalData.toString());
                bundleHelper.commit(mContext);

                sendProgressUpdateToUI("Processing the data...");
                populateDbTables(warId, playerId, myGuildId, rivalId, swcGuildWarPartRespData, swcGetWarStatusResultJson, guildResponseData, rivalData);


                return new MethodResult(true, warId);
            } else {
                return new MethodResult(false, "Error getting your war data, " + guildWarStatusData.getStatusCodeAndName());
            }

        } catch (Exception e) { //report error
            e.printStackTrace();
            return new MethodResult(false, e);
        }

    }


    private static MethodResult getWarParticipantData(PlayerLoginSession playerLoginSession, Context mContext) {
        try {
            SWCDefaultResponse getWarParticipantResponse = new SWCMessage(
                    playerLoginSession.getAuthKey(),
                    true,
                    playerLoginSession.getLoginTime(),
                    new Cmd_GetWarParticipant(playerLoginSession.newRequestId(), playerLoginSession.getPlayerId(), playerLoginSession.getLoginTime()),
                    "processWarStatus|guild.war.getParticipant",
                    mContext).getSwcMessageResponse();
            SWCDefaultResultData guildWarPartData = new SWCDefaultResultData(getWarParticipantResponse.getResponseDataByRequestId(playerLoginSession.getRequestId()));
            SWCGuildWarPartRespData swcGuildWarPartRespData = new SWCGuildWarPartRespData(guildWarPartData.getResult());
            if (guildWarPartData.isSuccess()) {//save the results and carry on.
                String key = String.format(ApplicationMessageTemplates.VISIT_KEY.getemplateString(), Cmd_GetWarParticipant.COMMAND, playerLoginSession.getPlayerId());
                BundleHelper bundleHelper = new BundleHelper(key, guildWarPartData.getResult().toString());
                bundleHelper.commit(mContext);
                return new MethodResult(true, swcGuildWarPartRespData);
            } else {
                return new MethodResult(false, guildWarPartData.getStatusCodeAndName());
            }
        } catch (Exception e) {
            return new MethodResult(false, e);
        }


    }

    private static MethodResult checkWarId(SWCGetPublicGuildResponseData guildResponseData) {
        JsonValue warIdJsonValue = guildResponseData.getCurrentWarIdJSONValue();
        /* TO CHECK THERE IS A VALID/CURRENT WAR ID AND AVOID BOOTING SQUAD FROM MM
         * Logic:
         * 1. Check if a War Id exists. If not found, return false, if found then:
         * 2. Check war history. If Id found in history, check there is not a queue entering time. If none found, war has ended and in delay phase. Allow carry on as it may still return data
         * If it is found, exit as the squad has entered MM.
         * 3. If no Id found in history, then war is active and return true
         * */

        if (warIdJsonValue != null) {
            try {
                String warId;
                if (StringUtil.isStringNotNull(guildResponseData.getcurrentWarId())) {
                    warId = guildResponseData.getcurrentWarId();
                    //Check war id is NOT in history.
                    for (JsonValue jsonValue : guildResponseData.getWarHistory()) {
                        WarHistory warHistory = new WarHistory((JsonObject) jsonValue);
                        if (warHistory.getWarId().equalsIgnoreCase(warId)) {
                            JsonValue signUpTime = guildResponseData.resultData.get("warSignUpTime");
                            if (signUpTime != null) {//then a new war has been initiated
                                return new MethodResult(false, NO_ACTIVE_WAR_ID);
                            }
                        }
                    }
                    return new MethodResult(true, warId);
                } else {
                    return new MethodResult(false, NO_ACTIVE_WAR_ID);
                }
            } catch (Exception e) {
                return new MethodResult(false, "Error getting current war Id. Are you in a war?!");
            }

        } else {
            return new MethodResult(false, NO_ACTIVE_WAR_ID);
        }


    }


    private static MethodResult getWarStatus(PlayerLoginSession playerLoginSession, String warId, Context mContext) {


        try {
            SWCDefaultResponse getWarStatusResponse = new SWCMessage(
                    playerLoginSession.getAuthKey(),
                    true, playerLoginSession.getLoginTime(),
                    new Cmd_GuildWarStatus(playerLoginSession.newRequestId(), playerLoginSession.getPlayerId(), warId, playerLoginSession.getLoginTime()),
                    "processWarStatus|guild.war.status", mContext).getSwcMessageResponse();
            SWCDefaultResultData guildWarStatusData = new SWCDefaultResultData(getWarStatusResponse.getResponseDataByRequestId(playerLoginSession.getRequestId()));


            SWCGetWarStatusResultJson swcGetWarStatusResultJson = new SWCGetWarStatusResultJson(guildWarStatusData.getResult());

            if (guildWarStatusData.isSuccess()) {
                String key = String.format(ApplicationMessageTemplates.VISIT_KEY.getemplateString(), Cmd_GuildWarStatus.COMMAND, playerLoginSession.getPlayerId());
                BundleHelper bundleHelper = new BundleHelper(key, guildWarStatusData.getResult().toString());
                bundleHelper.commit(mContext);

                return new MethodResult(true, swcGetWarStatusResultJson);
            } else {

                return new MethodResult(false, "Error getting your war data, " + guildWarStatusData.getStatusCodeAndName());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new MethodResult(false, e);
        }

    }

    private HashMap<String, WarRoomData_WarParticipant> getParticipantsHashMap(SWCGuildWarPartRespData warParticipantData, SWCGetWarStatusResultJson warStatusData, SWCGetPublicGuildResponseData guildData, SWCGetPublicGuildResponseData rivalData) {
        HashMap<String, WarRoomData_WarParticipant> partcipantHashMap = new HashMap<>();
//My guild:
        for (JsonValue jsonValue : warStatusData.getGuildParticipants()) {
            JsonObject warParticipantObj = (JsonObject) jsonValue;
            WarRoomData_WarParticipant warParticipant = new WarRoomData_WarParticipant(warParticipantObj, warStatusData.getGuildFaction(), context);
            warParticipant.setHQandBS(guildData);
            partcipantHashMap.put(warParticipant.getId(), warParticipant);
        }
        //Rival
        for (JsonValue jsonValue : warStatusData.getRivalParticipants()) {
            JsonObject warParticipantObj = (JsonObject) jsonValue;
            WarRoomData_WarParticipant warParticipant = new WarRoomData_WarParticipant(warParticipantObj, warStatusData.getRivalFaction(), context);
            warParticipant.setHQandBS(rivalData);
            partcipantHashMap.put(warParticipant.getId(), warParticipant);
        }
        return partcipantHashMap;
    }

    private void populateDbTables(String warId, String playerId, String guildId, String rivalId, SWCGuildWarPartRespData warParticipantData, SWCGetWarStatusResultJson warStatusData, SWCGetPublicGuildResponseData guildData, SWCGetPublicGuildResponseData rivalData) {
        /*
        process and throw all the data into tables so the UI can moreorless just read straight from the DB
        Uses the previous models created to serve the WarRoom UI to convert the data before shoving it into the DB - just to avoid reinventing the wheel.
        These could be swapped out with more purpose built classes/methods if time.
        */
        HashMap<String, String> guildsHashMap = new HashMap<>();
        HashMap<String, WarRoomData_WarParticipant> partcipantHashMap = getParticipantsHashMap(warParticipantData, warStatusData, guildData, rivalData);

        guildsHashMap.put(guildId, guildData.getName());
        guildsHashMap.put(rivalId, rivalData.getName());


        //1. Process WarLog table:
        addWarLogEntry(warId, playerId, guildId, rivalId, warParticipantData, warStatusData, guildData, rivalData);

        //2. WarBuffBases
        for (JsonValue jsonValue : warStatusData.buffBaseJsonArray()) {
            addWarBuffBases(warId, playerId, guildsHashMap, (JsonObject) jsonValue);
        }

        //3. WarParticipant
        ArrayList<WarRoomData_WarParticipant> tmpwarParticipants = new ArrayList<>();
        for (JsonValue jsonValue : warStatusData.getGuildParticipants()) {
            boolean isRequester = false;
            JsonObject warParticipantObj = (JsonObject) jsonValue;
            WarRoomData_WarParticipant warParticipant = new WarRoomData_WarParticipant(warParticipantObj, warStatusData.getGuildFaction(), context);
            warParticipant.setHQandBS(guildData);
            int scCap = 0;
            int scCapDonated = 0;
            String scContents = "";
            DonatedTroops donatedTroops = null;
            if (warParticipant.getId().equalsIgnoreCase(playerId)) {
                isRequester = true;
                MapBuildings mapBuildings = new MapBuildings(warParticipantData.warBuildings());
                scCap = mapBuildings.getSquadBuildingCap();
                try {
                    donatedTroops = new DonatedTroops(warParticipantData.donatedTroops(), warStatusData.getGuildFaction(), mapBuildings.getSquadBuilding(), guildId, GameUnitConversionListProvider.getMasterTroopList(context), context);
                    for (TacticalCapItem tacticalCapItem : donatedTroops.getTroopsList()) {
                        addPlayerWarSC(warId, playerId, tacticalCapItem);
                    }
                    scContents = warParticipantData.donatedTroops().toString();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            ContentValues warParticipantContentValues = new ContentValues();
            warParticipantContentValues.put(DatabaseContracts.WarParticipantTable.WARID, warId);
            warParticipantContentValues.put(DatabaseContracts.WarParticipantTable.PLAYERID, warParticipant.getId());
            warParticipantContentValues.put(DatabaseContracts.WarParticipantTable.PLAYER_NAME, warParticipant.getName());
            warParticipantContentValues.put(DatabaseContracts.WarParticipantTable.GUILDID, guildId);
            warParticipantContentValues.put(DatabaseContracts.WarParticipantTable.IS_REQUESTER, isRequester);
            warParticipantContentValues.put(DatabaseContracts.WarParticipantTable.BASE_SCORE, warParticipant.getBaseScore());
            warParticipantContentValues.put(DatabaseContracts.WarParticipantTable.HQLEVEL, warParticipant.getHqLevel());
            warParticipantContentValues.put(DatabaseContracts.WarParticipantTable.TURNS, warParticipant.getTurns());
            warParticipantContentValues.put(DatabaseContracts.WarParticipantTable.VICTORY_POINTS, warParticipant.getVictoryPoints());
            warParticipantContentValues.put(DatabaseContracts.WarParticipantTable.SCORE, warParticipant.getScore());
            warParticipantContentValues.put(DatabaseContracts.WarParticipantTable.SC_CAP, scCap);
            if (donatedTroops != null) {
                warParticipantContentValues.put(DatabaseContracts.WarParticipantTable.SC_CAP_DONATED, donatedTroops.capDonated());
                warParticipantContentValues.put(DatabaseContracts.WarParticipantTable.ERRORWITHSC, donatedTroops.isErrorCalculatiingCap());
            } else {
                warParticipantContentValues.put(DatabaseContracts.WarParticipantTable.SC_CAP_DONATED, 0);
                warParticipantContentValues.put(DatabaseContracts.WarParticipantTable.ERRORWITHSC, false);

            }
            warParticipantContentValues.put(DatabaseContracts.WarParticipantTable.SQUAD_CENTER_TROOPS, scContents);

            if (warParticipant.getCurrentlyDefending() != null) {
                warParticipantContentValues.put(DatabaseContracts.WarParticipantTable.LAST_ATTACKED, warParticipant.getCurrentlyDefending().getExpiration());
                warParticipantContentValues.put(DatabaseContracts.WarParticipantTable.LAST_ATTACKED_BY_ID, warParticipant.getCurrentlyDefending().getOpponentId());
                warParticipantContentValues.put(DatabaseContracts.WarParticipantTable.LAST_ATTACKED_BY_NAME, partcipantHashMap.get(warParticipant.getCurrentlyDefending().getOpponentId()).getName());
                warParticipantContentValues.put(DatabaseContracts.WarParticipantTable.LAST_BATTLE_ID, warParticipant.getCurrentlyDefending().getBattleId());
            }
            warParticipantContentValues.put(DatabaseContracts.WarParticipantTable.FACTION, warStatusData.getGuildFaction());
            tmpwarParticipants.add(new WarRoomData_WarParticipant(warParticipantObj, warStatusData.getGuildFaction(), context));
            DBSQLiteHelper.insertReplaceData(DatabaseContracts.WarParticipantTable.TABLE_NAME, warParticipantContentValues, context);

            warParticipantContentValues.clear();
        }
        // Now do rival participants:
        for (JsonValue jsonValue : warStatusData.getRivalParticipants()) {
            JsonObject warParticipantObj = (JsonObject) jsonValue;
            WarRoomData_WarParticipant warParticipant = new WarRoomData_WarParticipant(warParticipantObj, warStatusData.getRivalFaction(), context);
            warParticipant.setHQandBS(rivalData);
            ContentValues warParticipantContentValues = new ContentValues();
            warParticipantContentValues.put(DatabaseContracts.WarParticipantTable.WARID, warId);
            warParticipantContentValues.put(DatabaseContracts.WarParticipantTable.PLAYERID, warParticipant.getId());
            warParticipantContentValues.put(DatabaseContracts.WarParticipantTable.PLAYER_NAME, warParticipant.getName());
            warParticipantContentValues.put(DatabaseContracts.WarParticipantTable.GUILDID, rivalId);
            warParticipantContentValues.put(DatabaseContracts.WarParticipantTable.IS_REQUESTER, false);
            warParticipantContentValues.put(DatabaseContracts.WarParticipantTable.BASE_SCORE, warParticipant.getBaseScore());
            warParticipantContentValues.put(DatabaseContracts.WarParticipantTable.HQLEVEL, warParticipant.getHqLevel());
            warParticipantContentValues.put(DatabaseContracts.WarParticipantTable.TURNS, warParticipant.getTurns());
            warParticipantContentValues.put(DatabaseContracts.WarParticipantTable.VICTORY_POINTS, warParticipant.getVictoryPoints());
            warParticipantContentValues.put(DatabaseContracts.WarParticipantTable.SCORE, warParticipant.getScore());
            warParticipantContentValues.put(DatabaseContracts.WarParticipantTable.SQUAD_CENTER_TROOPS, "");
            warParticipantContentValues.put(DatabaseContracts.WarParticipantTable.FACTION, warStatusData.getRivalFaction());
            if (warParticipant.getCurrentlyDefending() != null) {
                warParticipantContentValues.put(DatabaseContracts.WarParticipantTable.LAST_ATTACKED, warParticipant.getCurrentlyDefending().getExpiration());
                warParticipantContentValues.put(DatabaseContracts.WarParticipantTable.LAST_ATTACKED_BY_ID, warParticipant.getCurrentlyDefending().getOpponentId());
                warParticipantContentValues.put(DatabaseContracts.WarParticipantTable.LAST_BATTLE_ID, warParticipant.getCurrentlyDefending().getBattleId());
                warParticipantContentValues.put(DatabaseContracts.WarParticipantTable.LAST_ATTACKED_BY_NAME, partcipantHashMap.get(warParticipant.getCurrentlyDefending().getOpponentId()).getName());

            }
            tmpwarParticipants.add(new WarRoomData_WarParticipant(warParticipantObj, warStatusData.getGuildFaction(), context));
            DBSQLiteHelper.insertReplaceData(DatabaseContracts.WarParticipantTable.TABLE_NAME, warParticipantContentValues, context);
            warParticipantContentValues.clear();
        }

        for (WarRoomData_WarParticipant warParticipant : tmpwarParticipants) {
            WarRoomData_CurrentlyDefending currentlyDefending = warParticipant.getCurrentlyDefending();
            if (currentlyDefending != null) {
                ContentValues warDefenseContentValues = new ContentValues();
                warDefenseContentValues.put(DatabaseContracts.WarDefense.WARID, warId);
                warDefenseContentValues.put(DatabaseContracts.WarDefense.PLAYERID, warParticipant.getId());
                warDefenseContentValues.put(DatabaseContracts.WarDefense.PLAYER_NAME, partcipantHashMap.get(warParticipant.getId()).getName());
                warDefenseContentValues.put(DatabaseContracts.WarDefense.BATTLEID, currentlyDefending.getBattleId());
                warDefenseContentValues.put(DatabaseContracts.WarDefense.OPPONENTID, currentlyDefending.getOpponentId());
                warDefenseContentValues.put(DatabaseContracts.WarDefense.OPPONENT_NAME, partcipantHashMap.get(currentlyDefending.getOpponentId()).getName());
                warDefenseContentValues.put(DatabaseContracts.WarDefense.ATTACKERWARBUFFS, currentlyDefending.getattackingOpsCSString());
                warDefenseContentValues.put(DatabaseContracts.WarDefense.DEFENDERWARBUFFS, currentlyDefending.getDefendingOpsCSString());
//                Log.d(TAG, "currentlyDefending.getDefendingOpsCSString(): " + currentlyDefending.getDefendingOpsCSString());
                warDefenseContentValues.put(DatabaseContracts.WarDefense.DEFENSE_END, currentlyDefending.getExpiration());
                warDefenseContentValues.put(DatabaseContracts.WarDefense.VICTORY_POINTS, warParticipant.getVictoryPoints());

                DBSQLiteHelper.insertReplaceData(DatabaseContracts.WarDefense.TABLE_NAME, warDefenseContentValues, context);
                warDefenseContentValues.clear();
                //Now process the troops used:

                for (Troop troop : currentlyDefending.getHeros()) {
                    addTroopsDeployed(warId, currentlyDefending, troop, DeployableTypes.HERO);
                }

                for (Troop troop : currentlyDefending.getTroops()) {
                    addTroopsDeployed(warId, currentlyDefending, troop, DeployableTypes.TROOP);
                }

                for (Troop troop : currentlyDefending.getSpecialAttack()) {
                    addTroopsDeployed(warId, currentlyDefending, troop, DeployableTypes.AIR);
                }

                for (Troop troop : currentlyDefending.getChampion()) {
                    addTroopsDeployed(warId, currentlyDefending, troop, DeployableTypes.DROIDEKA);
                }
            }
        }
    }

    private void addWarBuffBases(String warId, String playerId, HashMap<String, String> guildsHashMap, JsonObject jsonValue) {
        JsonObject buffBaseObj = jsonValue;
        WarRoomData_Outposts warOutpostOwner = new WarRoomData_Outposts(buffBaseObj, context);
        ContentValues warBuffContentValues = new ContentValues();
        warBuffContentValues.put(DatabaseContracts.WarBuffBases.PLAYERID, playerId);
        warBuffContentValues.put(DatabaseContracts.WarBuffBases.WARID, warId);
        warBuffContentValues.put(DatabaseContracts.WarBuffBases.BUFFUID, warOutpostOwner.getBuffUid());
        warBuffContentValues.put(DatabaseContracts.WarBuffBases.OUTPOST_NAME, WarDataHelper.getOutPostFriendlyName(warOutpostOwner.getBuffUid(), context));
        warBuffContentValues.put(DatabaseContracts.WarBuffBases.OWNERID, warOutpostOwner.getOwnerId());
        warBuffContentValues.put(DatabaseContracts.WarBuffBases.SQUADNAME, guildsHashMap.get(warOutpostOwner.getOwnerId()));
        warBuffContentValues.put(DatabaseContracts.WarBuffBases.LEVEL, warOutpostOwner.getGameOpLevel());
        warBuffContentValues.put(DatabaseContracts.WarBuffBases.ADJUSTED_LEVEL, warOutpostOwner.getLevel());

        DBSQLiteHelper.insertReplaceData(DatabaseContracts.WarBuffBases.TABLE_NAME, warBuffContentValues, context);
        warBuffContentValues.clear();
    }

    private void addWarLogEntry(String warId, String playerId, String guildId, String rivalId, SWCGuildWarPartRespData warParticipantData, SWCGetWarStatusResultJson warStatusData, SWCGetPublicGuildResponseData guildData, SWCGetPublicGuildResponseData rivalData) {
        ContentValues warLogContentValues = new ContentValues();
        warLogContentValues.put(DatabaseContracts.WarLog.WARID, warId);
        warLogContentValues.put(DatabaseContracts.WarLog.PLAYERID, playerId);
        warLogContentValues.put(DatabaseContracts.WarLog.GUILDID, guildId);
        warLogContentValues.put(DatabaseContracts.WarLog.GUILDNAME, guildData.getName());
        warLogContentValues.put(DatabaseContracts.WarLog.GUILDFACTION, warStatusData.getGuildFaction());
        warLogContentValues.put(DatabaseContracts.WarLog.RIVALGUILDID, rivalId);
        warLogContentValues.put(DatabaseContracts.WarLog.RIVALGUILDNAME, rivalData.getName());
        warLogContentValues.put(DatabaseContracts.WarLog.RIVALGUILDFACTION, warStatusData.getRivalFaction());
        warLogContentValues.put(DatabaseContracts.WarLog.GUILDRESULTDATA, guildData.resultData.toString());
        warLogContentValues.put(DatabaseContracts.WarLog.RIVALRESULTDATA, rivalData.resultData.toString());
        warLogContentValues.put(DatabaseContracts.WarLog.WARSTATUSRESULTDATA, warStatusData.getResult().toString());
        warLogContentValues.put(DatabaseContracts.WarLog.WARPARTICIPANTRESULTDATA, warParticipantData.resultData.toString());

        warLogContentValues.put(DatabaseContracts.WarLog.START_TIME, warStatusData.startTime());
        warLogContentValues.put(DatabaseContracts.WarLog.PREPSTARTTIME, warStatusData.prepGraceStartTime());
        warLogContentValues.put(DatabaseContracts.WarLog.PREPENDTIME, warStatusData.prepEndTime());
        warLogContentValues.put(DatabaseContracts.WarLog.ACTIONSTARTTIME, warStatusData.actionGraceStartTime());
        warLogContentValues.put(DatabaseContracts.WarLog.ACTIONENDTIME, warStatusData.actionEndTime());
        warLogContentValues.put(DatabaseContracts.WarLog.COOLDOWNEND, warStatusData.cooldownEndTime());

        DBSQLiteHelper.insertReplaceData(DatabaseContracts.WarLog.TABLE_NAME, warLogContentValues, context);
        warLogContentValues.clear();
    }

    private void addTroopsDeployed(String warId, WarRoomData_CurrentlyDefending currentlyDefending, Troop troop, DeployableTypes deployableType) {
        ContentValues troopContentValues = new ContentValues();
        troopContentValues.put(DatabaseContracts.WarBattleDeployable.BATTLEID, currentlyDefending.getBattleId());
        troopContentValues.put(DatabaseContracts.WarBattleDeployable.WARID, warId);
        troopContentValues.put(DatabaseContracts.WarBattleDeployable.DEPLOYMENT_TYPE, deployableType.toString());
        troopContentValues.put(DatabaseContracts.WarBattleDeployable.DEPLOYABLE, troop.descriptionAndLevel());
        troopContentValues.put(DatabaseContracts.WarBattleDeployable.DEPLOYMENT_COUNT, troop.getNumTroops());

        DBSQLiteHelper.insertReplaceData(DatabaseContracts.WarBattleDeployable.TABLE_NAME, troopContentValues, context);
        troopContentValues.clear();
    }

    private void addPlayerWarSC(String warId, String playerId, TacticalCapItem tacticalCapItem) {
        ContentValues warSCContentValues = new ContentValues();
        warSCContentValues.put(DatabaseContracts.WarSCContents.WARID, warId);
        warSCContentValues.put(DatabaseContracts.WarSCContents.PLAYERID, playerId);
        warSCContentValues.put(DatabaseContracts.WarSCContents.UINAME, tacticalCapItem.getItemName());
        warSCContentValues.put(DatabaseContracts.WarSCContents.QTY, tacticalCapItem.getItemQty());
        warSCContentValues.put(DatabaseContracts.WarSCContents.CAP, tacticalCapItem.getItemCapacity());
        warSCContentValues.put(DatabaseContracts.WarSCContents.LEVEL, tacticalCapItem.getItemLevel());
        warSCContentValues.put(DatabaseContracts.WarSCContents.DONATED_BY_NAME, tacticalCapItem.getItemDetail().toString());
        DBSQLiteHelper.insertReplaceData(DatabaseContracts.WarSCContents.TABLE_NAME, warSCContentValues, context);
    }


}
