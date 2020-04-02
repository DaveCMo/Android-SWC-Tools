package com.swctools.database.database_updates;

import android.content.Context;

import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;


public class DB16 {
    private static final String TAG = "DB16";
    private Context context;

    public DB16(Context context) {
        this.context = context;
    }

    public void executeUpdates() {
        try {
            legacy();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void legacy() {
        try {
            DBSQLiteHelper.rawQuery("ALTER TABLE " + DatabaseContracts.PlayersTable.TABLE_NAME + " ADD COLUMN " + DatabaseContracts.PlayersTable.NOTIFICATIONS + " TEXT", null, context);
            DBSQLiteHelper.rawQuery("ALTER TABLE " + DatabaseContracts.PlayersTable.TABLE_NAME + " ADD COLUMN " + DatabaseContracts.PlayersTable.REFRESH_INT + " INTEGER", null, context);
        } catch (Exception e) {
        }
        try {

            DBSQLiteHelper.rawQuery("ALTER TABLE " + DatabaseContracts.LayoutTable.TABLE_NAME + " ADD COLUMN " + DatabaseContracts.LayoutTable.PLAYER_ID + " TEXT", null, context);

        } catch (Exception e) {
        }
        try {
            DBSQLiteHelper.rawQuery("ALTER TABLE " + DatabaseContracts.LayoutTable.TABLE_NAME + " ADD COLUMN " + DatabaseContracts.LayoutTable.LAYOUT_JSON + " TEXT", null, context);
        } catch (Exception e) {
        }
    }


}
