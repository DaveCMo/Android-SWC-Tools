package com.swctools.activity_modules.war_battles.models;

public class War_TroopDeployed {

    private String deployed;
    private int qty;

    public War_TroopDeployed(String deployed, int qty) {
        this.deployed = deployed;
        this.qty = qty;
    }

    public String getDeployed() {
        return deployed;
    }

    public int getQty() {
        return qty;
    }

    @Override
    public String toString() {
        return "War_TroopDeployed{" +
                "deployed='" + deployed + '\'' +
                ", qty=" + qty +
                '}';
    }
}
