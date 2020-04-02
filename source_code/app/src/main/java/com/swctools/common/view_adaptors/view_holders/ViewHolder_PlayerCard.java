package com.swctools.common.view_adaptors.view_holders;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toolbar;

import com.swctools.R;

public class ViewHolder_PlayerCard extends RecyclerView.ViewHolder {
    //    private RecyclerAdaptor_PlayerList recyclerAdaptor_playerList;
    public ImageView playerListFaction, collapseCardImg, expandCardImg;
    public TextView playerList_Name, playerList_PlayerGuild;
    public Toolbar toolbar;
    public LinearLayout playerList_linearLayout;
    public Button playerListViewPlayerBtn, playerListViewLayoutsBtn, playerGetWARBtn, playerGetPVPBtn;
    public RecyclerView favLayoutRecycler;
    public ConstraintLayout expandRow, hideRow, playerTopClickLayout;
    public Spinner favouriteLayoutListTypeSpinner;


    public ViewHolder_PlayerCard(View v) {
        super(v);
//        this.recyclerAdaptor_playerList = recyclerAdaptor_playerList;
        playerListFaction = v.findViewById(R.id.playerListFaction);
        playerList_Name = v.findViewById(R.id.playerList_Name);
        playerList_PlayerGuild = v.findViewById(R.id.playerList_PlayerGuild);
        toolbar = v.findViewById(R.id.playerListCard_Toolbar);
        toolbar.inflateMenu(R.menu.player_card_menu);
        playerList_linearLayout = v.findViewById(R.id.playerList_linearLayout);
        expandCardImg = v.findViewById(R.id.expandCardImg);
        collapseCardImg = v.findViewById(R.id.collapseCardImg);
        playerListViewPlayerBtn = v.findViewById(R.id.playerListViewPlayerBtn);
        favLayoutRecycler = v.findViewById(R.id.favLayoutRecycler);

        playerListViewLayoutsBtn = v.findViewById(R.id.playerListViewLayoutsBtn);
        expandRow = v.findViewById(R.id.expandRow);
        hideRow = v.findViewById(R.id.hideRow);
        favouriteLayoutListTypeSpinner = v.findViewById(R.id.favouriteLayoutListTypeSpinner);
        playerGetPVPBtn = v.findViewById(R.id.playerGetPVPBtn);
        playerGetWARBtn = v.findViewById(R.id.playerGetWARBtn);
        playerTopClickLayout = v.findViewById(R.id.playerTopClickLayout);


    }


}
