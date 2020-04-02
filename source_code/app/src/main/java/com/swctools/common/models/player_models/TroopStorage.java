package com.swctools.common.models.player_models;

import android.content.Context;

import com.swctools.R;
import com.swctools.common.enums.Factions;
import com.swctools.activity_modules.player.models.TacticalCapacityData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.json.JsonObject;
import javax.json.JsonValue;

public class TroopStorage {
    private static final String TAG = "TroopStorage";
    private String faction;
    private JsonObject inventoryObject;
    private JsonObject subStorage;
    private TacticalCapacityData troopData;
    private TacticalCapacityData airData;
    private TacticalCapacityData heroesData;
    private Context mContext;
    private HashMap<String, Troop> getMasterTroopList;


    public TroopStorage(JsonObject inventoryObject, String faction, HashMap<String, Troop> getMasterTroopList, Context context) {
        this.inventoryObject = inventoryObject;
        this.faction = faction;
        this.getMasterTroopList = getMasterTroopList;

        this.mContext = context;
        this.subStorage = this.inventoryObject.getJsonObject("subStorage");
        buildTroopStorageData();
        buildHeroStorageData();
        buildAirStorageData();
    }

    private void buildAirStorageData() {
        JsonObject specialAttack = subStorage.getJsonObject("specialAttack");
        int maxCap = specialAttack.getInt("capacity");
        int troopCount = 0;
        ArrayList<TacticalCapItem> troopsList = new ArrayList<>();
        int currentCapacity = 0;
        for (Map.Entry<String, JsonValue> value : specialAttack.getJsonObject("storage").entrySet()) {
            JsonObject airItem = (JsonObject) value.getValue();
            Troop airTroop = getMasterTroopList.get(value.getKey());
            if (airTroop != null) {
                int amount = airItem.getInt("amount");
                currentCapacity = currentCapacity + (amount * airTroop.cap());
                troopsList.add(new TacticalCapItem(airTroop.uiName(), airTroop.getTroopLevel(), airTroop.getTotalCap(amount), amount, null));
                troopCount++;
            } else {
                troopsList.add(new TacticalCapItem(value.getKey().toString(), 0, 0, 0, null));
            }
        }
        airData = new TacticalCapacityData("STARSHIPS", starShipCommand(), maxCap, currentCapacity, troopsList, "STARSHIPS", troopCount, true, TacticalCapacityData.STARSHIP, mContext);
    }

    private void buildHeroStorageData() {
        JsonObject hero = subStorage.getJsonObject("hero");
        ArrayList<TacticalCapItem> troopsList = new ArrayList<>();
        int maxCap = 3;
        int currentCapacity = 0;
        int troopCount = 0;
        for (Map.Entry<String, JsonValue> value : hero.getJsonObject("storage").entrySet()) {
            JsonObject heroItem = (JsonObject) value.getValue();
            Troop heroTroop = getMasterTroopList.get(value.getKey());
            if (heroTroop != null) {
                troopsList.add(new TacticalCapItem(heroTroop.uiName(), heroTroop.getTroopLevel(), 1, 1, null));
                troopCount++;
            } else {
                troopsList.add(new TacticalCapItem(value.getKey(), 1, 1, 0, null));
            }
        }
        heroesData = new TacticalCapacityData("HEROES", heroCommand(), maxCap, troopCount, troopsList, "Heroes", troopCount, false, TacticalCapacityData.HERO, mContext);

    }

    private void buildTroopStorageData() {

        //Reference all the objects:
        JsonObject troop = subStorage.getJsonObject("troop");

        ArrayList<TacticalCapItem> troopsList = new ArrayList<>();

        //Build Troop Storage
        int troopCapacity = troop.getInt("capacity");
        int currentCapacity = 0;
        int troopCount = 0;
        for (Map.Entry<String, JsonValue> value : troop.getJsonObject("storage").entrySet()) {
            JsonObject troops = (JsonObject) value.getValue();
            Troop troop1 = getMasterTroopList.get(value.getKey());
            if (troop1 != null) {
                int amount = troops.getInt("amount");
                currentCapacity = currentCapacity + (amount * troop1.cap());
                troopsList.add(new TacticalCapItem(troop1.uiName(), troop1.getTroopLevel(), troop1.getTotalCap(amount), amount, null));
                troopCount++;
            } else {
                troopsList.add(new TacticalCapItem(value.getKey(), 0, 0, 0, null));
            }

        }
        troopData = new TacticalCapacityData("TROOPS", troopTransport(), troopCapacity, currentCapacity, troopsList, "", troopCount, true, TacticalCapacityData.TROOPTRANSPORT, mContext);

    }

    public TacticalCapacityData getTroopData() {
        return troopData;
    }

    public TacticalCapacityData getAirData() {
        return airData;
    }

    public TacticalCapacityData getHeroesData() {
        return heroesData;
    }

    private int troopTransport() {
        if (faction.equalsIgnoreCase(Factions.EMPIRE.getFactionName())) {
            return R.mipmap.troop_transport_empire_fg;
        } else {
            return R.mipmap.troop_transport_rebel_fg;
        }
    }

    private int heroCommand() {
        if (faction.equalsIgnoreCase(Factions.EMPIRE.getFactionName())) {
            return R.mipmap.hero_cmd_empire_fg;
        } else {
            return R.mipmap.hero_cmd_rebel_fg;
        }
    }

    private int starShipCommand() {
        if (faction.equalsIgnoreCase(Factions.EMPIRE.getFactionName())) {
            return R.mipmap.starship_cmd_empire_fg;
        } else {
            return R.mipmap.starship_cmd_rebel_fg;
        }
    }
}
