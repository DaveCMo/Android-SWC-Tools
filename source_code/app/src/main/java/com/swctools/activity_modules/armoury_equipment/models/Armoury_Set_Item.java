package com.swctools.activity_modules.armoury_equipment.models;

public class Armoury_Set_Item {
    private String equipGameName;
    private String equipUIName;
    private String faction;
    private int capacity;
    private int level;
    private String availability;
    private String type;

    public Armoury_Set_Item(String equipGameName, String equipUIName, String faction, int capacity, int level, String availability, String type) {
        this.equipGameName = equipGameName;
        this.equipUIName = equipUIName;
        this.faction = faction;
        this.capacity = capacity;
        this.level = level;
        this.availability = availability;
        this.type = type;
    }

    public String getFaction() {
        return faction;
    }

    public String getEquipGameName() {
        return equipGameName;
    }

    public String getEquipUIName() {
        return equipUIName;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getLevel() {
        return level;
    }

    public String getGameNameNoLevel() {
        return equipGameName.replace(String.valueOf(level), "");
    }

    public String getAvailability() {
        return availability;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Armoury_Set_Item{" +
                "equipGameName='" + equipGameName + '\'' +
                ", equipUIName='" + equipUIName + '\'' +
                ", faction='" + faction + '\'' +
                ", capacity=" + capacity +
                ", level=" + level +
                ", availability='" + availability + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
