package com.swctools.activity_modules.updates;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;

public class VersionJSON {
    private static final String TAG = "VersionJSON";
    private JsonObject versionInfJsonObj;
    private JsonObject appUpdate;
    private JsonObject dataUpdate;

    public VersionJSON(String gitDownloadedStr) {
        gitDownloadedStr.replaceAll("\r?\n", "");
        String jsonString = gitDownloadedStr.replaceAll("\r?\n", "");
        versionInfJsonObj = Json.createReader(new StringReader(gitDownloadedStr)).readObject();
        dataUpdate = versionInfJsonObj.getJsonObject("data_update");
        appUpdate = versionInfJsonObj.getJsonObject("app_update");
    }

    public JsonObject getAppUpdate() {
        return appUpdate;
    }

    public JsonObject getDataUpdate() {
        return dataUpdate;
    }

    public JsonObject getVersionInfJsonObj() {
        return versionInfJsonObj;
    }

    public JsonArray getDataTables() {
        return dataUpdate.getJsonArray("tables");
    }

    public JsonObject getDataTableItem(String tableName) {
        JsonArray tables = dataUpdate.getJsonArray("tables");
        for (JsonValue tableEntry : tables) {
            JsonObject tableObj = (JsonObject) tableEntry;
            if (tableObj.getString("name").equalsIgnoreCase(tableName)) {
                return tableObj;
            }

        }
        return null;
    }

    public JsonObject getDataTableVersionDetails(String tableName, int version) {
        boolean tableDataFound = false;
        JsonArray tables = dataUpdate.getJsonArray("tables");
        JsonObject tableObj = null;
        for (JsonValue tableEntry : tables) {
            tableObj = (JsonObject) tableEntry;
            if (tableObj.getString("name").equalsIgnoreCase(tableName)) {
                tableDataFound = true;
                break;
            }
        }

        if (tableDataFound) {
            JsonArray tableVersionArray = tableObj.getJsonArray("versions");
            for (JsonValue versionEntry : tableVersionArray) {
                JsonObject versionEntryObj = (JsonObject) versionEntry;
                if (versionEntryObj.getInt("version") == version) {
                    return versionEntryObj;
                }
            }
        }


        return null;
    }

}
