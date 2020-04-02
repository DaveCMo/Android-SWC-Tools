package com.swctools.common.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;
import com.swctools.layouts.LayoutHelper;
import com.swctools.activity_modules.layout_manager.models.LayoutManagerListProvider;
import com.swctools.layouts.models.PlayerTopSelectedModel;
import com.swctools.layouts.models.FavouriteLayoutItem;
import com.swctools.layouts.models.LayoutRecord;
import com.swctools.util.MethodResult;

import java.util.ArrayList;

public class PlayerFavouriteLayoutHelper {
    private static final String TAG = "LAYOUTTOPHELPER";

    public static ArrayList<FavouriteLayoutItem> getPlayerTopLayouts(String playerId, Context context) {
        Log.d(TAG, "getPlayerTopLayouts: "+ playerId);
        ArrayList<FavouriteLayoutItem> layoutRecords = new ArrayList<>();
        String whereClause = DatabaseContracts.LayoutTop.PLAYER_ID + " = ?";
        String[] whereArg = {playerId};

        Cursor cursor = DBSQLiteHelper.queryDB(DatabaseContracts.LayoutTop.TABLE_NAME, null, whereClause, whereArg, context);
        try {
            while (cursor.moveToNext()) {
                int favId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutTop.COLUMN_ID));
                int layoutId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutTop.LAYOUT_ID));
                LayoutRecord layoutRecord = LayoutManagerListProvider.getLayoutRecord(layoutId, context);
                Log.d(TAG, "getPlayerTopLayouts: " + favId);
                Log.d(TAG, "getPlayerTopLayouts: " + layoutId);
                Log.d(TAG, "getPlayerTopLayouts: " + layoutId);
                Log.d(TAG, "getPlayerTopLayouts: ----->" + layoutRecord.getDefaultLayoutVersion());
                Log.d(TAG, "getPlayerTopLayouts: ----->" + layoutRecord.getLayoutName());
                Log.d(TAG, "getPlayerTopLayouts: ----->" +  layoutRecord.getImageURIStr());
                Log.d(TAG, "getPlayerTopLayouts: ----->" +  FavouriteLayoutItem.PLAYERFAV);
                layoutRecords.add(new FavouriteLayoutItem(favId, layoutId, layoutRecord.getDefaultLayoutVersion(), layoutRecord.getLayoutName(), layoutRecord.getImageURIStr(), FavouriteLayoutItem.PLAYERFAV));
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return layoutRecords;
    }


    public static ArrayList<PlayerTopSelectedModel> getPlayersforLayout(int layoutId, Context context) {
        //First get all players with this layout selected:
        ArrayList<String> playerSelectedArrayList = new ArrayList<>();

        String whereClause = DatabaseContracts.LayoutTop.LAYOUT_ID + " = ?";
        String[] whereArg = {String.valueOf(layoutId)};

        Cursor cursor = DBSQLiteHelper.queryDB(DatabaseContracts.LayoutTop.TABLE_NAME, null, whereClause, whereArg, context);
        try {
            while (cursor.moveToNext()) {

                String playerId = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutTop.PLAYER_ID));
                playerSelectedArrayList.add(playerId);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        //now get all players
        ArrayList<PlayerTopSelectedModel> playerTopSelectedModels = new ArrayList<>();
        Cursor cursor1 = DBSQLiteHelper.queryDB(DatabaseContracts.PlayersTable.TABLE_NAME, context);
        try {
            while (cursor1.moveToNext()) {
                String playerId = cursor1.getString(cursor1.getColumnIndexOrThrow(DatabaseContracts.PlayersTable.PLAYERID));
                String playerName = cursor1.getString(cursor1.getColumnIndexOrThrow(DatabaseContracts.PlayersTable.PLAYERNAME));
                String playerFaction = cursor1.getString(cursor1.getColumnIndexOrThrow(DatabaseContracts.PlayersTable.FACTION));
                PlayerTopSelectedModel playerTopSelectedModel = new PlayerTopSelectedModel(playerId, playerName, playerFaction);
                for (String s : playerSelectedArrayList) {
                    if (s.equalsIgnoreCase(playerTopSelectedModel.playerId)) {
                        playerTopSelectedModel.setSelected(true);
                        break;
                    }
                }

                playerTopSelectedModels.add(playerTopSelectedModel);
            }
        } finally {
            if (cursor1 != null) {
                cursor1.close();
            }
        }


        return playerTopSelectedModels;
    }


    public static MethodResult addTopLayout(int layoutId, String playerId, Context context) {
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseContracts.LayoutTop.LAYOUT_ID, layoutId);
            contentValues.put(DatabaseContracts.LayoutTop.PLAYER_ID, playerId);
            long rowID = DBSQLiteHelper.insertData(DatabaseContracts.LayoutTop.TABLE_NAME, contentValues, context);
            if (rowID > 0) {
                LayoutHelper.markAsFavourite(layoutId, context);
                return new MethodResult(true, "Updated!");
            } else {
                return new MethodResult(false, "Failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new MethodResult(false, e);
        }
    }

    public static MethodResult removeTopLayout(int layoutId, String playerId, Context context) {

        try {
            String whereClause = DatabaseContracts.LayoutTop.LAYOUT_ID + " = ? AND " + DatabaseContracts.LayoutTop.PLAYER_ID + " = ?";
            String[] whereArgs = {String.valueOf(layoutId), playerId};

            DBSQLiteHelper.deleteDbRows(DatabaseContracts.LayoutTop.TABLE_NAME, whereClause, whereArgs, context);
            return new MethodResult(true, "");
        } catch (Exception e) {
            return new MethodResult(false, "");
        }
    }

    public static int countPlayerFavouritesForLayout(int layoutId, Context context) {

        String whereClause = DatabaseContracts.LayoutTop.LAYOUT_ID + " = ?";
        String[] whereArgs = {String.valueOf(layoutId)};

        Cursor cursor = DBSQLiteHelper.queryDB(DatabaseContracts.LayoutTop.TABLE_NAME, null, whereClause, whereArgs, context);
        try {
            int no = cursor.getCount();
            return no;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }


    }


}
