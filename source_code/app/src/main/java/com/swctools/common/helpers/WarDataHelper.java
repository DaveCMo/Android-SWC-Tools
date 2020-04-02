package com.swctools.common.helpers;

import android.content.Context;
import android.database.Cursor;

import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;

public class WarDataHelper {

    private static final String TAG = "WarDataHelper";

//    public static void assignWarAttacks(WarSquadModel warSquadModel, WarSquadModel rivalSquadModel) {
//
//        for (Object squadPartObj : warSquadModel.getWarParticipants()) {
//            if (squadPartObj instanceof WarParticipant) {
//                WarParticipant warParticipant = (WarParticipant) squadPartObj;
//
//                if (warParticipant.getCurrentlyDefending() != null) {
//                    assignAttack(rivalSquadModel, warParticipant.getCurrentlyDefending());
//                }
//            } else if (squadPartObj instanceof WarPlayer) {
//                WarPlayer warPlayer = (WarPlayer) squadPartObj;
//                if (warPlayer.getCurrentlyDefending() != null) {
//                    assignAttack(rivalSquadModel, warPlayer.getCurrentlyDefending());
//                }
//            }
//        }
//        int i1 = 0;
//        for (Object squadPartObj : rivalSquadModel.getWarParticipants()) {
//            if (squadPartObj instanceof WarParticipant) {
//                WarParticipant warParticipant = (WarParticipant) squadPartObj;
//                if (warParticipant.getCurrentlyDefending() != null) {
//                    assignAttack(warSquadModel, warParticipant.getCurrentlyDefending());
//
//                }
//            }
//            i1++;
//        }
//
//    }
//
//    private static void assignAttack(WarSquadModel otherSquad, CurrentlyDefending currentlyDefending) {
//        for (Object o : otherSquad.getWarParticipants()) {
//            if (o instanceof WarParticipant) {
//                WarParticipant warParticipant = (WarParticipant) o;
//                if (warParticipant.getId().equalsIgnoreCase(currentlyDefending.getOpponentId())) {
//                    warParticipant.addAttack(currentlyDefending);
//                }
//            } else if (o instanceof WarPlayer) {
//                WarPlayer warPlayer = (WarPlayer) o;
//                if (warPlayer.getId().equalsIgnoreCase(currentlyDefending.getOpponentId())) {
//                    warPlayer.addAttack(currentlyDefending);
//                }
//            }
//        }
//    }


    public static String getOutPostFriendlyName(String gameName, Context context) {
        String s = "";
        String whereStr = DatabaseContracts.Planets.WAR_NAME + " = ?";
        String[] args = {gameName};
        Cursor cursor = DBSQLiteHelper.queryDB(DatabaseContracts.Planets.TABLE_NAME, null, whereStr, args, context);
        try {
            while (cursor.moveToNext()) {
                s = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.Planets.UI_NAME));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return s;
    }


}
