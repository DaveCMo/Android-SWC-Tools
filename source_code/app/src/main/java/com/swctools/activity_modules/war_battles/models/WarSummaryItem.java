package com.swctools.activity_modules.war_battles.models;

public class WarSummaryItem {
    private String item;
    private int max;
    private int total;

    public WarSummaryItem(String item, int max, int total) {
        this.item = item;
        this.max = max;
        this.total = total;
    }


    public String getItem() {
        return item;
    }

    public int getMax() {
        return max;
    }

    public int getTotal() {
        return total;
    }

    @Override
    public String toString() {
        return "WarSummaryItem{" +
                "item='" + item + '\'' +
                ", max=" + max +
                ", total=" + total +
                '}';
    }
}
