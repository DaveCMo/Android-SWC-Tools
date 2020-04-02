package com.swctools.common.view_adaptors.view_holders;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.swctools.R;

public class ViewHolder_ImageView extends RecyclerView.ViewHolder {
    public ConstraintLayout unselected_longPressConstraint, unselected_image_label_constraint;
    public ImageView unselected_item_Selected_Image, deleteImage, editLabel;
    public TextView unselected_image_label;


    public ViewHolder_ImageView(@NonNull View itemView) {
        super(itemView);

        unselected_item_Selected_Image = itemView.findViewById(R.id.unselected_item_Selected_Image);
        unselected_longPressConstraint = itemView.findViewById(R.id.unselected_longPressConstraint);
        unselected_image_label = itemView.findViewById(R.id.unselected_image_label);
        unselected_image_label_constraint = itemView.findViewById(R.id.unselected_image_label_constraint);
        deleteImage = itemView.findViewById(R.id.deleteImage);
        editLabel = itemView.findViewById(R.id.editLabel);

    }
}
