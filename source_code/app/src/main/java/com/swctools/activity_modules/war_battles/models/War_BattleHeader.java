package com.swctools.activity_modules.war_battles.models;

public class War_BattleHeader {
    private String battleId;
    private String attackedBy;
    private int starsLeft;
    private int dateTimeOfAttack;
    private String withOps;
    private String againstOps;

    public War_BattleHeader(String battleId, String attackedBy, int starsLeft, int dateTimeOfAttack, String withOps, String againstOps) {
        this.battleId = battleId;
        this.attackedBy = attackedBy;
        this.starsLeft = starsLeft;
        this.dateTimeOfAttack = dateTimeOfAttack;
        this.withOps = withOps;
        this.againstOps = againstOps;
    }

    public String getAttackedBy() {
        return attackedBy;
    }

    public int getStarsLeft() {
        return starsLeft;
    }

    public int getDateTimeOfAttack() {
        return dateTimeOfAttack;
    }

    public String getWithOps() {
        return withOps;
    }

    public String getAgainstOps() {
        return againstOps;
    }

    @Override
    public String toString() {
        return "War_BattleHeader{" +
                "attackedBy='" + attackedBy + '\'' +
                ", starsLeft=" + starsLeft +
                ", dateTimeOfAttack=" + dateTimeOfAttack +
                ", withOps='" + withOps + '\'' +
                ", againstOps='" + againstOps + '\'' +
                '}';
    }

    public String getBattleId() {
        return battleId;
    }
}
