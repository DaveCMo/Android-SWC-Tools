package com.swctools.swc_server_interactions;

import android.content.Context;

import com.swctools.common.enums.ApplicationMessageTemplates;
import com.swctools.common.enums.BundleKeys;
import com.swctools.common.helpers.BundleHelper;
import com.swctools.common.helpers.DateTimeHelper;
import com.swctools.common.helpers.ManifestDataHelper;
import com.swctools.swc_server_interactions.results.SWCDefaultResponse;
import com.swctools.swc_server_interactions.results.SWCGetPublicGuildResponseData;
import com.swctools.swc_server_interactions.swc_commands.Cmd_ConfigEndPoints;
import com.swctools.swc_server_interactions.swc_commands.Cmd_DeviceRegister;
import com.swctools.swc_server_interactions.swc_commands.Cmd_GuildGetPublic;
import com.swctools.swc_server_interactions.swc_commands.Cmd_PlayerContentGet;
import com.swctools.swc_server_interactions.swc_commands.SWC_Command;
import com.swctools.util.MethodResult;

import java.util.ArrayList;
import java.util.List;

public class SWC_Server_Common {

    public static MethodResult getGuildPublicData(PlayerLoginSession loginPlayerObject, String guildId, Context mContext) {

        if (loginPlayerObject.isLoggedIn()) {

            try {
                SWCDefaultResponse swcDefaultResponse = new SWCMessage(
                        loginPlayerObject.getAuthKey(),
                        true, loginPlayerObject.getLoginTime(),
                        new Cmd_GuildGetPublic(loginPlayerObject.newRequestId(), loginPlayerObject.getPlayerId(), guildId, loginPlayerObject.getLoginTime()),
                        "getGuildPublicData",
                        mContext).getSwcMessageResponse();
                SWCGetPublicGuildResponseData guildResponseData = new SWCGetPublicGuildResponseData(swcDefaultResponse.getResponseDataByRequestId(loginPlayerObject.getRequestId()));
                if (guildResponseData.isSuccess()) {
                    try {
                        String guildBundleKey = String.format(ApplicationMessageTemplates.VISIT_KEY.getemplateString(), BundleKeys.GUILDPUBLIC.toString(), guildId);
                        BundleHelper guildBundle = new BundleHelper(guildBundleKey, guildResponseData.resultData.toString());
                        guildBundle.commit(mContext);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    return new MethodResult(true, guildResponseData, guildResponseData.resultData.toString());
                } else {
                    return new MethodResult(false, guildResponseData.getStatusCodeAndName());
                }
            } catch (Exception e1) {
                e1.printStackTrace();
                return new MethodResult(false, e1.getMessage());
            }
        } else {
            return new MethodResult(false, "Player not logged in!");
        }

    }

    private static void requestConfigEndPoints(int requestId, String authKey, Context mContext) {
        try {
            List<SWC_Command> commands = new ArrayList<>();
            commands.add(new Cmd_ConfigEndPoints(requestId));
            SWCMessage swcMessage = new SWCMessage(authKey, true, 0, new Cmd_ConfigEndPoints(requestId), "requestConfigEndPoints", mContext);
            swcMessage.getSwcMessageResponse();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void ancillaryLoginCommands(PlayerLoginSession loginPlayerObject, Context mContext) {
        List<SWC_Command> commands = new ArrayList<>();
        int playerContentReqId = loginPlayerObject.newRequestId();
        commands.add(new Cmd_PlayerContentGet(playerContentReqId, loginPlayerObject.getPlayerId(), loginPlayerObject.getLoginTime()));
        SWCMessage ancillLoginMessage = new SWCMessage(loginPlayerObject.getAuthKey(), true, loginPlayerObject.getLoginTime(), commands, "ancillaryLoginCommands", mContext);
        try {

            SWCDefaultResponse contentResponse = ancillLoginMessage.getSwcMessageResponse();// new SWCDefaultResponse(playerContentResult.getMessage());
            ManifestDataHelper.processPlayerContent(contentResponse.getResponseDataByRequestId(playerContentReqId), mContext);

        } catch (Exception e) {
            e.printStackTrace();
        }

        commands.clear();
        int registerDeviceReqId = loginPlayerObject.newRequestId();
        commands.add(new Cmd_DeviceRegister(registerDeviceReqId, loginPlayerObject.getPlayerId(), DateTimeHelper.swc_requestTimeStamp()));
        ancillLoginMessage = new SWCMessage(loginPlayerObject.getAuthKey(), true, loginPlayerObject.getLoginTime(), commands, "ancillaryLoginCommands", mContext);

        try {
            ancillLoginMessage.getSwcMessageResponse();
        } catch (Exception e) {
            e.printStackTrace();
        }
        commands.clear();


    }


}
