package com.swctools.common.model_list_providers;

import android.content.Context;
import android.database.Cursor;

import com.swctools.activity_modules.player.models.BuildingDAO;
import com.swctools.common.models.player_models.ArmouryEquipment;
import com.swctools.common.models.player_models.Troop;
import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;

import java.util.HashMap;

public class GameUnitConversionListProvider {
    private static final String TAG = "GameUCLisProvider";

    public static HashMap<String, Troop> getMasterTroopList(Context context) {
        HashMap<String, Troop> hashMap = new HashMap<>();
        Cursor cursor = DBSQLiteHelper.queryDB(DatabaseContracts.TroopTable.TABLE_NAME, context);
        try {
            while (cursor.moveToNext()) {
                String gname = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.TroopTable.GAME_NAME));
                String uiname = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.TroopTable.UI_NAME));
                String faction = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.TroopTable.FACTION));
                int cap = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.TroopTable.CAPACITY));
                int level = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.TroopTable.LEVEL));
                hashMap.put(gname, new Troop(gname, uiname, faction, cap, level));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return hashMap;
    }

    public static HashMap<String, ArmouryEquipment> getMasterArmourList(Context context) {
        HashMap<String, ArmouryEquipment> hashMap = new HashMap<>();
        Cursor cursor = DBSQLiteHelper.queryDB(DatabaseContracts.EquipmentTable.TABLE_NAME, context);
        try {
            while (cursor.moveToNext()) {
                String gname = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.EquipmentTable.GAME_NAME));
                String uiname = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.EquipmentTable.UI_NAME));
                String faction = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.EquipmentTable.FACTION));
                int cap = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.EquipmentTable.CAPACITY));
                int level = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.EquipmentTable.LEVEL));
                String availableOn = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.EquipmentTable.AVAILABLE_ON));
                String type = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.EquipmentTable.TYPE));
                hashMap.put(gname, new ArmouryEquipment(gname, uiname, faction, cap, level, availableOn, type));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return hashMap;
    }


    public static HashMap<String, BuildingDAO> getMasterBuildingList(Context context) {
        HashMap<String, BuildingDAO> hashMap = new HashMap<>();
        Cursor cursor = DBSQLiteHelper.queryDB(DatabaseContracts.Buildings.TABLE_NAME, context);
        try {
            while (cursor.moveToNext()) {
                String gname = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.Buildings.GAME_NAME));
                String genname = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.Buildings.GENERIC_NAME));
                String uiname = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.Buildings.UI_NAME));
                String faction = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.Buildings.FACTION));
                String type = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.Buildings.TYPE));
                int cap = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.Buildings.CAPACITY));
                boolean isTrap = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.Buildings.ISTRAP)));
                boolean isJunk = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.Buildings.ISJUNK)));
                int level = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.Buildings.LEVEL));

                hashMap.put(gname, new BuildingDAO(gname, genname, uiname, faction, type, isTrap, isJunk, cap, level));

            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return hashMap;
    }
}
