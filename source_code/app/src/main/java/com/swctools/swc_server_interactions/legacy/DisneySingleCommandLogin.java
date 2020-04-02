package com.swctools.swc_server_interactions.legacy;

import com.swctools.swc_server_interactions.results.JSONplayerModel;

import java.net.URLDecoder;

public class DisneySingleCommandLogin extends DisneySingleCommandResponse {
    private static final String TAG = "JSONLogin";
    public JSONplayerModel playerModel;

    public DisneySingleCommandLogin(String response){
        super(response);
        playerModel = new JSONplayerModel(data.result().getJsonObject("playerModel"));
    }
    public String name (){

        try {
            return URLDecoder.decode(data.result().getString("name"), "UTF8");
        } catch (Exception e) {
            try {
                return data.result().getString("name");
            } catch (Exception e1) {
                return  "";
            }
        }

    }

    public long loginTime(){
        long lt = 0;
        try {
            lt = data.messages().getJsonArray("login").getJsonObject(0).getJsonObject("message").getInt("loginTime");

        } catch (Exception e){

        }
        return lt;
    }

    public String faction(){
        return  playerModel.playerModel.getString("faction");
    }


}
