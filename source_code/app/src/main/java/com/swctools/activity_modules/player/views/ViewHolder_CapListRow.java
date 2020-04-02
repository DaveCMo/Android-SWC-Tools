package com.swctools.activity_modules.player.views;

import android.view.View;
import android.widget.TextView;

import com.swctools.R;

import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder_CapListRow extends RecyclerView.ViewHolder {
    public TextView list_item_group_3_left, list_item_group_3_right;


    public ViewHolder_CapListRow(View itemView) {
        super(itemView);
        list_item_group_3_left = (TextView) itemView.findViewById(R.id.list_item_group_3_left);
        list_item_group_3_right = (TextView) itemView.findViewById(R.id.list_item_group_3_right);


    }
}
