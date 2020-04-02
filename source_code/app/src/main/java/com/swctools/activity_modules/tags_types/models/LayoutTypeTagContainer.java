package com.swctools.activity_modules.tags_types.models;


public class LayoutTypeTagContainer {

    private int tagId;
    private String tName;

    public LayoutTypeTagContainer(int id, String name){
        this.tagId = id;
        this.tName = name;
    }

    public int getTagId() {
        return tagId;
    }
    public String getTName() {
        return tName;
    }
}
