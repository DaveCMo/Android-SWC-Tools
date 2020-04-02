package com.swctools.swc_server_interactions.results;

import java.math.BigDecimal;

import javax.json.JsonNumber;
import javax.json.JsonObject;

public class SWCGetTournamentLeaderRanksResult extends SWCDefaultResultData {
    private static final String TAG = "TOURNAMENTDATARESULT";
    private JsonObject tournament;

    public SWCGetTournamentLeaderRanksResult(JsonObject dataObject) {
        super(dataObject);
        tournament = getResult().getJsonObject("tournament");

    }



    public SWCGetTournamentLeaderRanksResult(String dataObjString) {
        super(dataObjString);
        tournament = getResult().getJsonObject("tournament");
    }


    public JsonObject getTournament() {
        return tournament;
    }

    public int getValue(){
        return getResult().getInt("value");
    }

    public int getRank(){
        return getResult().getInt("rank");
    }

    public BigDecimal getPercentile(){
        JsonNumber percentileValue = getResult().getJsonNumber("percentile");
        BigDecimal bigDecimal = percentileValue.bigDecimalValue();
        return bigDecimal;
    }

    public String getConflictUID(){
        return tournament.getString("uid");
    }
    public int attacksWon(){
        return tournament.getInt("attacksWon");
    }

    public int attacksLost(){
        return tournament.getInt("attacksLost");
    }

    public int defensesWon(){
        return tournament.getInt("defensesWon");
    }

    public int defensesLost(){
        return tournament.getInt("defensesLost");
    }

}
