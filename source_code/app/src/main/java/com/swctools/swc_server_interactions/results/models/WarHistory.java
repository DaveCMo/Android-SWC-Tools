package com.swctools.swc_server_interactions.results.models;

import javax.json.JsonObject;

public class WarHistory {

    private String warId;
    private int endDate;
    private int score;
    private int opponentScore;
    private String opponentGuildId;
    private String opponentName;
    private String opponentIcon;


    public WarHistory(String warId, int endDate, int score, int opponentScore, String opponentGuildId, String opponentName, String opponentIcon) {
        this.warId = warId;
        this.endDate = endDate;
        this.score = score;
        this.opponentScore = opponentScore;
        this.opponentGuildId = opponentGuildId;
        this.opponentName = opponentName;
        this.opponentIcon = opponentIcon;
    }

    public WarHistory(JsonObject jsonObject) {
        this.warId = jsonObject.getString("warId");
        this.endDate = jsonObject.getInt("endDate");
        this.score = jsonObject.getInt("score");
        this.opponentScore = jsonObject.getInt("opponentScore");
        this.opponentGuildId = jsonObject.getString("opponentGuildId");
        this.opponentName = jsonObject.getString("opponentName");
        this.opponentIcon = jsonObject.getString("opponentIcon");

    }

    public String getWarId() {
        return warId;
    }

    public int getEndDate() {
        return endDate;
    }

    public int getScore() {
        return score;
    }

    public int getOpponentScore() {
        return opponentScore;
    }

    public String getOpponentGuildId() {
        return opponentGuildId;
    }

    public String getOpponentName() {
        return opponentName;
    }

    public String getOpponentIcon() {
        return opponentIcon;
    }

    @Override
    public String toString() {
        return "WarHistory{" +
                "warId='" + warId + '\'' +
                ", endDate=" + endDate +
                ", score=" + score +
                ", opponentScore=" + opponentScore +
                ", opponentGuildId='" + opponentGuildId + '\'' +
                ", opponentName='" + opponentName + '\'' +
                ", opponentIcon='" + opponentIcon + '\'' +
                '}';
    }
}
