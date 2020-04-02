package com.swctools.database.database_updates;

import android.content.Context;
import android.database.Cursor;

import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;

import java.util.ArrayList;
import java.util.List;

public class DB43 {
    private static final String TAG = "DB43";
    private Context context;

    public DB43(Context context) {
        this.context = context;
    }

    public void executeUpdates() {
        try {

            //1. Add tmp columns to tag and type tables.
            Cursor cursor;
            cursor = DBSQLiteHelper.queryDB(DatabaseContracts.LayoutTable.TABLE_NAME, context);
            while (cursor.moveToNext()) {
                String[] cols = cursor.getColumnNames();
                for (String s : cols) {

                }

            }
            String TMP_TABLE = "tl";
            String TMP_TAG_COL_ID = "tmp_tag_id";
            String TMP_TYPE_ID = "tmp_type_id";
            String UPDATE_BRING_IDS =
                    "UPDATE %1$s SET %2$s = (SELECT %3$s FROM %4$s WHERE %5$s = %1$s.%6$s);";
            String UPDATE_IDS = "UPDATE tl SET " +
                    DatabaseContracts.LayoutTable.LAYOUT_TAG + " = " + TMP_TAG_COL_ID + ", " +
                    DatabaseContracts.LayoutTable.LAYOUT_TYPE + " = " + TMP_TYPE_ID + ";";
            String newCols =
                    DatabaseContracts.LayoutTable.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            DatabaseContracts.LayoutTable.LAYOUT_NAME + " TEXT, " +
                            DatabaseContracts.LayoutTable.PLAYER_ID + " TEXT, " +
                            DatabaseContracts.LayoutTable.LAYOUT_JSON + " TEXT, " +
                            DatabaseContracts.LayoutTable.LAYOUT_TYPE + " TEXT, " +
                            DatabaseContracts.LayoutTable.LAYOUT_TAG + " TEXT, " +
                            DatabaseContracts.LayoutTable.FACTION + " TEXT, " +
                            DatabaseContracts.LayoutTable.LAYOUT_ADDED + " INTEGER";
            String newColsNoId =
                    DatabaseContracts.LayoutTable.LAYOUT_NAME + ", " +
                            DatabaseContracts.LayoutTable.PLAYER_ID + ", " +
                            DatabaseContracts.LayoutTable.LAYOUT_JSON + ", " +
                            DatabaseContracts.LayoutTable.LAYOUT_TYPE + ", " +
                            DatabaseContracts.LayoutTable.LAYOUT_TAG + ", " +
                            DatabaseContracts.LayoutTable.FACTION + ", " +
                            DatabaseContracts.LayoutTable.LAYOUT_ADDED;
            String selectCols =
                    DatabaseContracts.LayoutTable.COLUMN_ID + ", " +
                            DatabaseContracts.LayoutTable.LAYOUT_NAME + ", " +
                            DatabaseContracts.LayoutTable.PLAYER_ID + ", " +
                            DatabaseContracts.LayoutTable.LAYOUT_JSON + ", " +
                            DatabaseContracts.LayoutTable.LAYOUT_TYPE + ", " +
                            DatabaseContracts.LayoutTable.LAYOUT_TAG + ", " +
                            DatabaseContracts.LayoutTable.FACTION + ", " +
                            DatabaseContracts.LayoutTable.LAYOUT_ADDED;

            List<String> migrationScript = new ArrayList<>();
            DBSQLiteHelper.rawQuery("DROP TABLE IF EXISTS " + TMP_TABLE, null, context);
            DBSQLiteHelper.rawQuery("CREATE TABLE IF NOT EXISTS  " + TMP_TABLE + " (" + newCols + ");", null, context);
            DBSQLiteHelper.rawQuery("INSERT INTO " + TMP_TABLE + " SELECT " + selectCols + " FROM " + DatabaseContracts.LayoutTable.TABLE_NAME + ";", null, context);
            DBSQLiteHelper.rawQuery("ALTER TABLE " + TMP_TABLE + " ADD COLUMN " + TMP_TAG_COL_ID + " INTEGER;", null, context);
            DBSQLiteHelper.rawQuery("ALTER TABLE " + TMP_TABLE + " ADD COLUMN " + TMP_TYPE_ID + " INTEGER;", null, context);
            DBSQLiteHelper.rawQuery(String.format(UPDATE_BRING_IDS,
                    TMP_TABLE,
                    TMP_TAG_COL_ID,
                    DatabaseContracts.LayoutTags.COLUMN_ID,
                    DatabaseContracts.LayoutTags.TABLE_NAME,
                    DatabaseContracts.LayoutTags.LAYOUT_TAG,
                    DatabaseContracts.LayoutTable.LAYOUT_TAG
            ), null, context);
            DBSQLiteHelper.rawQuery(String.format(UPDATE_BRING_IDS,
                    TMP_TABLE,
                    TMP_TYPE_ID,
                    DatabaseContracts.LayoutTypes.COLUMN_ID,
                    DatabaseContracts.LayoutTypes.TABLE_NAME,
                    DatabaseContracts.LayoutTypes.LAYOUT_TYPE,
                    DatabaseContracts.LayoutTable.LAYOUT_TYPE
            ), null, context);
            DBSQLiteHelper.rawQuery(UPDATE_IDS, null, context);
            DBSQLiteHelper.rawQuery("DROP TABLE IF EXISTS " + DatabaseContracts.LayoutTable.TABLE_NAME + ";", null, context);
            DBSQLiteHelper.rawQuery(DatabaseContracts.LayoutTable.CREATE_TABLE, null, context);
            DBSQLiteHelper.rawQuery("INSERT INTO " + DatabaseContracts.LayoutTable.TABLE_NAME + " (" +
                    selectCols + ") " +
                    " SELECT " +
                    DatabaseContracts.LayoutTable.COLUMN_ID + ", " +
                    DatabaseContracts.LayoutTable.LAYOUT_NAME + ", " +
                    DatabaseContracts.LayoutTable.PLAYER_ID + ", " +
                    DatabaseContracts.LayoutTable.LAYOUT_JSON + ", " +
                    TMP_TYPE_ID + ", " +
                    TMP_TAG_COL_ID + ", " +
                    DatabaseContracts.LayoutTable.FACTION + ", " +
                    DatabaseContracts.LayoutTable.LAYOUT_ADDED +
                    " FROM " + TMP_TABLE + ";", null, context);


            cursor = DBSQLiteHelper.queryDB(DatabaseContracts.LayoutTable.TABLE_NAME, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

