package com.swctools.common.view_adaptors.view_holders;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.swctools.R;

public class ViewHolder_LayoutDetailMain extends RecyclerView.ViewHolder {
    public ImageView layoutDetailImg, layoutFavImg, img_layoutDetaildeleteImage;

    public EditText layoutName;
    public Spinner playerSpinner, factionSpinner;

    public ViewHolder_LayoutDetailMain(@NonNull View itemView) {
        super(itemView);
        layoutDetailImg = itemView.findViewById(R.id.layoutDetailImg);
//        layoutFactionImage = itemView.findViewById(R.id.layoutFactionImage);
        layoutFavImg = itemView.findViewById(R.id.layoutFavImg);
        layoutName = itemView.findViewById(R.id.layoutName);


        playerSpinner = itemView.findViewById(R.id.playerSpinner);
        factionSpinner = itemView.findViewById(R.id.factionSpinner);
        img_layoutDetaildeleteImage = itemView.findViewById(R.id.img_layoutDetaildeleteImage);
    }
}
