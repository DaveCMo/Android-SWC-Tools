package com.swctools.common.view_adaptors.delegated_adaptors;

import android.view.ViewGroup;

import java.lang.reflect.Type;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class AdaptorDelegate {
    protected int viewType;

    public AdaptorDelegate(int viewType) {
        this.viewType = viewType;
    }

    public int getItemViewType() {
        return this.viewType;
    }

    public abstract RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent);

    public abstract boolean isForViewType(ArrayList<Object> itemList, int position);

    public abstract void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final ArrayList<Type> itemList, final int position);
}
