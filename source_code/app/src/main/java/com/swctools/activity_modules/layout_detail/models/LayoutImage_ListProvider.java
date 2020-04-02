package com.swctools.activity_modules.layout_detail.models;

import android.content.Context;
import android.database.Cursor;

import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;

import java.util.ArrayList;

public class LayoutImage_ListProvider {


    public static ArrayList<LayoutDetail_ImageListItem> getLayoutDetail_imageListItems(int layoutId, Context context) {
        ArrayList<LayoutDetail_ImageListItem> arrayList = new ArrayList<>();
        String whereClause = DatabaseContracts.LayoutImages.LAYOUT_ID + " = ?";
        String[] whereArgs = {String.valueOf(layoutId)};
        Cursor cursor = DBSQLiteHelper.queryDB(DatabaseContracts.LayoutImages.TABLE_NAME, null, whereClause, whereArgs, context);
        try {
            while (cursor.moveToNext()) {
                String b = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutImages.IMAGE_LOCATION));
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutImages.COLUMN_ID));
                String label = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutImages.IMAGE_LABEL));
                arrayList.add(new LayoutDetail_ImageListItem(id, b, false, label));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return arrayList;
    }


}
