package com.swctools.activity_modules.logs.models;

import android.content.Context;
import android.database.Cursor;

import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;

import java.util.ArrayList;

public class SWCMessageLog_ListProvider {

    public static ArrayList<SWCMessageLogModel> getSWCMessageLogList(Context mContext) {
        ArrayList<SWCMessageLogModel> swcMessageLogModels = new ArrayList<>();
        String sortBy = DatabaseContracts.SWCMessageLog.COLUMN_ID + " DESC";
        Cursor cursor = DBSQLiteHelper.queryDB(DatabaseContracts.SWCMessageLog.TABLE_NAME, null, null, null, null, null, sortBy, null, mContext);
        try {
            while (cursor.moveToNext()) {
                String f = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.SWCMessageLog.FUNCTION));
                String m = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.SWCMessageLog.MESSAGE));
                String r = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.SWCMessageLog.RESPONSE));
                int ts = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.SWCMessageLog.DATELOGGED));
                swcMessageLogModels.add(new SWCMessageLogModel(f, m, r, ts));
            }
        }finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return swcMessageLogModels;
    }

}
