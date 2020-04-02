package com.swctools.swc_server_interactions.runnables;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.swctools.common.enums.ApplicationMessageTemplates;
import com.swctools.common.enums.BundleKeys;
import com.swctools.common.helpers.BundleHelper;
import com.swctools.swc_server_interactions.PlayerLoginSession;
import com.swctools.swc_server_interactions.SWCMessage;
import com.swctools.swc_server_interactions.results.SWCDefaultResponse;
import com.swctools.swc_server_interactions.results.SWCGetPublicGuildResponseData;
import com.swctools.swc_server_interactions.results.SWCVisitResult;
import com.swctools.swc_server_interactions.swc_commands.Cmd_GuildGetPublic;
import com.swctools.swc_server_interactions.swc_commands.Cmd_NeighborVisit;

import org.jetbrains.annotations.NotNull;

public abstract class SWC_Runnable_Base {
    public Handler swc_interaction_handler;
    protected static final String NO_INTERNET = "Hmmm. Connected to the internet, you are not!";

    public SWC_Runnable_Base(Handler swc_interaction_handler) {
        this.swc_interaction_handler = swc_interaction_handler;
    }

    public void sendProgressUpdateToUI(String progressMessage) {
        Message msg = Message.obtain();
        Bundle bundle = new Bundle();
        bundle.putString(BundleKeys.RUNNABLE_PROGRESS_MESSAGE.toString(), progressMessage);
        msg.setData(bundle);
        swc_interaction_handler.sendMessage(msg);
    }

    @NotNull
    public SWCGetPublicGuildResponseData getSwcGetPublicGuildResponseData(Context mContext, PlayerLoginSession loginSession, String guildId) throws Exception {
        SWCDefaultResponse getGuildPublicDataResult = new SWCMessage(
                loginSession.getAuthKey(),
                true,
                loginSession.getLoginTime(),
                new Cmd_GuildGetPublic(loginSession.newRequestId(), loginSession.getPlayerId(), guildId, loginSession.getLoginTime()),
                "|getPlayerGuildResponse",
                mContext
        ).getSwcMessageResponse();
        SWCGetPublicGuildResponseData swcGetPublicGuildResponseData = new SWCGetPublicGuildResponseData(getGuildPublicDataResult.getResponseDataByRequestId(loginSession.getRequestId()));
        if (swcGetPublicGuildResponseData.isSuccess()) {
            String key = String.format(ApplicationMessageTemplates.VISIT_KEY.getemplateString(), BundleKeys.GUILD_ID, guildId);
            BundleHelper bundleHelper = new BundleHelper(key, swcGetPublicGuildResponseData.resultData.toString());
            bundleHelper.commit(mContext);

        }

        return swcGetPublicGuildResponseData;
    }

    @NotNull
    public SWCVisitResult visitPlayer(String playerId, Context mContext, PlayerLoginSession warBotLoginSession) throws Exception {

        int visitRequest = warBotLoginSession.newRequestId();
        SWCDefaultResponse swcDefaultResponse = new SWCMessage(warBotLoginSession.getAuthKey(), true, warBotLoginSession.getLoginTime(),
                new Cmd_NeighborVisit(visitRequest, warBotLoginSession.getPlayerId(), playerId), "", mContext).getSwcMessageResponse();
        return new SWCVisitResult(swcDefaultResponse.getResponseDataByRequestId(visitRequest));
    }
}
