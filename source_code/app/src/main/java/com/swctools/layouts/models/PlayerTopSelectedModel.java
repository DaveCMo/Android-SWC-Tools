package com.swctools.layouts.models;

public class PlayerTopSelectedModel {

    public String playerId;
    public String playerName;
    public String playerFaction;
    public boolean selected = false;


    public PlayerTopSelectedModel(String playerId, String playerName, String playerFaction) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.playerFaction = playerFaction;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}
