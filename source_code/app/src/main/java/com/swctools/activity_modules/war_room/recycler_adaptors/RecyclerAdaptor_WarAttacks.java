package com.swctools.activity_modules.war_room.recycler_adaptors;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swctools.R;
import com.swctools.activity_modules.war_room.processing_models.WarRoomData_CurrentlyDefending;
import com.swctools.activity_modules.war_room.views.ViewHolder_WarAttack;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdaptor_WarAttacks extends RecyclerView.Adapter<ViewHolder_WarAttack> {
    private static final String TAG = "RecyclerAdaptor_WarAttacks";
    private ArrayList<WarRoomData_CurrentlyDefending> itemList;

    public RecyclerAdaptor_WarAttacks(ArrayList<WarRoomData_CurrentlyDefending> rowData) {
        this.itemList = rowData;
    }

    @NonNull
    @Override
    public ViewHolder_WarAttack onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_war_sc_detail, viewGroup, false);
        return new ViewHolder_WarAttack(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder_WarAttack vHolder, int i) {
        final WarRoomData_CurrentlyDefending rowData = itemList.get(i);



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
