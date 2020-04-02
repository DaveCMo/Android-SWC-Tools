package com.swctools.common.models.player_models;

import android.content.Context;

import com.swctools.common.enums.Droidekas;
import com.swctools.swc_server_interactions.results.JSONplayerModel;

import java.io.Serializable;
import java.io.StringReader;
import java.text.DecimalFormat;

import javax.json.Json;
import javax.json.JsonObject;

public class Inventory implements Serializable {
    private static final String TAG = "Inventory";
    private static final String DEKA_STORAGE = "troopChampion%1$sDroideka%2$s";
    private static final String DEKO_STORAGE = "troopChampion%1$sHeavyDroideka%2$s";
    private static final String DEKA_UPGRADE = "Champion%1$sDroideka";
    private static final String DEKO_UPGRADE = "Champion%1$sHeavyDroideka";
    private static final String DEKA_PLATFORM = "%1$sPlatformDroideka%2$s";
    private static final String DEKO_PLATFORM = "%1$sPlatformHeavyDroideka%2$s";
    public Storage credits;
    public Storage materials;
    public Storage contraband;
    public Storage crystals;
    public Storage reputation;
    private JsonObject inventoryObject;
    private JsonObject champion;
    private Upgrades upgrades;
    private DekoDeka droidekaOp;
    private DekoDeka droidekaSent;
    private String faction;
    private Context context;
    //    private
    private JSONplayerModel playerModel;
    //VISIT_KEY("%1$s|%2$s"),


    public Inventory(JSONplayerModel playerModel, Context context) {

        this.playerModel = playerModel;
        this.context = context;
        this.inventoryObject = playerModel.inventory();
        if (playerModel.faction().equalsIgnoreCase("Rebel")) {
            this.faction = "Rebel";
        } else if (playerModel.faction().equalsIgnoreCase("Empire")) {
            this.faction = "Empire";
        }
        this.upgrades = new Upgrades(playerModel.upgrades(), this.faction);
        buildStorageObj(inventoryObject);
        droidekaOp = new DekoDeka();
        droidekaSent = new DekoDeka();
        processDekas(this.faction);
    }

    private void processDekas(String faction) {
        champion = inventoryObject.getJsonObject("subStorage").getJsonObject("champion");

        for (UpgradeItems upgradeItems : upgrades.getTroop().getUpgradeItems()) {

            if (upgradeItems.itemName.equalsIgnoreCase(String.format(DEKA_UPGRADE, faction))) {
                try {
//
                    droidekaSent.built = true;
                    droidekaSent.level = upgradeItems.itemLevel;
//
                    JsonObject sentinalObj = champion.getJsonObject("storage").getJsonObject(String.format(DEKA_STORAGE, faction, droidekaSent.level));
//                  Champion championClass = new Champion(sentinalObj);
                    droidekaSent.amount = sentinalObj.getInt("amount");
                    droidekaSent.capacity = sentinalObj.getInt("capacity");
                    droidekaSent.droidekasType = Droidekas.SENTINEL.toString();
//                    droidekaSent.type = DroidType.SENTINAL;
                    droidekaSent.setState(DEKA_STORAGE, DEKA_PLATFORM, faction, playerModel);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (upgradeItems.itemName.equalsIgnoreCase(String.format(DEKO_UPGRADE, faction))) {
                try {
                    droidekaOp.built = true;
                    droidekaOp.level = upgradeItems.itemLevel;
                    JsonObject opObj = champion.getJsonObject("storage").getJsonObject(String.format(DEKO_STORAGE, faction, droidekaOp.level));
//
                    droidekaOp.amount = opObj.getInt("amount");
                    droidekaOp.capacity = opObj.getInt("capacity");
                    droidekaOp.droidekasType = Droidekas.OPPRESSOR.toString();
//                    droidekaOp.type = DroidType.OPPRESSOR;
                    droidekaOp.setState(DEKO_STORAGE, DEKO_PLATFORM, faction, playerModel);
//                    droidekaOp.setDekaImage(droidekaOp.level);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public DekoDeka getDroidekaOp() {
        return droidekaOp;
    }

    public DekoDeka getDroidekaSent() {
        return droidekaSent;
    }

    private void buildStorageObj(JsonObject inventoryObject) {
        JsonObject storageObject = inventoryObject.getJsonObject("storage");
        credits = new Storage(storageObject.getJsonObject("credits").toString());
        materials = new Storage(storageObject.getJsonObject("materials").toString());
        contraband = new Storage(storageObject.getJsonObject("contraband").toString());
        crystals = new Storage(storageObject.getJsonObject("crystals").toString());
        reputation = new Storage(storageObject.getJsonObject("reputation").toString());
    }


    public class Storage {
        private int amount;
        private int capacity;

        public Storage(String jsonObject) {
            JsonObject storageObject = Json.createReader(new StringReader(jsonObject)).readObject();
            amount = storageObject.getInt("amount");
            capacity = storageObject.getInt("capacity");
        }

        public int getAmount() {
            return this.amount;
        }

        public int getCapacity() {
            return this.capacity;
        }

        public String geCapacityComma() {
            DecimalFormat formatter = new DecimalFormat("#,###,###");
            return formatter.format(getCapacity());
        }

        public String getAmountComma() {
            DecimalFormat formatter = new DecimalFormat("#,###,###");
            return formatter.format(getAmount());
        }


    }
}

