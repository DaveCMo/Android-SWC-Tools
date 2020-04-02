package com.swctools.swc_server_interactions.results;

import android.util.Log;

import javax.json.JsonArray;
import javax.json.JsonObject;

public class SWCGetWarStatusResultJson {
    private static final String TAG = "SWCGetWarStatusResultDa";
    private JsonObject result;

    public SWCGetWarStatusResultJson(JsonObject result) {
        this.result = result;
    }


    public JsonObject getResult() {
        return this.result;
    }

    public long startTime() {
        return this.getResult().getInt("startTime");
    }

    public long prepGraceStartTime() {
        return this.getResult().getInt("prepGraceStartTime");
    }

    public long prepEndTime() {
        return this.getResult().getInt("prepEndTime");
    }

    public long actionGraceStartTime() {
        return this.getResult().getInt("actionGraceStartTime");
    }

    public long actionEndTime() {
        return this.getResult().getInt("actionEndTime");
    }

    public long cooldownEndTime() {
        return this.getResult().getInt("cooldownEndTime");
    }

    public JsonArray buffBaseJsonArray() {
        JsonArray buffBaseJsonArray = this.getResult().getJsonArray("buffBases");
        return buffBaseJsonArray;
    }

    public String getRivalId() {
        return this.getResult().getJsonObject("rival").getString("guildId");
    }

    public String getRivalFaction(){
        return this.getResult().getJsonObject("rival").getString("faction");
    }

    public String getGuildFaction(){
        return this.getResult().getJsonObject("guild").getString("faction");
    }

    public JsonObject getGuildJson() {
        return this.getResult().getJsonObject("guild");
    }

    public JsonObject getRivalJson() {
        return this.getResult().getJsonObject("rival");
    }
    public JsonArray getGuildParticipants(){
        return this.getResult().getJsonObject("guild").getJsonArray("participants");
    }

    public JsonArray getRivalParticipants(){
        return this.getResult().getJsonObject("rival").getJsonArray("participants");

    }


}
