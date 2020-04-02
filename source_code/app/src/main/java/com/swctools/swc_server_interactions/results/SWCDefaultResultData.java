package com.swctools.swc_server_interactions.results;

import javax.json.JsonArray;
import javax.json.JsonObject;

public class SWCDefaultResultData extends SWCDefaultResultDataAbstract {
    private static final String TAG = "SWCDefaultResultData";
    protected JsonObject result;

    public SWCDefaultResultData() {

    }

    public SWCDefaultResultData(String dataObjString) {
        super(dataObjString);


    }

    public SWCDefaultResultData(JsonObject dataObject) {
        super(dataObject);
    }

    public JsonObject getResult() {
        return resultData.getJsonObject("result");
    }

    public JsonArray getResultArray() {
        return resultData.getJsonArray("result");
    }

    @Override
    void setResult() {
//        if(this.resultData.get("result"))
        try {
            this.result = resultData.getJsonObject("result");
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }


}
