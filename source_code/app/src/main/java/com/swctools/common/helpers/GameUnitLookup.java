package com.swctools.common.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;

import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;

public class GameUnitLookup {
    private static final String TAG = "GameUnitLookup";
    private static final String PREF_NAME = "_gameunitlookup";
    public static final String GAME_UNIT_DATA_FORMAT = "%1$s|%2$s|%3$s";
    private static SharedPreferences pref;
    private static int PRIVATE_MODE = 0;
    private Context _context;
    private SharedPreferences.Editor editor;

    public GameUnitLookup(String prefName, Context context) {

        this._context = context;
        pref = context.getSharedPreferences(prefName, PRIVATE_MODE); // 0 - for private mode
        editor = pref.edit();
    }

    public void clearData() {
        editor.clear();
    }

    public void addKeyPair(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public String getValue(String key) {
        return pref.getString(key, null);
    }

    public void getSourceDataFromDB(String table, Context context) {
//        this.uiName = uiName;
//        this.gameName = gameName;
//        this.level = level;
//        this.capacity = capacity;
        String keyFormat = "%1$s%2$s";
        clearData();
        Cursor cursor = DBSQLiteHelper.queryDB(table, context);
        try {
            while (cursor.moveToNext()) {
                String gameUnit = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.GameUnitTable.GAME_NAME));
                int level = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.GameUnitTable.LEVEL));
                int cap = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.GameUnitTable.CAPACITY));
                String uiName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.GameUnitTable.UI_NAME));
                String key = String.format(keyFormat, gameUnit, String.valueOf(level));
                addKeyPair(key, String.format(GAME_UNIT_DATA_FORMAT, uiName, String.valueOf(level), String.valueOf(cap)));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
