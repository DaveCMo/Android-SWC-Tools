package com.swctools.activity_modules.war_room.models;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;
import com.swctools.util.StringUtil;

import java.util.ArrayList;

public class WarRoomListProvider {
    private static final String TAG = "WarRoomListProvider";

    public static ArrayList<War_Room_OutPost> getWarOps(String warId, String squadId, Context context) {
        ArrayList<War_Room_OutPost> warOutPosts = new ArrayList<>();
        String whereClause = DatabaseContracts.WarBuffBases.WARID + " = ? AND " + DatabaseContracts.WarBuffBases.OWNERID + " = ? ";
        String[] whereArgs = {warId, squadId};

        Cursor cursor = DBSQLiteHelper.queryDB(DatabaseContracts.WarBuffBases.TABLE_NAME, null, whereClause, whereArgs, context);
        try {
            while (cursor.moveToNext()) {
                String ownerId = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.WarBuffBases.OWNERID));
                String buffUid = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.WarBuffBases.BUFFUID));
                String outPostName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.WarBuffBases.OUTPOST_NAME));
                int level = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.WarBuffBases.ADJUSTED_LEVEL));
                War_Room_OutPost warOutPost = new War_Room_OutPost(ownerId, buffUid, outPostName, level);
                warOutPosts.add(warOutPost);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return warOutPosts;
    }

    public static ArrayList<War_WarParticipant> getWarParticipants(String warId, String squadId, Context context) {
        ArrayList<War_WarParticipant> war_warParticipantArrayList = new ArrayList<>();



        String whereClause = DatabaseContracts.WarParticipantTable.WARID + " = ? AND " + DatabaseContracts.WarParticipantTable.GUILDID + " = ? ";
        String[] whereArgs = {warId, squadId};
        Cursor cursor = DBSQLiteHelper.queryDB(DatabaseContracts.WarParticipantTable.TABLE_NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                DatabaseContracts.WarParticipantTable.IS_REQUESTER + " DESC, " + DatabaseContracts.WarParticipantTable.BASE_SCORE + " DESC",
                null,context);
        try {
            while (cursor.moveToNext()) {


                String playerId = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.WarParticipantTable.PLAYERID));
                String playerName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.WarParticipantTable.PLAYER_NAME));
                String isRequester = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.WarParticipantTable.IS_REQUESTER));
                int baseScore = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.WarParticipantTable.BASE_SCORE));
                int hqLevel = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.WarParticipantTable.HQLEVEL));
                int turns = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.WarParticipantTable.TURNS));
                int victoryPoints = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.WarParticipantTable.VICTORY_POINTS));
                int score = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.WarParticipantTable.SCORE));
                int scCap = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.WarParticipantTable.SC_CAP));
                int scCapDonated = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.WarParticipantTable.SC_CAP_DONATED));
                int lastAttacked = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.WarParticipantTable.LAST_ATTACKED));
                String lastAttackedBy = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.WarParticipantTable.LAST_ATTACKED_BY_ID));
                String lastBattleId = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.WarParticipantTable.LAST_BATTLE_ID));
                String faction = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.WarParticipantTable.FACTION));
                String isErrorWithSC = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.WarParticipantTable.ERRORWITHSC));
                String attackedBYName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.WarParticipantTable.LAST_ATTACKED_BY_NAME));
                War_WarParticipant war_warParticipant = new War_WarParticipant(warId, playerId, squadId, playerName, isRequester, baseScore, hqLevel, turns, victoryPoints, score, scCap, scCapDonated, lastAttacked, lastAttackedBy, lastBattleId, attackedBYName, faction, isErrorWithSC);
                war_warParticipantArrayList.add(war_warParticipant);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return war_warParticipantArrayList;
    }

    public static War_Log getWarLog(String warId, Context context) {
        String whereClause = DatabaseContracts.WarLog.WARID + " = ? ";
        String[] whereArgs = {warId};
        War_Log war_log = null;
        Cursor cursor = DBSQLiteHelper.queryDB(DatabaseContracts.WarLog.TABLE_NAME, null, whereClause, whereArgs, context);
        try {

            while (cursor.moveToNext()) {
                String playerId = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.WarLog.PLAYERID));
                String guildId = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.WarLog.GUILDID));
                String guildName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.WarLog.GUILDNAME));
                String guildFaction = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.WarLog.GUILDFACTION));
                String rivalGuildId = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.WarLog.RIVALGUILDID));
                String rivalGuildName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.WarLog.RIVALGUILDNAME));
                String rivalGuildFaction = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.WarLog.RIVALGUILDFACTION));

                long startTime = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContracts.WarLog.START_TIME));
                long prepGraceStart = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContracts.WarLog.PREPSTARTTIME));
                long prepEnd = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContracts.WarLog.PREPENDTIME));
                long actionStart = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContracts.WarLog.ACTIONSTARTTIME));
                long actionEnd = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContracts.WarLog.ACTIONENDTIME));
                long cooldownEnd = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContracts.WarLog.COOLDOWNEND));
                war_log = new War_Log(warId, playerId, guildId, guildName, guildFaction, rivalGuildId, rivalGuildName, rivalGuildFaction, startTime, prepGraceStart, prepEnd, actionStart, actionEnd, cooldownEnd);
                break;
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        if (war_log != null) {
            int squadAttacks = 0;
            int rivalAttacks = 0;
            int squadScore = 0;
            int rivalScore = 0;
            int squadWipes = 0;
            int rivalWipes = 0;

            String[] columns = {
                    "SUM(" + DatabaseContracts.WarParticipantTable.TURNS + ") as 'countAttacks'",
                    "SUM(" + DatabaseContracts.WarParticipantTable.VICTORY_POINTS + ") as 'sumVictoryPoints'"
            };
            String attacksWhereClause = DatabaseContracts.WarParticipantTable.WARID + " = ? AND " + DatabaseContracts.WarParticipantTable.GUILDID + " = ? ";
            String[] attackWhereArg = {warId, war_log.getGuildId()};
            //Get guild
            Cursor cursorGuildAttacksAndScore;
            cursorGuildAttacksAndScore = DBSQLiteHelper.queryDB(DatabaseContracts.WarParticipantTable.TABLE_NAME, columns, attacksWhereClause, attackWhereArg, context);

            try {
                while (cursorGuildAttacksAndScore.moveToNext()) {
                    squadAttacks = cursorGuildAttacksAndScore.getInt(cursorGuildAttacksAndScore.getColumnIndexOrThrow("countAttacks"));
                    rivalScore = 45 - cursorGuildAttacksAndScore.getInt(cursorGuildAttacksAndScore.getColumnIndexOrThrow("sumVictoryPoints"));
                    break;
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } finally {
                if (cursorGuildAttacksAndScore != null) {
                    cursorGuildAttacksAndScore.close();
                }
            }
            //get rival
            String[] rivalWhereArg = {warId, war_log.getRivalGuildId()};
            cursorGuildAttacksAndScore = DBSQLiteHelper.queryDB(DatabaseContracts.WarParticipantTable.TABLE_NAME, columns, attacksWhereClause, rivalWhereArg, context);
            try {
                while (cursorGuildAttacksAndScore.moveToNext()) {
                    rivalAttacks = cursorGuildAttacksAndScore.getInt(cursorGuildAttacksAndScore.getColumnIndexOrThrow("countAttacks"));
                    squadScore = 45 - cursorGuildAttacksAndScore.getInt(cursorGuildAttacksAndScore.getColumnIndexOrThrow("sumVictoryPoints"));
                    break;
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } finally {
                if (cursorGuildAttacksAndScore != null) {
                    cursorGuildAttacksAndScore.close();
                }
            }
            String[] countWipesColumn = {"COUNT (" + DatabaseContracts.WarParticipantTable.PLAYERID + ") AS 'countWipes'"};
            String countWipesClause = DatabaseContracts.WarParticipantTable.WARID + " = ? AND " + DatabaseContracts.WarParticipantTable.VICTORY_POINTS + " = 0 AND " + DatabaseContracts.WarParticipantTable.GUILDID + " = ?";
            String[] countWipesArgs = {warId, war_log.getGuildId()};
            Cursor countWipesCursor = DBSQLiteHelper.queryDB(DatabaseContracts.WarParticipantTable.TABLE_NAME, countWipesColumn, countWipesClause, countWipesArgs, context);
            try {
                while (countWipesCursor.moveToNext()) {
                    rivalWipes = countWipesCursor.getInt(countWipesCursor.getColumnIndexOrThrow("countWipes"));
                    break;
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } finally {
                if (countWipesCursor != null) {
                    countWipesCursor.close();
                }
            }

            String[] countGuildWipesArgs = {warId, war_log.getRivalGuildId()};
            countWipesCursor = DBSQLiteHelper.queryDB(DatabaseContracts.WarParticipantTable.TABLE_NAME, countWipesColumn, countWipesClause, countGuildWipesArgs, context);
            try {
                while (countWipesCursor.moveToNext()) {
                    squadWipes = countWipesCursor.getInt(countWipesCursor.getColumnIndexOrThrow("countWipes"));
                    break;
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } finally {
                if (countWipesCursor != null) {
                    countWipesCursor.close();
                }
            }
            war_log.setAttacks(squadAttacks, rivalAttacks, squadScore, rivalScore, squadWipes, rivalWipes);
        }
        return war_log;
    }



    public static ArrayList<War_SC_Contents> getWarScContents(String warId, String playerId, Context context) {
        ArrayList<War_SC_Contents> war_sc_contentsArrayList = new ArrayList<>();

        String whereClause = DatabaseContracts.WarSCContents.WARID + " = ? AND " + DatabaseContracts.WarSCContents.PLAYERID + " = ? ";
        String[] whereArgs = {warId, playerId};
        Cursor cursor = DBSQLiteHelper.queryDB(DatabaseContracts.WarSCContents.TABLE_NAME, null, whereClause, whereArgs, context);
        try {
            while (cursor.moveToNext()) {
                String ui_name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.WarSCContents.UINAME));
                int qty = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.WarSCContents.QTY));
                int cap = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.WarSCContents.CAP));
                int unit_level = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.WarSCContents.LEVEL));
                String donatedByName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.WarSCContents.DONATED_BY_NAME));
                War_SC_Contents war_sc_contents = new War_SC_Contents(warId, playerId, ui_name, qty, cap, unit_level, donatedByName);
                war_sc_contentsArrayList.add(war_sc_contents);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }


        return war_sc_contentsArrayList;
    }


}
