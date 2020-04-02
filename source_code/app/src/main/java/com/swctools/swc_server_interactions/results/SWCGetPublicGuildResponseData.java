package com.swctools.swc_server_interactions.results;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;

public class SWCGetPublicGuildResponseData extends SWCDefaultResultData {
    private JsonArray members;
    private JsonArray warHistory;
    private String name;

    public SWCGetPublicGuildResponseData(JsonObject dataObject) {
        super(dataObject);
        members = this.getResult().getJsonArray("members");
        warHistory = this.getResult().getJsonArray("warHistory");

    }

    public SWCGetPublicGuildResponseData(String dataObjString) {
        super(dataObjString);
        members = this.getResult().getJsonArray("members");
        warHistory = this.getResult().getJsonArray("warHistory");
    }

    public JsonArray getMembers() {
        return members;
    }

    public JsonArray getWarHistory() {
        return warHistory;
    }

    public String getcurrentWarId() {
        return this.getResult().getString("currentWarId");
    }

    public JsonValue getCurrentWarIdJSONValue() {
        return this.getResult().get("currentWarId");
    }


    public String getName() {
        return this.getResult().getString("name");
    }

    public String toString() {
        return resultData.toString();
    }
}
