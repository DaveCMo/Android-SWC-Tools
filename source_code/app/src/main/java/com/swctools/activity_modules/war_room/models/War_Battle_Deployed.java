package com.swctools.activity_modules.war_room.models;

public class War_Battle_Deployed {
    String battleId;
    String warId;
    String deployableType;
    String deployable;
    int deployableQty;

    public War_Battle_Deployed(String battleId, String warId, String deployableType, String deployable, int deployableQty) {
        this.battleId = battleId;
        this.warId = warId;
        this.deployableType = deployableType;
        this.deployable = deployable;
        this.deployableQty = deployableQty;
    }

    public String getBattleId() {
        return battleId;
    }

    public String getWarId() {
        return warId;
    }

    public String getDeployableType() {
        return deployableType;
    }

    public String getDeployable() {
        return deployable;
    }

    public int getDeployableQty() {
        return deployableQty;
    }

    @Override
    public String toString() {
        return "War_Battle_Deployed{" +
                "battleId='" + battleId + '\'' +
                ", warId='" + warId + '\'' +
                ", deployableType='" + deployableType + '\'' +
                ", deployable='" + deployable + '\'' +
                ", deployableQty='" + deployableQty + '\'' +
                '}';
    }
}
