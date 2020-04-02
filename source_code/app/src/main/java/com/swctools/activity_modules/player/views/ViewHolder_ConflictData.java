package com.swctools.activity_modules.player.views;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.swctools.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewHolder_ConflictData extends RecyclerView.ViewHolder {
    public CircleImageView planet_img;
    public TextView planet_name, conflictPerc;
    public RecyclerView conflictExtraRecycler;
    public ConstraintLayout conflictListRow;

    public ViewHolder_ConflictData(@NonNull View itemView) {
        super(itemView);
        planet_img = itemView.findViewById(R.id.planet_img);
        planet_name = itemView.findViewById(R.id.planet_name);
        conflictPerc = itemView.findViewById(R.id.conflictPerc);
        conflictListRow = itemView.findViewById(R.id.conflictListRow);
        conflictExtraRecycler = itemView.findViewById(R.id.conflictExtraRecycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(null, LinearLayoutManager.VERTICAL, false);//, 2, GridLayoutManager.VERTICAL, false);
        conflictExtraRecycler.setLayoutManager(layoutManager);
        conflictExtraRecycler.setItemAnimator(new DefaultItemAnimator());
    }
}
