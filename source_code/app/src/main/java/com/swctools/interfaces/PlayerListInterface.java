package com.swctools.interfaces;

public interface PlayerListInterface {

    void viewPlayer(String playerId);
    void savePVP(String playerId);
    void saveWar(String playerId);
    void deletePlayer(int rowId);
    void viewPlayerConfig(String playerId);
    void setExpanded(String playerId, int b);
    void setPlayerFavouriteList(String playerId, String listOption);
    void getWarStatus(String playerId);
}
