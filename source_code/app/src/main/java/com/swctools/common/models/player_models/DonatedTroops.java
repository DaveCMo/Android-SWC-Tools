package com.swctools.common.models.player_models;


import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.swctools.common.enums.ApplicationMessageTemplates;
import com.swctools.common.enums.BundleKeys;
import com.swctools.common.helpers.BundleHelper;
import com.swctools.swc_server_interactions.results.SWCGetPublicGuildResponseData;
import com.swctools.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.json.JsonObject;
import javax.json.JsonValue;

public class DonatedTroops implements Parcelable {
    private static final String TAG = "DonatedTroops";
    private int numInSC = 0;
    private ArrayList<TacticalCapItem> troopsList;
    private Building squadBuilding;
    private HashMap<String, String> guildPlayerMap;
    private ArrayList<String> donatedByList;
    private boolean errorCalculatiingCap = false;

    public boolean isErrorCalculatiingCap() {
        return errorCalculatiingCap;
    }

    private SWCGetPublicGuildResponseData guildResponseData;
    private String DONATEDBY = "%1$s (x%2$s)";

    public static final Creator<DonatedTroops> CREATOR = new Creator<DonatedTroops>() {
        @Override
        public DonatedTroops createFromParcel(Parcel in) {
            return new DonatedTroops(in);
        }

        @Override
        public DonatedTroops[] newArray(int size) {
            return new DonatedTroops[size];
        }
    };

    public DonatedTroops(Parcel in) {

        troopsList = in.readArrayList(TacticalCapItem.class.getClassLoader());

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(troopsList);

    }


    public DonatedTroops(JsonObject donatedTroopsJsonObj, String faction, Building squadBuilding, String guildId, HashMap<String, Troop> getMasterTroopList, Context context) {
        troopsList = new ArrayList<>();
        donatedByList = new ArrayList<>();
        guildPlayerMap = new HashMap<>();
        this.squadBuilding = squadBuilding;
        //Get latest guild data
        String guildBundleKey = String.format(ApplicationMessageTemplates.VISIT_KEY.getemplateString(), BundleKeys.GUILDPUBLIC.toString(), guildId);
        BundleHelper guildBundleData = new BundleHelper(guildBundleKey);
        //Put it in a hashmap of player id and player name so we can reference it later.
        try {
            String s = guildBundleData.get_value(context);
            JsonObject guildPublicObject = StringUtil.stringToJsonObject(guildBundleData.get_value(context));
            guildResponseData = new SWCGetPublicGuildResponseData(guildPublicObject);
            for (int i = 0; i < guildResponseData.getMembers().size(); i++) {
                GuildMember guildMember = new GuildMember(guildResponseData.getMembers().getJsonObject(i));
                guildPlayerMap.put(guildMember.playerId, StringUtil.htmlRemovedGameName(guildMember.name));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Go through list of troops and extract how many, and who donated them.
        donatedByList = new ArrayList<>();
        for (Entry<String, JsonValue> value : donatedTroopsJsonObj.entrySet()) {
            JsonObject troops = (JsonObject) value.getValue();
            donatedByList.clear();
            donatedByList.addAll(getDonatedByList(troops));
            int tmpCount = 0;
            numInSC = 0;
            for (JsonValue donatedTroop : troops.values()) {
                tmpCount = tmpCount + Integer.valueOf(donatedTroop.toString());
                numInSC = numInSC + Integer.valueOf(donatedTroop.toString());
            }
            Troop troop = null;
            try {
                troop = new Troop(getMasterTroopList.get(value.getKey()), numInSC);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (troop != null) {
                troopsList.add(new TacticalCapItem(troop.get_uiName(), troop.getTroopLevel(), troop.getTotalCap(numInSC), tmpCount, getDonatedByList(troops)));
            } else {
                troopsList.add(new TacticalCapItem(value.getKey(), 0, 0, tmpCount, getDonatedByList(troops)));
            }
        }
    }

    public ArrayList<String> getDonatedByList(JsonObject troops) {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.clear();
        if (guildPlayerMap.size() > 0) {
            for (Entry<String, JsonValue> donatedBy : troops.entrySet()) {// go through each entry in the data set which is {"playerId" ; #donated,"playerId" ; #donated} and see if we can retrieve that player id, and then the name from our guild map.
                int numDonated = Integer.valueOf(donatedBy.getValue().toString());
                String name = guildPlayerMap.get(donatedBy.getKey());
                if (!StringUtil.isStringNotNull(name)) { //if nothing found - probably because the player left the squad.
                    arrayList.add(String.format(DONATEDBY, "Unknown player", String.valueOf(numDonated)));
                } else {
                    arrayList.add(String.format(DONATEDBY, StringUtil.htmlRemovedGameName(name), String.valueOf(numDonated)));
                }
            }
        }

        return arrayList;
    }

    private String getDonatedBy(JsonObject troops) {

        String rtnStr = "";
        if (guildPlayerMap.size() > 0) { //check we have something to work with
            for (Entry<String, JsonValue> donatedBy : troops.entrySet()) {// go through each entry in the data set which is {"playerId" ; #donated,"playerId" ; #donated} and see if we can retrieve that player id, and then the name from our guild map.
                int numDonated = Integer.valueOf(donatedBy.getValue().toString());
                String name = guildPlayerMap.get(donatedBy.getKey());
                if (!StringUtil.isStringNotNull(name)) { //if nothing found - probably because the player left the squad.
                    rtnStr = String.format(DONATEDBY, "Unknown player", String.valueOf(numDonated));
                } else {
                    rtnStr = String.format(DONATEDBY, StringUtil.htmlRemovedGameName(name), String.valueOf(numDonated));
                }
            }
        }
        return rtnStr;
    }

    public int getNumInSC() {
        return numInSC;
    }

    public List<TacticalCapItem> getTroopsList() {
        return troopsList;
    }


    public int capDonated() {
        int totalCap = 0;
        for (TacticalCapItem troop : troopsList) {
            if (troop.getItemCapacity() == 0) {
//                totalCap = 0;
//                break;
                errorCalculatiingCap = true;
            }
            totalCap = totalCap + troop.getItemCapacity();
        }
        return totalCap;
    }

    public int getSquadBuildingCap() {
        Log.d(TAG, "getSquadBuildingCap: "+squadBuilding.getBuildingProperties().getLevel());
        switch (squadBuilding.getBuildingProperties().getLevel()) {
            case 1:
                return 4;
            case 2:
                return 8;
            case 3:
                return 12;
            case 4:
                return 16;
            case 5:
                return 20;
            case 6:
                return 24;
            case 7:
                return 28;
            case 8:
                return 32;
            case 9:
                return 36;
            case 10:
                return 40;
            case 11:
                return 42;
            default:
                return 0;
        }
    }


}
