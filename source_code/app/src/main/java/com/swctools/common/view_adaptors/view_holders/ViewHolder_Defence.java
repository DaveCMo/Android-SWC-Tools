package com.swctools.common.view_adaptors.view_holders;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.swctools.R;

public class ViewHolder_Defence extends RecyclerView.ViewHolder {
    public TextView result, percentDamage, attackedDate, attackedBy, credits, alloy, contra, medals, gears, defenceTroopsUsedList, def_ExtraSCTroopList, def_SCUsed, defenceArmouryUsed;
    public ImageView newDefenceIcon, defenceExpandExtra;
    public LinearLayout defenceExtra, defenceRowMain;
    public ConstraintLayout defenceExtra_SCUSedContainer, def_BottomRow;

    public boolean extraOpen;


    public ViewHolder_Defence(View view) {
        super(view);

        result = (TextView) view.findViewById(R.id.result);
        percentDamage = (TextView) view.findViewById(R.id.percentDamage);
        attackedBy = (TextView) view.findViewById(R.id.attackedBy);
        attackedDate = (TextView) view.findViewById(R.id.attackedDate);
        credits = (TextView) view.findViewById(R.id.credits);
        alloy = (TextView) view.findViewById(R.id.alloy);
        contra = (TextView) view.findViewById(R.id.contra);
        medals = (TextView) view.findViewById(R.id.medals);
        gears = (TextView) view.findViewById(R.id.gears);
        newDefenceIcon = (ImageView) view.findViewById(R.id.newDefenceIcon);
//            defenceExpandExtra = (ImageView) view.findViewById(R.id.defenceExpandExtra);
        defenceExtra = (LinearLayout) view.findViewById(R.id.defenceExtra);
        defenceRowMain = (LinearLayout) view.findViewById(R.id.defenceRowMain);

        defenceTroopsUsedList = (TextView) view.findViewById(R.id.defenceTroopsUsedList);
        def_ExtraSCTroopList = (TextView) view.findViewById(R.id.def_ExtraSCTroopList);
        def_SCUsed = (TextView) view.findViewById(R.id.def_SCUsed);
        defenceExtra_SCUSedContainer = (ConstraintLayout) view.findViewById(R.id.defenceExtra_SCUSedContainer);
        defenceArmouryUsed = (TextView) view.findViewById(R.id.defenceArmouryUsed);
        def_BottomRow = (ConstraintLayout) view.findViewById(R.id.def_BottomRow);

    }


}
