package com.swctools.swc_server_interactions.fragments;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.swctools.base.PlayerServiceCallBackInterface;
import com.swctools.common.enums.BundleKeys;
import com.swctools.common.enums.PlayerBase;
import com.swctools.swc_server_interactions.runnables.AddPlayer;
import com.swctools.swc_server_interactions.runnables.ApplyPlayerLayout;
import com.swctools.swc_server_interactions.runnables.ChangePlayerName;
import com.swctools.swc_server_interactions.runnables.GetPVPLayout;
import com.swctools.swc_server_interactions.runnables.GetTournamentData;
import com.swctools.swc_server_interactions.runnables.GetWarLayout;
import com.swctools.swc_server_interactions.runnables.ProcessWarStatus;
import com.swctools.swc_server_interactions.runnables.RepairDroideka;
import com.swctools.swc_server_interactions.runnables.RequestPVPTroops;
import com.swctools.swc_server_interactions.runnables.RequestWarTroops;
import com.swctools.swc_server_interactions.runnables.Upgrade_AllTheThings;
import com.swctools.swc_server_interactions.runnables.ViewPlayer;
import com.swctools.swc_server_interactions.threads.SWC_Interaction_Thread;
import com.swctools.util.MethodResult;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class SWC_Server_Tasks_Fragment extends Fragment {
    private PlayerServiceCallBackInterface activityInterface;
    //    ProcessWarStatus processWarStatus;
    private SWC_Interaction_Thread looperThread;
    private Context context;
    private String command;
    private static final String TAG = "SWC_Server_Tasks_Fragme";
    private Handler fragmentHandler;

    public static SWC_Server_Tasks_Fragment getInstance(FragmentManager fragmentManager) {
        SWC_Server_Tasks_Fragment fragment = (SWC_Server_Tasks_Fragment) fragmentManager.findFragmentByTag(TAG);
        if (fragment == null) {

            fragment = new SWC_Server_Tasks_Fragment();

            fragmentManager.beginTransaction().add(fragment, TAG).commit();
        }
        return fragment;
    }

    private SWC_Server_Tasks_Fragment() {


        fragmentHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (activityInterface != null) {
                    if (msg.obj != null) {
                        if (msg.obj instanceof MethodResult) {
                            activityInterface.playerServiceResult(command, (MethodResult) msg.obj);
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


        activityInterface = (PlayerServiceCallBackInterface) context;
        Log.d(TAG, "onAttach: ----------> Started thread");
    }


    @Override
    public void onDetach() {
        super.onDetach();
        context = null;
        activityInterface = null;
        if (looperThread!=null) {
            looperThread.looper.quit();
            looperThread =null;
        }

    }


    private void startThread(){
        looperThread = new SWC_Interaction_Thread(context);
        looperThread.start();
    }

    public void visitPlayer(String playerId, Context context) {
        this.command = PlayerServiceCallBackInterface.VISITPVP;
        Message msg = Message.obtain();
        if(looperThread==null){
            looperThread = new SWC_Interaction_Thread(context);
            startThread();
        }
        looperThread.waitforHandler();
        msg.obj = fragmentHandler;
        looperThread.handler.post(new ViewPlayer(playerId, context, fragmentHandler));

    }


    public void getWarStatus(String playerId, Context context) {
        if(looperThread==null){
            startThread();
        }

        this.command = PlayerServiceCallBackInterface.GETWARSTATUS;
        Message msg = Message.obtain();
        msg.obj = fragmentHandler;
        looperThread.waitforHandler();
        looperThread.handler.post(new ProcessWarStatus(playerId, context, fragmentHandler));
    }

    public void addPlayerData(String playerId, String playerSecret, Context context) {
        if(looperThread==null){
            startThread();
        }

        this.command = PlayerServiceCallBackInterface.ADDPLAYER;
        Message msg = Message.obtain();
        msg.obj = fragmentHandler;
        looperThread.waitforHandler();
        looperThread.handler.post(new AddPlayer(playerId, playerSecret, context, fragmentHandler));
    }


    public void getConflictData(String playerId, Context context) {
        if(looperThread==null){
            startThread();
        }

        this.command = PlayerServiceCallBackInterface.GETCONFLICTRANKS;
        Message msg = Message.obtain();
        msg.obj = fragmentHandler;
        looperThread.waitforHandler();
        looperThread.handler.post(new GetTournamentData(playerId, context, fragmentHandler));
    }

    public void updatePlayerLayout(String playerId, int newLayoutId, int newLayoutVersion, PlayerBase playerBase, Context context) {
        if(looperThread==null){
            startThread();
        }

        this.command = PlayerServiceCallBackInterface.UPDATELAYOUT;
        Message msg = Message.obtain();
        msg.obj = fragmentHandler;
        looperThread.waitforHandler();
        looperThread.handler.post(new ApplyPlayerLayout(playerId, newLayoutId, newLayoutVersion, playerBase, context, fragmentHandler));
    }

    public void getPVPLayout(String playerId, Context context) {
        if(looperThread==null){
            startThread();
        }

        this.command = PlayerServiceCallBackInterface.PlayerBaseSelection.SAVEPVP;
        Message msg = Message.obtain();
        msg.obj = fragmentHandler;
        looperThread.waitforHandler();
        looperThread.handler.post(new GetPVPLayout(playerId, context, fragmentHandler));
    }


    public void getWARLayout(String playerId, Context context) {
        if(looperThread==null){
            startThread();
        }

        this.command = PlayerServiceCallBackInterface.PlayerBaseSelection.SAVEWAR;
        Message msg = Message.obtain();
        msg.obj = fragmentHandler;
        looperThread.waitforHandler();
        looperThread.handler.post(new GetWarLayout(playerId, context, fragmentHandler));
    }

    public void repairDroids(String playerId, String constructorId, String deployable, Context context) {
        if(looperThread==null){
            startThread();
        }

        this.command = PlayerServiceCallBackInterface.REPAIRDROID;
        Message msg = Message.obtain();
        msg.obj = fragmentHandler;
        looperThread.waitforHandler();
        looperThread.handler.post(new RepairDroideka(fragmentHandler, playerId, constructorId, deployable, context));
    }

    public void requestWarTroops(String playerId, String message, boolean crystal, Context context) {
        if(looperThread==null){
            startThread();
        }

        this.command = PlayerServiceCallBackInterface.REQUEST_WAR;
        Message msg = Message.obtain();
        msg.obj = fragmentHandler;
        looperThread.waitforHandler();
        looperThread.handler.post(new RequestWarTroops(fragmentHandler, playerId, message, crystal, context));
    }

    public void requestPVPTroops(String playerId, String message, boolean crystal, Context context) {
        if(looperThread==null){
            startThread();
        }

        this.command = PlayerServiceCallBackInterface.REQUEST_PVP;
        Message msg = Message.obtain();
        msg.obj = fragmentHandler;
        looperThread.waitforHandler();
        looperThread.handler.post(new RequestPVPTroops(fragmentHandler, playerId, message, crystal, context));
    }

    public void changeName(String playerId, String newName, Context context) {
        if(looperThread==null){
            startThread();
        }

        this.command = PlayerServiceCallBackInterface.CHANGENAME;
        Message msg = Message.obtain();
        msg.obj = fragmentHandler;
        looperThread.waitforHandler();
        looperThread.handler.post(new ChangePlayerName(playerId, newName, context, fragmentHandler));
    }

    public void doWork(String playerId, Context context) {
        if(looperThread==null){
            startThread();
        }

        this.command = PlayerServiceCallBackInterface.UPGRADEEQUIPMENT;
        Message msg = Message.obtain();
        msg.obj = fragmentHandler;
        looperThread.waitforHandler();
        looperThread.handler.post(new Upgrade_AllTheThings(fragmentHandler, playerId, context));
//        looperThread.handler.post(new Upgrade_Troops(fragmentHandler, playerId, context, buildingId));


//        Bundle bundle = new Bundle();
//        bundle.putString(BundleKeys.PLAYER_ID.toString(), "379c1754-464a-11e7-8e2a-06591c004ec5");
//        ArrayList<String> equipIds = new ArrayList<>();
//        equipIds.add("eqpEmpireRodian");
//        equipIds.add("eqpEmpireBurstTurretDamage");
//        equipIds.add("eqpEmpireBurstTurretHealth");
//        equipIds.add("eqpEmpireRocketTurretDamage");
//        equipIds.add("eqpEmpireRocketTurretHealth");
//
//
//       /* equipIds.add("eqpEmpirePentagonJumpTrooper");
//        equipIds.add("eqpEmpirePentagonTrooper");
//        equipIds.add("eqpEmpireCargoGreatDane");*/
//
//
//        bundle.putStringArrayList(BundleKeys.EQUIPMENT_IDS.toString(), equipIds);
//        Message msg = Message.obtain();
//
//        msg.what = SWC_Interaction_Thread.SWC_Interaction_Handler.ACTIVATE_EQUIPMENT;

//        looperThread.handler.sendMessage(msg);




 /*       Message msg2 = Message.obtain();
        msg2.what = ExampleLooperThread2.ExampleHandler2.GET_WAR_ROOM_DATA;
        looperThread2.handler.sendMessage(msg2);
*/

    }


}
