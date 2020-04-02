package com.swctools.database.database_updates;

import android.content.Context;

import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;
import com.swctools.common.helpers.AppUpdateHelper;

public class DB54 extends DatabaseVersionUpdate {
    private static final String TAG = "DB54";

    public DB54(int newVersion, Context context) {
        super(newVersion, context);
    }

    @Override
    public void executeUpdates() {
        try {
            DBSQLiteHelper.deleteDbRows(DatabaseContracts.LayoutLog.TABLE_NAME, null, null, context);
            AppUpdateHelper.logNewDbUpgradeResult(newVersion, "DB55executeUpdates", "Success", context);
        } catch (Exception e) {
            AppUpdateHelper.logNewDbUpgradeResult(newVersion, "DB55executeUpdates", e.getMessage(), context);
        }
    }
}
