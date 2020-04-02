package com.swctools.activity_modules.war_room.processing_models;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.swctools.common.enums.Factions;
import com.swctools.common.helpers.WarDataHelper;
import com.swctools.common.model_list_providers.GameUnitConversionListProvider;
import com.swctools.common.models.player_models.Troop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;

public class WarRoomData_CurrentlyDefending implements Parcelable {
    public final String TAG = WarRoomData_CurrentlyDefending.class.getName();
    private Context mContext;


    private JsonObject currentlyDefendingJson;
    private String battleId;
    private String opponentId;
    private String faction;
    private String attackerFaction;
    private int startTime;
    private int expiration;
    private ArrayList<Troop> troops;
    private ArrayList<Troop> heros;
    private ArrayList<Troop> specialAttack;
    private ArrayList<Troop> champion;
    private ArrayList<String> attackerOutposts;
    private ArrayList<String> defenderOutposts;
    private JsonObject attackerDeploymentData;
    private boolean hasAttackData;
    private HashMap<String, Troop> troopHashMap;

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public WarRoomData_CurrentlyDefending createFromParcel(Parcel in) {
            return new WarRoomData_CurrentlyDefending(in);
        }

        public WarRoomData_CurrentlyDefending[] newArray(int size) {
            return new WarRoomData_CurrentlyDefending[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(battleId);
        parcel.writeString(currentlyDefendingJson.toString());
        parcel.writeString(opponentId);
        parcel.writeString(faction);
        parcel.writeString(attackerFaction);
        parcel.writeList(troops);
        parcel.writeList(heros);
        parcel.writeList(specialAttack);
        parcel.writeList(champion);
        parcel.writeList(attackerOutposts);
        parcel.writeList(defenderOutposts);
        parcel.writeString(attackerDeploymentData.toString());
        parcel.writeInt(startTime);
        parcel.writeInt(expiration);
    }

    public WarRoomData_CurrentlyDefending(Parcel in) {


    }


    public WarRoomData_CurrentlyDefending(JsonObject currentlyDefendingJson, String faction, Context context) {
        this.attackerOutposts = new ArrayList<>();
        this.defenderOutposts = new ArrayList<>();
        this.battleId = currentlyDefendingJson.getString("battleId");
        this.startTime = currentlyDefendingJson.getInt("startTime");
        this.expiration = currentlyDefendingJson.getInt("expiration");
        this.troopHashMap = GameUnitConversionListProvider.getMasterTroopList(context);
        this.currentlyDefendingJson = currentlyDefendingJson;
        this.faction = faction;
        this.opponentId = currentlyDefendingJson.getString("opponentId");
        this.mContext = context;
        if (faction.equalsIgnoreCase(Factions.EMPIRE.getFactionName())) {
            attackerFaction = Factions.REBEL.getFactionName();
        } else {
            attackerFaction = Factions.EMPIRE.getFactionName();
        }
        JsonValue attackerDeploymentDataJsonValue = currentlyDefendingJson.get("attackerDeploymentData");
        if (!attackerDeploymentDataJsonValue.equals(JsonValue.NULL)) {
            hasAttackData = true;
            attackerDeploymentData = currentlyDefendingJson.getJsonObject("attackerDeploymentData");
            setTroops(attackerDeploymentData.getJsonObject("troop"), attackerFaction, context);
            setChampion(attackerDeploymentData.getJsonObject("champion"), attackerFaction, context);
            setSpecialAttack(attackerDeploymentData.getJsonObject("specialAttack"), attackerFaction, context);
            setHeros(attackerDeploymentData.getJsonObject("hero"), attackerFaction, context);
        } else {
            hasAttackData = false;
        }
        JsonArray attackingOps = currentlyDefendingJson.getJsonArray("attackerWarBuffs");
        for (int i = 0; i < attackingOps.size(); i++) {
            String opName = WarDataHelper.getOutPostFriendlyName(attackingOps.getString(i), context);
            attackerOutposts.add(opName);
        }

        JsonArray defendingOps = currentlyDefendingJson.getJsonArray("defenderWarBuffs");
        for (int i = 0; i < defendingOps.size(); i++) {
            String opName = WarDataHelper.getOutPostFriendlyName(defendingOps.getString(i), context);
            defenderOutposts.add(opName);
        }

    }

    private void setTroops(JsonObject troopsExpended, String attackerFaction, Context context) {
        troops = new ArrayList<>();
        for (Map.Entry<String, JsonValue> value : troopsExpended.entrySet()) {
            int tmpCount = Integer.parseInt(value.getValue().toString());
            Troop troop;
            try {
                troop = new Troop(troopHashMap.get(value.getKey()), tmpCount);
            } catch (Exception e) {
                e.printStackTrace();
                troop = new Troop(value.getKey(), value.getKey(), attackerFaction, 0, 0, tmpCount);

            }
            troops.add(troop);
        }

    }

    private void setHeros(JsonObject troopsExpended, String attackerFaction, Context context) {
        heros = new ArrayList<>();
        for (Map.Entry<String, JsonValue> value : troopsExpended.entrySet()) {
            int tmpCount = Integer.parseInt(value.getValue().toString());
            Troop troop;
            try {
                troop = new Troop(troopHashMap.get(value.getKey()), tmpCount);
            } catch (Exception e) {
                troop = new Troop(value.getKey(), value.getKey(), attackerFaction, 0, 0, tmpCount);
                e.printStackTrace();
            }
            heros.add(troop);
        }
    }

    private void setSpecialAttack(JsonObject troopsExpended, String attackerFaction, Context context) {
        specialAttack = new ArrayList<>();
        for (Map.Entry<String, JsonValue> value : troopsExpended.entrySet()) {
            int tmpCount = Integer.parseInt(value.getValue().toString());
            Troop troop;
            try {
                troop = new Troop(troopHashMap.get(value.getKey()), tmpCount);
            } catch (Exception e) {
                troop = new Troop(value.getKey(), value.getKey(), attackerFaction, 0, 0, tmpCount);
            }
            specialAttack.add(troop);
        }
    }


    private void setChampion(JsonObject troopsExpended, String attackerFaction, Context context) {
        champion = new ArrayList<>();
        for (Map.Entry<String, JsonValue> value : troopsExpended.entrySet()) {
            int tmpCount = Integer.parseInt(value.getValue().toString());
            Troop troop;
            try {
                troop = new Troop(troopHashMap.get(value.getKey()), tmpCount);
            } catch (Exception e) {
                troop = new Troop(value.getKey(), value.getKey(), attackerFaction, 0, 0, tmpCount);
                e.printStackTrace();
            }
            champion.add(troop);
        }
    }

    public void setOpponentId(String opponentId) {
        this.opponentId = opponentId;
    }

    public ArrayList<Troop> getTroops() {
        return troops;
    }

    public ArrayList<Troop> getHeros() {
        return heros;
    }

    public ArrayList<Troop> getSpecialAttack() {
        return specialAttack;
    }

    public ArrayList<Troop> getChampion() {
        return champion;
    }

    public void setTroops(ArrayList<Troop> troops) {
        this.troops = troops;
    }

    public void setHeros(ArrayList<Troop> heros) {
        this.heros = heros;
    }

    public void setSpecialAttack(ArrayList<Troop> specialAttack) {
        this.specialAttack = specialAttack;
    }

    public void setChampion(ArrayList<Troop> champion) {
        this.champion = champion;
    }

    public void setFaction(String faction) {
        this.faction = faction;
    }


    public void setAttackerFaction(String attackerFaction) {
        this.attackerFaction = attackerFaction;
    }

    public void setAttackerDeploymentData(JsonObject attackerDeploymentData) {
        this.attackerDeploymentData = attackerDeploymentData;
    }

    public void setHasAttackData(boolean hasAttackData) {
        this.hasAttackData = hasAttackData;
    }

    public String getOpponentId() {
        return opponentId;
    }

    public JsonObject getCurrentlyDefendingJson() {
        return currentlyDefendingJson;
    }

    public String getDefendingOpsCSString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (defenderOutposts.size() > 0) {
            for (int i = 0; i < defenderOutposts.size(); i++) {
                stringBuilder.append(defenderOutposts.get(i));
                if (i < defenderOutposts.size() - 1) {
                    stringBuilder.append(", ");
                }
            }
        }
        return stringBuilder.toString();
    }

    public String getattackingOpsCSString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (attackerOutposts.size() > 0) {
            for (int i = 0; i < attackerOutposts.size(); i++) {
                stringBuilder.append(attackerOutposts.get(i));
                if (i < attackerOutposts.size() - 1) {
                    stringBuilder.append(", ");
                }
            }
        }
        return stringBuilder.toString();
    }

    public ArrayList<String> getAttackerOutposts() {
        return attackerOutposts;
    }

    public ArrayList<String> getDefenderOutposts() {
        return defenderOutposts;
    }

    public String getBattleId() {
        return battleId;
    }


    public int getStartTime() {
        return startTime;
    }


    public int getExpiration() {
        return expiration;
    }

    @Override
    public String toString() {
        return "WarRoomData_CurrentlyDefending{" +
                "TAG='" + TAG + '\'' +
                ", mContext=" + mContext +
                ", currentlyDefendingJson=" + currentlyDefendingJson +
                ", battleId='" + battleId + '\'' +
                ", opponentId='" + opponentId + '\'' +
                ", faction='" + faction + '\'' +
                ", attackerFaction='" + attackerFaction + '\'' +
                ", startTime=" + startTime +
                ", expiration=" + expiration +
                ", troops=" + troops +
                ", heros=" + heros +
                ", specialAttack=" + specialAttack +
                ", champion=" + champion +
                ", attackerOutposts=" + attackerOutposts +
                ", defenderOutposts=" + defenderOutposts +
                ", attackerDeploymentData=" + attackerDeploymentData +
                ", hasAttackData=" + hasAttackData +
                ", troopHashMap=" + troopHashMap +
                '}';
    }
}

