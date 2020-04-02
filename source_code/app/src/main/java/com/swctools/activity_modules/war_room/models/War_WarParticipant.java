package com.swctools.activity_modules.war_room.models;

import com.swctools.util.StringUtil;

public class War_WarParticipant {
    private String warId;
    private String playerId;
    private String guildId;
    private String playerName;
    private String is_requester;
    private int baseScore;
    private int hqLevel;
    private int turns;
    private int victoryPoints;
    private int score;
    private int scCap;
    private int scCap_Donated;

    private int lastAttacked;
    private String lastAttackedBy;
    private String lastBattleId;
    private String lastAttackedByName;
    private String faction;
    private String isErrorWithSC;

    public War_WarParticipant(String warId, String playerId, String guildId, String playerName, String is_requester, int baseScore, int hqLevel, int turns, int victoryPoints, int score, int scCap, int scCap_Donated, int lastAttacked, String lastAttackedBy, String lastBattleId, String lastAttackedByName, String faction, String isErrorWithSC) {
        this.warId = warId;
        this.playerId = playerId;
        this.guildId = guildId;
        this.playerName = playerName;
        this.is_requester = is_requester;
        this.baseScore = baseScore;
        this.hqLevel = hqLevel;
        this.turns = turns;
        this.victoryPoints = victoryPoints;
        this.score = score;
        this.scCap = scCap;
        this.scCap_Donated = scCap_Donated;
        this.lastAttacked = lastAttacked;
        this.lastAttackedBy = lastAttackedBy;
        this.lastBattleId = lastBattleId;
        this.lastAttackedByName = lastAttackedByName;
        this.faction = faction;
        this.isErrorWithSC = isErrorWithSC;
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

    public String getPlayerName() {
        return playerName;
    }
    public String getCleanName(){
        return StringUtil.htmlRemovedGameName(playerName);
    }


    public String getIs_requester() {
        return is_requester;
    }

    public int getBaseScore() {
        return baseScore;
    }

    public int getHqLevel() {
        return hqLevel;
    }

    public int getTurns() {
        return turns;
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }

    public int getScore() {
        return score;
    }

    public int getScCap() {
        return scCap;
    }

    public int getScCap_Donated() {
        return scCap_Donated;
    }

    public int getLastAttacked() {
        return lastAttacked;
    }

    public String getLastAttackedBy() {
        return lastAttackedBy;
    }

    public String getLastBattleId() {
        return lastBattleId;
    }

    public String getLastAttackedByName() {
        return StringUtil.htmlRemovedGameName(lastAttackedByName);
    }

    public String getFaction() {
        return faction;
    }

    public String getIsErrorWithSC() {
        return isErrorWithSC;
    }

    @Override
    public String toString() {
        return "War_WarParticipant{" +
                "warId='" + warId + '\'' +
                ", playerId='" + playerId + '\'' +
                ", guildId='" + guildId + '\'' +
                ", playerName='" + playerName + '\'' +
                ", is_requester='" + is_requester + '\'' +
                ", baseScore=" + baseScore +
                ", hqLevel=" + hqLevel +
                ", turns=" + turns +
                ", victoryPoints=" + victoryPoints +
                ", score=" + score +
                ", scCap=" + scCap +
                ", scCap_Donated=" + scCap_Donated +
                ", lastAttacked=" + lastAttacked +
                ", lastAttackedBy='" + lastAttackedBy + '\'' +
                ", lastBattleId='" + lastBattleId + '\'' +
                ", lastAttackedByName='" + lastAttackedByName + '\'' +
                ", faction='" + faction + '\'' +
                ", isErrorWithSC='" + isErrorWithSC + '\'' +
                '}';
    }
}
