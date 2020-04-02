package com.swctools.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.swctools.R;
import com.swctools.util.JSONConfigData;

import java.io.InputStream;

import javax.json.JsonObject;
import javax.json.JsonValue;

public class DatabaseStandingData {
    //Created to move all this functionality out of the DBSQLiteHelper class
    private SQLiteDatabase db;
    private Context context;

    public DatabaseStandingData(SQLiteDatabase db, Context context) {
        this.db = db;
        this.context = context;
    }

    public void executePopulation() {
        String[] deleteTbls = {DatabaseContracts.Planets.TABLE_NAME,
                DatabaseContracts.TroopTable.TABLE_NAME,
                DatabaseContracts.EquipmentTable.TABLE_NAME,
                DatabaseContracts.SWC_HTTP_HEADERS.TABLE_NAME,
                DatabaseContracts.Buildings.TABLE_NAME};
        try {

            for (int i = 0; i < deleteTbls.length; i++) {
                db.delete(deleteTbls[i], null, null);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        populatePlanets(db);
        populateTroops(db);
        populateEquipment(db);
        populateHeaders(db);
        populateBuildings(db);
    }

    private void populateBuildings(SQLiteDatabase db) {
        InputStream is = context.getResources().openRawResource(R.raw.buildings);
        ;
        JSONConfigData jsonConfigData = new JSONConfigData(is);

        ContentValues values = new ContentValues();
        for (JsonValue jsonValue : jsonConfigData.get_data()) {
            try {
                JsonObject obj = (JsonObject) jsonValue;
                values.put(DatabaseContracts.Buildings.GAME_NAME, obj.getString(DatabaseContracts.Buildings.GAME_NAME));
                values.put(DatabaseContracts.Buildings.UI_NAME, obj.getString(DatabaseContracts.Buildings.UI_NAME));
                values.put(DatabaseContracts.Buildings.FACTION, obj.getString(DatabaseContracts.Buildings.FACTION));
                values.put(DatabaseContracts.Buildings.CAPACITY, obj.getInt(DatabaseContracts.Buildings.CAPACITY));
                values.put(DatabaseContracts.Buildings.ISTRAP, obj.getBoolean(DatabaseContracts.Buildings.ISTRAP));

                db.insert(DatabaseContracts.Buildings.TABLE_NAME, null, values);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
            }
        }
    }

    private void populateHeaders(SQLiteDatabase db) {
        InputStream is = context.getResources().openRawResource(R.raw.headers);
        ;
        JSONConfigData jsonConfigData = new JSONConfigData(is);

        ContentValues values = new ContentValues();
        for (JsonValue jsonValue : jsonConfigData.get_data()) {
            try {
                JsonObject obj = (JsonObject) jsonValue;
                values.put(DatabaseContracts.SWC_HTTP_HEADERS.HEADER_GROUP, obj.getString(DatabaseContracts.SWC_HTTP_HEADERS.HEADER_GROUP));
                values.put(DatabaseContracts.SWC_HTTP_HEADERS.KEY, obj.getString(DatabaseContracts.SWC_HTTP_HEADERS.KEY));
                values.put(DatabaseContracts.SWC_HTTP_HEADERS.VALUE, obj.getString(DatabaseContracts.SWC_HTTP_HEADERS.VALUE));
                db.insert(DatabaseContracts.SWC_HTTP_HEADERS.TABLE_NAME, null, values);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
            }
        }
    }

    private void populatePlanets(SQLiteDatabase db) {
        InputStream is = context.getResources().openRawResource(R.raw.planets);
        ;
        JSONConfigData jsonConfigData = new JSONConfigData(is);

        ContentValues values = new ContentValues();
        for (JsonValue jsonValue : jsonConfigData.get_data()) {
            JsonObject obj = (JsonObject) jsonValue;
            values.put(DatabaseContracts.Planets.GAME_NAME, obj.getString(DatabaseContracts.Planets.GAME_NAME));
            values.put(DatabaseContracts.Planets.UI_NAME, obj.getString(DatabaseContracts.Planets.UI_NAME));
            values.put(DatabaseContracts.Planets.TYPE, obj.getString(DatabaseContracts.Planets.TYPE));
            db.insert(DatabaseContracts.Planets.TABLE_NAME, null, values);
        }
    }

    public void populateTroops(SQLiteDatabase db) {

        InputStream is = context.getResources().openRawResource(R.raw.troop_data);

        JSONConfigData jsonConfigData = new JSONConfigData(is);

        ContentValues values = new ContentValues();
        for (JsonValue jsonValue : jsonConfigData.get_data()) {
            JsonObject obj = (JsonObject) jsonValue;
            values.put(DatabaseContracts.TroopTable.GAME_NAME, obj.getString(DatabaseContracts.TroopTable.GAME_NAME));
            values.put(DatabaseContracts.TroopTable.UI_NAME, obj.getString(DatabaseContracts.TroopTable.UI_NAME));
            values.put(DatabaseContracts.TroopTable.FACTION, obj.getString(DatabaseContracts.TroopTable.FACTION));
            values.put(DatabaseContracts.TroopTable.CAPACITY, obj.getInt(DatabaseContracts.TroopTable.CAPACITY));
            db.insert(DatabaseContracts.TroopTable.TABLE_NAME, null, values);
        }
    }

    public void populateEquipment(SQLiteDatabase db) {

        InputStream is = context.getResources().openRawResource(R.raw.equipment_data);
        ;
        JSONConfigData jsonConfigData = new JSONConfigData(is);

        ContentValues values = new ContentValues();
        for (JsonValue jsonValue : jsonConfigData.get_data()) {
            JsonObject obj = (JsonObject) jsonValue;
            values.put(DatabaseContracts.EquipmentTable.GAME_NAME, obj.getString(DatabaseContracts.EquipmentTable.GAME_NAME));
            values.put(DatabaseContracts.EquipmentTable.UI_NAME, obj.getString(DatabaseContracts.EquipmentTable.UI_NAME));
            values.put(DatabaseContracts.EquipmentTable.FACTION, obj.getString(DatabaseContracts.EquipmentTable.FACTION));
            values.put(DatabaseContracts.EquipmentTable.AVAILABLE_ON, obj.getString(DatabaseContracts.EquipmentTable.AVAILABLE_ON));
            values.put(DatabaseContracts.EquipmentTable.CAPACITY, obj.getInt(DatabaseContracts.EquipmentTable.CAPACITY));
            db.insert(DatabaseContracts.EquipmentTable.TABLE_NAME, null, values);
        }
    }

}