package com.swctools.swc_server_interactions.runnables;

import android.content.Context;

import com.swctools.activity_modules.main.models.PlayerDAO;
import com.swctools.swc_server_interactions.PlayerLoginSession;
import com.swctools.swc_server_interactions.SWCMessage;
import com.swctools.swc_server_interactions.results.SWCDefaultResponse;
import com.swctools.swc_server_interactions.results.SWCDefaultResultData;
import com.swctools.swc_server_interactions.swc_commands.Cmd_Equipment_Activate;
import com.swctools.swc_server_interactions.swc_commands.SWC_Command;
import com.swctools.util.MethodResult;

import java.util.ArrayList;

public class ProcessActivateEquipment {
    private static final String TAG = "ProcessActivateEquipmen";
    public static MethodResult activateEquipment(String playerId, ArrayList<String> equipmentIds, Context context) {

        PlayerDAO playerDAO = new PlayerDAO(playerId, context);
        PlayerLoginSession playerLoginSession = new PlayerLoginSession(playerId, playerDAO.getPlayerSecret(), playerDAO.getDeviceId(), true);
        playerLoginSession.login(true, context);
        if (playerLoginSession.isLoggedIn()) {

            try {
                ArrayList<SWC_Command> cmds = new ArrayList<>();
                for (String equip : equipmentIds) {
                    cmds.add(new Cmd_Equipment_Activate(playerLoginSession.newRequestId(), playerId, equip));
                }


                SWCDefaultResponse swcDefaultResponse = new SWCMessage(
                        playerLoginSession.getAuthKey(),
                        true,
                        playerLoginSession.getLoginTime(),
                        cmds,
                        TAG + "|Cmd_Equipment_Activate",
                        context
                ).getSwcMessageResponse();

                SWCDefaultResultData swcDefaultResultData = new SWCDefaultResultData(swcDefaultResponse.getResponseDataByRequestId(playerLoginSession.getRequestId()));
                if (swcDefaultResultData.isSuccess()) {
                    return new MethodResult(true, "Done!");
                } else {
                    return new MethodResult(false, swcDefaultResultData.getStatusCodeAndName());
                }
            } catch (Exception e) {
                return new MethodResult(false, e);
            }
        } else {
            return playerLoginSession.getLoginResult();
        }
    }


}
