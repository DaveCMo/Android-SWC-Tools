package com.swctools.swc_server_interactions.fragments;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.swctools.activity_modules.armoury_equipment.ArmouryEquip_BackgroundInterface;
import com.swctools.activity_modules.armoury_equipment.models.Armoury_Set_Item;
import com.swctools.base.PlayerServiceCallBackInterface;
import com.swctools.common.enums.BundleKeys;
import com.swctools.swc_server_interactions.runnables.SetEquipment;
import com.swctools.swc_server_interactions.runnables.Upgrade_AllTheThings;
import com.swctools.swc_server_interactions.runnables.ViewPlayer;
import com.swctools.swc_server_interactions.threads.SWC_Interaction_Thread;
import com.swctools.util.MethodResult;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class SWC_Server_Armoury_Equipment_Fragment extends Fragment {
    private ArmouryEquip_BackgroundInterface activityInterface;
    //    ProcessWarStatus processWarStatus;

    public static final String EQUIPMENT = "EQUIPMENT";
    public static final String VISIT = "VISIT";
    private SWC_Interaction_Thread looperThread;
    private Context context;
    private String command;
    private static final String TAG = "SWC_Server_Armoury_Equi";
    private Handler fragmentHandler;

    public static SWC_Server_Armoury_Equipment_Fragment getInstance(FragmentManager fragmentManager) {
        SWC_Server_Armoury_Equipment_Fragment fragment = (SWC_Server_Armoury_Equipment_Fragment) fragmentManager.findFragmentByTag(TAG);
        if (fragment == null) {

            fragment = new SWC_Server_Armoury_Equipment_Fragment();

            fragmentManager.beginTransaction().add(fragment, TAG).commit();
        }
        return fragment;
    }

    private SWC_Server_Armoury_Equipment_Fragment() {


        fragmentHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (activityInterface != null) {
                    if (msg.obj != null) {
                        if (msg.obj instanceof MethodResult) {
                            activityInterface.receiveResult(command, (MethodResult) msg.obj);
                        }
                    } else if (msg.getData() != null) {
                        String s = msg.getData().getString(BundleKeys.RUNNABLE_PROGRESS_MESSAGE.toString());
                        activityInterface.progressUpdate(s);
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
        activityInterface = (ArmouryEquip_BackgroundInterface) context;

    }


    @Override
    public void onDetach() {
        super.onDetach();
        context = null;
        activityInterface = null;
        if (looperThread!=null) {
            looperThread.looper.quit();
        }
    }

    public void setEquipment(String playerId, ArrayList<Armoury_Set_Item> equipToDeactivate, ArrayList<Armoury_Set_Item> equipToActivate, Context context) {
        this.command = EQUIPMENT;
        Message msg = Message.obtain();
        msg.obj = fragmentHandler;
        looperThread.handler.post(new SetEquipment(fragmentHandler, playerId, context, equipToDeactivate, equipToActivate));

    }

    public void visitPlayer(String playerId, Context context) {
        this.command = VISIT;
        Message msg = Message.obtain();
        msg.obj = fragmentHandler;
        looperThread.handler.post(new ViewPlayer(playerId, context, fragmentHandler));

    }


    public void doWork(String playerId, Context context) {

        this.command = PlayerServiceCallBackInterface.UPGRADEEQUIPMENT;
        Message msg = Message.obtain();
        msg.obj = fragmentHandler;
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
