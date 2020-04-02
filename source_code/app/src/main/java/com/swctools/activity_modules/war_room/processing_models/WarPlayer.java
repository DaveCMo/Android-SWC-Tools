package com.swctools.activity_modules.war_room.processing_models;

import android.content.Context;

import com.swctools.swc_server_interactions.results.SWCGuildWarPartRespData;
import com.swctools.common.models.player_models.DonatedTroops;
import com.swctools.common.models.player_models.MapBuildings;
import com.swctools.common.models.player_models.Troop;

import java.util.HashMap;

import javax.json.JsonObject;

public class WarPlayer extends WarRoomData_WarParticipant {
    public static final String TAG = WarPlayer.class.getName();
    private DonatedTroops donatedTroops;
    private WarRoomData_WarParticipant warParticipant;

    public WarPlayer(JsonObject warParticipantJson, SWCGuildWarPartRespData swcGuildWarPartRespData, String faction, String guildId, HashMap<String, Troop> getMasterTroopList, WarRoomData_WarParticipant warParticipant, Context context) {
        super(warParticipantJson, faction, context);
        this.warParticipantJson = warParticipantJson;

        this.warParticipant = warParticipant;
//
//        this.name = this.warParticipantJson.getString("name");
//        this.level = this.warParticipantJson.getInt("level");
//        this.turns = this.warParticipantJson.getInt("turns");
//        this.score = this.warParticipantJson.getInt("score");
//        this.victoryPoints = this.warParticipantJson.getInt("victoryPoints");
//
//        this.faction = faction;
//        this.mContext = context;


        if (warParticipant.getCurrentlyDefending() != null) {
            this.currentlyDefending = warParticipant.getCurrentlyDefending();
        }

        try {
            MapBuildings mapBuildings = new MapBuildings(swcGuildWarPartRespData.warBuildings());
            if (swcGuildWarPartRespData.donatedTroops() != null) {
                donatedTroops = new DonatedTroops(swcGuildWarPartRespData.donatedTroops(), faction, mapBuildings.getSquadBuilding(), guildId, getMasterTroopList, mContext);
            }
        } catch (Exception e) {
//            e.printStackTrace();
        }

    }

    public String getFaction() {
        return this.faction;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getLevel() {
        return this.level;
    }

    public int getTurns() {
        return this.turns;
    }

    public int getVictoryPoints() {
        return this.victoryPoints;
    }

    public int getScore() {
        return this.score;
    }




    public DonatedTroops getDonatedTroops() {
        return donatedTroops;
    }
}
