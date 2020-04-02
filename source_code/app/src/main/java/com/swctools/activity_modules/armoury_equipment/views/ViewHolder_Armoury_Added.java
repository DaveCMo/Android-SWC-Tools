package com.swctools.activity_modules.armoury_equipment.views;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.swctools.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder_Armoury_Added extends RecyclerView.ViewHolder {

    public ImageView armouryImage;
    public TextView itemName;
    public TextView equipCap;
    public ImageView removeBtn;

    public ViewHolder_Armoury_Added(@NonNull View itemView) {
        super(itemView);
        armouryImage = itemView.findViewById(R.id.armouryImage);
        itemName = itemView.findViewById(R.id.itemName);
        equipCap = itemView.findViewById(R.id.equipCap);
        removeBtn = itemView.findViewById(R.id.removeBtn);

    }
}
