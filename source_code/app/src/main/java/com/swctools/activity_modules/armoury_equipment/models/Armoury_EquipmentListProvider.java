package com.swctools.activity_modules.armoury_equipment.models;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.swctools.common.models.player_models.ArmouryEquipment;
import com.swctools.common.models.player_models.UpgradeItemModel;
import com.swctools.common.models.player_models.UpgradeItems;
import com.swctools.common.models.player_models.Upgrades;
import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;
import com.swctools.swc_server_interactions.results.SWCVisitResult;

import java.util.ArrayList;
import java.util.HashMap;

public class Armoury_EquipmentListProvider {
    private static final String TAG = "Armoury_EquipmentListPr";

//    public static ArrayList<Armoury_Set_Item> getArmourySetItems(String faction, Context context) {
//        ArrayList<Armoury_Set_Item> armourySetItems = new ArrayList<>();
//
//        String whereClause = DatabaseContracts.EquipmentTable.FACTION + " = ? ";
//        String[] whereArgs = {faction};
//
//        Cursor cursor = DBSQLiteHelper.queryDB(DatabaseContracts.EquipmentTable.TABLE_NAME, null, whereClause, whereArgs, context);
//
//        try {
//            while (cursor.moveToNext()) {
//                equipName =
//
//                Armoury_Set_Item armoury_set_item = new Armoury_Set_Item(equipName, uiName, faction, capacity, level, availability);
//            }
//        } finally {
//
//            if (cursor != null) {
//                cursor.close();
//            }
//
//        }
//
//
//        return armourySetItems;
//    }


    public static ArrayList<Armoury_Set_Item> getAvailableEquipment(SWCVisitResult disneyVisitResult, HashMap<String, ArmouryEquipment> getMasterArmourList, Context context) {
        ArrayList<Armoury_Set_Item> upgradeItemModels = new ArrayList<>();
        String planetName = getPlanetName(disneyVisitResult.mplayerModel().map().getString("planet"), context);
        Log.d(TAG, "getAvailableEquipment: " + planetName);
        Upgrades upgrades = new Upgrades(disneyVisitResult.mplayerModel().upgrades(), disneyVisitResult.mplayerModel().faction());
        ///Do troops first....
        for (UpgradeItems upgradeItem : upgrades.getEquipment().getUpgradeItems()) {
            //because some units require some special prefix, not derived from the json object they are in:
            UpgradeItemModel upgradeItemModel = new UpgradeItemModel(upgradeItem.itemName, upgradeItem.itemLevel, 0);
            ArmouryEquipment armouryEquipment = getMasterArmourList.get(upgradeItem.itemName + upgradeItem.itemLevel);
            if (armouryEquipment.get_availableOn().equalsIgnoreCase(planetName)||armouryEquipment.get_availableOn().equalsIgnoreCase("all")) {

                Armoury_Set_Item armoury_set_item = new Armoury_Set_Item(upgradeItem.itemName, armouryEquipment.uiName(), disneyVisitResult.mplayerModel().faction(), armouryEquipment.cap(), upgradeItem.itemLevel, armouryEquipment.get_availableOn(), armouryEquipment.getType());
                Log.d(TAG, armoury_set_item.toString());
                upgradeItemModels.add(armoury_set_item);
            }
        }


        return upgradeItemModels;
    }

    private static String getPlanetName(String lookup, Context context) {
        String planetName = lookup;
        String[] columns = {DatabaseContracts.Planets.UI_NAME, DatabaseContracts.Planets.GAME_NAME, DatabaseContracts.Planets.TYPE};
        String selectionStr = DatabaseContracts.Planets.GAME_NAME + " = ?";
        String[] selectionArgs = {lookup};
        Cursor cursor = DBSQLiteHelper.queryDB(DatabaseContracts.Planets.TABLE_NAME, columns, selectionStr, selectionArgs, context);
        while (cursor.moveToNext()) {
            planetName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.Planets.UI_NAME));
            return cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.Planets.TYPE));

        }
        return planetName;
    }
}
