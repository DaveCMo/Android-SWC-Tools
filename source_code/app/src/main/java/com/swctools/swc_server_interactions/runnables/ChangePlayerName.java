package com.swctools.swc_server_interactions.runnables;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.swctools.activity_modules.main.models.PlayerDAO;
import com.swctools.swc_server_interactions.PlayerLoginSession;
import com.swctools.swc_server_interactions.SWCMessage;
import com.swctools.swc_server_interactions.results.SWCDefaultResponse;
import com.swctools.swc_server_interactions.results.SWCDefaultResultData;
import com.swctools.swc_server_interactions.swc_commands.Cmd_ChangePlayerName;
import com.swctools.util.MethodResult;
import com.swctools.util.Utils;

public class ChangePlayerName extends SWC_Runnable_Base implements Runnable {
    private static final String TAG = "ChangePlayerName";
    private String playerId;
    private String newName;

    private Context context;
    private PlayerLoginSession playerLoginSession;


    public ChangePlayerName(String playerId, String newName, Context context, Handler swc_interaction_handler) {
        super(swc_interaction_handler);
        this.playerId = playerId;
        this.newName = newName;
        this.context = context;
    }


    @Override
    public void run() {
        MethodResult methodResult = changeName();
        Message msg = Message.obtain();

        msg.obj = methodResult;
        swc_interaction_handler.sendMessage(msg);
    }

    private MethodResult changeName() {

        if (!Utils.isConnected(context)) {
            return new MethodResult(false, NO_INTERNET);
        }
        MethodResult playerValid = Utils.validatePlayer(playerId, newName);


        sendProgressUpdateToUI("Attempting login....");
        PlayerDAO playerDAO = new PlayerDAO(playerId, context);

        playerLoginSession = new PlayerLoginSession(playerId, playerDAO.getPlayerSecret(), playerDAO.getDeviceId(), true);
        playerLoginSession.login(true, context);

        if (!playerLoginSession.isLoggedIn()) {
            return playerLoginSession.getLoginResult();
        }


        try {
            SWCDefaultResponse swcDefaultResponse = new SWCMessage(
                    playerLoginSession.getAuthKey(),
                    true,
                    playerLoginSession.getLoginTime(),
                    new Cmd_ChangePlayerName(playerLoginSession.newRequestId(), playerId, newName, playerLoginSession.getLoginTime()),
                    "ChangePlayerName", context
            ).getSwcMessageResponse();

            SWCDefaultResultData swcDefaultResultData = new SWCDefaultResultData(swcDefaultResponse.getResponseDataByRequestId(playerLoginSession.getRequestId()));
            if (!swcDefaultResultData.isSuccess()) {
                return new MethodResult(false, "Server returned: " + swcDefaultResultData.getStatusCodeAndName());
            }
            return new MethodResult(true, "Name changed!!");
        } catch (Exception e) {

            e.printStackTrace();
            return new MethodResult(false, e);
        }


    }
}
