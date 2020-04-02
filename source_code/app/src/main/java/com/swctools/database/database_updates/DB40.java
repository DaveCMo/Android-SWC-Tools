package com.swctools.database.database_updates;

import android.content.Context;

import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;

public class DB40 {
    private static final String TAG = "DB40";
    private Context context;

    public DB40(Context context) {
        this.context = context;
    }

    public void executeUpdates() {
        try {
            DBSQLiteHelper.rawQuery(DatabaseContracts.LayoutLog.CREATE_TABLE, null, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

