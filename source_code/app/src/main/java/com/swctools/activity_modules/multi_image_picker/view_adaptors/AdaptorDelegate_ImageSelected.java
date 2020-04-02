package com.swctools.activity_modules.multi_image_picker.view_adaptors;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swctools.R;
import com.swctools.common.helpers.ImageHelpers;
import com.swctools.common.view_adaptors.view_holders.ViewHolder_ImageSelected;
import com.swctools.activity_modules.multi_image_picker.ImageSelectorRecyclerInterface;
import com.swctools.activity_modules.multi_image_picker.models.SelectedImageModel;

import java.util.ArrayList;

public class AdaptorDelegate_ImageSelected {
    private int viewType;
    private Context mContext;
    private ImageSelectorRecyclerInterface imageSelectorRecyclerInterface;

    public AdaptorDelegate_ImageSelected(int viewType, Context context) {
        this.viewType = viewType;
        this.mContext = context;
        this.imageSelectorRecyclerInterface = (ImageSelectorRecyclerInterface) mContext;
    }


    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_image_selected, parent, false);
        return new ViewHolder_ImageSelected(itemView);
    }


    public int getViewType() {
        return this.viewType;
    }

    public int getItemViewType() {
        return this.viewType;
    }

    public boolean isForViewType(ArrayList<SelectedImageModel> items, int position) {
        if (items.get(position).selected) {
            return true;
        } else {
            return false;
        }

    }


    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder tholder, ArrayList<SelectedImageModel> itemList, final int position) {
        final SelectedImageModel selectedImageModel = itemList.get(position);
        ViewHolder_ImageSelected holder = (ViewHolder_ImageSelected) tholder;
        holder.item_Selected_Image.setImageBitmap(ImageHelpers.bytesToBitmap(selectedImageModel.bytes));
        holder.selected_image_label.setText(selectedImageModel.label);

        holder.selected_layout_image_constraint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageSelectorRecyclerInterface.setLabel(position);
            }
        });

        holder.item_Selected_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageSelectorRecyclerInterface.itemDeselected(position);
            }
        });

        holder.selected_longPressConstraint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageSelectorRecyclerInterface.itemDeselected(position);
            }
        });
        holder.selectedImageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageSelectorRecyclerInterface.itemDelete(position);
            }
        });

    }
}
