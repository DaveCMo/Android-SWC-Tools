package com.swctools.common.view_adaptors.view_holders;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.swctools.R;

public class ViewHolder_PlayerNotification extends RecyclerView.ViewHolder {
    public TextView pNotification_Name;
    public Switch pNotification_Switch;

    public ViewHolder_PlayerNotification(View view) {
        super(view);
        pNotification_Name = (TextView) view.findViewById(R.id.pNotification_Name);
        pNotification_Switch = (Switch) view.findViewById(R.id.pNotification_Switch);

    }
}
