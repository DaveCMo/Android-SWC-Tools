package com.swctools.activity_modules.main.fagments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swctools.R;
import com.swctools.layouts.models.FavouriteLayoutItem;
import com.swctools.activity_modules.main.models.PlayerDAO_WithLayouts;
import com.swctools.activity_modules.main.recycler_adaptors.RecyclerAdaptor_PlayerList;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FragmentHome extends Fragment {
    private static final String TAG = "FragmentHome";
    private Context mContext;
    private RecyclerView playerListRecyler;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home_screen, container, false);

        playerListRecyler = (RecyclerView) view.findViewById(R.id.playerListRecyler);
        mLayoutManager = new LinearLayoutManager(mContext);
        playerListRecyler.setLayoutManager(mLayoutManager);
        playerListRecyler.setHasFixedSize(true);
        playerListRecyler.setItemAnimator(new DefaultItemAnimator());


        return view;
    }

    public void setPlayerListRecyler(List<PlayerDAO_WithLayouts> playerDAOList, List<FavouriteLayoutItem> favouriteLayoutItemList) {

        mAdapter = new RecyclerAdaptor_PlayerList(playerDAOList, favouriteLayoutItemList, mContext);
        playerListRecyler.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        playerListRecyler.smoothScrollToPosition(0);

    }


    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }



}
