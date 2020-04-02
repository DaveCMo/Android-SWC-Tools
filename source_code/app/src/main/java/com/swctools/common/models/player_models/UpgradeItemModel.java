package com.swctools.common.models.player_models;

public class UpgradeItemModel {


    private String uid;
    private int currentLevel;
    private int maxLevel;
    private String logName;

    public UpgradeItemModel(String uid, int currentLevel, int maxLevel) {
        this.uid = uid;
        this.currentLevel = currentLevel;
        this.maxLevel = maxLevel;
    }


    public String getUid() {
        return uid;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public int getMaxLevel() {
        return maxLevel;
    }


    public String getLogName() {
        return logName;
    }

    public void setLogName(String logName) {
        this.logName = logName;
    }

    @Override
    public String toString() {
        return "UpgradeItemModel{" +
                "uid='" + uid + '\'' +
                ", currentLevel=" + currentLevel +
                ", maxLevel=" + maxLevel +
                '}';
    }
}
