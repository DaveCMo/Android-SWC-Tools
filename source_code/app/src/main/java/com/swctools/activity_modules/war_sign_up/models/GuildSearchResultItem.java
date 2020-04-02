package com.swctools.activity_modules.war_sign_up.models;

import javax.json.JsonObject;

public class GuildSearchResultItem {
    private static final String TAG = "GuildSearchResultItem";
    public String faction;
    public String guildName;
    public String guildId;
    public int members;
    public JsonObject guildObject;

    public GuildSearchResultItem() {

    }

    public GuildSearchResultItem(JsonObject guildObject) {

        this.guildObject = guildObject;
        this.faction = guildObject.getString("faction");
        this.guildName = guildObject.getString("name");
        this.guildId = guildObject.getString("_id");
        this.members = guildObject.getInt("members");
    }

}
