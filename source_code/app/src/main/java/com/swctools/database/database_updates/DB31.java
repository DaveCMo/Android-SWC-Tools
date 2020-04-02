package com.swctools.database.database_updates;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;


import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;
import com.swctools.config.AppConfig;
import com.swctools.util.SaveJsonFile;

public class DB31 {
    private static final String TAG = "DB29";
    private Context context;

    public DB31(Context context) {
        this.context = context;
    }

    public void executeUpdates() {
        try {
            migrateLayoutDataToVersions();
        } catch (Exception e) {
            e.printStackTrace();
        }

        DBSQLiteHelper.rawQuery(DatabaseContracts.BundledValues.CREATE_TABLE, null, context);
    }

    private void popTestData() {


    }

    private void migrateLayoutDataToVersions() {


        DBSQLiteHelper.rawQuery(DatabaseContracts.LayoutVersions.CREATE_TABLE, null, context);
        ContentValues contentValues = new ContentValues();
        Cursor cursor = DBSQLiteHelper.queryDB(DatabaseContracts.LayoutTable.TABLE_NAME, context);
        int er2 = 0;
        try {
            while (cursor.moveToNext()) {
                int layoutId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutTable.COLUMN_ID));
                boolean layoutVersionExits = versionExists(layoutId);
                int er1 = 0;
                if (!layoutVersionExits) {
                    contentValues.put(DatabaseContracts.LayoutVersions.LAYOUT_ID, layoutId);
                    contentValues.put(DatabaseContracts.LayoutVersions.LAYOUT_JSON, cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutTable.LAYOUT_JSON)));
                    contentValues.put(DatabaseContracts.LayoutVersions.LAYOUT_VERSION, "1");

                    try {
                        DBSQLiteHelper.insertData(DatabaseContracts.LayoutVersions.TABLE_NAME, contentValues, context);
                    } catch (Exception e) {
                        e.printStackTrace();
                        String fileN = er1 + "err";
                        AppConfig appConfig = new AppConfig(context);
                        SaveJsonFile.saveJsonFile(e.getMessage(), fileN, appConfig.layoutManagerExportFolder(), ".json");
                        er1++;
                    }
                    contentValues.clear();
                } else {

                    String selection = DatabaseContracts.LayoutVersions.LAYOUT_ID + " = ?";
                    String[] selectionArgs = {String.valueOf(layoutId)};
                    Cursor layoutVersRecords = DBSQLiteHelper.queryDB(DatabaseContracts.LayoutVersions.TABLE_NAME, null, selection, selectionArgs, context);
                    if (layoutVersRecords.getCount() == 1) {
                        ContentValues versionFIX = new ContentValues();
                        versionFIX.put(DatabaseContracts.LayoutVersions.LAYOUT_VERSION, "1");
                        DBSQLiteHelper.updateData(DatabaseContracts.LayoutVersions.TABLE_NAME, versionFIX, selection, selectionArgs, context);
                    }

                }
            }
        } catch (Exception e) {
            String fileN = er2 + "err";
            AppConfig appConfig = new AppConfig(context);
            SaveJsonFile.saveJsonFile(e.getMessage(), fileN, appConfig.layoutManagerExportFolder(), ".json");
            e.printStackTrace();
        }
    }

    private boolean versionExists(int layoutId) {
        String selection = DatabaseContracts.LayoutVersions.LAYOUT_ID + " = ?";
        String[] selectionArgs = {String.valueOf(layoutId)};
        Cursor cursor = DBSQLiteHelper.queryDB(DatabaseContracts.LayoutVersions.TABLE_NAME, null, selection, selectionArgs, context);
        if (cursor.getCount()<= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }
}

