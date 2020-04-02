package com.swctools.activity_modules.war_room.processing_models;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.swctools.swc_server_interactions.results.SWCGetPublicGuildResponseData;
import com.swctools.util.StringUtil;
import com.swctools.activity_modules.war_sign_up.models.GuildMember;

import javax.json.JsonObject;
import javax.json.JsonValue;

public class WarRoomData_WarParticipant implements Parcelable {
    public static final String TAG = WarRoomData_WarParticipant.class.getName();
    protected JsonObject warParticipantJson;
    protected String name;
    protected String faction;
    protected String id;
    protected int level;
    protected int turns;
    protected int victoryPoints;
    protected int score;
    protected int hqLevel;
    protected int baseScore;
    protected WarRoomData_CurrentlyDefending currentlyDefending;


    protected Context mContext;

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public WarRoomData_WarParticipant createFromParcel(Parcel in) {
            return new WarRoomData_WarParticipant(in);
        }

        public WarRoomData_WarParticipant[] newArray(int size) {
            return new WarRoomData_WarParticipant[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(warParticipantJson.toString());
        parcel.writeString(name);
        parcel.writeString(faction);
        parcel.writeString(id);
        parcel.writeInt(level);
        parcel.writeInt(turns);
        parcel.writeInt(victoryPoints);
        parcel.writeInt(score);
        parcel.writeInt(hqLevel);
        parcel.writeInt(baseScore);
        parcel.writeParcelable(currentlyDefending, Parcelable.PARCELABLE_WRITE_RETURN_VALUE);

    }

    public WarRoomData_WarParticipant(Parcel in) {
        try {
            warParticipantJson = StringUtil.stringToJsonObject(in.readString());
            name = in.readString();
            faction = in.readString();
            id = in.readString();
            level = in.readInt();
            turns = in.readInt();
            victoryPoints = in.readInt();
            score = in.readInt();
            hqLevel = in.readInt();
            baseScore = in.readInt();
            currentlyDefending = in.readParcelable(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public WarRoomData_CurrentlyDefending getCurrentlyDefending() {
        return currentlyDefending;
    }


    public JsonObject getWarParticipantJson() {
        return warParticipantJson;
    }

    public WarRoomData_WarParticipant() {

    }

    public void setHQandBS(SWCGetPublicGuildResponseData swcGetPublicGuildResponseData) {
        for (int i = 0; i < swcGetPublicGuildResponseData.getMembers().size(); i++) {
            GuildMember guildMember = new GuildMember(swcGetPublicGuildResponseData.getMembers().getJsonObject(i));
            if (guildMember.playerId.equalsIgnoreCase(id)) {
                baseScore = guildMember.xp;
                hqLevel = guildMember.hqLevel;
                break;
            }
        }
    }


    public int getHqLevel() {
        return hqLevel;
    }

    public int getBaseScore() {
        return baseScore;
    }

    public String getFaction() {
        return faction;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
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


    public WarRoomData_WarParticipant(JsonObject warParticipantJson, String faction, Context context) {

        this.warParticipantJson = warParticipantJson;
        this.name = this.warParticipantJson.getString("name");
        this.level = this.warParticipantJson.getInt("level");
        this.turns = this.warParticipantJson.getInt("turns");
        this.score = this.warParticipantJson.getInt("score");
        this.victoryPoints = this.warParticipantJson.getInt("victoryPoints");
        this.id = this.warParticipantJson.getString("id");
        this.faction = faction;
        this.mContext = context;
        JsonValue currentlyDefVal = this.warParticipantJson.get("currentlyDefending");
        if (!currentlyDefVal.equals(JsonValue.NULL)) {
            this.currentlyDefending = new WarRoomData_CurrentlyDefending(this.warParticipantJson.getJsonObject("currentlyDefending"), faction, mContext);
        }
    }

    @Override
    public String toString() {
        return "WarParticipant{" +
                "warParticipantJson=" + warParticipantJson +
                ", name='" + name + '\'' +
                ", faction='" + faction + '\'' +
                ", id='" + id + '\'' +
                ", level=" + level +
                ", turns=" + turns +
                ", victoryPoints=" + victoryPoints +
                ", score=" + score +
                ", hqLevel=" + hqLevel +
                ", baseScore=" + baseScore +
                ", currentlyDefending=" + currentlyDefending +
                ", mContext=" + mContext +
                '}';
    }
}
