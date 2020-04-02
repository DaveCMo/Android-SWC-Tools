package com.swctools.common.models.player_models;

import android.os.Parcel;
import android.os.Parcelable;

public class TacticalCapItemDetail implements Parcelable {
    public String getS() {
        return s;
    }

    public int getI() {
        return i;
    }

    private String s;
    private int i;


    public TacticalCapItemDetail(String s, int i) {
        this.s = s;
        this.i = i;
    }


    public static final Creator<TacticalCapItemDetail> CREATOR = new Creator<TacticalCapItemDetail>() {
        @Override
        public TacticalCapItemDetail createFromParcel(Parcel in) {
            return new TacticalCapItemDetail(in);
        }

        @Override
        public TacticalCapItemDetail[] newArray(int size) {
            return new TacticalCapItemDetail[size];
        }
    };

    public TacticalCapItemDetail(Parcel in) {
        s = in.readString();
        i = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(s);
        parcel.writeInt(i);
    }
}
