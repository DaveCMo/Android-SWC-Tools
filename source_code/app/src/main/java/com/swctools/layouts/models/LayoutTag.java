package com.swctools.layouts.models;

import android.os.Parcel;
import android.os.Parcelable;

public class LayoutTag implements Parcelable {
    private static final String TAG = "LayoutTag";
    public int tagId;
    public String tagString;

    public LayoutTag(int t, String s) {
        this.tagId = t;
        this.tagString = s;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(tagId);
        parcel.writeString(tagString);
    }

    protected LayoutTag(Parcel in) {
        tagId = in.readInt();
        tagString = in.readString();
    }

    public String tagIdString() {
        return String.valueOf(tagId);
    }

    public static final Creator<LayoutTag> CREATOR = new Creator<LayoutTag>() {
        @Override
        public LayoutTag createFromParcel(Parcel in) {
            return new LayoutTag(in);
        }

        @Override
        public LayoutTag[] newArray(int size) {
            return new LayoutTag[size];
        }
    };


    @Override
    public String toString() {
        return "LayoutTag{" +
                "tagId=" + tagId +
                ", tagString='" + tagString + '\'' +
                '}';
    }
}
