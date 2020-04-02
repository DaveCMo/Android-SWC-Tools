package com.swctools.swc_server_interactions.threads;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.swctools.common.enums.BundleKeys;
import com.swctools.swc_server_interactions.runnables.ProcessActivateEquipment;
import com.swctools.swc_server_interactions.runnables.ProcessDeactivateEquipment;
import com.swctools.util.MethodResult;
import com.swctools.util.StringUtil;

import java.util.ArrayList;

public class SWC_Interaction_Thread extends Thread {
    private static final String TAG = "ExampleLooperThread";

    public Handler handler;
    public Looper looper;
    private Context mContext;

    public SWC_Interaction_Thread(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void run() {
        Looper.prepare();
        looper = Looper.myLooper();
        handler = new SWC_Interaction_Handler();

        Looper.loop();
    }
    public boolean waitforHandler() {

        while (handler == null) {

        }

        return true;
    }


    public class SWC_Interaction_Handler extends Handler {
        private static final String TAG = "ExampleHandler";

        public static final int GET_WAR_ROOM_DATA = 1;
        public static final int TASK_B = 2;
        public static final int DEACTIVATE_EQUIPMENT = 3;
        public static final int ACTIVATE_EQUIPMENT = 4;
        public static final int APPLY_LAYOUT = 5;
        public static final int VIEW_BASE = 6;
        private String playerId;


        @Override
        public void handleMessage(Message msg) {

        }
    }
}
