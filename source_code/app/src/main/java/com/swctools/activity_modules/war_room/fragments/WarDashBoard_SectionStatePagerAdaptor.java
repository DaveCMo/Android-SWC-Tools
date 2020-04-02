package com.swctools.activity_modules.war_room.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;

import com.swctools.activity_modules.war_room.models.War_WarParticipant;
import com.swctools.common.enums.BundleKeys;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

public class WarDashBoard_SectionStatePagerAdaptor extends FragmentStatePagerAdapter {
    private static final String TAG = "WDSectionStatPager";
    private String playerId;

    private long baseId = 0;
    private FragmentWarSquad mFragmentWarSquad;
    private FragmentWarRivalSquad mFragmentWarRivalSquad;
    private FragmentWarSignup mFragmentWarSignup;
    private ArrayList<War_WarParticipant> guildParticipants = new ArrayList<>();
    private ArrayList<War_WarParticipant> rivalParticipants = new ArrayList<>();
    public WarDashBoard_SectionStatePagerAdaptor(FragmentManager fm, String playerId) {
        super(fm);
        this.playerId = playerId;


    }

    public void updateHitList(String hitList) {
        mFragmentWarSignup.setHitList(hitList);
    }


    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public void updateMySquadPage(String playerId, ArrayList<War_WarParticipant> war_warParticipants) {
        mFragmentWarSquad.updateSquadStatus(playerId, war_warParticipants);
        guildParticipants.clear();
        guildParticipants.addAll(war_warParticipants);
    }

    public void updateRivalSquadPage(String playerId, ArrayList<War_WarParticipant> war_warParticipants) {
        mFragmentWarRivalSquad.updateSquadStatus(playerId, war_warParticipants);
        rivalParticipants.clear();
        rivalParticipants.addAll(war_warParticipants);
    }

    @Override
    public Fragment getItem(int position) {
        Bundle args = new Bundle();
        args.putString(BundleKeys.PLAYER_ID.toString(), playerId);
        switch (position) {
            case 0:
                mFragmentWarSquad = FragmentWarSquad.newInstance(guildParticipants);
                mFragmentWarSquad.setArguments(args);
                return mFragmentWarSquad;
            case 1:
                mFragmentWarRivalSquad = FragmentWarRivalSquad.newInstance(rivalParticipants);
                mFragmentWarRivalSquad.setArguments(args);
                return mFragmentWarRivalSquad;
            case 2:
                mFragmentWarSignup = FragmentWarSignup.newInstance();
                return mFragmentWarSignup;
            default:
                return null;
        }

    }


    @Override
    public int getCount() {
        return 3;
    }


    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "My Squad";
            case 1:
                return "Rival";
            case 2:
                return "Sign Up";
        }
        return "";//mFragmentTitleList.get(position);
    }

    @Override
    public void startUpdate(ViewGroup container) {
        super.startUpdate(container);
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        super.finishUpdate(container);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return super.isViewFromObject(view, object);
    }

    @Override
    public Parcelable saveState() {
        return super.saveState();
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
        // get the tags set by FragmentPagerAdapter
        switch (position) {
            case 0:
                mFragmentWarSquad = (FragmentWarSquad) createdFragment;
                break;
            case 1:
                mFragmentWarRivalSquad = (FragmentWarRivalSquad) createdFragment;
                break;
            case 2:
                mFragmentWarSignup = (FragmentWarSignup) createdFragment;
                break;
        }
        return createdFragment;
    }

}