package com.swctools.activity_modules.war_sign_up.interfaces;

import com.swctools.activity_modules.war_sign_up.models.GuildMember;

public interface WarMembersListCallBack {
    void memberChkChanged(GuildMember guildMember, boolean b);
}