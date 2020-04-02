package com.swctools.swc_server_interactions.results;

import com.swctools.common.helpers.DateTimeHelper;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 * Created by davec on 28/03/2018.
 */

public class JSONplayerModel {
    private static final String TAG = "JSONplayerModel";
    public JsonObject playerModel;

    public JSONplayerModel(JsonObject playerModel) {
        this.playerModel = playerModel;
    }

    public JsonObject guildInfo()  {
        try {
            JsonObject gInfo = playerModelObject("guildInfo");
            return gInfo;
        } catch (Exception e) {
            JsonObjectBuilder blankG = Json.createObjectBuilder();;
            return blankG.build();
        }

    }

    public JsonArray unlockedPlanets(){
        return playerModel.getJsonArray("unlockedPlanets");
    }
    public JsonObject donatedTroops() {
        return playerModelObject("donatedTroops");
    }

    public JsonObject map() {
        return playerModelObject("map");
    }

    public JsonObject activeArmory() {
        return playerModelObject("activeArmory");
    }

    public JsonArray battleLogs() {
        return playerModel.getJsonArray("battleLogs");
    }

    public JsonObject inventory() {
        return playerModelObject("inventory");
    }

    public JsonObject upgrades() {
        return playerModelObject("upgrades");
    }

    public String faction() {
        return playerModel.getString("faction");
    }

    public int protectedUntil() {
        int val;
        try {
            val = playerModel.getInt("protectedUntil");
            long nowT = DateTimeHelper.userIDTimeStamp() /1000;
            if(val<nowT){
                val = 0;
            }
        } catch (Exception e) {
            val = 0;
        }
        return val;
    }



    public JsonArray contracts(){
        return playerModel.getJsonArray("contracts");
    }

    public String toString() {
        return playerModel.toString();
    }

    private JsonObject playerModelObject(String objectString) {
        return playerModel.getJsonObject(objectString);
    }



}
