package com.swctools.swc_server_interactions.runnables;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;

import com.swctools.config.AppConfig;
import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;
import com.swctools.common.enums.PlayerBase;
import com.swctools.layouts.LayoutHelper;
import com.swctools.layouts.LayoutMatcher;
import com.swctools.activity_modules.main.models.PlayerDAO;
import com.swctools.common.models.player_models.GuildMember;
import com.swctools.common.models.player_models.GuildModel;
import com.swctools.common.models.player_models.MapBuildings;
import com.swctools.swc_server_interactions.PlayerLoginSession;
import com.swctools.swc_server_interactions.SWCMessage;
import com.swctools.swc_server_interactions.threads.SWC_Interaction_Thread;
import com.swctools.swc_server_interactions.results.SWCDefaultResponse;
import com.swctools.swc_server_interactions.results.SWCDefaultResultData;
import com.swctools.swc_server_interactions.results.SWCGetPublicGuildResponseData;
import com.swctools.swc_server_interactions.results.SWCVisitResult;
import com.swctools.swc_server_interactions.swc_commands.Cmd_ChangeLayout;
import com.swctools.util.MethodResult;
import com.swctools.util.StringUtil;
import com.swctools.util.Utils;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

public class ApplyPlayerLayout extends SWC_Runnable_Base implements Runnable {
    private static final String TAG = "ApplyPlayerLayout";
    private String playerId;
    private int newLayoutId;
    private int newLayoutVersion;
    private PlayerBase playerBase;
    private Context context;

    public ApplyPlayerLayout(String playerId, int newLayoutId, int newLayoutVersion, PlayerBase playerBase, Context context, Handler swc_interaction_handler) {
        super(swc_interaction_handler);
        this.playerId = playerId;
        this.newLayoutId = newLayoutId;
        this.newLayoutVersion = newLayoutVersion;
        this.playerBase = playerBase;
        this.context = context;
        this.swc_interaction_handler = swc_interaction_handler;
    }

    @Override
    public void run() {
        MethodResult methodResult = applyPlayerLayout(playerId, newLayoutId, newLayoutVersion, playerBase);
        Message msg = Message.obtain();
        msg.what = SWC_Interaction_Thread.SWC_Interaction_Handler.APPLY_LAYOUT;
        msg.obj = methodResult;
        swc_interaction_handler.sendMessage(msg);

    }

    private MethodResult applyPlayerLayout(String playerId, int newLayoutId, int newLayoutVersion, PlayerBase playerBase) {
        /*Steps:
        0. get stuff, layouts etc

            1. Get current Layout
            2. Match buildings
            3. Send first command
            4. loop through the extra commands to update layout
         */
        if (!Utils.isConnected(context)) {
            return new MethodResult(false, NO_INTERNET);
        } else {
            AppConfig appConfig = new AppConfig(context);


            //first visit the player
            PlayerLoginSession visitorLoginSession = new PlayerLoginSession(appConfig.getVisitor_playerId(), appConfig.getVisitor_playersecret(), appConfig.getVisitorDeviceId(), false);
            visitorLoginSession.login(true, context);
            if (!visitorLoginSession.isLoggedIn()) {
                return visitorLoginSession.getLoginResult();
            }
            if (playerBase == PlayerBase.WAR) {
                if (!isInWar(playerId, context, visitorLoginSession).success) {
                    return new MethodResult(false, "You do not appear to be in a war...");
                }
            }
            sendProgressUpdateToUI("Fetching the list of all your buildings...");
            try {
                SWCVisitResult disneyVisitResult = visitPlayer(playerId, context, visitorLoginSession);
                if (!disneyVisitResult.isSuccess()) {
                    return new MethodResult(false, "Server returned when visiting player: " + disneyVisitResult.getStatusCodeAndName());
                }
                sendProgressUpdateToUI("Comparing your buildings to the selected layout...");
                String playerFaction = disneyVisitResult.mplayerModel().faction();
                String whereStr = DatabaseContracts.LayoutVersions.LAYOUT_ID + "= ? AND " + DatabaseContracts.LayoutVersions.LAYOUT_VERSION + " = ?";
                String[] whereArgs = {String.valueOf(newLayoutId), String.valueOf(newLayoutVersion)};
                Cursor cursor = DBSQLiteHelper.queryDB(DatabaseContracts.LayoutVersions.TABLE_NAME, null, whereStr, whereArgs, context);
                String newLayoutString;
                newLayoutString = "";
                try {
                    while (cursor.moveToNext()) {
                        newLayoutString = cursor.getString(cursor.getColumnIndex(DatabaseContracts.LayoutVersions.LAYOUT_JSON));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return new MethodResult(false, e);
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }

                }
                JsonReader jReader = Json.createReader(new StringReader(newLayoutString));
                JsonArray arraynewLayout = jReader.readArray();
                MapBuildings newLayout = new MapBuildings(arraynewLayout);
                MapBuildings curLayout = new MapBuildings(disneyVisitResult.mplayerModel().map());
                MethodResult layoutMatcherResult = LayoutMatcher.matchedLayout(newLayoutString, disneyVisitResult.mplayerModel().map().getJsonArray("buildings").toString(), playerFaction, context);
                if (!layoutMatcherResult.success) {
                    return layoutMatcherResult;
                }
                MapBuildings matchedLayout = (MapBuildings) layoutMatcherResult.getResultObject();

                PlayerDAO playerDAO = new PlayerDAO(playerId, context);
                PlayerLoginSession playerLoginSession = new PlayerLoginSession(playerDAO.getPlayerId(), playerDAO.getPlayerSecret(), playerDAO.getDeviceId(), true);
                playerLoginSession.login(true, context);
                if (!playerLoginSession.isLoggedIn()) {
                    return playerLoginSession.getLoginResult();
                }
                sendProgressUpdateToUI("Logging into your player account...");
                Cmd_ChangeLayout cmd_changeLayout = new Cmd_ChangeLayout(playerLoginSession.newRequestId(), playerId, matchedLayout.layoutPositions(), playerBase);
                SWCDefaultResponse swcDefaultResponse = new SWCMessage(playerLoginSession.getAuthKey(), true, playerLoginSession.getLoginTime(), cmd_changeLayout, "applyPlayerLayout", context).getSwcMessageResponse();// new SWCDefaultResponse(getRanksResult.getMessage());
                SWCDefaultResultData layoutUpdateResultData = new SWCDefaultResultData(swcDefaultResponse.getResponseDataByRequestId(playerLoginSession.getRequestId()));
                if (!layoutUpdateResultData.isSuccess()) {
                    String junkMessage = "";
                    if (curLayout.junkCount() > 0) {
                        junkMessage = "\nYOU HAVE " + curLayout.junkCount() + " PIECES OF JUNK ON YOUR BASE!";
                    }
                    return new MethodResult(false, "Server returned code: " + String.valueOf(layoutUpdateResultData.status), layoutUpdateResultData.getStatusCodeAndName(), junkMessage, layoutMatcherResult.getMessage());
                }
                LayoutHelper.logLayoutUsed(newLayoutId, newLayoutVersion, playerId, playerBase.toString(), context);
                return new MethodResult(true, layoutMatcherResult.getMessage(), "Layout updated!");
            } catch (Exception e) {
                e.printStackTrace();
                return new MethodResult(false, e);
            }
        }
    }


    public MethodResult isInWar(String playerId, Context context, PlayerLoginSession playerLoginSession) {
        //


        try {
            SWCVisitResult disneyVisitResult = visitPlayer(playerId, context, playerLoginSession);
            if (!disneyVisitResult.isSuccess()) {
                return new MethodResult(false, disneyVisitResult.getStatusCodeAndName());
            }

            String playerFaction = disneyVisitResult.mplayerModel().faction();
            GuildModel guildModel = new GuildModel(disneyVisitResult.mplayerModel().guildInfo().toString());
            String guildId = guildModel.getGuildId();
            if (!StringUtil.isStringNotNull(guildId)) {
                return new MethodResult(false, "Not in a squad!");
                //get squad data:
            }
            SWCGetPublicGuildResponseData guildResponseData = getSwcGetPublicGuildResponseData(context, playerLoginSession, guildId);
            JsonArray members = guildResponseData.getMembers();
            for (JsonValue jsonValue : members) {
                JsonObject obj = (JsonObject) jsonValue;
                GuildMember guildMember = new GuildMember(obj);

                if (guildMember.playerId.equalsIgnoreCase(playerId)) {
                    if (guildMember.warParty == 1) {
                        return new MethodResult(true, "");
                    } else {
                        return new MethodResult(false, "You don't appear to be in the war party. Are you in a war?!");
                    }
                }
            }
            return new MethodResult(false, "Unknown issue with finding player in squad");


        } catch (Exception e) {
            e.printStackTrace();
            return new MethodResult(false, e);
        }


    }


}

//Now do the barracks/fax thing:
//                            for (Building building : matchedLayout.getBuildings()) {
//                                if (building.getBuildingProperties().getGenericName().equalsIgnoreCase(BuildingGeneric.BARRACKS.toString())
//                                        || building.getBuildingProperties().getGenericName().equalsIgnoreCase(BuildingGeneric.FACTORY.toString())) {
//                                    requestId = getRequestId();
//                                    Cmd_ChangeLayout changeFaxLayout = new Cmd_ChangeLayout(requestId, playerId, building.asLayoutObject(), playerBase);
//                                    SWCDefaultResponse changeFaxResponse = new SWCMessage(authKey, true, loginTime, changeFaxLayout, "applyPlayerLayout - RaxFAx", mContext).getSwcMessageResponse();
//                                    SWCDefaultResultData changeFaxResponseResultData = new SWCDefaultResultData(changeFaxResponse.getResponseDataByRequestId(requestId));
//                                    if (!changeFaxResponseResultData.isSuccess()) {
//                                        return new MethodResult(false, "Server returned: " + changeFaxResponseResultData.status_code);
//                                    }
//                                }
//                            }
