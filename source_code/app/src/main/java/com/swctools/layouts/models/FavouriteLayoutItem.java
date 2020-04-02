package com.swctools.layouts.models;

import android.util.Log;

public class FavouriteLayoutItem {
    public static final String ALLFAV = "ALLFAV";
    public static final String PLAYERFAV = "PLAYERFAV";
    public static final String LASTUSED = "LASTUSED";
    public static final String MOSTUSED = "MOSTUSED";


    private int favouriteId;
    private int layoutId;
    private int layoutVersion;
    private String layoutName;
    private String imagePath;
    private String type;

    public FavouriteLayoutItem(int favouriteId, int layoutId, int layoutVersion, String layoutName, String imagePath, String type) {
        this.favouriteId = favouriteId;

        this.layoutId = layoutId;
        this.layoutVersion = layoutVersion;
        this.layoutName = layoutName;
        this.imagePath = imagePath;
        this.type = type;
    }

    public int getLayoutId() {
        return layoutId;
    }

    public int getLayoutVersion() {
        return layoutVersion;
    }

    public String getLayoutName() {
        return layoutName;
    }


    public int getFavouriteId() {
        return favouriteId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getType() {
        return type;
    }
}
