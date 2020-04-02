package com.swctools.activity_modules.war_room.views;

import android.view.View;
import android.widget.TextView;

import com.swctools.R;

import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder_WarAttack extends RecyclerView.ViewHolder {

    public TextView warAttackPlayerName;
    public TextView heroesUsed;
    public TextView troopsUsed;
    public TextView airUsed;
    public TextView droidUsed;

    public ViewHolder_WarAttack(View itemView) {
        super(itemView);
        warAttackPlayerName = itemView.findViewById(R.id.warAttackPlayerName);
        heroesUsed = itemView.findViewById(R.id.heroesUsed);
        troopsUsed = itemView.findViewById(R.id.troopsUsed);
        airUsed = itemView.findViewById(R.id.airUsed);
        droidUsed = itemView.findViewById(R.id.droidUsed);
    }


}
