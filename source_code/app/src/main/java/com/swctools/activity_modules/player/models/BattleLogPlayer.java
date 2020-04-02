package com.swctools.activity_modules.player.models;

import android.os.Parcelable;
import android.os.Parcel;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.json.JsonObject;

public class BattleLogPlayer implements Parcelable {
    private String playerId;
    private String playerName;
    private String guildId;
    private String guildName;
    private String faction;
    private int attackRatingDelta;
    private int defenseRating;
    private int defenseRatingDelta;
    private int tournamentRating;
    private int tournamentRatingDelta;

    public static final Creator<BattleLogPlayer> CREATOR = new Creator<BattleLogPlayer>() {
        @Override
        public BattleLogPlayer createFromParcel(Parcel in) {
            return new BattleLogPlayer(in);
        }

        @Override
        public BattleLogPlayer[] newArray(int size) {
            return new BattleLogPlayer[size];
        }
    };

    public BattleLogPlayer(Parcel in) {
        playerId = in.readString();
        playerName = in.readString();
        guildId = in.readString();
        guildName = in.readString();
        faction = in.readString();
        attackRatingDelta = in.readInt();
        defenseRating = in.readInt();
        defenseRatingDelta = in.readInt();
        tournamentRating = in.readInt();
        tournamentRatingDelta = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(playerId);
        dest.writeString(playerName);
        dest.writeString(guildId);
        dest.writeString(guildName);
        dest.writeString(faction);
        dest.writeInt(attackRatingDelta);
        dest.writeInt(defenseRating);
        dest.writeInt(defenseRatingDelta);
        dest.writeInt(tournamentRating);
        dest.writeInt(tournamentRatingDelta);
    }

    public BattleLogPlayer(JsonObject player) {
        setPlayerId(player.getString("playerId"));
        setPlayerName(player.getString("name"));
        try {
            setGuildId(player.getString("guildId"));
        } catch (Exception e1) {
            setGuildId("");
        }

        try {
            setGuildName(player.getString("guildName"));
        } catch (Exception e1) {
            setGuildName("");
        }

        try {
            setAttackRatingDelta(player.getInt("attackRatingDelta"));
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            setAttackRatingDelta(0);
        }
        try {
            setDefenseRating(player.getInt("defenceRating"));
        } catch (Exception e) {
            setDefenseRating(0);
        }
        try {
            setDefenseRatingDelta(player.getInt("defenseRatingDelta"));
        } catch (Exception e) {
            setDefenseRatingDelta(0);
        }
        try {
            setTournamentRatingDelta(player.getInt("tournamentRatingDelta"));
        } catch (Exception e) {
            e.printStackTrace();
            setTournamentRatingDelta(0);
        }
        try {
            tournamentRating = player.getInt("tournamentRating");
        } catch (Exception e) {
            tournamentRating = 0;

        }
        faction = player.getString("faction");

    }

    public int getDefenseRatingDelta() {
        return defenseRatingDelta;
    }

    public void setDefenseRatingDelta(int defenseRatingDelta) {
        this.defenseRatingDelta = defenseRatingDelta;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        try {
            this.playerName = URLDecoder.decode(playerName, "UTF8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            this.playerName = playerName;
        }
    }

    public String getGuildId() {
        return guildId;
    }

    public String getFaction() {
        return faction;
    }

    public void setGuildId(String guildId) {
        this.guildId = guildId;
    }

    public String getGuildName() {
        return guildName;
    }

    public void setGuildName(String guildName) {

        try {
            this.guildName = URLDecoder.decode(guildName, "UTF8");
        } catch (UnsupportedEncodingException e) {
            this.guildName = guildName;
        }
    }

    public int getAttackRatingDelta() {
        return attackRatingDelta;
    }

    public void setAttackRatingDelta(int attackRatingDelta) {
        this.attackRatingDelta = attackRatingDelta;
    }

    public int getDefenseRating() {
        return defenseRating;
    }

    public void setDefenseRating(int defenseRating) {
        this.defenseRating = defenseRating;
    }


    public int getTournamentRatingDelta() {
        return tournamentRatingDelta;
    }


    public void setTournamentRatingDelta(int tournamentRatingDelta) {
        this.tournamentRatingDelta = tournamentRatingDelta;
    }

    public int getTournamentRating() {
        return tournamentRating;
    }
}