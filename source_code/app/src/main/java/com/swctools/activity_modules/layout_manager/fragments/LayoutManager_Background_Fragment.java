package com.swctools.activity_modules.layout_manager.fragments;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.swctools.activity_modules.layout_manager.background_tasks.DeleteLayouts;
import com.swctools.activity_modules.layout_manager.background_tasks.GetAllLayouts;
import com.swctools.activity_modules.layout_manager.background_tasks.GetLayoutListRunnable;
import com.swctools.activity_modules.layout_manager.background_tasks.MoveLayoutsFolders;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class LayoutManager_Background_Fragment extends Fragment {
    private static final String TAG = "MainActivityAsync";

    private LayoutManager_LongTasks_Interface activityInterface;
    private Context mContext;
    private LayoutManagerListThread layoutManagerListThread;
    private Handler fragmentHandler;
    public static final int GET_LIST = 0;
    public static final int DELETE = 1;


    public LayoutManager_Background_Fragment() {
        setRetainInstance(true);


        fragmentHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                Object o = msg.obj;

                activityInterface.receiveNewList((ArrayList<Object>) o);
            }
        };

    }

    public void buildMainList(int parentFolderId, boolean doFolders, Context context) {

        /*Message message = Message.obtain();
        message.obj = fragmentHandler;*/

        GetLayoutListRunnable getLayoutListRunnable = new GetLayoutListRunnable(parentFolderId, "", doFolders, true, fragmentHandler, context);
        getLayoutListRunnable.start();
        /*layoutManagerListThread.handler.sendMessage(message);
        layoutManagerListThread.handler.post(new GetLayoutListRunnable(parentFolderId, "", doFolders, true, fragmentHandler, context));*/
    }

    public void searchLayouts(String search, Context context) {
//        layoutManagerListThread.handler.post(new GetLayoutListRunnable(0, search, false, false, fragmentHandler, context));


        GetLayoutListRunnable getLayoutListRunnable = new GetLayoutListRunnable(0, search, false, false, fragmentHandler, context);
        getLayoutListRunnable.start();

    }

    public void getAllLayoutsNoFolder(Context context) {
        GetAllLayouts getAllLayouts =new GetAllLayouts(fragmentHandler, context);
        getAllLayouts.start();
//        layoutManagerListThread.handler.post(new GetAllLayouts(fragmentHandler, context));
    }

    public void deleteLayouts(int parentFolderId, boolean doFolders, String search, ArrayList<Object> objects, Context context) {
//        layoutManagerListThread.handler.post(new DeleteLayouts(parentFolderId, search, doFolders, fragmentHandler, context, objects));
        DeleteLayouts deleteLayouts =new DeleteLayouts(parentFolderId, search, doFolders, fragmentHandler, context, objects);
        deleteLayouts.start();
    }

    public void moveLayouts(int parentFolderId, int newFolder, boolean doFolders, String search, ArrayList<Object> objects, Context context) {
//        layoutManagerListThread.handler.post(new MoveLayoutsFolders(parentFolderId, newFolder, search, doFolders, fragmentHandler, context, objects));
        MoveLayoutsFolders moveLayoutsFolders= new MoveLayoutsFolders(parentFolderId, newFolder, search, doFolders, fragmentHandler, context, objects);
        moveLayoutsFolders.start();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    /*    layoutManagerListThread = new LayoutManagerListThread(context.getApplicationContext());
        layoutManagerListThread.start();*/
        mContext = context.getApplicationContext();
        activityInterface = (LayoutManager_LongTasks_Interface) context;

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
        activityInterface = null;
/*        if (layoutManagerListThread!=null) {
            layoutManagerListThread.looper.quit();
        }*/
    }

    public static LayoutManager_Background_Fragment getInstance(FragmentManager fragmentManager) {
        LayoutManager_Background_Fragment asyncFragment = (LayoutManager_Background_Fragment) fragmentManager.findFragmentByTag(LayoutManager_Background_Fragment.TAG);

        if (asyncFragment == null) {
            asyncFragment = new LayoutManager_Background_Fragment();
            fragmentManager.beginTransaction().add(asyncFragment, TAG).commit();
        }
        return asyncFragment;
    }

    public interface LayoutManager_LongTasks_Interface {
        void itemsDeleted(ArrayList<Object> objects);

        void receiveNewList(ArrayList<Object> objects);

    }

}
