package com.swctools.activity_modules.war_sign_up.models;

import com.swctools.util.StringUtil;

import java.net.URLDecoder;

import javax.json.JsonObject;

public class GuildMember {
    public String memberName;
    public int xp;
    public int warParty;
    public int hqLevel;
    public String playerId;
    public boolean selected = false;

    public GuildMember(JsonObject guildMember) {
        try {
            this.memberName = StringUtil.htmlRemovedGameName(URLDecoder.decode(guildMember.getString("name")));
        } catch (Exception e) {
            this.memberName = guildMember.getString("name");
        }
        this.xp = guildMember.getInt("xp");
        this.hqLevel = guildMember.getInt("hqLevel");
        this.warParty = guildMember.getInt("warParty");
        this.playerId = guildMember.getString("playerId");
    }

}
