package com.swctools.activity_modules.war_room.models;

public class War_Log {
    private String warId;
    private String playerId;
    private String guildId;
    private String guildName;
    private String guildFaction;
    private String rivalGuildId;
    private String rivalGuildName;
    private String rivalGuildFaction;

    private int squadAttacks;
    private int rivalAttacks;
    private int squadScore;
    private int rivalScore;
    private int squadWipes;
    private int rivalWipes;

    private long startTime;
    private long prepGraceStartTime;
    private long prepEndTime;
    private long actionGraceStartTime;
    private long actionEndTime;
    private long cooldownEndTime;

    public War_Log(String warId, String playerId, String guildId, String guildName, String guildFaction, String rivalGuildId, String rivalGuildName, String rivalGuildFaction, long startTime, long prepGraceStartTime, long prepEndTime, long actionGraceStartTime, long actionEndTime, long cooldownEndTime) {
        this.warId = warId;
        this.playerId = playerId;
        this.guildId = guildId;
        this.guildName = guildName;
        this.guildFaction = guildFaction;
        this.rivalGuildId = rivalGuildId;
        this.rivalGuildName = rivalGuildName;
        this.rivalGuildFaction = rivalGuildFaction;
        this.startTime = startTime;
        this.prepGraceStartTime = prepGraceStartTime;
        this.prepEndTime = prepEndTime;
        this.actionGraceStartTime = actionGraceStartTime;
        this.actionEndTime = actionEndTime;
        this.cooldownEndTime = cooldownEndTime;
    }

    public void setAttacks(int squadAttacks, int rivalAttacks, int squadScore, int rivalScore, int squadWipes, int rivalWipes) {
        this.squadAttacks = squadAttacks;
        this.rivalAttacks = rivalAttacks;
        this.squadScore = squadScore;
        this.rivalScore = rivalScore;
        this.squadWipes = squadWipes;
        this.rivalWipes = rivalWipes;
    }


    public String getWarId() {
        return warId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public String getGuildId() {
        return guildId;
    }

    public String getGuildName() {
        return guildName;
    }

    public String getGuildFaction() {
        return guildFaction;
    }

    public String getRivalGuildId() {
        return rivalGuildId;
    }

    public String getRivalGuildName() {
        return rivalGuildName;
    }

    public String getRivalGuildFaction() {
        return rivalGuildFaction;
    }

    public int getSquadAttacks() {
        return squadAttacks;
    }

    public int getRivalAttacks() {
        return rivalAttacks;
    }

    public int getSquadScore() {
        return squadScore;
    }

    public int getRivalScore() {
        return rivalScore;
    }

    public int getSquadWipes() {
        return squadWipes;
    }

    public int getRivalWipes() {
        return rivalWipes;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getPrepGraceStartTime() {
        return prepGraceStartTime;
    }

    public long getPrepEndTime() {
        return prepEndTime;
    }

    public long getActionGraceStartTime() {
        return actionGraceStartTime;
    }

    public long getActionEndTime() {
        return actionEndTime;
    }

    public long getCooldownEndTime() {
        return cooldownEndTime;
    }

    @Override
    public String toString() {
        return "War_Log{" +
                "warId='" + warId + '\'' +
                ", playerId='" + playerId + '\'' +
                ", guildId='" + guildId + '\'' +
                ", guildName='" + guildName + '\'' +
                ", guildFaction='" + guildFaction + '\'' +
                ", rivalGuildId='" + rivalGuildId + '\'' +
                ", rivalGuildName='" + rivalGuildName + '\'' +
                ", rivalGuildFaction='" + rivalGuildFaction + '\'' +
                ", squadAttacks=" + squadAttacks +
                ", rivalAttacks=" + rivalAttacks +
                ", squadScore=" + squadScore +
                ", rivalScore=" + rivalScore +
                ", squadWipes=" + squadWipes +
                ", rivalWipes=" + rivalWipes +
                ", startTime=" + startTime +
                ", prepGraceStartTime=" + prepGraceStartTime +
                ", prepEndTime=" + prepEndTime +
                ", actionGraceStartTime=" + actionGraceStartTime +
                ", actionEndTime=" + actionEndTime +
                ", cooldownEndTime=" + cooldownEndTime +
                '}';
    }
}
