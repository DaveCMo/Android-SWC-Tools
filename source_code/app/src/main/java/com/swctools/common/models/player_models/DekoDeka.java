package com.swctools.common.models.player_models;

import android.os.Parcel;
import android.os.Parcelable;

import com.swctools.common.enums.Statuses;
import com.swctools.common.helpers.DateTimeHelper;
import com.swctools.swc_server_interactions.results.JSONplayerModel;
import com.swctools.util.StringUtil;
import com.swctools.util.Utils;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;


public class DekoDeka implements Parcelable {
    private static final String TAG = "DekoDeka";
    public int amount = 0;
    public int capacity = 0;
    public boolean built = false;
    public int level = 0;
    public String state;
    public long readyTime = 0;
    public String droidekasType;
    public String deployable;
    public String padBuilding;


    public static final Creator<DekoDeka> CREATOR = new Creator<DekoDeka>() {
        @Override
        public DekoDeka createFromParcel(Parcel in) {
            return new DekoDeka(in);
        }

        @Override
        public DekoDeka[] newArray(int size) {
            return new DekoDeka[size];
        }
    };

    public DekoDeka(Parcel in) {
        amount = in.readInt();
        capacity = in.readInt();
        built = Boolean.parseBoolean(in.readString());
        level = in.readInt();
        state = in.readString();
        readyTime = in.readLong();
        droidekasType = in.readString();
        deployable = in.readString();
        padBuilding = in.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(amount);
        dest.writeInt(capacity);
        dest.writeString(String.valueOf(built));
        dest.writeInt(level);
        dest.writeString(state);
        dest.writeLong(readyTime);
        dest.writeString(droidekasType);
        dest.writeString(deployable);
        dest.writeString(padBuilding);

    }


    public DekoDeka() {


    }

    public void setState(String uidDekFormat, String uidPlatformFormat, String faction, JSONplayerModel playerModel) {
        String uidDek = String.format(uidDekFormat, faction, level);
        String uidPlatform = String.format(uidPlatformFormat, faction, level);
        Utils.printLogDConcatStrings(uidDekFormat, uidPlatformFormat, faction);
        int upgradingLevel = level++;
        if (amount == 1) {
            state = Statuses.DROIDEKA_READY.toString();
        } else {
            state = Statuses.DROIDEKA_DOWN.toString();
            MapBuildings mapBuildings = new MapBuildings(playerModel.map());
            String buildingId = "";
            for (Building building : mapBuildings.getBuildings()) {
                if (uidPlatform.equalsIgnoreCase(building.getUid())) {
                    buildingId = building.getKey();
                    padBuilding = building.getKey();
                }
            }
            if (StringUtil.isStringNotNull(buildingId)) {
                JsonArray contracts = playerModel.contracts();
                for (JsonValue jsonValue : contracts) { //Fetch contracts (stuff under construction and iterate through to see if the droid is in there
                    JsonObject contractItem = (JsonObject) jsonValue;
                    if (contractItem.getString("buildingId").equalsIgnoreCase(buildingId)) {
                        readyTime = contractItem.getInt("endTime");
                        long nowL = DateTimeHelper.userIDTimeStamp() / 1000;
                        if (readyTime > nowL) {
                            String contractType = contractItem.getString("contractType");
                            if (contractType.equalsIgnoreCase("Upgrade")) {
                                state = Statuses.DROIDEKA_UPGRADING.toString();
                            } else if (contractType.equalsIgnoreCase("Champion")) {
                                state = Statuses.DROIDEKA_REPAIRING.toString();
                            }
                        } else {
                            state = Statuses.DROIDEKA_READY.toString();
                        }
                        return;
                    }
                }
            }
        }

        deployable = uidDek;
    }

    @Override
    public String toString() {
        return "DekoDeka{" +
                "amount=" + amount +
                ", capacity=" + capacity +
                ", built=" + built +
                ", level=" + level +
                ", state='" + state + '\'' +
                ", readyTime=" + readyTime +
                ", droidekasType='" + droidekasType + '\'' +
                '}';
    }
}

