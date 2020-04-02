package com.swctools.swc_server_interactions.runnables;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.swctools.activity_modules.main.models.PlayerDAO;
import com.swctools.common.enums.ServerConstants;
import com.swctools.swc_server_interactions.PlayerLoginSession;
import com.swctools.swc_server_interactions.SWCMessage;
import com.swctools.swc_server_interactions.results.SWCDefaultResponse;
import com.swctools.swc_server_interactions.results.SWCDefaultResultData;
import com.swctools.swc_server_interactions.swc_commands.Cmd_GuildWarTroopsRequest;
import com.swctools.util.MethodResult;

public class RequestWarTroops extends SWC_Runnable_Base implements Runnable {
    private static final String TAG = "TrainTroops";
    private String playerId;
    private String message;
private boolean crystal;
    private Context context;

    public RequestWarTroops(Handler swc_interaction_handler, String playerId, String message, boolean crystal, Context context) {
        super(swc_interaction_handler);
        this.playerId = playerId;
        this.message = message;
        this.crystal = crystal;
        this.context = context;
    }

    @Override
    public void run() {
        MethodResult methodResult = processRequest();
//        MethodResult methodResult = new MethodResult(false, "2320");
        Message msg = Message.obtain();

        msg.obj = methodResult;
        swc_interaction_handler.sendMessage(msg);

    }


    private MethodResult processRequest() {
        PlayerDAO playerDAO = new PlayerDAO(playerId, context);
        PlayerLoginSession playerLoginSession = new PlayerLoginSession(playerDAO.getPlayerId(), playerDAO.getPlayerSecret(), playerDAO.getDeviceId(), true);
        playerLoginSession.login(true, context);

        if (!playerLoginSession.isLoggedIn()) {
            return playerLoginSession.getLoginResult();
        }

        try {
            SWCDefaultResponse swcDefaultResponse = new SWCMessage(playerLoginSession.getAuthKey(), true, playerLoginSession.getLoginTime(), new Cmd_GuildWarTroopsRequest(playerLoginSession.newRequestId(), playerId, crystal, message), "Request War Troops", context).getSwcMessageResponse();
            SWCDefaultResultData swcDefaultResultData = new SWCDefaultResultData(swcDefaultResponse.getResponseDataByRequestId(playerLoginSession.getRequestId()));
            if (!swcDefaultResultData.isSuccess()) {
                if (swcDefaultResultData.status == ServerConstants.STATUS_CODE_TOO_SOON_TO_REQUEST_TROOPS_AGAIN.toInt()) {
                    Log.d(TAG, "processRequest--->: too soon!!");
                    return new MethodResult(false, String.valueOf(ServerConstants.STATUS_CODE_TOO_SOON_TO_REQUEST_TROOPS_AGAIN.toInt()));
                } else {
                    return new MethodResult(false, "Server returned: " + swcDefaultResultData.getStatusCodeAndName());
                }
            }

            return new MethodResult(true, "Troops requested!!");
        } catch (Exception e) {
            return new MethodResult(false, e.getLocalizedMessage());
        }


    }
}
