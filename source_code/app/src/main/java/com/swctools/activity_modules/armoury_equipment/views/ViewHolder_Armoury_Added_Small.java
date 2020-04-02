package com.swctools.activity_modules.armoury_equipment.views;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.swctools.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder_Armoury_Added_Small extends RecyclerView.ViewHolder {

    public ImageView armouryImage;
    public TextView itemName;

    public ViewHolder_Armoury_Added_Small(@NonNull View itemView) {
        super(itemView);
        armouryImage = itemView.findViewById(R.id.armouryImage);
        itemName = itemView.findViewById(R.id.itemName);

    }
}
