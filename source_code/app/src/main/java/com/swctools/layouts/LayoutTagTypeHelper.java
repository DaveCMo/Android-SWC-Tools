package com.swctools.layouts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;
import com.swctools.util.MethodResult;
import com.swctools.util.StringUtil;
import com.swctools.layouts.models.LayoutTag;

import java.util.ArrayList;
import java.util.Arrays;

public class LayoutTagTypeHelper {
    private static final String TAG = "LayoutTagTypeHelper";

    public static MethodResult updateTag(int id, String newName, Context context) {
        MethodResult methodResponse;
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContracts.LayoutTags.LAYOUT_TAG, newName);
        String whereClause = DatabaseContracts.LayoutTags.COLUMN_ID + " = ? ";
        String[] whereArgs = {String.valueOf(id)};
        try {
            DBSQLiteHelper.updateData(DatabaseContracts.LayoutTags.TABLE_NAME, contentValues, whereClause, whereArgs, context);
            methodResponse = new MethodResult(true, "Updated row!");
        } catch (Exception e) {
            methodResponse = new MethodResult(false, e.getMessage());
        }
        return methodResponse;
    }

    public static ArrayList<LayoutTag> getLayoutList(ArrayList<LayoutTag> currentTags, Context mContext) {
        ArrayList<LayoutTag> layoutTagArrayList = new ArrayList<>();
        Cursor cursor = null;
        try {
            if (currentTags.size() > 0) {

                ArrayList<String> tagIDString = new ArrayList<String>();
                for (int i = 0; i < currentTags.size(); i++) {
                    tagIDString.add(currentTags.get(i).tagIdString());
                }

                Object[] tagIdObjs = tagIDString.toArray();

                String whereClause = DatabaseContracts.LayoutTags.COLUMN_ID + " NOT IN (" + StringUtil.getInsertQMs(currentTags.size()) + ")";
                String[] whereArgs = Arrays.copyOf(tagIdObjs, tagIdObjs.length, String[].class);

                cursor = DBSQLiteHelper.queryDB(DatabaseContracts.LayoutTags.TABLE_NAME, null, whereClause, whereArgs, mContext);
            } else {

                cursor = DBSQLiteHelper.queryDB(DatabaseContracts.LayoutTags.TABLE_NAME, mContext);
            }
            while (cursor.moveToNext()) {

                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutTags.COLUMN_ID));
                String tag = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutTags.LAYOUT_TAG));
                layoutTagArrayList.add(new LayoutTag(id, tag));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return layoutTagArrayList;

    }

}
