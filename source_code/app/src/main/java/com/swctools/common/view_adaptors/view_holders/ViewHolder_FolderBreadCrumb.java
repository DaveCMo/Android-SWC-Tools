package com.swctools.common.view_adaptors.view_holders;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.swctools.R;

public class ViewHolder_FolderBreadCrumb extends RecyclerView.ViewHolder {
    public TextView folderName;

    public ViewHolder_FolderBreadCrumb(View itemView) {
        super(itemView);
        folderName = itemView.findViewById(R.id.breadCrumbName);

    }
}
