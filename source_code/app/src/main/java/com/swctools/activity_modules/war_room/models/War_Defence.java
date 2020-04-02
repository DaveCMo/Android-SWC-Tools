package com.swctools.activity_modules.war_room.models;

public class War_Defence {
    private String warId;
    private String playerId;
    private String playerName;
    private String opponentId;
    private String opponentName;
    private String attackerWarBuffs;
    private String defenderWarBuffs;

    public War_Defence(String warId, String playerId, String playerName, String opponentId, String opponentName, String attackerWarBuffs, String defenderWarBuffs) {
        this.warId = warId;
        this.playerId = playerId;
        this.playerName = playerName;
        this.opponentId = opponentId;
        this.opponentName = opponentName;
        this.attackerWarBuffs = attackerWarBuffs;
        this.defenderWarBuffs = defenderWarBuffs;
    }


    public String getWarId() {
        return warId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getOpponentId() {
        return opponentId;
    }

    public String getOpponentName() {
        return opponentName;
    }

    public String getAttackerWarBuffs() {
        return attackerWarBuffs;
    }

    public String getDefenderWarBuffs() {
        return defenderWarBuffs;
    }

    @Override
    public String toString() {
        return "War_Defence{" +
                "warId='" + warId + '\'' +
                ", playerId='" + playerId + '\'' +
                ", playerName='" + playerName + '\'' +
                ", opponentId='" + opponentId + '\'' +
                ", opponentName='" + opponentName + '\'' +
                ", attackerWarBuffs='" + attackerWarBuffs + '\'' +
                ", defenderWarBuffs='" + defenderWarBuffs + '\'' +
                '}';
    }
}
