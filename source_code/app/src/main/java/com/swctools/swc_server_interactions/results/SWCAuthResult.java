package com.swctools.swc_server_interactions.results;

import javax.json.JsonObject;

public class SWCAuthResult extends SWCDefaultResultDataAbstract {
public String result;
    public SWCAuthResult(JsonObject dataObject) {
        super(dataObject);
    }

    @Override
    void setResult() {
        result = resultData.getString("result");
    }

    public SWCAuthResult(String dataObjString) {
        super(dataObjString);
    }

    public String getAuthKey(){
        return this.resultData.getString("result");
    }


}
