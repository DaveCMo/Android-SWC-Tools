package com.swctools.layouts.models;

public class MostUsedLayout {

    private int layoutId;
    private int versionId;
    private  String layoutName;
    private  String playerId;
    private int countUsed;

    public MostUsedLayout(int layoutId, int versionId, String layoutName, String playerId, int countUsed) {
        this.layoutId = layoutId;
        this.versionId = versionId;
        this.layoutName = layoutName;
        this.playerId = playerId;
        this.countUsed = countUsed;
    }

    public int getLayoutId() {
        return layoutId;
    }

    public int getVersionId() {
        return versionId;
    }

    public String getLayoutName() {
        return layoutName;
    }

    public String getPlayerId() {
        return playerId;
    }

    public int getCountUsed() {
        return countUsed;
    }

    @Override
    public String toString() {
        return "MostUsedLayout{" +
                "layoutId=" + layoutId +
                ", versionId=" + versionId +
                ", layoutName='" + layoutName + '\'' +
                ", playerId='" + playerId + '\'' +
                ", countUsed=" + countUsed +
                '}';
    }
}
