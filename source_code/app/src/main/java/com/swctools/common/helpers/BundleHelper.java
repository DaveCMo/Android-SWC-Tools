package com.swctools.common.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;

public class BundleHelper {
    private static final String TAG = "BundleHelper";
    public String _key;
    public String _value;

    public BundleHelper(String k, String v) {
        this._key = k;
        this._value = v;
    }

    public BundleHelper(String k) {
        this._key = k;
    }

    public void deleteBundleValue(Context context) {
        String whereClause = DatabaseContracts.BundledValues.BUNDLE_KEY + "=?";
        String[] whereArgs = {_key};
        DBSQLiteHelper.deleteDbRows(DatabaseContracts.BundledValues.TABLE_NAME, whereClause, whereArgs, context);
    }

    public void commit(Context context) {
        boolean commitResult = false;
        deleteBundleValue(context);
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContracts.BundledValues.BUNDLE_KEY, _key);
        contentValues.put(DatabaseContracts.BundledValues.BUNDLE_VALUE, _value);

        if (DBSQLiteHelper.insertReplaceData(DatabaseContracts.BundledValues.TABLE_NAME, contentValues, context) > 0) {
            commitResult = true;
        }

    }

    public String get_value(Context context) {
        Cursor cursor = null;
        String[] projection = {
                DatabaseContracts.BundledValues.BUNDLE_KEY,
                DatabaseContracts.BundledValues.BUNDLE_VALUE};
        String selection = DatabaseContracts.BundledValues.BUNDLE_KEY + " like ?";
        String[] selectionArgs = {_key};
        String result = "";
        try {

            cursor = DBSQLiteHelper.queryDB(DatabaseContracts.BundledValues.TABLE_NAME, projection, selection, selectionArgs, context);

            while (cursor.moveToNext()) {

                result = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.BundledValues.BUNDLE_VALUE));
                break;
            }
            cursor.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return result;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }


    }
}
