package com.swctools.activity_modules.war_room;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.swctools.R;
import com.swctools.activity_modules.war_battles.WarBattles;
import com.swctools.activity_modules.war_battles.models.WarBattleListProvider;
import com.swctools.activity_modules.war_room.background_tasks.WarRoom_Background_Fragment;
import com.swctools.activity_modules.war_room.fragments.WarDashBoard_SectionStatePagerAdaptor;
import com.swctools.activity_modules.war_room.models.War_RoomModel;
import com.swctools.activity_modules.war_room.recycler_adaptors.RecyclerAdaptor_WarOutposts;
import com.swctools.base.BaseActivity;
import com.swctools.common.base_adaptors.PlayerListBaseAdaptor;
import com.swctools.common.enums.ApplicationMessageTemplates;
import com.swctools.common.enums.BundleKeys;
import com.swctools.common.enums.ServerConstants;
import com.swctools.common.helpers.BundleHelper;
import com.swctools.common.helpers.DateTimeHelper;
import com.swctools.common.popups.WarSCTroopsFragment;
import com.swctools.common.popups.YesNoFragment;
import com.swctools.config.AppConfig;
import com.swctools.swc_server_interactions.swc_commands.Cmd_GuildWarStatus;
import com.swctools.util.ActivitySwitcher;
import com.swctools.util.MethodResult;
import com.swctools.util.StringUtil;
import com.swctools.util.Utils;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class WarRoomActivity extends BaseActivity implements WarParticipantInterface, WarDashSignupListInterface, WarOutpostCallBack, WarRoomBackgroundInterface {
    private static final String TAG = "WarDashboard";
    /**
     * The {@link PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link FragmentStatePagerAdapter}.
     */
    private static final String REFRESH_WAR_DATA = "REFRESH_WAR_DATA";
    private static final String GETSCREQUESTSTRING = "GETSCREQUESTSTRING";
    private static final String REQUESTNOW = "REQUESTNOW";
    private static final String WARVS = "WAR! vs. %1s\n\n";
    private static final String PRINTMEMBER = "%1$s (%2$s: %3$s) -\n";
    private static final String PRINTMEMBERWITHUL = "%1$s (%2$s: %3$s) %4$sUL -\n";
    private static final String PRINTMEMBERCLEARED = "%1$s (%2$s: %3$s)\n";
    private static final String KEY_warSquadModel = "KEY_warSquadModel";


    //UI COMPONENTS
    private WarDashBoard_SectionStatePagerAdaptor mSectionsPagerAdapter;
    private WarRoom_Background_Fragment warRoomBackgroundFragment;
    private TabLayout tabs;
    private ViewPager mViewPager;
    private FloatingActionButton warDashbrd_FAB;
    private PlayerListBaseAdaptor playerListBaseAdaptor;
    private Spinner warDashSelectedPlayer;
    private TextView warDash_MySquad;
    private TextView warDash_RivalSquad;
    private TextView warDash_SquadAttacks;
    private TextView warDash_RivalAttacks;
    private TextView warDash_TimeStartEnd;
    private TextView enemySquadScore;
    private TextView plyrSquadScore;
    private RecyclerView neutralPlanetRecycler;
    private RecyclerView enemyOpsRecycler;
    private RecyclerView myOpsRecycler;
    private LinearLayoutManager neutralOutpostsLM;
    private LinearLayoutManager myOutpostsLM;
    private LinearLayoutManager enemylOutpostsLM;

    //RUNTIME VARIABLES
    private War_RoomModel war_roomModel;
    private RecyclerAdaptor_WarOutposts recyclerNeutralOpsAdaptor;
    private RecyclerAdaptor_WarOutposts recycleMyOpsAdaptor;
    private RecyclerAdaptor_WarOutposts recyclerEnemyOpsAdaptor;
    private boolean spinnersPopulated = false;
    private String warId;
    private String command;
    private String tzId;
    private boolean includeTz = true;
    private boolean includeOps = true;
    private boolean includeULS = true;
    private boolean splitList = true;
    private boolean includeScore = false;
    private String scRequestMessage;


    private boolean isDebug = false;

    @Override
    protected void onResume() {
        super.onResume();
        rebuildListData();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_war_room);
        //for the progress indicator\/\/\/\/
        progress_overlay_container = findViewById(R.id.progress_overlay_container_include);
        progress_overlay_bar = findViewById(R.id.progress_overlay_bar);
        progress_overlay_message = findViewById(R.id.progress_overlay_message);
        //for the progress indicator^^^^


        Toolbar toolbar = findViewById(R.id.toolbar);
        mViewPager = findViewById(R.id.wardashViewPager);
        tabs = findViewById(R.id.warDashTabs);
        warDashbrd_FAB = findViewById(R.id.warDashbrd_FAB);
        warDashSelectedPlayer = findViewById(R.id.warDashSelectedPlayer);
        warDash_MySquad = findViewById(R.id.warDash_MySquad);
        warDash_RivalSquad = findViewById(R.id.warDash_RivalSquad);
        warDash_SquadAttacks = findViewById(R.id.warDash_SquadAttacks);
        warDash_RivalAttacks = findViewById(R.id.warDash_RivalAttacks);
        warDash_TimeStartEnd = findViewById(R.id.warDash_TimeStartEnd);
        neutralPlanetRecycler = findViewById(R.id.neutralPlanetRecycler);
        myOpsRecycler = findViewById(R.id.myOpsRecycler);
        enemyOpsRecycler = findViewById(R.id.enemyOpsRecycler);
        plyrSquadScore = findViewById(R.id.plyrSquadScore);
        enemySquadScore = findViewById(R.id.enemySquadScore);

        warRoomBackgroundFragment = WarRoom_Background_Fragment.getInstance(getSupportFragmentManager());

        neutralOutpostsLM = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        neutralPlanetRecycler.setLayoutManager(neutralOutpostsLM);

        enemylOutpostsLM = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        enemyOpsRecycler.setLayoutManager(enemylOutpostsLM);

        myOutpostsLM = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        myOpsRecycler.setLayoutManager(myOutpostsLM);

        playerListBaseAdaptor = new PlayerListBaseAdaptor(this);
        warDashSelectedPlayer.setAdapter(playerListBaseAdaptor);
        long countPlayers = Utils.countPlayers(this);
        if (countPlayers == 1) {
            warDashSelectedPlayer.setSelection(1);
        }
        spinnersPopulated = true;

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mSectionsPagerAdapter = new WarDashBoard_SectionStatePagerAdaptor(getSupportFragmentManager(), "");
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabs.setupWithViewPager(mViewPager);


        //listeners:
        warDashbrd_FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressLayout(YODA_PROGRESS);
                swc_server_tasks_fragment.getWarStatus(playerId, getApplicationContext());
            }
        });
        warDashSelectedPlayer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (spinnersPopulated) {
                    playerId = playerListBaseAdaptor.getPlayerList().get(position).playerId;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                //meh
            }

        });

        //Show disclaimer:
        AppConfig appConfig = new AppConfig(this);
        if (appConfig.getWarDashDisclaimerSetting()) {
            String message = getResources().getString(R.string.wardashdisclaimer);
            showAlertFrag("Important", message);
            appConfig.setGetWarDashDisclaimerSetting(false);
        }

        playerId = getIntent().getStringExtra(BundleKeys.PLAYER_ID.toString());
        warId = getIntent().getStringExtra(BundleKeys.WAR_ID.toString());
        if (StringUtil.isStringNotNull(playerId)) {

            for (int i = 0; i < warDashSelectedPlayer.getCount(); i++) {
                if (playerListBaseAdaptor.getPlayerList().get(i).playerId.equalsIgnoreCase(playerId)) {
                    warDashSelectedPlayer.setSelection(i);
                }
            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_war_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateHeader() {
        warDash_MySquad.setText(StringUtil.getHtmlForTxtBox(war_roomModel.getWarLog().getGuildName()));
        warDash_RivalSquad.setText(StringUtil.getHtmlForTxtBox(war_roomModel.getWarLog().getRivalGuildName()));

        warDash_SquadAttacks.setText(String.valueOf(war_roomModel.getWarLog().getSquadAttacks()));
        warDash_RivalAttacks.setText(String.valueOf(war_roomModel.getWarLog().getRivalAttacks()));
        enemySquadScore.setText(String.valueOf(war_roomModel.getWarLog().getRivalScore()));
        plyrSquadScore.setText(String.valueOf(war_roomModel.getWarLog().getSquadScore()));
        long timeNow = DateTimeHelper.swc_requestTimeStamp();
        String startEndText = "";
        if (war_roomModel.getWarLog().getActionGraceStartTime() < timeNow) {
            startEndText = String.format(ApplicationMessageTemplates.SEMI_COLON_ITEM.getemplateString(), "End Time", "\n" + DateTimeHelper.longDateTime(war_roomModel.getWarLog().getActionGraceStartTime() - 300, this));
        } else {
            startEndText = String.format(ApplicationMessageTemplates.SEMI_COLON_ITEM.getemplateString(), "Start Time", "\n" + DateTimeHelper.longDateTime(war_roomModel.getWarLog().getPrepEndTime(), this));
        }
        warDash_TimeStartEnd.setText(startEndText);

        recyclerNeutralOpsAdaptor = new RecyclerAdaptor_WarOutposts(war_roomModel.getNeutralOps(), this);
        neutralPlanetRecycler.setAdapter(recyclerNeutralOpsAdaptor);
        recyclerNeutralOpsAdaptor.notifyDataSetChanged();

        recycleMyOpsAdaptor = new RecyclerAdaptor_WarOutposts(war_roomModel.getGuildOps(), this);
        myOpsRecycler.setAdapter(recycleMyOpsAdaptor);
        recycleMyOpsAdaptor.notifyDataSetChanged();

        recyclerEnemyOpsAdaptor = new RecyclerAdaptor_WarOutposts(war_roomModel.getRivalOps(), this);
        enemyOpsRecycler.setAdapter(recyclerEnemyOpsAdaptor);
        recyclerEnemyOpsAdaptor.notifyDataSetChanged();

    }

    @Override
    public void playerServiceResult(String command, MethodResult methodResult) {

        if (methodResult.success) {
            if (command.equalsIgnoreCase(GETWARSTATUS)) {
                rebuildListData();
            } else {
                showLongToast(methodResult.getMessage());
            }
        } else if (REQUEST_WAR.equalsIgnoreCase(command)) {
            Log.d(TAG, "playerServiceResult: request command failed!");
            Log.d(TAG, "playerServiceResult: " + methodResult.getMessage());
            if (String.valueOf(ServerConstants.STATUS_CODE_TOO_SOON_TO_REQUEST_TROOPS_AGAIN.toInt()).equalsIgnoreCase(methodResult.getMessage())) {
                Bundle bundle = new Bundle();
                bundle.putString(BundleKeys.DIALOG_TITLE.toString(), "Request now?");
                bundle.putString(BundleKeys.DIALOG_MESSAGE.toString(), "Use crystals to request now?");
                bundle.putString(BundleKeys.DIALOG_COMMAND.toString(), REQUESTNOW);

                yesNoFragment = YesNoFragment.getInstance(getSupportFragmentManager(), bundle);
                Log.d(TAG, "playerServiceResult: should be shown???");

            }
        } else {
            showLongToast(methodResult.getMessage());
        }
        hideProgressView();
    }


    private void rebuildListData() {
        showProgressLayout(YODA_PROGRESS);
        warRoomBackgroundFragment.processWar(warId, getApplicationContext());
    }

    public void handleListUpdate(String activity_command, boolean success) {

    }

    @Override
    public void onBackPressed() {
        if (mDownloading) {
            finishDownloading();
        } else {
            super.onBackPressed();
            this.finish();
            ActivitySwitcher.launchMainActivity(this);
        }
    }

    @Override
    public void showDonatedTroops() {

        Bundle bundle = new Bundle();
        bundle.putString(BundleKeys.PLAYER_ID.toString(), playerId);
        bundle.putString(BundleKeys.WAR_ID.toString(), warId);
        WarSCTroopsFragment warSCTroopsFragment = new WarSCTroopsFragment();

        warSCTroopsFragment.setArguments(bundle);
        warSCTroopsFragment.show(getFragmentManager(), command);
    }

    @Override
    public void showLastDefence(String playerId, String guildId) {
        //check there are defences first...
        if (WarBattleListProvider.countBattlesForWar(warId, getApplicationContext()) > 0) {
            ///then if, load the screen
            Intent intent = new Intent(this, WarBattles.class);
            intent.putExtra(BundleKeys.PLAYER_ID.toString(), playerId);
            intent.putExtra(BundleKeys.WAR_ID.toString(), warId);
            intent.putExtra(BundleKeys.GUILD_ID.toString(), guildId);

            startActivity(intent);
        } else {
            showLongToast("No battles recorded for this war yet!");
        }
    }


    @Override
    public void triggerRebuildList(String tzId, boolean includeTz, boolean includeOps, boolean includeULS, boolean splitList, boolean includeScore, boolean includeBS) {
        this.tzId = tzId;
        this.includeTz = includeTz;
        this.includeOps = includeOps;
        this.includeULS = includeULS;
        this.splitList = splitList;
        this.includeScore = includeScore;

        mSectionsPagerAdapter.updateHitList(war_roomModel.buildHitList(includeTz, includeOps, includeScore, includeULS, splitList, tzId, includeBS, getApplicationContext()));

    }


    @Override
    public void shareText(String s) {
        Utils.shareText(s, this);
    }

    @Override
    public void copyText(String s) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("SWC TOOLS HITLIST", s);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "Text copied!", Toast.LENGTH_LONG).show();
    }


    @Override
    public void showOPNameAndLevel(String s) {
        showShortToast(s);
    }


    @Override
    public void finishDownloading() {
        mDownloading = false;
    }


    @Override
    public void receiveWarRoomModel(War_RoomModel war_roomModel) {
        finishDownloading();
        this.war_roomModel = war_roomModel;
        hideProgressView();
        updateHeader();
        mSectionsPagerAdapter.setPlayerId(playerId);
        mSectionsPagerAdapter.updateMySquadPage(playerId, war_roomModel.getGuildParticipants());
        mSectionsPagerAdapter.updateRivalSquadPage(playerId, war_roomModel.getRivalParticipants());
    }

    @Override
    public void requestSC(String playerId) {
        long timeNow = DateTimeHelper.swc_requestTimeStamp();
        Log.d(TAG, "war_roomModel.getWarLog().getActionGraceStartTime()----> " + war_roomModel.getWarLog().getActionGraceStartTime());
        Log.d(TAG, "timeNow----> " + timeNow);
        if (war_roomModel.getWarLog().getActionGraceStartTime() < timeNow) {
            showLongToast("Too late - the war has started!!");
        } else {
            callMessageTextViewFrag("Request Troops", "Your request:", GETSCREQUESTSTRING, null);

        }
    }

    @Override
    public void onTextViewDialogPositiveClick(String msg, String cmd) {

        if (cmd.equalsIgnoreCase(GETSCREQUESTSTRING)) {
            scRequestMessage = msg;
            showProgressLayout(YODA_PROGRESS);
            swc_server_tasks_fragment.requestWarTroops(playerId, msg, false, getApplicationContext());
        }
    }


    @Override
    public void onYesNoDialogYesClicked(String activity_command, Bundle bundle) {
        super.onYesNoDialogYesClicked(activity_command, bundle);
        Log.d(TAG, "onYesNoDialogYesClicked: " + activity_command);
        Utils.printLogDConcatStrings(activity_command + REQUESTNOW);
        if (REQUESTNOW.equalsIgnoreCase(activity_command)) {
            showProgressLayout(YODA_PROGRESS);
            swc_server_tasks_fragment.requestWarTroops(playerId, scRequestMessage, true, getApplicationContext());
        }

    }
}
