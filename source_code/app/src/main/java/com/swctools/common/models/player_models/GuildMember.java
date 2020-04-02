package com.swctools.common.models.player_models;

import javax.json.JsonObject;

public class GuildMember {
    public String name;
    public boolean isOwner;
    public boolean isOfficer;
    public long joinDate;
    public int troopsDonated;
    public int troopsReceived;
    public int rank;
    public int hqLevel;
    public int reputationInvested;
    public int xp;
    public int score;
    public int warParty;
    public int tournamentRating;
    public JsonObject tournamentScores;
    public int attacksWon;
    public int defensesWon;
    public String planet;
    public long lastLoginTime;
    public long lastUpdated;
    public boolean hasPlanetaryCommand;
    public String playerId;

    public GuildMember(JsonObject memberObj){
        name = memberObj.getString("name");
        isOwner = memberObj.getBoolean("isOwner");
        isOfficer = memberObj.getBoolean("isOfficer");
        joinDate = memberObj.getInt("joinDate");
        troopsDonated = memberObj.getInt("troopsDonated");
        troopsReceived = memberObj.getInt("troopsReceived");
        rank = memberObj.getInt("rank");
        hqLevel = memberObj.getInt("hqLevel");
        reputationInvested = memberObj.getInt("reputationInvested");
        xp = memberObj.getInt("xp");
        score = memberObj.getInt("score");
        warParty = memberObj.getInt("warParty");
//        tournamentRating = memberObj.getInt("tournamentRating");
//        tournamentScores = memberObj.getJsonObject("tournamentScores");
        attacksWon = memberObj.getInt("attacksWon");
        defensesWon = memberObj.getInt("defensesWon");
        planet = memberObj.getString("planet");
        lastLoginTime = memberObj.getInt("lastLoginTime");
        lastUpdated = memberObj.getInt("lastUpdated");
        hasPlanetaryCommand = memberObj.getBoolean("hasPlanetaryCommand");
        playerId = memberObj.getString("playerId");
    }


}
