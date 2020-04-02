package com.swctools.activity_modules.updates;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;

import com.swctools.activity_modules.main.MainActivity;
import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;
import com.swctools.common.enums.BundleKeys;
import com.swctools.common.helpers.DateTimeHelper;
import com.swctools.common.http.DownloadHttp;
import com.swctools.common.notifications.NotificationInterface;
import com.swctools.common.notifications.NotificationSender;
import com.swctools.util.JSONConfigData;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;

public class DataUpdateHandler {
    private static final String TAG = "DataUpdateHandler";
    private JsonArray tableArray;
    private Context context;
    private boolean silentUpdate;
    private int notificationId = 0;
    private ArrayList<DataTablesDetails> dataTablesDetails;
    private VersionJSON versionJSON;

    public DataUpdateHandler(VersionJSON versionJSON, boolean silentUpdate, Context context) {
        this.versionJSON = versionJSON;
        dataTablesDetails = new ArrayList<>();
        this.silentUpdate = silentUpdate;
        tableArray = versionJSON.getDataTables();
        this.context = context;


    }

    public ArrayList<DataTablesDetails> getDataTablesDetails() {
        return dataTablesDetails;
    }

    public void dataUpdateHandlerExecuteNotificationUpdate() {
        processTables();
//        ContentValues contentValues = new ContentValues();
////        contentValues.put(DatabaseContracts.DataVersion.APP_TABLE, DatabaseContracts.TroopTable.TABLE_NAME);
//        contentValues.put(DatabaseContracts.DataVersion.APP_TABLE, DatabaseContracts.DataVersion.TABLE_NAME);
//        contentValues.put(DatabaseContracts.DataVersion.APP_TABLE_VERSION, 2);

        checkAppVersion(versionJSON);
    }

    public void checkAppVersion(VersionJSON versionJSON) {
        String versionName = "";
        int versionCode = -1;
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;
            versionCode = packageInfo.versionCode;

            int latestVersion = versionJSON.getAppUpdate().getInt("latestVersion");
            String releaseLabel = versionJSON.getAppUpdate().getString("releaseLabel");
            String releaseURl = versionJSON.getAppUpdate().getString("release_url");

            if (latestVersion > versionCode) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(releaseURl));
//                showDataUpdateNotification("Application Update Available!", releaseLabel + " available. Tap here to get it!", browserIntent);
                NotificationSender notificationSender = new NotificationSender(context);
                notificationSender.showSimpleNotification("Application Update Available!", releaseLabel + " available. Tap here to get it!", browserIntent, NotificationInterface.APP_UPDATE_AVAILABLE);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

        }
    }

    public void processTables() {


        //Get a list of the tables...
        setDataTablesDetailsList();

        if (dataTablesDetails.size() > 0) {

            Intent intent = new Intent(context, UpdateDataActivity.class);
            intent.putParcelableArrayListExtra(BundleKeys.DATA_TABLES_TO_UPDATE.toString(), dataTablesDetails);
            NotificationSender notificationSender = new NotificationSender(context);
            notificationSender.showPersistentNotification("Data Updates available!", "There are " + dataTablesDetails.size() + " updates available. Tap to begin.", intent, NotificationInterface.DATA_UPDATE_AVAILABLE);
        } else {
            if (!silentUpdate) {
                Intent intent = new Intent(context, MainActivity.class);
                showDataUpdateNotification("No data updates found", "You already have the latest data in your app :)", intent);
            } else {

            }
        }

    }

    public void setDataTablesDetailsList() {
        for (JsonValue jsonValue : tableArray) {
            JsonObject tableObj = (JsonObject) jsonValue;
            String tableName = tableObj.getString("name");
            String tableUrl = tableObj.getString("url");
            int latestVers = tableObj.getInt("latestVersion");

            if (!versionExists(latestVers, tableName)) {
                dataTablesDetails.add(new DataTablesDetails(tableName, tableUrl, latestVers));
            }
        }
    }

    private void logUpdateFinished(String tableName, int tableVersion, String updateNotes) {
        ContentValues contentValues = new ContentValues();
        try {
            long addedDate = (DateTimeHelper.userIDTimeStamp() / 1000);
            contentValues.put(DatabaseContracts.DataVersionUpdateHistory.APP_TABLE, tableName);
            contentValues.put(DatabaseContracts.DataVersionUpdateHistory.APP_TABLE_VERSION, tableVersion);
            contentValues.put(DatabaseContracts.DataVersionUpdateHistory.UPDATE_NOTES, updateNotes);
            contentValues.put(DatabaseContracts.DataVersionUpdateHistory.APP_TABLE_UPDATED_ON, addedDate);
            try {
                long rowID = DBSQLiteHelper.insertData(DatabaseContracts.DataVersionUpdateHistory.TABLE_NAME, contentValues, context);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        contentValues.clear();

    }

    private void showDataUpdateNotification(String title, String message, Intent intent) {
        Intent resultIntent = new Intent(context, MainActivity.class);
        NotificationSender notificationSender = new NotificationSender(context);
        notificationSender.showSimpleNotification(title, message, resultIntent, NotificationInterface.DATA_UPDATED);
    }

    private void updateDataVersion(String tableName, int newVersion) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContracts.DataVersion.APP_TABLE_VERSION, newVersion);
        contentValues.put(DatabaseContracts.DataVersion.APP_TABLE, tableName);
        String whereClause = DatabaseContracts.DataVersion.APP_TABLE + " = ?";
        String[] whereArgs = {tableName};
        if (checkTableRecordExists(tableName)) {
            DBSQLiteHelper.updateData(DatabaseContracts.DataVersion.TABLE_NAME, contentValues, whereClause, whereArgs, context);
        } else {
            try {
                DBSQLiteHelper.insertData(DatabaseContracts.DataVersion.TABLE_NAME, contentValues, context);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private Boolean checkTableRecordExists(String tableName) {

        Cursor cursor = null;
        String[] columns = {
                DatabaseContracts.DataVersion.APP_TABLE,
                DatabaseContracts.DataVersion.APP_TABLE_VERSION};
        String selection =
                DatabaseContracts.DataVersion.APP_TABLE + " = ?";
        String[] selectionArgs = {tableName};

        try {

            cursor = DBSQLiteHelper.queryDB(DatabaseContracts.DataVersion.TABLE_NAME, columns, selection, selectionArgs, context);

            while (cursor.moveToNext()) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {

            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private Boolean versionExists(int versionId, String tableName) {

        Cursor cursor = null;
        String[] columns = {
                DatabaseContracts.DataVersionUpdateHistory.APP_TABLE,
                DatabaseContracts.DataVersionUpdateHistory.APP_TABLE_VERSION};
        String selection =
                DatabaseContracts.DataVersionUpdateHistory.APP_TABLE_VERSION +
                        " = ? AND " +
                        DatabaseContracts.DataVersionUpdateHistory.APP_TABLE + " = ?";
        String[] selectionArgs = {String.valueOf(versionId), tableName};

        try {

            cursor = DBSQLiteHelper.queryDB(DatabaseContracts.DataVersionUpdateHistory.TABLE_NAME, columns, selection, selectionArgs, context);
            while (cursor.moveToNext()) {
                return true;

            }
            return false;
        } catch (Exception e) {
            return false;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

    }

    private void processTableUpdate(String tableName, String method, String tableUrl) {

        ContentValues contentValues = new ContentValues();
        try {
            URL url = new URL(tableUrl);
            String jsonTableData = DownloadHttp.downloadUrl(url).replaceAll("\r?\n", "");

            if (method.equalsIgnoreCase(DataUpdateInterface.methods.REPLACE)) {
                DBSQLiteHelper.deleteDbRows(tableName, null, null, context);
            }
            JSONConfigData jsonConfigData = new JSONConfigData(jsonTableData);

            for (int i = 1; i < 11; i++) {
                for (JsonValue updateObject : jsonConfigData.get_data()) {
                    JsonObject obj = (JsonObject) updateObject;
                    for (Map.Entry<String, JsonValue> updateKeyValue : obj.entrySet()) {
                        String value = "";
                        if (updateKeyValue.getValue().getValueType().toString().equalsIgnoreCase("STRING")) {
                            value = obj.getString(updateKeyValue.getKey()).toString();
                        } else if (updateKeyValue.getValue().getValueType().toString().equalsIgnoreCase("NUMBER")) {
                            value = String.valueOf(obj.getInt(updateKeyValue.getKey()));
                        }
                        contentValues.put(updateKeyValue.getKey(), value);
                        contentValues.put("level", i);
                    }
                    try {

                        long insResult = DBSQLiteHelper.insertData(tableName, contentValues, context);

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {

                    }
                    contentValues.clear();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    interface DataUpdateInterface {
        interface methods {
            String REPLACE = "Replace";
            String ADD = "Add";
        }
    }


}
