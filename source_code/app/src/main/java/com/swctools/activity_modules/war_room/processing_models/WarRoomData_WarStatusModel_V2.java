package com.swctools.activity_modules.war_room.processing_models;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.swctools.activity_modules.war_sign_up.models.GuildMember;
import com.swctools.common.enums.ApplicationMessageTemplates;
import com.swctools.common.enums.BundleKeys;
import com.swctools.common.helpers.BundleHelper;
import com.swctools.common.helpers.DateTimeHelper;
import com.swctools.common.model_list_providers.GameUnitConversionListProvider;
import com.swctools.swc_server_interactions.results.SWCGetPublicGuildResponseData;
import com.swctools.swc_server_interactions.results.SWCGetWarStatusResultJson;
import com.swctools.swc_server_interactions.results.SWCGuildWarPartRespData;
import com.swctools.swc_server_interactions.swc_commands.Cmd_GetWarParticipant;
import com.swctools.swc_server_interactions.swc_commands.Cmd_GuildWarStatus;
import com.swctools.util.StringUtil;

import java.io.StringReader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonValue;

public class WarRoomData_WarStatusModel_V2 implements Parcelable {


    private static final String TAG = "WarStatusModel";
    private static final String WARVS = "WAR! vs. %1s\n\n";
    private static final String PRINTMEMBER = "%1$s (%2$s: %3$s) -\n";
    private static final String PRINTMEMBERWITHUL = "%1$s (%2$s: %3$s) %4$sUL -\n";
    private static final String PRINTMEMBERCLEARED = "%1$s (%2$s: %3$s)\n";

    private SWCGetPublicGuildResponseData rivalGuildResponseData;
    private SWCGetPublicGuildResponseData guildResponseData;

    private SWCGetWarStatusResultJson swcGetWarStatusResultData;
    private SWCGuildWarPartRespData swcGuildWarPartRespData;

    private String playerId;
    private int squadAttacks;
    private int rivalAttacks;
    private int squadScore;
    private int rivalScore;
    private int squadWipes;
    private int rivalWipes;
    private WarRoomData_WarSquadModel warSquadModel;
    private WarRoomData_WarSquadModel rivalSquadModel;
    private ArrayList<WarRoomData_Outposts> myOutposts;
    private ArrayList<WarRoomData_Outposts> neutralOutposts;
    private ArrayList<WarRoomData_Outposts> enemyOutposts;
    private ArrayList<WarRoomData_Outposts> buffBases;
    private Context context;

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(playerId);
        parcel.writeInt(squadAttacks);
        parcel.writeInt(rivalAttacks);
        parcel.writeInt(squadScore);
        parcel.writeInt(rivalScore);
        parcel.writeInt(squadWipes);
        parcel.writeInt(rivalWipes);
        parcel.writeParcelable(warSquadModel, Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
        parcel.writeParcelable(rivalSquadModel, Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
        parcel.writeList(myOutposts);
        parcel.writeList(neutralOutposts);
        parcel.writeList(enemyOutposts);
        parcel.writeList(buffBases);
    }

    public WarRoomData_WarStatusModel_V2(Parcel in) {
        playerId = in.readString();
        squadAttacks = in.readInt();
        rivalAttacks = in.readInt();
        squadScore = in.readInt();
        rivalScore = in.readInt();
        squadWipes = in.readInt();
        rivalWipes = in.readInt();
        warSquadModel = in.readParcelable(WarRoomData_WarSquadModel.class.getClassLoader());
        rivalSquadModel = in.readParcelable(WarRoomData_WarSquadModel.class.getClassLoader());
        myOutposts = in.readArrayList(WarRoomData_Outposts.class.getClassLoader());
        neutralOutposts = in.readArrayList(WarRoomData_Outposts.class.getClassLoader());
        enemyOutposts = in.readArrayList(WarRoomData_Outposts.class.getClassLoader());
        buffBases = in.readArrayList(WarRoomData_Outposts.class.getClassLoader());

    }

    public static final Creator CREATOR = new Creator() {
        public WarRoomData_WarStatusModel_V2 createFromParcel(Parcel in) {
            return new WarRoomData_WarStatusModel_V2(in);
        }

        public WarRoomData_WarStatusModel_V2[] newArray(int size) {
            return new WarRoomData_WarStatusModel_V2[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }


    public WarRoomData_WarStatusModel_V2(String playerId, Context context) {
        this.playerId = playerId;
        this.context = context;
    }


    public void buildModel() {
        String key;
        BundleHelper bundleHelper;
        key = String.format(ApplicationMessageTemplates.VISIT_KEY.getemplateString(), Cmd_GuildWarStatus.COMMAND, playerId);
        bundleHelper = new BundleHelper(key);
        JsonObject warStatusResultObj = Json.createReader(new StringReader(bundleHelper.get_value(context))).readObject();
        key = String.format(ApplicationMessageTemplates.VISIT_KEY.getemplateString(), Cmd_GetWarParticipant.COMMAND, playerId);
        bundleHelper = new BundleHelper(key);
        JsonObject warPartResultObj = Json.createReader(new StringReader(bundleHelper.get_value(context))).readObject();



        swcGuildWarPartRespData = new SWCGuildWarPartRespData(warPartResultObj);
        swcGetWarStatusResultData = new SWCGetWarStatusResultJson(warStatusResultObj);
        warSquadModel = new WarRoomData_WarSquadModel(swcGetWarStatusResultData.getGuildJson(), playerId, swcGuildWarPartRespData, GameUnitConversionListProvider.getMasterTroopList(context), context);
        rivalSquadModel = new WarRoomData_WarSquadModel(swcGetWarStatusResultData.getRivalJson(), context);

        key = String.format(ApplicationMessageTemplates.VISIT_KEY.getemplateString(), BundleKeys.GUILD_ID, rivalSquadModel.getGuildId());
        bundleHelper = new BundleHelper(key);
        rivalGuildResponseData = new SWCGetPublicGuildResponseData(bundleHelper.get_value(context));


        key = String.format(ApplicationMessageTemplates.VISIT_KEY.getemplateString(), BundleKeys.GUILD_ID, warSquadModel.getGuildId());
        bundleHelper = new BundleHelper(key);
        guildResponseData = new SWCGetPublicGuildResponseData(bundleHelper.get_value(context));


        enemyOutposts = new ArrayList<>();
        myOutposts = new ArrayList<>();
        neutralOutposts = new ArrayList<>();


        for (WarPlayer warPlayer : warSquadModel.getWarPlayers()) {
            warPlayer.setHQandBS(guildResponseData);
            squadAttacks = squadAttacks + warPlayer.getTurns();
            squadScore = squadScore + warPlayer.getScore();
            if (warPlayer.getVictoryPoints() == 0) {
                rivalWipes++;
            }
        }

        for (WarRoomData_WarParticipant warParticipant : warSquadModel.getSquadParticipants()) {
            warParticipant.setHQandBS(guildResponseData);
            squadAttacks = squadAttacks + warParticipant.getTurns();
            squadScore = squadScore + warParticipant.getScore();
            if (warParticipant.getVictoryPoints() == 0) {
                rivalWipes++;
            }
        }
//        for (Object squadWarParticipantObj : warSquadModel.getWarParticipants()) {
//            if (squadWarParticipantObj instanceof WarPlayer) {
//                WarPlayer tosetBs = (WarPlayer) squadWarParticipantObj;
//                tosetBs.setHQandBS(guildResponseData);
//            }
//            WarParticipant squadWarParticipant = (WarParticipant) squadWarParticipantObj;
//            squadWarParticipant.setHQandBS(guildResponseData);
//            squadAttacks = squadAttacks + squadWarParticipant.getTurns();
//            squadScore = squadScore + squadWarParticipant.getScore();
//            if (squadWarParticipant.getVictoryPoints() == 0) {
//                rivalWipes++;
//            }
//        }

        for (Object rivalWarParticipantObj : rivalSquadModel.getWarParticipants()) {
            WarRoomData_WarParticipant rivalWarParticipant = (WarRoomData_WarParticipant) rivalWarParticipantObj;
            rivalWarParticipant.setHQandBS(rivalGuildResponseData);
            rivalAttacks = rivalAttacks + rivalWarParticipant.getTurns();
            rivalScore = rivalScore + rivalWarParticipant.getScore();
            if (rivalWarParticipant.getVictoryPoints() == 0) {
                squadWipes++;
            }
        }
        buffBases = new ArrayList<>();
        for (JsonValue jsonValue : swcGetWarStatusResultData.buffBaseJsonArray()) {
            JsonObject buffBaseObj = (JsonObject) jsonValue;
            buffBases.add(new WarRoomData_Outposts(buffBaseObj, context));
        }

        for (int i = 0; i < buffBases.size(); i++) {
            WarRoomData_Outposts warBuffBase = buffBases.get(i);
            if (warBuffBase.getOwnerId().equalsIgnoreCase("")) {
                neutralOutposts.add(warBuffBase);
            } else if (warBuffBase.getOwnerId().equalsIgnoreCase(warSquadModel.getGuildId())) {
                myOutposts.add(warBuffBase);
            } else if (warBuffBase.getOwnerId().equalsIgnoreCase(rivalSquadModel.getGuildId())) {
                enemyOutposts.add(warBuffBase);
            }
        }

        Collections.sort(rivalSquadModel.getWarParticipants(), new Comparator<Object>() {
            @Override
            public int compare(Object m1, Object m2) {
                if (m1 instanceof WarRoomData_WarParticipant && m2 instanceof WarRoomData_WarParticipant) {
                    WarRoomData_WarParticipant warParticipant1 = (WarRoomData_WarParticipant) m1;
                    WarRoomData_WarParticipant warParticipant2 = (WarRoomData_WarParticipant) m2;
                    return warParticipant2.getBaseScore() - warParticipant1.getBaseScore();
                } else {
                    return 1 - 1;
                }
            }
        });

        Collections.sort(warSquadModel.getSquadParticipants(), new Comparator<Object>() {
            @Override
            public int compare(Object m1, Object m2) {
                if (m1 instanceof WarRoomData_WarParticipant && m2 instanceof WarRoomData_WarParticipant) {
                    WarRoomData_WarParticipant warParticipant1 = (WarRoomData_WarParticipant) m1;
                    WarRoomData_WarParticipant warParticipant2 = (WarRoomData_WarParticipant) m2;
                    return warParticipant2.getBaseScore() - warParticipant1.getBaseScore();
                } else {
                    return 1 - 1;
                }
            }
        });

        for (WarPlayer warPlayer : warSquadModel.getWarPlayers()) {
            warSquadModel.getWarParticipants().add(warPlayer);
        }

        warSquadModel.getWarParticipants().addAll(warSquadModel.getSquadParticipants());


    }


    private String hitListHeader(String tzId, boolean includeTz, boolean includeOps, boolean includeScore) {
        StringBuilder hitList = new StringBuilder();
        String squadName = StringUtil.htmlRemovedGameName(URLDecoder.decode(rivalGuildResponseData.getResult().getString("name")));

        hitList.append(String.format(WARVS, squadName));
        if (includeTz) {
            hitList.append(DateTimeHelper.longDateTimeByTZ(getSwcGetWarStatusResultData().startTime(), tzId, context) + " (" + tzId + ")\n\n");
        }
        if (includeScore) {
            hitList.append("Score: " + squadScore + "-" + rivalScore + "\n");
            hitList.append("Wipes: " + squadWipes + "-" + rivalWipes + "\n");
            hitList.append("Attacks: " + squadAttacks + "-" + rivalAttacks + "\n\n");
        }

        if (includeOps) {
            hitList.append("OUTPOSTS:\n");
            hitList.append("----------\n");
            for (WarRoomData_Outposts warBuffBase : getBuffBases()) {
                hitList.append(warBuffBase.getOutPostFriendlyName(context) + "\n");
            }
        }

        return hitList.toString();
    }

    private String getBases(boolean includeULS, boolean splitList) {
        StringBuilder hitList = new StringBuilder();
        hitList.append("\n");

        hitList.append("BASES (HQ - Strength");
        if (includeULS) {
            hitList.append(" - ULS");
        }
        hitList.append(")\n");
        hitList.append("-------------------\n");

        ArrayList<GuildMember> warParty = new ArrayList<>();
        warParty.clear();
        for (int i = 0; i < rivalGuildResponseData.getMembers().size(); i++) {
            GuildMember guildMember = new GuildMember(rivalGuildResponseData.getMembers().getJsonObject(i));
            for (Object o : getRivalSquadModel().getWarParticipants()) {
                if (o instanceof WarRoomData_WarParticipant) {
                    WarRoomData_WarParticipant warParticipant = (WarRoomData_WarParticipant) o;
                    if (warParticipant.getId().equalsIgnoreCase(guildMember.playerId)) {
                        warParty.add(guildMember);
                        break;
                    }
                }
            }
        }
        if (warParty.size() > 0) {
            hitList.append(sortAndFinaliseLIst(warParty, includeULS, splitList).toString());
        }
        return hitList.toString();
    }


    public String buildHitList(boolean includeTz, boolean includeOps, boolean includeScore, boolean includeULS, boolean splitList, String tzId) {
        StringBuilder hitList = new StringBuilder();
        hitList.append(hitListHeader(tzId, includeTz, includeOps, includeScore));
        hitList.append(getBases(includeULS, splitList));


        return hitList.toString();

    }

    private StringBuilder sortAndFinaliseLIst(ArrayList<GuildMember> warParty, boolean includeULS, boolean splitList) {
        StringBuilder hitList = new StringBuilder();
        StringBuilder clearedList = new StringBuilder();
        Collections.sort(warParty, new Comparator<GuildMember>() {
            @Override
            public int compare(GuildMember m1, GuildMember m2) {
                return m2.xp - m1.xp;
            }
        });

        for (GuildMember m : warParty) {
            //find the member in the rival party:
            int uls = 0;
            for (Object obj : getRivalSquadModel().getWarParticipants()) {
                if (obj instanceof WarRoomData_WarParticipant) {
                    WarRoomData_WarParticipant warParticipant = (WarRoomData_WarParticipant) obj;
                    if (warParticipant.getId().equalsIgnoreCase(m.playerId)) {
                        uls = warParticipant.getVictoryPoints();
                        break;
                    }
                }
            }

            String memberStr = "";
            if (includeULS) {
                memberStr = String.format(PRINTMEMBERWITHUL, m.memberName, m.hqLevel, m.xp, uls);
            } else {
                memberStr = String.format(PRINTMEMBER, m.memberName, m.hqLevel, m.xp);
            }
            if (splitList && uls == 0) {
                clearedList.append(String.format(PRINTMEMBERCLEARED, m.memberName, m.hqLevel, m.xp));
            } else if ((splitList && uls > 0) || !splitList) {
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


    public SWCGetWarStatusResultJson getSwcGetWarStatusResultData() {
        return swcGetWarStatusResultData;
    }

    public SWCGuildWarPartRespData getSwcGuildWarPartRespData() {
        return swcGuildWarPartRespData;
    }

    public WarRoomData_WarSquadModel getWarSquadModel() {
        return warSquadModel;
    }

    public WarRoomData_WarSquadModel getRivalSquadModel() {
        return rivalSquadModel;
    }

    public ArrayList<WarRoomData_Outposts> getMyOutposts() {
        return myOutposts;
    }

    public ArrayList<WarRoomData_Outposts> getNeutralOutposts() {
        return neutralOutposts;
    }

    public ArrayList<WarRoomData_Outposts> getEnemyOutposts() {
        return enemyOutposts;
    }

    public ArrayList<WarRoomData_Outposts> getBuffBases() {
        return buffBases;
    }

    public int getSquadAttacks() {
        return squadAttacks;
    }

    public int getRivalAttacks() {
        return rivalAttacks;
    }

    public int getSquadScore() {
        return squadScore;
    }

    public int getRivalScore() {
        return rivalScore;
    }
}

