package com.swctools.common.helpers;

import android.content.Context;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;
import com.swctools.common.enums.ApplicationMessageTemplates;
import com.swctools.common.enums.BundleKeys;
import com.swctools.util.MethodResult;

public class PlayerHelper implements Parcelable {
    public static final Parcelable.Creator<PlayerHelper> CREATOR = new Parcelable.Creator<PlayerHelper>() {
        // This simply calls our new constructor (typically private) and
        // passes along the unmarshalled `Parcel`, and then returns the new object!
        @Override
        public PlayerHelper createFromParcel(Parcel in) {
            return new PlayerHelper(in);
        }

        // We just need to copy this and change the type to match our class.
        @Override
        public PlayerHelper[] newArray(int size) {
            return new PlayerHelper[size];
        }
    };
    private static final String TAG = "Player";
    public String playerId = "";
    public String playerSecret = "";
    public String playerName = "";


    public PlayerHelper(String playerId, String secret) {
        this.playerId = playerId;
        this.playerSecret = secret;
    }

    public static String getVisitorKey(String playerId) {
        return String.format(ApplicationMessageTemplates.VISIT_KEY.getemplateString(), BundleKeys.VISIT_RESPONSE.toString(), playerId);
    }

    public PlayerHelper(String playerId, String secret, String playerName) {
        this.playerId = playerId;
        this.playerSecret = secret;
        this.playerName = playerName;
    }


    // Using the `in` variable, we can retrieve the values that
    // we originally wrote into the `Parcel`.  This constructor is usually
    // private so that only the `CREATOR` field can access.
    private PlayerHelper(Parcel in) {

        playerId = in.readString();
        playerSecret = in.readString();
        playerName = in.readString();


    }

    public static String getPlayerName(String playerId, Context context) {
        String playerName = "";
        String playerSecret = "";

        Cursor cursor = null;
        String[] columns = {
                DatabaseContracts.PlayersTable.PLAYERNAME};
        String selection = DatabaseContracts.PlayersTable.PLAYERID + " like ?";
        String[] selectionArgs = {playerId};
        try {

            cursor = DBSQLiteHelper.queryDB(DatabaseContracts.PlayersTable.TABLE_NAME, columns, selection, selectionArgs, context);
            while (cursor.moveToNext()) {
                playerName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.PlayersTable.PLAYERNAME));

            }
        } catch (Exception e) {
            playerName = "";
        } finally {
            if (cursor != null) {
                cursor.close();
            }

        }
        return playerName;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(playerId);
        out.writeString(playerSecret);
        out.writeString(playerName);
    }

    @Override
    public int describeContents() {
        return 0;
    }


    public String toString() {
        return playerName;
    }

    public static MethodResult deletePlayer(int id, Context context) {
        try {
            String whereClause = DatabaseContracts.PlayersTable.COLUMN_ID + " = ? ";

            if (DBSQLiteHelper.deleteDbRow(DatabaseContracts.PlayersTable.TABLE_NAME, whereClause, String.valueOf(id), context)) {
                return new MethodResult(true, "Player deleted");
            } else {
                return new MethodResult(false, "Player NOT deleted!:(");
            }
        } catch (Exception e) {
            return new MethodResult(false, e.getMessage());
        }


    }


}
