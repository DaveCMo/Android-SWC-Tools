package com.swctools.activity_modules.player.models;

public class BuildingDAO {
    public String game_name;
    public String generic_name;
    public String ui_name;
    public String faction;
    public String type;
    public boolean isTrap;
    public boolean isJunk;
    public int cap;
    public int level;

    public BuildingDAO (String game_name, String generic_name, String ui_name, String faction, String type, boolean isTrap, boolean isJunk, int cap, int level){

        this.game_name = game_name;
        this.generic_name = generic_name;
        this.ui_name = ui_name;
        this.faction = faction;
        this.type = type;
        this.isTrap = isTrap;
        this.isJunk = isJunk;
        this.cap = cap;
        this.level = level;

    }


}
