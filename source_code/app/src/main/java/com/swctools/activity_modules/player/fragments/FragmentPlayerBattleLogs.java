package com.swctools.activity_modules.player.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.swctools.R;
import com.swctools.common.enums.BundleKeys;
import com.swctools.activity_modules.player.PlayerDetailsFragmentInterface;
import com.swctools.activity_modules.player.models.BattleLogs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

public class FragmentPlayerBattleLogs extends Fragment {
    private static final String TAG = "FragmentPlayerBattleLogs";
    private BattleLog_SectionStatePagerAdapter mSectionsStatePagerAdapter;

    private String playerId;
    private PlayerDetailsFragmentInterface mCallback;
    private ViewPager battleLog_ViewPager;


    public static FragmentPlayerBattleLogs newInstance() {
        return new FragmentPlayerBattleLogs();
    }

    public static FragmentPlayerBattleLogs getInstance(FragmentManager fragmentManager, String playerId) {
        FragmentPlayerBattleLogs FragmentPlayerBattleLogs = (FragmentPlayerBattleLogs) fragmentManager.findFragmentByTag(TAG);
        if (FragmentPlayerBattleLogs == null) {
            ;
            FragmentPlayerBattleLogs = new FragmentPlayerBattleLogs();
            Bundle args = new Bundle();
            args.putString(BundleKeys.PLAYER_ID.toString(), playerId);
            FragmentPlayerBattleLogs.setArguments(args);
            fragmentManager.beginTransaction().add(FragmentPlayerBattleLogs, TAG).commit();
        }
        return FragmentPlayerBattleLogs;
    }

    @Override
    public void onResume() {
        super.onResume();
//        mCallback.getPlayerModel(false, true, false);
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setRetainInstance(true);

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        playerId = getArguments().getString(BundleKeys.PLAYER_ID.toString());
        View view = inflater.inflate(R.layout.fragment_battlelog_container, container, false);
        battleLog_ViewPager = (ViewPager) view.findViewById(R.id.battleLog_ViewPager);
        TabLayout battleLogTabs = (TabLayout) view.findViewById(R.id.battleLogTabs);
//        getChildFragmentManager()
        mSectionsStatePagerAdapter = new BattleLog_SectionStatePagerAdapter(getChildFragmentManager(), playerId);
        battleLog_ViewPager.setAdapter(mSectionsStatePagerAdapter);
        battleLogTabs.setupWithViewPager(battleLog_ViewPager);

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback = (PlayerDetailsFragmentInterface) context;

    }

    @Override
    public void onDetach() {
        super.onDetach();
        // Clear reference to host Activity.
        mCallback = null;

    }

    public void refreshData(String playerId, BattleLogs battleLogs) {

        this.playerId = playerId;
        mSectionsStatePagerAdapter.refreshData(playerId, battleLogs);
    }
}
