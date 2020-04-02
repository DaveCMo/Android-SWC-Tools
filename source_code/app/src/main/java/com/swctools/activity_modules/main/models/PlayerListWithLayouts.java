package com.swctools.activity_modules.main.models;

import com.swctools.layouts.models.FavouriteLayoutItem;

import java.util.List;

public class PlayerListWithLayouts {
    private List<PlayerDAO_WithLayouts> playerDAOList;
    private List<FavouriteLayoutItem> favouriteLayoutItemList;


    public PlayerListWithLayouts(List<PlayerDAO_WithLayouts> playerDAOList, List<FavouriteLayoutItem> favouriteLayoutItemList) {
        this.playerDAOList = playerDAOList;
        this.favouriteLayoutItemList = favouriteLayoutItemList;
    }

    public List<PlayerDAO_WithLayouts> getPlayerDAOList() {
        return playerDAOList;
    }

    public List<FavouriteLayoutItem> getFavouriteLayoutItemList() {
        return favouriteLayoutItemList;
    }
}
