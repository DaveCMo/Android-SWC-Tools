package com.swctools.activity_modules.EditLayoutJson.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;
import com.swctools.layouts.LayoutHelper;
import com.swctools.common.models.player_models.Building;
import com.swctools.common.models.player_models.MapBuildings;
import com.swctools.util.MethodResult;

import java.io.StringReader;
import java.util.ArrayList;

import javax.json.Json;
import javax.json.JsonArray;

public class EditLayoutJsonListProvider {
    private static final String TAG = "EditLaJsLProv";

    public static MethodResult populateLayoutJsonRecords(int layoutId, int layoutVersId, Context context) {
        //clear out all old data first.
        DBSQLiteHelper.deleteDbRows(DatabaseContracts.LayoutEditor.TABLE_NAME, null, null, context);
        String layoutJson = LayoutHelper.getLayoutJson(layoutId, layoutVersId, context);
        JsonArray jsonArray = Json.createReader(new StringReader(layoutJson)).readArray();
        MapBuildings mapBuildings = new MapBuildings(jsonArray);
        try {
            for (Building mapBuilding : mapBuildings.getBuildings()) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(DatabaseContracts.LayoutEditor.KEY, mapBuilding.getKey());
                contentValues.put(DatabaseContracts.LayoutEditor.UID, mapBuilding.getUid());
                contentValues.put(DatabaseContracts.LayoutEditor.X, mapBuilding.getX());
                contentValues.put(DatabaseContracts.LayoutEditor.Z, mapBuilding.getZ());
                DBSQLiteHelper.insertData(DatabaseContracts.LayoutEditor.TABLE_NAME, contentValues, context);

            }
        } catch (Exception e) {
            return new MethodResult(false, e.getMessage());
        }
        return new MethodResult(true, "Done");
    }

    public static ArrayList<EditBuilding> getListFromTable(Context context) {
        Cursor cursor = DBSQLiteHelper.queryDB(DatabaseContracts.LayoutEditor.TABLE_NAME, null, null, null, null, null, null, null, context);
        ArrayList<EditBuilding> buildingArrayList;
        try {
            buildingArrayList = new ArrayList<>();
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(DatabaseContracts.LayoutEditor._ID));
                String key = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutEditor.KEY));
                String uid = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutEditor.UID));
                int x = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutEditor.X));
                int z = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutEditor.Z));
                int edit = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutEditor.EDIT));
                EditBuilding building = new EditBuilding(id, key, uid, x, z, edit);
                buildingArrayList.add(building);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return buildingArrayList;
    }

    public static MethodResult deleteBuilding(int id, Context context) {
        String whereClause = DatabaseContracts.LayoutEditor.KEY + " = ?";
        String[] whereArg = {String.valueOf(id)};
        return new MethodResult(DBSQLiteHelper.deleteDbRows(DatabaseContracts.LayoutEditor.TABLE_NAME, whereClause, whereArg, context), "");
    }


    public static MethodResult updateBuilding(int id, String buildingKey, String uid, int x, int z, Context context) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContracts.LayoutEditor.UID, uid);
        contentValues.put(DatabaseContracts.LayoutEditor.X, x);
        contentValues.put(DatabaseContracts.LayoutEditor.Z, z);
        contentValues.put(DatabaseContracts.LayoutEditor.EDIT, 0);
        String whereClause = DatabaseContracts.LayoutEditor.COLUMN_ID + " = ?";
        String whereArg = String.valueOf(id);
        int countUpdated = DBSQLiteHelper.updateData(DatabaseContracts.LayoutEditor.TABLE_NAME, contentValues, whereClause, whereArg, context);

        if (countUpdated > 0) {
            return new MethodResult(true, "Updated!");
        } else {

            return new MethodResult(false, "");
        }
    }

    public static long addNewRow(Context context) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseContracts.LayoutEditor.UID, "");
        contentValues.put(DatabaseContracts.LayoutEditor.KEY, "");
        contentValues.put(DatabaseContracts.LayoutEditor.X, 0);
        contentValues.put(DatabaseContracts.LayoutEditor.Z, 0);
        contentValues.put(DatabaseContracts.LayoutEditor.EDIT, 1);

        long newId = DBSQLiteHelper.insertData(DatabaseContracts.LayoutEditor.TABLE_NAME, contentValues, context);
        return newId;
    }


}
