package com.swctools.common.view_adaptors.layout_managers;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;

public class LayoutList_LinearLayoutManager extends LinearLayoutManager {
    private boolean isScrollEnabled = true;

    public LayoutList_LinearLayoutManager(Context context) {
        super(context);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        return isScrollEnabled && super.canScrollVertically();
    }
}
