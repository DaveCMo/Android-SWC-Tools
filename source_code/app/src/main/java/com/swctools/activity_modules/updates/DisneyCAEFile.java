package com.swctools.activity_modules.updates;

import com.swctools.util.StringUtil;

import javax.json.JsonArray;
import javax.json.JsonObject;

public class DisneyCAEFile {
    private static final String TAG = "DISNEYCAEFILE";
    private JsonArray tournamentData;
    private String rawJson;
    private JsonObject caeObj;

    public DisneyCAEFile(String rawJson){
        this.rawJson = rawJson.replaceAll("\\\\r\\\\n|\\\\r|\\\\n", "");
        try {
            caeObj = StringUtil.stringToJsonObject(this.rawJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JsonArray getTournamentData(){
        return caeObj.getJsonObject("content").getJsonObject("objects").getJsonArray("TournamentData");
    }
}
