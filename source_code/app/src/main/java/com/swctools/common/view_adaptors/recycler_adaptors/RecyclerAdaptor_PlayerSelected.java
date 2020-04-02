package com.swctools.common.view_adaptors.recycler_adaptors;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.swctools.R;
import com.swctools.common.helpers.ImageHelpers;
import com.swctools.interfaces.PlayerTopLayoutSelectionInterface;
import com.swctools.layouts.models.PlayerTopSelectedModel;
import com.swctools.util.StringUtil;
import com.swctools.common.view_adaptors.view_holders.ViewHolder_PlayerSelected;

import java.util.ArrayList;

public class RecyclerAdaptor_PlayerSelected extends RecyclerView.Adapter<ViewHolder_PlayerSelected> {
    private static final String TAG = "RecyAdpr_PyrSelected";
    private ArrayList<PlayerTopSelectedModel> itemList;
    private Context mContext;
    private PlayerTopLayoutSelectionInterface playerTopLayoutSelectionInterface;

    public RecyclerAdaptor_PlayerSelected(ArrayList<PlayerTopSelectedModel> itemList, Context context) {
        this.itemList = itemList;
        this.mContext = context;
        playerTopLayoutSelectionInterface = (PlayerTopLayoutSelectionInterface) context;
    }

    @NonNull
    @Override
    public ViewHolder_PlayerSelected onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout_top_selection, parent, false);
        return new ViewHolder_PlayerSelected(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder_PlayerSelected holder, int position) {
        final PlayerTopSelectedModel playerTopSelectedModel = itemList.get(position);
        holder.playerName.setText(StringUtil.getHtmlForTxtBox(playerTopSelectedModel.playerName));
        holder.playerSelected.setChecked(playerTopSelectedModel.selected);
        holder.playerFactionIcon.setImageDrawable(ImageHelpers.factionIcon(playerTopSelectedModel.playerFaction, mContext));
        holder.playerSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                playerTopLayoutSelectionInterface.playerSelected(playerTopSelectedModel.playerId, b);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


}
