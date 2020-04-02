package com.swctools.common.view_adaptors.view_holders;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.swctools.R;

public class ViewHolder_LayoutDetailVersions extends RecyclerView.ViewHolder {
    public RecyclerView listItemRecycler;
    public Button btnMveFolder;

    public ViewHolder_LayoutDetailVersions(@NonNull View itemView) {
        super(itemView);
        listItemRecycler = itemView.findViewById(R.id.listItemRecycler);
    }
}
