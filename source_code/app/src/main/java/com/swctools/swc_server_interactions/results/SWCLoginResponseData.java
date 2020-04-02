package com.swctools.swc_server_interactions.results;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.json.JsonObject;

public class SWCLoginResponseData extends SWCDefaultResultData {
    private static final String TAG = "DISNEYLOGINRESPONSEDATA";

    public SWCLoginResponseData(JsonObject dataObject) {
        super(dataObject);
    }

    public JSONplayerModel mplayerModel() {
        return new JSONplayerModel(this.getResult().getJsonObject("playerModel"));
    }

    public String name() {

        try {
            return URLDecoder.decode(resultData.getJsonObject("result").getString("name"), "UTF8");
        } catch (UnsupportedEncodingException e) {
            return resultData.getJsonObject("result").getString("name");
        }

    }

    public long loginTime() {
        long lt = 0;
        try {
            lt = messages.getJsonArray("login").getJsonObject(0).getJsonObject("message").getInt("loginTime");
        } catch (Exception e) {
        }
        return lt;
    }
}
