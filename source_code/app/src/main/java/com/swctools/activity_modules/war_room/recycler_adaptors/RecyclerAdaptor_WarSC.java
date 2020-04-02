package com.swctools.activity_modules.war_room.recycler_adaptors;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swctools.R;
import com.swctools.activity_modules.war_room.models.War_SC_Contents;
import com.swctools.common.models.player_models.TacticalCapItem;
import com.swctools.activity_modules.war_room.views.ViewHolder_WarSC;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdaptor_WarSC extends RecyclerView.Adapter<ViewHolder_WarSC> {
    private static final String TAG = "RecyclerAdaptor_WarSC";
    private ArrayList<War_SC_Contents> itemList;

    public RecyclerAdaptor_WarSC(ArrayList<War_SC_Contents> rowData) {
        this.itemList = rowData;
    }

    @NonNull
    @Override
    public ViewHolder_WarSC onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_war_sc_detail, viewGroup, false);
        return new ViewHolder_WarSC(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder_WarSC vHolder, int i) {
        final War_SC_Contents rowData = itemList.get(i);
        vHolder.warScTroop.setText(rowData.getUi_name() +" (Level "+ rowData.getUnit_level()+")");
        vHolder.warScTroopQTY.setText(" x" + rowData.getQty());

        vHolder.warScTroopDonatedBy.setText(rowData.getDonated_by_name());


    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
