package com.swctools.swc_server_interactions.results;

import javax.json.JsonArray;
import javax.json.JsonObject;

public class SWCGuildWarPartRespData {
    private static final String TAG = "SWCGuildWarPartRespData";
    public JsonObject resultData;

    public SWCGuildWarPartRespData(JsonObject dataObject) {
        resultData = dataObject;

    }

//    @Override
//    public JsonObject getResult() {
//        return this.resultData;
//    }


    public JsonObject warMap() {

        return resultData.getJsonObject("warMap");
    }

    public JsonArray warBuildings() {
        return warMap().getJsonArray("buildings");
    }

    public JsonArray creatureTraps() {
        return resultData.getJsonArray("creatureTraps");
    }

    public JsonObject donatedTroops(){
        return resultData.getJsonObject("donatedTroops");
    }
}
