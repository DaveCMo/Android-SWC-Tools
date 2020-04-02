package com.swctools.common.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import com.swctools.config.AppConfig;
import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;
import com.swctools.util.SaveJsonFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

import androidx.annotation.NonNull;

public class AppLoggerHelper {
    public static final String TAG = "AppLoggerHelper";

    public static void clearLog(Context context) {
        try {

            DBSQLiteHelper.deleteDbRows(DatabaseContracts.AppLog.TABLE_NAME, null, null, context);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    public static void logStuff(String function, String logData, Context context) {
        try {
            ContentValues contentValues = new ContentValues();
            long addedDate = (DateTimeHelper.userIDTimeStamp() / 1000);
            contentValues.put(DatabaseContracts.AppLog.FUNCTION, function);
            contentValues.put(DatabaseContracts.AppLog.LOG, logData);
            contentValues.put(DatabaseContracts.AppLog.LOG_DATE, addedDate);

            DBSQLiteHelper.insertData(DatabaseContracts.AppLog.TABLE_NAME, contentValues, context);

        } catch (Exception e) {

        } finally {
        }
    }

    public static void outPutLog(Context context) {
        try {
            JsonArray jsonArray = getLogJson(context);
            long addedDate = (DateTimeHelper.userIDTimeStamp() / 1000);
            String fileName = "LogData_" + addedDate;
            AppConfig appConfig = new AppConfig(context);
            SaveJsonFile.saveJsonFile(jsonArray.toString(), fileName, appConfig.layoutManagerExportFolder(), ".json");
            Toast.makeText(context, "Log file created!", Toast.LENGTH_LONG).show();
        } catch (IllegalArgumentException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();

        }
    }

    @NonNull
    public static JsonArray getLogJson(Context context) {

        Cursor cursor = DBSQLiteHelper.queryDB(DatabaseContracts.AppLog.TABLE_NAME, context);
        JsonArrayBuilder logBuilder;
        try {
            logBuilder = Json.createArrayBuilder();
            while (cursor.moveToNext()) {
                JsonObjectBuilder logRow = Json.createObjectBuilder();
                logRow.add(DatabaseContracts.AppLog.COLUMN_ID, cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.AppLog.COLUMN_ID)));
                logRow.add(DatabaseContracts.AppLog.FUNCTION, cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.AppLog.FUNCTION)));
                JsonArrayBuilder data = Json.createArrayBuilder();
                data.add(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.AppLog.LOG)));
                logRow.add(DatabaseContracts.AppLog.LOG, data.build());
                logRow.add(DatabaseContracts.AppLog.LOG_DATE, cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.AppLog.LOG_DATE)));

                logBuilder.add(logRow.build());

            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        JsonArray jsonArray = logBuilder.build();
        return jsonArray;
    }

    public static void startTableInfo(Context context) {
        String dbName = context.getDatabasePath(DBSQLiteHelper.DATABASE_NAME).toString();
        BuildTableTask buildTableTask = new BuildTableTask(dbName, context);
        buildTableTask.execute();

    }

    public static JsonArray getAppTableInfo(Context context) {
        JsonArrayBuilder tableArray = Json.createArrayBuilder();


        List<String> tableList = new ArrayList<>();

        Cursor c = DBSQLiteHelper.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null, context);
        try {
            if (c.moveToFirst()) {
                while (!c.isAfterLast()) {
                    tableList.add(c.getString(0));
                }
            }
        } finally {
            if (c != null) {
                c.close();
            }
        }


        for (String tableName : tableList) {
            JsonObjectBuilder tableObj = Json.createObjectBuilder();
            tableObj.add("table", tableName);
            Cursor rowCursor = DBSQLiteHelper.queryDB(tableName, context);

            try {
                rowCursor.moveToFirst();
                JsonArrayBuilder fieldArray = Json.createArrayBuilder();
                for (int i = 0; i < rowCursor.getColumnNames().length; i++) {
                    JsonObjectBuilder fieldObj = Json.createObjectBuilder();
                    fieldObj.add("field", rowCursor.getColumnName(i));
                    fieldObj.add("type", rowCursor.getType(i));
                    fieldArray.add(fieldObj.build());
                }
                tableObj.add("fields", fieldArray.build());

                tableArray.add(tableObj.build());
            } finally {
                if (rowCursor != null) {
                    rowCursor.close();
                }
            }

        }
        return tableArray.build();
    }

    public static class BuildTableTask extends AsyncTask<Void, Void, Void> {
        Context context;
        String dbPath;

        public BuildTableTask(String dbPath, Context context) {
            this.dbPath = dbPath;
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            JsonArrayBuilder tableArray = Json.createArrayBuilder();


            List<String> tableList = new ArrayList<>();
            tableList.add(DatabaseContracts.LayoutTable.TABLE_NAME);
            tableList.add(DatabaseContracts.LayoutFolders.TABLE_NAME);
            tableList.add(DatabaseContracts.LayoutVersions.TABLE_NAME);

            for (String tableName : tableList) {
                JsonObjectBuilder tableObj = Json.createObjectBuilder();
                tableObj.add("table", tableName);
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                Cursor rowCursor = DBSQLiteHelper.queryDB(tableName, context);

                try {
                    rowCursor.moveToFirst();
                    JsonArrayBuilder fieldArray = Json.createArrayBuilder();
                    for (int i = 0; i < rowCursor.getColumnNames().length; i++) {
                        JsonObjectBuilder fieldObj = Json.createObjectBuilder();
                        fieldObj.add("field", rowCursor.getColumnName(i));
                        fieldObj.add("type", rowCursor.getType(i));
                        fieldArray.add(fieldObj.build());
                    }
                    tableObj.add("fields", fieldArray.build());

                    tableArray.add(tableObj.build());
                    rowCursor.moveToLast();
                    rowCursor.close();
                } finally {
                    if (rowCursor != null) {
                        rowCursor.close();
                    }
                }

            }
            AppConfig appConfig = new AppConfig(context);
            SaveJsonFile.saveJsonFile(tableArray.build().toString(), "Layout Folders", appConfig.layoutManagerExportFolder(), ".json");

            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            FileChannel source = null;
            FileChannel destination = null;
            String currentDBPath = dbPath;
            String backupDBPath = "swc_tools_database";
            File currentDB = new File(data, currentDBPath);
            File backupDB = new File(sd, backupDBPath);
            try {
                source = new FileInputStream(currentDB).getChannel();
                destination = new FileOutputStream(backupDB).getChannel();
                destination.transferFrom(source, 0, source.size());
                source.close();
                destination.close();
//                Toast.makeText(this, "DB Exported!", Toast.LENGTH_LONG).show();

            } catch (IOException e) {
                e.printStackTrace();
            }

            //            Toast.makeText(context, "Table details exported", Toast.LENGTH_LONG).show();
            return null;
        }
    }
}
