package com.swctools.activity_modules.player.models;

import android.os.Parcel;
import android.os.Parcelable;

public class ResourceDataItem implements Parcelable {
    public String title;
    public int capacity;
    public int amount;
    public int barColour;
    public static final Creator<ResourceDataItem> CREATOR = new Creator<ResourceDataItem>() {
        @Override
        public ResourceDataItem createFromParcel(Parcel in) {
            return new ResourceDataItem(in);
        }

        @Override
        public ResourceDataItem[] newArray(int size) {
            return new ResourceDataItem[size];
        }
    };

    public ResourceDataItem(Parcel in) {
        title = in.readString();
        capacity = in.readInt();
        amount = in.readInt();
        barColour = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeInt(capacity);
        dest.writeInt(amount);
        dest.writeInt(barColour);

    }

    public ResourceDataItem(String title, int cap, int amt, int colourID) {
        this.title = title;
        this.capacity = cap;
        this.amount = amt;
        this.barColour = colourID;
    }
}
