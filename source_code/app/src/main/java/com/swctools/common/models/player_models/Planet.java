package com.swctools.common.models.player_models;

/**
 * Created by David on 12/03/2018.
 */

public class Planet {

    public String GAMENAME;
    public String UINAME;
    public String TYPE;

    public Planet(String gameName, String uiName, String type ){
        this.GAMENAME = gameName;
        this.UINAME = uiName;
        this.TYPE = type;
    }
}
