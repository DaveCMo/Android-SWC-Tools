package com.swctools.activity_modules.layout_manager.recycler_adaptors;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swctools.R;
import com.swctools.interfaces.LayoutTagListPillInterface;
import com.swctools.layouts.models.LayoutTag;
import com.swctools.common.view_adaptors.view_holders.ViewHolder_LayoutTagList;

import java.util.ArrayList;

public class RecyclerAdaptor_LayoutTagPillList extends RecyclerView.Adapter<ViewHolder_LayoutTagList> {
    private static final String TAG = "LayoutTagListVAdaptor";
    private ArrayList<LayoutTag> listItems;
    private boolean showX;
    private LayoutTagListPillInterface mActivityCallBack;
    private Context mContext;

    public RecyclerAdaptor_LayoutTagPillList(ArrayList<LayoutTag> listItems, boolean showX, Context context) {
        this.showX = showX;
        this.listItems = listItems;
        this.mContext = context;
        mActivityCallBack = (LayoutTagListPillInterface) mContext;

    }

    @NonNull
    @Override
    public ViewHolder_LayoutTagList onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_tag_pill, viewGroup, false);
        return new ViewHolder_LayoutTagList(v, this.showX);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder_LayoutTagList viewHolderLayoutTagList, final int i) {
        final LayoutTag layoutTag = listItems.get(i);
        viewHolderLayoutTagList.tag_txt.setText(layoutTag.tagString);
        viewHolderLayoutTagList.layoutTagPillCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showX) {
                    mActivityCallBack.removeTag(layoutTag, i);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }
}

