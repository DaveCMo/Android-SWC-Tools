package com.swctools.common.view_adaptors.view_holders;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.swctools.R;

public class ViewHolder_CheckItemImgTxt extends RecyclerView.ViewHolder {
    public CheckBox itemCheckBox;
    public ImageView itemImage;
    public TextView itemText;

    public ViewHolder_CheckItemImgTxt(@NonNull View itemView) {
        super(itemView);
        itemImage = itemView.findViewById(R.id.itemImage);
        itemCheckBox = itemView.findViewById(R.id.itemCheckBox);
        itemText = itemView.findViewById(R.id.itemText);

    }
}
