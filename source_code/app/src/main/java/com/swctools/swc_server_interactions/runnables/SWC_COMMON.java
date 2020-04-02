package com.swctools.swc_server_interactions.runnables;

import android.content.ContentValues;
import android.content.Context;

import com.swctools.config.AppConfig;
import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;
import com.swctools.common.enums.ApplicationMessageTemplates;
import com.swctools.common.enums.BundleKeys;
import com.swctools.common.helpers.BundleHelper;
import com.swctools.common.models.player_models.GuildModel;
import com.swctools.swc_server_interactions.PlayerLoginSession;
import com.swctools.swc_server_interactions.SWCMessage;
import com.swctools.swc_server_interactions.results.SWCDefaultResponse;
import com.swctools.swc_server_interactions.results.SWCGetPublicGuildResponseData;
import com.swctools.swc_server_interactions.results.SWCVisitResult;
import com.swctools.swc_server_interactions.swc_commands.Cmd_GuildGetPublic;
import com.swctools.swc_server_interactions.swc_commands.Cmd_NeighborVisit;
import com.swctools.util.MethodResult;
import com.swctools.util.StringUtil;
import com.swctools.util.Utils;

public class SWC_COMMON {

    protected static final String NO_INTERNET = "Hmmm. Connected to the internet, you are not!";

    public static MethodResult visitNeighbour(String neighborId, boolean doUpdateGuild, Context context) {
//        PlayerServiceCallBackInterface

        if (!Utils.isConnected(context)) {
            return new MethodResult(false, NO_INTERNET);
        } else {
            AppConfig appConfig = new AppConfig(context);

            PlayerLoginSession botLoginSession = new PlayerLoginSession(appConfig.getVisitor_playerId(), appConfig.getVisitor_playersecret(), appConfig.getVisitorDeviceId(), false);
            botLoginSession.login(true, context);

            if (!botLoginSession.isLoggedIn()) {
                return botLoginSession.getLoginResult();
            }

            try {
                SWCDefaultResponse visitResponse = new SWCMessage(botLoginSession.getAuthKey(), true, botLoginSession.getLoginTime(), new Cmd_NeighborVisit(botLoginSession.newRequestId(), appConfig.getVisitor_playerId(), neighborId), "visitNeighbour", context).getSwcMessageResponse(); // new SWCDefaultResponse(sendPlayerMessage(new SWCMessage(authKey, true, loginTime, new Cmd_NeighborVisit(visitRequestId, playerId, neighborId), "visitNeighbour", mContext), true, true).getMessage());
                SWCVisitResult disneyVisitResult = new SWCVisitResult(visitResponse.getResponseDataByRequestId(botLoginSession.getRequestId()));
                if (!disneyVisitResult.isSuccess()) {
                    return new MethodResult(false, disneyVisitResult.getStatusCodeAndName());
                }
                //then save response to database and update squad data for player.
                String bundleKey = String.format(ApplicationMessageTemplates.VISIT_KEY.getemplateString(), BundleKeys.VISIT_RESPONSE.toString(), neighborId);
                BundleHelper bundleHelper = new BundleHelper(bundleKey, disneyVisitResult.resultData.toString());
                bundleHelper.commit(context);
                if (doUpdateGuild) {
                    updateGuild(disneyVisitResult, neighborId, context, botLoginSession);
                }
                return new MethodResult(true, disneyVisitResult.resultData.toString());
            } catch (Exception e) {
                e.printStackTrace();
                return new MethodResult(false, e.getMessage());

            }
        }
    }

    protected static void updateGuild(SWCVisitResult disneyVisitResult, String neighbourId, Context context, PlayerLoginSession botLoginSession) {
        try {
            //First thing - update the squad name. Just do it. Not checking if it is changed and needs to be changed. In the words of Shia la Beouf or whatever his name is - "JUST DO IT!"l
            GuildModel guildModel = new GuildModel(disneyVisitResult.mplayerModel().guildInfo().toString());
            String guildId = guildModel.getGuildId();

            try {
                ContentValues values = new ContentValues();
                values.put(DatabaseContracts.PlayersTable.PLAYERGUILD, guildModel.getGuildName());
                String whereClause = DatabaseContracts.PlayersTable.PLAYERID + " = ? ";
                String[] whereArgs = {neighbourId};
                DBSQLiteHelper.updateData(DatabaseContracts.PlayersTable.TABLE_NAME, values, whereClause, whereArgs, context);
            } catch (Exception e1) {
                e1.printStackTrace();
            } finally {

            }

            //Well we got this far so gonna assume is in a squad. Go get the squad details!!

            if (StringUtil.isStringNotNull(guildId)) {


                Cmd_GuildGetPublic cmd_guildGetPublic = new Cmd_GuildGetPublic(botLoginSession.newRequestId(), neighbourId, guildId, botLoginSession.getLoginTime());
                SWCMessage guildGetMessage = new SWCMessage(botLoginSession.getAuthKey(), true, botLoginSession.getLoginTime(), cmd_guildGetPublic, "updateGuild", context);

                SWCDefaultResponse swcDefaultResponse = guildGetMessage.getSwcMessageResponse();// new SWCDefaultResponse(messageResult.getMessage());
                if (swcDefaultResponse.mException == null) {
                    SWCGetPublicGuildResponseData disneyGetPublicGuildResponseData = null;
                    try {
                        disneyGetPublicGuildResponseData = new SWCGetPublicGuildResponseData(swcDefaultResponse.getResponseDataByRequestId(botLoginSession.getRequestId()));
                        String guildBundleKey = String.format(ApplicationMessageTemplates.VISIT_KEY.getemplateString(), BundleKeys.GUILDPUBLIC.toString(), guildModel.getGuildId());
                        BundleHelper guildBundle = new BundleHelper(guildBundleKey, disneyGetPublicGuildResponseData.resultData.toString());
                        guildBundle.commit(context);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }

        } catch (Exception e) {
        }

    }

}
