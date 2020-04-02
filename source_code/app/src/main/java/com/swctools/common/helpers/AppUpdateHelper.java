package com.swctools.common.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;
import com.swctools.activity_modules.updates.DataTablesDetails;

import java.util.ArrayList;

public class AppUpdateHelper {


    private static final String TAG = "AppUpdateHelper";

    private AppUpdateHelper() {

    }

    public static void logNewDbUpgradeResult(int version, String appCode, String message, Context context) {
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseContracts.DatabaseUpgradeLog.DB_UPGRADE_VERSION, version);
            contentValues.put(DatabaseContracts.DatabaseUpgradeLog.DB_UPGRADE_CODE, appCode);
            contentValues.put(DatabaseContracts.DatabaseUpgradeLog.DB_UPGRADE_MESSAGE, message);
            DBSQLiteHelper.insertData(DatabaseContracts.DatabaseUpgradeLog.TABLE_NAME, contentValues, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<DataTablesDetails> dataTablesDetails(Context context) {
        ArrayList<DataTablesDetails> dataTablesDetails = new ArrayList<>();

        DataTablesDetails troopsTbl = new DataTablesDetails(DatabaseContracts.TroopTable.TABLE_NAME, "https://raw.githubusercontent.com/DaveCMo/Android-SWC-Tools/master/troop_data.json", latestVersion(DatabaseContracts.TroopTable.TABLE_NAME, context));
        dataTablesDetails.add(troopsTbl);

        DataTablesDetails armouryTbl = new DataTablesDetails(DatabaseContracts.EquipmentTable.TABLE_NAME, "https://raw.githubusercontent.com/DaveCMo/Android-SWC-Tools/master/armoury_data.json", latestVersion(DatabaseContracts.TroopTable.TABLE_NAME, context));
        dataTablesDetails.add(armouryTbl);

        DataTablesDetails buildingTbl = new DataTablesDetails(DatabaseContracts.Buildings.TABLE_NAME, "https://raw.githubusercontent.com/DaveCMo/Android-SWC-Tools/master/building_data.json", latestVersion(DatabaseContracts.Buildings.TABLE_NAME, context));
        dataTablesDetails.add(buildingTbl);

        DataTablesDetails planetTbl = new DataTablesDetails(DatabaseContracts.Planets.TABLE_NAME, "https://raw.githubusercontent.com/DaveCMo/Android-SWC-Tools/master/planet_data.json", latestVersion(DatabaseContracts.Planets.TABLE_NAME, context));
        dataTablesDetails.add(planetTbl);

        return dataTablesDetails;

    }


    public static int latestVersion(String tableName, Context context) {
        String whereClause = DatabaseContracts.DataVersion.APP_TABLE + " = ?";
        String[] whereArg = {tableName};
        int latestVersion = 0;
        Cursor cursor = DBSQLiteHelper.queryDB(DatabaseContracts.DataVersion.TABLE_NAME, null, whereClause, whereArg, context);
        try {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    latestVersion = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.DataVersion.APP_TABLE_VERSION));
                }
            } else {
                ContentValues contentValues = new ContentValues();
                contentValues.put(DatabaseContracts.DataVersion.APP_TABLE, tableName);
                contentValues.put(DatabaseContracts.DataVersion.APP_TABLE_VERSION, 1);

                DBSQLiteHelper.insertData(DatabaseContracts.DataVersion.TABLE_NAME, contentValues, context);
                latestVersion = 0;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return latestVersion;

    }

    public static void updateDataLog(String updateNotes, String tableName, int dataVersion, Context mContext) {
        ContentValues contentValues = new ContentValues();
        long addedDate = (DateTimeHelper.userIDTimeStamp() / 1000);


        contentValues.put(DatabaseContracts.DataVersionUpdateHistory.APP_TABLE, tableName);
        contentValues.put(DatabaseContracts.DataVersionUpdateHistory.APP_TABLE_VERSION, dataVersion);
        contentValues.put(DatabaseContracts.DataVersionUpdateHistory.UPDATE_NOTES, updateNotes);
        contentValues.put(DatabaseContracts.DataVersionUpdateHistory.APP_TABLE_UPDATED_ON, addedDate);
        DBSQLiteHelper.insertData(DatabaseContracts.DataVersionUpdateHistory.TABLE_NAME, contentValues, mContext);
    }

}
