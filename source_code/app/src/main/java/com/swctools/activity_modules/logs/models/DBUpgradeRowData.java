package com.swctools.activity_modules.logs.models;

public class DBUpgradeRowData {

    public int id;
    public int dbVersion;
    public String appCode;
    public String codeMessage;

    public DBUpgradeRowData(int id, int dbVersion, String appCode, String codeMessage) {
        this.id = id;
        this.dbVersion = dbVersion;
        this.appCode = appCode;
        this.codeMessage = codeMessage;
    }
}
