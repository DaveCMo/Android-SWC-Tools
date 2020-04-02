package com.swctools.common.view_adaptors.delegated_adaptors;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swctools.R;
import com.swctools.layouts.models.LayoutDetailVersionsData;
import com.swctools.activity_modules.layout_detail.view_adaptors.RecyclerAdaptor_LayoutVersion;
import com.swctools.common.view_adaptors.view_holders.ViewHolder_LayoutDetailVersions;

import java.util.ArrayList;

public class AdaptorDelegate_LayoutDetailVersions {
    private static final String TAG = "LayoutDetailTagsData";

    private int viewType;
    private Context mContext;

    private RecyclerAdaptor_LayoutVersion recyclerAdaptorLayoutVersion;

    public AdaptorDelegate_LayoutDetailVersions(int viewType, Context context) {
        this.viewType = viewType;
        this.mContext = context;
    }

    public int getViewType() {
        return this.viewType;
    }

    public int getItemViewType() {
        return this.viewType;
    }

    public boolean isForViewType(ArrayList<Object> items, int position) {

        return items.get(position) instanceof LayoutDetailVersionsData;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_recycler_view, parent, false);
        return new ViewHolder_LayoutDetailVersions(itemView);
    }

    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder tholder, ArrayList<Object> itemList, int position) {
        final ViewHolder_LayoutDetailVersions viewHolder = (ViewHolder_LayoutDetailVersions) tholder;
        final LayoutDetailVersionsData itemData = (LayoutDetailVersionsData) itemList.get(position);
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, true);


        recyclerAdaptorLayoutVersion = new RecyclerAdaptor_LayoutVersion(itemData.getLayoutVersions(), mContext);

        viewHolder.listItemRecycler.setHasFixedSize(true);
        viewHolder.listItemRecycler.setLayoutManager(linearLayoutManager);
        viewHolder.listItemRecycler.setItemAnimator(new DefaultItemAnimator());
        viewHolder.listItemRecycler.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));
        viewHolder.listItemRecycler.setAdapter(recyclerAdaptorLayoutVersion);
        recyclerAdaptorLayoutVersion.notifyDataSetChanged();


    }

}
