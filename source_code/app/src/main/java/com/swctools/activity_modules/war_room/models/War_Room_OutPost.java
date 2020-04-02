package com.swctools.activity_modules.war_room.models;

public class War_Room_OutPost {
    private String ownerId;
    private String buffUid;
    private String outPostName;
    private int level;

    public War_Room_OutPost(String ownerId, String buffUid, String outPostName, int level) {
        this.ownerId = ownerId;
        this.buffUid = buffUid;
        this.outPostName = outPostName;
        this.level = level;
    }


    public String getOwnerId() {
        return ownerId;
    }

    public String getBuffUid() {
        return buffUid;
    }

    public String getOutPostName() {
        return outPostName;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return "WarOutPost{" +
                "ownerId='" + ownerId + '\'' +
                ", buffUid='" + buffUid + '\'' +
                ", outPostName='" + outPostName + '\'' +
                ", level=" + level +
                '}';
    }
}
