package com.swctools.activity_modules.main.models;

import android.content.Context;
import android.database.Cursor;

import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;
import com.swctools.common.helpers.PlayerFavouriteLayoutHelper;

import java.util.ArrayList;
import java.util.List;

public class PlayerDAOList {
    private static final String TAG = "PlayerDAOList";

    public static List<PlayerDAO_WithLayouts> getPlayerDAOListWithLayout(Context context) {
        List<PlayerDAO_WithLayouts> playerList = new ArrayList<PlayerDAO_WithLayouts>();


        Cursor cursor = null;

        try {

            cursor = DBSQLiteHelper.queryDB(DatabaseContracts.PlayersTable.TABLE_NAME, context);

            while (cursor.moveToNext()) {
                int db_rowId = cursor.getInt(cursor.getColumnIndex(DatabaseContracts.PlayersTable.COLUMN_ID));
                String playerId = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.PlayersTable.PLAYERID));
                String playerSecret = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.PlayersTable.PLAYERSECRET));
                String playerName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.PlayersTable.PLAYERNAME));
                String playerFaction = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.PlayersTable.FACTION));
                String playerGuild = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.PlayersTable.PLAYERGUILD));
                String notifications = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.PlayersTable.NOTIFICATIONS));
                int refresh_interval = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.PlayersTable.REFRESH_INT));
                String deviceId = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.PlayersTable.DEVICE_ID));
                String card_default = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.PlayersTable.DEFAULT_FAV));
                int card_expanded = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.PlayersTable.CARD_EXPANDED));
                playerList.add(new PlayerDAO_WithLayouts(db_rowId, playerId, playerSecret, playerName, playerFaction, playerGuild, notifications, refresh_interval, deviceId, card_default, card_expanded));
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        for (PlayerDAO_WithLayouts playerDAO_withLayouts : playerList) {
            playerDAO_withLayouts.setLastUsedLayoutItemList(FavouriteLayoutListProvider.autoFavouriteLayoutList(playerDAO_withLayouts.getPlayerId(), FavouriteLayoutListProvider.AutoType.LAST_USED, 5, context));
            playerDAO_withLayouts.setMostUsedLayoutItemList(FavouriteLayoutListProvider.autoFavouriteLayoutList(playerDAO_withLayouts.getPlayerId(), FavouriteLayoutListProvider.AutoType.MOST_USED, 5, context));
            playerDAO_withLayouts.setTopLayoutItemList(PlayerFavouriteLayoutHelper.getPlayerTopLayouts(playerDAO_withLayouts.getPlayerId(), context));
        }
        return playerList;
    }

    public static List<PlayerDAO> getPlayerDAOList(Context context) {
        List<PlayerDAO> playerList = new ArrayList<PlayerDAO>();


        Cursor cursor = null;

        try {
            cursor = DBSQLiteHelper.queryDB(DatabaseContracts.PlayersTable.TABLE_NAME, context);
            while (cursor.moveToNext()) {
                int db_rowId = cursor.getInt(cursor.getColumnIndex(DatabaseContracts.PlayersTable.COLUMN_ID));
                String playerId = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.PlayersTable.PLAYERID));
                String playerSecret = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.PlayersTable.PLAYERSECRET));
                String playerName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.PlayersTable.PLAYERNAME));
                String playerFaction = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.PlayersTable.FACTION));
                String playerGuild = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.PlayersTable.PLAYERGUILD));
                String notifications = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.PlayersTable.NOTIFICATIONS));
                int refresh_interval = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.PlayersTable.REFRESH_INT));
                String deviceId = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.PlayersTable.DEVICE_ID));
                String card_default = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.PlayersTable.DEFAULT_FAV));
                int card_expanded = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.PlayersTable.CARD_EXPANDED));
                playerList.add(new PlayerDAO(db_rowId, playerId, playerSecret, playerName, playerFaction, playerGuild, notifications, refresh_interval, deviceId, card_default, card_expanded));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return playerList;
    }
}
