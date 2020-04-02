package com.swctools.common.models.player_models;

import android.content.Context;

import com.swctools.common.enums.BattleOutcome;
import com.swctools.activity_modules.player.models.Battle;

import java.io.Serializable;
import java.util.HashMap;

import javax.json.JsonObject;

 class Attack extends Battle implements Serializable {
    private BattleOutcome outcome;

    public Attack(String playerId, JsonObject battleLog, Boolean isNew, HashMap<String, Troop> getMasterTroopList, HashMap<String, ArmouryEquipment> getMasterArmourList, Context context) {
        super(playerId, battleLog, isNew, getMasterTroopList, getMasterArmourList, context);
    }

//    @Override
//    public void setOutcome(int stars) {
//        if (stars == 0) {
//            this.outcome = BattleOutcome.DEFEAT;
//        } else {
//            this.outcome = BattleOutcome.VICTORY;
//        }
//    }

    public BattleOutcome getOutcome() {
        return outcome;
    }


}
