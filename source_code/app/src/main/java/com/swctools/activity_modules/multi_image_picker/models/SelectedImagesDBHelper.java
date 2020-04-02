package com.swctools.activity_modules.multi_image_picker.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;
import com.swctools.util.StringUtil;

import java.util.ArrayList;

public class SelectedImagesDBHelper {
    private static final String TAG = "SelectedImagesDBHelper";

    public static void fillWithImages(String idColumn, int id, String table, String tableImageColumn, Context context) {
        String whereClause = idColumn + " = ?";
        String[] whereArg = {String.valueOf(id)};
        Cursor cursor = DBSQLiteHelper.queryDB(table, null, whereClause, whereArg, context);
        try {
            while (cursor.moveToNext()) {
                byte[] b = cursor.getBlob(cursor.getColumnIndexOrThrow(tableImageColumn));
                addRecord(b, false, context);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


    public static ArrayList<SelectedImageModel> selectedImageModelArrayList(Context context) {
        ArrayList<SelectedImageModel> selectedImageModels = new ArrayList<>();

        Cursor cursor = DBSQLiteHelper.queryDB(DatabaseContracts.TmpImageSelection.TABLE_NAME, context);
        try {
            while (cursor.moveToNext()) {
                int i = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.TmpImageSelection.IMAGE_NUMBER));
                byte[] bytes = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseContracts.TmpImageSelection.IMAGE_BLOB));
                String bStr = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.TmpImageSelection.IMAGE_SELECTED));

                boolean b;
                if (bStr.equalsIgnoreCase("1")) {
                    b = true;
                } else {
                    b = false;
                }
                String l = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.TmpImageSelection.IMAGE_LABEL));
                if (!StringUtil.isStringNotNull(l)) {
                    l = "";
                }

                selectedImageModels.add(new SelectedImageModel(bytes, i, b, l));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return selectedImageModels;
    }

    public static void clearTmp(Context context) {
        DBSQLiteHelper.deleteDbRows(DatabaseContracts.TmpImageSelection.TABLE_NAME, null, null, context);
    }

    public static long addRecord(byte[] bytes, boolean selected, Context context) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseContracts.TmpImageSelection.IMAGE_BLOB, bytes);
        contentValues.put(DatabaseContracts.TmpImageSelection.IMAGE_SELECTED, selected);
        return DBSQLiteHelper.insertData(DatabaseContracts.TmpImageSelection.TABLE_NAME, contentValues, context);
    }

    public static void updateSelected(SelectedImageModel selectedImageModel, Context context) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContracts.TmpImageSelection.IMAGE_SELECTED, selectedImageModel.selected);

        String whereClause = DatabaseContracts.TmpImageSelection.IMAGE_NUMBER + " = ?";
        String[] whereArgs = {String.valueOf(selectedImageModel.no)};
        long no = DBSQLiteHelper.updateData(DatabaseContracts.TmpImageSelection.TABLE_NAME, contentValues, whereClause, whereArgs, context);

    }
    public static void updateLabel(SelectedImageModel selectedImageModel, Context context) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContracts.TmpImageSelection.IMAGE_LABEL, selectedImageModel.label);

        String whereClause = DatabaseContracts.TmpImageSelection.IMAGE_NUMBER + " = ?";
        String[] whereArgs = {String.valueOf(selectedImageModel.no)};
        long no = DBSQLiteHelper.updateData(DatabaseContracts.TmpImageSelection.TABLE_NAME, contentValues, whereClause, whereArgs, context);

    }
    public static void deleteSelectedImage(SelectedImageModel selectedImageModel, Context
            context) {
        String whereClause = DatabaseContracts.TmpImageSelection.IMAGE_NUMBER + " = ?";
        String whereArgs = String.valueOf(selectedImageModel.no);
        DBSQLiteHelper.deleteDbRow(DatabaseContracts.TmpImageSelection.TABLE_NAME, whereClause, whereArgs, context);
    }

}
