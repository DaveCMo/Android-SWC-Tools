package com.swctools.common.models.player_models;

import java.util.Map;

import javax.json.JsonValue;

public class UpgradeItems {
    public String itemName;
    public int itemLevel;

    public UpgradeItems(Map.Entry<String, JsonValue> upgradeItem) {
        itemName = upgradeItem.getKey();
        itemLevel = Integer.parseInt(upgradeItem.getValue().toString());
    }
}