package com.swctools.database.database_updates;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;
import com.swctools.common.helpers.AppUpdateHelper;

public class DatabaseVersionUpdateHelper {
    //Container for update functions to be run
    private static Context mContext;

    public static void handleUpdate(int oldVersion, int newVersion, Context context) {
        mContext = context;
        if (oldVersion < newVersion) {
            try {

            } catch (Exception e) {
                try {
                    DBSQLiteHelper.rawQuery(DatabaseContracts.VersionLog.CREATE_TABLE, null, context);

                } catch (Exception e1) {
                    e1.printStackTrace();
                }

            }
        }

        if (oldVersion < 110) {
            try {
                migrateToTagAssignment();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        AppUpdateHelper.logNewDbUpgradeResult(newVersion, "Database Upgrade", "UPGRADE COMPLETE", context);

    }

    public static void migrateToTagAssignment() {


        //First go and migrate the type data.
        String migrateTagInsSQL = "INSERT INTO " +
                DatabaseContracts.LayoutTagAssignmentTable.TABLE_NAME +
                " (" + DatabaseContracts.LayoutTagAssignmentTable.LAYOUT_ID + ", " + DatabaseContracts.LayoutTagAssignmentTable.TAG_ID + ") " +
                "SELECT " + DatabaseContracts.LayoutTable.COLUMN_ID + ", " + DatabaseContracts.LayoutTable.LAYOUT_TAG + " FROM " + DatabaseContracts.LayoutTable.TABLE_NAME;
        String migrateTypeInsSQL = "INSERT INTO " +
                DatabaseContracts.LayoutTagAssignmentTable.TABLE_NAME +
                " (" + DatabaseContracts.LayoutTagAssignmentTable.LAYOUT_ID + ", " + DatabaseContracts.LayoutTagAssignmentTable.TAG_ID + ") " +
                "SELECT " + DatabaseContracts.LayoutTable.COLUMN_ID + ", %1$s " + " FROM " + DatabaseContracts.LayoutTable.TABLE_NAME + " WHERE " + DatabaseContracts.LayoutTable.LAYOUT_TYPE + " = %2$s";
        ContentValues contentValues = new ContentValues();

        try {
            DBSQLiteHelper.rawQuery(DatabaseContracts.LayoutTagAssignmentTable.CREATE_TABLE, null, mContext);
            DBSQLiteHelper.deleteDbRows(DatabaseContracts.LayoutTagAssignmentTable.TABLE_NAME, null, null, mContext);
            DBSQLiteHelper.rawQuery(migrateTagInsSQL, null, mContext);
            Cursor cursor = DBSQLiteHelper.queryDB(DatabaseContracts.LayoutTypes.TABLE_NAME, mContext);
            try {


                while (cursor.moveToNext()) {
                    int oldId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutTypes.COLUMN_ID));
                    String typeString = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutTypes.LAYOUT_TYPE));
                    contentValues.put(DatabaseContracts.LayoutTags.LAYOUT_TAG, typeString);
                    long newId = DBSQLiteHelper.insertData(DatabaseContracts.LayoutTags.TABLE_NAME, contentValues, mContext);
                    String runSQL = String.format(migrateTypeInsSQL, String.valueOf(newId), String.valueOf(oldId));
                    DBSQLiteHelper.rawQuery(runSQL, null, mContext);
                    contentValues.clear();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }


//
    }


}
