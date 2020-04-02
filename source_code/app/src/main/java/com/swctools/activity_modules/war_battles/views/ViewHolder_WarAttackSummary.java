package com.swctools.activity_modules.war_battles.views;

import android.view.View;
import android.widget.TextView;

import com.swctools.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder_WarAttackSummary extends RecyclerView.ViewHolder {

    public TextView summaryDeployableItem, summaryDeployableTotal, summaryMaxDeployedValue;

    public ViewHolder_WarAttackSummary(@NonNull View itemView) {
        super(itemView);
        summaryDeployableItem = itemView.findViewById(R.id.summaryDeployableItem);
        summaryDeployableTotal = itemView.findViewById(R.id.summaryDeployableTotal);
        summaryMaxDeployedValue = itemView.findViewById(R.id.summaryMaxDeployedValue);

    }
}
