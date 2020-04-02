package com.swctools.common.model_list_providers;

import android.content.Context;
import android.database.Cursor;

import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;
import com.swctools.activity_modules.multi_image_picker.models.SelectedImageModel;

import java.util.ArrayList;

public class LayoutImageBytesListProvider {
    private static final String TAG = "LytImgBytesListPrvdr";

    public static ArrayList<byte[]> getTmpImageBytesList(Context context) {
        ArrayList<byte[]> arrayList = new ArrayList<>();

        Cursor cursor = DBSQLiteHelper.queryDB(DatabaseContracts.LayoutImagesTmp.TABLE_NAME, context);
        try {
            while (cursor.moveToNext()) {
                byte[] b = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutImagesTmp.IMAGE_BLOB));
                arrayList.add(b);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return arrayList;
    }


    public static ArrayList<SelectedImageModel> getTmpImageSelectedModelList(Context context) {
        ArrayList<SelectedImageModel> arrayList = new ArrayList<>();

        Cursor cursor = DBSQLiteHelper.queryDB(DatabaseContracts.LayoutImagesTmp.TABLE_NAME, context);
        try {
            while (cursor.moveToNext()) {
                byte[] b = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutImagesTmp.IMAGE_BLOB));
                long id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutImagesTmp.COLUMN_ID));
                String s = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutImagesTmp.IMAGE_LABEL));
                arrayList.add(new SelectedImageModel(b, id, false, s));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return arrayList;
    }


    public static ArrayList<byte[]> getSavedImageBytesList(int layoutId, Context context) {
        ArrayList<byte[]> arrayList = new ArrayList<>();
        String whereClause = DatabaseContracts.LayoutImages.LAYOUT_ID + " = ?";
        String[] whereArgs = {String.valueOf(layoutId)};
        Cursor cursor = DBSQLiteHelper.queryDB(DatabaseContracts.LayoutImages.TABLE_NAME, null, whereClause, whereArgs, context);
        try {
            while (cursor.moveToNext()) {
                byte[] b = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutImages.IMAGE_BLOB));
                arrayList.add(b);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return arrayList;
    }

    public static ArrayList<SelectedImageModel> getSavedImageSelectedModelList(int layoutId, Context context) {
        ArrayList<SelectedImageModel> arrayList = new ArrayList<>();
        String whereClause = DatabaseContracts.LayoutImages.LAYOUT_ID + " = ?";
        String[] whereArgs = {String.valueOf(layoutId)};
        Cursor cursor = DBSQLiteHelper.queryDB(DatabaseContracts.LayoutImages.TABLE_NAME, null, whereClause, whereArgs, context);
        try {
            while (cursor.moveToNext()) {
                byte[] b = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutImages.IMAGE_BLOB));
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutImages.COLUMN_ID));
                String label = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutImages.IMAGE_LABEL));
                arrayList.add(new SelectedImageModel(b, id, false, label));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return arrayList;
    }


    public static ArrayList<SelectedImageModel> getSavedImageSelectedModelList(Context context) {
        ArrayList<SelectedImageModel> arrayList = new ArrayList<>();

        Cursor cursor = DBSQLiteHelper.queryDB(DatabaseContracts.LayoutImages.TABLE_NAME, context);
        try {
            while (cursor.moveToNext()) {
                byte[] b = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutImages.IMAGE_BLOB));
                long id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutImages.COLUMN_ID));
                String s = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutImages.IMAGE_LABEL));
                arrayList.add(new SelectedImageModel(b, id, false, s));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return arrayList;
    }


}
