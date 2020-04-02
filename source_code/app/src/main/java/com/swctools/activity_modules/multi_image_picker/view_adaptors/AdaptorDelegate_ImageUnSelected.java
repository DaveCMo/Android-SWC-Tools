package com.swctools.activity_modules.multi_image_picker.view_adaptors;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swctools.R;
import com.swctools.common.helpers.ImageHelpers;
import com.swctools.common.view_adaptors.view_holders.ViewHolder_ImageUnselected;
import com.swctools.activity_modules.multi_image_picker.ImageSelectorRecyclerInterface;
import com.swctools.activity_modules.multi_image_picker.models.SelectedImageModel;

import java.util.ArrayList;

public class AdaptorDelegate_ImageUnSelected {
    private int viewType;
    private Context mContext;
    private ImageSelectorRecyclerInterface imageSelectorRecyclerInterface;
    private int currentPosition;

    public AdaptorDelegate_ImageUnSelected(int viewType, Context context) {
        this.viewType = viewType;
        this.mContext = context;
        this.imageSelectorRecyclerInterface = (ImageSelectorRecyclerInterface) mContext;
    }


    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_image_unselected, parent, false);
        return new ViewHolder_ImageUnselected(itemView);
    }


    public int getViewType() {
        return this.viewType;
    }

    public int getItemViewType() {
        return this.viewType;
    }

    public boolean isForViewType(ArrayList<SelectedImageModel> items, int position) {
        if (items.get(position).selected) {
            return false;
        } else {
            return true;
        }
    }

    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder tholder, final ArrayList<SelectedImageModel> itemList, final int position) {
        final SelectedImageModel selectedImageModel = itemList.get(position);
        ViewHolder_ImageUnselected holder = (ViewHolder_ImageUnselected) tholder;
        holder.unselected_item_Selected_Image.setImageBitmap(ImageHelpers.bytesToBitmap(selectedImageModel.bytes));
        holder.unselected_image_label.setText(selectedImageModel.label);


        holder.unselected_image_label_constraint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageSelectorRecyclerInterface.setLabel(position);
            }
        });

        holder.unselected_item_Selected_Image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                imageSelectorRecyclerInterface.itemSelected(position);
                return true;
            }
        });
        holder.unselected_longPressConstraint.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                imageSelectorRecyclerInterface.itemSelected(position);
                return true;
            }
        });
        holder.unselected_item_Selected_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageSelectorRecyclerInterface.itemShortPressed(position);
            }
        });
        holder.unselected_longPressConstraint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageSelectorRecyclerInterface.itemShortPressed(position);
            }
        });

    }


}
