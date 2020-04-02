package com.swctools.activity_modules.layout_manager.background_tasks;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.swctools.activity_modules.layout_manager.models.LayoutManagerListProvider;

import java.util.ArrayList;

public class GetLayoutListRunnable extends Thread {
    private static final String TAG = "GetLayoutList";
    private int folder;
    private String search;
    private boolean doFolders;
    private boolean newList;
    private Handler handler;
    private Context context;

    public GetLayoutListRunnable(int folder, String search, boolean doFolders, boolean newList, Handler handler, Context context) {
        this.folder = folder;

        this.search = search;
        this.doFolders = doFolders;
        this.newList = newList;
        this.handler = handler;
        this.context = context;

    }

    @Override
    public void run() {

        ArrayList<Object> layoutRecyclerList = getList();
        Message msg = Message.obtain();
        msg.obj = layoutRecyclerList;
        handler.sendMessage(msg);

    }

    private ArrayList<Object> getList() {
        ArrayList<Object> layoutRecyclerList = new ArrayList<>();


        if (doFolders) {
            layoutRecyclerList.addAll(LayoutManagerListProvider.getAllLayoutFolders(folder, context));
        }
        if (search != null && search.length() > 0) {

            layoutRecyclerList.addAll(LayoutManagerListProvider.getLayoutSearchResults(search, context));
        } else {

            ArrayList<Object> objects = new ArrayList<>();
            objects.addAll(LayoutManagerListProvider.getLayoutLayoutList(folder, context));

            layoutRecyclerList.addAll(objects);

        }
        return layoutRecyclerList;
    }

    @Override
    public String toString() {
        return "GetLayoutList{" +
                "folder=" + folder +
                ", search='" + search + '\'' +
                ", doFolders=" + doFolders +
                ", newList=" + newList +
                ", handler=" + handler +
                ", context=" + context +
                '}';
    }
}

