package com.swctools.common.view_adaptors.view_holders;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.swctools.R;

public class ViewHolder_LayoutDetailFolder extends RecyclerView.ViewHolder {
    public RecyclerView layoutDetailsFolder;
    public ImageView layoutDetailEdit;
    public ViewHolder_LayoutDetailFolder(@NonNull View itemView) {
        super(itemView);
        layoutDetailsFolder = itemView.findViewById(R.id.layoutDetailsFolder);
        layoutDetailEdit= itemView.findViewById(R.id.layoutDetailEdit);

    }
}
