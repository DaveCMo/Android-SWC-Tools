package com.swctools.database.database_updates;

import android.content.Context;
import android.database.SQLException;

import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;

public class DB17 {
    private static final String TAG = "DB17";
    private Context context;

    public DB17(Context context) {
        this.context = context;
    }

    public void executeUpdates() {
        try {
            createLayoutVersion();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createLayoutVersion() {
        try {
            DBSQLiteHelper.rawQuery("DROP TABLE IF EXISTS " + DatabaseContracts.LayoutVersions.TABLE_NAME, null, context);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DBSQLiteHelper.rawQuery(DatabaseContracts.LayoutVersions.CREATE_TABLE, null, context);
        String migrateLayouts = "INSERT INTO " + DatabaseContracts.LayoutVersions.TABLE_NAME +
                " (" + DatabaseContracts.LayoutVersions.LAYOUT_JSON + ", " +
                DatabaseContracts.LayoutVersions.LAYOUT_VERSION + ", "  +
                DatabaseContracts.LayoutVersions.LAYOUT_ID + ") " +
                " SELECT " +
                DatabaseContracts.LayoutTable.LAYOUT_JSON + ", " +
                "'1' as vers, "  +
                DatabaseContracts.LayoutTable.COLUMN_ID;

        DBSQLiteHelper.rawQuery(migrateLayouts, null, context);
        fixLayoutTable();

    }
    private void fixLayoutTable() {
        //fix for layout manager table.
        String TMP_TABLE = "layoutTmp";
        String tmpLayoutTblCreate = "CREATE TABLE " + TMP_TABLE + " AS SELECT * FROM " +
                DatabaseContracts.LayoutTable.TABLE_NAME + " " +
                "WHERE " + DatabaseContracts.LayoutTable.COLUMN_ID + " IS NOT NULL;";
        DBSQLiteHelper.rawQuery(tmpLayoutTblCreate, null, context);
        String dropLayoutTbl = "DROP TABLE IF EXISTS " + DatabaseContracts.LayoutTable.TABLE_NAME;
        DBSQLiteHelper.rawQuery(dropLayoutTbl, null, context);

        DBSQLiteHelper.rawQuery(DatabaseContracts.LayoutTable.CREATE_TABLE, null, context);

        String copyDataBack = "INSERT INTO " +
                DatabaseContracts.LayoutTable.TABLE_NAME + " " +
                "SELECT * FROM " + TMP_TABLE;
        DBSQLiteHelper.rawQuery(copyDataBack, null, context);
        DBSQLiteHelper.rawQuery("DROP TABLE IF EXISTS " + TMP_TABLE, null, context);
    }
}

