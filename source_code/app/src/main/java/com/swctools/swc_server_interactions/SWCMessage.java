package com.swctools.swc_server_interactions;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.swctools.config.AppConfig;
import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;
import com.swctools.common.helpers.DateTimeHelper;
import com.swctools.swc_server_interactions.results.SWCDefaultResponse;
import com.swctools.swc_server_interactions.swc_commands.SWC_Command;
import com.swctools.util.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.net.ssl.HttpsURLConnection;


public class SWCMessage {
    private static final String TAG = "SWCMessage";
    private String authKey;
    private boolean pickupMessages;

    private List<SWC_Command> commands;
    private JsonObjectBuilder messageObjectBuilder;
    private JsonObjectBuilder messageObjectBuilderNoAuthKey;
    private HashMap<String, Integer> commandIdIndex;
    private JsonArrayBuilder commandsArray;
    private JsonArray commandsArrayBuilt;
    private Context mContext;
    private SWCDefaultResponse swcMessageResponse;
    private AppConfig appConfig;
    private String functionCalled;
    private String messageToSend;
    private String messageToSendNoAuthKey;

    public SWCMessage(String authKey, boolean pickupMessages, long lastLoginTime, List<SWC_Command> commands, String function, Context context) {
        this.commands = commands;
        this.pickupMessages = pickupMessages;
        commandIdIndex = new HashMap<>();
        this.mContext = context;
        this.appConfig = new AppConfig(mContext);
        this.functionCalled = function;
        this.mContext = context;
        prepMessage(authKey, lastLoginTime);

    }

    public SWCMessage(String authKey, boolean pickupMessages, long lastLoginTime, SWC_Command command, String function, Context context) {

        Utils.Log("SWCMessage", function);
        this.commands = new ArrayList<>();
        this.pickupMessages = pickupMessages;
        commands.add(command);
        commandIdIndex = new HashMap<>();
        this.mContext = context;
        this.functionCalled = function;
        prepMessage(authKey, lastLoginTime);

    }

    private static String getResponse(String message, Context context) throws Exception {
        AppConfig appconfig = new AppConfig(context);
        String response = "";
        InputStream inputStream = null;
        HttpsURLConnection connection;
        OutputStream output = null;
        String serverAddress = appconfig.getServerAddress();
        String uriencodedValues = "batch" + "=" + URLEncoder.encode(message, "UTF-8");
        int contentLen = uriencodedValues.length();
        URL url = new URL(serverAddress);
        connection = setURLConnection(contentLen, url, context);


        try {
            output = connection.getOutputStream();
            output.write(uriencodedValues.getBytes(StandardCharsets.UTF_8));
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpsURLConnection.HTTP_OK) {
                throw new IOException("HTTP error code: " + responseCode);
            }
            inputStream = connection.getInputStream();
            if (inputStream != null) {
                BufferedReader bufferedReader = null;
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                response = bufferedReader.readLine();
                bufferedReader.close();
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
            if (output != null) {
                output.close();
            }
        }
        return response;
    }

    public static HttpsURLConnection setURLConnection(int contentLen, URL url, Context context) throws Exception {
        HttpsURLConnection urlConnection;
        urlConnection = (HttpsURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setDoOutput(true);


        Cursor cursor = null;
        try {
            String whereClause = DatabaseContracts.SWC_HTTP_HEADERS.HEADER_GROUP + " = ?";
            String[] whereArgs = {"default"};
            cursor = DBSQLiteHelper.queryDB(DatabaseContracts.SWC_HTTP_HEADERS.TABLE_NAME, null, whereClause, whereArgs, context);

            while (cursor.moveToNext()) {
                String k = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.SWC_HTTP_HEADERS.KEY));
                String v = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.SWC_HTTP_HEADERS.VALUE));
                urlConnection.setRequestProperty(k, v);
            }
        } catch (Exception e) {
            throw e;

        } finally {

            if (cursor != null) {
                cursor.close();
            }

            urlConnection.setRequestProperty("Content-Length", String.valueOf(contentLen));
        }

        return urlConnection;
    }

    public void prepMessage(String authKey, long lastLoginTime) {
        messageObjectBuilder = Json.createObjectBuilder();
        messageObjectBuilderNoAuthKey = Json.createObjectBuilder();
        messageObjectBuilder.add("authKey", authKey);
        commandsArray = Json.createArrayBuilder();
        for (SWC_Command c : commands) {
            commandIdIndex.put(c.getAction(), c.getRequestId());
            JsonObject command = c.buildCommand();
            commandsArray.add(command);
        }

        messageObjectBuilder.add("lastLoginTime", lastLoginTime);
        messageObjectBuilder.add("pickupMessages", this.pickupMessages);

        messageObjectBuilderNoAuthKey.add("lastLoginTime", lastLoginTime);
        messageObjectBuilderNoAuthKey.add("pickupMessages", this.pickupMessages);


    }

    public void addCommand(SWC_Command command) {
        this.commands.add(command);

    }

    public JsonObject getMessage() {
        commandsArrayBuilt = commandsArray.build();
        messageObjectBuilder.add("commands", commandsArrayBuilt);
        return messageObjectBuilder.build();
    }

    private JsonObject getMessageForLog() {
        messageObjectBuilderNoAuthKey.add("commands", commandsArrayBuilt);
        return messageObjectBuilderNoAuthKey.build();
    }


    public List<SWC_Command> getCommands() {
        return commands;
    }

    public HashMap<String, Integer> getCommandIdIndex() {
//        for (Map.Entry<String, Integer> entry : commandIdIndex.entrySet()) {
//            String key = entry.getKey();
//            Integer value = entry.getValue();
//        }
        return commandIdIndex;
    }

    public SWCDefaultResponse getSwcMessageResponse() throws Exception {
        if (commands.size() > 0) {
            this.messageToSend = getMessage().toString();
            Utils.Log("SWCMessage.getSwcMessageResponse|Message", this.messageToSend);

            String response = getResponse(this.messageToSend, mContext);
            Utils.Log("SWCMessage.getSwcMessageResponse|Response", response);
            this.swcMessageResponse = new SWCDefaultResponse(response);

            logMessage(response);
            return this.swcMessageResponse;
        }
        return null;
    }

    private void logMessage(String response) {
        appConfig = new AppConfig(mContext);
        if (appConfig.bLogSWCMessage()) {
            long addedDate = (DateTimeHelper.userIDTimeStamp() / 1000);

            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseContracts.SWCMessageLog.FUNCTION, functionCalled);
            contentValues.put(DatabaseContracts.SWCMessageLog.MESSAGE, getMessageForLog().toString());
            contentValues.put(DatabaseContracts.SWCMessageLog.RESPONSE, response);
            contentValues.put(DatabaseContracts.SWCMessageLog.DATELOGGED, addedDate);
            DBSQLiteHelper.insertData(DatabaseContracts.SWCMessageLog.TABLE_NAME, contentValues, mContext);
        }

    }
}
