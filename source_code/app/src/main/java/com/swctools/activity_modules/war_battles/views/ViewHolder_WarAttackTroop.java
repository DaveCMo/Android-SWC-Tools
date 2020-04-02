package com.swctools.activity_modules.war_battles.views;

import android.view.View;
import android.widget.TextView;

import com.swctools.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder_WarAttackTroop extends RecyclerView.ViewHolder {
    public TextView troopCooked;
    public TextView troopCookedQty;

    public ViewHolder_WarAttackTroop(@NonNull View itemView) {
        super(itemView);
        troopCooked = itemView.findViewById(R.id.troopCooked);
        troopCookedQty = itemView.findViewById(R.id.troopCookedQty);
    }
}
