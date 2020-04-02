package com.swctools.activity_modules.war_battles.views;

import android.view.View;
import android.widget.TextView;

import com.swctools.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder_WarAttackHeader extends RecyclerView.ViewHolder {
    public TextView attackedByName, attackedByDate, attackResult, withOps, againstOps;

    public ViewHolder_WarAttackHeader(@NonNull View itemView) {
        super(itemView);
        attackedByName = itemView.findViewById(R.id.attackedByName);
        attackedByDate = itemView.findViewById(R.id.attackedByDate);
        attackResult = itemView.findViewById(R.id.attackResult);
        withOps = itemView.findViewById(R.id.withOps);
        againstOps = itemView.findViewById(R.id.againstOps);

    }
}
