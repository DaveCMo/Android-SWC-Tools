package com.swctools.activity_modules.player.recycler_adaptors;

import android.content.Context;
import android.content.res.Resources;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.swctools.interfaces.SendMessageFromList;
import com.swctools.common.view_adaptors.delegated_adaptors.AdaptorDelegate_Spacer;
import com.swctools.common.view_adaptors.delegated_adaptors.AdaptorDelegate_TwoTextItem;

import java.util.ArrayList;

public class RecyclerAdaptor_PlayerDetails extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TACTICALCAP = 0;
    private static final int DROIDEKA = 1;
    private static final int RESOURCES = 2;
    private static final int TRAPS = 3;
    private static final int SPACER = 4;
    private static final int TWOTEXT = 5;
    private static final int ARMOURY = 6;
    private static final int SC = 7;

    private static final String TAG = " TacticalCapacity";
    private static final String capTitleFormat = "%1$s (%2$s)";
    private ArrayList<Object> itemList;
    private Context mContext;
    private SendMessageFromList mcallBack;
    private int bottomMargin;
    private AdaptorDelegate_TacticalCap adaptorDelegateTacticalCap;
    private AdaptorDelegate_Armoury adaptorDelegate_armoury;
    private AdaptorDelegate_SC adaptorDelegate_sc;
    private AdaptorDelegate_Droideka adaptorDelegateDroideka;
    private AdaptorDelegate_Resource adaptorDelegateResource;
    private AdaptorDelegate_Trap adaptorDelegateTrap;
    private AdaptorDelegate_Spacer adaptorDelegateSpacer;
    private AdaptorDelegate_TwoTextItem adaptorDelegateTwoTextItem;

    public RecyclerAdaptor_PlayerDetails(ArrayList<Object> items, Context context) {
        this.itemList = items;
        this.mContext = context;
        this.mcallBack = (SendMessageFromList) context;
        this.adaptorDelegateTacticalCap = new AdaptorDelegate_TacticalCap(TACTICALCAP, mContext, itemList);
        this.adaptorDelegateDroideka = new AdaptorDelegate_Droideka(DROIDEKA, mContext);
        this.adaptorDelegateResource = new AdaptorDelegate_Resource(RESOURCES, mContext);
        this.adaptorDelegateTrap = new AdaptorDelegate_Trap(TRAPS, mContext);
        this.adaptorDelegateSpacer = new AdaptorDelegate_Spacer(SPACER, mContext);
        this.adaptorDelegateTwoTextItem = new AdaptorDelegate_TwoTextItem(TWOTEXT, mContext);
        this.adaptorDelegate_armoury = new AdaptorDelegate_Armoury(ARMOURY, mContext, itemList);
        this.adaptorDelegate_sc = new AdaptorDelegate_SC(SC, mContext, itemList);


        this.bottomMargin = 64 / (int) mContext.getResources().getDisplayMetrics().density;

    }

    public static void setBottomMargin(View view, int bottomMargin) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            params.setMargins(params.leftMargin, params.topMargin, params.rightMargin, bottomMargin);
            view.requestLayout();
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (adaptorDelegateTacticalCap.isForViewType(itemList, position)) {
            return adaptorDelegateTacticalCap.getViewType();
        } else if (adaptorDelegateDroideka.isForViewType(itemList, position)) {
            return adaptorDelegateDroideka.getViewType();
        } else if (adaptorDelegateResource.isForViewType(itemList, position)) {
            return adaptorDelegateResource.getViewType();
        } else if (adaptorDelegateTrap.isForViewType(itemList, position)) {
            return adaptorDelegateTrap.getViewType();
        } else if (adaptorDelegateSpacer.isForViewType(itemList, position)) {
            return adaptorDelegateSpacer.getViewType();
        } else if (adaptorDelegateTwoTextItem.isForViewType(itemList, position)) {
            return adaptorDelegateTwoTextItem.getViewType();
        } else if(adaptorDelegate_armoury.isForViewType(itemList, position)){
            return adaptorDelegate_armoury.getItemViewType();
        }else if(adaptorDelegate_sc.isForViewType(itemList, position)){
            return adaptorDelegate_sc.getItemViewType();
        }
        throw new IllegalArgumentException("No delegate found");
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (adaptorDelegateTacticalCap.getViewType() == viewType) {
            return adaptorDelegateTacticalCap.onCreateViewHolder(parent);
        } else if (adaptorDelegateDroideka.getItemViewType() == viewType) {
            return adaptorDelegateDroideka.onCreateViewHolder(parent);
        } else if (adaptorDelegateResource.getItemViewType() == viewType) {
            return adaptorDelegateResource.onCreateViewHolder(parent);
        } else if (adaptorDelegateTrap.getItemViewType() == viewType) {
            return adaptorDelegateTrap.onCreateViewHolder(parent);
        } else if (adaptorDelegateSpacer.getItemViewType() == viewType) {
            return adaptorDelegateSpacer.onCreateViewHolder(parent);
        } else if (adaptorDelegateTwoTextItem.getItemViewType() == viewType) {
            return adaptorDelegateTwoTextItem.onCreateViewHolder(parent);
        } else if(adaptorDelegate_sc.getItemViewType()==viewType){
            return adaptorDelegate_sc.onCreateViewHolder(parent);
        } else if(adaptorDelegate_armoury.getItemViewType()==viewType){
            return adaptorDelegate_armoury.onCreateViewHolder(parent);
        }

        throw new IllegalArgumentException("No delegate found");
//        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_player_tactical_capacity, parent, false);
//        return adaptorDelegateTacticalCap.onCreateViewHolder(parent);
//        return new RecyclerAdaptor_PlayerDetails.ViewHolder_DBUpgrade(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        int viewType = holder.getItemViewType();

        if (adaptorDelegateTacticalCap.getViewType() == viewType) {
            adaptorDelegateTacticalCap.onBindViewHolder(holder, itemList, position);
        } else if (adaptorDelegateDroideka.getItemViewType() == viewType) {
            adaptorDelegateDroideka.onBindViewHolder(holder, itemList, position);
        } else if (adaptorDelegateResource.getItemViewType() == viewType) {
            adaptorDelegateResource.onBindViewHolder(holder, itemList, position);
        } else if (adaptorDelegateTrap.getItemViewType() == viewType) {
            adaptorDelegateTrap.onBindViewHolder(holder, itemList, position);
        } else if (adaptorDelegateSpacer.getItemViewType() == viewType) {
            adaptorDelegateSpacer.onBindViewHolder(holder, itemList, position);
        } else if (adaptorDelegateTwoTextItem.getItemViewType() == viewType) {
            adaptorDelegateTwoTextItem.onBindViewHolder(holder, itemList, position);
        } else if(adaptorDelegate_armoury.getItemViewType()==viewType){
            adaptorDelegate_armoury.onBindViewHolder(holder, itemList, position);
        }else if(adaptorDelegate_sc.getItemViewType()==viewType){
            adaptorDelegate_sc.onBindViewHolder(holder,itemList,position);
        }

        if (position + 1 == getItemCount()) {
            setBottomMargin(holder.itemView, (int) (72 * Resources.getSystem().getDisplayMetrics().density));
        } else {
            setBottomMargin(holder.itemView, bottomMargin);
        }


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

    }


}
