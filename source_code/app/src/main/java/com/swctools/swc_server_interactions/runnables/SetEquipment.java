package com.swctools.swc_server_interactions.runnables;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.swctools.activity_modules.armoury_equipment.models.Armoury_Set_Item;
import com.swctools.activity_modules.main.models.PlayerDAO;
import com.swctools.swc_server_interactions.PlayerLoginSession;
import com.swctools.swc_server_interactions.SWCMessage;
import com.swctools.swc_server_interactions.results.SWCDefaultResponse;
import com.swctools.swc_server_interactions.results.SWCDefaultResultData;
import com.swctools.swc_server_interactions.swc_commands.Cmd_Equipment_Activate;
import com.swctools.swc_server_interactions.swc_commands.Cmd_Equipment_Deactivate;
import com.swctools.swc_server_interactions.swc_commands.SWC_Command;
import com.swctools.util.MethodResult;

import java.util.ArrayList;

public class SetEquipment extends SWC_Runnable_Base implements Runnable {
    private static final String TAG = "SetEquipment";
    private String playerId;
    private Context context;
    private ArrayList<Armoury_Set_Item> equipToDeactivate;
    private ArrayList<Armoury_Set_Item> equipToActivate;

    public SetEquipment(Handler swc_interaction_handler, String playerId, Context context, ArrayList<Armoury_Set_Item> equipToDeactivate, ArrayList<Armoury_Set_Item> equipToActivate) {
        super(swc_interaction_handler);
        this.playerId = playerId;
        this.context = context;
        this.equipToDeactivate = equipToDeactivate;
        this.equipToActivate = equipToActivate;
    }

    @Override
    public void run() {
        MethodResult methodResult = doWork();
        Message msg = Message.obtain();

        msg.obj = methodResult;
        swc_interaction_handler.sendMessage(msg);

    }


    private MethodResult doWork() {
        PlayerDAO playerDAO = new PlayerDAO(playerId, context);
        sendProgressUpdateToUI("Logging in...");

        PlayerLoginSession playerLoginSession = new PlayerLoginSession(playerDAO.getPlayerId(), playerDAO.getPlayerSecret(), playerDAO.getDeviceId(), true);
        playerLoginSession.login(true, context);

        if (!playerLoginSession.isLoggedIn()) {
            return playerLoginSession.getLoginResult();
        }
        sendProgressUpdateToUI("Deactivating current equipment...");
        try {
            //Deactivate equip:
            ArrayList<SWC_Command> deactivateCommands = new ArrayList<>();
            ArrayList<Integer> deactivateReqIds = new ArrayList<>();
            for (Armoury_Set_Item item : equipToDeactivate) {
                deactivateCommands.add(new Cmd_Equipment_Deactivate(playerLoginSession.newRequestId(), playerId, item.getGameNameNoLevel()));
                deactivateReqIds.add(playerLoginSession.getRequestId());
            }

            SWCDefaultResponse deactivateResponse = new SWCMessage(
                    playerLoginSession.getAuthKey(),
                    true,
                    playerLoginSession.getLoginTime(),
                    deactivateCommands,
                    "DEACTIVATE|EQUIP",
                    context
            ).getSwcMessageResponse();

            //check results:
            for (Integer i : deactivateReqIds) {
                SWCDefaultResultData swcDefaultResultData = new SWCDefaultResultData(deactivateResponse.getResponseDataByRequestId(i));
                if (!swcDefaultResultData.isSuccess()) {
                    return new MethodResult(false, "Attempted to deactivate current equipment, server returned: " + swcDefaultResultData.getStatusCodeAndName());
                }
            }
            sendProgressUpdateToUI("Activating new equipment...");
            //Activate equipment:
            ArrayList<SWC_Command> activateCommands = new ArrayList<>();
            ArrayList<Integer> activateReqIds = new ArrayList<>();
            for (Armoury_Set_Item item : equipToActivate) {
                activateCommands.add(new Cmd_Equipment_Activate(playerLoginSession.newRequestId(), playerId, item.getGameNameNoLevel()));
                activateReqIds.add(playerLoginSession.getRequestId());
            }


            SWCDefaultResponse activateResponse = new SWCMessage(
                    playerLoginSession.getAuthKey(),
                    true,
                    playerLoginSession.getLoginTime(),
                    activateCommands,
                    "ACTIVATE|EQUIP",
                    context
            ).getSwcMessageResponse();
            //check results:
            for (Integer i : activateReqIds) {
                SWCDefaultResultData swcDefaultResultData = new SWCDefaultResultData(activateResponse.getResponseDataByRequestId(i));
                if (!swcDefaultResultData.isSuccess()) {
                    return new MethodResult(false, "Attempted to activate current equipment, server returned: " + swcDefaultResultData.getStatusCodeAndName());
                }
            }


            return new MethodResult(true, "Equipment Set!");
        } catch (Exception e) {
            return new MethodResult(false, e.getLocalizedMessage());
        }


    }
}
