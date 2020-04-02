package com.swctools.activity_modules.layout_manager;

import java.util.ArrayList;

public interface LayoutManagerAsyncCallback {
    void publishData(ArrayList<Object> layoutRecyclerList, String method);
    void   layoutListBuildCancelled();
}
