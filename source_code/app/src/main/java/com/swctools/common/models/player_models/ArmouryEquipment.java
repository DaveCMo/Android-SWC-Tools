package com.swctools.common.models.player_models;

import android.content.Context;

public class ArmouryEquipment {

    private String _gameName;
    private String _uiName;
    private String _faction;
    private int _cap;
    private int equipLevel;
    private String _availableOn;
    private String type;
    private String _level;
    private final String LEVEL_FORMAT = "(Level %1$s)";


    public ArmouryEquipment(String _gameName, String _uiName, String _faction, int _cap, int equipLevel, String _availableOn, String type) {
        this._gameName = _gameName;
        this._uiName = _uiName;
        this._faction = _faction;
        this._cap = _cap;
        this.equipLevel = equipLevel;
        this._availableOn = _availableOn;
        this.type = type;
    }

    public String gameName() {
        return _gameName;
    }

    public String uiName() {
        return _uiName;
    }

    public String faction() {
        return _faction;
    }

    public String level() {
        return _level;
    }

    public int cap() {
        return _cap;
    }

    public String get_availableOn() {
        return _availableOn;
    }

    private void setEquipmentLevel(String gameName, String rawName) {
        int gameName_Len = gameName.length();
        int uiName_len = rawName.length();
        try {
            _level = String.format(LEVEL_FORMAT, gameName.substring((gameName.indexOf(rawName) + uiName_len), gameName_Len));
            try {
                equipLevel = Integer.parseInt(gameName.substring((gameName.indexOf(rawName) + uiName_len), gameName_Len));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            _level = "";
        }
    }

    public int getEquipLevel() {
        return equipLevel;
    }

    public String getType() {
        return type;
    }
}
