package com.swctools.common.models.player_models;

import javax.json.JsonObject;

public class Upgrades {
    public final static String TAG = "Upgrades";
    private static final String DEKA_FORMAT = "Champion%1$sDroideka";
    private static final String DEKO_FORMAT = "Champion%1$sHeavyDroideka";

    private UpgradesObjects troop;
    private UpgradesObjects specialAttack;
    private UpgradesObjects equipment;


    public Upgrades(JsonObject upgrades, String faction) {
        troop = new UpgradesObjects(upgrades.getJsonObject("troop"));
        specialAttack = new UpgradesObjects(upgrades.getJsonObject("specialAttack"));
        equipment = new UpgradesObjects(upgrades.getJsonObject("equipment"));
    }

    public UpgradesObjects getTroop() {
        return troop;
    }

    public UpgradesObjects getSpecialAttack() {
        return specialAttack;
    }

    public UpgradesObjects getEquipment() {
        return equipment;
    }
}
