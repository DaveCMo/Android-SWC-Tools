package com.swctools.common.models.player_models;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;

public class ActiveArmoury implements Parcelable {
    private static final String TAG = "ActiveArmoury";
    private int _capacity = 0;
    private int _activatedCapacity;
    private int _noActivated = 0;
    private final String EQUIPMENT_FORMAT = "%1$s %2$s";//"Attacked by %1$s (%2$s) on %3$s";
    private final String EQUIPMENT_FORMAT_DEFENCE = "%1$s (Level %2$s)";//"Attacked by %1$s (%2$s) on %3$s";
    private ArrayList<TacticalCapItem> activatedEquipment;
    private HashMap<String, ArmouryEquipment> getMasterArmourList;


    public static final Creator<ActiveArmoury> CREATOR = new Creator<ActiveArmoury>() {
        @Override
        public ActiveArmoury createFromParcel(Parcel in) {
            return new ActiveArmoury(in);
        }

        @Override
        public ActiveArmoury[] newArray(int size) {
            return new ActiveArmoury[size];
        }
    };

    public ActiveArmoury(Parcel in) {


    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {


    }

    public ActiveArmoury(JsonObject activeArmouryJsonObj, String faction, HashMap<String, ArmouryEquipment> getMasterArmourList, Context context) {
        this.getMasterArmourList = getMasterArmourList;
        JsonValue capNullCheck = activeArmouryJsonObj.get("capacity");
        if (!capNullCheck.toString().equalsIgnoreCase("null")) {
            _capacity = activeArmouryJsonObj.getInt("capacity");
            JsonArray equipment = activeArmouryJsonObj.getJsonArray("equipment");
            buildEquipmentList(faction, context, equipment);
        } else {
            _capacity = 0;
            _activatedCapacity = 0;
        }
    }

    public ActiveArmoury(JsonArray attackerEquipment, String faction, HashMap<String, ArmouryEquipment> getMasterArmourList, Context context) {
        this.getMasterArmourList = getMasterArmourList;
        try {
            buildEquipmentList(faction, context, attackerEquipment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buildEquipmentList(String faction, Context context, JsonArray equipment) {
//        activeEquipment = new ArrayList<>();
        activatedEquipment = new ArrayList<>();
        for (int i = 0; i < equipment.size(); i++) {
            String equipString = equipment.getString(i);
            ArmouryEquipment armouryEquipment = getMasterArmourList.get(equipString);// new ArmouryEquipment(equipString, faction, context);
//            activeEquipment.add(armouryEquipment);
            if (armouryEquipment != null) {
                activatedEquipment.add(new TacticalCapItem(armouryEquipment.uiName(), armouryEquipment.getEquipLevel(), armouryEquipment.cap(), 0, null));

            } else {
                activatedEquipment.add(new TacticalCapItem(equipString, 0, 0, 0, null));
            }

            _noActivated++;
        }
    }


    public int capacity() {
        return _capacity;
    }

    public int noActivated() {
        return _noActivated;
    }

    public int get_activatedCapacity() {
        _activatedCapacity = 0;
        try {
            for (TacticalCapItem equipment : activatedEquipment) {
                if (equipment.getItemCapacity() == 0) {
                    _activatedCapacity = 0;
                    break;
                }
                _activatedCapacity = _activatedCapacity + equipment.getItemCapacity();
            }
//            for(ArmouryEquipment equipment : activeEquipment){
//                if(equipment.cap() ==0){
//                    _activatedCapacity = 0;
//                    break;
//                }
//                _activatedCapacity = _activatedCapacity + equipment.cap();
//            }
        } catch (Exception e) {
            _activatedCapacity = 0;
        }
        return _activatedCapacity;
    }

    public String getArmouryList() {
        StringBuilder sb = new StringBuilder("");
        int i = 0;
        for (TacticalCapItem equipment : activatedEquipment) {
            sb.append(String.format(EQUIPMENT_FORMAT_DEFENCE, equipment.getItemName(), equipment.getItemLevel()));
            if (i <= _noActivated) {
                sb.append("\n");
            }
            i++;
        }
        return sb.toString();
    }
//    public String getArmouryListNoLevel() {
//        StringBuilder sb = new StringBuilder("");
//        int i = 0;
//        for (ArmouryEquipment equipment : activeEquipment) {
//            sb.append(equipment.uiName());
//            if (i <= _noActivated) {
//                sb.append("\n");
//            }
//            i++;
//        }
//        return sb.toString();
//    }

    public ArrayList<TacticalCapItem> getActivatedEquipment() {
        return activatedEquipment;
    }

}
