package com.swctools.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.text.TextUtils;
import android.util.Log;

import com.swctools.common.enums.ApplicationMessageTemplates;
import com.swctools.util.ErrorLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by David on 20/02/2018.
 */

public class DBSQLiteHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "swc_tools_database";
    private static final String TAG = "DBSQLiteHelper";
    public static final int DATABASE_VERSION = 193;
    private static DBSQLiteHelper instance;
    public Context context;

    public DBSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//        setWriteAheadLoggingEnabled(true);
        this.context = context;
    }


    private static synchronized DBSQLiteHelper getHelper(Context context) {

        if (instance == null) {
            instance = new DBSQLiteHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DatabaseContracts.PlayersTable.CREATE_TABLE);
        sqLiteDatabase.execSQL(DatabaseContracts.Planets.CREATE_TABLE);
        sqLiteDatabase.execSQL(DatabaseContracts.Config.CREATE_TABLE);
        sqLiteDatabase.execSQL(DatabaseContracts.TroopTable.CREATE_TABLE);
        sqLiteDatabase.execSQL(DatabaseContracts.EquipmentTable.CREATE_TABLE);
        sqLiteDatabase.execSQL(DatabaseContracts.LayoutTable.CREATE_TABLE);
        sqLiteDatabase.execSQL(DatabaseContracts.LayoutVersions.CREATE_TABLE);
        sqLiteDatabase.execSQL(DatabaseContracts.LayoutTypes.CREATE_TABLE);
        sqLiteDatabase.execSQL(DatabaseContracts.LayoutTags.CREATE_TABLE);
        sqLiteDatabase.execSQL(DatabaseContracts.BundledValues.CREATE_TABLE);
        sqLiteDatabase.execSQL(DatabaseContracts.DataVersionUpdateHistory.CREATE_TABLE);
        sqLiteDatabase.execSQL(DatabaseContracts.DataVersion.CREATE_TABLE);
        sqLiteDatabase.execSQL(DatabaseContracts.ReleaseLog.CREATE_TABLE);
        sqLiteDatabase.execSQL(DatabaseContracts.LayoutLog.CREATE_TABLE);
        sqLiteDatabase.execSQL(DatabaseContracts.DatabaseUpgradeLog.CREATE_TABLE);
        sqLiteDatabase.execSQL(DatabaseContracts.VersionLog.CREATE_TABLE);
        sqLiteDatabase.execSQL(DatabaseContracts.LayoutFolders.CREATE_TABLE);
        sqLiteDatabase.execSQL(DatabaseContracts.AppLog.CREATE_TABLE);
        sqLiteDatabase.execSQL(DatabaseContracts.SWC_HTTP_HEADERS.CREATE_TABLE);
        sqLiteDatabase.execSQL(DatabaseContracts.BattleLog.CREATE_TABLE);
        sqLiteDatabase.execSQL(DatabaseContracts.Buildings.CREATE_TABLE);
        sqLiteDatabase.execSQL(DatabaseContracts.ConflictData.CREATE_TABLE);
        sqLiteDatabase.execSQL(DatabaseContracts.LayoutTagAssignmentTable.CREATE_TABLE);
        sqLiteDatabase.execSQL(DatabaseContracts.SWCMessageLog.CREATE_TABLE);
        sqLiteDatabase.execSQL(DatabaseContracts.LayoutEditor.CREATE_TABLE);
        sqLiteDatabase.execSQL(DatabaseContracts.LayoutTop.CREATE_TABLE);
        sqLiteDatabase.execSQL(DatabaseContracts.TmpImageSelection.CREATE_TABLE);
        sqLiteDatabase.execSQL(DatabaseContracts.LayoutImagesTmp.CREATE_TABLE);
        sqLiteDatabase.execSQL(DatabaseContracts.LayoutImages.CREATE_TABLE);
        sqLiteDatabase.execSQL(DatabaseContracts.ImageLoaderTest.CREATE_TABLE);
        sqLiteDatabase.execSQL(DatabaseContracts.TroopBaseData.CREATE_TABLE);
        sqLiteDatabase.execSQL(DatabaseContracts.BuildingBaseData.CREATE_TABLE);
        //V5+
        sqLiteDatabase.execSQL(DatabaseContracts.WarBuffBases.CREATE_TABLE);
        sqLiteDatabase.execSQL(DatabaseContracts.WarDefense.CREATE_TABLE);
        sqLiteDatabase.execSQL(DatabaseContracts.WarParticipantTable.CREATE_TABLE);
        sqLiteDatabase.execSQL(DatabaseContracts.WarLog.CREATE_TABLE);
        sqLiteDatabase.execSQL(DatabaseContracts.WarSCContents.CREATE_TABLE);
        sqLiteDatabase.execSQL(DatabaseContracts.WarBattleDeployable.CREATE_TABLE);

        populateStandingData(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        Log.e(TAG, "Updating table from " + oldVersion + " to " + newVersion);
        // You will not need to modify this unless you need to do some android specific things.
        // When upgrading the database, all you need to do is add a file to the assets folder and name it:
        // from_1_to_2.sql with the version that you are upgrading to as the last version.

        for (int i = oldVersion; i < newVersion; ++i) {

            try {
                String migrationName = String.format("from_%d_to_%d.sql", i, (i + 1));
                readAndExecuteSQLScript(sqLiteDatabase, context, migrationName);
                ContentValues dataUpgradeLog = new ContentValues();
                dataUpgradeLog.put(DatabaseContracts.DatabaseUpgradeLog.DB_UPGRADE_VERSION, newVersion);
                dataUpgradeLog.put(DatabaseContracts.DatabaseUpgradeLog.DB_UPGRADE_CODE, String.format(ApplicationMessageTemplates.VISIT_KEY.getemplateString(), "onUpgrade", "readAndExecuteSQLScript"));
                dataUpgradeLog.put(DatabaseContracts.DatabaseUpgradeLog.DB_UPGRADE_MESSAGE, "Automated Script Ran OK");

                sqLiteDatabase.insert(DatabaseContracts.DatabaseUpgradeLog.TABLE_NAME, null, dataUpgradeLog);
            } catch (Exception e) {
                Log.e(TAG, "Exception running upgrade script:", e);
                ErrorLogger.LogError(e.getMessage(), "DBSQLiteHelper", "onUpgrade", "Old|" + oldVersion + "|New|" + newVersion);

            }
        }

        onCreate(sqLiteDatabase);//run through all create scripts just in case

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    private void readAndExecuteSQLScript(SQLiteDatabase db, Context ctx, String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return;
        }

        AssetManager assetManager = ctx.getAssets();
        BufferedReader reader = null;

        try {
            InputStream is = assetManager.open(fileName);
            InputStreamReader isr = new InputStreamReader(is);
            reader = new BufferedReader(isr);
            executeSQLScript(db, reader);
        } catch (IOException e) {

        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {

                }
            }
        }

    }

    private void executeSQLScript(SQLiteDatabase db, BufferedReader reader) throws IOException {
        String line;
        StringBuilder statement = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            statement.append(line);
            statement.append("\n");
            if (line.endsWith(";")) {
                db.execSQL(statement.toString());
                statement = new StringBuilder();
            }
        }
    }


    private void populateStandingData(SQLiteDatabase db) {
        //placeholder for populating standing data. May look to use files bundled in with the app in future releases instead of hard coding.
        //functions to populate data:
//        DatabaseStandingData databaseStandingData = new DatabaseStandingData(db, context);
//        databaseStandingData.executePopulation();
        db.delete(DatabaseContracts.VersionLog.TABLE_NAME, null, null);

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContracts.VersionLog.COLUMN_ID, 1);
        contentValues.put(DatabaseContracts.VersionLog.VALUE, 1);
        db.insert(DatabaseContracts.VersionLog.TABLE_NAME, null, contentValues);
    }


    /* SQL methods */

    public static void execSQL(String sql, Context context) {
        SQLiteDatabase sqLiteDatabase = getHelper(context).getWritableDatabase();
        if (sqLiteDatabase.inTransaction()) {
            sqLiteDatabase.endTransaction();
        }
        sqLiteDatabase.beginTransaction();

        try {
//            SQLiteStatement stmt = sqLiteDatabase.compileStatement(sql);
//            Log.d(TAG, "execSQL: " +sql);
//            stmt.executeUpdateDelete();
//            sqLiteDatabase.compileStatement(sql);
//
            sqLiteDatabase.execSQL(sql);
            sqLiteDatabase.setTransactionSuccessful();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            sqLiteDatabase.endTransaction();

        }
    }

    public static boolean bulkInsert(String tblName, ArrayList<ContentValues> contentValuesArrayList, int repeatNo, String repeatColumn, Context context) {
        SQLiteDatabase sqLiteDatabase = getHelper(context).getWritableDatabase();
        if (sqLiteDatabase.inTransaction()) {
            sqLiteDatabase.endTransaction();
        }
        sqLiteDatabase.beginTransaction();
        try {
            for (int i = 0; i < contentValuesArrayList.size(); i++) {

                if (repeatNo > 0) {
                    for (int repItr = 1; repItr <= 50; repItr++) {
                        contentValuesArrayList.get(i).put(repeatColumn, repItr);
                        sqLiteDatabase.insert(tblName, null, contentValuesArrayList.get(i));
                    }
                } else {
                    sqLiteDatabase.insert(tblName, null, contentValuesArrayList.get(i));
                }
            }
            sqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            sqLiteDatabase.endTransaction();
        }
        return true;

    }

    public static int updateData(String tableName, ContentValues contentValues, @Nullable String whereClause, @Nullable String[] whereArgs, Context context) {
        return updateDBTable(tableName, contentValues, whereClause, whereArgs, context);
    }

    public static int updateAllData(String tableName, ContentValues contentValues, Context context) {
        return updateDBTable(tableName, contentValues, null, null, context);
    }

    public static int updateData(String tableName, ContentValues contentValues, @Nullable String whereClause, @Nullable String whereArg, Context context) {
        String[] whereArgs = {whereArg};
        return updateDBTable(tableName, contentValues, whereClause, whereArgs, context);
    }

    public static int updateData(String tableName, ContentValues contentValues, Context context) {
//        String[] whereArgs = {whereArg};
        return updateDBTable(tableName, contentValues, null, null, context);
    }

    private static int updateDBTable(String tableName, ContentValues contentValues, @Nullable String whereClause, @Nullable String[] whereArgs, Context context) {
        SQLiteDatabase sqLiteDatabase = getHelper(context).getWritableDatabase();
        int result = -1;
        if (sqLiteDatabase.inTransaction()) {
            sqLiteDatabase.endTransaction();
        }
        try {
            sqLiteDatabase.beginTransaction();
            sqLiteDatabase.setTransactionSuccessful();
            result = sqLiteDatabase.update(tableName, contentValues, whereClause, whereArgs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqLiteDatabase.endTransaction();
        }
        return result;
    }

    public static long insertData(String tableName, ContentValues contentValues, Context context) {
        SQLiteDatabase sqLiteDatabase = getHelper(context).getWritableDatabase();
        long rowInserted = -1;
        if (sqLiteDatabase.inTransaction()) {
            sqLiteDatabase.endTransaction();
        }
        try {
            sqLiteDatabase.beginTransaction();
            rowInserted = sqLiteDatabase.insert(tableName, null, contentValues);
            sqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqLiteDatabase.endTransaction();
        }
        return rowInserted;
    }

    public static Cursor rawQuery(@NonNull String qryString, String[] whereArgs, Context context) {
        return getHelper(context).getReadableDatabase().rawQuery(qryString, whereArgs);
    }

    public static Cursor queryDB(@NonNull String tableName, Context context) {
        return getData(tableName, null, null, null, null, null, null, null, context);
    }

    public static Cursor queryDB(@NonNull String tableName, @Nullable String[] columns, Context context) {
        return getData(tableName, columns, null, null, null, null, null, null, context);
    }

    public static Cursor queryDB(@NonNull String tableName, @Nullable String[] columns, @Nullable String whereClause, @Nullable String whereArg, Context context) {
        String[] whereArgs = {whereArg};
        return getData(tableName, columns, whereClause, whereArgs, null, null, null, null, context);
    }


    public static long countAllTableRows(String tableName, Context context) {
        SQLiteDatabase sqLiteDatabase = getHelper(context).getWritableDatabase();
        if (sqLiteDatabase.inTransaction()) {
            sqLiteDatabase.endTransaction();
        }


        long rows = 0;
        rows = DatabaseUtils.queryNumEntries(sqLiteDatabase, tableName);

        return rows;
    }

    public static long insertReplaceData(String tableName, ContentValues contentValues, Context context) {
        SQLiteDatabase sqLiteDatabase = getHelper(context).getWritableDatabase();
        if (sqLiteDatabase.inTransaction()) {
            sqLiteDatabase.endTransaction();
        }
        long rowInserted = -1;

        try {
            sqLiteDatabase.beginTransaction();
            rowInserted = sqLiteDatabase.insertWithOnConflict(tableName, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
            sqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqLiteDatabase.endTransaction();
        }
        return rowInserted;
    }

    public static Cursor queryDB(@NonNull String tableName, @Nullable String[] columns, @Nullable String whereClause, @Nullable String[] whereArgs, String groupBy, String having, String orderBy, String limit, Context context) {

        return getData(tableName, columns, whereClause, whereArgs, groupBy, having, orderBy, limit, context);
    }

    public static Cursor queryDB(@NonNull String tableName, @Nullable String[] columns, @Nullable String whereClause, @Nullable String[] whereArgs, Context context) {

        return getData(tableName, columns, whereClause, whereArgs, null, null, null, null, context);
    }

    private static Cursor getData(@NonNull String tableName, @Nullable String[] columns, @Nullable String whereClause, @Nullable String[] whereArgs, String groupBy, String having, String orderBy, String limit, Context context) {
        Cursor cursor = null;
        SQLiteDatabase sqLiteDatabase = getHelper(context).getReadableDatabase();
        if (sqLiteDatabase.inTransaction()) {
            sqLiteDatabase.endTransaction();
        }
        sqLiteDatabase.beginTransaction();
        try {

            cursor = sqLiteDatabase.query(tableName, columns, whereClause, whereArgs, groupBy, having, orderBy, limit);
            sqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqLiteDatabase.endTransaction();

        }
        return cursor;

    }

    public static boolean deleteDbRows(String tableName, String whereClause, String[] whereArgs, Context context) {
        return deleteDbData(tableName, whereClause, whereArgs, context);
    }

    public static boolean deleteDbRow(String tableName, String whereClause, String whereArg, Context context) {
        String[] whereArgs = {whereArg};
        return deleteDbData(tableName, whereClause, whereArgs, context);
    }

    private static boolean deleteDbData(@NonNull String tableName, String whereClause, @Nullable String[] whereArgs, Context context) {
        SQLiteDatabase sqLiteDatabase = getHelper(context).getWritableDatabase();
        boolean result = false;
        if (sqLiteDatabase.inTransaction()) {
            sqLiteDatabase.endTransaction();
        }
        try {
            sqLiteDatabase.beginTransaction();
            int countDeleted = sqLiteDatabase.delete(tableName, whereClause, whereArgs);
            sqLiteDatabase.setTransactionSuccessful();
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        } finally {
            sqLiteDatabase.endTransaction();
        }
        return result;
    }

    public static boolean CheckIsDataAlreadyInDBorNot(String TableName,
                                                      String dbfield, String fieldValue, Context context) {
        SQLiteDatabase database = DBSQLiteHelper.getHelper(context).getReadableDatabase();
        Cursor cursor = null;
        String[] columns = {dbfield};
        String Query = "Select * from " + TableName + " where " + dbfield + " = ? " + fieldValue;
        String selection = dbfield + " like ?";
        String[] selectionArgs = {fieldValue};
        try {

            cursor = database.query(TableName, columns, selection, selectionArgs, null, null, null); //.rawQuery(Query, null);

            if (cursor.getCount() <= 0) {

                return false;
            }
        } catch (Exception e) {
            return false;
        } finally {
        }
        return true;
    }

}
