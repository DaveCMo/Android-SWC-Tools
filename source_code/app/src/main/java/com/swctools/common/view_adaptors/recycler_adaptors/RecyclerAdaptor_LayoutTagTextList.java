package com.swctools.common.view_adaptors.recycler_adaptors;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swctools.R;
import com.swctools.interfaces.LayoutTagListInterface;
import com.swctools.layouts.models.LayoutTag;
import com.swctools.common.view_adaptors.view_holders.ViewHolder_TwoTextNonBold;

import java.util.ArrayList;

public class RecyclerAdaptor_LayoutTagTextList extends RecyclerView.Adapter<ViewHolder_TwoTextNonBold> {
    private static final String TAG = "LayoutTagTextListVA";
    private ArrayList<LayoutTag> listItems;
    private Context mContext;
    private LayoutTagListInterface mCallBack;


    public RecyclerAdaptor_LayoutTagTextList(ArrayList<LayoutTag> listItems, Context context) {
        this.listItems = listItems;
        this.mContext = context;
        this.mCallBack = (LayoutTagListInterface) mContext;
    }

    @NonNull
    @Override
    public ViewHolder_TwoTextNonBold onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_2_text_group_item_nonbold, viewGroup, false);
        return new ViewHolder_TwoTextNonBold(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder_TwoTextNonBold layoutTagListViewHolder, final int i) {
        final LayoutTag layoutTag = listItems.get(i);
        layoutTagListViewHolder.list_item_group_3_left.setText(layoutTag.tagString);
        layoutTagListViewHolder.twoTextLayoutContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallBack.selectTag(layoutTag, i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }
}

