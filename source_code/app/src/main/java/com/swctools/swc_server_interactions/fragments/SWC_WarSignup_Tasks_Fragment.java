package com.swctools.swc_server_interactions.fragments;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.swctools.common.enums.BundleKeys;
import com.swctools.swc_server_interactions.threads.SWC_Interaction_Thread;
import com.swctools.swc_server_interactions.runnables.GetGuildPublic;
import com.swctools.swc_server_interactions.runnables.SearchSquads;
import com.swctools.util.MethodResult;
import com.swctools.activity_modules.war_sign_up.interfaces.WarSignupInterface;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class SWC_WarSignup_Tasks_Fragment extends Fragment {
    private WarSignupInterface activityInterface;
    private SWC_Interaction_Thread looperThread;
    private Context context;
    private static final String TAG = "SWC_WarSignup_Tasks_Fra";
    private Handler fragmentHandler;

    private int OPTION = -1;
    private static final int SEARCH = 1;
    private static final int GETGUILD = 2;


    public static SWC_WarSignup_Tasks_Fragment getInstance(FragmentManager fragmentManager) {
        SWC_WarSignup_Tasks_Fragment fragment = (SWC_WarSignup_Tasks_Fragment) fragmentManager.findFragmentByTag(TAG);
        if (fragment == null) {

            fragment = new SWC_WarSignup_Tasks_Fragment();

            fragmentManager.beginTransaction().add(fragment, TAG).commit();
        }
        return fragment;
    }

    private SWC_WarSignup_Tasks_Fragment() {


        fragmentHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (activityInterface != null) {
                    if (msg.obj != null) {
                        if (msg.obj instanceof MethodResult) {
                            switch (OPTION) {
                                case SEARCH:
                                    activityInterface.receiveSearchResults((MethodResult) msg.obj);
                                    break;
                                case GETGUILD:
                                    activityInterface.receiveGuildData((MethodResult) msg.obj);
                                    break;
                            }
                        }
                    } else if (msg.getData() != null) {
                        String s = msg.getData().getString(BundleKeys.RUNNABLE_PROGRESS_MESSAGE.toString());
                        activityInterface.publishProgress(s);
                    }

                }
            }
        };


        setRetainInstance(true);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context.getApplicationContext());
        this.context = context;

        looperThread = new SWC_Interaction_Thread(context);
        looperThread.start();
        activityInterface = (WarSignupInterface) context;

    }


    @Override
    public void onDetach() {
        super.onDetach();
        context = null;
        activityInterface = null;
        looperThread.looper.quit();

    }

    public void getGuild(String guildId, Context context) {

        this.OPTION = GETGUILD;
        Message msg = Message.obtain();
        msg.obj = fragmentHandler;
        looperThread.handler.post(new GetGuildPublic(guildId, context, fragmentHandler));

    }


    public void searchForSquad(String search, Context context) {

        this.OPTION = SEARCH;
        Message msg = Message.obtain();
        msg.obj = fragmentHandler;
        looperThread.handler.post(new SearchSquads(search, context, fragmentHandler));

    }


}
