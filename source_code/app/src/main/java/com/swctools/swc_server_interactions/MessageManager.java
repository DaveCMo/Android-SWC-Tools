package com.swctools.swc_server_interactions;

import android.content.Context;
import android.database.Cursor;

import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;
import com.swctools.config.AppConfig;
import com.swctools.util.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

public class MessageManager {
    private static final String TAG = "MessageManager";

    private MessageManager() {

    }


    private static String sendMessage(SWCMessage swcMessage, Context context) throws Exception {
        if (swcMessage.getCommands().size() > 0) {
            String response = getResponse(swcMessage.getMessage().toString(), context);
            return response;
        } else {
            throw new Exception("Error building commands! None passed into the message!");
        }
    }

    private static String sendMessage(String message, Context context) throws Exception {
        String response = getResponse(message, context);
        return response;
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
        Utils.Log(TAG, response);
        return response;
    }

    private static HttpsURLConnection setURLConnection(int contentLen, URL url, Context context) throws Exception {
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
                urlConnection.setRequestProperty(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.SWC_HTTP_HEADERS.KEY)), cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.SWC_HTTP_HEADERS.VALUE)));
            }
        } catch (Exception e) {

            throw e;

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return urlConnection;
    }

}
