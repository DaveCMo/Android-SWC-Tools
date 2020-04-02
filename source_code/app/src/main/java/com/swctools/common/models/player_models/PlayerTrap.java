package com.swctools.common.models.player_models;

import android.os.Parcel;
import android.os.Parcelable;

public class PlayerTrap implements Parcelable {
    private static final String TAG = "PlayerTrap";
    private String trapType;
    private int trapLevel = 0;
    private boolean armed;
    private String TRAP_DESCRIPTION = "%1$s Level %2$s";
    private String TRAP_LEVEL = "Level %1$s";


    public static final Creator<PlayerTrap> CREATOR = new Creator<PlayerTrap>() {
        @Override
        public PlayerTrap createFromParcel(Parcel in) {
            return new PlayerTrap(in);
        }

        @Override
        public PlayerTrap[] newArray(int size) {
            return new PlayerTrap[size];
        }
    };

    public PlayerTrap(Parcel in) {
        trapType = in.readString();
        trapLevel = in.readInt();
        armed = Boolean.parseBoolean(in.readString());

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(trapType);
        dest.writeInt(trapLevel);
        dest.writeString(String.valueOf(armed));

    }

    public PlayerTrap(String uiName, int level, Building building) {
        this.trapType = uiName;
        this.trapLevel = level;
        if (building.getCurrentStorage() == 1) {
            armed = true;
        } else {
            armed = false;
        }
    }
//    public PlayerTrap(Building building, String faction, Context context) {
//
//        GameUnitEquipNameHelper.GameUnitResult gr = GameUnitEquipNameHelper.gameUnitResult(building.getUid(), faction, DatabaseContracts.Buildings.TABLE_NAME, context);
//        this.trapType = gr.uiName;
//        this.trapLevel = building.getBuildingProperties().getLevel();
//        if (building.getCurrentStorage() == 1) {
//            armed = true;
//        } else {
//            armed = false;
//        }
//
//
//    }


    public String trapDescription() {
//        return String.format(TRAP_DESCRIPTION, this.trapType, String.valueOf(this.trapLevel));
        return this.trapType;
    }

    public String trapLevelString() {
        return String.format(TRAP_LEVEL, String.valueOf(this.trapLevel));
    }


    public String armed() {
        if (armed) {
            return "Armed";
        } else {
            return "Unarmed";
        }
    }

    public boolean isArmed() {
        return armed;
    }
}
