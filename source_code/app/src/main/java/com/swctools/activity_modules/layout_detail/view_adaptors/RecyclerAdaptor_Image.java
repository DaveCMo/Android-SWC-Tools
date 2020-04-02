package com.swctools.activity_modules.layout_detail.view_adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.swctools.R;
import com.swctools.common.view_adaptors.view_holders.ViewHolder_ImageView;
import com.swctools.activity_modules.layout_detail.models.LayoutDetail_ImageListItem;
import com.swctools.activity_modules.multi_image_picker.ImageListInterface;
import com.swctools.util.StringUtil;

import java.io.File;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.swctools.R.drawable.layout_editor;

public class RecyclerAdaptor_Image extends RecyclerView.Adapter<ViewHolder_ImageView> {
    private static final String TAG = "RecyclerAdaptor_Image";
    private ArrayList<LayoutDetail_ImageListItem> itemList;
    private ImageListInterface imageListInterface;

    public RecyclerAdaptor_Image(ArrayList<LayoutDetail_ImageListItem> itemList, Context context) {
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
        final LayoutDetail_ImageListItem selectedImageModel = itemList.get(position);
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(layout_editor);
        if (StringUtil.isStringNotNull(selectedImageModel.getFilePath())) {

            File file = new File(selectedImageModel.getFilePath());
            Glide.with(holder.itemView.getContext())
                    .setDefaultRequestOptions(requestOptions)
                    .load(file)
                    .into(holder.unselected_item_Selected_Image);
        }

        holder.unselected_image_label.setText(selectedImageModel.getLabel());
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
                imageListInterface.deleteImage(selectedImageModel.getNo());
            }
        });
        holder.editLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageListInterface.editImageLabel(selectedImageModel.getNo(), selectedImageModel.getLabel());
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


}
