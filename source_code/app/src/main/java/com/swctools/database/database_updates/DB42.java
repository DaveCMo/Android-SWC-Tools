package com.swctools.database.database_updates;

import android.content.Context;

import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;

public class DB42 {
    private static final String TAG = "DB42";
    private Context context;

    public DB42(Context context) {
        this.context = context;
    }

    public void executeUpdates() {
        try {
            DBSQLiteHelper.rawQuery("ALTER TABLE " + DatabaseContracts.LayoutLog.TABLE_NAME + " ADD COLUMN " + DatabaseContracts.LayoutLog.LAYOUT_VERSION + " INTEGER", null, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

