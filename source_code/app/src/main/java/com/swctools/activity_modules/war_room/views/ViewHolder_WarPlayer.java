package com.swctools.activity_modules.war_room.views;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.swctools.R;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRatingBar;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder_WarPlayer extends RecyclerView.ViewHolder {

    public TextView txtAttacksLeft, sullustSCText, warPlayerName, hqBaseScore, attackedBy;
    public AppCompatRatingBar plystatus_links;
    public ImageView warPlayerFactionImg;
    public ProgressBar sullustSCFullBar;
    public ConstraintLayout warPlayerBG;
    public Button viewSCBtn;
    public Button requestButton;
//    public Toolbar myPlayerWarToolBar;

    public ViewHolder_WarPlayer(@NonNull View itemView) {
        super(itemView);
        warPlayerName = itemView.findViewById(R.id.warPlayerName);
        txtAttacksLeft = itemView.findViewById(R.id.txtAttacksLeft);
        sullustSCText = itemView.findViewById(R.id.sullustSCText);
        plystatus_links = itemView.findViewById(R.id.plystatus_links);
        warPlayerFactionImg = itemView.findViewById(R.id.warPlayerFactionImg);
        sullustSCFullBar = itemView.findViewById(R.id.sullustSCFullBar);
        warPlayerBG = itemView.findViewById(R.id.warPlayerBG);
        viewSCBtn = itemView.findViewById(R.id.viewSCBtn);
        requestButton = itemView.findViewById(R.id.requestButton);
        hqBaseScore = itemView.findViewById(R.id.hqBaseScore);
        attackedBy = itemView.findViewById(R.id.attackedBy);
//        myPlayerWarToolBar = itemView.findViewById(R.id.myPlayerWarToolBar);
//        myPlayerWarToolBar.inflateMenu(R.menu.war_player_menu);
        plystatus_links.setMax(3);

    }
}
