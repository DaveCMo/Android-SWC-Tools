package com.swctools.util;

/**
 * Created by David on 07/03/2018.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.swctools.BuildConfig;
import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;

import static com.swctools.database.DBSQLiteHelper.countAllTableRows;

/**
 * Created by ozgur on 16.04.2016.
 */
public class Utils {
    private static final String TAG = "Utils";


    public static void shareText(String s, Context context) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, s);
        intent.setType("text/plain");
        context.startActivity(intent);
    }


    public static void printTableContents(String tbl, Context context) {
        Log.d(TAG, tbl + "----------------------------------------------------------------");

        try {
            Cursor cursor = DBSQLiteHelper.queryDB(tbl, context);
            printCurorContents(cursor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void printCurorContents(Cursor cursor) {
        Log.d(TAG, "printCurorContents: ----------------------------------------------------------------");
        while (cursor.moveToNext()) {
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                try {
                    Log.d(cursor.getColumnName(i), cursor.getString(i));
                } catch (Exception e) {
                    Log.d(cursor.getColumnName(i), "");
                }
            }
        }
        Log.d(TAG, "/------------------------printCurorContents: ----------------------------------------------------------------");
    }

    public static void Log(String tag, String message) {
        if ((BuildConfig.DEBUG)) {
            Log.d(tag, message + "");
        }
    }

    public static void Log(String tag, Object message) {
        if ((BuildConfig.DEBUG)) {
            Log.d(tag, String.valueOf(message) + "");
        }
    }

    public static void printLogDConcatStrings(String... strings) {
        //Debug method to print an array of strings to the run window. Saves using log.d and manually adding delimiters
        StringBuilder sb = new StringBuilder();
        String delim = "|";
        for (int i = 0; i < strings.length; i++) {
            sb.append(strings[i] + delim);
        }
        Log.d(TAG, sb.toString());
    }


    public static boolean playerIdDoesNotExist(String playerId, Context context) {
        if (!DBSQLiteHelper.CheckIsDataAlreadyInDBorNot(
                DatabaseContracts.PlayersTable.TABLE_NAME,
                DatabaseContracts.PlayersTable.PLAYERID,
                playerId,
                context)) {
            return true;
        } else {
            return false;
        }
    }

    public static long countPlayers(Context context) {
        long noRows = countAllTableRows(DatabaseContracts.PlayersTable.TABLE_NAME, context);
        return noRows;
    }

    public static String getOnlyPlayerId(Context context) {
        String playerId = "";

        Cursor cursor = DBSQLiteHelper.queryDB(DatabaseContracts.PlayersTable.TABLE_NAME, context);
        try {
            while (cursor.moveToNext()) {
                playerId = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.PlayersTable.PLAYERID));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }


        return playerId;

    }

    public static MethodResult validatePlayer(String playerId, String playerSecret) {
        //helper function to validate the credentials passed in make sense.
        MethodResult result;
        //first check values are present:
        if (StringUtil.isStringNotNull(playerId) && StringUtil.isStringNotNull(playerSecret)) {
            if (playerId.length() >= 36 && playerSecret.length() == 32) {
                result = new MethodResult(true, "Player Credentials OK");


            } else {
                String title = "These are not the credentials you are looking for....\n";
                String pIdErr = null;

                if (playerId.length() <= 36) {
                    pIdErr = "Player ID should be at least 36 characters. The ID you have entered is " + playerId.length() + " characters.";

                }
                String pSeErr = null;
                if (playerSecret.length() != 32) {
                    pSeErr = "Player secret should be 32 characters. You have entered a player secret " + playerSecret.length() + " characters long";

                }
                result = new MethodResult(false, title, pIdErr, pSeErr);
            }
        } else {
            result = new MethodResult(false, "Player Id or Secret cannot be null!");

        }
        return result;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

            Network[] activeNetworks = cm.getAllNetworks();
            for (Network n : activeNetworks) {
                NetworkInfo nInfo = cm.getNetworkInfo(n);
                if (nInfo.isConnected())
                    return true;
            }

        } else {
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null)
                for (NetworkInfo anInfo : info)
                    if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }

        return false;

    }
}
