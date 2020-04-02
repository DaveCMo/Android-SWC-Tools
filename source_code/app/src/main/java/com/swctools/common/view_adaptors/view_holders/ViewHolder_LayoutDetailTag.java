package com.swctools.common.view_adaptors.view_holders;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.swctools.R;

public class ViewHolder_LayoutDetailTag extends RecyclerView.ViewHolder {
    public RecyclerView layoutTagRecycler;
    public ImageView imgTagEdit;

    public ViewHolder_LayoutDetailTag(@NonNull View itemView) {
        super(itemView);
        layoutTagRecycler = itemView.findViewById(R.id.layoutTagRecycler);
        imgTagEdit = itemView.findViewById(R.id.imgTagEdit);
    }
}
