package com.swctools.base;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.swctools.config.AppConfig;
import com.swctools.common.enums.BundleKeys;
import com.swctools.common.enums.PlayerBase;
import com.swctools.common.helpers.PlayerHelper;
import com.swctools.common.popups.AlertFragment;
import com.swctools.common.popups.MessageTextViewFragment;
import com.swctools.common.popups.PlayerSelectionFragment;
import com.swctools.common.popups.YesNoFragment;
import com.swctools.interfaces.YesNoAlertCallBack;
import com.swctools.swc_server_interactions.fragments.SWC_Server_Tasks_Fragment;
import com.swctools.util.ActivitySwitcher;
import com.swctools.util.MethodResult;
import com.swctools.util.StringUtil;
import com.swctools.util.Utils;

import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public abstract class BaseActivity extends AppCompatActivity implements
        PlayerServiceCallBackInterface,
        PlayerSelectFragInterface,
        ApplyLayoutInterface,
        MessageTextViewInterface,
        YesNoAlertCallBack {
    private static final String TAG = "BaseActivity";
//Commands:

    protected static final String DELETE_PLAYER = "DELETE_PLAYER";
    protected static final String YODA_PROGRESS = "Patience you must have <(-_-)>";

    protected ConstraintLayout progress_overlay_container;
    protected ProgressBar progress_overlay_bar;
    protected TextView progress_overlay_message;
    protected boolean mDownloading = false;
    protected String progressMessage;
    protected int max, progress, progressShowStep, progressCurrStep;
    protected boolean mVisitorBound;
    protected boolean isActualBound;
    protected boolean actualBound = false;
    protected int player_RowId;
    protected PlayerBase selectedBase;
    protected String playerId;
    protected String playerSecret;
    protected String command;
    protected Context context;
    protected YesNoFragment yesNoFragment;
    protected AlertFragment alertFragment;

    protected String previousScreen;
    protected AppConfig appConfig;

    protected SWC_Server_Tasks_Fragment swc_server_tasks_fragment;

    private int selectedVersionToApply;
    private int selectedLayoutId;


    @Override
    protected void onStop() {
        super.onStop();

    }


    protected void getConflictData(String playerId) {
        this.playerId = playerId;
        showProgressLayout("Please wait...<(-_-)>");
        swc_server_tasks_fragment.getConflictData(playerId, getApplicationContext());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        appConfig = new AppConfig(this);

        swc_server_tasks_fragment = SWC_Server_Tasks_Fragment.getInstance(getSupportFragmentManager());


    }

    protected void showProgressLayout(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progress_overlay_container.setVisibility(View.VISIBLE);
                progress_overlay_message.setText(msg);
                progress_overlay_bar.setIndeterminate(true);
            }
        });
    }

    private void updateRefreshStatus(final boolean refreshing, final String progressMessage, final boolean indProgress, final int progress, final int max) {
        this.progressMessage = progressMessage;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (refreshing) {
                    progress_overlay_container.setVisibility(View.VISIBLE);
                    progress_overlay_message.setText(progressMessage);
                    if (indProgress) {
                        progress_overlay_bar.setIndeterminate(false);
                        progress_overlay_bar.setMax(max);
                        progress_overlay_bar.setProgress(progress);
                    } else {
                        progress_overlay_bar.setIndeterminate(true);
                    }
                }
            }
        });
    }

    protected void hideProgressView() {
        progressMessage = "";
        progress_overlay_container.setVisibility(View.GONE);
        progress_overlay_message.setText("");
        progress_overlay_bar.setProgress(0);
        progress_overlay_bar.setMax(0);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
        }
        return false;// super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        if (item.getItemId() == android.R.id.home) // Press Back Icon
        {
            finish();
            ActivitySwitcher.launchMainActivity(this);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    @CallSuper
    public void onBackPressed() {
        if (mDownloading) {
            finishDownloading();
            hideProgressView();
        } else {
            super.onBackPressed();
            this.finish();
        }

    }

    @Override
    public void publishProgress(String msg) {
        updateRefreshStatus(true, msg, false, 0, 0);
    }

    @Override
    public void publishProgress(String msg, int progress, int max) {


        this.progress = progress;
        this.max = max;

        updateRefreshStatus(true, msg, true, progress, max);

    }

    @Override
//    @CallSuper
    public void finishDownloading() {
        hideProgressView();
    }

    @Override
    public void playerSelected(String playerId) {
        showProgressLayout("Please wait...<(-_-)>");
        this.playerId = playerId;
        swc_server_tasks_fragment.updatePlayerLayout(playerId, selectedLayoutId, selectedVersionToApply, selectedBase, getApplicationContext());
    }

    @Override
    public void sendMessageFromFragment(String message) {
        showLongToast(message);
    }

    protected void showLongToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    protected void showShortToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void applySelectedLayout(int layoutId, int versId) {
        selectedLayoutId = layoutId;
        selectedVersionToApply = versId;
        selectedBase = PlayerBase.PVP;
        selectPlayerToUpdate();

    }

    @Override
    public void applySelectedLayoutWar(int layoutId, int versId) {
        selectedLayoutId = layoutId;
        selectedVersionToApply = versId;
        selectedBase = PlayerBase.WAR;
        selectPlayerToUpdate();

    }

    @Override
    public void applySelectedLayout(int layoutId, int versId, String playerId) {
        showProgressLayout("Please wait...<(-_-)>");
        selectedLayoutId = layoutId;
        selectedVersionToApply = versId;
        selectedBase = PlayerBase.PVP;
        this.playerId = playerId;
        swc_server_tasks_fragment.updatePlayerLayout(playerId, layoutId, versId, selectedBase, getApplicationContext());

    }

    @Override
    public void applySelectedLayoutWar(int layoutId, int versId, String playerId) {
        showProgressLayout("Please wait...<(-_-)>");
        selectedLayoutId = layoutId;
        selectedVersionToApply = versId;
        selectedBase = PlayerBase.WAR;
        this.playerId = playerId;
        swc_server_tasks_fragment.updatePlayerLayout(playerId, layoutId, versId, selectedBase, getApplicationContext());
    }

    protected void visitPlayer(String playerId) {
        showProgressLayout("Please wait...<(-_-)>");
        this.playerId = playerId;
        swc_server_tasks_fragment.visitPlayer(playerId, getApplicationContext());
    }

    protected void getPVPLayout(String playerId) {
        showProgressLayout("Please wait...<(-_-)>");
        this.playerId = playerId;
        swc_server_tasks_fragment.getPVPLayout(playerId, getApplicationContext());
    }

    protected void getWARLayout(String playerId) {
        showProgressLayout("Please wait...<(-_-)>");
        this.playerId = playerId;
        swc_server_tasks_fragment.getWARLayout(playerId, getApplicationContext());
    }

    private void selectPlayerToUpdate() {
        if (Utils.countPlayers(this) > 1) {
            PlayerSelectionFragment.getInstance(getSupportFragmentManager(), playerId);
        } else {
            showProgressLayout("Please wait...<(-_-)>");
            playerId = Utils.getOnlyPlayerId(this);
            swc_server_tasks_fragment.updatePlayerLayout(playerId, selectedLayoutId, selectedVersionToApply, selectedBase, getApplicationContext());
        }
    }

    public void addPlayer(String pid, String pSec) {
        showProgressLayout("Please wait...<(-_-)>");
        playerId = pid;
        playerSecret = pSec;
        if (StringUtil.isStringNotNull(playerId) && StringUtil.isStringNotNull(playerSecret)) {
            swc_server_tasks_fragment.addPlayerData(pid, pSec, getApplicationContext());
        } else {
            showLongToast("Please enter both your playerId and playerSecret!!");
        }
    }

    protected void sendGetWarStatusCall(String playerId) {
        showProgressLayout("Please wait...<(-_-)>");
        this.playerId = playerId;
        swc_server_tasks_fragment.getWarStatus(playerId, getApplicationContext());
    }


    public void showAlertFrag(String title, String message) {
        Bundle bundle = new Bundle();
        bundle.putString(BundleKeys.DIALOG_MESSAGE.toString(), message);
        bundle.putString(BundleKeys.DIALOG_TITLE.toString(), title);
        AlertFragment alertFragment = new AlertFragment();
        alertFragment.setArguments(bundle);
        alertFragment.show(getFragmentManager(), "HELP");
    }

    protected void callMessageTextViewFrag(String title, String message, String command, @Nullable String initialVal) {

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
    public void onTextViewDialogPositiveClick(String msg, String cmd) {

    }

    @Override
    public void onYesNoDialogNoClicked(String activity_command) {

    }

    @Override
    public void onYesNoDialogYesClicked(String activity_command, Bundle bundle) {

        if (activity_command.equalsIgnoreCase(DELETE_PLAYER)) {

            MethodResult methodResult = PlayerHelper.deletePlayer(player_RowId, this);
            handleListUpdate(command, methodResult.success);
        } else if (activity_command.equalsIgnoreCase(PlayerBaseSelection.UPDATEWAROVERRIDE)) {
            showProgressLayout("Please wait...<(-_-)>");
            swc_server_tasks_fragment.updatePlayerLayout(playerId, selectedLayoutId, selectedVersionToApply, PlayerBase.WAR, getApplicationContext());
//            actualPlayer.applyWARLayout(playerId, selectedLayoutId, selectedVersionToApply, true, this);
        }
    }

    protected abstract void handleListUpdate(String activity_command, boolean success);

    @Override
    public void setDownloading() {
        mDownloading = true;
    }
}
