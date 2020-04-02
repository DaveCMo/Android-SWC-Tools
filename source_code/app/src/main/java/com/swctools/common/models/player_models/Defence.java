package com.swctools.common.models.player_models;

import android.content.Context;

import com.swctools.common.enums.BattleOutcome;
import com.swctools.activity_modules.player.models.Battle;

import java.util.HashMap;

import javax.json.JsonObject;

 class Defence extends Battle {
    private static final String TAG = "DEFENCE";
    private BattleOutcome outcome;

    public Defence(String playerId, JsonObject battleLog, Boolean isNew, HashMap<String, Troop> getMasterTroopList, HashMap<String, ArmouryEquipment> getMasterArmourList, Context context) {
        super(playerId, battleLog, isNew, getMasterTroopList, getMasterArmourList, context);
    }


//    @Override
//    public void setOutcome(int stars) {
//        if (stars == 0) {
//            this.outcome = BattleOutcome.VICTORY;
//        } else {
//            this.outcome = BattleOutcome.DEFEAT;
//        }
//    }

    public BattleOutcome getOutcome() {
        return outcome;
    }


}
