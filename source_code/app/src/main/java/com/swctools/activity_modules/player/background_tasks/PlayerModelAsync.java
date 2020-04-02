package com.swctools.activity_modules.player.background_tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.swctools.activity_modules.player.PlayerModelAsyncCallBackReceiver;
import com.swctools.common.enums.BundleKeys;
import com.swctools.common.models.player_models.PlayerModel;
import com.swctools.util.MethodResult;
import com.swctools.layouts.models.FavouriteLayoutItem;
import com.swctools.activity_modules.main.models.FavouriteLayoutListProvider;

import java.util.List;


public class PlayerModelAsync extends Fragment {
    private static final String TAG = "PlayerModel";
    private String playerId;
    private Context mContext;

    private boolean running;
    private PlayerModel playerModel;
    private PlayerModelTask playerModelTask;


    private PlayerModelAsyncCallBackReceiver activityCallBack;

    public static PlayerModelAsync getInstance(FragmentManager fragmentManager, String playerId) {
        PlayerModelAsync playerModelAsyncFragment = (PlayerModelAsync) fragmentManager.findFragmentByTag(PlayerModelAsync.TAG);
        if (playerModelAsyncFragment == null) {
            playerModelAsyncFragment = new PlayerModelAsync();
            Bundle args = new Bundle();
            args.putString(BundleKeys.PLAYER_ID.toString(), playerId);
            playerModelAsyncFragment.setArguments(args);
            fragmentManager.beginTransaction().add(playerModelAsyncFragment, TAG).commit();
        }
        return playerModelAsyncFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstance) {
        super.onCreate(savedInstance);
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        activityCallBack = (PlayerModelAsyncCallBackReceiver) mContext;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activityCallBack = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void buildPlayerModel(String playerId, boolean doPlayerModel, boolean doBattleLog, boolean doFavLayout) {
        cancelbuildPlayerModel();
        this.playerId = playerId;
        playerModelTask = new PlayerModelTask();
        playerModelTask.execute(playerId, String.valueOf(doPlayerModel), String.valueOf(doBattleLog), String.valueOf(doFavLayout));
    }

    public void cancelbuildPlayerModel() {
        if (playerModelTask != null) {
            playerModelTask.cancel(true);
            playerModelTask = null;
        }
    }

    private class PlayerModelTask extends AsyncTask<String, String, MethodResult> {
        PlayerModel playerModel;
        List<FavouriteLayoutItem> favouriteLayoutItemList;
        boolean doPlayerModel;
        boolean doBattleLog;
        boolean doFavLayout;
        protected void onPostExecute(MethodResult methodResult) {
            super.onPostExecute(methodResult);
//            try {
//                activityCallBack.receivePlayerModel(playerModel);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected MethodResult doInBackground(String... strings) {
            running = true;
//get what we are going to do...
            doPlayerModel = Boolean.parseBoolean(strings[1]);
            doBattleLog = Boolean.parseBoolean(strings[2]);
            doFavLayout = Boolean.parseBoolean(strings[3]);

            if (doPlayerModel) {
                playerModel = new PlayerModel(strings[0], mContext);
                playerModel.buildModel();
                playerModel.buildBattleLog();
            }
            if (doFavLayout) {
                favouriteLayoutItemList = FavouriteLayoutListProvider.selectedFavs(0, mContext);
            }

            return new MethodResult(true, "");
        }
    }


}
