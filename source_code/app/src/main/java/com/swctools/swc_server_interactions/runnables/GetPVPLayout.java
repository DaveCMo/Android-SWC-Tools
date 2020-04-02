package com.swctools.swc_server_interactions.runnables;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.swctools.config.AppConfig;
import com.swctools.common.enums.ApplicationMessageTemplates;
import com.swctools.common.enums.BundleKeys;
import com.swctools.common.helpers.BundleHelper;
import com.swctools.common.models.player_models.MapBuildings;
import com.swctools.swc_server_interactions.PlayerLoginSession;
import com.swctools.swc_server_interactions.SWCMessage;
import com.swctools.swc_server_interactions.results.SWCDefaultResponse;
import com.swctools.swc_server_interactions.results.SWCVisitResult;
import com.swctools.swc_server_interactions.swc_commands.Cmd_NeighborVisit;
import com.swctools.util.MethodResult;
import com.swctools.util.Utils;

public class GetPVPLayout extends SWC_Runnable_Base implements Runnable {
    private String playerId;
    private Context context;
    private PlayerLoginSession botLoginSession;

    public GetPVPLayout(String playerId, Context context, Handler swc_interaction_handler) {
        super(swc_interaction_handler);
        this.playerId = playerId;
        this.context = context;
        this.swc_interaction_handler = swc_interaction_handler;
    }


    @Override
    public void run() {
        MethodResult methodResult = getPvpLayout();
        Message msg = Message.obtain();

        msg.obj = methodResult;
        swc_interaction_handler.sendMessage(msg);
    }

    private MethodResult getPvpLayout() {

        if (!Utils.isConnected(context)) {
            return new MethodResult(false, NO_INTERNET);
        } else {
            AppConfig appConfig = new AppConfig(context);

            botLoginSession = new PlayerLoginSession(appConfig.getVisitor_playerId(), appConfig.getVisitor_playersecret(), appConfig.getVisitorDeviceId(), false);
            botLoginSession.login(true, context);
            if (!botLoginSession.isLoggedIn()) {
                return botLoginSession.getLoginResult();
            }
            sendProgressUpdateToUI("Getting PVP Layout...");
            try {
                SWCDefaultResponse visitResponse = new SWCMessage(botLoginSession.getAuthKey(), true, botLoginSession.getLoginTime(), new Cmd_NeighborVisit(botLoginSession.newRequestId(), appConfig.getVisitor_playerId(), playerId), "visitNeighbour", context).getSwcMessageResponse(); // new SWCDefaultResponse(sendPlayerMessage(new SWCMessage(authKey, true, loginTime, new Cmd_NeighborVisit(visitRequestId, playerId, neighborId), "visitNeighbour", mContext), true, true).getMessage());
                SWCVisitResult disneyVisitResult = new SWCVisitResult(visitResponse.getResponseDataByRequestId(botLoginSession.getRequestId()));
                if (!disneyVisitResult.isSuccess()) {
                    return new MethodResult(false, disneyVisitResult.getStatusCodeAndName());
                }

                String mapBuildingStr;

                MapBuildings mapBuildings = new MapBuildings(disneyVisitResult.mplayerModel().map().getJsonArray("buildings"));
                mapBuildingStr = mapBuildings.asOutputJSON();

                //then save response to database and update squad data for player.
                String bundleKey = String.format(ApplicationMessageTemplates.VISIT_KEY.getemplateString(), BundleKeys.VISIT_RESPONSE.toString(), playerId);
                BundleHelper bundleHelper = new BundleHelper(bundleKey, disneyVisitResult.resultData.toString());
                bundleHelper.commit(context);
                return new MethodResult(true, mapBuildingStr);

            } catch (Exception e) {
                e.printStackTrace();
                return new MethodResult(false, e);
            }

        }


    }


}

