package com.swctools.activity_modules.main.models;

import android.content.Context;
import android.database.Cursor;

import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;

public class PlayerDAO {
    private static final String TAG = "PlayerDAO";
    private int db_rowId;
    private String playerId;
    private String playerSecret;
    private String playerName;
    private String playerFaction;
    private String playerGuild;
    private String notifications;
    private int refresh_interval;
    private String deviceId;
    private String card_default;
    private int card_expanded = 0;

    public PlayerDAO(int db_rowId, String playerId, String playerSecret, String playerName, String playerFaction, String playerGuild, String notifications, int refresh_interval, String deviceId, String card_default, int card_expanded) {
        this.db_rowId = db_rowId;
        this.playerId = playerId;
        this.playerSecret = playerSecret;
        this.playerName = playerName;
        this.playerFaction = playerFaction;
        this.playerGuild = playerGuild;
        this.notifications = notifications;
        this.refresh_interval = refresh_interval;
        this.deviceId = deviceId;
        this.card_default = card_default;
        this.card_expanded = card_expanded;
    }

    public PlayerDAO(String playerId, Context context) {
        String whereStr = DatabaseContracts.PlayersTable.PLAYERID + " = ?";
        String[] whereArg = {playerId};
        Cursor cursor = DBSQLiteHelper.queryDB(DatabaseContracts.PlayersTable.TABLE_NAME, null, whereStr, whereArg, context);

        try {
            while (cursor.moveToNext()) {
                this.playerName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.PlayersTable.PLAYERNAME));
                this.playerFaction = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.PlayersTable.FACTION));
                this.playerGuild = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.PlayersTable.PLAYERGUILD));
                this.playerId = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.PlayersTable.PLAYERID));
                this.db_rowId = cursor.getInt(cursor.getColumnIndex(DatabaseContracts.PlayersTable.COLUMN_ID));
                this.playerSecret = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.PlayersTable.PLAYERSECRET));
                this.deviceId = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.PlayersTable.DEVICE_ID));
                this.card_expanded = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.PlayersTable.CARD_EXPANDED));
                this.notifications = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.PlayersTable.NOTIFICATIONS));
                this.refresh_interval = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.PlayersTable.REFRESH_INT));
                this.card_default = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.PlayersTable.DEFAULT_FAV));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }




    }

    public void setNotifications(String s) {
        this.notifications = s;
    }

    public int getDb_rowId() {
        return this.db_rowId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerFaction() {
        return playerFaction;
    }

    public void setPlayerFaction(String playerFaction) {
        this.playerFaction = playerFaction;
    }

    public String getPlayerGuild() {
        return playerGuild;
    }

    public void setPlayerGuild(String playerGuild) {
        this.playerGuild = playerGuild;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }


    public String getNotifications() {
        return notifications;
    }

    public String getCard_default() {
        return card_default;
    }

    public String getPlayerSecret() {
        return playerSecret;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public int getCard_expanded() {
        return card_expanded;
    }
}
