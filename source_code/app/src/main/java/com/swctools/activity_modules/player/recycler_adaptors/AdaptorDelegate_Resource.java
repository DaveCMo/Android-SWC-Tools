package com.swctools.activity_modules.player.recycler_adaptors;

import android.content.Context;
import android.content.res.ColorStateList;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.swctools.R;
import com.swctools.common.enums.ApplicationMessageTemplates;
import com.swctools.activity_modules.player.models.ResourceDataItem;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class AdaptorDelegate_Resource {
    private int viewType;
    private Context mContext;
    private DecimalFormat formatter;

    public AdaptorDelegate_Resource(int viewType, Context context) {
        this.viewType = viewType;
        this.mContext = context;
        this.formatter = new DecimalFormat("#,###,###");
    }


    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_player_resource, parent, false);
        return new ResourceViewHolder(itemView);
    }


    public int getViewType() {
        return this.viewType;
    }

    public int getItemViewType() {
        return this.viewType;
    }

    public boolean isForViewType(ArrayList<Object> items, int position) {
        return items.get(position) instanceof ResourceDataItem;
    }


    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder tholder, ArrayList<Object> itemList, int position) {
        final ResourceDataItem resourceDataItem = (ResourceDataItem) itemList.get(position);
        final ResourceViewHolder viewHolder = (ResourceViewHolder) tholder;

        viewHolder.resourceFullBar.setMax(resourceDataItem.capacity);
        viewHolder.resourceFullBar.setProgress(resourceDataItem.amount);
        if (resourceDataItem.barColour!=0){
            viewHolder.resourceFullBar.setProgressTintList(ColorStateList.valueOf(resourceDataItem.barColour));
        } else {
            viewHolder.resourceFullBar.setProgressTintList(ColorStateList.valueOf(mContext.getResources().getColor(R.color.colorAccent)));
        }
        viewHolder.resourceLabel.setText(String.format(ApplicationMessageTemplates.PROGRESS_BAR_LABEL.getemplateString(), formatter.format(resourceDataItem.amount), formatter.format(resourceDataItem.capacity)));
        viewHolder.resourceTitle.setText(resourceDataItem.title);
    }

    class ResourceViewHolder extends RecyclerView.ViewHolder {
        public TextView resourceLabel, resourceTitle;
        public ProgressBar resourceFullBar;

        public ResourceViewHolder(View itemView) {
            super(itemView);
            resourceLabel = itemView.findViewById(R.id.resourceLabel);
            resourceFullBar = itemView.findViewById(R.id.resourceFullBar);
            resourceTitle = itemView.findViewById(R.id.resourceTitle);
        }

    }

}
