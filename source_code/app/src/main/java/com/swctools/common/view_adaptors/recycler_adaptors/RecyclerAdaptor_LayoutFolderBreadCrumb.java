package com.swctools.common.view_adaptors.recycler_adaptors;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.swctools.R;
import com.swctools.interfaces.FolderBreadCrumbInterface;
import com.swctools.layouts.models.LayoutFolderItem;
import com.swctools.common.view_adaptors.view_holders.ViewHolder_FolderBreadCrumb;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdaptor_LayoutFolderBreadCrumb extends RecyclerView.Adapter<ViewHolder_FolderBreadCrumb> implements Filterable {
    private static final String TAG = "FolderBreadCrumbAda";
    private ArrayList<LayoutFolderItem> folderItems;
    private ArrayList<LayoutFolderItem> folderSelectedItems;
    private Context mContext;
    private FolderBreadCrumbInterface mCallback;
    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<LayoutFolderItem> filteredList = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0) {
                for (LayoutFolderItem layoutFolderItem : folderItems) {
                    if (layoutFolderItem.getParentFolderId() == 0) {
                        filteredList.add(layoutFolderItem);
                    }
                }
            } else {
//                String folderStringPattern = charSequence.toString();
//                int folderId = Integer.parseInt(folderStringPattern);
                for (LayoutFolderItem layoutFolderItem : folderItems) {
                    filteredList.add(layoutFolderItem);
                }

            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            folderSelectedItems.clear();
            folderSelectedItems.addAll((ArrayList<LayoutFolderItem>) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public RecyclerAdaptor_LayoutFolderBreadCrumb(ArrayList<LayoutFolderItem> list, Context context) {
        this.folderItems = list;
        this.mContext = context;
        mCallback = (FolderBreadCrumbInterface) context;
    }

    @NonNull
    @Override
    public ViewHolder_FolderBreadCrumb onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_folder_breadcrumb, parent, false);
        return new ViewHolder_FolderBreadCrumb(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder_FolderBreadCrumb holder, final int position) {
        final LayoutFolderItem layoutFolderItem = folderItems.get(position);

        holder.folderName.setText(layoutFolderItem.getFolderName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.breadCrumbSelected(layoutFolderItem.getFolderId(), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return folderItems.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    public void addBreadCrumb(LayoutFolderItem layoutFolderItem) {
        if (folderItems == null) {
            folderItems = new ArrayList<>();
        }
        folderItems.add(layoutFolderItem);
        notifyDataSetChanged();
    }

    public void addBreadCrumbs(ArrayList<LayoutFolderItem> folderSelectedItems){
        if(folderSelectedItems==null){
            folderItems=new ArrayList<>();
        }

        folderItems=folderSelectedItems;
    }
}
