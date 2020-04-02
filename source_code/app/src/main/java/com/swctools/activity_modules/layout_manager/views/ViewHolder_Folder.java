package com.swctools.activity_modules.layout_manager.views;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.swctools.R;

public class ViewHolder_Folder extends RecyclerView.ViewHolder {
    public TextView folderName;
    public TextView folderContentsCount;
    public ConstraintLayout folderClick;

    public ViewHolder_Folder(View itemView) {
        super(itemView);
        folderName = itemView.findViewById(R.id.folderName);
        folderContentsCount = itemView.findViewById(R.id.folderContentsCount);
        folderClick = itemView.findViewById(R.id.folderClick);
    }
}
