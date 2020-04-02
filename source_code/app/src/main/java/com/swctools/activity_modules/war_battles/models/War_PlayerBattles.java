package com.swctools.activity_modules.war_battles.models;

import com.swctools.util.StringUtil;

public class War_PlayerBattles {
    private String playerId;
    private String playerName;

    public War_PlayerBattles(String playerId, String playerName) {
        this.playerId = playerId;
        this.playerName = playerName;
    }

    public String getPlayerId() {
        return playerId;
    }

    public String getPlayerName() {
        return StringUtil.htmlRemovedGameName(playerName);
    }

    @Override
    public String toString() {
        return "War_PlayerBattles{" +
                "playerId='" + playerId + '\'' +
                ", playerName='" + playerName + '\'' +
                '}';
    }
}
