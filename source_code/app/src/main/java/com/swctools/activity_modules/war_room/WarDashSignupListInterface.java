package com.swctools.activity_modules.war_room;

public interface WarDashSignupListInterface {


    void triggerRebuildList(String tzId, boolean includeTz, boolean includeOps, boolean includeULS, boolean splitList, boolean includeScore, boolean includeBS);

    void shareText(String s);

    void copyText(String s);
}
