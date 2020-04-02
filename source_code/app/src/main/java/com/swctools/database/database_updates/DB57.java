package com.swctools.database.database_updates;

import android.content.Context;
import android.database.SQLException;

import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;
import com.swctools.common.helpers.AppUpdateHelper;

public class DB57 extends DatabaseVersionUpdate {

    public DB57(int newVersion, Context context) {
        super(newVersion, context);


    }

    @Override
    public void executeUpdates() {
        try {
            DBSQLiteHelper.rawQuery("ALTER TABLE " + DatabaseContracts.LayoutTable.TABLE_NAME + " ADD COLUMN " + DatabaseContracts.LayoutTable.LAYOUT_FOLDER_ID + " INTEGER DEFAULT 0", null, context);
            try {
                DBSQLiteHelper.rawQuery("DROP TABLE IF EXISTS layout_folder_assignment ", null, context);
            } catch (SQLException e) {
            }
            AppUpdateHelper.logNewDbUpgradeResult(newVersion, "DB57", "New Layout Folder column added" , context);
        } catch (Exception e) {
            AppUpdateHelper.logNewDbUpgradeResult(newVersion, "DB57", e.getMessage(), context);
        }

    }
}
