package com.swctools.activity_modules.main.views;

import com.google.android.material.button.MaterialButton;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.swctools.R;

public class ViewHolder_FavouriteLayout extends RecyclerView.ViewHolder {
    public TextView favourite_layout_name, favourite_layout_version;
    //    public Button favLayout_UpdatePVP, favLayout_UpdateWAR;
    public MaterialButton updateWar_Pill, updatePVP_Pill;
    public ImageView layoutFavImg;//deleteMostLastUsedLayoutLog;
    public Toolbar favLayoutToolbar;

    public ViewHolder_FavouriteLayout(View itemView, boolean showDelete) {
        super(itemView);
        favourite_layout_name = itemView.findViewById(R.id.favourite_layout_name);
        favourite_layout_version = itemView.findViewById(R.id.favourite_layout_version);
//        favLayout_UpdatePVP = itemView.findViewById(R.id.favLayout_UpdatePVP);
//        favLayout_UpdateWAR = itemView.findViewById(R.id.favLayout_UpdateWAR);
//        deleteMostLastUsedLayoutLog = itemView.findViewById(R.id.deleteMostLastUsedLayoutLog);
        layoutFavImg = itemView.findViewById(R.id.layoutFavImg);
        updateWar_Pill = itemView.findViewById(R.id.updateWar_Pill);
        updatePVP_Pill = itemView.findViewById(R.id.updatePVP_Pill);

        favLayoutToolbar = itemView.findViewById(R.id.favLayoutToolbar);
        favLayoutToolbar.inflateMenu(R.menu.layout_fav_menu);

    }

}
