package com.swctools.common.models.player_models;

import javax.json.JsonObject;

public class Champion {
    private static final String TAG = "CHAMPION";
    private static final String DEKA_STORAGE = "troopChampion%1$sDroideka%2$s";
    private static final String DEKO_STORAGE = "troopChampion%1$sHeavyDroideka%2$s";
    private static final String DEKA_UPGRADE = "Champion%1$sDroideka";
    private static final String DEKO_UPGRADE = "Champion%1$sHeavyDroideka";
    private static final String DEKA_PLATFORM = "%1$sPlatformDroideka%2$s";
    private static final String DEKO_PLATFORM = "%1$sPlatformHeavyDroideka%2$s";

    private String gameName;
    private int amount;
    private int capacity;
    private int scale;
    private int level;

    public enum DroidekaType {
        SENTINEL, OPPRESSOR
    }
    public enum DroidekaStatus {
        READY, UPGRADING, DOWN
    }

    public Champion (JsonObject championObj){
;
    }

}
