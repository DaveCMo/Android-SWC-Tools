package com.swctools.activity_modules.main.models;

import com.swctools.layouts.models.FavouriteLayoutItem;

import java.util.ArrayList;
import java.util.List;

public class PlayerDAO_WithLayouts {
    private static final String TAG = "PlayerDAO";

    private int db_rowId;
    private String playerId;
    private String playerSecret;
    private String playerName;
    private String playerFaction;
    private String playerGuild;
    private String notifications;
    private int refresh_interval;
    private String deviceId;
    private String card_default;
    private int card_expanded = 0;
    private List<FavouriteLayoutItem> topLayoutItemList = new ArrayList<>();
    private List<FavouriteLayoutItem> lastUsedLayoutItemList = new ArrayList<>();
    private List<FavouriteLayoutItem> mostUsedLayoutItemList = new ArrayList<>();

    public List<FavouriteLayoutItem> getTopLayoutItemList() {
        return topLayoutItemList;
    }

    public List<FavouriteLayoutItem> getLastUsedLayoutItemList() {
        return lastUsedLayoutItemList;
    }

    public List<FavouriteLayoutItem> getMostUsedLayoutItemList() {
        return mostUsedLayoutItemList;
    }

    public PlayerDAO_WithLayouts(int db_rowId, String playerId, String playerSecret, String playerName, String playerFaction, String playerGuild, String notifications, int refresh_interval, String deviceId, String card_default, int card_expanded) {
        this.db_rowId = db_rowId;
        this.playerId = playerId;
        this.playerSecret = playerSecret;
        this.playerName = playerName;
        this.playerFaction = playerFaction;
        this.playerGuild = playerGuild;
        this.notifications = notifications;
        this.refresh_interval = refresh_interval;
        this.deviceId = deviceId;
        this.card_default = card_default;
        this.card_expanded = card_expanded;
    }

    public int getDb_rowId() {
        return this.db_rowId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerFaction() {
        return playerFaction;
    }

    public void setPlayerFaction(String playerFaction) {
        this.playerFaction = playerFaction;
    }

    public String getPlayerGuild() {
        return playerGuild;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getFavDefault() {
        return card_default;
    }

    public String getPlayerSecret() {
        return playerSecret;
    }

    public int getExpanded() {
        return card_expanded;
    }

    public void setTopLayoutItemList(List<FavouriteLayoutItem> topLayoutItemList) {
        this.topLayoutItemList = topLayoutItemList;
    }

    public void setLastUsedLayoutItemList(List<FavouriteLayoutItem> lastUsedLayoutItemList) {
        this.lastUsedLayoutItemList = lastUsedLayoutItemList;
    }

    public void setMostUsedLayoutItemList(List<FavouriteLayoutItem> mostUsedLayoutItemList) {
        this.mostUsedLayoutItemList = mostUsedLayoutItemList;
    }
}
