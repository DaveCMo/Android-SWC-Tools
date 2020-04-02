package com.swctools.activity_modules.player.recycler_adaptors;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.swctools.R;
import com.swctools.common.models.player_models.PlayerTrap;

import java.util.ArrayList;

public class AdaptorDelegate_Trap {
    private int viewType;
    private Context mContext;
    private int COLOR_RED;
    private int COLOR_GREEN;

    public AdaptorDelegate_Trap(int viewType, Context context) {
        this.viewType = viewType;
        this.mContext = context;
        COLOR_RED = mContext.getResources().getColor(R.color.dark_red);
        COLOR_GREEN = mContext.getResources().getColor(R.color.green);
    }


    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_3_text_group_item, parent, false);
        return new TrapViewHolder(itemView);
    }


    public int getViewType() {
        return this.viewType;
    }

    public int getItemViewType() {
        return this.viewType;
    }

    public boolean isForViewType(ArrayList<Object> items, int position) {
        return items.get(position) instanceof PlayerTrap;
    }


    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder tholder, ArrayList<Object> itemList, int position) {
        final PlayerTrap playerTrap = (PlayerTrap) itemList.get(position);
        final TrapViewHolder viewHolder = (TrapViewHolder) tholder;
        viewHolder.list_item_group_3_1.setText(playerTrap.trapDescription());
        viewHolder.list_item_group_3_2.setText(playerTrap.trapLevelString());
        viewHolder.list_item_group_3_3.setText(playerTrap.armed());
        if(playerTrap.isArmed()){
            viewHolder.list_item_group_3_3.setTextColor(COLOR_GREEN);
        } else {
            viewHolder.list_item_group_3_3.setTextColor(COLOR_RED);
        }

    }

    class TrapViewHolder extends RecyclerView.ViewHolder {
        public TextView list_item_group_3_1, list_item_group_3_2, list_item_group_3_3;

        public TrapViewHolder(View itemView) {
            super(itemView);
            list_item_group_3_1 = itemView.findViewById(R.id.list_item_group_3_1);
            list_item_group_3_2 = itemView.findViewById(R.id.list_item_group_3_2);
            list_item_group_3_3 = itemView.findViewById(R.id.list_item_group_3_3);
        }

    }

}
