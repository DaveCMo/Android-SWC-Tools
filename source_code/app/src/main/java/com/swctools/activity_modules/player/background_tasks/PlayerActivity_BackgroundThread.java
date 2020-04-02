package com.swctools.activity_modules.player.background_tasks;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import com.swctools.activity_modules.main.models.FavouriteLayoutListProvider;
import com.swctools.common.models.player_models.PlayerModel;

public class PlayerActivity_BackgroundThread extends Thread {
    private static final String TAG = "PlayerActivity_Backgrou";
    public static final int QUIT = 1;
    public Handler handler;
    public Looper looper;
    private boolean doPlayerModel;
    private boolean doBattleLog;
    private boolean doFavLayout;
    private String playerId;
    private Context context;

    public PlayerActivity_BackgroundThread(String playerId, boolean doPlayerModel, boolean doBattleLog, boolean doFavLayout, Handler handler, Context context) {
        this.playerId = playerId;
        this.doPlayerModel = doPlayerModel;
        this.doBattleLog = doBattleLog;
        this.doFavLayout = doFavLayout;
        this.handler = handler;
        this.context = context;
    }


    @Override
    public void run() {
//        Looper.prepare();
//        looper = Looper.myLooper();
//        handler = new MyHandler();
//
//        Looper.loop();


        Message msg = Message.obtain();

        msg.obj = getPlayerModel();
        handler.sendMessage(msg);


    }


    private PlayerModel getPlayerModel() {
        PlayerModel playerModel = null;
        if (doPlayerModel) {
            playerModel = new PlayerModel(playerId, context);
            playerModel.buildModel();
            playerModel.buildBattleLog();
        }
      /*  if (doFavLayout) {
            favouriteLayoutItemList = FavouriteLayoutListProvider.selectedFavs(0, context);
        }*/


        return playerModel;
    }

    class MyHandler extends android.os.Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==QUIT){

            }
        }
    }
    public boolean waitforHandler() {

        while (handler == null) {

        }

        return true;
    }

}
