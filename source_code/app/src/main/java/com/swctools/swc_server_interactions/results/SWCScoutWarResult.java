package com.swctools.swc_server_interactions.results;

import javax.json.JsonObject;

public class SWCScoutWarResult extends SWCDefaultResultData {

    public  JsonObject warMap;
    public SWCScoutWarResult(JsonObject dataObject) {
        super(dataObject);
        warMap = getResult().getJsonObject("warMap");

    }

    public SWCScoutWarResult(String dataObjString) {
        super(dataObjString);
        warMap = getResult().getJsonObject("warMap");
    }
}
