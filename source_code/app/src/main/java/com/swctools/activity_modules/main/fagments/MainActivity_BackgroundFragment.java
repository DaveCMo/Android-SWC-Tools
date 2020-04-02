package com.swctools.activity_modules.main.fagments;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.swctools.activity_modules.main.MainHomeInterface;
import com.swctools.activity_modules.main.PlayerListThread;
import com.swctools.activity_modules.main.models.PlayerListWithLayouts;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class MainActivity_BackgroundFragment extends Fragment {
    private static final String TAG = "MainActivityAsync";

    private MainHomeInterface mainHomeInterface;
    private Context mContext;
    //    private PlayerListThread playerListThread;
    private Handler fragmentHandler;


    public MainActivity_BackgroundFragment() {
        setRetainInstance(true);


        fragmentHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                Object o = msg.obj;
                if (o instanceof PlayerListWithLayouts) {
                    if (mainHomeInterface != null) {
                        mainHomeInterface.finishedBuildingPlayerList(((PlayerListWithLayouts) msg.obj).getPlayerDAOList(), ((PlayerListWithLayouts) msg.obj).getFavouriteLayoutItemList());
                    }
                }
            }
        };

    }

    public void buildPlayerList() {
//        if(playerListThread==null){
//            playerListThread = new PlayerListThread(mContext.getApplicationContext());
//            playerListThread.start();
//        }
        mainHomeInterface.startedBuildingPlayerList();
        Message message = Message.obtain();
        message.obj = fragmentHandler;

//        playerListThread.waitforHandler();
        PlayerListThread playerListThread = new PlayerListThread(fragmentHandler, getContext());
        playerListThread.start();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context.getApplicationContext();
        mainHomeInterface = (MainHomeInterface) context;


    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
        mainHomeInterface = null;


    }

    public static MainActivity_BackgroundFragment getInstance(FragmentManager fragmentManager) {
        MainActivity_BackgroundFragment asyncFragment = (MainActivity_BackgroundFragment) fragmentManager.findFragmentByTag(MainActivity_BackgroundFragment.TAG);

        if (asyncFragment == null) {
            asyncFragment = new MainActivity_BackgroundFragment();
            fragmentManager.beginTransaction().add(asyncFragment, TAG).commit();
        }
        return asyncFragment;
    }
}
