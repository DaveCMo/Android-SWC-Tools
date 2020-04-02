package com.swctools.activity_modules.EditLayoutJson.models;

import com.swctools.common.models.player_models.Building;

public class EditBuilding extends Building {
    private int id;
    public int edit;

    public int getId() {
        return id;
    }

    public EditBuilding(int _id, String key, String uid, int x, int z, int edit) {
        super(key, uid, x, z);
        this.id = _id;
        this.edit = edit;
    }
}
