package com.swctools.activity_modules.updates;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;
import com.swctools.common.helpers.AppUpdateHelper;
import com.swctools.common.http.DownloadHttp;
import com.swctools.util.JSONConfigData;
import com.swctools.util.MethodResult;
import com.swctools.util.Utils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import javax.json.JsonObject;
import javax.json.JsonValue;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class UpdateProcessor extends Fragment {
    private static final String TAG = "UpdateProcessor";
    private static final String VERSIONINFO = "https://raw.githubusercontent.com/DaveCMo/Android-SWC-Tools/master/swc_version_info";

    private DataUpdateInterface activityCallBack;
    private Context mContext;
    private boolean isWorking;
    private ArrayList<DataTablesDetails> dataTablesDetails;
    private ProcessUpdatesTask processUpdatesTask;
    private boolean ifReWrite = true;

    public static UpdateProcessor getInstance(FragmentManager fragmentManager) {
        UpdateProcessor updateProcessor = (UpdateProcessor) fragmentManager.findFragmentByTag(UpdateProcessor.TAG);
        if (updateProcessor == null) {
            updateProcessor = new UpdateProcessor();
            fragmentManager.beginTransaction().add(updateProcessor, TAG).commit();
        }
        return updateProcessor;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstance) {
        super.onCreate(savedInstance);
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        activityCallBack = (DataUpdateInterface) mContext;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activityCallBack = null;
    }

    public void processUpdate(ArrayList<DataTablesDetails> dataTablesDetails) {
        if (!isWorking) {
            this.dataTablesDetails = dataTablesDetails;
            processUpdatesTask = new ProcessUpdatesTask();
            processUpdatesTask.execute("");
        }
    }

    public void cancelUpdate() {
        if (isWorking) {
            processUpdatesTask.cancel(true);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public class ProcessUpdatesTask extends AsyncTask<String, String, MethodResult> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(MethodResult o) {
            activityCallBack.updateCompleted(o);
            isWorking = false;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            activityCallBack.postUpdateToActivity(values[0]);

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            isWorking = false;
            activityCallBack.updateCompleted(new MethodResult(false, "Process cancelled!"));
        }

        @Override
        protected MethodResult doInBackground(String... strings) {
            isWorking = true;


            MethodResult updateResult = new MethodResult(true);
            try {
                URL versionInfoURL = new URL(VERSIONINFO);
                String versionInfoStr = DownloadHttp.downloadUrl(versionInfoURL);
                VersionJSON versionJSON = new VersionJSON(versionInfoStr);
                DBSQLiteHelper.deleteDbRows(DatabaseContracts.TroopBaseData.TABLE_NAME, null, null, mContext);
                DBSQLiteHelper.deleteDbRows(DatabaseContracts.BuildingBaseData.TABLE_NAME, null, null, mContext);
                ArrayList<ContentValues> tableData = new ArrayList<>();
                for (DataTablesDetails dataTablesDetail : dataTablesDetails) {
                    int repeatNo = 0;
                    try {
                        URL url = new URL(dataTablesDetail.dataUrl);
                        String updateNotes = "";
                        try {
                            activityCallBack.postUpdateToActivity("Downloading table data...");
                            String jsonTableData = DownloadHttp.downloadUrl(url).replaceAll("\r?\n", "");
                            JSONConfigData jsonConfigData = new JSONConfigData(jsonTableData);
                            updateNotes = jsonConfigData.getNotesArray().toString();
                            activityCallBack.postUpdateToActivity("Processing new data...");
                            try {
                                //Delete old data:
                                DBSQLiteHelper.deleteDbRows(dataTablesDetail.tableName, null, null, mContext);
                                //now get column names we are going to fetch from the dataset...
//                                Cursor cursor = DBSQLiteHelper.queryDB(dataTablesDetail.tableName, null, null, null, null, null, null, "1", mContext);

                                int dataCounter = 1;
                                for (JsonValue updateObject : jsonConfigData.get_data()) { // For each object in the array:
                                    JsonObject obj = (JsonObject) updateObject;
                                    Log.d(TAG, "doInBackground: "+ obj.toString());
                                    ContentValues data;
                                    if (dataTablesDetail.tableName.equalsIgnoreCase(DatabaseContracts.TroopTable.TABLE_NAME)) {


                                        ContentValues baseDataValues = new ContentValues();
                                        baseDataValues.put(DatabaseContracts.TroopBaseData.CAPACITY, obj.getString(DatabaseContracts.TroopBaseData.CAPACITY));
                                        baseDataValues.put(DatabaseContracts.TroopBaseData.FACTION, obj.getString(DatabaseContracts.TroopBaseData.FACTION));
                                        baseDataValues.put(DatabaseContracts.TroopBaseData.GAME_NAME, obj.getString(DatabaseContracts.TroopBaseData.GAME_NAME));
                                        baseDataValues.put(DatabaseContracts.TroopBaseData.MAXLEVEL, obj.getInt(DatabaseContracts.TroopBaseData.MAXLEVEL));
                                        baseDataValues.put(DatabaseContracts.TroopBaseData.MINLEVEL, obj.getInt(DatabaseContracts.TroopBaseData.MINLEVEL));
                                        baseDataValues.put(DatabaseContracts.TroopBaseData.UI_NAME, obj.getString(DatabaseContracts.TroopBaseData.UI_NAME));

                                        DBSQLiteHelper.insertData(DatabaseContracts.TroopBaseData.TABLE_NAME, baseDataValues, mContext);
                                    }

                                    if(dataTablesDetail.tableName.equalsIgnoreCase(DatabaseContracts.Buildings.TABLE_NAME)){

                                        ContentValues baseDataValues = new ContentValues();
                                        baseDataValues.put(DatabaseContracts.BuildingBaseData.CAPACITY, obj.getString(DatabaseContracts.BuildingBaseData.CAPACITY));
                                        baseDataValues.put(DatabaseContracts.BuildingBaseData.FACTION, obj.getString(DatabaseContracts.BuildingBaseData.FACTION));
                                        baseDataValues.put(DatabaseContracts.BuildingBaseData.GAME_NAME, obj.getString(DatabaseContracts.BuildingBaseData.GAME_NAME));
                                        baseDataValues.put(DatabaseContracts.BuildingBaseData.MAXLEVEL, obj.getInt(DatabaseContracts.BuildingBaseData.MAXLEVEL));
                                        baseDataValues.put(DatabaseContracts.BuildingBaseData.MINLEVEL, obj.getInt(DatabaseContracts.BuildingBaseData.MINLEVEL));
                                        baseDataValues.put(DatabaseContracts.BuildingBaseData.UI_NAME, obj.getString(DatabaseContracts.BuildingBaseData.UI_NAME));

                                        DBSQLiteHelper.insertData(DatabaseContracts.BuildingBaseData.TABLE_NAME, baseDataValues, mContext);
                                    }

                                    if (jsonConfigData.getLevel()) {
                                        int min = obj.getInt("minLevel");
                                        int max = obj.getInt("maxLevel");
                                        for (int i = min; i <= max; i++) {
                                            data = new ContentValues();
                                            for (Map.Entry<String, JsonValue> updateKeyValue : obj.entrySet()) {
                                                String value = "";
                                                if (updateKeyValue.getValue().getValueType().toString().equalsIgnoreCase("STRING")) {
                                                    value = obj.getString(updateKeyValue.getKey());
                                                } else if (updateKeyValue.getValue().getValueType().toString().equalsIgnoreCase("NUMBER")) {
                                                    value = String.valueOf(obj.getInt(updateKeyValue.getKey()));
                                                }
                                                if (updateKeyValue.getKey().toLowerCase().equalsIgnoreCase("minLevel".toLowerCase()) || updateKeyValue.getKey().toLowerCase().equalsIgnoreCase("maxLevel".toLowerCase())) {
                                                    data.put("level", i);
                                                } else if (updateKeyValue.getKey().toLowerCase().equalsIgnoreCase("game_name")) {
                                                    data.put(updateKeyValue.getKey(), value + i);
                                                } else {
                                                    data.put(updateKeyValue.getKey(), value);
                                                }
                                            }
                                            tableData.add(data);
                                        }
                                    } else {
                                        data = new ContentValues();
                                        for (Map.Entry<String, JsonValue> updateKeyValue : obj.entrySet()) {
                                            String value = "";
                                            if (updateKeyValue.getValue().getValueType().toString().equalsIgnoreCase("STRING")) {
                                                value = obj.getString(updateKeyValue.getKey());
                                            } else if (updateKeyValue.getValue().getValueType().toString().equalsIgnoreCase("NUMBER")) {
                                                value = String.valueOf(obj.getInt(updateKeyValue.getKey()));
                                            }
                                            data.put(updateKeyValue.getKey(), value);
                                        }
                                        tableData.add(data);

                                    }

                                }

                                activityCallBack.postUpdateToActivity("Processing record " + dataCounter + " of " + jsonConfigData.get_data().size() + " for " + dataTablesDetail.tableName);

                                dataCounter++;

                            } catch (Exception e) {
                                e.printStackTrace();
                                activityCallBack.postErrorBacktoActivity(e);
                            } finally {
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                            activityCallBack.postErrorBacktoActivity(e);
                            activityCallBack.postUpdateToActivity("Error getting data " + dataTablesDetail.tableName);
                        }

                        activityCallBack.postUpdateToActivity("Updating app database... ");
                        DBSQLiteHelper.bulkInsert(dataTablesDetail.tableName, tableData, repeatNo, "level", mContext);
                        try {
                            AppUpdateHelper.updateDataLog(updateNotes, dataTablesDetail.tableName, dataTablesDetail.dataVersion, mContext);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        tableData.clear();
                        updateResult.addMessage("Updated... " + dataTablesDetail.tableName + "\n");
                    } catch (Exception e) {
                        e.printStackTrace();
                        activityCallBack.postErrorBacktoActivity(e);
                        activityCallBack.postUpdateToActivity("Error processing " + dataTablesDetail.tableName);
                    }
                }
            } catch (
                    IOException e) {
                e.printStackTrace();
            }


            return updateResult;
        }


    }


}
