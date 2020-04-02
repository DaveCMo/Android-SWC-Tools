package com.swctools.database.database_updates;

import android.content.Context;

import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;

public class DB35 {
    private static final String TAG = "DB35";
    private Context context;

    public DB35(Context context) {
        this.context = context;
    }

    public void executeUpdates() {
        try {
            DBSQLiteHelper.rawQuery("ALTER TABLE " + DatabaseContracts.ReleaseLog.TABLE_NAME + " ADD COLUMN " + DatabaseContracts.ReleaseLog.VERSION_CODE + " TEXT", null, context);
            DBSQLiteHelper.rawQuery("ALTER TABLE " + DatabaseContracts.ReleaseLog.TABLE_NAME + " ADD COLUMN " + DatabaseContracts.ReleaseLog.READ + " TEXT", null, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

