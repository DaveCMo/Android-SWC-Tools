package com.swctools.swc_server_interactions.runnables;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

import com.swctools.config.AppConfig;
import com.swctools.activity_modules.main.models.PlayerDAO;
import com.swctools.common.models.player_models.UpgradeItemModel;
import com.swctools.common.models.player_models.Inventory;
import com.swctools.swc_server_interactions.PlayerLoginSession;
import com.swctools.swc_server_interactions.SWCMessage;
import com.swctools.swc_server_interactions.results.SWCDefaultResponse;
import com.swctools.swc_server_interactions.results.SWCDefaultResultData;
import com.swctools.swc_server_interactions.results.SWCVisitResult;
import com.swctools.swc_server_interactions.swc_commands.Cmd_Deployable_Upgrade;
import com.swctools.swc_server_interactions.swc_commands.Cmd_NeighborVisit;
import com.swctools.util.MethodResult;
import com.swctools.util.Utils;

import java.util.ArrayList;

public class Upgrade_Troops extends SWC_Runnable_Base implements Runnable {
    private static final String TAG = "Upgrade_Troops";
    private String playerId;
    private Context context;
    private PlayerLoginSession botLoginSession;
    private String buildingId;


    public Upgrade_Troops(Handler swc_interaction_handler, String playerId, Context context, String buildingId) {
        super(swc_interaction_handler);
        this.playerId = playerId;
        this.context = context;
        this.buildingId = buildingId;
    }

    @Override
    public void run() {
        MethodResult methodResult = upgradeDeployable();
        Message msg = Message.obtain();

        msg.obj = methodResult;
        swc_interaction_handler.sendMessage(msg);

    }


    private MethodResult upgradeDeployable() {

        if (!Utils.isConnected(context)) {
            return new MethodResult(false, NO_INTERNET);
        } else {
            AppConfig appConfig = new AppConfig(context);
            botLoginSession = new PlayerLoginSession(appConfig.getVisitor_playerId(), appConfig.getVisitor_playersecret(), appConfig.getVisitorDeviceId(), false);
            botLoginSession.login(true, context);
            if (!botLoginSession.isLoggedIn()) {
                return botLoginSession.getLoginResult();
            }
            sendProgressUpdateToUI("Getting data...");
            try {
                SWCDefaultResponse visitResponse = new SWCMessage(botLoginSession.getAuthKey(), true, botLoginSession.getLoginTime(), new Cmd_NeighborVisit(botLoginSession.newRequestId(), appConfig.getVisitor_playerId(), playerId), "visitNeighbour", context).getSwcMessageResponse(); // new SWCDefaultResponse(sendPlayerMessage(new SWCMessage(authKey, true, loginTime, new Cmd_NeighborVisit(visitRequestId, playerId, neighborId), "visitNeighbour", mContext), true, true).getMessage());
                SWCVisitResult disneyVisitResult = new SWCVisitResult(visitResponse.getResponseDataByRequestId(botLoginSession.getRequestId()));
                if (!disneyVisitResult.isSuccess()) {
                    return new MethodResult(false, disneyVisitResult.getStatusCodeAndName());
                }

                PlayerDAO playerDAO = new PlayerDAO(playerId, context);

                PlayerLoginSession playerLoginSession = new PlayerLoginSession(playerId, playerDAO.getPlayerSecret(), playerDAO.getDeviceId(), true);
                playerLoginSession.login(true, context);
                if (!playerLoginSession.isLoggedIn()) {
                    return playerLoginSession.getLoginResult();
                }
                sendProgressUpdateToUI("Logged in...");

                Inventory inventory = new Inventory(disneyVisitResult.mplayerModel(), context);
                for (UpgradeItemModel upgradeItemModel : upgradeItemModels()) {
                    int upgradeStart = upgradeItemModel.getCurrentLevel() + 1;
                    for (int i = upgradeStart; i <= upgradeItemModel.getMaxLevel(); i++) {
                        sendProgressUpdateToUI("Upgrading " + upgradeItemModel.getUid() + " to level " + i + "...");
                        String deployableUid = upgradeItemModel.getUid().concat(String.valueOf(i));
                        SWCDefaultResponse swcDefaultResponse = new SWCMessage(playerLoginSession.getAuthKey(), true, playerLoginSession.getLoginTime(), new Cmd_Deployable_Upgrade(
                                playerLoginSession.newRequestId(),
                                deployableUid,
                                playerId,
                                buildingId
                        ), "upgradedeployable", context).getSwcMessageResponse();
                        SWCDefaultResultData swcDefaultResultData = new SWCDefaultResultData(swcDefaultResponse.getResponseDataByRequestId(playerLoginSession.getRequestId()));
                        if (!swcDefaultResultData.isSuccess()) {
                            break;
//                            if (swcDefaultResultData.status == 1114) {
//                            } else {
//                                return new MethodResult(false, swcDefaultResultData.getStatusCodeAndName());
//                            }
                        }
                        SystemClock.sleep(10100);
                        //visit again to get the refreshed resources....
                        visitResponse = new SWCMessage(botLoginSession.getAuthKey(), true, botLoginSession.getLoginTime(), new Cmd_NeighborVisit(botLoginSession.newRequestId(), appConfig.getVisitor_playerId(), playerId), "visitNeighbour", context).getSwcMessageResponse();
                        disneyVisitResult = new SWCVisitResult(visitResponse.getResponseDataByRequestId(botLoginSession.getRequestId()));
                        if (!disneyVisitResult.isSuccess()) {
                            return new MethodResult(false, disneyVisitResult.getStatusCodeAndName());
                        }
                        inventory = new Inventory(disneyVisitResult.mplayerModel(), context);

                    }

                }
                return new MethodResult(true, "Complete!");

            } catch (Exception e) {
                return new MethodResult(false, e.getMessage());

            }
        }

    }

    private ArrayList<UpgradeItemModel> upgradeItemModels() {
        ArrayList<UpgradeItemModel> upgradeItemModels = new ArrayList<>();


        return upgradeItemModels;
    }




}
