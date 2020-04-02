package com.swctools.common.view_adaptors.view_holders;

import com.google.android.material.button.MaterialButton;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import com.swctools.R;

public class ViewHolder_LayoutSimple extends RecyclerView.ViewHolder {
    public TextView favourite_layout_name, favourite_layout_version;
    public MaterialButton updateWar_Pill, updatePVP_Pill;

    public Toolbar favLayoutToolbar;

    public ViewHolder_LayoutSimple(View itemView) {
        super(itemView);
        favourite_layout_name = itemView.findViewById(R.id.favourite_layout_name);
        favourite_layout_version = itemView.findViewById(R.id.favourite_layout_version);
        updateWar_Pill = itemView.findViewById(R.id.updateWar_Pill);
        updatePVP_Pill = itemView.findViewById(R.id.updatePVP_Pill);

        favLayoutToolbar = itemView.findViewById(R.id.favLayoutToolbar);
//        favLayoutToolbar.inflateMenu(R.menu.layout_fav_menu);

    }

}
