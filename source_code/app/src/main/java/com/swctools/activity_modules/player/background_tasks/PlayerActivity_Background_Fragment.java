package com.swctools.activity_modules.player.background_tasks;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.swctools.activity_modules.player.PlayerModelAsyncCallBackReceiver;
import com.swctools.common.models.player_models.PlayerModel;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class PlayerActivity_Background_Fragment extends Fragment {
    private static final String TAG = "PlayerActivity_Backgrou";
    private PlayerModelAsyncCallBackReceiver activityCallBack;
    private Handler fragmentHandler;
//    private PlayerActivity_BackgroundThread looperThread;

    public static PlayerActivity_Background_Fragment getInstance(FragmentManager fragmentManager) {
        PlayerActivity_Background_Fragment fragment = (PlayerActivity_Background_Fragment) fragmentManager.findFragmentByTag(TAG);
        if (fragment == null) {

            fragment = new PlayerActivity_Background_Fragment();

            fragmentManager.beginTransaction().add(fragment, TAG).commit();
        }
        return fragment;

    }

    private PlayerActivity_Background_Fragment() {
        fragmentHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (activityCallBack != null) {
//                    activityCallBack.receivePlayerModel();;
                    if ((msg.obj instanceof PlayerModel)) {
                        activityCallBack.receivePlayerModel((PlayerModel) msg.obj);
                    }
                }
            }
        };


        setRetainInstance(true);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activityCallBack = (PlayerModelAsyncCallBackReceiver) context;
//        looperThread = new PlayerActivity_BackgroundThread();
//        looperThread.start();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activityCallBack = null;

        Log.d(TAG, "onDetach: ");
    }

    public void killThread() {

    }

    public void buildPlayerModel(String playerId, boolean doPlayerModel, boolean doBattleLog, boolean doFavLayout, Context context) {
     /*   if (looperThread == null) {

        }
        Message message = Message.obtain();
        message.obj = fragmentHandler;
        looperThread.waitforHandler();

        looperThread.handler.post(new PlayerModelRunnable(playerId, doPlayerModel, doBattleLog, doFavLayout, fragmentHandler, context));*/
        PlayerActivity_BackgroundThread looperThread  = new PlayerActivity_BackgroundThread(playerId, doPlayerModel, doBattleLog, doFavLayout, fragmentHandler, context);
        looperThread.start();
    }


}
