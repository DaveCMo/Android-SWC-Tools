package com.swctools.activity_modules.player.models;

import android.os.Parcelable;
import android.os.Parcel;
import javax.json.JsonObject;

public class BattleLogLoot implements Parcelable {
    private int contraband;
    private int credits;
    private int material;

    public static final Creator<BattleLogLoot> CREATOR = new Creator<BattleLogLoot>() {
        @Override
        public BattleLogLoot createFromParcel(Parcel in) {
            return new BattleLogLoot(in);
        }

        @Override
        public BattleLogLoot[] newArray(int size) {
            return new BattleLogLoot[size];
        }
    };

    public BattleLogLoot(Parcel in) {
        in.writeInt(contraband);
        in.writeInt(credits);
        in.writeInt(material);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(contraband);
        dest.writeInt(credits);
        dest.writeInt(material);
    }
    public BattleLogLoot(JsonObject battleLogLoot) {
        try {
            setContraband(battleLogLoot.getInt("contraband"));
        } catch (Exception e) {
            setContraband(0);
        }
        try {
            setCredits(battleLogLoot.getInt("credits"));
        } catch (Exception e) {
            setCredits(0);
        }
        try {
            setMaterial(battleLogLoot.getInt("materials"));
        } catch (Exception e) {
            setMaterial(0);
        }
    }

    public int getContraband() {
        return contraband;
    }

    public void setContraband(int contraband) {
        this.contraband = contraband;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public int getMaterial() {
        return material;
    }

    public void setMaterial(int material) {
        this.material = material;
    }

}
