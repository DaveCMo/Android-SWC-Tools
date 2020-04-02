package com.swctools.activity_modules.player.models;

import android.content.Context;

import com.swctools.common.enums.BattleOutcome;
import com.swctools.common.enums.BattleType;
import com.swctools.common.models.player_models.ArmouryEquipment;
import com.swctools.common.models.player_models.Troop;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.TreeMap;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;

public class BattleLogs implements Serializable {
    private static final String TAG = "BattleLogs";

    private TreeMap<Long, Battle> defenceLogs = new TreeMap<>(Collections.reverseOrder());
    private TreeMap<Long, Battle> attackLogs = new TreeMap<>(Collections.reverseOrder());
    private ArrayList<Battle> defenceBattleArrayList;
    private ArrayList<Battle> attackBattleArrayList;

//    private ArrayList<Battle> list = new ArrayList<>(Collections.reverseOrder());

    private JsonArray battleLogArray;
    private int wins = 0;
    private int losses = 0;
    private int attWins = 0;
    private int attLosses = 0;

    public BattleLogs(String playerId, JsonArray battleLogArr, HashMap<String, Troop> getMasterTroopList, HashMap<String, ArmouryEquipment> getMasterArmourList, Context context) {
        defenceBattleArrayList = new ArrayList<>();
        attackBattleArrayList = new ArrayList<>();

        this.battleLogArray = battleLogArr;
        try {
            for (JsonValue jsonValue : battleLogArr) {

                JsonObject jsonBattleObj = (JsonObject) jsonValue;
//
                Battle battle = new Battle(playerId, jsonBattleObj, false, getMasterTroopList, getMasterArmourList, context);

                if (battle.getBattleType().equals(BattleType.DEFENCE)) {
                    if (battle.getOutcome().equals(BattleOutcome.DEFEAT)) {
                        losses++;
                    } else {
                        wins++;
                    }
                    defenceLogs.put(battle.getAttackDateSec(), battle);
                    defenceBattleArrayList.add(battle);
                } else {
                    if (battle.getOutcome().equals(BattleOutcome.DEFEAT)) {
                        attLosses++;
                    } else {
                        attWins++;
                    }
                    attackLogs.put(battle.getAttackDateSec(), battle);
                    attackBattleArrayList.add(battle);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    public JsonArray getBattleLogArray() {
        return this.battleLogArray;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public TreeMap<Long, Battle> getDefenceLogs() {
        return defenceLogs;
    }

    public TreeMap<Long, Battle> getAttackLogs() {
        return attackLogs;
    }

    public int getAttLosses() {
        return attLosses;
    }

    public int getAttWins() {
        return attWins;
    }

    public ArrayList<Battle> getDefenceBattleArrayList() {
        return defenceBattleArrayList;
    }

    public ArrayList<Battle> getAttackBattleArrayList() {
        return attackBattleArrayList;
    }
}
//    String battleId = jsonBattleObj.getString("battleId");
//
////                if (battleIds.contains(battleId)) {
////                    newBattle = false;
////                } else {
//////                    ContentValues contentValues= new ContentValues();
//////                    newBattle = true;//then log it for next time.
//////                    contentValues.put(DatabaseContracts.BattleLog.PLAYER_ID, playerId);
//////                    contentValues.put(DatabaseContracts.BattleLog.BATTLE_ID, battleId);
//////                    DBSQLiteHelper.insertReplaceData(DatabaseContracts.BattleLog.TABLE_NAME, contentValues, context);
//////                    contentValues.clear();
//////                    stmt.bindString(1, playerId);
//////                    stmt.bindString(2, battleId);
//////                    stmt.executeInsert();
//////                    stmt.clearBindings();
////                }


//        battleIds = new HashSet<>();
//        String[] columns = {DatabaseContracts.BattleLog.BATTLE_ID, DatabaseContracts.BattleLog.PLAYER_ID};
//        String whereClause = DatabaseContracts.BattleLog.PLAYER_ID + " = ?";
//        String[] whereArgs = {playerId};
//        //Retrieve battle ids.
//        Cursor cursor = null;
//        try {
//            cursor = DBSQLiteHelper.queryDB(DatabaseContracts.BattleLog.TABLE_NAME, columns, whereClause, whereArgs, context);
//            ///----- create list of battle ids.
//            for (String c : cursor.getColumnNames()) {
//            }
//            while (cursor.moveToNext()) {
//                battleIds.add(cursor.getString(cursor.getColumnIndex(DatabaseContracts.BattleLog.BATTLE_ID)));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//        SQLiteStatement stmt = database.compileStatement(
//                DatabaseContracts.BattleLog.INSERT_STMT);