package com.swctools.activity_modules.war_room.models;

public class War_SC_Contents {

    private String warId;
    private String playerId;
    private String ui_name;
    private int qty;
    private int cap;
    private int unit_level;
    private String donated_by_name;


    public War_SC_Contents(String warId, String playerId, String ui_name, int qty, int cap, int unit_level, String donated_by_name) {
        this.warId = warId;
        this.playerId = playerId;
        this.ui_name = ui_name;
        this.qty = qty;
        this.cap = cap;
        this.unit_level = unit_level;
        this.donated_by_name = donated_by_name;
    }

    public String getWarId() {
        return warId;
    }

    public String getPlayerId() {
        return playerId;
    }


    public String getUi_name() {
        return ui_name;
    }

    public int getQty() {
        return qty;
    }

    public int getCap() {
        return cap;
    }

    public int getUnit_level() {
        return unit_level;
    }


    public String getDonated_by_name() {
        return donated_by_name;
    }

    @Override
    public String toString() {
        return "War_SC_Contents{" +
                "warId='" + warId + '\'' +
                ", playerId='" + playerId + '\'' +
                ", ui_name='" + ui_name + '\'' +
                ", qty=" + qty +
                ", cap=" + cap +
                ", unit_level=" + unit_level +
                ", donated_by_name='" + donated_by_name + '\'' +
                '}';
    }
}
