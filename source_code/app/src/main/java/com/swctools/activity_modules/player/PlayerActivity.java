package com.swctools.activity_modules.player;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.swctools.R;
import com.swctools.activity_modules.armoury_equipment.ArmouryEquipmentActivity;
import com.swctools.activity_modules.defence_tracker.DefenceNotificationHelper;
import com.swctools.activity_modules.player.background_tasks.PlayerActivity_Background_Fragment;
import com.swctools.activity_modules.player.fragments.FragmentPlayerBattleLog_Defence;
import com.swctools.activity_modules.player.fragments.FragmentPlayerConflictTracker;
import com.swctools.activity_modules.player.fragments.PlayerDetails_SectionStatePagerAdapter;
import com.swctools.activity_modules.player.models.Battle;
import com.swctools.activity_modules.player.models.Conflict_Data_Model;
import com.swctools.activity_modules.player.recycler_adaptors.RecyclerAdaptor_Defence;
import com.swctools.base.BaseLayoutActivity;
import com.swctools.common.enums.ApplicationMessageTemplates;
import com.swctools.common.enums.BattleType;
import com.swctools.common.enums.BundleKeys;
import com.swctools.common.enums.Factions;
import com.swctools.common.enums.ScreenCommands.SaveLayoutInterface;
import com.swctools.common.enums.ServerConstants;
import com.swctools.common.helpers.BundleHelper;
import com.swctools.common.helpers.DateTimeHelper;
import com.swctools.common.helpers.ImageHelpers;
import com.swctools.common.models.player_models.MapBuildings;
import com.swctools.common.models.player_models.PlayerModel;
import com.swctools.common.popups.BattleDetailFragment;
import com.swctools.common.popups.YesNoFragment;
import com.swctools.interfaces.FavouriteLayoutRowItemInterface;
import com.swctools.interfaces.SendMessageFromList;
import com.swctools.interfaces.YesNoAlertCallBack;
import com.swctools.swc_server_interactions.results.SWCLoginResponseData;
import com.swctools.swc_server_interactions.results.SWCVisitResult;
import com.swctools.util.ActivitySwitcher;
import com.swctools.util.MethodResult;
import com.swctools.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class PlayerActivity extends BaseLayoutActivity implements
        PlayerDetailsFragmentInterface,
        FragmentPlayerBattleLog_Defence.DefenceLogFragmentInterface,
        FavouriteLayoutRowItemInterface,
        YesNoAlertCallBack, SendMessageFromList,
        PlayerModelAsyncCallBackReceiver,
        RecyclerAdaptor_Defence.BattleLogCallback,
        FragmentPlayerConflictTracker.FragementConflictTrackerInterface, Player_Details_Callback,
        Player_TacticalCallback {


    private static final String TAG = "PlayerActivity";
    private static final String PLAYERFACTION = "PLAYERFACTION";
    private static final String PLAYERPLANETTXT = "PLAYERPLANETTXT";
    private static final String PLAYERPROTECTIONCOLOUR = "PLAYERPROTECTIONCOLOUR";
    private static final String PLAYERPROTECTIONSTATUS = "PLAYERPROTECTIONSTATUS";
    private static final String PLAYERPROTECTIONSTRING = "PLAYERPROTECTIONSTRING";
    private static final String PLAYERPROCESSDATA = "PLAYERPROCESSDATA";
    private static final String GETSCREQUESTSTRING = "GETSCREQUESTSTRING";
    private static final String REQUESTNOW = "REQUESTNOW";


    private YesNoFragment yesNoFragment;
    volatile boolean running;
    private boolean firstLoad;


    private PlayerDetails_SectionStatePagerAdapter mSectionsStatePagerAdapter;
    private String playerId;
    private String scRequestMessage;

    private TabLayout tabs;
    private ViewPager mViewPager;
    private FloatingActionButton fab_Open, fab_savePvpLayout, fab_saveWarLayout, fab_updatePvpLayout, fab_updateWarLayout, fab_WarStatus;
    private ConstraintLayout fab_SavePvPConstraint, fab_SaveWarConstraint, fab_UpdateWarConstraint, fab_UpdatePvPConstraint, fab_WarStatusConstraint;
    private FrameLayout mainOverlay;

    private List<FloatingActionButton> hiddenFabs;
    private List<ConstraintLayout> fabHiddenLayouts;
    private boolean fab_isOpen = false;
    //Task/service shit
    private boolean mDownloading = false;
    private boolean progressRunning;
    private String progressMessage;
    private ImageView plrAppBar_FactionImage, imgVPlanet;
    private TextView plrAppBar_Guild, plrAppBar_Name, txtPlrProtStatus, txtPlrProtTime, txtPlrPlanet;
    private PlayerModel playerModel;
    private boolean playerModelBound;

    private PlayerActivity_Background_Fragment playerActivity_background_fragment;
    private String faction;
    private Drawable factionImage;
    private String playerNameStr;
    private String guildNameStr;
    private String protectedStr, planetString, protectedString, protectionStatus;
    private long protectedUntil;
    private int COLOR_RED;
    private int COLOR_GREEN;
    private int protectedColour;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_player);
        // Define the view things!
        Toolbar toolbar = findViewById(R.id.playerToolbar);
        setSupportActionBar(toolbar);
        tabs = findViewById(R.id.playerTabLayout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        plrAppBar_FactionImage = findViewById(R.id.plrAppBar_FactionImage);
        plrAppBar_Guild = findViewById(R.id.plrAppBar_Guild);
        plrAppBar_Name = findViewById(R.id.plrAppBar_Name);
        fab_Open = findViewById(R.id.fab_playerOpenClose);
        fab_savePvpLayout = findViewById(R.id.fab_SavePVP);
        fab_saveWarLayout = findViewById(R.id.fab_SaveWar);
        fab_updatePvpLayout = findViewById(R.id.fab_UpdatePvP);
        fab_SavePvPConstraint = findViewById(R.id.fab_SavePvPConstraint);
        fab_SaveWarConstraint = findViewById(R.id.fab_SaveWarConstraint);
        fab_UpdatePvPConstraint = findViewById(R.id.fab_UpdatePvPConstraint);
        fab_WarStatusConstraint = findViewById(R.id.fab_WarStatusConstraint);
        fab_WarStatus = findViewById(R.id.fab_WarStatus);
        mViewPager = findViewById(R.id.playerDetailsViewPager);
        mainOverlay = findViewById(R.id.player_overlay);

        progress_overlay_bar = findViewById(R.id.progress_overlay_bar);
        progress_overlay_container = findViewById(R.id.progress_overlay_container_include);
        progress_overlay_message = findViewById(R.id.progress_overlay_message);

        txtPlrProtStatus = findViewById(R.id.txtPlrProtStatus);
        txtPlrProtTime = findViewById(R.id.txtPlrProtTime);
        imgVPlanet = findViewById(R.id.imgVPlanet);
        txtPlrPlanet = findViewById(R.id.txtPlrPlanet);
        COLOR_GREEN = getResources().getColor(R.color.green);
        COLOR_RED = getResources().getColor(R.color.red);


        //Lists and stuff...
        hiddenFabs = new ArrayList<>();
        hiddenFabs.add(fab_savePvpLayout);
        hiddenFabs.add(fab_saveWarLayout);
        hiddenFabs.add(fab_updatePvpLayout);
        hiddenFabs.add(fab_WarStatus);

        fabHiddenLayouts = new ArrayList<>();
        fabHiddenLayouts.add(fab_SavePvPConstraint);
        fabHiddenLayouts.add(fab_SaveWarConstraint);
        fabHiddenLayouts.add(fab_UpdatePvPConstraint);
        fabHiddenLayouts.add(fab_WarStatusConstraint);


        playerId = getIntent().getStringExtra(BundleKeys.PLAYER_ID.toString());
//        if (savedInstanceState == null) {
        mSectionsStatePagerAdapter = new PlayerDetails_SectionStatePagerAdapter(getSupportFragmentManager(), playerId, playerModel);

        mViewPager.setAdapter(mSectionsStatePagerAdapter);
        tabs.setupWithViewPager(mViewPager);
        running = true;
        //click listeners:
        fab_savePvpLayout.setOnClickListener(new SavePvpClickListener());
        fab_Open.setOnClickListener(new FabOpenClickListener());
        fab_saveWarLayout.setOnClickListener(new SaveWarClickListener());
        fab_updatePvpLayout.setOnClickListener(new UpdateLayoutClickListener());
        fab_WarStatus.setOnClickListener(new WarStatusClickListener());
        mainOverlay.setOnClickListener(new MainOverlayClickListener());
        /*------->*/

        playerActivity_background_fragment = PlayerActivity_Background_Fragment.getInstance(getSupportFragmentManager());



        /*------->*/
        if (savedInstanceState == null) { // NEW
            firstLoad = true;

//            publishProgress("Loading your player details...");
            //            args.putString(BundleKeys.PLAYER_ID.toString(), playerId);
            try {
                DefenceNotificationHelper.clearAll(playerId, this);
            } catch (Exception e) {
            }
        } else { // retrieve from instance and restore state of things.
            firstLoad = false;
            progressRunning = savedInstanceState.getBoolean(BundleKeys.PROGRESS_RUNNING.toString(), false);
            mDownloading = savedInstanceState.getBoolean(BundleKeys.DOWNLOADING.toString(), false);
            fab_isOpen = savedInstanceState.getBoolean(BundleKeys.FAB_STATE.toString(), false);
            progressMessage = savedInstanceState.getString(BundleKeys.PROGRESS_MESSAGE.toString());
            //            playerId = savedInstanceState.getString(BundleKeys.PLAYER_ID.toString());
            if (mDownloading) {
                publishProgress(progressMessage);
            }
            playerNameStr = savedInstanceState.getString(BundleKeys.PLAYER_NAME.toString());
            plrAppBar_Name.setText(playerNameStr);
            guildNameStr = savedInstanceState.getString(BundleKeys.GUILDNAME.toString());
            plrAppBar_Guild.setText(guildNameStr);
            planetString = savedInstanceState.getString(PLAYERPLANETTXT);
            txtPlrPlanet.setText(planetString);
            imgVPlanet.setImageDrawable(ImageHelpers.getPlanetImage(planetString, this));
            faction = savedInstanceState.getString(PLAYERFACTION);
            plrAppBar_FactionImage.setImageDrawable(ImageHelpers.factionIcon(faction, this));
            protectedStr = savedInstanceState.getString(PLAYERPROTECTIONSTRING);
            protectionStatus = savedInstanceState.getString(PLAYERPROTECTIONSTATUS);
            protectedColour = savedInstanceState.getInt(PLAYERPROTECTIONCOLOUR);
            txtPlrProtTime.setText(protectedStr);
            txtPlrProtStatus.setText(protectionStatus);
            txtPlrProtStatus.setTextColor(protectedColour);
        }
        Log.d(TAG, "onCreate: ");

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (firstLoad) {

        }
    }

    @Override
    public void finish() {
        super.finish();
        Log.d(TAG, "finish: ");
        playerActivity_background_fragment.killThread();
        getSupportFragmentManager().beginTransaction().remove(playerActivity_background_fragment);
    }

    @Override
    protected void onStart() {
        super.onStart();
        publishProgress("Processing player data...");
        playerActivity_background_fragment.buildPlayerModel(playerId, true, true, true, getApplicationContext());
    }

    @Override
    public void onYesNoDialogNoClicked(String activity_command) {

    }

    @Override
    public void receivePlayerModel(PlayerModel playerModel) {
        try {
            playerNameStr = "";
            faction = playerModel.playerFaction;

            if (faction.equalsIgnoreCase(Factions.EMPIRE.toString())) {
                factionImage = ContextCompat.getDrawable(this, R.mipmap.ic_empire_foreground);

            } else if (faction.equalsIgnoreCase(Factions.REBEL.toString())) {
                factionImage = ContextCompat.getDrawable(this, R.mipmap.ic_rebel_foreground);

            }
            plrAppBar_FactionImage.setImageDrawable(factionImage);
            playerNameStr = StringUtil.htmlRemovedGameName(playerModel.playerName);
            guildNameStr = StringUtil.htmlRemovedGameName(playerModel.guildModel.getGuildName());
            plrAppBar_Guild.setText(guildNameStr);
            plrAppBar_Name.setText(playerNameStr);
            if (playerModel.mplayerModel.protectedUntil() != 0) {
                protectedUntil = playerModel.mplayerModel.playerModel.getInt("protectedUntil");
                protectedStr = "Until: " + DateTimeHelper.longDateTime(protectedUntil, this);
                protectionStatus = "Closed";
                protectedColour = COLOR_RED;
            } else {
                protectedStr = "";
                protectionStatus = "Open";
                protectedColour = COLOR_GREEN;

            }
            txtPlrProtTime.setText(protectedStr);
            txtPlrProtStatus.setText(protectionStatus);
            txtPlrProtStatus.setTextColor(protectedColour);

            planetString = playerModel.planetName;
            txtPlrPlanet.setText(planetString);
            imgVPlanet.setImageDrawable(ImageHelpers.getPlanetImage(playerModel.planetName, this));
            mSectionsStatePagerAdapter.updatePlayerModelInfrags(playerModel);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            hideProgressView();
//            finishDownloading();
            super.finishDownloading();
//            showShortToast("Player Details Refreshed!");
        }
    }

    @Override
    public void receiveBattleLog() {

    }

    @Override
    public void receiveFavourites() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (Integer.parseInt(Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (fab_isOpen) {
            fabButton();
        } else {
            ActivitySwitcher.launchMainActivity(this);
            finish();
        }
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (fab_isOpen) {
            mainOverlay.setVisibility(View.VISIBLE);
            for (FloatingActionButton floatingActionButton : hiddenFabs) {
                floatingActionButton.setClickable(true);
            }

            for (ConstraintLayout constraintLayout : fabHiddenLayouts) {
                constraintLayout.setVisibility(View.VISIBLE);//
                constraintLayout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fab_open_onrestore));
            }

            fab_isOpen = true;

        }

        if (progressRunning) {
            publishProgress(progressMessage);
        }
        playerId = savedInstanceState.getString(BundleKeys.PLAYER_ID.toString());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(BundleKeys.PROGRESS_RUNNING.toString(), progressRunning);
        outState.putBoolean(BundleKeys.DOWNLOADING.toString(), mDownloading);
        outState.putBoolean(BundleKeys.FAB_STATE.toString(), fab_isOpen);
        outState.putString(BundleKeys.PROGRESS_MESSAGE.toString(), progressMessage);
        outState.putString(BundleKeys.PLAYER_ID.toString(), playerId);
        outState.putString(BundleKeys.PLAYER_NAME.toString(), playerNameStr);
        outState.putString(BundleKeys.GUILDNAME.toString(), guildNameStr);
        outState.putString(PLAYERPLANETTXT, planetString);
        outState.putString(BundleKeys.GUILDNAME.toString(), guildNameStr);
        outState.putString(PLAYERFACTION, faction);
        outState.putString(PLAYERPROTECTIONSTATUS, protectionStatus);
        outState.putString(PLAYERPROTECTIONSTRING, protectedStr);
        outState.putInt(PLAYERPROTECTIONCOLOUR, protectedColour);


    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

    }


    private void fabButton() {
//        FrameLayout mainOverlay = (FrameLayout) findViewById(R.id.player_overlay);
        Animation animFabOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        Animation animFabClose = AnimationUtils.loadAnimation(this, R.anim.fab_close);
        Animation animFabClockW = AnimationUtils.loadAnimation(this, R.anim.rotate_clockwise);
        Animation animFabAntiClockW = AnimationUtils.loadAnimation(this, R.anim.rotate_anticlockwise);
        if (fab_isOpen) { //then we are closing it...
            mainOverlay.setVisibility(View.GONE);


            for (ConstraintLayout constraintLayout : fabHiddenLayouts) {
                constraintLayout.startAnimation(animFabClose);
            }
            for (FloatingActionButton floatingActionButton : hiddenFabs) {
                floatingActionButton.setClickable(false);
            }

            fab_Open.startAnimation(animFabAntiClockW);
            fab_isOpen = false;
        } else { //then we are opening it... duh.
            mainOverlay.setVisibility(View.VISIBLE);
            for (FloatingActionButton floatingActionButton : hiddenFabs) {
                floatingActionButton.setClickable(true);
            }
            for (ConstraintLayout constraintLayout : fabHiddenLayouts) {
                constraintLayout.startAnimation(animFabOpen);
            }
            fab_Open.startAnimation(animFabClockW);
            fab_isOpen = true;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        running = false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.player, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.playerDetailsRefresh) {
            publishProgress("Please wait...");
            visitPlayer(playerId);
            return true;
        }
        if (id == android.R.id.home) {
            ActivitySwitcher.launchMainActivity(this);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void sendRefreshCommand() {
        visitPlayer(playerId);
    }


    @Override
    public void removeFavourite(int layoutId) {
        this.layoutId = layoutId;
        Bundle bundle = new Bundle();
        bundle.putString(BundleKeys.DIALOG_TITLE.toString(), "Are you sure?");
        bundle.putString(BundleKeys.DIALOG_MESSAGE.toString(), "Are you sure you want to unmark as a favourite?");
        bundle.putString(BundleKeys.DIALOG_COMMAND.toString(), REMOVE_FAVOURITE);

        yesNoFragment = YesNoFragment.getInstance(getSupportFragmentManager(), bundle);

    }

    @Override
    public void deleteMostLastUsedLayoutLog(int layoutId, int layoutVersion) {
        this.layoutId = layoutId;
        this.layoutVersionID = layoutVersion;
        Bundle bundle = new Bundle();
        bundle.putString(BundleKeys.DIALOG_TITLE.toString(), "Are you sure?");
        bundle.putString(BundleKeys.DIALOG_MESSAGE.toString(), "Are you sure you want to delete this? It will delete from both most and recent used lists.");
        bundle.putString(BundleKeys.DIALOG_COMMAND.toString(), REMOVE_MOSTTOPLAYOUTLOG);

        yesNoFragment = YesNoFragment.getInstance(getSupportFragmentManager(), bundle);
    }

    @Override
    public void showMessage(String title, String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

    }

    @Override
    public void viewBattleDetail(Battle defenceResult) {
        String fragTitle = "Battle Detail";
        if (defenceResult.getBattleType() == BattleType.ATTACK) {
            fragTitle = "Attack Detail";
        } else if (defenceResult.getBattleType() == BattleType.DEFENCE) {
            fragTitle = "Defence Detail";
        }

        BattleDetailFragment battleDetailFragment = new BattleDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(BattleDetailFragment.DEFENCERESULT, defenceResult);
        args.putString(BattleDetailFragment.TITLE, fragTitle);
        battleDetailFragment.setArguments(args);
        battleDetailFragment.show(getSupportFragmentManager(), "battledtail");
    }

    @Override
    public void refreshConflictData() {
        getConflictData(playerId);
    }

    @Override
    public void sendCommand(String command) {


    }


    enum PlayerCommands {DELETE_FAVOURITE, REMOVE_FAV}


    class SavePvpClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            getPVPLayout(playerId);
            fabButton();
        }
    }

    class SaveWarClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            getWARLayout(playerId);
            fabButton();
        }
    }

    class UpdateLayoutClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            ActivitySwitcher.launchLayoutManager(context);
            fabButton();
            finish();
        }
    }

    class WarStatusClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            sendGetWarStatusCall(playerId);
            fabButton();
        }
    }

    class MainOverlayClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (fab_isOpen) {
                fabButton();
            }
        }
    }

    class FabOpenClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            fabButton();
        }
    }


    @Override
    public void playerServiceResult(String command, MethodResult methodResult) {
//        super.playerServiceResult(command, methodResult);
        super.finishDownloading();
        this.command = command;
        if (methodResult.success) {
            if (command.equalsIgnoreCase(GETCONFLICTRANKS)) {
                ArrayList<Conflict_Data_Model> conflictDataList = (ArrayList<Conflict_Data_Model>) methodResult.getResultObject();
                mSectionsStatePagerAdapter.updateConflict(conflictDataList);
            } else if (command.equalsIgnoreCase(PlayerBaseSelection.SAVEWAR)) {
                try {
                    String bundleKey = String.format(ApplicationMessageTemplates.VISIT_KEY.getemplateString(), BundleKeys.LOGIN_RESPONSE.toString(), playerId);
                    BundleHelper bundleHelper = new BundleHelper(bundleKey);
                    SWCLoginResponseData loginResponseData = new SWCLoginResponseData(StringUtil.stringToJsonObject(bundleHelper.get_value(this)));
                    ActivitySwitcher.launchSaveLayoutActivity_Save(methodResult.getMessage(), this.playerId, methodResult.getMessage(), SaveLayoutInterface.SAVE, this);
                    finish();
                } catch (Exception e) {
                    showShortToast(e.getMessage());
                }
            } else if (command.equalsIgnoreCase(VISITPVP)) {
                this.command = PLAYERPROCESSDATA;
                publishProgress("Please wait...");
                playerActivity_background_fragment.buildPlayerModel(playerId, true, true, true, getApplicationContext());
                showShortToast("Player details refreshed");
            } else if (command.equalsIgnoreCase(PlayerBaseSelection.SAVEPVP)) {
                String bundleKey = String.format(ApplicationMessageTemplates.VISIT_KEY.getemplateString(), BundleKeys.VISIT_RESPONSE.toString(), playerId);
                BundleHelper bundleHelper = new BundleHelper(bundleKey);
                SWCVisitResult disneyVisitResult = new SWCVisitResult(bundleHelper.get_value(this));
                String faction = disneyVisitResult.mplayerModel().faction();
                MapBuildings mapBuildings = new MapBuildings(disneyVisitResult.mplayerModel().map());
                ActivitySwitcher.launchSaveLayoutActivity_Save(mapBuildings.asOutputJSON(), playerId, faction, SaveLayoutInterface.SAVE, this);
                finish();
            } else if (command.equalsIgnoreCase(GETWARSTATUS)) {
                finish();
                String warId = methodResult.getMessage();
                ActivitySwitcher.launchWarStatus(playerId, warId, this);
            } else if (command.equalsIgnoreCase(REPAIRDROID)) {
                showLongToast(methodResult.getMessage());
                //refresh view....
                visitPlayer(playerId);
            } else {
                //Default
                showLongToast(methodResult.getMessage());
            }
        } else if (command.equalsIgnoreCase(PlayerBaseSelection.UPDATEPVP) || command.equalsIgnoreCase(PlayerBaseSelection.UPDATEWAR)) {
            showAlertFrag("Update Layout Result", methodResult.getMessage());
        } else if (command.equalsIgnoreCase(REQUEST_PVP)) {
            if (String.valueOf(ServerConstants.STATUS_CODE_TOO_SOON_TO_REQUEST_TROOPS_AGAIN.toInt()).equalsIgnoreCase(methodResult.getMessage())) {
                Bundle bundle = new Bundle();
                bundle.putString(BundleKeys.DIALOG_TITLE.toString(), "Request now?");
                bundle.putString(BundleKeys.DIALOG_MESSAGE.toString(), "Use crystals to request now?");
                bundle.putString(BundleKeys.DIALOG_COMMAND.toString(), REQUESTNOW);

                yesNoFragment = YesNoFragment.getInstance(getSupportFragmentManager(), bundle);
                Log.d(TAG, "playerServiceResult: should be shown???");

            }
        } else {

            if (methodResult.getmException() != null) {
                showLongToast(methodResult.getmException().getMessage());
            } else {
                showLongToast(methodResult.getMessage());
            }
        }
    }

    @Override
    protected void handleListUpdate(String activity_command, boolean success) {
        if (activity_command.equalsIgnoreCase(REMOVE_FAVOURITE)) {
            mSectionsStatePagerAdapter.updateLayout();
        }
    }


    @Override
    public void finishedRendering() {
        this.command = PLAYERPROCESSDATA;
        finishDownloading();
    }


    @Override
    public void layoutSelected(int layoutId, int position) {

    }

    @Override
    public void layoutDeSelected(int layoutId, int position) {

    }

    @Override
    public void repairDroideka(String droideka, String building) {
        showProgressLayout(YODA_PROGRESS);
        swc_server_tasks_fragment.repairDroids(playerId, building, droideka, getApplicationContext());
    }

    @Override
    public void requestTroops(int cap, int donated) {
        if (donated < cap) {
            callMessageTextViewFrag("Request Troops", "Your request:", GETSCREQUESTSTRING, null);

        } else if (donated == cap) {
            showLongToast("SC full!!!");
        }
    }

    @Override
    public void onTextViewDialogPositiveClick(String msg, String cmd) {

        if (cmd.equalsIgnoreCase(GETSCREQUESTSTRING)) {
            scRequestMessage = msg;
            showProgressLayout(YODA_PROGRESS);
            swc_server_tasks_fragment.requestPVPTroops(playerId, msg, false, getApplicationContext());
        }
    }

    @Override
    public void setEquipment() {
        Intent intent = new Intent(this, ArmouryEquipmentActivity.class);
        intent.putExtra(BundleKeys.PLAYER_ID.toString(), playerId);
        startActivity(intent);
        finish();
    }

    @Override
    public void onYesNoDialogYesClicked(String activity_command, Bundle bundle) {
        super.onYesNoDialogYesClicked(activity_command, bundle);

        if (REQUESTNOW.equalsIgnoreCase(activity_command)) {
            showProgressLayout(YODA_PROGRESS);
            swc_server_tasks_fragment.requestPVPTroops(playerId, scRequestMessage, true, getApplicationContext());
        }

    }
}
