package com.swctools.activity_modules.layout_detail.view_adaptors;

import android.content.Context;
import android.content.res.Resources;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.swctools.common.view_adaptors.delegated_adaptors.AdaptorDelegate_LayoutDetailFolders;
import com.swctools.common.view_adaptors.delegated_adaptors.AdaptorDelegate_LayoutDetailMainDetails;
import com.swctools.common.view_adaptors.delegated_adaptors.AdaptorDelegate_LayoutDetailTags;
import com.swctools.common.view_adaptors.delegated_adaptors.AdaptorDelegate_LayoutDetailVersions;

import java.util.ArrayList;

public class RecyclerAdaptor_LayoutDetailList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "LAYOUTDETLISTADAPT";

    private static final int MAINDETAIL = 0;
    private static final int FOLDERS = 1;
    private static final int TAGS = 2;
    private static final int VERSIONS = 3;
    private AdaptorDelegate_LayoutDetailMainDetails mainDetailDelegate;
    private AdaptorDelegate_LayoutDetailFolders folders_adaptorDelegate;
    private AdaptorDelegate_LayoutDetailTags tags_adaptorDelegate;
    private AdaptorDelegate_LayoutDetailVersions versions_adaptorDelegate;

    private ArrayList<Object> itemList;
    private Context mContext;

    public RecyclerAdaptor_LayoutDetailList(ArrayList<Object> itemList, Context context) {
        this.itemList = itemList;
        this.mContext = context;
        this.mainDetailDelegate = new AdaptorDelegate_LayoutDetailMainDetails(MAINDETAIL, mContext);
        this.folders_adaptorDelegate = new AdaptorDelegate_LayoutDetailFolders(FOLDERS, mContext);
        this.tags_adaptorDelegate = new AdaptorDelegate_LayoutDetailTags(TAGS, mContext);
        this.versions_adaptorDelegate = new AdaptorDelegate_LayoutDetailVersions(VERSIONS, mContext);
    }

//    public void setEditActive
    public static void setBottomMargin(View view, int bottomMargin) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            params.setMargins(params.leftMargin, params.topMargin, params.rightMargin, bottomMargin);
            view.requestLayout();
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (mainDetailDelegate.isForViewType(itemList, position)) {
            return mainDetailDelegate.getItemViewType();
        }
        if (folders_adaptorDelegate.isForViewType(itemList, position)) {
            return folders_adaptorDelegate.getItemViewType();
        }
        if (tags_adaptorDelegate.isForViewType(itemList, position)) {
            return tags_adaptorDelegate.getItemViewType();
        }
        if (versions_adaptorDelegate.isForViewType(itemList, position)) {
            return versions_adaptorDelegate.getItemViewType();
        }
        throw new IllegalArgumentException("No delegate found");
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mainDetailDelegate.getItemViewType() == viewType) {
            return mainDetailDelegate.onCreateViewHolder(parent);
        }
        if (folders_adaptorDelegate.getItemViewType() == viewType) {
            return folders_adaptorDelegate.onCreateViewHolder(parent);
        }
        if (tags_adaptorDelegate.getItemViewType() == viewType) {
            return tags_adaptorDelegate.onCreateViewHolder(parent);
        }
        if (versions_adaptorDelegate.getItemViewType() == viewType) {
            return versions_adaptorDelegate.onCreateViewHolder(parent);
        }

        throw new IllegalArgumentException("No delegate found");
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int position) {

        int viewType = viewHolder.getItemViewType();

        if (mainDetailDelegate.getViewType() == viewType) {
            mainDetailDelegate.onBindViewHolder(viewHolder, itemList, position);
        }
        if (folders_adaptorDelegate.getViewType() == viewType) {
            folders_adaptorDelegate.onBindViewHolder(viewHolder, itemList, position);
        }

        if (tags_adaptorDelegate.getItemViewType() == viewType) {
            tags_adaptorDelegate.onBindViewHolder(viewHolder, itemList, position);
        }

        if (versions_adaptorDelegate.getItemViewType() == viewType) {
            versions_adaptorDelegate.onBindViewHolder(viewHolder, itemList, position);
        }
        if (position + 1 == getItemCount()) {
            setBottomMargin(viewHolder.itemView, (int) (80 * Resources.getSystem().getDisplayMetrics().density));
        } else {
            setBottomMargin(viewHolder.itemView, 0);
        }

    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }

}
