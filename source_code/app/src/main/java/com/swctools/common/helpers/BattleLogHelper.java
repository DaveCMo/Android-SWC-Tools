package com.swctools.common.helpers;

import com.swctools.activity_modules.player.models.Battle;
import com.swctools.activity_modules.player.models.BattleLogs;

import java.util.Map;

public class BattleLogHelper {
    private static final String TAG = "BattleLogHelper";
//    private BattleLogs oldLog;
//    private BattleLogs newLog;

    private BattleLogHelper(){

    }

    public static BattleLogs newBattleLog(BattleLogs oldLog, BattleLogs newLog){
    boolean matched = false;
        for (Map.Entry<Long, Battle> entry : newLog.getDefenceLogs().entrySet()) {

            Battle newBattle = entry.getValue();

            matched = false;
            for(Map.Entry<Long, Battle> oldBattle : oldLog.getDefenceLogs().entrySet()){

                if(newBattle.getBattleId().equalsIgnoreCase(oldBattle.getValue().getBattleId())){
                    matched = true;
                    newBattle.setNewBattle(false);

                    break;
                }
            }
            if(!matched){
                newBattle.setNewBattle(true);
            }
        }
        return newLog;
    }
}
