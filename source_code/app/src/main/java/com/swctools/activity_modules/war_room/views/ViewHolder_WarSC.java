package com.swctools.activity_modules.war_room.views;

import android.view.View;
import android.widget.TextView;

import com.swctools.R;

import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder_WarSC extends RecyclerView.ViewHolder {

    public TextView warScTroop, warScTroopQTY, warScTroopDonatedBy;

    public ViewHolder_WarSC(View itemView) {
        super(itemView);
        warScTroop = itemView.findViewById(R.id.warScTroop);
        warScTroopQTY = itemView.findViewById(R.id.warScTroopQTY);
        warScTroopDonatedBy = itemView.findViewById(R.id.warScTroopDonatedBy);
    }


}
