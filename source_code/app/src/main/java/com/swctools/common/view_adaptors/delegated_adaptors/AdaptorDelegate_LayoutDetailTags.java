package com.swctools.common.view_adaptors.delegated_adaptors;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swctools.R;
import com.swctools.activity_modules.layout_manager.recycler_adaptors.RecyclerAdaptor_LayoutTagPillList;
import com.swctools.layouts.models.LayoutDetailTagsData;
import com.swctools.common.view_adaptors.view_holders.ViewHolder_LayoutDetailTag;

import java.util.ArrayList;

public class AdaptorDelegate_LayoutDetailTags {
    private static final String TAG = "LayoutDetailTagsData";

    private int viewType;
    private Context mContext;

    private RecyclerAdaptor_LayoutTagPillList recyclerAdaptorLayoutTagPillList;
    private LayoutTagDelegateInterface mActivityCallBack;

    public AdaptorDelegate_LayoutDetailTags(int viewType, Context context) {
        this.viewType = viewType;
        this.mContext = context;
        this.mActivityCallBack = (LayoutTagDelegateInterface) mContext;
    }

    public int getViewType() {
        return this.viewType;
    }

    public int getItemViewType() {
        return this.viewType;
    }

    public boolean isForViewType(ArrayList<Object> items, int position) {

        return items.get(position) instanceof LayoutDetailTagsData;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout_detail_tags, parent, false);
        return new ViewHolder_LayoutDetailTag(itemView);
    }

    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder tholder, ArrayList<Object> itemList, int position) {
        final ViewHolder_LayoutDetailTag viewHolder = (ViewHolder_LayoutDetailTag) tholder;
        final LayoutDetailTagsData itemData = (LayoutDetailTagsData) itemList.get(position);


        recyclerAdaptorLayoutTagPillList = new RecyclerAdaptor_LayoutTagPillList(itemData.getLayoutTagArrayList(), false, mContext);
//
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
//
        viewHolder.layoutTagRecycler.setLayoutManager(mLayoutManager);
//        playerDetailsCapRecycler.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));
        viewHolder.layoutTagRecycler.setItemAnimator(new DefaultItemAnimator());
        viewHolder.layoutTagRecycler.setAdapter(recyclerAdaptorLayoutTagPillList);
        recyclerAdaptorLayoutTagPillList.notifyDataSetChanged();


        viewHolder.imgTagEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivityCallBack.editTags();
            }
        });
    }

    public interface LayoutTagDelegateInterface {
        void editTags();
    }

}
