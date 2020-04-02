package com.swctools.activity_modules.player.recycler_adaptors;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.ViewGroup;

import com.swctools.common.view_adaptors.delegated_adaptors.AdaptorDelegate_CompactTwoTextItem;
import com.swctools.common.view_adaptors.delegated_adaptors.AdaptorDelegate_SpacerNoPadding;

import java.util.ArrayList;

public class RecyclerAdaptor_BattleDetail extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String TAG = "BattleDetailAdaptor";
    private static final int TACTICALCAP = 0;
    public static final int SPACER = 4;
    public static final int TWOTEXT = 5;
    public static final int STATTWOTEXT = 6;

    private ArrayList<Object> itemList;
    private Context mContext;
    private AdaptorDelegate_TacticalCap adaptorDelegateTacticalCap;
    private AdaptorDelegate_SpacerNoPadding spacerAdaptorDelegate;
    private AdaptorDelegate_CompactTwoTextItem twoTextItemAdaptorDelegate;
    private AdaptorDelegate_StatItem adaptorDelegateStatItem;


    public RecyclerAdaptor_BattleDetail(ArrayList<Object> itemList, Context context) {
        this.mContext = context;
        this.itemList = itemList;
        this.spacerAdaptorDelegate = new AdaptorDelegate_SpacerNoPadding(SPACER, mContext);
        this.twoTextItemAdaptorDelegate = new AdaptorDelegate_CompactTwoTextItem(TWOTEXT, mContext);
        this.adaptorDelegateStatItem = new AdaptorDelegate_StatItem(STATTWOTEXT, mContext);
        this.adaptorDelegateTacticalCap = new AdaptorDelegate_TacticalCap(TACTICALCAP, mContext, itemList);

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (spacerAdaptorDelegate.getItemViewType() == viewType) {
            return spacerAdaptorDelegate.onCreateViewHolder(parent);
        } else if (twoTextItemAdaptorDelegate.getItemViewType() == viewType) {
            return twoTextItemAdaptorDelegate.onCreateViewHolder(parent);
        } else if (adaptorDelegateStatItem.getItemViewType() == viewType) {
            return adaptorDelegateStatItem.onCreateViewHolder(parent);
        }

        throw new IllegalArgumentException("No delegate found");
    }


    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        int viewType = holder.getItemViewType();

        if (spacerAdaptorDelegate.getItemViewType() == viewType) {
            spacerAdaptorDelegate.onBindViewHolder(holder, itemList, position);
        } else if (twoTextItemAdaptorDelegate.getItemViewType() == viewType) {
            twoTextItemAdaptorDelegate.onBindViewHolder(holder, itemList, position);
        } else if (adaptorDelegateStatItem.getItemViewType() == viewType) {
            adaptorDelegateStatItem.onBindViewHolder(holder, itemList, position);
        }


    }

    @Override
    public int getItemViewType(int position) {
        if (adaptorDelegateTacticalCap.isForViewType(itemList, position)) {
            return adaptorDelegateTacticalCap.getViewType();
        } else if (spacerAdaptorDelegate.isForViewType(itemList, position)) {
            return spacerAdaptorDelegate.getItemViewType();
        } else if (twoTextItemAdaptorDelegate.isForViewType(itemList, position)) {
            return twoTextItemAdaptorDelegate.getItemViewType();
        } else if (adaptorDelegateStatItem.isForViewType(itemList, position)) {
            return adaptorDelegateStatItem.getItemViewType();
        }
        throw new IllegalArgumentException("No delegate found");
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
