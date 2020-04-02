package com.swctools.swc_server_interactions.runnables;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;

import com.swctools.config.AppConfig;
import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;
import com.swctools.activity_modules.main.models.PlayerDAO;
import com.swctools.activity_modules.player.models.TwoTextItemData;
import com.swctools.activity_modules.player.models.Conflict_Data_Model;
import com.swctools.swc_server_interactions.PlayerLoginSession;
import com.swctools.swc_server_interactions.SWCMessage;
import com.swctools.swc_server_interactions.results.SWCDefaultResponse;
import com.swctools.swc_server_interactions.results.SWCGetTournamentLeaderRanksResult;
import com.swctools.swc_server_interactions.results.SWCLoginResponseData;
import com.swctools.swc_server_interactions.swc_commands.Cmd_TournamentGetRank;
import com.swctools.util.MethodResult;
import com.swctools.util.StringUtil;
import com.swctools.util.Utils;

import java.util.ArrayList;

import javax.json.JsonObject;

public class GetTournamentData extends SWC_Runnable_Base implements Runnable {

    private String playerId;
    private Context context;
    private PlayerLoginSession playerLoginSession;

    public GetTournamentData(String playerId, Context context, Handler swc_interaction_handler) {
        super(swc_interaction_handler);
        this.playerId = playerId;
        this.context = context;
    }


    @Override
    public void run() {
        MethodResult methodResult = getTournamentData();
        Message msg = Message.obtain();

        msg.obj = methodResult;
        swc_interaction_handler.sendMessage(msg);
    }

    private MethodResult getTournamentData() {
        if (!Utils.isConnected(context)) {
            return new MethodResult(false, NO_INTERNET);
        }
        sendProgressUpdateToUI("Compiling list of planets...");
        AppConfig appConfig = new AppConfig(context);
        ArrayList<Conflict_Data_Model> conflict_data_models = new ArrayList<>();
        //Get list of planets
        ArrayList<TwoTextItemData> planetList = new ArrayList<>();
        String[] columns = {DatabaseContracts.Planets.GAME_NAME, DatabaseContracts.Planets.UI_NAME};
        Cursor cursor = DBSQLiteHelper.queryDB(DatabaseContracts.Planets.TABLE_NAME, columns, context);
        try {
            while (cursor.moveToNext()) {
                planetList.add(new TwoTextItemData(
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.Planets.GAME_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.Planets.UI_NAME))));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        sendProgressUpdateToUI("Logging in....");
        PlayerDAO playerDAO = new PlayerDAO(playerId, context);
        playerLoginSession = new PlayerLoginSession(playerId, playerDAO.getPlayerSecret(), playerDAO.getDeviceId(), true);
        playerLoginSession.login(true, context);
        if (!playerLoginSession.isLoggedIn()) {
            return playerLoginSession.getLoginResult();
        }
        JsonObject loginData;
        SWCLoginResponseData disneyLoginResponseData;
        try {
            loginData = StringUtil.stringToJsonObject(playerLoginSession.getLoginResult().getMessage());
            disneyLoginResponseData = new SWCLoginResponseData(loginData);

            for (int planetItr = 0; planetItr < planetList.size(); planetItr++) {
                try {
                    if (disneyLoginResponseData.mplayerModel().unlockedPlanets().toString().contains(planetList.get(planetItr).text1)) {
                        sendProgressUpdateToUI("Getting conflict data for " + planetList.get(planetItr).text2);

                        SWCDefaultResponse swcDefaultResponse = new SWCMessage(playerLoginSession.getAuthKey(), true, playerLoginSession.getLoginTime(), new Cmd_TournamentGetRank(playerLoginSession.newRequestId(), playerId, planetList.get(planetItr).text1), "getTournamentData", context).getSwcMessageResponse();// new SWCDefaultResponse(getRanksResult.getMessage());
                        SWCGetTournamentLeaderRanksResult conflictRanksResult = new SWCGetTournamentLeaderRanksResult(swcDefaultResponse.getResponseDataByRequestId(playerLoginSession.getRequestId()));
                        try {
                            String[] conflictDataColumns = {
                                    DatabaseContracts.ConflictData.COLUMN_ID,
                                    DatabaseContracts.ConflictData.UID,
                                    DatabaseContracts.ConflictData.ENDDATE,
                                    DatabaseContracts.ConflictData.STARTDATE
                            };
                            String conflictUID = conflictRanksResult.getConflictUID();
                            String whereClause = DatabaseContracts.ConflictData.UID + " = ? ";
                            String[] whereArgs = {conflictUID};
                            String endDate = "";
                            String startDate = "";
                            Cursor appConflictData = DBSQLiteHelper.queryDB(DatabaseContracts.ConflictData.TABLE_NAME, conflictDataColumns, whereClause, whereArgs, context);

                            while (appConflictData.moveToNext()) {
                                endDate = appConflictData.getString(appConflictData.getColumnIndexOrThrow(DatabaseContracts.ConflictData.ENDDATE));
                                startDate = appConflictData.getString(appConflictData.getColumnIndexOrThrow(DatabaseContracts.ConflictData.STARTDATE));
                            }

                            conflict_data_models.add(
                                    new Conflict_Data_Model(
                                            conflictRanksResult.getPercentile(),
                                            conflictRanksResult.getRank(),
                                            conflictRanksResult.getValue(),
                                            conflictRanksResult.attacksWon(),
                                            conflictRanksResult.attacksLost(),
                                            conflictRanksResult.defensesWon(),
                                            conflictRanksResult.defensesLost(),
                                            planetList.get(planetItr).text2, startDate, endDate));
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {

                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new MethodResult(true, conflict_data_models, "");

    }

}
