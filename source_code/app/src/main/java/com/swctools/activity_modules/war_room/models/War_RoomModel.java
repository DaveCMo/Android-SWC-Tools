package com.swctools.activity_modules.war_room.models;

import android.content.Context;
import android.util.Log;

import com.swctools.common.helpers.DateTimeHelper;
import com.swctools.util.StringUtil;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class War_RoomModel {
    private static final String TAG = "War_RoomModel";

    private static final String WARVS = "WAR! vs. %1s\n\n";
    private static final String PRINTMEMBER = "%1$s (%2$s: %3$s) -\n";
    private static final String PRINTMEMBER_NOBS = "%1$s -\n";
    private static final String PRINTMEMBERWITHUL = "%1$s (%2$s: %3$s) %4$sUL -\n";
    private static final String PRINTMEMBERWITHUL_NOBS = "%1$s %2$sUL -\n";
    private static final String PRINTMEMBERCLEARED = "%1$s (%2$s: %3$s)\n";
    private static final String PRINTMEMBERCLEAREDNOBS = "%1$s \n";


    private String warId;
    private War_Log warLog;
    private ArrayList<War_Room_OutPost> guildOps;
    private ArrayList<War_Room_OutPost> neutralOps;
    private ArrayList<War_Room_OutPost> rivalOps;
    private ArrayList<War_WarParticipant> guildParticipants;
    private ArrayList<War_WarParticipant> rivalParticipants;

    public War_RoomModel(String warId, Context context) {
        this.warId = warId;
        //build model:
        this.warLog = WarRoomListProvider.getWarLog(warId, context);
        this.guildOps = WarRoomListProvider.getWarOps(warId, warLog.getGuildId(), context);
        this.neutralOps = WarRoomListProvider.getWarOps(warId, "", context);
        this.rivalOps = WarRoomListProvider.getWarOps(warId, warLog.getRivalGuildId(), context);
        this.guildParticipants = WarRoomListProvider.getWarParticipants(warId, warLog.getGuildId(), context);
        this.rivalParticipants = WarRoomListProvider.getWarParticipants(warId, warLog.getRivalGuildId(), context);
    }

    public String getWarId() {
        return warId;
    }

    public War_Log getWarLog() {
        Log.d(TAG, "getWarLog: "+ toString());
        return warLog;
    }

    public ArrayList<War_Room_OutPost> getGuildOps() {
        return guildOps;
    }

    public ArrayList<War_Room_OutPost> getNeutralOps() {
        return neutralOps;
    }

    public ArrayList<War_Room_OutPost> getRivalOps() {
        return rivalOps;
    }

    public ArrayList<War_WarParticipant> getGuildParticipants() {
        return guildParticipants;
    }

    public ArrayList<War_WarParticipant> getRivalParticipants() {
        return rivalParticipants;
    }

    @Override
    public String toString() {
        return "War_RoomModel{" +
                "warId='" + warId + '\'' +
                ", warLog=" + warLog +
                ", guildOps=" + guildOps +
                ", neutralOps=" + neutralOps +
                ", rivalOps=" + rivalOps +
                ", guildParticipants=" + guildParticipants +
                ", rivalParticipants=" + rivalParticipants +
                '}';
    }


    public String buildHitList(boolean includeTz, boolean includeOps, boolean includeScore, boolean includeULS, boolean splitList, String tzId, boolean includeBS, Context context) {
        StringBuilder hitList = new StringBuilder();
        hitList.append(hitListHeader(tzId, includeTz, includeOps, includeScore, context));
        hitList.append(getBases(includeULS, splitList, includeBS));


        return hitList.toString();

    }

    private String hitListHeader(String tzId, boolean includeTz, boolean includeOps, boolean includeScore, Context context) {
        StringBuilder hitList = new StringBuilder();
        String squadName = StringUtil.htmlRemovedGameName(URLDecoder.decode(warLog.getRivalGuildName()));

        hitList.append(String.format(WARVS, squadName));
        if (includeTz) {
            hitList.append(DateTimeHelper.longDateTimeByTZ(warLog.getPrepEndTime(), tzId, context) + " (" + tzId + ")\n\n");
        }
        if (includeScore) {
            hitList.append("Score: " + warLog.getSquadScore() + "-" + warLog.getRivalScore() + "\n");
            hitList.append("Wipes: " + warLog.getSquadWipes() + "-" + warLog.getRivalWipes() + "\n");
            hitList.append("Attacks: " + warLog.getSquadAttacks() + "-" + warLog.getRivalAttacks() + "\n\n");
        }

        if (includeOps) {
            hitList.append("OUTPOSTS:\n");
            hitList.append("----------\n");

            for (War_Room_OutPost warBuffBase : getRivalOps()) {
                hitList.append(warBuffBase.getOutPostName() + "\n");
            }
            for (War_Room_OutPost warBuffBase : getNeutralOps()) {
                hitList.append(warBuffBase.getOutPostName() + "\n");
            }
            for (War_Room_OutPost warBuffBase : getGuildOps()) {
                hitList.append(warBuffBase.getOutPostName() + "\n");
            }

        }

        return hitList.toString();
    }


    private String getBases(boolean includeULS, boolean splitList, boolean includeBS) {
        StringBuilder hitList = new StringBuilder();
        hitList.append("\n");

        if (includeBS) {
            hitList.append("BASES HQ - Strength");
        } else {
            hitList.append("BASES");

        }
        if (includeULS) {
            hitList.append(" - ULS");
        }
        hitList.append("\n");
        hitList.append("-------------------\n");


        hitList.append(sortAndFinaliseLIst(includeULS, splitList, includeBS).toString());

        return hitList.toString();
    }

    private StringBuilder sortAndFinaliseLIst(boolean includeULS, boolean splitList, boolean includeBS) {
        StringBuilder hitList = new StringBuilder();
        StringBuilder clearedList = new StringBuilder();
        Collections.sort(rivalParticipants, new Comparator<War_WarParticipant>() {
            @Override
            public int compare(War_WarParticipant m1, War_WarParticipant m2) {
                return m2.getBaseScore() - m1.getBaseScore();
            }
        });

        for (War_WarParticipant m : rivalParticipants) {
            String memberStr = "";
            if (includeULS) {
                if (includeBS) {
                    memberStr = String.format(PRINTMEMBERWITHUL, m.getCleanName(), m.getHqLevel(), m.getBaseScore(), m.getVictoryPoints());
                } else {
                    memberStr = String.format(PRINTMEMBERWITHUL_NOBS, m.getCleanName(), m.getVictoryPoints());
                }
            } else {
                if (includeBS) {
                    memberStr = String.format(PRINTMEMBER, m.getCleanName(), m.getHqLevel(), m.getBaseScore());
                } else {
                    memberStr = String.format(PRINTMEMBER_NOBS, m.getCleanName());
                }
            }

            if (splitList && m.getVictoryPoints() == 0) {
                if(includeBS){
                    clearedList.append(String.format(PRINTMEMBERCLEARED, m.getCleanName(), m.getHqLevel(), m.getBaseScore()));
                } else {
                    clearedList.append(String.format(PRINTMEMBERCLEAREDNOBS, m.getCleanName()));
                }
            } else if ((splitList && m.getVictoryPoints() > 0) || !splitList) {
                hitList.append(memberStr);
            }
        }

        if (splitList) {
            hitList.append("\n");
            hitList.append("Clears\n");
            hitList.append("-------------------\n");
            hitList.append(clearedList);
        }
        return hitList;
    }
}
