package com.swctools.activity_modules.logs;

import android.os.Bundle;
import android.view.KeyEvent;

import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.swctools.R;
import com.swctools.common.sections_state_pager_adaptors.SectionsStatePagerAdapter;
import com.swctools.activity_modules.logs.fragments.FragmentDBUpgrade;
import com.swctools.activity_modules.logs.fragments.FragmentDataUpdate;
import com.swctools.activity_modules.logs.fragments.FragmentDatabaseTables;
import com.swctools.activity_modules.logs.fragments.FragmentMessageLog;
import com.swctools.util.ActivitySwitcher;

public class LogsActivity extends AppCompatActivity {
    private FragmentDBUpgrade mFragmentDBUpgrade;
    private FragmentDataUpdate fragmentDataUpdate;
    private FragmentDatabaseTables fragmentDatabaseTables;
    private FragmentMessageLog fragmentMessageLog;
    private ViewPager mLogs_ViewPager;
    private SectionsStatePagerAdapter mSectionsStatePagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);
        Toolbar toolbar = findViewById(R.id.logsToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Logs");

        //Fragment shit:
        mFragmentDBUpgrade = new FragmentDBUpgrade();
        fragmentDataUpdate = new FragmentDataUpdate();
        fragmentDatabaseTables = new FragmentDatabaseTables();
//        fragmentMessageLog = new FragmentMessageLog();
        mSectionsStatePagerAdapter = new SectionsStatePagerAdapter(getSupportFragmentManager());

        mLogs_ViewPager = findViewById(R.id.about_ViewPager);
        mSectionsStatePagerAdapter.addFragment(mFragmentDBUpgrade, "DB Upgrades");
        mSectionsStatePagerAdapter.addFragment(fragmentDataUpdate, "Data Update Log");
//        mSectionsStatePagerAdapter.addFragment(fragmentMessageLog, "Request Log");
        mSectionsStatePagerAdapter.notifyDataSetChanged();
        mLogs_ViewPager.setAdapter(mSectionsStatePagerAdapter);

        TabLayout tabs = findViewById(R.id.logsTabLayout);
//
        tabs.setupWithViewPager(mLogs_ViewPager);

    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        this.finish();

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            ActivitySwitcher.launchMainActivity(this);
        }
        return false;// super.onKeyDown(keyCode, event);
    }


}
