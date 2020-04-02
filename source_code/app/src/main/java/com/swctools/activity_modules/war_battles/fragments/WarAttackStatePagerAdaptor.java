package com.swctools.activity_modules.war_battles.fragments;

import android.os.Bundle;
import android.util.Log;

import com.swctools.activity_modules.war_battles.models.War_PlayerBattles;
import com.swctools.common.enums.BundleKeys;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class WarAttackStatePagerAdaptor extends FragmentStatePagerAdapter {
    private static final String TAG = "WarAttackStatePagerAdap";
//    private ArrayList<WarSummaryItem> warSummaryItems;

    private String playerId;
    private String warId;
    private String guildId;
    private ArrayList<War_PlayerBattles> war_playerBattles;

    public WarAttackStatePagerAdaptor(FragmentManager fm, String playerId, String warId, String guildId, ArrayList<War_PlayerBattles> war_playerBattles) {
        super(fm);
        this.playerId = playerId;
        this.warId = warId;
        this.guildId = guildId;
        this.war_playerBattles = war_playerBattles;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle args = new Bundle();
        args.putString(BundleKeys.WAR_ID.toString(), warId);
        args.putString(BundleKeys.GUILD_ID.toString(), guildId);
        switch (position) {

            case 0:
                Fragment_WarAttacksSummary fragment_warAttacksSummary = Fragment_WarAttacksSummary.newInstance();
                fragment_warAttacksSummary.setArguments(args);
                return fragment_warAttacksSummary;
            default:
                return Fragment_PlayerWarAttacks.newInstance(war_playerBattles.get(position-1).getPlayerId(),warId, guildId);
        }
    }

    @Override
    public int getCount() {
        if (war_playerBattles.size() == 0) {
            return 1;
        } else {
            return war_playerBattles.size() + 1;
        }
    }


    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Summary";
           default:
               return war_playerBattles.get(position-1).getPlayerName();
        }
    }
}
