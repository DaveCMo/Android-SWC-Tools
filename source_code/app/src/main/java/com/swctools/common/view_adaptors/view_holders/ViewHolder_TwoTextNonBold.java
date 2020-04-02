package com.swctools.common.view_adaptors.view_holders;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.swctools.R;

public class ViewHolder_TwoTextNonBold extends RecyclerView.ViewHolder {

    public TextView list_item_group_3_left, list_item_group_3_right;
    public ConstraintLayout twoTextLayoutContainer;

    public ViewHolder_TwoTextNonBold(View itemView) {
        super(itemView);
        list_item_group_3_left = itemView.findViewById(R.id.list_item_group_3_left);
        list_item_group_3_right = itemView.findViewById(R.id.list_item_group_3_right);
        twoTextLayoutContainer = itemView.findViewById(R.id.twoTextLayoutContainer);
    }


}
