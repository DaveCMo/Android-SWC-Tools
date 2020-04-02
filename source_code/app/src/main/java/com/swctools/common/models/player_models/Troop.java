package com.swctools.common.models.player_models;


import android.os.Parcel;
import android.os.Parcelable;

public class Troop implements Parcelable {
    private static final String TAG = "Troop";
    //    private final String LEVEL_FORMAT = "(Level %1$s)";
    private final String LEVEL_FORMAT = "Level %1$s";
    private final String NAME_LEVEL = "%1$s Level %2$s";
    public String gameName;
    private String faction;
    private String _uiName;
    private String level;
    private int numTroops = 1;
    private int capacity = 0;
    private int troopLevel;

    public static final Creator<Troop> CREATOR = new Creator<Troop>() {
        @Override
        public Troop createFromParcel(Parcel in) {
            return new Troop(in);
        }

        @Override
        public Troop[] newArray(int size) {
            return new Troop[size];
        }
    };

    public Troop() {
    }

    public Troop(Parcel in) {
        gameName = in.readString();
        faction = in.readString();
        _uiName = in.readString();
        level = in.readString();
        numTroops = in.readInt();
        capacity = in.readInt();
        troopLevel = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(gameName);
        dest.writeString(faction);
        dest.writeString(_uiName);
        dest.writeString(level);
        dest.writeInt(numTroops);
        dest.writeInt(capacity);
        dest.writeInt(troopLevel);

    }

    public Troop(String gn, String ui, String f, int c, int l) {
        gameName = gn;
        _uiName = ui;
        faction = f;
        capacity = c;
        troopLevel = l;
    }

    public Troop(Troop troop, int qty) {
        gameName = troop.gameName;
        _uiName = troop._uiName;
        faction = troop.faction;
        capacity = troop.capacity;
        troopLevel = troop.troopLevel;
        numTroops = qty;
    }

    public Troop(String gameName, String uiName, String faction, int capacity, int level, int numTroops) {
        this.gameName = gameName;
        this._uiName = uiName;
        this.faction = faction;
        this.capacity = capacity;
        troopLevel = level;
        this.numTroops = numTroops;
    }

    public int getTotalCap(int no) {
        if (capacity != 0) {
            return capacity * no;
        } else {
            return 0;
        }
    }

    public String formattedTroopLevelString() {
        return level;
    }

    public String descriptionAndLevel() {
        return String.format(NAME_LEVEL, this._uiName, this.troopLevel);
    }

    public String uiName() {
        return _uiName;
    }

    public int cap() {
        return capacity;
    }


    public String get_uiName() {
        return _uiName;
    }


    public int getTroopLevel() {
        return troopLevel;
    }


    public int getNumTroops() {
        return numTroops;
    }

    public void setNumTroops(int numTroops) {
        this.numTroops = numTroops;
    }


    public String getFaction() {
        return faction;
    }
}
