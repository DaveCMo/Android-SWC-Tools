package com.swctools.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import androidx.annotation.Nullable;

public class DBSQLiteDataHelper {
    private static final String TAG = "DBSQLiteDataHelper";

    private DBSQLiteDataHelper() {
    }


    private static long addNewBundleVal(String key, String value, Context context) {
        try {
            ContentValues values = new ContentValues();
            values.put(DatabaseContracts.BundledValues.BUNDLE_KEY, key);
            values.put(DatabaseContracts.BundledValues.BUNDLE_VALUE, value);
            return saveNewRecord(values, DatabaseContracts.BundledValues.TABLE_NAME, context);
        } catch (Exception e) {
            return 0;
        }
    }


    private static long addNewLayout(String playerId, String layoutName, String faction, String layoutJson, String type, String tag, long addedDate, Context context) {
        try {
            ContentValues values = new ContentValues();
            values.put(DatabaseContracts.LayoutTable.LAYOUT_NAME, layoutName);
            values.put(DatabaseContracts.LayoutTable.LAYOUT_JSON, layoutJson);
            values.put(DatabaseContracts.LayoutTable.PLAYER_ID, playerId);
            values.put(DatabaseContracts.LayoutTable.FACTION, faction);
            values.put(DatabaseContracts.LayoutTable.LAYOUT_TYPE, type);
            values.put(DatabaseContracts.LayoutTable.LAYOUT_TAG, tag);
            values.put(DatabaseContracts.LayoutTable.LAYOUT_ADDED, addedDate);

            long addedRow = saveNewRecord(values, DatabaseContracts.LayoutTable.TABLE_NAME, context);
            return addedRow;// new MethodResponse(true, String.valueOf(addedRow));

        } catch (Exception e) {
            return -1;
        }
    }

    private static long addNewLayoutVersion(int LAYOUT_ID, String LAYOUT_JSON, int LAYOUT_VERSION, Context context) {
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseContracts.LayoutVersions.LAYOUT_ID, LAYOUT_ID);
            contentValues.put(DatabaseContracts.LayoutVersions.LAYOUT_JSON, LAYOUT_JSON);
            contentValues.put(DatabaseContracts.LayoutVersions.LAYOUT_VERSION, LAYOUT_VERSION);
            return saveNewRecord(contentValues, DatabaseContracts.LayoutVersions.TABLE_NAME, context);
        } catch (Exception e) {
            return -1;
        }
    }

    private static boolean updateBundleValue(String key, String value, Context context) {
        ContentValues values = new ContentValues();
        values.put(DatabaseContracts.BundledValues.BUNDLE_VALUE, value);
        String whereClause = DatabaseContracts.BundledValues.BUNDLE_KEY + " like ?";
        String[] selectionArgs = {key};
        return true;//updateDB(
//                DatabaseContracts.BundledValues.TABLE_NAME,
//                values,
//                whereClause,
//                selectionArgs,
//                context);


    }

    private static long addNewLayoutTag(String layoutTag, Context context) throws Exception {

        try {
            ContentValues values = new ContentValues();
            values.put(DatabaseContracts.LayoutTags.LAYOUT_TAG, layoutTag);
            return saveNewRecord(values, DatabaseContracts.LayoutTags.TABLE_NAME, context);
        } catch (Exception e) {
            throw e;
        }

    }

    private static long addNewLayoutType(String layoutType, Context context) throws Exception {

        try {
            ContentValues values = new ContentValues();
            values.put(DatabaseContracts.LayoutTypes.LAYOUT_TYPE, layoutType);
            return saveNewRecord(values, DatabaseContracts.LayoutTypes.TABLE_NAME, context);
        } catch (Exception e) {
            throw e;
        }

    }


    private static long addNewPlayerRecord(String playerId, String playerSecret, String playerName, String playerFaction, String playerGuild, Context context) throws Exception {

        try {
            ContentValues values = new ContentValues();
            values.put(DatabaseContracts.PlayersTable.PLAYERID.toString(), playerId);
            values.put(DatabaseContracts.PlayersTable.PLAYERSECRET.toString(), playerSecret);
            values.put(DatabaseContracts.PlayersTable.PLAYERNAME.toString(), playerName);
            values.put(DatabaseContracts.PlayersTable.FACTION.toString(), playerFaction);
            values.put(DatabaseContracts.PlayersTable.PLAYERGUILD.toString(), playerGuild);
            return saveNewRecord(values, DatabaseContracts.PlayersTable.TABLE_NAME, context);
        } catch (Exception e) {
            throw e;
        }
    }

    private static boolean updateDB(String tableName, ContentValues values, String whereClause, String[] whereArgs, Context context) {
        //String selection = DatabaseContracts.EquipmentTable.FACTION + " like ?";
        try {

            DBSQLiteHelper.updateData(tableName, values, whereClause, whereArgs, context);

            return true;
        } catch (Exception e) {
            return false;
        } finally {

        }
    }

    private static long saveNewRecord(ContentValues values, String tableName, Context context) throws Exception {


        long newRowId = 0;
        try {

            newRowId = DBSQLiteHelper.insertData(tableName, values, context);

            return newRowId;
        } catch (Exception e) {
            throw e;
        } finally {

        }
    }

    private static boolean CheckIsDataAlreadyInDBorNot(String TableName,
                                                       String dbfield, String fieldValue, Context context) {

        Cursor cursor = null;
        String[] columns = {dbfield};
        String Query = "Select * from " + TableName + " where " + dbfield + " = ? " + fieldValue;
        String selection = dbfield + " like ?";
        String[] selectionArgs = {fieldValue};
        try {

            cursor = DBSQLiteHelper.queryDB(TableName, columns, selection, selectionArgs, context); //.rawQuery(Query, null);

            if (cursor.getCount() <= 0) {
                cursor.close();
                return false;
            }
        } catch (Exception e) {
            return false;
        } finally {

            if (cursor != null) {
                cursor.close();
            }
        }
        return true;
    }


    private static boolean deleteRow(String tableName, @Nullable String whereClause, @Nullable String[] whereArgs, Context context) {

        DBSQLiteHelper.deleteDbRows(tableName, whereClause, whereArgs, context);
        return true;
    }

    private static boolean deleteTableContents(String tableName, Context context) {

        DBSQLiteHelper.deleteDbRows(tableName, null, null, context);
        return true;
    }
}
