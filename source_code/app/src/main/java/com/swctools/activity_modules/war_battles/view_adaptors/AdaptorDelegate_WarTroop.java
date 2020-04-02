package com.swctools.activity_modules.war_battles.view_adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swctools.R;
import com.swctools.activity_modules.war_battles.models.War_TroopDeployed;
import com.swctools.activity_modules.war_battles.views.ViewHolder_WarAttackTroop;
import com.swctools.activity_modules.war_room.models.War_Battle_Deployed;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdaptorDelegate_WarTroop {
    private static final String TAG = "RecyclerAdaptor_WarTroo";
    private int viewType;
    private Context context;

    public AdaptorDelegate_WarTroop(int viewType, Context context) {
        this.viewType = viewType;
        this.context = context;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_war_battle_troops, parent, false);
        return new ViewHolder_WarAttackTroop(itemView);
    }

    public int getViewType() {
        return this.viewType;
    }

    public boolean isForViewType(ArrayList<Object> itemList, int position) {
        if (itemList.get(position) instanceof War_Battle_Deployed) {
            return true;
        } else {
            return false;
        }
    }

    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, ArrayList<Object> itemList, int position) {
        final ViewHolder_WarAttackTroop view = (ViewHolder_WarAttackTroop) viewHolder;
        final War_Battle_Deployed war_troopDeployed = (War_Battle_Deployed) itemList.get(position);
        view.troopCooked.setText(war_troopDeployed.getDeployable());
        view.troopCookedQty.setText(String.valueOf(war_troopDeployed.getDeployableQty()));
    }


}
