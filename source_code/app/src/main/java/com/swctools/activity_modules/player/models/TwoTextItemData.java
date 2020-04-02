package com.swctools.activity_modules.player.models;

import android.os.Parcel;
import android.os.Parcelable;

public class TwoTextItemData implements Parcelable {
    public String text1;
    public String text2;

    public TwoTextItemData(String s1, String s2) {
        this.text1 = s1;
        this.text2 = s2;
    }

    public TwoTextItemData(){

    }
    public static final Parcelable.Creator<TwoTextItemData> CREATOR = new Parcelable.Creator<TwoTextItemData>() {
        @Override
        public TwoTextItemData createFromParcel(Parcel in) {
            return new TwoTextItemData(in);
        }

        @Override
        public TwoTextItemData[] newArray(int size) {
            return new TwoTextItemData[size];
        }
    };

    public TwoTextItemData(Parcel in) {
        text1 = in.readString();
        text2 = in.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text1);
        dest.writeString(text2);

    }
}
