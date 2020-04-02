package com.swctools.common.models.player_models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class TacticalCapItem implements Parcelable {
    private int itemLevel;
    private String itemName;
    private int itemCapacity;
    private int itemQty;
//    private ArrayList<TacticalCapItemDetail> itemDetail;
    private ArrayList<String> itemDetail;

    public static final Creator<TacticalCapItem> CREATOR = new Creator<TacticalCapItem>() {
        @Override
        public TacticalCapItem createFromParcel(Parcel in) {
            return new TacticalCapItem(in);
        }

        @Override
        public TacticalCapItem[] newArray(int size) {
            return new TacticalCapItem[size];
        }
    };

    public TacticalCapItem(Parcel in) {
        itemLevel = in.readInt();
        itemName = in.readString();
        itemCapacity = in.readInt();
        itemQty = in.readInt();
        itemDetail = in.readArrayList(String.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(itemLevel);
        dest.writeString(itemName);
        dest.writeInt(itemCapacity);
        dest.writeInt(itemQty);
        dest.writeList(itemDetail);
    }

    public TacticalCapItem(String n, int l, int c, int q, ArrayList<String> itemDetail) {
        this.itemLevel = l;
        this.itemName = n;
        this.itemCapacity = c;
        this.itemQty = q;
        this.itemDetail = itemDetail;
    }

    public int getItemLevel() {
        return itemLevel;
    }

    public String getItemName() {
        return itemName;
    }

    public int getItemCapacity() {
        return itemCapacity;
    }

    public int getItemQty() {
        return itemQty;
    }

    public ArrayList<String> getItemDetail() {
        return itemDetail;
    }


    @Override
    public String toString() {
        return "TacticalCapItem{" +
                "itemLevel=" + itemLevel +
                ", itemName='" + itemName + '\'' +
                ", itemCapacity=" + itemCapacity +
                ", itemQty=" + itemQty +
                ", itemDetail=" + itemDetail +
                '}';
    }
}
