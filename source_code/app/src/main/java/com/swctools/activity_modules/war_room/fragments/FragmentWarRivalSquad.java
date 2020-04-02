package com.swctools.activity_modules.war_room.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swctools.R;
import com.swctools.activity_modules.war_room.models.War_WarParticipant;
import com.swctools.activity_modules.war_room.recycler_adaptors.RecyclerAdaptor_WarParticipant;
import com.swctools.common.enums.BundleKeys;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FragmentWarRivalSquad extends Fragment {
    private static final String TAG = "FragmentWarRivalSquad";
    private static final String WARSQUADMODEL = "WARSQUADMODEL";
    private RecyclerView warSquadParty_Recycler;
    private Context mContext;
    private String playerId;
    private ArrayList<War_WarParticipant> war_warParticipants;


    private FragmentWarRivalSquad(ArrayList<War_WarParticipant> war_warParticipants) {
        this.war_warParticipants = war_warParticipants;
    }

    public static FragmentWarRivalSquad newInstance(ArrayList<War_WarParticipant> war_warParticipants) {
        return new FragmentWarRivalSquad(war_warParticipants);
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

       /* try {
            outState.putParcelable(WARSQUADMODEL, warSquadModel);
            outState.putString(BundleKeys.PLAYER_ID.toString(), playerId);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        View view = inflater.inflate(R.layout.war_dashboard_fragment_squad, container, false);

        setViews(view);
        RecyclerAdaptor_WarParticipant adaptor = new RecyclerAdaptor_WarParticipant(war_warParticipants, false, mContext);


        warSquadParty_Recycler.setAdapter(adaptor);
        adaptor.notifyDataSetChanged();

        return view;
    }

    public void updateSquadStatus(String playerId, ArrayList<War_WarParticipant> war_warParticipants) {

        this.playerId = playerId;
        getArguments().putString(BundleKeys.PLAYER_ID.toString(), playerId);

        RecyclerAdaptor_WarParticipant adaptor = new RecyclerAdaptor_WarParticipant(war_warParticipants, false, mContext);


        warSquadParty_Recycler.setAdapter(adaptor);
        adaptor.notifyDataSetChanged();

    }

    private void setViews(View view) {
        warSquadParty_Recycler = view.findViewById(R.id.warSquadParty_Recycler);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);// GridLayoutManager(mContext, 2, GridLayoutManager.HORIZONTAL, false);
        warSquadParty_Recycler.setLayoutManager(mLayoutManager);
        warSquadParty_Recycler.setItemAnimator(new DefaultItemAnimator());
    }


}
