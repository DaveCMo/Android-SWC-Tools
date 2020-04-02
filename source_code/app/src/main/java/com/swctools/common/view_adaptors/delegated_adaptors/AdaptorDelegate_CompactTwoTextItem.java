package com.swctools.common.view_adaptors.delegated_adaptors;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.swctools.R;
import com.swctools.activity_modules.player.models.TwoTextItemData;

import java.util.ArrayList;

public class AdaptorDelegate_CompactTwoTextItem {
    private int viewType;
    private Context mContext;


    public AdaptorDelegate_CompactTwoTextItem(int viewType, Context context) {
        this.viewType = viewType;
        this.mContext = context;

    }


    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_2_text_group_item_compact, parent, false);
        return new TwoTextViewHolder(itemView);
    }


    public int getViewType() {
        return this.viewType;
    }

    public int getItemViewType() {
        return this.viewType;
    }

    public boolean isForViewType(ArrayList<Object> items, int position) {
        return items.get(position) instanceof TwoTextItemData;
    }


    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder tholder, ArrayList<Object> itemList, int position) {
        final TwoTextItemData rowData = (TwoTextItemData) itemList.get(position);
        final TwoTextViewHolder viewHolder = (TwoTextViewHolder) tholder;
        viewHolder.list_item_group_3_left.setText(rowData.text1);
        viewHolder.list_item_group_3_right.setText(rowData.text2);


    }

    class TwoTextViewHolder extends RecyclerView.ViewHolder {
        public TextView list_item_group_3_left, list_item_group_3_right;

        public TwoTextViewHolder(View itemView) {
            super(itemView);
            list_item_group_3_left = itemView.findViewById(R.id.compactlist_item_group_3_left);
            list_item_group_3_right = itemView.findViewById(R.id.compactlist_item_group_3_right);
        }

    }

}
