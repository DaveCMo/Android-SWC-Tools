package com.swctools.activity_modules.war_sign_up.models;

import javax.json.JsonObject;

public class GuildGetPublicResultItem extends GuildSearchResultItem {
    public GuildGetPublicResultItem(JsonObject guildObject) {
        super();
        this.guildObject = guildObject;
        this.faction = guildObject.getJsonObject("membershipRestrictions").getString("faction");

        this.guildName = guildObject.getString("name");
        this.guildId = guildObject.getString("id");
        this.members = guildObject.getJsonArray("members").size();
    }
}
