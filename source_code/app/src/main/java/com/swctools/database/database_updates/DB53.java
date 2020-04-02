package com.swctools.database.database_updates;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;
import com.swctools.common.helpers.AppUpdateHelper;

public class DB53 {
    private static final String TAG = "DB48";
    private Context context;
    private int newVersion;

    public DB53(int newVersion, Context context) {

        this.newVersion = newVersion;
        this.context = context;
    }

    public void executeUpdates() {
        try {
            DBSQLiteHelper.rawQuery("ALTER TABLE " + DatabaseContracts.LayoutTable.TABLE_NAME +
                    " ADD COLUMN " + DatabaseContracts.LayoutTable.LAYOUT_IMAGE + " TEXT", null, context);
            DBSQLiteHelper.rawQuery("ALTER TABLE " + DatabaseContracts.LayoutVersions.TABLE_NAME +
                    " ADD COLUMN " + DatabaseContracts.LayoutVersions.LAYOUT_IMAGE + " TEXT", null, context);
            DBSQLiteHelper.rawQuery("ALTER TABLE " + DatabaseContracts.LayoutVersions.TABLE_NAME +
                    " ADD COLUMN " + DatabaseContracts.LayoutVersions.LAYOUT_ADDED + " INTEGER", null, context);
            AppUpdateHelper.logNewDbUpgradeResult(newVersion, "DB48executeUpdates", "Finished adding new fields", context);

        } catch (Exception e) {
            AppUpdateHelper.logNewDbUpgradeResult(newVersion, "DB48executeUpdates", e.getMessage(), context);
        }
        try {
            migrateAddedDates();
            AppUpdateHelper.logNewDbUpgradeResult(newVersion, "DB48executeUpdates", "Data Migration ran", context);

        } catch (Exception e){}
        AppUpdateHelper.logNewDbUpgradeResult(newVersion, "DB48executeUpdates", "Data Migration ran", context);
    }

    private void migrateAddedDates() {
        ContentValues contentValues = new ContentValues();
        Cursor cursor = DBSQLiteHelper.queryDB(DatabaseContracts.LayoutTable.TABLE_NAME, context);
        String whereClause = DatabaseContracts.LayoutVersions.LAYOUT_ID + " = ?";
        while (cursor.moveToNext()) {
            int layoutId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutTable.COLUMN_ID));
            int layoutAdded = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutTable.LAYOUT_ADDED));
            contentValues.put(DatabaseContracts.LayoutVersions.LAYOUT_ADDED, layoutAdded);
            String[] whereArgs = {String.valueOf(layoutId)};
            DBSQLiteHelper.updateData(DatabaseContracts.LayoutVersions.TABLE_NAME, contentValues, whereClause, whereArgs, context);
            contentValues.clear();
        }

    }

}

