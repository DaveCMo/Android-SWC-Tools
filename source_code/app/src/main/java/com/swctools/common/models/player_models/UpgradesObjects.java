package com.swctools.common.models.player_models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.json.JsonObject;
import javax.json.JsonValue;

public class UpgradesObjects {
    private List<UpgradeItems> upgradeItems;

    public UpgradesObjects(JsonObject upgradesObj) {
        upgradeItems = new ArrayList<>();
        for (Map.Entry<String, JsonValue> value : upgradesObj.entrySet()) {
            upgradeItems.add(new UpgradeItems(value));
        }


    }

    public List<UpgradeItems> getUpgradeItems() {
        return upgradeItems;
    }


}

