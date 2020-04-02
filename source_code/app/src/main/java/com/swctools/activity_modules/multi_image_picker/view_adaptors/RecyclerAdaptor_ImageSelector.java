package com.swctools.activity_modules.multi_image_picker.view_adaptors;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ViewGroup;

import com.swctools.activity_modules.multi_image_picker.models.SelectedImageModel;

import java.util.ArrayList;

public class RecyclerAdaptor_ImageSelector extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "RrAdr_ImageSelector";
    private static final int UNSELECTED = 0;
    private static final int SELECTED = 1;
    private ArrayList<SelectedImageModel> itemList;
    private Context mContext;
    private AdaptorDelegate_ImageSelected adaptorDelegate_imageSelected;
    private AdaptorDelegate_ImageUnSelected adaptorDelegate_imageUnSelected;


    public RecyclerAdaptor_ImageSelector(ArrayList<SelectedImageModel> list, Context context) {
        this.itemList = list;
        this.mContext = context;
        this.adaptorDelegate_imageSelected = new AdaptorDelegate_ImageSelected(SELECTED, mContext);
        this.adaptorDelegate_imageUnSelected = new AdaptorDelegate_ImageUnSelected(UNSELECTED, mContext);

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (adaptorDelegate_imageUnSelected.getViewType() == viewType) {
            return adaptorDelegate_imageUnSelected.onCreateViewHolder(parent);
        } else if (adaptorDelegate_imageSelected.getViewType() == viewType) {
            return adaptorDelegate_imageSelected.onCreateViewHolder(parent);
        }
        throw new IllegalArgumentException("No delegate found");

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        int viewType = holder.getItemViewType();
        if (adaptorDelegate_imageSelected.getViewType() == viewType) {
            adaptorDelegate_imageSelected.onBindViewHolder(holder, itemList, position);
        } else if (adaptorDelegate_imageUnSelected.getViewType() == viewType) {
            adaptorDelegate_imageUnSelected.onBindViewHolder(holder, itemList, position);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (adaptorDelegate_imageSelected.isForViewType(itemList, position)) {
            return adaptorDelegate_imageSelected.getViewType();
        } else if (adaptorDelegate_imageUnSelected.isForViewType(itemList, position)) {
            return adaptorDelegate_imageUnSelected.getViewType();
        }
        throw new IllegalArgumentException("No delegate found");

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


}
