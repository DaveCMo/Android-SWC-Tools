package com.swctools.layouts.models;

public class FavouriteLayoutItemSimple {

    private int layoutId;
    private int layoutVersion;
    private String layoutName;



    public FavouriteLayoutItemSimple(int id, int vers, String n) {

        this.layoutId = id;
        this.layoutVersion = vers;
        this.layoutName = n;

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


}
