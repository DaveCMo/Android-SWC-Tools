package com.swctools.activity_modules.war_room.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swctools.R;
import com.swctools.activity_modules.war_room.models.War_WarParticipant;
import com.swctools.common.enums.BundleKeys;
import com.swctools.activity_modules.war_room.processing_models.WarRoomData_WarSquadModel;
import com.swctools.activity_modules.war_room.recycler_adaptors.RecyclerAdaptor_WarParticipant;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FragmentWarSquad extends Fragment {
    private static final String TAG = "FragmentWarSquad";
    private static final String KEY_warSquadModel = "KEY_warSquadModel";

    private RecyclerView warSquadParty_Recycler;
    private Context mContext;
    private String playerId;
    private WarRoomData_WarSquadModel warSquadModel;
    private RecyclerAdaptor_WarParticipant adaptor;
    private LinearLayoutManager mLayoutManager;
    private ArrayList<War_WarParticipant> war_warParticipants;

    private FragmentWarSquad(ArrayList<War_WarParticipant> war_warParticipants) {
        this.war_warParticipants = war_warParticipants;
    }

    public static FragmentWarSquad newInstance(ArrayList<War_WarParticipant> war_warParticipants) {
        return new FragmentWarSquad(war_warParticipants);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mContext = null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (playerId != null) {
            outState.putString(BundleKeys.PLAYER_ID.toString(), playerId);
        }
        if (warSquadModel != null) {
            outState.putParcelable(KEY_warSquadModel, warSquadModel);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.war_dashboard_fragment_squad, container, false);
        setViews(view);
        adaptor = new RecyclerAdaptor_WarParticipant(war_warParticipants, true, mContext);
        warSquadParty_Recycler.setAdapter(adaptor);

        return view;
    }

    public void updateSquadStatus(String playerId, ArrayList<War_WarParticipant> war_warParticipants) {
        this.playerId = playerId;
        getArguments().putString(BundleKeys.PLAYER_ID.toString(), playerId);
        this.warSquadModel = warSquadModel;
        try {
            adaptor = new RecyclerAdaptor_WarParticipant(war_warParticipants, true, mContext);
            warSquadParty_Recycler.setAdapter(adaptor);
            adaptor.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setViews(View view) {
        warSquadParty_Recycler = view.findViewById(R.id.warSquadParty_Recycler);
        mLayoutManager = new LinearLayoutManager(mContext);// GridLayoutManager(mContext, 2, GridLayoutManager.HORIZONTAL, false);
        warSquadParty_Recycler.setLayoutManager(mLayoutManager);
        warSquadParty_Recycler.setItemAnimator(new DefaultItemAnimator());
    }


}
