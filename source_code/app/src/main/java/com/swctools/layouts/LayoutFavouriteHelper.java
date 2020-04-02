package com.swctools.layouts;

import android.content.Context;
import android.database.Cursor;

import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;
import com.swctools.layouts.models.LayoutRecord;

import java.util.ArrayList;

public class LayoutFavouriteHelper {


    public static ArrayList<LayoutRecord> getAllFavourites(Context context) {
        ArrayList<LayoutRecord> layoutRecords = new ArrayList<>();
        Cursor cursor = DBSQLiteHelper.queryDB(DatabaseContracts.LayoutTop.TABLE_NAME, context);
        try {
            while (cursor.moveToNext()){
    //LayoutRecord layoutRecord = new LayoutRecord()
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return layoutRecords;
    }
}
