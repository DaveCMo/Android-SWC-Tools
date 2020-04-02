package com.swctools.activity_modules.war_room.processing_models;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.swctools.swc_server_interactions.results.SWCGuildWarPartRespData;
import com.swctools.common.models.player_models.Troop;
import com.swctools.util.StringUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;

public class WarRoomData_WarSquadModel implements Parcelable {
    private static final String TAG = WarRoomData_WarSquadModel.class.getName();
    private JsonObject warSquadJsonObject;
    private String guildId;
    private String name;
    private String faction;
    private JsonArray participantsJsonArr;
    private ArrayList<Object> warParticipants;
    private ArrayList<WarRoomData_WarParticipant> squadParticipants;
    private ArrayList<WarPlayer> warPlayers;
    private Context mContext;

    //Parceablestuffs
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(warSquadJsonObject.toString());
        parcel.writeString(guildId);
        parcel.writeString(name);
        parcel.writeString(faction);
        parcel.writeString(participantsJsonArr.toString());

    }

    public WarRoomData_WarSquadModel(Parcel in) {
        try {
            warSquadJsonObject = StringUtil.stringToJsonObject(in.readString());
            guildId = in.readString();
            name = in.readString();
            guildId = in.readString();
            faction = in.readString();
            participantsJsonArr = StringUtil.stringToJsonArray(in.readString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public WarRoomData_WarSquadModel createFromParcel(Parcel in) {
            return new WarRoomData_WarSquadModel(in);
        }

        public WarRoomData_WarSquadModel[] newArray(int size) {
            return new WarRoomData_WarSquadModel[size];
        }
    };

    public WarRoomData_WarSquadModel(JsonObject jsonObject, String playerId, SWCGuildWarPartRespData swcGuildWarPartRespData, HashMap<String, Troop> stringTroopHashMap, Context context) {
        ArrayList<WarRoomData_WarParticipant> tmpwarParticipants = new ArrayList<>();

        this.warParticipants = new ArrayList<>();
        this.warPlayers = new ArrayList<>();
        this.squadParticipants = new ArrayList<>();

        this.mContext = context;
        this.warSquadJsonObject = jsonObject;
        this.guildId = this.warSquadJsonObject.getString("guildId");
        this.name = this.warSquadJsonObject.getString("name");
        this.faction = this.warSquadJsonObject.getString("faction");
        this.participantsJsonArr = this.warSquadJsonObject.getJsonArray("participants");

        for (JsonValue jsonValue : this.participantsJsonArr) {
            JsonObject warParticipantObj = (JsonObject) jsonValue;
            tmpwarParticipants.add(new WarRoomData_WarParticipant(warParticipantObj, this.faction, this.mContext));
        }

        for (WarRoomData_WarParticipant warParticipant : tmpwarParticipants) {
            if (warParticipant.getId().equalsIgnoreCase(playerId)) {
                warPlayers.add(new WarPlayer(warParticipant.getWarParticipantJson(), swcGuildWarPartRespData, faction, guildId, stringTroopHashMap, warParticipant, mContext));
            }
        }

        for (WarRoomData_WarParticipant warParticipant : tmpwarParticipants) {
            if (!warParticipant.getId().equalsIgnoreCase(playerId)) {
//                this.warParticipants.add(warParticipant);
                this.squadParticipants.add(warParticipant);
            }
        }

    }


    public WarRoomData_WarSquadModel(JsonObject jsonObject, Context context) {
        ArrayList<WarRoomData_WarParticipant> tmpwarParticipants = new ArrayList<>();

        this.mContext = context;
        this.warSquadJsonObject = jsonObject;
        this.guildId = this.warSquadJsonObject.getString("guildId");
        this.name = this.warSquadJsonObject.getString("name");
        this.faction = this.warSquadJsonObject.getString("faction");
        this.participantsJsonArr = this.warSquadJsonObject.getJsonArray("participants");
        this.warParticipants = new ArrayList<>();
        for (JsonValue jsonValue : this.participantsJsonArr) {
            JsonObject warParticipantObj = (JsonObject) jsonValue;
            tmpwarParticipants.add(new WarRoomData_WarParticipant(warParticipantObj, this.faction, this.mContext));
        }
        warParticipants.addAll(tmpwarParticipants);

    }


    //GETTERs

    public ArrayList<Object> getWarParticipants() {
        return warParticipants;
    }


    public String getGuildId() {
        return guildId;
    }

    public String getName() {

        try {
            return URLDecoder.decode(name, "UTF8");
        } catch (UnsupportedEncodingException e) {
            return name;
        }

    }

    public String getFaction() {
        return faction;
    }

    public JsonArray getParticipantsJsonArr() {
        return participantsJsonArr;
    }

    public JsonObject getWarSquadJsonObject() {
        return warSquadJsonObject;
    }

    public ArrayList<WarRoomData_WarParticipant> getSquadParticipants() {
        return squadParticipants;
    }

    public ArrayList<WarPlayer> getWarPlayers() {
        return warPlayers;
    }

    public void setWarParticipants(ArrayList<Object> warParticipants) {
        this.warParticipants = warParticipants;
    }

    public void setSquadParticipants(ArrayList<WarRoomData_WarParticipant> squadParticipants) {
        this.squadParticipants = squadParticipants;
    }

    public void setWarPlayers(ArrayList<WarPlayer> warPlayers) {
        this.warPlayers = warPlayers;
    }
}
