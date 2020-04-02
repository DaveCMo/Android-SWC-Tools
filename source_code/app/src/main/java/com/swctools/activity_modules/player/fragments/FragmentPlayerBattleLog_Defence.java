package com.swctools.activity_modules.player.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.captain_miao.optroundcardview.OptRoundCardView;
import com.swctools.R;
import com.swctools.common.enums.BundleKeys;
import com.swctools.activity_modules.player.PlayerDetailsFragmentInterface;
import com.swctools.activity_modules.player.models.Battle;
import com.swctools.activity_modules.player.models.BattleLogs;
import com.swctools.activity_modules.player.recycler_adaptors.RecyclerAdaptor_Defence;

import java.util.ArrayList;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class FragmentPlayerBattleLog_Defence extends Fragment {
    private static final String TAG = "PlayerBattleLog_Defence";
    private static final String BATTLELOGLIST = "DEFENCELOGLIST";
    private static final String DEFENCEWINS = "DEFENCEWINS";
    private static final String DEFENCELOSSES = "DEFENCELOSSES";
    private TextView lossNo, winNo;
    private TextView attackLabel, defenceLabel;
    private RecyclerAdaptor_Defence mDefenceAdapter;
    private RecyclerView defenceRecyclerView;
    private SwipeRefreshLayout mainSwipeRefresh;
    private OptRoundCardView defenceCard, attackRdCard;
    private String playerId;
    protected int COLOR_GREEN;
    protected int COLOR_PRIMARY;
    protected int COLOR_PRIMARY_DARK;
    protected int COLOR_WHITE;
    private int lossInt, winInt;
    private BattleLogs battleLogs;


    private ArrayList<Battle> battleList = new ArrayList<>();
    private DefenceLogFragmentInterface mCallback;


    public static FragmentPlayerBattleLog_Defence newInstance() {
        return new FragmentPlayerBattleLog_Defence();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        try {
            outState.putParcelableArrayList(BATTLELOGLIST, battleList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            outState.putInt(DEFENCEWINS, this.winInt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            outState.putInt(DEFENCELOSSES, this.lossInt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static FragmentPlayerBattleLog_Defence getInstance(FragmentManager fragmentManager) {
        FragmentPlayerBattleLog_Defence fragmentPlayerBattleLogDefence = (FragmentPlayerBattleLog_Defence) fragmentManager.findFragmentByTag(TAG);
        if (fragmentPlayerBattleLogDefence == null) {
            fragmentPlayerBattleLogDefence = new FragmentPlayerBattleLog_Defence();
            fragmentManager.beginTransaction().add(fragmentPlayerBattleLogDefence, TAG).commit();
        }
        return fragmentPlayerBattleLogDefence;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback = (DefenceLogFragmentInterface) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // Clear reference to host Activity.
        mCallback = null;

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        playerId = getArguments().getString(BundleKeys.PLAYER_ID.toString());
        battleList = getArguments().getParcelableArrayList(BATTLELOGLIST);
        if (battleList != null) {

        } else {

        }

        View view = inflater.inflate(R.layout.fragment_battle_log, container, false);
        setViewItems(view);

        if (savedInstanceState != null) {
            this.battleList = savedInstanceState.getParcelableArrayList(BATTLELOGLIST);
            this.lossInt = savedInstanceState.getInt(DEFENCELOSSES);
            this.winInt = savedInstanceState.getInt(DEFENCEWINS);
            setDefenceRecyclerView();
        }
        return view;
    }

    public void setViewItems(View view) {

        COLOR_GREEN = view.getResources().getColor(R.color.green);
        COLOR_PRIMARY = view.getResources().getColor(R.color.colorPrimary);
        COLOR_PRIMARY_DARK = view.getResources().getColor(R.color.colorPrimaryDark);
        COLOR_WHITE = view.getResources().getColor(R.color.white);
        winNo = (TextView) view.findViewById(R.id.winNo);
        lossNo = (TextView) view.findViewById(R.id.lossNo);
        defenceRecyclerView = (RecyclerView) view.findViewById(R.id.home_defence_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        defenceRecyclerView.setLayoutManager(mLayoutManager);
        defenceRecyclerView.setItemAnimator(new DefaultItemAnimator());
        defenceRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        mainSwipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.defenceLog_SwipeRefresh);
        mainSwipeRefresh.setOnRefreshListener(new SwipeRefreshListener());


    }


    public void processBattleLog(String playerId, BattleLogs battleLogs) {
        this.battleLogs = battleLogs;
        this.winInt = this.battleLogs.getWins();
        this.lossInt = this.battleLogs.getLosses();

        battleList = new ArrayList<>();
        battleList.clear();


        for (Map.Entry<Long, Battle> entry : battleLogs.getDefenceLogs().entrySet()) {

            Battle value = entry.getValue();
            battleList.add(value);

        }
        setDefenceRecyclerView();

    }

    private void setDefenceRecyclerView() {
        mDefenceAdapter = new RecyclerAdaptor_Defence(battleList, getActivity());


        defenceRecyclerView.setAdapter(mDefenceAdapter);
        mDefenceAdapter.notifyDataSetChanged();
        lossNo.setText(String.valueOf(lossInt));
        winNo.setText(String.valueOf(winInt));
    }

    public interface DefenceLogFragmentInterface {
        void sendCommand(String command);
    }

    public class SwipeRefreshListener implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            mCallback.sendCommand(PlayerDetailsFragmentInterface.COMMANDS.REFRESH_ME);
            mainSwipeRefresh.setRefreshing(false);
        }
    }


}
