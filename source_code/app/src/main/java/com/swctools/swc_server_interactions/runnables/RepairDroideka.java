package com.swctools.swc_server_interactions.runnables;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.swctools.activity_modules.main.models.PlayerDAO;
import com.swctools.swc_server_interactions.PlayerLoginSession;
import com.swctools.swc_server_interactions.SWCMessage;
import com.swctools.swc_server_interactions.results.SWCDefaultResponse;
import com.swctools.swc_server_interactions.results.SWCDefaultResultData;
import com.swctools.swc_server_interactions.swc_commands.Cmd_DeployableTrain;
import com.swctools.util.MethodResult;

public class RepairDroideka extends SWC_Runnable_Base implements Runnable {
    private static final String TAG = "TrainTroops";
    private String playerId;
    private String constructorId;
    private String deployable;
    private Context context;


    public RepairDroideka(Handler swc_interaction_handler, String playerId, String constructorId, String deployable, Context context) {
        super(swc_interaction_handler);
        this.playerId = playerId;
        this.constructorId = constructorId;
        this.deployable = deployable;
        this.context = context;
    }

    @Override
    public void run() {
        MethodResult methodResult = trainDeployable();
        Message msg = Message.obtain();

        msg.obj = methodResult;
        swc_interaction_handler.sendMessage(msg);

    }


    private MethodResult trainDeployable() {
        PlayerDAO playerDAO = new PlayerDAO(playerId, context);


        PlayerLoginSession playerLoginSession = new PlayerLoginSession(playerDAO.getPlayerId(), playerDAO.getPlayerSecret(), playerDAO.getDeviceId(), true);
        playerLoginSession.login(true, context);

        if (!playerLoginSession.isLoggedIn()) {
            return playerLoginSession.getLoginResult();
        }

        try {
            SWCDefaultResponse swcDefaultResponse = new SWCMessage(playerLoginSession.getAuthKey(), true, playerLoginSession.getLoginTime(),
                    new Cmd_DeployableTrain(playerLoginSession.newRequestId(), playerId, constructorId, deployable, 1),
                    "TrainTroops", context).getSwcMessageResponse();

            SWCDefaultResultData swcDefaultResultData = new SWCDefaultResultData(swcDefaultResponse.getResponseDataByRequestId(playerLoginSession.getRequestId()));

            if (!swcDefaultResultData.isSuccess()) {
                return new MethodResult(false, "SERVER RETURNED: " + swcDefaultResultData.getStatusCodeAndName());
            }

            return new MethodResult(true, "Repair started!");
        } catch (Exception e) {
            return new MethodResult(false, e.getLocalizedMessage());
        }


    }
}
