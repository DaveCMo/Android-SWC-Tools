package com.swctools.common.view_adaptors.view_holders;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.swctools.R;

public class ViewHolder_LayoutTagList extends RecyclerView.ViewHolder {
    public TextView tag_txt;
    public CardView layoutTagPillCard;
    public ImageView tagX;
    public ViewHolder_LayoutTagList(@NonNull View itemView, boolean showX) {
        super(itemView);
        tag_txt = itemView.findViewById(R.id.tag_txt);
        tagX =itemView.findViewById(R.id.tagX);
        layoutTagPillCard = itemView.findViewById(R.id.layoutTagPillCard);
        if(!showX){
            tagX.setVisibility(View.GONE);
        } else {
            tagX.setVisibility(View.VISIBLE);
        }

    }
}
