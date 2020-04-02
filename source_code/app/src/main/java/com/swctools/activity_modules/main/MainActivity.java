package com.swctools.activity_modules.main;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.swctools.R;
import com.swctools.activity_modules.Splash;
import com.swctools.activity_modules.main.fagments.FragmentGettingStarted;
import com.swctools.activity_modules.main.fagments.FragmentHome;
import com.swctools.activity_modules.main.fagments.MainActivity_BackgroundFragment;
import com.swctools.activity_modules.main.models.PlayerDAO_WithLayouts;
import com.swctools.activity_modules.updates.UpdateService;
import com.swctools.base.BaseLayoutActivity;
import com.swctools.common.enums.ApplicationMessageTemplates;
import com.swctools.common.enums.BundleKeys;
import com.swctools.common.enums.ScreenCommands.SaveLayoutInterface;
import com.swctools.common.helpers.BundleHelper;
import com.swctools.common.models.player_models.MapBuildings;
import com.swctools.common.popups.YesNoFragment;
import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;
import com.swctools.interfaces.FavouriteLayoutRowItemInterface;
import com.swctools.interfaces.PlayerListInterface;
import com.swctools.interfaces.YesNoAlertCallBack;
import com.swctools.layouts.models.FavouriteLayoutItem;
import com.swctools.swc_server_interactions.results.SWCLoginResponseData;
import com.swctools.swc_server_interactions.results.SWCVisitResult;
import com.swctools.util.ActivitySwitcher;
import com.swctools.util.ImportUtils;
import com.swctools.util.MethodResult;
import com.swctools.util.StringUtil;
import com.swctools.util.Utils;

import java.util.List;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends BaseLayoutActivity
        implements
        NavigationView.OnNavigationItemSelectedListener,
        PlayerListInterface,
        YesNoAlertCallBack,
        FavouriteLayoutRowItemInterface,
        MainHomeInterface {
    private static final int IMPORT_LAYOUT_JSON_CODE = 42;
    private static String TAG = "MAINACTIVITY";
    private static MainActivity parent;
    volatile boolean mDownloading = false;
    private String playerId;
    private int mDownloadAction;
    private String progressMessage;
    private YesNoFragment yesNoFragment;

    private FloatingActionButton fab_addPlayer;
    private FragmentHome fragmentHome;
    private FragmentGettingStarted gettingStartedFragment;
    private FragmentManager fm;
    private boolean progressRunning;
    private boolean running;

    private String proposedLayout;

    private MainActivity_BackgroundFragment mainActivityAsync;

    private String HOME_FRAGMENT = "HOME_FRAGMENT";
    private String GETTING_STARTED_FRAGMENT = "GETTING_STARTED_FRAGMENT";

    private TextView appVersionText;


    private Handler mainActivityHandler;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mainActivityAsync.buildPlayerList();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent updateIntent = new Intent(this, UpdateService.class);
        updateIntent.putExtra(BundleKeys.SILENT_UPDATE.toString(), true);
        startService(updateIntent);

        fragmentHome = new FragmentHome();
        gettingStartedFragment = new FragmentGettingStarted();
        fm = getSupportFragmentManager();
        setFragment();

        progress_overlay_bar = findViewById(R.id.progress_overlay_bar);
        progress_overlay_container = findViewById(R.id.progress_overlay_container_include);
        progress_overlay_message = findViewById(R.id.progress_overlay_message);
        appVersionText = findViewById(R.id.appVersionText);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);


        fab_addPlayer = (FloatingActionButton) findViewById(R.id.fab);
        fab_addPlayer.setOnClickListener(new FabOnClickListener());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        if (savedInstanceState != null) {
            progressRunning = savedInstanceState.getBoolean(BundleKeys.PROGRESS_RUNNING.toString(), false);
            if (progressRunning) {
                publishProgress("");
            }
        }
        running = true;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }

        try {
            PackageInfo pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            appVersionText.setText(pinfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
        }
        mainActivityAsync = MainActivity_BackgroundFragment.getInstance(getSupportFragmentManager());

        mainActivityHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                showProgressLayout("Hello from thread" + msg.what);
            }
        };
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
//                    exportLayout();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void setFragment() {
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.main_FrameLayout, fragmentHome, HOME_FRAGMENT);
        ft.commit();

        //Let's see which one we need to show:

//        Cursor cursor = DBSQLiteHelper.queryDB(DatabaseContracts.PlayersTable.TABLE_NAME, this);
//
//        if (cursor.getCount() > 0) {
//            FragmentTransaction ft = fm.beginTransaction();
//            ft.replace(R.id.main_FrameLayout, fragmentHome, HOME_FRAGMENT);
//            ft.commit();
//        } else {
//            FragmentTransaction ft = fm.beginTransaction();
//            ft.replace(R.id.main_FrameLayout, gettingStartedFragment, GETTING_STARTED_FRAGMENT);
//            ft.commit();
//        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        running = false;

    }


    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            ActivitySwitcher.launchSettings(this);
//            testSomeShit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.drawer_import_layout) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            startActivityForResult(intent, IMPORT_LAYOUT_JSON_CODE);
        } else if (id == R.id.drawer_layout_manager) {
            ActivitySwitcher.launchLayoutManager(this);
            finish();
        } else if (id == R.id.drawer_logs) {
            ActivitySwitcher.launchLogs(this);
            finish();
        } else if (id == R.id.drawer_settings) {
            ActivitySwitcher.launchSettings(this);
            finish();
        } else if (id == R.id.drawer_update) {
            Intent intent = new Intent(this, UpdateService.class);
            startService(intent);
            showShortToast("Checking for updates. Check your notifications for progress");
        } else if (id == R.id.drawer_force_update) {

            try {

                DBSQLiteHelper.deleteDbRows(DatabaseContracts.DataVersionUpdateHistory.TABLE_NAME, null, null, this);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {

            }
            Intent intent = new Intent(this, UpdateService.class);
            startService(intent);
            showShortToast("Forcing updates. Check your notifications for progress");
        } else if (id == R.id.drawer_say_thanks) {
            ActivitySwitcher.launchSayThanks(this);

        } else if (id == R.id.drawer_defence_tracker) {
            ActivitySwitcher.launchDefenceTrackerSettings(this);
        } else if (id == R.id.drawer_manage_tags) {
            ActivitySwitcher.launchManageTags(this);

        } else if (id == R.id.drawer_war_signup) {
            ActivitySwitcher.launchWarSignUp(this);

        } else if (id == R.id.drawer_war_room) {
            ActivitySwitcher.launchWarDash(this);
            finish();
        } else if (id == R.id.drawer_about) {
            ActivitySwitcher.launchAbout(this);
            finish();
        } else if (id == R.id.drawer_release_notes) {
            ActivitySwitcher.launchReleaseNotes(this);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }


    private void testSomeShit() {

        final Context context = getApplicationContext();
        AsyncTask<String, Integer, MethodResult> asyncTask = new AsyncTask<String, Integer, MethodResult>() {
            @Override
            protected MethodResult doInBackground(String... strings) {

                return new MethodResult(true, "");


            }

            @Override
            protected void onPostExecute(MethodResult o) {
                showAlertFrag("Message", o.getMessage());
            }
        };

        Intent intent = new Intent(this, Splash.class);
        startActivity(intent);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {

        if (requestCode == IMPORT_LAYOUT_JSON_CODE && resultCode == Activity.RESULT_OK) {
            MethodResult methodResponse = ImportUtils.processImportLayout(resultData, this);
            if (methodResponse.success) {
                ActivitySwitcher.launchSaveLayoutActivity_Save(methodResponse.getMessage(), "", "", SaveLayoutInterface.SAVE, this);
            } else {
                showShortToast(methodResponse.getMessage());
            }

        }
    }


    @Override
    public void viewPlayer(String playerId) {
        this.playerId = playerId;
        visitPlayer(playerId);
    }

    @Override
    public void savePVP(String playerId) {
        this.playerId = playerId;
        getPVPLayout(playerId);
    }

    @Override
    public void saveWar(String playerId) {
        this.playerId = playerId;
        getWARLayout(playerId);
    }


    @Override
    public void deletePlayer(int rowId) {
        player_RowId = rowId;
        Bundle bundle = new Bundle();
        bundle.putString(BundleKeys.DIALOG_TITLE.toString(), "Are you sure?");
        bundle.putString(BundleKeys.DIALOG_MESSAGE.toString(), "Are you sure you want to delete this player?");
        bundle.putString(BundleKeys.DIALOG_COMMAND.toString(), DELETE_PLAYER);

        yesNoFragment = YesNoFragment.getInstance(getSupportFragmentManager(), bundle);


    }

    @Override
    public void viewPlayerConfig(String playerId) {
        ActivitySwitcher.launchPlayerConfig(playerId, this);
    }

    @Override
    public void playerServiceResult(String command, MethodResult methodResult) {
        Log.d(TAG, "playerServiceResult: " + command);
        finishDownloading();
        if (methodResult.success) {
            if (command.equalsIgnoreCase(PlayerBaseSelection.SAVEWAR)) {

                try {

                    String bundleKey = String.format(ApplicationMessageTemplates.VISIT_KEY.getemplateString(), BundleKeys.LOGIN_RESPONSE.toString(), playerId);
                    BundleHelper bundleHelper = new BundleHelper(bundleKey);
                    SWCLoginResponseData loginResponseData = new SWCLoginResponseData(StringUtil.stringToJsonObject(bundleHelper.get_value(this)));
                    ActivitySwitcher.launchSaveLayoutActivity_Save(methodResult.getMessage(), this.playerId, methodResult.getMessage(), SaveLayoutInterface.SAVE, this);
                } catch (Exception e) {
                    showShortToast(e.getMessage());
                }
            } else if (command.equalsIgnoreCase(VISITPVP)) {

                ActivitySwitcher.launchPlayerDetails(playerId, this);
            } else if (command.equalsIgnoreCase(PlayerBaseSelection.SAVEPVP)) {
                String bundleKey = String.format(ApplicationMessageTemplates.VISIT_KEY.getemplateString(), BundleKeys.VISIT_RESPONSE.toString(), playerId);
                BundleHelper bundleHelper = new BundleHelper(bundleKey);
                SWCVisitResult disneyVisitResult = new SWCVisitResult(bundleHelper.get_value(this));
                String faction = disneyVisitResult.mplayerModel().faction();
                MapBuildings mapBuildings = new MapBuildings(disneyVisitResult.mplayerModel().map());

                ActivitySwitcher.launchSaveLayoutActivity_Save(mapBuildings.asOutputJSON(), playerId, faction, SaveLayoutInterface.SAVE, this);

            } else if (command.equalsIgnoreCase(GETWARSTATUS)) {

                String warId = methodResult.getMessage();
                ActivitySwitcher.launchWarStatus(playerId, warId, this);
            } else if (command.equalsIgnoreCase(PlayerBaseSelection.UPDATEPVP) || command.equalsIgnoreCase(PlayerBaseSelection.UPDATEWAR) || command.equalsIgnoreCase(UPDATELAYOUT)) {
                showAlertFrag("Update Layout Result", methodResult.getMessage());
            }
        } else {

            if (methodResult.getmException() != null) {
                showLongToast(methodResult.getmException().getMessage());
            } else {
                showLongToast(methodResult.getMessage());
            }
        }
    }


    class FabOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            finish();
            ActivitySwitcher.launchAddPlayer(context);
        }
    }

    @Override
    protected void handleListUpdate(String activity_command, boolean success) {
        mainActivityAsync.buildPlayerList();
    }

    @Override
    public void setExpanded(String playerId, int i) {
        String whereClause = DatabaseContracts.PlayersTable.PLAYERID + " = ?";
        String[] whereArg = {playerId};
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContracts.PlayersTable.CARD_EXPANDED, i);
        DBSQLiteHelper.updateData(DatabaseContracts.PlayersTable.TABLE_NAME, contentValues, whereClause, whereArg, this);
    }

    @Override
    public void setPlayerFavouriteList(String playerId, String listOption) {
        String whereClause = DatabaseContracts.PlayersTable.PLAYERID + " = ?";
        String[] whereArgs = {playerId};
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContracts.PlayersTable.DEFAULT_FAV, listOption);
        DBSQLiteHelper.updateData(DatabaseContracts.PlayersTable.TABLE_NAME, contentValues, whereClause, whereArgs, this);
    }


    @Override
    public void startedBuildingPlayerList() {
        publishProgress(YODA_PROGRESS);


    }

    @Override
    public void finishedBuildingPlayerList(List<PlayerDAO_WithLayouts> playerDAOList, List<FavouriteLayoutItem> favouriteLayoutItemList) {
        fragmentHome.setPlayerListRecyler(playerDAOList, favouriteLayoutItemList);
        hideProgressView();
    }

    @Override
    public void getWarStatus(String playerId) {
        this.playerId = playerId;
        sendGetWarStatusCall(playerId);
    }

    @Override
    public void layoutSelected(int layoutId, int position) {

    }

    @Override
    public void layoutDeSelected(int layoutId, int position) {

    }
}

