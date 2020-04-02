package com.swctools.util;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;
import com.swctools.layouts.LayoutHelper;
import com.swctools.activity_modules.layout_manager.models.LayoutManagerListProvider;
import com.swctools.layouts.models.LayoutRecord;

public class Tests {
    private static final String TAG = "TESTS";

    public static void deleteLayouts(Context context) {
        DBSQLiteHelper.deleteDbRows(DatabaseContracts.LayoutTable.TABLE_NAME, null, null, context);
        DBSQLiteHelper.deleteDbRows(DatabaseContracts.LayoutVersions.TABLE_NAME, null, null, context);
        DBSQLiteHelper.deleteDbRows(DatabaseContracts.LayoutTagAssignmentTable.TABLE_NAME, null, null, context);
        DBSQLiteHelper.deleteDbRows(DatabaseContracts.LayoutImages.TABLE_NAME, null, null, context);
    }

    public static void fillLayouts(int noLayouts, Context context) {
        Cursor cursor = DBSQLiteHelper.queryDB(DatabaseContracts.LayoutTable.TABLE_NAME, context);
        cursor.moveToFirst();
        int layoutId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutTable.COLUMN_ID));
        cursor.close();

        LayoutRecord layoutRecord = LayoutManagerListProvider.getLayoutRecord(layoutId, context);

        for (int i = 2; i < noLayouts; i++) {
            layoutRecord.setLayoutName("AutoFill " + " " + i);
            LayoutHelper.saveNewLayout(layoutRecord.getLayoutName(), layoutRecord.getLayoutPlayerId(), "{}", layoutRecord.getLayoutFaction(), layoutRecord.getLayoutTags(), 0, null, context);
        }
    }
}
