package com.swctools.activity_modules.war_room.recycler_adaptors;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;

import com.swctools.activity_modules.war_room.models.War_WarParticipant;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdaptor_WarParticipant extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "RecyclerAdaptor_WarParticipant";
    private static final int PLAYERPARTICIPANT = 1;
    private static final int MEMBERPARTICIPANT = 2;

    private AdaptorDelegate_WarParticipant_Mmbr warPartMembDelegate;
    private AdaptorDelegate_WarPlayer adaptorDelegateWarPlayer;
    private ArrayList<War_WarParticipant> listItems;
    private Context mContext;
    int bottomMargin;
    private boolean mySquad;

    public RecyclerAdaptor_WarParticipant(ArrayList<War_WarParticipant> listItems, boolean mySquad, Context mContext) {
        this.listItems = listItems;
        this.mContext = mContext;
        this.mySquad = mySquad;

        warPartMembDelegate = new AdaptorDelegate_WarParticipant_Mmbr(MEMBERPARTICIPANT, mySquad, mContext);
        adaptorDelegateWarPlayer = new AdaptorDelegate_WarPlayer(PLAYERPARTICIPANT, mContext);
        this.bottomMargin = 32 / (int) mContext.getResources().getDisplayMetrics().density;

    }

    public static void setBottomMargin(View view, int bottomMargin) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            params.setMargins(params.leftMargin, params.topMargin, params.rightMargin, bottomMargin);
            view.requestLayout();
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        if (warPartMembDelegate.getViewType() == i) {
            return warPartMembDelegate.onCreateViewHolder(parent);
        } else if (adaptorDelegateWarPlayer.getViewType() == i) {
            return adaptorDelegateWarPlayer.onCreateViewHolder(parent);
        }
        throw new IllegalArgumentException("No delegate found");
    }

    @Override
    public int getItemViewType(int position) {
        if (adaptorDelegateWarPlayer.isForViewType(listItems, position)) {
            return adaptorDelegateWarPlayer.getViewType();
        } else if (warPartMembDelegate.isForViewType(listItems, position)) {
            return warPartMembDelegate.getViewType();
        } else {
            return warPartMembDelegate.getViewType();
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        int viewType = holder.getItemViewType();
        if (warPartMembDelegate.getViewType() == viewType) {
            warPartMembDelegate.onBindViewHolder(holder, listItems, position);
        } else if (adaptorDelegateWarPlayer.getViewType() == viewType) {
            adaptorDelegateWarPlayer.onBindViewHolder(holder, listItems, position);
        }


        if (position + 1 == getItemCount()) {
            setBottomMargin(holder.itemView, (int) (72 * Resources.getSystem().getDisplayMetrics().density));
        } else {
            setBottomMargin(holder.itemView, bottomMargin);
        }
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }
}
