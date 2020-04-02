package com.swctools.activity_modules.layout_manager.background_tasks;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.swctools.activity_modules.layout_manager.models.LayoutManagerListProvider;

import java.util.ArrayList;

public class GetAllLayouts extends Thread {

    private Handler handler;
    private Context context;


    public GetAllLayouts(Handler handler, Context context) {
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


        layoutRecyclerList.addAll(LayoutManagerListProvider.getAllLayouts(context));

        return layoutRecyclerList;
    }

}
