package com.swctools.common.models.player_models;

import android.content.Context;
import android.database.Cursor;

import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;

import java.util.HashMap;
import java.util.Map;

import javax.json.JsonObject;
import javax.json.JsonValue;

public class DonatedTroop {
    private static final String TAG = "DonatedTroop";
    private String troopGameName;
    private String troopUIName;
    private int troopLevel;
    private int troopCap;
    private int troopTotalCap;
    private int troopDonatedTotal;
    private String faction;


    private HashMap<String, Integer> donatedByPlayerId;

    public DonatedTroop(Map.Entry<String, JsonValue> value, String faction, Context context) {
        troopGameName = value.getKey();
        this.faction = faction;

        troopDonatedTotal = 0;
        JsonObject donatedByObj = (JsonObject) value.getValue();
        donatedByPlayerId = new HashMap<>();
        for (Map.Entry<String, JsonValue> donatedByEntry : donatedByObj.entrySet()) {
            int troopDonated = Integer.parseInt(donatedByEntry.getValue().toString());
            donatedByPlayerId.put(donatedByEntry.getKey(), troopDonated);
            troopDonatedTotal = troopDonatedTotal + troopDonated;
            setTroopInfo(context);
        }
        troopTotalCap = troopDonatedTotal * troopCap;
    }


    private void setTroopInfo(Context context) {


        Cursor cursor = null;
        String[] projection = {
                DatabaseContracts.TroopTable._ID,
                DatabaseContracts.TroopTable.GAME_NAME,
                DatabaseContracts.TroopTable.UI_NAME,
                DatabaseContracts.TroopTable.FACTION,
                DatabaseContracts.TroopTable.CAPACITY};

        String selection = DatabaseContracts.TroopTable.FACTION + " like ?";

        String[] selectionArgs = {faction};
        try {

            cursor = DBSQLiteHelper.queryDB(DatabaseContracts.TroopTable.TABLE_NAME, projection, selection, selectionArgs, context);

            while (cursor.moveToNext()) {

                String dbGameNameStr = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.TroopTable.GAME_NAME));

                if (troopGameName.indexOf(dbGameNameStr) != -1) {//then we found something!!!!
                    troopUIName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.TroopTable.UI_NAME));
                    troopCap = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.TroopTable.CAPACITY));
                    setTroopLevel(troopGameName, dbGameNameStr);
                    break;
                }
            }
            cursor.close();
        } catch (Exception e) {
            troopUIName = troopGameName;
            troopCap = 0;
        } finally {
            if (cursor != null) {
                cursor.close();
            }

        }
    }


    private void setTroopLevel(String gameName, String rawName) {
        int gameName_Len = gameName.length();
        int uiName_len = rawName.length();
        try {
            troopLevel = Integer.parseInt(gameName.substring((gameName.indexOf(rawName) + uiName_len), gameName_Len));
        } catch (Exception e) {
            troopLevel = 0;
        }


    }
}
