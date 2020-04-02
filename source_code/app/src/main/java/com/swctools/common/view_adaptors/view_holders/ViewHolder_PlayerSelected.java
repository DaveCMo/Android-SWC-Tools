package com.swctools.common.view_adaptors.view_holders;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.swctools.R;

public class ViewHolder_PlayerSelected extends RecyclerView.ViewHolder {
    public CheckBox playerSelected;
    public ImageView playerFactionIcon;
    public TextView playerName;

    public ViewHolder_PlayerSelected(@NonNull View itemView) {
        super(itemView);
        playerFactionIcon = itemView.findViewById(R.id.playerFactionIcon);
        playerSelected = itemView.findViewById(R.id.playerSelected);
        playerName = itemView.findViewById(R.id.playerName);

    }
}
