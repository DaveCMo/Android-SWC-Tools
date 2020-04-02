package com.swctools.swc_server_interactions.runnables;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.swctools.config.AppConfig;
import com.swctools.swc_server_interactions.PlayerLoginSession;
import com.swctools.swc_server_interactions.SWCMessage;
import com.swctools.swc_server_interactions.results.SWCDefaultResponse;
import com.swctools.swc_server_interactions.results.SWCGetPublicGuildResponseData;
import com.swctools.swc_server_interactions.swc_commands.Cmd_GuildGetPublic;
import com.swctools.util.MethodResult;
import com.swctools.util.Utils;

public class GetGuildPublic extends SWC_Runnable_Base implements Runnable {

    private String guildId;
    private Context context;


    public GetGuildPublic(String guildId, Context context, Handler swc_interaction_handler) {
        super(swc_interaction_handler);
        this.guildId = guildId;
        this.context = context;
    }

    @Override
    public void run() {
        MethodResult methodResult = getGuildPublicData();
        Message msg = Message.obtain();

        msg.obj = methodResult;
        swc_interaction_handler.sendMessage(msg);
    }

    private MethodResult getGuildPublicData() {
        sendProgressUpdateToUI("Getting Squad Data...");
        if (!Utils.isConnected(context)) {
            return new MethodResult(false, NO_INTERNET);
        }
        AppConfig appConfig = new AppConfig(context);
        PlayerLoginSession playerLoginSession = new PlayerLoginSession(appConfig.getBot2ID(), appConfig.getBot2Secret(), appConfig.getVisitorDeviceId(), false);
        playerLoginSession.login(true, context);
        if (!playerLoginSession.isLoggedIn()) {
            return playerLoginSession.getLoginResult();
        }
        try {

            SWCMessage guildGetPublicMsg = new SWCMessage(playerLoginSession.getAuthKey(), true, playerLoginSession.getLoginTime(), new Cmd_GuildGetPublic(playerLoginSession.newRequestId(), playerLoginSession.getPlayerId(), guildId, playerLoginSession.getLoginTime()), "getGuildPublicData", context);

            try {
                SWCDefaultResponse swcDefaultResponse = guildGetPublicMsg.getSwcMessageResponse();// new SWCDefaultResponse(sendMessageResult.getMessage());
                SWCGetPublicGuildResponseData disneyGetPublicGuildResponseData =
                        new SWCGetPublicGuildResponseData(swcDefaultResponse.getResponseDataByRequestId(playerLoginSession.getRequestId()));
                if (disneyGetPublicGuildResponseData.isSuccess()) {
                    return new MethodResult(true, disneyGetPublicGuildResponseData.resultData.toString());
                } else {
                    return new MethodResult(false, disneyGetPublicGuildResponseData.getStatusCodeAndName());
                }
            } catch (Exception e) {
                e.printStackTrace();
                return new MethodResult(false, "Application Error!", e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new MethodResult(false, "Application Error!", e.getMessage());
        }

    }

}
