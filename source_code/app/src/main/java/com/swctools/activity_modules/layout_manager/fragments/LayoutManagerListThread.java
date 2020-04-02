package com.swctools.activity_modules.layout_manager.fragments;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class LayoutManagerListThread extends Thread {
    private static final String TAG = "LayoutManagerListThread";

    public Handler handler;
    public Looper looper;
    private Context mContext;


    public LayoutManagerListThread(Context mContext) {
        this.mContext = mContext;

    }


    @Override
    public void run() {
        Looper.prepare();
        handler = new Layout_List_Handler();

        looper = Looper.myLooper();

        Looper.loop();
    }
    public boolean waitforHandler() {

        while (handler == null) {

        }

        return true;
    }
    class Layout_List_Handler extends Handler {


        @Override
        public void handleMessage(Message msg) {

        }
    }



}
