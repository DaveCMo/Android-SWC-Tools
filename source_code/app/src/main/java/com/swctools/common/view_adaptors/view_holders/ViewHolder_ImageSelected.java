package com.swctools.common.view_adaptors.view_holders;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.swctools.R;

public class ViewHolder_ImageSelected extends RecyclerView.ViewHolder {
    public ConstraintLayout selected_longPressConstraint, selected_layout_image_constraint;
    public ImageView item_Selected_Image, selectedImageDelete;
    public TextView selected_image_label;


    public ViewHolder_ImageSelected(@NonNull View itemView) {
        super(itemView);

        item_Selected_Image = itemView.findViewById(R.id.item_Selected_Image);
        selected_longPressConstraint = itemView.findViewById(R.id.selected_longPressConstraint);
        selectedImageDelete = itemView.findViewById(R.id.selectedImageDelete);
        selected_image_label = itemView.findViewById(R.id.selected_image_label);
        selected_layout_image_constraint = itemView.findViewById(R.id.selected_layout_image_constraint);


    }
}
