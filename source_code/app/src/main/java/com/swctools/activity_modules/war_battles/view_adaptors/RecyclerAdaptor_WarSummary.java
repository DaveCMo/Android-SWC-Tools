package com.swctools.activity_modules.war_battles.view_adaptors;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swctools.R;
import com.swctools.activity_modules.war_battles.models.WarSummaryItem;
import com.swctools.activity_modules.war_battles.views.ViewHolder_WarAttackSummary;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdaptor_WarSummary extends RecyclerView.Adapter<ViewHolder_WarAttackSummary> {
    private static final String TAG = "RecyclerAdaptor_WarSumm";
    private ArrayList<WarSummaryItem> warSummaryItems;


    public RecyclerAdaptor_WarSummary(ArrayList<WarSummaryItem> warSummaryItems) {
        this.warSummaryItems = warSummaryItems;
    }

    @NonNull
    @Override
    public ViewHolder_WarAttackSummary onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_war_summary, parent, false);
        return new ViewHolder_WarAttackSummary(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder_WarAttackSummary holder, int position) {
        holder.summaryDeployableItem.setText(warSummaryItems.get(position).getItem());
        holder.summaryDeployableTotal.setText(String.valueOf(warSummaryItems.get(position).getTotal()));
        holder.summaryMaxDeployedValue.setText(String.valueOf(warSummaryItems.get(position).getMax()));

    }

    @Override
    public int getItemCount() {
        return warSummaryItems.size();
    }
}
