package com.swctools.activity_modules.war_battles.views;

import android.view.View;
import android.widget.TextView;

import com.swctools.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder_WarAttackTroopHeader extends RecyclerView.ViewHolder {
    public TextView troop_header;
    public ViewHolder_WarAttackTroopHeader(@NonNull View itemView) {
        super(itemView);
        troop_header = itemView.findViewById(R.id.troop_header);
    }
}
