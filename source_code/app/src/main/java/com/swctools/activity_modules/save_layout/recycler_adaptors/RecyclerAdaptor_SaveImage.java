package com.swctools.activity_modules.save_layout.recycler_adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swctools.R;
import com.swctools.common.helpers.ImageHelpers;
import com.swctools.common.view_adaptors.view_holders.ViewHolder_ImageView;
import com.swctools.activity_modules.multi_image_picker.ImageListInterface;
import com.swctools.activity_modules.multi_image_picker.models.SelectedImageModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdaptor_SaveImage extends RecyclerView.Adapter<ViewHolder_ImageView> {
    private static final String TAG = "RecyclerAdaptor_Image";
    private ArrayList<SelectedImageModel> itemList;
    private ImageListInterface imageListInterface;

    public RecyclerAdaptor_SaveImage(ArrayList<SelectedImageModel> itemList, Context context) {
        this.itemList = itemList;
        this.imageListInterface = (ImageListInterface) context;
    }

    @NonNull
    @Override
    public ViewHolder_ImageView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_image_view, parent, false);
        return new ViewHolder_ImageView(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder_ImageView holder, final int position) {
        final SelectedImageModel selectedImageModel = itemList.get(position);
        holder.unselected_item_Selected_Image.setImageBitmap(ImageHelpers.bytesToBitmap(selectedImageModel.bytes));
        holder.unselected_image_label.setText(selectedImageModel.label);
        holder.unselected_item_Selected_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageListInterface.showGallery(position);
            }
        });
        holder.unselected_longPressConstraint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageListInterface.showGallery(position);
            }
        });

        holder.deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageListInterface.deleteImage(selectedImageModel.no);
            }
        });
        holder.editLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageListInterface.editImageLabel(selectedImageModel.no, selectedImageModel.label);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


}
