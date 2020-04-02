package com.swctools.swc_server_interactions.runnables;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.swctools.config.AppConfig;
import com.swctools.activity_modules.main.models.PlayerDAO;
import com.swctools.common.models.player_models.GuildMember;
import com.swctools.common.models.player_models.GuildModel;
import com.swctools.common.models.player_models.MapBuildings;
import com.swctools.swc_server_interactions.PlayerLoginSession;
import com.swctools.swc_server_interactions.SWCMessage;
import com.swctools.swc_server_interactions.threads.SWC_Interaction_Thread;
import com.swctools.swc_server_interactions.results.SWCDefaultResponse;
import com.swctools.swc_server_interactions.results.SWCGetPublicGuildResponseData;
import com.swctools.swc_server_interactions.results.SWCGuildWarPartRespDataForLayout;
import com.swctools.swc_server_interactions.results.SWCVisitResult;
import com.swctools.swc_server_interactions.swc_commands.Cmd_GetWarParticipant;
import com.swctools.util.MethodResult;
import com.swctools.util.StringUtil;
import com.swctools.util.Utils;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;

public class GetWarLayout extends SWC_Runnable_Base implements Runnable {

    private String playerId;
    private PlayerLoginSession botLoginSession;
    private PlayerLoginSession playerLoginSession;
    private Context context;

    public GetWarLayout(String playerId, Context context, Handler swc_interaction_handler) {
        super(swc_interaction_handler);
        this.playerId = playerId;
        this.context = context;
    }

    @Override
    public void run() {
        MethodResult methodResult = getWarParticipant();
        Message msg = Message.obtain();

        msg.what = SWC_Interaction_Thread.SWC_Interaction_Handler.GET_WAR_ROOM_DATA;
        msg.obj = methodResult;
        swc_interaction_handler.sendMessage(msg);
    }

    private MethodResult getWarParticipant() {

        if (!Utils.isConnected(context)) {
            return new MethodResult(false, NO_INTERNET);
        }
        sendProgressUpdateToUI("Fetching war layout data...");
        AppConfig appConfig = new AppConfig(context);

        botLoginSession = new PlayerLoginSession(appConfig.getVisitor_playerId(), appConfig.getVisitor_playersecret(), appConfig.getVisitorDeviceId(), false);
        botLoginSession.login(true, context);
        if (!botLoginSession.isLoggedIn()) {
            return botLoginSession.getLoginResult();
        }

        if (!isInWar(playerId, context, botLoginSession).success) {
            return new MethodResult(false, "Not in a war");
        }
        PlayerDAO playerDAO = new PlayerDAO(playerId, context);
        playerLoginSession = new PlayerLoginSession(playerId, playerDAO.getPlayerSecret(), playerDAO.getDeviceId(), true);
        playerLoginSession.login(true, context);
        if (!playerLoginSession.isLoggedIn()) {
            return playerLoginSession.getLoginResult();
        }

        try {
            SWCDefaultResponse swcMessageResponse = new SWCMessage(playerLoginSession.getAuthKey(), true, playerLoginSession.getLoginTime(), new Cmd_GetWarParticipant(playerLoginSession.newRequestId(), playerId, playerLoginSession.getLoginTime()), "ancillaryLoginCommands", context).getSwcMessageResponse();

            SWCGuildWarPartRespDataForLayout guildGetWarPartData = new SWCGuildWarPartRespDataForLayout(swcMessageResponse.getResponseDataByRequestId(playerLoginSession.getRequestId()));
            if (StringUtil.isStringNotNull(guildGetWarPartData.warBuildings().toString())) {
                MapBuildings mapBuildings = new MapBuildings(guildGetWarPartData.warBuildings());
                return new MethodResult(true, mapBuildings.asOutputJSON(), playerDAO.getPlayerFaction());
            } else {
                return new MethodResult(false, "Error parsing layout");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new MethodResult(false, e);
        }


    }


    public MethodResult isInWar(String playerId, Context context, PlayerLoginSession playerLoginSession) {
        //


        try {
            SWCVisitResult disneyVisitResult = visitPlayer(playerId, context, playerLoginSession);
            if (!disneyVisitResult.isSuccess()) {
                return new MethodResult(false, disneyVisitResult.getStatusCodeAndName());
            }

            String playerFaction = disneyVisitResult.mplayerModel().faction();
            GuildModel guildModel = new GuildModel(disneyVisitResult.mplayerModel().guildInfo().toString());
            String guildId = guildModel.getGuildId();
            if (!StringUtil.isStringNotNull(guildId)) {
                return new MethodResult(false, "Not in a squad!");
                //get squad data:
            }
            SWCGetPublicGuildResponseData guildResponseData = getSwcGetPublicGuildResponseData(context, playerLoginSession, guildId);
            JsonArray members = guildResponseData.getMembers();
            for (JsonValue jsonValue : members) {
                JsonObject obj = (JsonObject) jsonValue;
                GuildMember guildMember = new GuildMember(obj);

                if (guildMember.playerId.equalsIgnoreCase(playerId)) {
                    if (guildMember.warParty == 1) {
                        return new MethodResult(true, "");
                    } else {
                        return new MethodResult(false, "You don't appear to be in the war party. Are you in a war?!");
                    }
                }
            }
            return new MethodResult(false, "Unknown issue with finding player in squad");


        } catch (Exception e) {
            e.printStackTrace();
            return new MethodResult(false, e);
        }


    }


}
