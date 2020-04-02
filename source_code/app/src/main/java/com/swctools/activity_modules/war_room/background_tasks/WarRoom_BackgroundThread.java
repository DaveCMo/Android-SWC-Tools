package com.swctools.activity_modules.war_room.background_tasks;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.swctools.activity_modules.war_room.models.War_RoomModel;

public class WarRoom_BackgroundThread extends Thread {
    private static final String TAG = "WarRoom_BackgroundThrea";
    public Handler handler;
    public Looper looper;
    private String warId;
    private Context context;

    public WarRoom_BackgroundThread(String warId, Handler handler, Context context) {
        this.warId = warId;
        this.handler = handler;
        this.context = context;
    }


    @Override
    public void run() {
 /*       Looper.prepare();
        looper = Looper.myLooper();
        handler = new MyHandler();

        Looper.loop();*/
        Message msg = Message.obtain();

        msg.obj = war_roomModel();
        handler.sendMessage(msg);


    }

    class MyHandler extends Handler {

    }

    private War_RoomModel war_roomModel() {
        War_RoomModel war_roomModel = new War_RoomModel(warId, context);
        return war_roomModel;

    }
}
