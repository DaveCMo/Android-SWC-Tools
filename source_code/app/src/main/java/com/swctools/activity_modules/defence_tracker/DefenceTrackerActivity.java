package com.swctools.activity_modules.defence_tracker;

import android.content.ContentValues;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;
import com.swctools.R;
import com.swctools.base.MessageTextViewInterface;
import com.swctools.config.AppConfig;
import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;
import com.swctools.common.enums.BundleKeys;
import com.swctools.common.popups.AlertFragment;
import com.swctools.common.popups.MessageTextViewFragment;
import com.swctools.activity_modules.main.models.PlayerDAOList;
import com.swctools.activity_modules.main.models.PlayerDAO;
import com.swctools.util.ActivitySwitcher;
import com.swctools.activity_modules.defence_tracker.recycler_adaptor.RecyclerAdaptor_PlayerNotification;

import java.util.List;

public class DefenceTrackerActivity extends AppCompatActivity implements MessageTextViewInterface, RecyclerAdaptor_PlayerNotification.UpdatePlayerNotificationInterface {
    private static final String TAG = "DefenceTrackerActivity";
    private FirebaseJobDispatcher dispatcher;
    private Switch masterDefenceSwitch;
    private AppConfig appConfig;
    private RecyclerView settings_PlrNotificationsRecycler;
    private RecyclerAdaptor_PlayerNotification mAdapter;
    private List<PlayerDAO> playerDAOList;
    private LinearLayout settings_NotifContainer;
    private ConstraintLayout notif_IntervalContainer;
    private TextView notif_Interval, defenceOnOffText;
    private static final int DEFENCE_JOB = 1000;
    private static final String DEFENCE_JOB_TAG = "DEFENCE_TRACK_JOB";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defence_tracker);
        appConfig = new AppConfig(this);
        dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Toolbar toolbar = (Toolbar) findViewById(R.id.defenceToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Defence Tracker");

        notif_Interval = (TextView) findViewById(R.id.notif_Interval);
        int interval = appConfig.getNotificationInterval();
        notif_Interval.setText(String.valueOf(interval));
        notif_IntervalContainer = (ConstraintLayout) findViewById(R.id.notif_IntervalContainer);
        notif_IntervalContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callMessageTextViewFrag("Set Refresh Interval", "Set how often (in minutes) you want your defence log to be checked", DefenceLogCommands.SET_REFRESH_INTERVAL);
            }
        });
        settings_NotifContainer = (LinearLayout) findViewById(R.id.settings_NotifContainer);


        playerDAOList = PlayerDAOList.getPlayerDAOList(this);
        mAdapter = new RecyclerAdaptor_PlayerNotification(playerDAOList, this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        settings_PlrNotificationsRecycler = (RecyclerView) findViewById(R.id.settings_PlrNotificationsRecycler);
        settings_PlrNotificationsRecycler.setLayoutManager(mLayoutManager);
        settings_PlrNotificationsRecycler.setHasFixedSize(true);
        settings_PlrNotificationsRecycler.setItemAnimator(new DefaultItemAnimator());
        settings_PlrNotificationsRecycler.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        settings_PlrNotificationsRecycler.setAdapter(mAdapter);

        mAdapter.notifyDataSetChanged();


        defenceOnOffText = (TextView) findViewById(R.id.defenceOnOffText);
        masterDefenceSwitch = (Switch) findViewById(R.id.masterDefenceSwitch);
        setMasterDefenceSwitch(appConfig.bNotificationsEnabled());
        masterDefenceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                appConfig.setbNotificationsEnabled(masterDefenceSwitch.isChecked());
                setMasterDefenceSwitch(appConfig.bNotificationsEnabled());
                setAllNotificationRows(masterDefenceSwitch.isChecked());
                showHideTrackerBlock(appConfig.bNotificationsEnabled());
                if (masterDefenceSwitch.isChecked()) {
                    startDefence();
                } else {
                    stopDefence();
                }
            }
        });

        showHideTrackerBlock(appConfig.bNotificationsEnabled());
    }

    private void setMasterDefenceSwitch(boolean on) {
        masterDefenceSwitch.setChecked(on);
        if (on) {
            defenceOnOffText.setText("ON");
        } else {
            defenceOnOffText.setText("OFF");
        }
    }

    private void startDefence() {
        int refreshInt = appConfig.getNotificationInterval() * 60;

        Job job = dispatcher.newJobBuilder()
                .setTag(DEFENCE_JOB_TAG)
                .setService(DefenceLogJobService.class)
                .setReplaceCurrent(false)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(0, refreshInt))
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .build();

        dispatcher.mustSchedule(job);
    }

    private void stopDefence() {
        dispatcher.cancel(DEFENCE_JOB_TAG);
//        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
//        scheduler.cancel(DEFENCE_JOB);
    }

    private void setAllNotificationRows(Boolean isChecked) {
        for (int i = 0; i < settings_PlrNotificationsRecycler.getChildCount(); i++) {
            mAdapter.toggleRowEnabled(i, isChecked);
        }
    }

    private void showHideTrackerBlock(boolean enabled) {
        if (enabled) {
            settings_NotifContainer.setVisibility(View.VISIBLE);
        } else {
            settings_NotifContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public void togglePlayerNotification(int db_Id, Boolean isSelected) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContracts.PlayersTable.NOTIFICATIONS, isSelected);
        String whereClause = DatabaseContracts.PlayersTable.COLUMN_ID + " = ?";
        String[] whereArgs = {String.valueOf(db_Id)};
        DBSQLiteHelper.updateData(DatabaseContracts.PlayersTable.TABLE_NAME, contentValues, whereClause, whereArgs, this);
    }

    @Override
    public void onTextViewDialogPositiveClick(String msg, String cmd) {
        switch (cmd) {

            case DefenceLogCommands.SET_REFRESH_INTERVAL:
                try {
                    int refreshInt = Integer.parseInt(msg);
                    if (refreshInt >= 2) {
                        appConfig.setNotificationInterval(refreshInt);
                        notif_Interval.setText(String.valueOf(refreshInt));
                        stopDefence();
                        startDefence();
                    } else {
                        showAlert("Number too low!", "Please enter a number greater than or equal to 2.");
                    }
                } catch (Exception e) {
                    showAlert("Error saving your value", e.getMessage());
                }
        }
    }

    private void callMessageTextViewFrag(String title, String message, String command) {

        Bundle bundle = new Bundle();
        bundle.putString(BundleKeys.DIALOG_TITLE.toString(), title);
        bundle.putString(BundleKeys.DIALOG_MESSAGE.toString(), message);
        bundle.putString(BundleKeys.DIALOG_COMMAND.toString(), command);
        MessageTextViewFragment messageTextViewFragment = new MessageTextViewFragment();
        messageTextViewFragment.setArguments(bundle);
        messageTextViewFragment.show(getFragmentManager(), command);
    }

    private void showAlert(String title, String msg) {
        AlertFragment alertFragment = new AlertFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BundleKeys.DIALOG_TITLE.toString(), title);
        bundle.putString(BundleKeys.DIALOG_MESSAGE.toString(), msg);
        alertFragment.setArguments(bundle);
        alertFragment.show(getFragmentManager(), "DEFENCEALERT");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            ActivitySwitcher.launchMainActivity(this);
        }
        return false;// super.onKeyDown(keyCode, event);
    }
    interface DefenceLogCommands {
        String SET_REFRESH_INTERVAL = "SET_REFRESH_INTERVAL";
    }

}

