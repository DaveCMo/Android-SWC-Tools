package com.swctools.swc_server_interactions.runnables;

import android.content.Context;

import com.swctools.activity_modules.main.models.PlayerDAO;
import com.swctools.swc_server_interactions.PlayerLoginSession;
import com.swctools.swc_server_interactions.SWCMessage;
import com.swctools.swc_server_interactions.results.SWCDefaultResponse;
import com.swctools.swc_server_interactions.results.SWCDefaultResultData;
import com.swctools.swc_server_interactions.swc_commands.Cmd_Equipment_Deactivate;
import com.swctools.swc_server_interactions.swc_commands.SWC_Command;
import com.swctools.util.MethodResult;

import java.util.ArrayList;

public class ProcessDeactivateEquipment {
    private static final String TAG = "ProcessDeactivateEquipm";

    public static MethodResult deactivateEquipment(String playerId, ArrayList<String> equipmentIds, Context context) {

        PlayerDAO playerDAO = new PlayerDAO(playerId, context);
        PlayerLoginSession playerLoginSession = new PlayerLoginSession(playerId, playerDAO.getPlayerSecret(), playerDAO.getDeviceId(), true);
        playerLoginSession.login(true, context);
        if (playerLoginSession.isLoggedIn()) {

            try {
                ArrayList<SWC_Command> cmd_equipment_deactivateList = new ArrayList<>();
                for (String equip : equipmentIds) {
                    cmd_equipment_deactivateList.add(new Cmd_Equipment_Deactivate(playerLoginSession.newRequestId(), playerId, equip));
                }


                SWCDefaultResponse swcDefaultResponse = new SWCMessage(
                        playerLoginSession.getAuthKey(),
                        true,
                        playerLoginSession.getLoginTime(),
                        cmd_equipment_deactivateList,
                        TAG + "|Cmd_Equipment_Deactivate",
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
