package com.swctools.activity_modules.war_battles.view_adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swctools.R;
import com.swctools.activity_modules.war_battles.views.ViewHolder_WarAttackTroopHeader;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdaptorDelegate_WarTroopHeader {
    private static final String TAG = "AdaptorDelegate_WarTroo";
    private int viewType;
    private Context context;

    public AdaptorDelegate_WarTroopHeader(int viewType, Context context) {
        this.viewType = viewType;
        this.context = context;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_war_troop_header, parent, false);
        return new ViewHolder_WarAttackTroopHeader(itemView);
    }

    public int getViewType() {
        return this.viewType;
    }

    public boolean isForViewType(ArrayList<Object> itemList, int position) {
        if (itemList.get(position) instanceof String) {
            return true;
        } else {
            return false;
        }
    }

    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, ArrayList<Object> itemList, int position) {
        final ViewHolder_WarAttackTroopHeader view = (ViewHolder_WarAttackTroopHeader) viewHolder;
        final String s = (String) itemList.get(position);
        view.troop_header.setText(s);
    }

}
