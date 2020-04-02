package com.swctools.activity_modules.player.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import android.view.ViewGroup;

import com.swctools.common.enums.BundleKeys;
import com.swctools.activity_modules.player.models.BattleLogs;

public class BattleLog_SectionStatePagerAdapter extends FragmentStatePagerAdapter {
    private static final String TAG = "BLSectionStatPager";
    private String playerId;
    private Fragment[] mFragments = {};
    private long baseId = 0;
    private FragmentPlayerBattleLog_Attack mFragmentPlayerBattleLog_attack;
    private FragmentPlayerBattleLog_Defence mFragmentPlayerBattleLogDefence;
    private FragmentManager fm;

    public BattleLog_SectionStatePagerAdapter(FragmentManager fm, String playerId) {
        super(fm);
        this.fm = fm;

        this.playerId = playerId;

    }


    @Override
    public Fragment getItem(int position) {
        Bundle args = new Bundle();
        args.putString(BundleKeys.PLAYER_ID.toString(), playerId);
        switch (position) {
            case 0:
                mFragmentPlayerBattleLogDefence = FragmentPlayerBattleLog_Defence.newInstance();
                mFragmentPlayerBattleLogDefence.setArguments(args);
                return mFragmentPlayerBattleLogDefence;
            case 1:
                mFragmentPlayerBattleLog_attack = FragmentPlayerBattleLog_Attack.newInstance();
                mFragmentPlayerBattleLog_attack.setArguments(args);
                return mFragmentPlayerBattleLog_attack;
            default:
                return null;

        }

    }

    @Override
    public int getCount() {
        return 2;
    }


    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Defences";
            case 1:
                return "Attacks";

        }
        return "";//mFragmentTitleList.get(position);
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
        // get the tags set by FragmentPagerAdapter

        switch (position) {
            case 0:
                mFragmentPlayerBattleLogDefence = (FragmentPlayerBattleLog_Defence) createdFragment;
                break;
            case 1:
                mFragmentPlayerBattleLog_attack = (FragmentPlayerBattleLog_Attack) createdFragment;
                break;
        }
        return createdFragment;
    }

    public void refreshData(String playerId, BattleLogs battleLogs) {

        this.playerId = playerId;
        mFragmentPlayerBattleLogDefence.processBattleLog(playerId, battleLogs);
        mFragmentPlayerBattleLog_attack.processBattleLog(playerId, battleLogs);
    }
}