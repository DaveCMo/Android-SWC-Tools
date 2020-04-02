package com.swctools.database.database_updates;

import android.content.Context;

import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;

public class DB29 {
    private static final String TAG = "DB29";
    private Context context;

    public DB29(Context context) {
        this.context = context;
    }

    public void executeUpdates() {
        try {
            DBSQLiteHelper.rawQuery("ALTER TABLE " + DatabaseContracts.DataVersionUpdateHistory.TABLE_NAME + " ADD COLUMN " + DatabaseContracts.DataVersionUpdateHistory.UPDATE_NOTES + " TEXT", null, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

