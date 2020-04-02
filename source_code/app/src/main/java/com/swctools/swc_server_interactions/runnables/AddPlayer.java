package com.swctools.swc_server_interactions.runnables;

import android.content.ContentValues;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;
import com.swctools.common.models.player_models.GuildModel;
import com.swctools.swc_server_interactions.PlayerLoginSession;
import com.swctools.swc_server_interactions.results.SWCLoginResponseData;
import com.swctools.util.MethodResult;
import com.swctools.util.StringUtil;
import com.swctools.util.Utils;

import java.util.UUID;

import javax.json.JsonObject;

public class AddPlayer extends SWC_Runnable_Base implements Runnable {
    private String playerId;
    private String playerSecret;
    private String deviceId;
    private Context context;
    private PlayerLoginSession playerLoginSession;


    public AddPlayer(String playerId, String playerSecret, Context context, Handler swc_interaction_handler) {
        super(swc_interaction_handler);
        this.playerId = playerId;
        this.playerSecret = playerSecret;
        this.deviceId = UUID.randomUUID().toString();
        this.context = context;
    }


    @Override
    public void run() {
        MethodResult methodResult = addPlayer();
        Message msg = Message.obtain();

        msg.obj = methodResult;
        swc_interaction_handler.sendMessage(msg);
    }

    private MethodResult addPlayer() {

        if (!Utils.isConnected(context)) {
            return new MethodResult(false, NO_INTERNET);
        }
        MethodResult playerValid = Utils.validatePlayer(playerId, playerSecret);

        if (!playerValid.success) {
            return playerValid;
        }


        if (!Utils.playerIdDoesNotExist(playerId, context)) {
            return new MethodResult(false, "Player already added!!");
        }

        sendProgressUpdateToUI("Attempting login....");

        playerLoginSession = new PlayerLoginSession(playerId, playerSecret, deviceId, true);
        playerLoginSession.login(true, context);

        if (!playerLoginSession.isLoggedIn()) {
            return playerLoginSession.getLoginResult();
        }
        JsonObject loginData;
        SWCLoginResponseData jsonLogin;
        try {
            loginData = StringUtil.stringToJsonObject(playerLoginSession.getLoginResult().getMessage());
            jsonLogin = new SWCLoginResponseData(loginData);
            String playerName = jsonLogin.name();
            String playerFaction = jsonLogin.mplayerModel().faction();

            GuildModel guildModel = new GuildModel(jsonLogin.mplayerModel().guildInfo().toString());
            String playerGuild = guildModel.getGuildName();


            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseContracts.PlayersTable.PLAYERID, playerId);
            contentValues.put(DatabaseContracts.PlayersTable.PLAYERSECRET, playerSecret);
            contentValues.put(DatabaseContracts.PlayersTable.PLAYERNAME, playerName);
            contentValues.put(DatabaseContracts.PlayersTable.FACTION, playerFaction);
            contentValues.put(DatabaseContracts.PlayersTable.PLAYERGUILD, playerGuild);
            contentValues.put(DatabaseContracts.PlayersTable.DEVICE_ID, deviceId);

            long newRowId = DBSQLiteHelper.insertData(DatabaseContracts.PlayersTable.TABLE_NAME, contentValues, context);

            return new MethodResult(true, "");
        } catch (Exception e) {
            e.printStackTrace();
            return new MethodResult(false, e);
        }
    }
}
