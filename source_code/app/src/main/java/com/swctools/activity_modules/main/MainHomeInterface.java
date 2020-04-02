package com.swctools.activity_modules.main;

import com.swctools.layouts.models.FavouriteLayoutItem;
import com.swctools.activity_modules.main.models.PlayerDAO_WithLayouts;

import java.util.List;

public interface MainHomeInterface {
    void startedBuildingPlayerList();

    void finishedBuildingPlayerList(List<PlayerDAO_WithLayouts> playerDAOList, List<FavouriteLayoutItem> favouriteLayoutItemList);

}
