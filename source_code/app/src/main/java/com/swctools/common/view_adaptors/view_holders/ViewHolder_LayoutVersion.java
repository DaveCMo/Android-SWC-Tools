package com.swctools.common.view_adaptors.view_holders;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import com.swctools.R;

public class ViewHolder_LayoutVersion extends RecyclerView.ViewHolder {
    public TextView layoutVersion;
    public Button applyVersionWAR, applyVersionPVP;
    public Toolbar layoutVersionToolbar;


    public ViewHolder_LayoutVersion(View itemView) {
        super(itemView);
        layoutVersion = (TextView) itemView.findViewById(R.id.layoutVersion);
        applyVersionPVP = (Button) itemView.findViewById(R.id.applyVersionPVP);
        applyVersionWAR = (Button) itemView.findViewById(R.id.applyVersionWAR);
        layoutVersionToolbar = (Toolbar) itemView.findViewById(R.id.layoutVersionToolbar);
    }
}
