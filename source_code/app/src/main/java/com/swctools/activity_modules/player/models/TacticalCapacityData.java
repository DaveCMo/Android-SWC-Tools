package com.swctools.activity_modules.player.models;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.swctools.common.models.player_models.TacticalCapItem;
import com.swctools.config.AppConfig;

import java.util.List;

public class TacticalCapacityData implements Parcelable {
    /*Generic class used to hold Squad Center, Armoury and Deka info*/
    private static final String TAG = "TacticalCapacityData";
    public static final int SC = 0;
    public static final int ARMOURY = 1;
    public static final int TROOPTRANSPORT = 2;
    public static final int HERO = 3;
    public static final int STARSHIP = 4;

    private String title;
    private int titleImage;
    private int maxCap;
    private int curCap;
    private String listTitle;
    private List<TacticalCapItem> capacityList;
    private int numItems;
    private boolean showQty;
    private int TYPE;
    public String expandedSettingString;
    public boolean locked;

    public static final Creator<TacticalCapacityData> CREATOR = new Creator<TacticalCapacityData>() {
        @Override
        public TacticalCapacityData createFromParcel(Parcel in) {
            return new TacticalCapacityData(in);
        }

        @Override
        public TacticalCapacityData[] newArray(int size) {
            return new TacticalCapacityData[size];
        }
    };

    public TacticalCapacityData(Parcel in) {
        title = in.readString();
        titleImage = in.readInt();
        maxCap = in.readInt();
        curCap = in.readInt();
        listTitle = in.readString();
        capacityList = in.readArrayList(TacticalCapItem.class.getClassLoader());
        numItems = in.readInt();
        showQty = Boolean.parseBoolean(in.readString());
        TYPE = in.readInt();
        expandedSettingString = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeInt(titleImage);
        dest.writeInt(maxCap);
        dest.writeInt(curCap);
        dest.writeString(listTitle);
        dest.writeList(capacityList);
        dest.writeInt(numItems);
        dest.writeString(String.valueOf(showQty));
        dest.writeInt(TYPE);
        dest.writeString(expandedSettingString);


    }


    public TacticalCapacityData(String title, int titleImage, int maxCap, int curCap, List<TacticalCapItem> capacityList, String listTitle, int numItems, boolean showQty, int typeOptions, Context context) {
        this.title = title;
        this.titleImage = titleImage;
        this.maxCap = maxCap;
        this.curCap = curCap;
        this.capacityList = capacityList;
        this.listTitle = listTitle;
        this.numItems = numItems;
        this.showQty = showQty;
        this.TYPE = typeOptions;
        if (this.TYPE == SC) {
            expandedSettingString = AppConfig.Settings.SCDETAILEXPAND.toString();
        } else if (this.TYPE == ARMOURY) {
            expandedSettingString = AppConfig.Settings.ARMOURYDETAILEXPAND.toString();
        } else if (this.TYPE == TROOPTRANSPORT) {
            expandedSettingString = AppConfig.Settings.TROOPTRANSPORTEXPAND.toString();
        } else if (this.TYPE == HERO) {
            expandedSettingString = AppConfig.Settings.TROOPHEROEXPAND.toString();
        } else if (this.TYPE == STARSHIP) {
            expandedSettingString = AppConfig.Settings.TROOPAIREXPAND.toString();
        }
        AppConfig appConfig = new AppConfig(context);
        this.locked = appConfig.getPlayerDetailExpanded(expandedSettingString);
    }

    public String getTitle() {
        return title;
    }

    public int getTitleImage() {
        return titleImage;
    }

    public int getMaxCap() {
        return maxCap;
    }

    public int getCurCap() {
        return curCap;
    }

    public List<TacticalCapItem> getCapacityList() {
        return capacityList;
    }

    public String getListTitle() {
        return listTitle;
    }

    public int getNumItems() {
        return numItems;
    }

    public boolean isShowQty() {
        return showQty;
    }

    public int getTYPE() {
        return TYPE;
    }

}
