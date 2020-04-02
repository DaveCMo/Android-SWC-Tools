package com.swctools.swc_server_interactions.results;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;

public class SWCDefaultResponse {
    private static final String TAG = "SWCDefaultResponse";
    int protocolVersion;
    JsonArray data;
    long serverTimestamp;
    String serverTime;
    String rawJsonResponseString;
    public Exception mException;

    public SWCDefaultResponse(String rawJsonResponseString) {
        this.rawJsonResponseString = rawJsonResponseString;
        //now build a JSON object
        nowBuildAJSONObject();
    }

    private void nowBuildAJSONObject() {
        try {

            JsonObject responseJsonObj = Json.createReader(new StringReader(this.rawJsonResponseString)).readObject();
            this.protocolVersion = responseJsonObj.getInt("protocolVersion");
            this.serverTimestamp = Long.parseLong(responseJsonObj.getJsonNumber("serverTimestamp").toString());
            this.serverTime = responseJsonObj.getString("serverTime");
            this.data = responseJsonObj.getJsonArray("data");
        } catch (Exception e) {
            e.printStackTrace();
            mException = e;
        }
    }

//    public SWCDefaultResponse(SWCMessage swcMessage, Context context) throws Exception{
//
//        try {
//            this.rawJsonResponseString = MessageManager.sendMessage(swcMessage, context);
//            nowBuildAJSONObject();
//        } catch (Exception e) {
//            e.printStackTrace();
//            mException = e;
//            throw mException;
//        }
//
//    }

    public JsonObject getResponseDataByRequestId(int requestId) throws Exception {
        //iterate over result array to find the object matching the request id:
        nowBuildAJSONObject();
        for (int i = 0; i < this.data.size(); i++) {
            JsonObject dataArrayItem = this.data.getJsonObject(i);
            int objRequestId = dataArrayItem.getInt("requestId");
            if (objRequestId == requestId) {
                return dataArrayItem;
            }
        }
        throw new Exception("Could not find result object in response JSON!");
    }

    public String getResponseRawJsonStr() {
        return this.rawJsonResponseString;
    }
}
