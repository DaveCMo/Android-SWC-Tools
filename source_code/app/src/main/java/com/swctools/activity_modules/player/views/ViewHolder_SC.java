package com.swctools.activity_modules.player.views;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.swctools.R;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder_SC extends RecyclerView.ViewHolder {
    public ImageView cap_Image, imgExpandTacCapLess, imgMoreLessTacCapFlip, imgExpandLocked;
    public TextView cap_Title, cap_Number, expandHintTxt, expandRowHint;
    public RecyclerView cap_List;
    public ConstraintLayout cap_Container, capListContainer;
    public ProgressBar cap_ProgressBar;
    public Toolbar tacticalCalToolbar;

    public ViewHolder_SC(View itemView) {

        super(itemView);

        cap_Image = (ImageView) itemView.findViewById(R.id.cap_Image);
        cap_Title = (TextView) itemView.findViewById(R.id.cap_Title);
        cap_Number = (TextView) itemView.findViewById(R.id.cap_Number);
        cap_List = (RecyclerView) itemView.findViewById(R.id.cap_List);
        cap_ProgressBar = (ProgressBar) itemView.findViewById(R.id.cap_ProgressBar);
        cap_Container = itemView.findViewById(R.id.cap_Container);
        capListContainer = itemView.findViewById(R.id.capListContainer);
        imgExpandTacCapLess = itemView.findViewById(R.id.imgExpandTacCapLess);
        imgMoreLessTacCapFlip = itemView.findViewById(R.id.imgMoreLessTacCapFlip);
        expandHintTxt = itemView.findViewById(R.id.expandHintTxt);
        imgExpandLocked = itemView.findViewById(R.id.imgExpandLocked);
        expandRowHint = itemView.findViewById(R.id.expandRowHint);
        tacticalCalToolbar = itemView.findViewById(R.id.tacticalCalToolbar);
        tacticalCalToolbar.inflateMenu(R.menu.player_sc_menu);
    }
}
