package com.swctools.database.database_updates;

import android.content.ContentValues;
import android.content.Context;

import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;
import com.swctools.common.helpers.AppUpdateHelper;

public class DB58 extends DatabaseVersionUpdate {
    public DB58(int newVersion, Context context) {
        super(newVersion, context);
    }

    @Override
    public void executeUpdates() {

        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseContracts.LayoutTable.LAYOUT_IMAGE, "");
            DBSQLiteHelper.updateData(DatabaseContracts.LayoutTable.TABLE_NAME, contentValues, context);
            AppUpdateHelper.logNewDbUpgradeResult( newVersion, "DB58", "Cleaned layout image ", context);
        } catch (Exception e) {
            AppUpdateHelper.logNewDbUpgradeResult(newVersion, "DB58", e.getMessage(), context);

        }
    }
}
