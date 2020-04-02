package com.swctools.activity_modules.player.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.swctools.R;
import com.swctools.common.enums.BundleKeys;
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

public class FragmentPlayerBattleLog_Attack extends Fragment {
    private static final String TAG = "PlayerBattleLog_Attack";
    private static final String BATTLELOGLIST = "ATTACKLOGLIST";
    private static final String ATTACKWINS = "ATTACKWINS";
    private static final String ATTACKLOSSES = "ATTACKLOSSES";
    //Defence log:
    private TextView lossNo, winNo;
    private RecyclerAdaptor_Defence mDefenceAdapter;
    private RecyclerView defenceRecyclerView;
    private SwipeRefreshLayout mainSwipeRefresh;
    private String playerId;
    protected int COLOR_GREEN;
    protected int COLOR_PRIMARY;
    protected int COLOR_PRIMARY_DARK;
    protected int COLOR_WHITE;
    private int lossInt, winInt;
    private BattleLogs battleLogs;

    private ArrayList<Battle> battleList = new ArrayList<>();


    public static FragmentPlayerBattleLog_Attack newInstance() {
        return new FragmentPlayerBattleLog_Attack();
    }


    public static FragmentPlayerBattleLog_Attack getInstance(FragmentManager fragmentManager, String playerId) {
        FragmentPlayerBattleLog_Attack fragmentPlayerBattleLogDefence = (FragmentPlayerBattleLog_Attack) fragmentManager.findFragmentByTag(TAG);
        if (fragmentPlayerBattleLogDefence == null) {
            ;
            fragmentPlayerBattleLogDefence = new FragmentPlayerBattleLog_Attack();
            Bundle args = new Bundle();
            args.putString(BundleKeys.PLAYER_ID.toString(), playerId);
            fragmentPlayerBattleLogDefence.setArguments(args);
            fragmentManager.beginTransaction().add(fragmentPlayerBattleLogDefence, TAG).commit();
        }
        return fragmentPlayerBattleLogDefence;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        mCallback = (DefenceLogFragmentInterface) context;
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
            outState.putInt(ATTACKWINS, this.winInt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            outState.putInt(ATTACKLOSSES, this.lossInt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setRetainInstance(true);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        playerId = getArguments().getString(BundleKeys.PLAYER_ID.toString());
        View view = inflater.inflate(R.layout.fragment_battle_log, container, false);
        setViewItems(view);
        if (savedInstanceState != null) {
            battleList = savedInstanceState.getParcelableArrayList(BATTLELOGLIST);
            lossInt = savedInstanceState.getInt(ATTACKLOSSES);
            winInt = savedInstanceState.getInt(ATTACKWINS);
            setDefenceRecyclerView();
        }
        return view;
    }

    public void setViewItems(View view) {
//        playerDetails_battlelog_pager = (ViewPager) view.findViewById(R.id.playerDetails_battlelog_pager);
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

    @Override
    public void onResume() {
        super.onResume();
    }

    public void processBattleLog(String playerId, BattleLogs battleLogs) {
        this.battleLogs = battleLogs;
        this.winInt = this.battleLogs.getAttWins();
        this.lossInt = this.battleLogs.getAttLosses();
        battleList = new ArrayList<>();
        battleList.clear();


        for (Map.Entry<Long, Battle> entry : battleLogs.getAttackLogs().entrySet()) {

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
//            mCallback.sendCommand(PlayerDetailsFragmentInterface.COMMANDS.REFRESH_ME);
//            mainSwipeRefresh.setRefreshing(false);
        }
    }


}
