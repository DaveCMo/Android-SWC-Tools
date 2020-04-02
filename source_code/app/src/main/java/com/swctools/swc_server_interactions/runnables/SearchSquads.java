package com.swctools.swc_server_interactions.runnables;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.swctools.config.AppConfig;
import com.swctools.swc_server_interactions.PlayerLoginSession;
import com.swctools.swc_server_interactions.SWCMessage;
import com.swctools.swc_server_interactions.results.SWCDefaultResponse;
import com.swctools.swc_server_interactions.results.SWCSearchGuildResultData;
import com.swctools.swc_server_interactions.swc_commands.Cmd_SearchGuildByName;
import com.swctools.util.MethodResult;
import com.swctools.util.Utils;

public class SearchSquads extends SWC_Runnable_Base implements Runnable {
    private static final String TAG = "SearchSquads";
    private String searchStr;
    private Context context;


    public SearchSquads(String searchStr, Context context, Handler swc_interaction_handler) {
        super(swc_interaction_handler);
        this.searchStr = searchStr;
        this.context = context;
    }


    @Override
    public void run() {
        MethodResult methodResult = searchSquads();
        Message msg = Message.obtain();

        msg.obj = methodResult;
        swc_interaction_handler.sendMessage(msg);
    }


    private MethodResult searchSquads() {
        sendProgressUpdateToUI("Searching for '" + searchStr + "'");
        if (!Utils.isConnected(context)) {
            return new MethodResult(false, NO_INTERNET);
        }
        AppConfig appConfig = new AppConfig(context);
        PlayerLoginSession botLoginSession = new PlayerLoginSession(appConfig.getBot2ID(), appConfig.getBot2Secret(), appConfig.getVisitorDeviceId(), false);
        botLoginSession.login(true, context);
        if (!botLoginSession.isLoggedIn()) {
            return botLoginSession.getLoginResult();
        }

        try {
            SWCDefaultResponse swcDefaultResponse = new SWCMessage(botLoginSession.getAuthKey(), true, botLoginSession.getLoginTime(), new Cmd_SearchGuildByName(botLoginSession.newRequestId(), botLoginSession.getPlayerId(), searchStr), "searchGuilds", context).getSwcMessageResponse();
            SWCSearchGuildResultData swcSearchGuildResultData = new SWCSearchGuildResultData(swcDefaultResponse.getResponseDataByRequestId(botLoginSession.getRequestId()));
            return new MethodResult(true, swcSearchGuildResultData.result.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return new MethodResult(false, "Application Error: ", e.getMessage());
        }

    }

}
