package com.swctools.activity_modules.war_battles.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swctools.R;
import com.swctools.activity_modules.war_battles.models.WarBattleListProvider;
import com.swctools.activity_modules.war_battles.models.WarSummaryItem;
import com.swctools.activity_modules.war_battles.view_adaptors.RecyclerAdaptor_WarSummary;
import com.swctools.common.enums.BundleKeys;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Fragment_WarAttacksSummary extends Fragment {
    private static final String TAG = "Fragment_WarAttacksSumm";

    private String playerId;
    private String warId;
    private String guildId;
    private ArrayList<WarSummaryItem> warSummaryItems;
    private RecyclerView recyclerView;

    private Fragment_WarAttacksSummary() {
        setRetainInstance(true);
    }

    public static Fragment_WarAttacksSummary newInstance() {
        return new Fragment_WarAttacksSummary();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_war_summary, container, false);

        playerId = getArguments().getString(BundleKeys.PLAYER_ID.toString());
        guildId = getArguments().getString(BundleKeys.GUILD_ID.toString());
        warId = getArguments().getString(BundleKeys.WAR_ID.toString());
        warSummaryItems = WarBattleListProvider.getWarSummary(warId, guildId, getContext());

        recyclerView = view.findViewById(R.id.warAttacks_SummaryRecycler);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());// GridLayoutManager(mContext, 2, GridLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        RecyclerAdaptor_WarSummary recyclerAdaptor_warSummary = new RecyclerAdaptor_WarSummary(warSummaryItems);
        recyclerView.setAdapter(recyclerAdaptor_warSummary);
        recyclerAdaptor_warSummary.notifyDataSetChanged();

        return view;
    }
}
