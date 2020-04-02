package com.swctools.activity_modules.player.recycler_adaptors;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.swctools.R;
import com.swctools.activity_modules.player.models.StatItemData;

import java.util.ArrayList;

public class AdaptorDelegate_StatItem {
    private static final String TAG = "AdaptorDelegate_StatItem";
    private int viewType;
    private Context mContext;


    public AdaptorDelegate_StatItem(int viewType, Context context) {
        this.viewType = viewType;
        this.mContext = context;

    }


    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_battle_stats, parent, false);
        return new StatItemDataHolder(itemView);
    }


    public int getViewType() {
        return this.viewType;
    }

    public int getItemViewType() {
        return this.viewType;
    }

    public boolean isForViewType(ArrayList<Object> items, int position) {
        return items.get(position) instanceof StatItemData;
    }


    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder tholder, ArrayList<Object> itemList, int position) {
        final StatItemData rowData = (StatItemData) itemList.get(position);
        final StatItemDataHolder viewHolder = (StatItemDataHolder) tholder;
        viewHolder.stat.setText(rowData.text1);
        viewHolder.stat_label.setText(rowData.text2);
        viewHolder.stat.setTextColor(rowData.statColour);
    }

    class StatItemDataHolder extends RecyclerView.ViewHolder {
        public TextView stat, stat_label;

        public StatItemDataHolder(View itemView) {
            super(itemView);
            stat = itemView.findViewById(R.id.stat);
            stat_label = itemView.findViewById(R.id.stat_label);
        }

    }

}
