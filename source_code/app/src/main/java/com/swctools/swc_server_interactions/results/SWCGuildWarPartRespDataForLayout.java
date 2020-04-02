package com.swctools.swc_server_interactions.results;

import javax.json.JsonArray;
import javax.json.JsonObject;

public class SWCGuildWarPartRespDataForLayout extends SWCDefaultResultData {
private static final String TAG ="RespDataForLayout";

    public SWCGuildWarPartRespDataForLayout(JsonObject dataObject) {
        super(dataObject);

    }

    @Override
    public JsonObject getResult() {
        return this.resultData.getJsonObject("result");
    }

    public JsonObject warMap() {
        return getResult().getJsonObject("warMap");
    }

    public JsonArray warBuildings() {
        return warMap().getJsonArray("buildings");
    }

    public JsonArray creatureTraps() {
        return getResult().getJsonArray("creatureTraps");
    }

    public JsonObject donatedTroops() {
        return getResult().getJsonObject("donatedTroops");
    }
}