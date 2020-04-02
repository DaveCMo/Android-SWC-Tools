package com.swctools.interfaces;

public interface FavouriteLayoutRowItemInterface {

    void removeFavourite(int layoutId);

    void deleteMostLastUsedLayoutLog(int layoutId, int layoutVersion);

    void deleteTopLayout(String player, int layoutId);
}