package com.swctools.activity_modules.player.views;

import android.content.res.ColorStateList;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.swctools.R;
import com.swctools.activity_modules.player.models.TacticalCapacityData;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder_TacticalCapacity extends RecyclerView.ViewHolder {
    public ImageView cap_Image, imgExpandTacCapLess, imgMoreLessTacCapFlip, imgExpandLocked;
    public TextView cap_Title, cap_Number, expandHintTxt, expandRowHint;
    public RecyclerView cap_List;
    public ConstraintLayout cap_Container, capListContainer;
    public ProgressBar cap_ProgressBar;

    public ViewHolder_TacticalCapacity(View itemView) {

        super(itemView);

        cap_Image = (ImageView) itemView.findViewById(R.id.cap_Image);
        cap_Title = (TextView) itemView.findViewById(R.id.cap_Title);
        cap_Number = (TextView) itemView.findViewById(R.id.cap_Number);
        cap_List = (RecyclerView) itemView.findViewById(R.id.cap_List);
        cap_ProgressBar = (ProgressBar) itemView.findViewById(R.id.cap_ProgressBar);
        cap_Container = itemView.findViewById(R.id.cap_Container);
        capListContainer = itemView.findViewById(R.id.capListContainer);
        imgExpandTacCapLess = itemView.findViewById(R.id.imgExpandTacCapLess);
        imgMoreLessTacCapFlip = itemView.findViewById(R.id.imgMoreLessTacCapFlip);
        expandHintTxt = itemView.findViewById(R.id.expandHintTxt);
        imgExpandLocked = itemView.findViewById(R.id.imgExpandLocked);
        expandRowHint = itemView.findViewById(R.id.expandRowHint);

/*        HideShowExpandedViewListener hideShowExpandedViewListener = new HideShowExpandedViewListener();
        cap_Container.setOnClickListener(hideShowExpandedViewListener);
        imgExpandTacCapLess.setOnClickListener(hideShowExpandedViewListener);

        imgExpandLocked.setOnClickListener(new LockUnlock());*/

    }

   /* class HideShowExpandedViewListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            boolean visible = false;
            appConfig.setPlayerDetailsExpandedHintClicked();
            if (capListContainer.getVisibility() == View.GONE) {
                capListContainer.setVisibility(View.VISIBLE);
                imgMoreLessTacCapFlip.setImageDrawable(expandLess);
                rowsExpanded.add(position, true);
                visible = true;

            } else {
                rowsExpanded.add(position, false);
                capListContainer.setVisibility(View.GONE);
                imgMoreLessTacCapFlip.setImageDrawable(expandMore);
                visible = false;

            }

            TacticalCapacityData tacticalCapacityData = (TacticalCapacityData) itemList.get(position);
            tacticalCapacityData.locked = visible;
        }
    }

    class LockUnlock implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            //feel like this is a piece of shit bit of code but meh
            int position = getAdapterPosition();
            boolean expanded;
            appConfig.setPlayerDetailsExpandedHintClicked();
            TacticalCapacityData rowData = (TacticalCapacityData) itemList.get(position);
            expanded = appConfig.getPlayerDetailExpanded(rowData.expandedSettingString);

            if (expanded) {
                appConfig.setPlayerDetailExpanded(false, rowData.expandedSettingString);
                imgExpandLocked.setImageDrawable(lockUnlocked);
                imgExpandLocked.setImageTintList(ColorStateList.valueOf(COLOR_RED));
            } else {
                appConfig.setPlayerDetailExpanded(true, rowData.expandedSettingString);
                imgExpandLocked.setImageDrawable(lockLocked);
                imgExpandLocked.setImageTintList(ColorStateList.valueOf(COLOR_GREEN));
            }


        }
    }*/
}
