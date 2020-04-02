package com.swctools.activity_modules.main;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.swctools.activity_modules.main.models.FavouriteLayoutListProvider;
import com.swctools.activity_modules.main.models.PlayerDAOList;
import com.swctools.activity_modules.main.models.PlayerDAO_WithLayouts;
import com.swctools.activity_modules.main.models.PlayerListWithLayouts;
import com.swctools.layouts.models.FavouriteLayoutItem;

import java.util.List;

public class PlayerListThread extends Thread {
    private static final String TAG = "PlayerListThread";

    public Handler handler;
    private Context mContext;
    public Looper looper;
    private List<PlayerDAO_WithLayouts> playerDAOList;
    private List<FavouriteLayoutItem> favouriteLayoutItemList;

    public PlayerListThread(Handler handler, Context mContext) {
        this.handler = handler;
        this.mContext = mContext;
    }

    @Override
    public void run() {


        favouriteLayoutItemList = FavouriteLayoutListProvider.selectedFavs(5, mContext);
        playerDAOList = PlayerDAOList.getPlayerDAOListWithLayout(mContext);


        PlayerListWithLayouts playerListWithLayouts = new PlayerListWithLayouts(playerDAOList, favouriteLayoutItemList);
        Message message = Message.obtain();
        message.obj = playerListWithLayouts;
        handler.sendMessage(message);
    }



    class PlayerListHandler extends Handler {
        private static final String TAG = "PlayerListHandler";
        private List<PlayerDAO_WithLayouts> playerDAOList;
        private List<FavouriteLayoutItem> favouriteLayoutItemList;

        @Override
        public void handleMessage(Message msg) {
            Handler handler = (Handler) msg.obj;
            favouriteLayoutItemList = FavouriteLayoutListProvider.selectedFavs(5, mContext);
            playerDAOList = PlayerDAOList.getPlayerDAOListWithLayout(mContext);


            PlayerListWithLayouts playerListWithLayouts = new PlayerListWithLayouts(playerDAOList, favouriteLayoutItemList);
            Message message = Message.obtain();
            message.obj = playerListWithLayouts;
            handler.sendMessage(message);

        }
    }
}
