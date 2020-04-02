package com.swctools.common.models.player_models;

import com.swctools.common.enums.BuildingGeneric;
import com.swctools.common.enums.Factions;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class Building implements Serializable {
    private static final String TAG = "Building";
    private static final Map<String, String[]> REPLACEABLEBUILDINGS = new HashMap<String, String[]>() {
        /**
         *
         */
        private static final long serialVersionUID = 1L;

        {
            //Turrets:
            put("BurstTurret", new String[]{"RapidFireTurret", "RocketTurret", "Mortar", "SonicTurret"});
            put("Mortar", new String[]{"SonicTurret", "RapidFireTurret", "RocketTurret", "BurstTurret"});
            put("RapidFireTurret", new String[]{"BurstTurret", "RocketTurret", "Mortar", "SonicTurret"});
            put("RocketTurret", new String[]{"RapidFireTurret", "BurstTurret", "Mortar", "SonicTurret"});
            put("SonicTurret", new String[]{"Mortar", "RapidFireTurret", "RocketTurret", "BurstTurret"});
            put("SonicTurret", new String[]{"Mortar", "RapidFireTurret", "RocketTurret", "BurstTurret"});
//		//TRAPS:
            put("TrapDropship", new String[]{"TrapStrikeAOE", "TrapStrikeGeneric", "TrapStrikeHeavy"});
            put("TrapStrikeAOE", new String[]{"TrapDropship", "TrapStrikeGeneric", "TrapStrikeHeavy"});
            put("TrapStrikeGeneric", new String[]{"TrapStrikeAOE", "TrapDropship", "TrapStrikeHeavy"});
            put("TrapStrikeHeavy", new String[]{"TrapStrikeAOE", "TrapStrikeGeneric", "TrapDropship"});
            //4x4
            put("PlatformDroideka", new String[]{"PlatformHeavyDroideka"});
            put("PlatformHeavyDroideka", new String[]{"PlatformDroideka"});
            put("Armory", new String[]{"TacticalCommand", "FleetCommand"});
            put("TacticalCommand", new String[]{"Armory", "FleetCommand"});
            put("FleetCommand", new String[]{"TacticalCommand", "Armory"});
            //3x3
            put("Barracks", new String[]{"ContrabandCantina", "NavigationCenter", "OffenseLab", "ScoutTower",
                    "CreditGenerator", "MaterialsGenerator", "ContrabandGenerator",
                    "MaterialsStorage", "CreditStorage", "ContrabandStorage"});
            put("ContrabandCantina", new String[]{"Barracks", "NavigationCenter", "OffenseLab", "ScoutTower",
                    "CreditGenerator", "MaterialsGenerator", "ContrabandGenerator",
                    "MaterialsStorage", "CreditStorage", "ContrabandStorage"});
            put("NavigationCenter", new String[]{"ContrabandCantina", "Barracks", "OffenseLab", "ScoutTower",
                    "CreditGenerator", "MaterialsGenerator", "ContrabandGenerator",
                    "MaterialsStorage", "CreditStorage", "ContrabandStorage"});
            put("OffenseLab", new String[]{"ContrabandCantina", "NavigationCenter", "Barracks", "ScoutTower",
                    "CreditGenerator", "MaterialsGenerator", "ContrabandGenerator",
                    "MaterialsStorage", "CreditStorage", "ContrabandStorage"});
            put("ScoutTower", new String[]{"ContrabandCantina", "NavigationCenter", "OffenseLab", "Barracks",
                    "CreditGenerator", "MaterialsGenerator", "ContrabandGenerator",
                    "MaterialsStorage", "CreditStorage", "ContrabandStorage"});
            put("ContrabandGenerator", new String[]{"CreditGenerator", "MaterialsGenerator",
                    "Barracks", "ContrabandCantina", "NavigationCenter", "OffenseLab", "ScoutTower",
                    "MaterialsStorage", "CreditStorage", "ContrabandStorage"});
            put("CreditGenerator", new String[]{"ContrabandGenerator", "MaterialsGenerator",
                    "Barracks", "ContrabandCantina", "NavigationCenter", "OffenseLab", "ScoutTower",
                    "MaterialsStorage", "CreditStorage", "ContrabandStorage"});
            put("MaterialsGenerator", new String[]{"CreditGenerator", "ContrabandGenerator",
                    "Barracks", "ContrabandCantina", "NavigationCenter", "OffenseLab", "ScoutTower",
                    "MaterialsStorage", "CreditStorage", "ContrabandStorage"});
            put("ContrabandStorage", new String[]{"CreditStorage", "MaterialsStorage",
                    "CreditGenerator", "MaterialsGenerator", "ContrabandGenerator",
                    "Barracks", "ContrabandCantina", "NavigationCenter", "OffenseLab", "ScoutTower",});
            put("CreditStorage", new String[]{"ContrabandStorage", "MaterialsStorage",
                    "CreditGenerator", "MaterialsGenerator", "ContrabandGenerator",
                    "Barracks", "ContrabandCantina", "NavigationCenter", "OffenseLab", "ScoutTower",});
            put("MaterialsStorage", new String[]{"CreditStorage", "ContrabandStorage",
                    "CreditGenerator", "MaterialsGenerator", "ContrabandGenerator",
                    "Barracks", "ContrabandCantina", "NavigationCenter", "OffenseLab", "ScoutTower"});


        }
    };
    private final String[] junkTypes = {"rockSmall", "rockMedium", "rockLarge", "junkSmall", "junkMedium", "junkLarge"};
    private String key;
    private String uid;
    private int x;
    private int z;
    private int currentStorage;
    private JsonObject asJson;
    private BuildingProperties buildingProperties;
    private boolean assigned;


    public Building(String key, String uid, int x, int z) {
        this.key = key;
        this.uid = uid;
        this.x = x;
        this.z = z;
        setBuildingProperties(new BuildingProperties(getUid()));


    }

    public Building(JsonObject building) {
        setKey(building.getString("key"));
        setUid(building.getString("uid"));
        setX(building.getInt("x"));
        setZ(building.getInt("z"));
        try {
            setcurrentStorage(building.getInt("currentStorage"));
        } catch (Exception e) {
            setcurrentStorage(0);
        }
        setBuildingProperties(new BuildingProperties(getUid()));


    }

    public static HashMap<String, String[]> getReplaceableBuildingList() {
        HashMap<String, String[]> stringHashMap = new HashMap<>();
        //Turrets:
        stringHashMap.put("BurstTurret", new String[]{"RapidFireTurret", "RocketTurret", "Mortar", "SonicTurret"});
        stringHashMap.put("Mortar", new String[]{"SonicTurret", "RapidFireTurret", "RocketTurret", "BurstTurret"});
        stringHashMap.put("RapidFireTurret", new String[]{"BurstTurret", "RocketTurret", "Mortar", "SonicTurret"});
        stringHashMap.put("RocketTurret", new String[]{"RapidFireTurret", "BurstTurret", "Mortar", "SonicTurret"});
        stringHashMap.put("SonicTurret", new String[]{"Mortar", "RapidFireTurret", "RocketTurret", "BurstTurret"});
//		//TRAPS:
        stringHashMap.put("TrapDropship", new String[]{"TrapStrikeAOE", "TrapStrikeGeneric", "TrapStrikeHeavy"});
        stringHashMap.put("TrapStrikeAOE", new String[]{"TrapDropship", "TrapStrikeGeneric", "TrapStrikeHeavy"});
        stringHashMap.put("TrapStrikeGeneric", new String[]{"TrapStrikeAOE", "TrapDropship", "TrapStrikeHeavy"});
        stringHashMap.put("TrapStrikeHeavy", new String[]{"TrapStrikeAOE", "TrapStrikeGeneric", "TrapDropship"});
        //4x4
        stringHashMap.put("PlatformDroideka", new String[]{"PlatformHeavyDroideka"});
        stringHashMap.put("PlatformHeavyDroideka", new String[]{"PlatformDroideka"});
        stringHashMap.put("Armory", new String[]{"TacticalCommand", "FleetCommand"});
        stringHashMap.put("TacticalCommand", new String[]{"Armory", "FleetCommand"});
        stringHashMap.put("FleetCommand", new String[]{"TacticalCommand", "Armory"});
        //3x3
        stringHashMap.put("Barracks", new String[]{"ContrabandCantina", "NavigationCenter", "OffenseLab", "ScoutTower",
                "CreditGenerator", "MaterialsGenerator", "ContrabandGenerator",
                "MaterialsStorage", "CreditStorage", "ContrabandStorage"});
        stringHashMap.put("ContrabandCantina", new String[]{"Barracks", "NavigationCenter", "OffenseLab", "ScoutTower",
                "CreditGenerator", "MaterialsGenerator", "ContrabandGenerator",
                "MaterialsStorage", "CreditStorage", "ContrabandStorage"});
        stringHashMap.put("NavigationCenter", new String[]{"ContrabandCantina", "Barracks", "OffenseLab", "ScoutTower",
                "CreditGenerator", "MaterialsGenerator", "ContrabandGenerator",
                "MaterialsStorage", "CreditStorage", "ContrabandStorage"});
        stringHashMap.put("OffenseLab", new String[]{"ContrabandCantina", "NavigationCenter", "Barracks", "ScoutTower",
                "CreditGenerator", "MaterialsGenerator", "ContrabandGenerator",
                "MaterialsStorage", "CreditStorage", "ContrabandStorage"});
        stringHashMap.put("ScoutTower", new String[]{"ContrabandCantina", "NavigationCenter", "OffenseLab", "Barracks",
                "CreditGenerator", "MaterialsGenerator", "ContrabandGenerator",
                "MaterialsStorage", "CreditStorage", "ContrabandStorage"});
        stringHashMap.put("ContrabandGenerator", new String[]{"CreditGenerator", "MaterialsGenerator",
                "Barracks", "ContrabandCantina", "NavigationCenter", "OffenseLab", "ScoutTower",
                "MaterialsStorage", "CreditStorage", "ContrabandStorage"});
        stringHashMap.put("CreditGenerator", new String[]{"ContrabandGenerator", "MaterialsGenerator",
                "Barracks", "ContrabandCantina", "NavigationCenter", "OffenseLab", "ScoutTower",
                "MaterialsStorage", "CreditStorage", "ContrabandStorage"});
        stringHashMap.put("MaterialsGenerator", new String[]{"CreditGenerator", "ContrabandGenerator",
                "Barracks", "ContrabandCantina", "NavigationCenter", "OffenseLab", "ScoutTower",
                "MaterialsStorage", "CreditStorage", "ContrabandStorage"});
        stringHashMap.put("ContrabandStorage", new String[]{"CreditStorage", "MaterialsStorage",
                "CreditGenerator", "MaterialsGenerator", "ContrabandGenerator",
                "Barracks", "ContrabandCantina", "NavigationCenter", "OffenseLab", "ScoutTower",});
        stringHashMap.put("CreditStorage", new String[]{"ContrabandStorage", "MaterialsStorage",
                "CreditGenerator", "MaterialsGenerator", "ContrabandGenerator",
                "Barracks", "ContrabandCantina", "NavigationCenter", "OffenseLab", "ScoutTower",});
        stringHashMap.put("MaterialsStorage", new String[]{"CreditStorage", "ContrabandStorage",
                "CreditGenerator", "MaterialsGenerator", "ContrabandGenerator",
                "Barracks", "ContrabandCantina", "NavigationCenter", "OffenseLab", "ScoutTower"});
        stringHashMap.put("Wall", new String[]{"Wall"});

        return stringHashMap;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getCurrentStorage() {
        return currentStorage;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {

        this.x = x;
    }

    public String printDetail() {
        String BUILDINGFORMAT = "%1$s (x:%2$s z:%3$s)\n";

        return this.key + "|" + String.format(BUILDINGFORMAT, this.uid, this.x, this.z);
    }

    public String printCurrentSQL() {
        String prepSt = "INSERT INTO current_layout (`key`, `uid`, `x`, `z`) VALUES ('%1$s', '%2$s', %3$s, %4$s);";
        return String.format(prepSt, key, uid, x, z);
    }

    public String printProposedSQL() {
        String prepSt = "INSERT INTO proposed_layout (`key`, `uid`, `x`, `z`) VALUES (`%1$s`, `%2$s`, %3$s, %4$s);";
        return String.format(prepSt, key, uid, x, z);
    }

    public void setcurrentStorage(int currentStorage) {
        this.currentStorage = currentStorage;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public JsonObject getAsJson() {
        asJson = Json.createObjectBuilder()
                .add("key", this.key)
                .add("uid", this.uid)
                .add("x", this.x)
                .add("z", this.z)
                .build();
        return asJson;
    }

    public BuildingProperties getBuildingProperties() {
//        setBuildingProperties(new BuildingProperties(getUid()));
        return buildingProperties;
    }

    public void setBuildingProperties(BuildingProperties buildingProperties) {
        this.buildingProperties = buildingProperties;
    }

    public boolean getAssigned() {
        return assigned;
    }

    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
    }

    public JsonObject asLayoutObject() {
        JsonObjectBuilder buildingsBuilder = Json.createObjectBuilder();
        JsonObjectBuilder positionsBuilder = Json.createObjectBuilder();
        positionsBuilder.add("x", getX());
        positionsBuilder.add("z", getZ());
        buildingsBuilder.add(getKey(), positionsBuilder);
        return buildingsBuilder.build();
    }

    public class BuildingProperties {
        private String genericName;
        //        private BuildingGeneric genericName;
        private int level;
        private boolean isTrap = false;
        private boolean isJunk = false;

        public BuildingProperties(String uid) {

            String tmpStr = null;
            if (uid.startsWith(Factions.EMPIRE.getFactionName())) {
                tmpStr = uid.replaceAll(Factions.EMPIRE.getFactionName(), "");

            } else if (uid.startsWith(Factions.REBEL.getFactionName())) {
                tmpStr = uid.replaceAll(Factions.REBEL.getFactionName(), "");
            } else {
                tmpStr = uid; //probably junk
            }
            boolean genericFound = false;

            for (BuildingGeneric buildGeneric : BuildingGeneric.values()) {
                try {
                    if (tmpStr.substring(0, buildGeneric.getName().length()).equalsIgnoreCase(buildGeneric.getName())) {
                        genericFound = true;
                        setGenericName(buildGeneric.getName());
                        isTrap = buildGeneric.isTrap();
                        isJunk = buildGeneric.isJunk();
                        setLevel(Integer.parseInt(uid.replaceAll(Factions.EMPIRE.getFactionName(), "").replaceAll(Factions.REBEL.getFactionName(), "").substring(getGenericName().length())));
                        break;
                    }
                } catch (Exception e) {
                    setGenericName(tmpStr);
//                    e.printStackTrace();
                }
            }
            if (!genericFound) {
                setGenericName(tmpStr);
            }
        }

        public boolean isJunk() {
            return isJunk;
        }

        public String getGenericName() {
            return genericName;
        }

        public void setGenericName(String genericName) {

            this.genericName = genericName;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public boolean isTrap() {
            return isTrap;
        }
    }


}
