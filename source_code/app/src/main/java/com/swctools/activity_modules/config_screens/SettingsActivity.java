package com.swctools.activity_modules.config_screens;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.swctools.R;
import com.swctools.base.MessageTextViewInterface;
import com.swctools.common.enums.BundleKeys;
import com.swctools.common.popups.AlertFragment;
import com.swctools.common.popups.MessageSpinnerViewFragment;
import com.swctools.common.popups.MessageTextViewFragment;
import com.swctools.config.AppConfig;
import com.swctools.util.ActivitySwitcher;
import com.swctools.util.StringUtil;

enum SettingsCommand {
    SET_SERVER_ADDRESS,
    SET_TIME_FORMAT,
    SET_REAUTHTIME,
    SET_BOTID,
    SET_BOTSECRET;


}

public class SettingsActivity extends AppCompatActivity implements
        MessageTextViewInterface,
        MessageSpinnerViewFragment.MessageSpinnerFragmentInterface {
    private static final String TAG = "SettingsActivity";
    private static final int DEFENCE_JOB = 1000;
    private TextView settings_ServerAddress, settings_DateFormat, settings_LoginTimeout, settings_BotId, settings_BotSecret;
    private Switch layoutImageOn, rememberFavPref_Switch, logSWCMsgSwitch;
    private AppConfig appConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        appConfig = new AppConfig(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.settingsToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Settings");
        settings_ServerAddress = (TextView) findViewById(R.id.settings_ServerAddress);
        settings_DateFormat = (TextView) findViewById(R.id.settings_DateFormat);
        settings_LoginTimeout = (TextView) findViewById(R.id.settings_LoginTimeout);
        settings_BotId = (TextView) findViewById(R.id.settings_BotId);
        settings_BotSecret = (TextView) findViewById(R.id.settings_BotSecret);
        layoutImageOn = (Switch) findViewById(R.id.layoutImageOn);
        rememberFavPref_Switch = (Switch) findViewById(R.id.rememberFavPref_Switch);
        logSWCMsgSwitch = findViewById(R.id.logSWCMsgSwitch);
        ConstraintLayout settings_serverAddressContainer = (ConstraintLayout) findViewById(R.id.settings_serverAddressContainer);
        ConstraintLayout settings_DateFormatContainer = (ConstraintLayout) findViewById(R.id.settings_DateFormatContainer);
        ConstraintLayout settings_serverRelogin = (ConstraintLayout) findViewById(R.id.settings_serverRelogin);
        LinearLayout botIdContainer = (LinearLayout) findViewById(R.id.botIdContainer);
        LinearLayout botSecretContainer = (LinearLayout) findViewById(R.id.botSecretContainer);

        settings_DateFormatContainer.setOnClickListener(new DateFormatListener());
        settings_serverAddressContainer.setOnClickListener(new ServerAddressListener());
        settings_serverRelogin.setOnClickListener(new ServerReloginListener());
        botIdContainer.setOnClickListener(new BotIdListener());
        botSecretContainer.setOnClickListener(new BotSecretListener());

        settings_ServerAddress.setText(appConfig.getServerAddress());
        settings_DateFormat.setText(appConfig.shortDateFormat());
        settings_LoginTimeout.setText(String.valueOf(appConfig.lServerTimeOut()));
        settings_BotId.setText(appConfig.getVisitor_playerId());
        settings_BotSecret.setText(appConfig.getVisitor_playersecret());
        layoutImageOn.setChecked(appConfig.bLayoutImageOn());
        rememberFavPref_Switch.setChecked(appConfig.bFavPref());

        logSWCMsgSwitch.setChecked(appConfig.bLogSWCMessage());
        logSWCMsgSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                appConfig.setbLogSWCMessage(b);
            }
        });
        layoutImageOn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                appConfig.setbLayoutImageON(isChecked);

            }
        });
        rememberFavPref_Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                appConfig.set_bFavPref(isChecked);
            }
        });
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        this.finish();
        ActivitySwitcher.launchMainActivity(this);

    }


    private void callMessageTextViewFrag(String title, String message, String command, @Nullable String initialVal) {

        Bundle bundle = new Bundle();
        bundle.putString(BundleKeys.DIALOG_TITLE.toString(), title);
        bundle.putString(BundleKeys.DIALOG_MESSAGE.toString(), message);
        bundle.putString(BundleKeys.DIALOG_COMMAND.toString(), command);
        bundle.putString(BundleKeys.DIALOG_VALUE.toString(), initialVal);

        MessageTextViewFragment messageTextViewFragment = new MessageTextViewFragment();
        messageTextViewFragment.setArguments(bundle);
        messageTextViewFragment.show(getFragmentManager(), command);
    }


    @Override
    public void onSpinnerDialogPositiveClick(String msg, String cmd) {

        if (cmd.equalsIgnoreCase(SettingsCommand.SET_SERVER_ADDRESS.toString())) {
            appConfig.setServerAddress(msg);
            settings_ServerAddress.setText(msg);
        } else if (cmd.equalsIgnoreCase(SettingsCommand.SET_TIME_FORMAT.toString())) {
            appConfig.setDateFormat(msg);
            settings_DateFormat.setText(msg);
        } else if (cmd.equalsIgnoreCase(SettingsCommand.SET_REAUTHTIME.toString())) {
            try {
                int reAuthTime = Integer.parseInt(msg);
                appConfig.setLServerTimeOut(reAuthTime);
                settings_LoginTimeout.setText(msg);
            } catch (Exception e) {
                showAlert("Error saving timeout!", e.getMessage());
            }
        } else if (cmd.equalsIgnoreCase(SettingsCommand.SET_BOTID.toString())) {
            appConfig.setVisitor_playerId(msg);
            settings_BotId.setText(msg);
        } else if (cmd.equalsIgnoreCase(SettingsCommand.SET_BOTSECRET.toString())) {
            appConfig.setVisitor_Secret(msg);
            settings_BotSecret.setText(msg);
        }
    }

    private void showAlert(String title, String msg) {
        AlertFragment alertFragment = new AlertFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BundleKeys.DIALOG_TITLE.toString(), title);
        bundle.putString(BundleKeys.DIALOG_MESSAGE.toString(), msg);
        alertFragment.setArguments(bundle);
        alertFragment.show(getFragmentManager(), "SETTINGSALERT");
    }

    @Override
    public void onTextViewDialogPositiveClick(String msg, String cmd) {

        if (cmd.equalsIgnoreCase(SettingsCommand.SET_BOTID.toString())) {
            if (StringUtil.isStringNotNull(msg)) {
                appConfig.setVisitor_playerId(msg);
                settings_BotId.setText(appConfig.getVisitor_playerId());
            } else {
                showAlert("Error!", "Must enter a value for the ID!");
            }

        } else if (cmd.equalsIgnoreCase(SettingsCommand.SET_BOTSECRET.toString())) {
            if (StringUtil.isStringNotNull(msg)) {
                appConfig.setVisitor_Secret(msg);
                settings_BotSecret.setText(appConfig.getVisitor_playersecret());
            } else {
                showAlert("Error!", "Must enter a value for the secret!");
            }
        } else if (cmd.equalsIgnoreCase(SettingsCommand.SET_REAUTHTIME.toString())) {
            try {
                int newTime = Integer.parseInt(msg);
                appConfig.setLServerTimeOut(newTime);
                settings_LoginTimeout.setText(String.valueOf(appConfig.getNotificationInterval()));
            } catch (Exception e) {
                showAlert("Error!", e.getMessage());
            }
        } else if (cmd.equalsIgnoreCase(SettingsCommand.SET_SERVER_ADDRESS.toString())) {
            if (StringUtil.isStringNotNull(msg)) {
                appConfig.setServerAddress(msg);
                settings_ServerAddress.setText(msg);
            } else {
                showAlert("Error!", "Must enter a URL to send the requests to!");
            }
        }

    }


    class ServerAddressListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String currVal = appConfig.getServerAddress();
            callMessageTextViewFrag("Set Server Address", "Make sure you enter this correctly!", SettingsCommand.SET_SERVER_ADDRESS.toString(), currVal);
        }
    }

    class BotIdListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String currID = appConfig.getVisitor_playerId();
            callMessageTextViewFrag("Set Bot ID", "Enter new bot player id below (don't forget to also set the secret!).", SettingsCommand.SET_BOTID.toString(), currID);
        }
    }

    class BotSecretListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String currVal = appConfig.getVisitor_playersecret();
            callMessageTextViewFrag("Set Bot Secret", "Enter new bot player secret below (don't forget to also set the id!).", SettingsCommand.SET_BOTSECRET.toString(), currVal);
        }
    }

    class DateFormatListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            Bundle bundle = new Bundle();
            bundle.putString(BundleKeys.DIALOG_TITLE.toString(), "Set Date Format");
            bundle.putString(BundleKeys.DIALOG_MESSAGE.toString(), "Pick a format from the list:");
            bundle.putString(BundleKeys.DIALOG_COMMAND.toString(), SettingsCommand.SET_TIME_FORMAT.toString());
            String[] myResArray = getResources().getStringArray(R.array.short_date_formats);
            bundle.putStringArray(BundleKeys.DIALOG_SPINNER_STRING_ARRAY.toString(), myResArray);
            MessageSpinnerViewFragment spinnerViewFragment = new MessageSpinnerViewFragment();
            spinnerViewFragment.setArguments(bundle);
            spinnerViewFragment.show(getFragmentManager(), SettingsCommand.SET_TIME_FORMAT.toString());
        }

    }

    class ServerReloginListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            int currVal = appConfig.lServerTimeOut();
            callMessageTextViewFrag("Login Timeout Setting", "Set a time (in minutes):", SettingsCommand.SET_REAUTHTIME.toString(), String.valueOf(currVal));
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            ActivitySwitcher.launchMainActivity(this);
        }
        return false;// super.onKeyDown(keyCode, event);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) // Press Back Icon
        {
            finish();
        }


        return super.onOptionsItemSelected(item);
    }

}
