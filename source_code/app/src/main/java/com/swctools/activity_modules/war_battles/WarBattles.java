package com.swctools.activity_modules.war_battles;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;
import com.swctools.R;
import com.swctools.activity_modules.war_battles.fragments.WarAttackStatePagerAdaptor;
import com.swctools.activity_modules.war_battles.models.WarBattleListProvider;
import com.swctools.activity_modules.war_battles.models.WarSummaryItem;
import com.swctools.activity_modules.war_battles.models.War_PlayerBattles;
import com.swctools.common.enums.BundleKeys;
import com.swctools.util.Utils;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

public class WarBattles extends AppCompatActivity {
    private static final String TAG = "WarAttacks";


    private String playerId;
    private String warId;
    private String guildId;
    private String rivalGuildId;
    private ArrayList<War_PlayerBattles> war_playerBattles;

    //UI Components:
    private TabLayout warAttackTabs;
    private ViewPager warAttacksViewPager;
    private WarAttackStatePagerAdaptor warAttackStatePagerAdaptor;


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        Log.d(TAG, "onResumeFragments: ");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        setContentView(R.layout.activity_war_attacks);
        Toolbar toolbar = findViewById(R.id.warAttackToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        warAttacksViewPager = findViewById(R.id.warAttacksViewPager);
        warAttackTabs = findViewById(R.id.warAttackTabs);

        warId = getIntent().getStringExtra(BundleKeys.WAR_ID.toString());
        guildId = getIntent().getStringExtra(BundleKeys.GUILD_ID.toString());
        playerId = getIntent().getStringExtra(BundleKeys.PLAYER_ID.toString());
        setTitle("War Defences");

        war_playerBattles = WarBattleListProvider.returnWarBattles(warId, guildId, getApplicationContext());
        ArrayList<WarSummaryItem> warSummaryItems = WarBattleListProvider.getWarSummary(warId, guildId, getApplicationContext());

        warAttackStatePagerAdaptor = new WarAttackStatePagerAdaptor(getSupportFragmentManager(), playerId, warId, guildId, war_playerBattles);
        warAttacksViewPager.setAdapter(warAttackStatePagerAdaptor);
        warAttackTabs.setupWithViewPager(warAttacksViewPager, true);

        for (int i = 0; i < war_playerBattles.size(); i++) {
            if (war_playerBattles.get(i).getPlayerId().equalsIgnoreCase(playerId)) {
                warAttacksViewPager.setCurrentItem(i + 1, true);
            }

        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return false;// super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) // Press Back Icon
        {
            finish();
        }


        return true;
    }
}
