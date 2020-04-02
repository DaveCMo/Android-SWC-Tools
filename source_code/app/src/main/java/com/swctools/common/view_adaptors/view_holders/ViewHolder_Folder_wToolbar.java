package com.swctools.common.view_adaptors.view_holders;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import com.swctools.R;

public class ViewHolder_Folder_wToolbar extends RecyclerView.ViewHolder {
    public TextView folderName, folderContentsCount;
    public Toolbar layoutFolder_Toolbar;
    public CardView folderCard;
    public ConstraintLayout folderClick;

    public ViewHolder_Folder_wToolbar(View itemView) {
        super(itemView);
        layoutFolder_Toolbar = itemView.findViewById(R.id.layoutFolder_Toolbar);
        folderName = itemView.findViewById(R.id.folderName);
        folderContentsCount = itemView.findViewById(R.id.folderContentsCount);
        folderCard = itemView.findViewById(R.id.folderCard);
        folderClick = itemView.findViewById(R.id.folderClick);

    }
}
