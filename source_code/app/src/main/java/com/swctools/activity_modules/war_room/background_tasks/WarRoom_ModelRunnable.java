package com.swctools.activity_modules.war_room.background_tasks;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.swctools.activity_modules.war_room.models.War_RoomModel;

public class WarRoom_ModelRunnable implements Runnable {
    private static final String TAG = "WarRoom_ModelRunnable";
    private String warId;
    public Handler handler;
    private Context context;


    public WarRoom_ModelRunnable(String warId, Handler handler, Context context) {
        this.warId = warId;
        this.handler = handler;
        this.context = context;
    }

    @Override
    public void run() {
        Message msg = Message.obtain();

        msg.obj = war_roomModel();
        handler.sendMessage(msg);
    }

    private War_RoomModel war_roomModel() {
        War_RoomModel war_roomModel = new War_RoomModel(warId, context);
        return war_roomModel;

    }


}
