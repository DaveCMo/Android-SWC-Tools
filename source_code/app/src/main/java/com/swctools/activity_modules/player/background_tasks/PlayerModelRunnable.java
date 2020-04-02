package com.swctools.activity_modules.player.background_tasks;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.swctools.activity_modules.main.models.FavouriteLayoutListProvider;
import com.swctools.common.models.player_models.PlayerModel;
import com.swctools.layouts.models.FavouriteLayoutItem;

import java.util.List;

public class PlayerModelRunnable implements Runnable {
    private static final String TAG = "PlayerModelRunnable";
    private PlayerModel playerModel;
    private List<FavouriteLayoutItem> favouriteLayoutItemList;
    private boolean doPlayerModel;
    private boolean doBattleLog;
    private boolean doFavLayout;
    private String playerId;
    public Handler handler;
    private Context context;


    public PlayerModelRunnable(String playerId, boolean doPlayerModel, boolean doBattleLog, boolean doFavLayout, Handler handler, Context context) {
        this.playerId = playerId;
        this.doPlayerModel = doPlayerModel;
        this.doBattleLog = doBattleLog;
        this.doFavLayout = doFavLayout;
        this.handler = handler;
        this.context = context;
    }

    @Override
    public void run() {
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
        if (doFavLayout) {
            favouriteLayoutItemList = FavouriteLayoutListProvider.selectedFavs(0, context);
        }


        return playerModel;
    }


}
