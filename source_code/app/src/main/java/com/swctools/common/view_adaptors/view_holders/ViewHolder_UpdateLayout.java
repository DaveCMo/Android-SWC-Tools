package com.swctools.common.view_adaptors.view_holders;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.swctools.R;

public class ViewHolder_UpdateLayout extends RecyclerView.ViewHolder {
    public TextView updateLayout_Name, updateLayoutPlayer;
    public Button updateLayoutSelect;
    public ImageView updateLayoutFaction;




    public ViewHolder_UpdateLayout(View view) {
        super(view);
        updateLayout_Name = (TextView)view.findViewById(R.id.updateLayout_Name);
        updateLayoutPlayer = (TextView) view.findViewById(R.id.updateLayoutPlayer);

        updateLayoutSelect = (Button) view.findViewById(R.id.updateLayoutSelect);

        updateLayoutFaction = (ImageView) view.findViewById(R.id.updateLayoutFaction);
    }
}
