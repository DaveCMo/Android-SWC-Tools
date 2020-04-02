package com.swctools.activity_modules.war_room.background_tasks;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.swctools.activity_modules.war_room.WarRoomBackgroundInterface;
import com.swctools.activity_modules.war_room.models.War_RoomModel;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class WarRoom_Background_Fragment extends Fragment {
    private static final String TAG = "WarRoom_Background_Frag";
    private WarRoomBackgroundInterface activityCallBack;
    private Handler fragmentHandler;
//    private WarRoom_BackgroundThread looperThread;

    public static WarRoom_Background_Fragment getInstance(FragmentManager fragmentManager) {
        WarRoom_Background_Fragment fragment = (WarRoom_Background_Fragment) fragmentManager.findFragmentByTag(TAG);
        if (fragment == null) {

            fragment = new WarRoom_Background_Fragment();

            fragmentManager.beginTransaction().add(fragment, TAG).commit();
        }
        return fragment;

    }

    private WarRoom_Background_Fragment() {
        fragmentHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (activityCallBack != null) {
                    if (msg.obj instanceof War_RoomModel) {
                        War_RoomModel war_roomModel = (War_RoomModel) msg.obj;
                        activityCallBack.receiveWarRoomModel(war_roomModel);
                    }
                }
            }
        };


        setRetainInstance(true);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        looperThread = new WarRoom_BackgroundThread();
//        looperThread.start();
        activityCallBack = (WarRoomBackgroundInterface) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activityCallBack = null;

    }

    public void processWar(String warId, Context context) {

//        Message message = Message.obtain();
//        message.obj = fragmentHandler;
//        while (looperThread.handler == null) {
//            /*HACKY!*/
//        }

        WarRoom_BackgroundThread looperThread = new WarRoom_BackgroundThread(warId, fragmentHandler, context);
        looperThread.start();
    }


}
