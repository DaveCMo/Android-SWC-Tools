package com.swctools.activity_modules.player.models;

import android.os.Parcel;
import android.os.Parcelable;

public class StatItemData implements Parcelable {
    public String text1;
    public String text2;
    public int statColour;

    public StatItemData(String s1, String s2, int statColour) {
        this.text1 = s1;
        this.text2 = s2;
        this.statColour = statColour;
    }

    public StatItemData(){

    }
    public static final Parcelable.Creator<StatItemData> CREATOR = new Parcelable.Creator<StatItemData>() {
        @Override
        public StatItemData createFromParcel(Parcel in) {
            return new StatItemData(in);
        }

        @Override
        public StatItemData[] newArray(int size) {
            return new StatItemData[size];
        }
    };

    public StatItemData(Parcel in) {
        text1 = in.readString();
        text2 = in.readString();
        statColour = in.readInt();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text1);
        dest.writeString(text2);
        dest.writeInt(statColour);

    }
}