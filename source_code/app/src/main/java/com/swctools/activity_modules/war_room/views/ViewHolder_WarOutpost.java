package com.swctools.activity_modules.war_room.views;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.swctools.R;

import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder_WarOutpost extends RecyclerView.ViewHolder {

    public TextView opLevel;
    public ImageView war_outpostImg;

    public ViewHolder_WarOutpost(View itemView) {
        super(itemView);
        opLevel = itemView.findViewById(R.id.opLevel);
        war_outpostImg = itemView.findViewById(R.id.war_outpostImg);


    }


}
