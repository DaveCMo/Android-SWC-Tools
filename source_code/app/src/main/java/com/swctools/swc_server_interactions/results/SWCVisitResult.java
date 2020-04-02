package com.swctools.swc_server_interactions.results;

import com.swctools.common.models.player_models.Scalars;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.json.JsonObject;

public class SWCVisitResult extends SWCDefaultResultData {
    private static final String TAG = "DisneyVisitResponse";

    public SWCVisitResult(JsonObject dataObject) {
        super(dataObject);

    }

    public SWCVisitResult(String dataobjStr) {
        super(dataobjStr);
    }

    public JSONplayerModel mplayerModel() {
        return new JSONplayerModel(this.getResult().getJsonObject("player").getJsonObject("playerModel"));
    }

    public Scalars scalars() {
        return new Scalars(this.getResult().getJsonObject("player").getJsonObject("scalars").toString());
    }

    public String name() {

        try {
            return URLDecoder.decode(this.getResult().getJsonObject("player").getString("name"), "UTF8");
        } catch (UnsupportedEncodingException e) {
            return this.getResult().getJsonObject("player").getString("name");
        }

    }

    public String playerId() {
        return this.getResult().getJsonObject("player").getString("playerId");
    }
}
