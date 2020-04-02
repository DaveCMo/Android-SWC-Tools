package com.swctools.activity_modules.layout_manager.background_tasks;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.swctools.layouts.LayoutHelper;
import com.swctools.layouts.models.LayoutRecord;
import com.swctools.activity_modules.layout_manager.models.LayoutManagerListProvider;
import com.swctools.util.StringUtil;

import java.util.ArrayList;

public class DeleteLayouts extends Thread {
    private static final String TAG = "DeleteLayouts";
    private Handler handler;
    private Context context;
    private ArrayList<Object> objects;
    private int folder;
    private String search;
    private boolean doFolders;

    public DeleteLayouts(int folder, String search, boolean doFolders, Handler handler, Context context, ArrayList<Object> objects) {
        this.handler = handler;
        this.context = context;
        this.objects = objects;
        this.folder = folder;
        this.search = search;
        this.doFolders = doFolders;

    }

    @Override
    public void run() {
        Message msg = Message.obtain();
        ArrayList<Object> objects = new ArrayList<>();
        objects.addAll(deleteAndReturnLayouts());
        msg.obj = objects;
        handler.sendMessage(msg);
    }


    private ArrayList<Object> deleteAndReturnLayouts() {
        ArrayList<Object> layoutList = new ArrayList<>();

        for (Object o : objects) {
            if (o instanceof LayoutRecord) {
                LayoutRecord layoutRecord = (LayoutRecord) o;
                if (layoutRecord.isSelected()) {
                    LayoutHelper.deleteLayoutRecord(layoutRecord.getLayoutId(), context);
                }
            }
        }

        //now get list
        if (StringUtil.isStringNotNull(search)) {
            if (search.length() > 0) {
                layoutList.addAll(LayoutManagerListProvider.getLayoutSearchResults(search, context));
                return layoutList;
            }
        }
        if(doFolders){
            layoutList.addAll(LayoutManagerListProvider.getAllLayoutFolders(folder, context));
            layoutList.addAll(LayoutManagerListProvider.getLayoutLayoutList(folder, context));
            return layoutList;
        } else {
            layoutList.addAll(LayoutManagerListProvider.getAllLayouts(context));
            return layoutList;
        }


    }

}
