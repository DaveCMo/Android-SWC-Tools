package com.swctools.activity_modules.layout_manager.views;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.swctools.R;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder_Layout_Selected extends RecyclerView.ViewHolder {
    public ImageView layoutImage, layoutCard_factionImage, layoutFavourite;
    public TextView
            layoutCard_LayoutName,
            layoutCard_Player,
            noVersionsTxt;

    public ConstraintLayout layoutCardButtonRow;
    public RecyclerView layoutTagRecycler;
    public ConstraintLayout layoutCard_Layout;


    public ViewHolder_Layout_Selected(View v) {
        super(v);
        layoutImage = (ImageView) v.findViewById(R.id.layoutFavImg);
        layoutCard_factionImage = (ImageView) v.findViewById(R.id.layoutCard_factionImage);
        layoutCard_LayoutName = (TextView) v.findViewById(R.id.layoutCard_LayoutName);
        layoutCard_Player = (TextView) v.findViewById(R.id.layoutCard_Player);
        noVersionsTxt = v.findViewById(R.id.noVersionsTxt);
        layoutTagRecycler = v.findViewById(R.id.layoutTagRecycler);
        layoutCardButtonRow = v.findViewById(R.id.layoutCardButtonRow);
//            layoutVersionsContainer = (ConstraintLayout) v.findViewById(R.id.layoutVersionsContainer);
        layoutFavourite = (ImageView) v.findViewById(R.id.layoutFavourite);
        layoutCard_Layout = v.findViewById(R.id.layoutCard_Layout);
    }

}
