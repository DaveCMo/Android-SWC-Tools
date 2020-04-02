package com.swctools.layouts.models;

public class LayoutDetailMainData {

    private String layoutName, playerId, layoutFaction, imageStr, layoutIsFav;


    public LayoutDetailMainData(String n, String id, String imgS, String pFact, String isFav) {
        this.layoutName = n;

        this.playerId = id;
        this.imageStr = imgS;
        this.layoutFaction = pFact;

        this.layoutIsFav = isFav;
    }

    public String getLayoutName() {
        return layoutName;
    }

//    public String getPlayerName() {
//        return playerName;
//    }

    public String getLayoutFaction() {
        return layoutFaction;
    }

    public String getImageStr() {
        return imageStr;
    }

    public String getLayoutIsFav() {
        return layoutIsFav;
    }

    public String getPlayerId() {
        return playerId;
    }
}
