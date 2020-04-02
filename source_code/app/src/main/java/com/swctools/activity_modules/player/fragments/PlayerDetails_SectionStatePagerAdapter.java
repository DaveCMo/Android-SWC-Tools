package com.swctools.activity_modules.player.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.swctools.common.enums.BundleKeys;
import com.swctools.activity_modules.player.models.Conflict_Data_Model;
import com.swctools.common.models.player_models.PlayerModel;

import java.util.ArrayList;

public class PlayerDetails_SectionStatePagerAdapter extends FragmentStatePagerAdapter {
    private static final String TAG = "PDSectionStatPager";
    private String playerId;

    private long baseId = 0;
    private FragmentPlayerDetails mFragmentPlayerDetails;
    private FragmentPlayerBattleLogs mFragmentPlayerBattleLogs;
    private FragmentFavouriteLayout mFragmentFavouriteLayout;
    private FragmentPlayerConflictTracker mFragmentPlayerConflictTracker;
    private PlayerModel playerModel;

    public PlayerDetails_SectionStatePagerAdapter(FragmentManager fm, String playerId, PlayerModel playerModel) {
        super(fm);
        this.playerId = playerId;
        this.playerModel = playerModel;

    }
    public void  updatePlayerModelInfrags(PlayerModel playerModel){
        mFragmentPlayerDetails.processPlayerModel(playerModel);
        mFragmentPlayerBattleLogs.refreshData(playerId, playerModel.battleLogs);
//        mFragmentPlayerBattleLogs.
    }


    @Override
    public Fragment getItem(int position) {
        Bundle args = new Bundle();
        args.putString(BundleKeys.PLAYER_ID.toString(), playerId);
        switch (position) {
            case 0:
                mFragmentPlayerDetails = FragmentPlayerDetails.newInstance();
                mFragmentPlayerDetails.setArguments(args);
                return mFragmentPlayerDetails;
            case 1:
                mFragmentPlayerBattleLogs = FragmentPlayerBattleLogs.newInstance();
                mFragmentPlayerBattleLogs.setArguments(args);
                return mFragmentPlayerBattleLogs;
            case 2:
                mFragmentFavouriteLayout = FragmentFavouriteLayout.newInstance();
                mFragmentFavouriteLayout.setArguments(args);
                return mFragmentFavouriteLayout;
            case 3:
                mFragmentPlayerConflictTracker = FragmentPlayerConflictTracker.newInstance();
                return mFragmentPlayerConflictTracker;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 4;
    }
//
//    public void updatePlayerDetails(String playerId) {
//        this.playerId = playerId;
//        mFragmentPlayerDetails.processMessage(this.playerId);
//        mFragmentPlayerDetails.stopRefreshing();
////        mFragmentPlayerBattleLogs.refreshData(playerId);
////        mFragmentPlayerBattleLogDefence.processDefenceLog(this.playerId);
////        mFragmentPlayerBattleLogDefence.stopRefreshing();
//    }
    public void updateLayout(){
        mFragmentFavouriteLayout.refreshList();
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }



    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Player";
            case 1:
                return "Battles";
            case 2:
                return "Layouts";
            case 3:
                return "Conflicts";
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
        switch (position) {
            case 0:
                mFragmentPlayerDetails = null;
            case 1:
                mFragmentPlayerBattleLogs = null;
            case 2:
                mFragmentFavouriteLayout = null;
            case 3:
                mFragmentPlayerConflictTracker = null;
            default:

        }

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
                mFragmentPlayerDetails = (FragmentPlayerDetails)createdFragment;
                break;
            case 1:
                mFragmentPlayerBattleLogs = (FragmentPlayerBattleLogs) createdFragment;
                break;
            case 2:
                mFragmentFavouriteLayout = (FragmentFavouriteLayout)createdFragment;
                break;
            case 3:
                mFragmentPlayerConflictTracker = (FragmentPlayerConflictTracker) createdFragment;
                break;
        }
        return createdFragment;
    }


    public void updateConflict(ArrayList<Conflict_Data_Model> conflictDataModels){
        mFragmentPlayerConflictTracker.updateConflictRecycler(conflictDataModels);
    }
}