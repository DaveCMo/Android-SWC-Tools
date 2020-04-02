package com.swctools.database.database_updates;

import android.content.Context;

import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;

public class DB28 {
    private static final String TAG = "DB17";
    private Context context;

    public DB28(Context context) {
        this.context = context;
    }

    public void executeUpdates() {
        try {
            DBSQLiteHelper.rawQuery(DatabaseContracts.DataVersionUpdateHistory.CREATE_TABLE, null, context);
            DBSQLiteHelper.rawQuery(DatabaseContracts.DataVersion.CREATE_TABLE, null, context);
            DBSQLiteHelper.rawQuery(DatabaseContracts.ReleaseLog.CREATE_TABLE, null, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

