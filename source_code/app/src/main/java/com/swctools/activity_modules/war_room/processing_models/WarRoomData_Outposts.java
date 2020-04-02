package com.swctools.activity_modules.war_room.processing_models;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.swctools.common.helpers.WarDataHelper;

import javax.json.JsonObject;

public class WarRoomData_Outposts implements Parcelable {
    private static final String TAG = "WarBuffBase";
    private String ownerId;
    private String buffUid;
    private String uiOPName;
    private int level;
    private int gameOpLevel;
    private JsonObject buffBaseJsonObj;

    public WarRoomData_Outposts(Parcel in) {
        ownerId = in.readString();
        buffUid = in.readString();
        uiOPName = in.readString();
        level = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(ownerId);
        parcel.writeString(buffUid);
        parcel.writeString(uiOPName);
        parcel.writeInt(level);
    }


    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public WarRoomData_Outposts createFromParcel(Parcel in) {
            return new WarRoomData_Outposts(in);
        }

        public WarRoomData_Outposts[] newArray(int size) {
            return new WarRoomData_Outposts[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public WarRoomData_Outposts(JsonObject buffBaseJsonObj, Context context) {
        this.buffBaseJsonObj = buffBaseJsonObj;
        this.buffUid = buffBaseJsonObj.getString("buffUid");
        this.level = buffBaseJsonObj.getInt("level");
        this.gameOpLevel =level;
        this.uiOPName = WarDataHelper.getOutPostFriendlyName(this.buffUid, context);
        try {
            this.ownerId = buffBaseJsonObj.getString("ownerId");
        } catch (Exception e) {
            this.ownerId = "";
        }
    }


    public String getOwnerId() {
        return ownerId;
    }

    public int getLevel() {
        return level + 5;
    }

    public String getBuffUid() {
        return buffUid;
    }

    public String getOutPostFriendlyName(Context context) {
        return uiOPName;
    }

    public JsonObject getBuffBaseJsonObj() {
        return buffBaseJsonObj;
    }

    public int getGameOpLevel() {
        return gameOpLevel;
    }

    @Override
    public String toString() {
        return "WarOutpostOwner{" +
                "ownerId='" + ownerId + '\'' +
                ", buffUid='" + buffUid + '\'' +
                ", uiOPName='" + uiOPName + '\'' +
                ", level=" + level +
                ", gameOpLevel=" + gameOpLevel +
                ", buffBaseJsonObj=" + buffBaseJsonObj +
                '}';
    }
}
