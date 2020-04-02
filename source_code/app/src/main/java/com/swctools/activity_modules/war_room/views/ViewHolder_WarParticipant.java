package com.swctools.activity_modules.war_room.views;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRatingBar;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.swctools.R;

public class ViewHolder_WarParticipant extends RecyclerView.ViewHolder {

    public TextView warPartPlrName, warPart_Attacks, hqBaseScore, lastAttackedBy;
    public AppCompatRatingBar warPart_Stars;
    public ImageView warPart_FactionImg;
//    public Toolbar warPartToolBar;
public ConstraintLayout warPartBG;



    public ViewHolder_WarParticipant(@NonNull View itemView) {
        super(itemView);
        warPartPlrName = itemView.findViewById(R.id.warPartPlrName);
        warPart_Attacks = itemView.findViewById(R.id.warPart_Attacks);
        warPart_Stars = itemView.findViewById(R.id.warPart_Stars);
        lastAttackedBy = itemView.findViewById(R.id.lastAttackedBy);
        warPart_FactionImg = itemView.findViewById(R.id.warPart_FactionImg);
        warPartBG = itemView.findViewById(R.id.warPartBG);
        hqBaseScore = itemView.findViewById(R.id.hqBaseScore);
//        warPartToolBar = itemView.findViewById(R.id.warPartToolBar);
//        warPartToolBar.inflateMenu(R.menu.war_participant_menu);

    }
}
