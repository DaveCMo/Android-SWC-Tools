package com.swctools.common.view_adaptors.delegated_adaptors;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swctools.R;
import com.swctools.activity_modules.player.models.TwoTextItemData;
import com.swctools.common.view_adaptors.view_holders.ViewHolder_TwoText;

import java.util.ArrayList;

public class AdaptorDelegate_TwoTextItem {
    private int viewType;
    private Context mContext;


    public AdaptorDelegate_TwoTextItem(int viewType, Context context) {
        this.viewType = viewType;
        this.mContext = context;

    }


    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_2_text_group_item, parent, false);
        return new ViewHolder_TwoText(itemView);
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
        final ViewHolder_TwoText viewHolder = (ViewHolder_TwoText) tholder;
        viewHolder.list_item_group_3_left.setText(rowData.text1);
        viewHolder.list_item_group_3_right.setText(rowData.text2);


    }


}
