package com.swctools.common.models.player_models;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.swctools.R;
import com.swctools.activity_modules.player.models.ResourceDataItem;
import com.swctools.activity_modules.player.models.BuildingDAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PlayerTraps implements Parcelable {
    private static final String TAG = "PlayerTraps";
    private ArrayList<PlayerTrap> playerTraps;
    private HashMap<String, ResourceDataItem> playerTrapMap;
    private ArrayList<ResourceDataItem> trapResourceDataItems;
    private int COLOR_GREEN;
    private HashMap<String, BuildingDAO> getMasterBuildingList;

    public static final Creator<PlayerTraps> CREATOR = new Creator<PlayerTraps>() {
        @Override
        public PlayerTraps createFromParcel(Parcel in) {
            return new PlayerTraps(in);
        }

        @Override
        public PlayerTraps[] newArray(int size) {
            return new PlayerTraps[size];
        }
    };

    public PlayerTraps(Parcel in) {
        playerTraps = in.readArrayList(PlayerTrap.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(playerTraps);

    }


    public PlayerTraps(MapBuildings mapBuildings, String faction, HashMap<String, BuildingDAO> getMasterBuildingList, Context context) {
        COLOR_GREEN = context.getResources().getColor(R.color.green);
        this.getMasterBuildingList = getMasterBuildingList;
        playerTrapMap = new HashMap<>();
        playerTraps = new ArrayList<>();
        trapResourceDataItems = new ArrayList<>();
        for (Building building : mapBuildings.getBuildings()) {
            ResourceDataItem trapDataItem;
            if (building.getBuildingProperties().isTrap()) {
                PlayerTrap playerTrap = null;
                BuildingDAO buildingDAO = getMasterBuildingList.get(building.getUid());
                try {
//                    playerTrap = new PlayerTrap(building, faction, context);
                    playerTrap = new PlayerTrap(buildingDAO.ui_name, buildingDAO.level, building);
                } catch (Exception e) {
//                    playerTrap = new PlayerTrap(building.)
                    e.printStackTrace();
                }
                trapDataItem = playerTrapMap.get(playerTrap.trapDescription());

                if (trapDataItem == null) {
                    trapDataItem = new ResourceDataItem(playerTrap.trapDescription(), 1, 0, COLOR_GREEN);
                } else {
                    trapDataItem.capacity++;
                }
                if (playerTrap.isArmed()) {
                    trapDataItem.amount++;
                }
                playerTrapMap.put(playerTrap.trapDescription(), trapDataItem);
                playerTraps.add(playerTrap);
            }
        }

        //now add it to the array:
        for (Map.Entry<String, ResourceDataItem> itemEntry : playerTrapMap.entrySet()) {
            trapResourceDataItems.add(itemEntry.getValue());
        }
    }

    public ArrayList<ResourceDataItem> getTrapResourceDataItems() {
        return trapResourceDataItems;
    }

    public ArrayList<PlayerTrap> getPlayerTraps() {
        return playerTraps;
    }

}
