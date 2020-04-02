package com.swctools.activity_modules.war_battles.view_adaptors;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdaptor_WarBattle extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "RecyclerAdaptor_WarBatt";
    private static final int HEADER = 1;
    private static final int DIVIDER = 2;
    private static final int TROOP = 3;

    private AdaptorDelegate_WarAttackHeader adaptorDelegate_warAttackHeader;
    private AdaptorDelegate_WarTroopHeader adaptorDelegate_warTroopHeader;
    private AdaptorDelegate_WarTroop adaptorDelegate_warTroop;
    private ArrayList<Object> itemList;

    private Context context;

    public RecyclerAdaptor_WarBattle(ArrayList<Object> itemList, Context context) {
        this.adaptorDelegate_warAttackHeader = new AdaptorDelegate_WarAttackHeader(HEADER, context);
        this.adaptorDelegate_warTroopHeader = new AdaptorDelegate_WarTroopHeader(DIVIDER, context);
        this.adaptorDelegate_warTroop = new AdaptorDelegate_WarTroop(TROOP, context);
        this.itemList = itemList;
        this.context = context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (adaptorDelegate_warAttackHeader.getViewType() == viewType) {
            return adaptorDelegate_warAttackHeader.onCreateViewHolder(parent);
        } else if (adaptorDelegate_warTroopHeader.getViewType() == viewType) {
            return adaptorDelegate_warTroopHeader.onCreateViewHolder(parent);
        } else if (adaptorDelegate_warTroop.getViewType() == viewType) {
            return adaptorDelegate_warTroop.onCreateViewHolder(parent);
        }

        throw new IllegalArgumentException("No delegate found!");
    }

    @Override
    public int getItemViewType(int position) {
        if (adaptorDelegate_warAttackHeader.isForViewType(itemList, position)) {
            return adaptorDelegate_warAttackHeader.getViewType();
        } else if (adaptorDelegate_warTroopHeader.isForViewType(itemList, position)) {
            return adaptorDelegate_warTroopHeader.getViewType();
        } else if (adaptorDelegate_warTroop.isForViewType(itemList, position)) {
            return adaptorDelegate_warTroop.getViewType();
        }
        throw new IllegalArgumentException("No delegate found");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = holder.getItemViewType();
        if (adaptorDelegate_warAttackHeader.getViewType() == viewType) {
            adaptorDelegate_warAttackHeader.onBindViewHolder(holder, itemList, position);
        } else if (adaptorDelegate_warTroopHeader.getViewType() == viewType) {
            adaptorDelegate_warTroopHeader.onBindViewHolder(holder, itemList, position);
        } else if (adaptorDelegate_warTroop.getViewType() == viewType) {
            adaptorDelegate_warTroop.onBindViewHolder(holder, itemList, position);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
