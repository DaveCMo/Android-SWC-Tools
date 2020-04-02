package com.swctools.swc_server_interactions.results;

import javax.json.JsonArray;
import javax.json.JsonObject;

public class SWCSearchGuildResultData extends SWCDefaultResultDataAbstract {
    public JsonArray result;
    public SWCSearchGuildResultData(JsonObject dataObject) {
        super(dataObject);
    }

    @Override
    void setResult() {
        try {
            this.result = this.resultData.getJsonArray("result");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
