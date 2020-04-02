package com.swctools.swc_server_interactions.legacy;

import com.swctools.swc_server_interactions.results.JSONplayerModel;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by davec on 28/03/2018.
 */

public class DisneySingleCommandVisitor extends DisneySingleCommandResponse {
    private static final String TAG = "JSONVisitor";
    public JSONplayerModel playerModel;

    public DisneySingleCommandVisitor(String response) {
        super(response);
        try {
            playerModel = new JSONplayerModel(data.result().getJsonObject("player").getJsonObject("playerModel"));
        } catch (Exception e) {
            mException =e;
        }

    }

    public String name() {

        try {
            return URLDecoder.decode(data.result().getJsonObject("player").getString("name"), "UTF8");
        } catch (UnsupportedEncodingException e) {
            return data.result().getJsonObject("player").getString("name");
        }

    }

    public String playerId() {
        return data.result().getJsonObject("player").getString("playerId");
    }

    public String faction() {
        return playerModel.playerModel.getString("faction");
    }

}
