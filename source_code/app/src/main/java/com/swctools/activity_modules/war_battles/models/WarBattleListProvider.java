package com.swctools.activity_modules.war_battles.models;

import android.content.Context;
import android.database.Cursor;

import com.swctools.activity_modules.war_room.models.War_Battle_Deployed;
import com.swctools.activity_modules.war_room.models.War_Defence;
import com.swctools.common.enums.ScreenCommands.DeployableTypes;
import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;
import com.swctools.util.Utils;

import java.util.ArrayList;


public class WarBattleListProvider {
    private static final String TAG = "WarBattleListProvider";

    public static int countBattlesForWar(String warId, Context context) {

        String whereClause = DatabaseContracts.WarDefense.WARID + " = ? ";
        String[] whereArgs = {warId};
        int count = 0;
        Cursor cursor = DBSQLiteHelper.queryDB(DatabaseContracts.WarDefense.TABLE_NAME, null, whereClause, whereArgs, context);
        try {
            count = cursor.getCount();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return count;
    }

    public static ArrayList<WarSummaryItem> getWarSummary(String warId, String guildId, Context context) {
        ArrayList<WarSummaryItem> warSummaryItems = new ArrayList<>();
        String maxCol = "maxDeployed";
        String sumCol = "sumDeployed";
        String sql = "SELECT MAX(" + DatabaseContracts.WarBattleDeployable.DEPLOYMENT_COUNT + ") as '" + maxCol + "', " +
                "SUM(" + DatabaseContracts.WarBattleDeployable.DEPLOYMENT_COUNT + ") as '" + sumCol + "', " +
                DatabaseContracts.WarBattleDeployable.DEPLOYABLE + " " +
                "FROM " + DatabaseContracts.WarBattleDeployable.TABLE_NAME + " " +
                "INNER JOIN " + DatabaseContracts.WarDefense.TABLE_NAME + " ON " +
                DatabaseContracts.WarDefense.TABLE_NAME + "." + DatabaseContracts.WarDefense.BATTLEID + " = " +
                DatabaseContracts.WarBattleDeployable.TABLE_NAME + "." + DatabaseContracts.WarBattleDeployable.BATTLEID + " " +
                "INNER JOIN " + DatabaseContracts.WarParticipantTable.TABLE_NAME + " ON " +
                DatabaseContracts.WarParticipantTable.TABLE_NAME + "." + DatabaseContracts.WarParticipantTable.PLAYERID + " = " +
                DatabaseContracts.WarDefense.TABLE_NAME + "." + DatabaseContracts.WarDefense.PLAYERID +
                " WHERE " + DatabaseContracts.WarParticipantTable.TABLE_NAME + "." + DatabaseContracts.WarParticipantTable.WARID +
                " = ? AND " + DatabaseContracts.WarParticipantTable.TABLE_NAME + "." + DatabaseContracts.WarParticipantTable.GUILDID + " = ? " +
                " AND " + DatabaseContracts.WarBattleDeployable.TABLE_NAME + "." + DatabaseContracts.WarBattleDeployable.WARID + " = ? " +
                "GROUP BY " + DatabaseContracts.WarBattleDeployable.DEPLOYABLE +
                " ORDER BY " + maxCol + " DESC";
        Utils.printLogDConcatStrings(sql, warId, guildId);
        Cursor cursor = DBSQLiteHelper.rawQuery(sql, new String[]{warId, guildId, warId}, context);
        while (cursor.moveToNext()) {
            String deployable = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.WarBattleDeployable.DEPLOYABLE));
            int max = cursor.getInt(cursor.getColumnIndexOrThrow(maxCol));
            int sum = cursor.getInt(cursor.getColumnIndexOrThrow(sumCol));
            WarSummaryItem warSummaryItem = new WarSummaryItem(deployable, max, sum);
            warSummaryItems.add(warSummaryItem);
        }
        return warSummaryItems;
    }


    public static ArrayList<War_Battle_Deployed> getWarBattleDeployed(String warId, String battleId, DeployableTypes type, Context context) {
        ArrayList<War_Battle_Deployed> war_battle_deployeds = new ArrayList<>();
        String whereClause = DatabaseContracts.WarBattleDeployable.WARID + " = ? AND " + DatabaseContracts.WarBattleDeployable.BATTLEID + " = ? AND " +
                DatabaseContracts.WarBattleDeployable.DEPLOYMENT_TYPE + " = ? ";
        String[] whereArgs = {warId, battleId, type.toString()};

        Cursor cursor = DBSQLiteHelper.queryDB(DatabaseContracts.WarBattleDeployable.TABLE_NAME, null, whereClause, whereArgs, null, null, DatabaseContracts.WarBattleDeployable.DEPLOYMENT_COUNT + " DESC ", null, context);
        try {
            while (cursor.moveToNext()) {
                String deployableType = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.WarBattleDeployable.DEPLOYMENT_TYPE));
                String deployable = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.WarBattleDeployable.DEPLOYABLE));
                int deployableQty = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.WarBattleDeployable.DEPLOYMENT_COUNT));
                War_Battle_Deployed war_battle_deployed = new War_Battle_Deployed(battleId, warId, deployableType, deployable, deployableQty);
                war_battle_deployeds.add(war_battle_deployed);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return war_battle_deployeds;
    }

    public static ArrayList<String> getPlayersWithDefences(String warId, String guildId, Context context) {
        ArrayList<String> playerIdList = new ArrayList<>();
        String colName = "playerWithDef";
        String sql = "SELECT DISTINCT " + DatabaseContracts.WarDefense.TABLE_NAME + "." + DatabaseContracts.WarDefense.PLAYER_NAME + " As '" + colName + "' FROM " +
                DatabaseContracts.WarDefense.TABLE_NAME +
                " INNER JOIN " + DatabaseContracts.WarParticipantTable.TABLE_NAME + " ON " +
                DatabaseContracts.WarParticipantTable.TABLE_NAME + "." + DatabaseContracts.WarParticipantTable.PLAYERID +
                " = " + DatabaseContracts.WarDefense.TABLE_NAME + "." + DatabaseContracts.WarDefense.PLAYERID +
                " WHERE " + DatabaseContracts.WarParticipantTable.TABLE_NAME + "." + DatabaseContracts.WarParticipantTable.WARID +
                " = ? AND " + DatabaseContracts.WarParticipantTable.TABLE_NAME + "." + DatabaseContracts.WarParticipantTable.GUILDID + " = ? ";
        Cursor cursor = DBSQLiteHelper.rawQuery(sql, new String[]{warId, guildId}, context);
        try {
            while (cursor.moveToNext()) {
                String s = cursor.getString(cursor.getColumnIndexOrThrow(colName));
                playerIdList.add(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return playerIdList;
    }

    public static ArrayList<String> getSquadDefences(String warId, String guildId, Context context) {
        ArrayList<String> battleIds = new ArrayList<>();
        String colName = "warDefenseId";
        String sql = "SELECT " + DatabaseContracts.WarDefense.BATTLEID + " as '" + colName + "' FROM " +
                DatabaseContracts.WarDefense.TABLE_NAME + " " +
                "INNER JOIN " + DatabaseContracts.WarParticipantTable.TABLE_NAME + " ON " +
                DatabaseContracts.WarParticipantTable.TABLE_NAME + "." + DatabaseContracts.WarParticipantTable.PLAYERID + " = " + DatabaseContracts.WarDefense.TABLE_NAME + "." + DatabaseContracts.WarDefense.PLAYERID +
                " WHERE " + DatabaseContracts.WarParticipantTable.TABLE_NAME + "." + DatabaseContracts.WarParticipantTable.WARID + " = ? AND " + DatabaseContracts.WarParticipantTable.TABLE_NAME + "." + DatabaseContracts.WarParticipantTable.GUILDID + " = ? ";
        Cursor cursor = DBSQLiteHelper.rawQuery(sql, new String[]{warId, guildId}, context);
        try {
            while (cursor.moveToNext()) {
                String battleId = cursor.getString(cursor.getColumnIndexOrThrow(colName));
                battleIds.add(battleId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }


        return battleIds;
    }


    public static ArrayList<War_Defence> getPlayerWarDefences(String warId, String playerId, Context context) {
        ArrayList<War_Defence> war_defences = new ArrayList<>();

        String whereClause = DatabaseContracts.WarDefense.WARID + " = ? AND " + DatabaseContracts.WarDefense.PLAYERID + " = ? ";
        String[] whereArgs = {warId, playerId};

        Cursor cursor = DBSQLiteHelper.queryDB(DatabaseContracts.WarDefense.TABLE_NAME, null, whereClause, whereArgs, context);

        try {
            while (cursor.moveToNext()) {
                String playerName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.WarDefense.PLAYER_NAME));
                String opponentId = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.WarDefense.OPPONENTID));
                String opponentName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.WarDefense.OPPONENT_NAME));
                String attackerWarBuffs = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.WarDefense.ATTACKERWARBUFFS));
                String defenderWarBuffs = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.WarDefense.DEFENDERWARBUFFS));

                War_Defence war_defence = new War_Defence(warId, playerId, playerName, opponentId, opponentName, attackerWarBuffs, defenderWarBuffs);
                war_defences.add(war_defence);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return war_defences;
    }


    public static ArrayList<War_PlayerBattles> returnWarBattles(String warId, String guildId, Context context) {
        ArrayList<War_PlayerBattles> playerIdList = new ArrayList<>();
        String playerName = "playerWithDef";
        String playerIdCol = "playerIdWithDef";
        String sql = "SELECT DISTINCT " +
                DatabaseContracts.WarDefense.TABLE_NAME + "." + DatabaseContracts.WarDefense.PLAYER_NAME + " As '" + playerName + "', " +
                DatabaseContracts.WarDefense.TABLE_NAME + "." + DatabaseContracts.WarDefense.PLAYERID + " As '" + playerIdCol + "'" +
                " FROM " +
                DatabaseContracts.WarDefense.TABLE_NAME +
                " INNER JOIN " + DatabaseContracts.WarParticipantTable.TABLE_NAME + " ON " +
                DatabaseContracts.WarParticipantTable.TABLE_NAME + "." + DatabaseContracts.WarParticipantTable.PLAYERID +
                " = " + DatabaseContracts.WarDefense.TABLE_NAME + "." + DatabaseContracts.WarDefense.PLAYERID +
                " WHERE " + DatabaseContracts.WarParticipantTable.TABLE_NAME + "." + DatabaseContracts.WarParticipantTable.WARID +
                " = ? AND " + DatabaseContracts.WarParticipantTable.TABLE_NAME + "." + DatabaseContracts.WarParticipantTable.GUILDID + " = ? ";
        Cursor cursor = DBSQLiteHelper.rawQuery(sql, new String[]{warId, guildId}, context);
        try {
            while (cursor.moveToNext()) {
                String s = cursor.getString(cursor.getColumnIndexOrThrow(playerName));
                String id = cursor.getString(cursor.getColumnIndexOrThrow(playerIdCol));
                playerIdList.add(new War_PlayerBattles(id, s));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return playerIdList;
    }


}
