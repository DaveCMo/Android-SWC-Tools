package com.swctools.activity_modules.layout_manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.swctools.R;
import com.swctools.activity_modules.layout_manager.fragments.LayoutManager_Background_Fragment;
import com.swctools.activity_modules.layout_manager.models.LayoutManagerListProvider;
import com.swctools.activity_modules.layout_manager.recycler_adaptors.RecyclerAdaptor_LayoutList;
import com.swctools.activity_modules.main.MainActivity;
import com.swctools.base.BaseLayoutActivity;
import com.swctools.common.enums.BundleKeys;
import com.swctools.common.enums.ScreenCommands.SaveLayoutInterface;
import com.swctools.common.helpers.AppLoggerHelper;
import com.swctools.common.view_adaptors.layout_managers.LayoutList_LinearLayoutManager;
import com.swctools.common.view_adaptors.recycler_adaptors.RecyclerAdaptor_LayoutFolderBreadCrumb;
import com.swctools.config.AppConfig;
import com.swctools.interfaces.FolderBreadCrumbInterface;
import com.swctools.interfaces.LayoutFolderInterface;
import com.swctools.interfaces.LayoutFoldersFragmentInterface;
import com.swctools.interfaces.LayoutTagListPillInterface;
import com.swctools.interfaces.LayoutVersionViewAdaptorInterface;
import com.swctools.interfaces.YesNoAlertCallBack;
import com.swctools.layouts.LayoutFolderHelper;
import com.swctools.layouts.LayoutHelper;
import com.swctools.layouts.fragments.LayoutFoldersFragment;
import com.swctools.layouts.models.LayoutFolderItem;
import com.swctools.layouts.models.LayoutRecord;
import com.swctools.util.ActivitySwitcher;
import com.swctools.util.ImportUtils;
import com.swctools.util.MethodResult;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class LayoutManager extends BaseLayoutActivity implements SearchView.OnQueryTextListener,
        LayoutListInterface,
        YesNoAlertCallBack,
        LayoutFoldersFragmentInterface,
        LayoutFolderInterface,
        FolderBreadCrumbInterface,
        LayoutVersionViewAdaptorInterface,
        LayoutTagListPillInterface, LayoutManager_Background_Fragment.LayoutManager_LongTasks_Interface {
    private static final String TAG = "LayoutManager";
    private static final String VIEWMODE = "VIEWMODE";
    private static final String LIST_UPDATE = "LIST_UPDATE";
    private static final String LIST_REBUILD = "LIST_REBUILD";
    private static final int IMPORT_LAYOUT_JSON_CODE = 42;

    //UI COMPONENTS:

    private FrameLayout mainOverlay;
    public RecyclerView layoutListRecyler;
    public RecyclerView breadcrumbRecycler;
    private FloatingActionButton fab_LayoutManager, fabAddFolderCardAddNewFolder, fabImportLayout;
    private ConstraintLayout fab_AddNewFolderContainer;
    private ConstraintLayout fab_ImportLayoutContainer;
    private Switch imagesOnOff;
    private ImageView layoutManagerFolderUp, deleteSelectedLayoutsBtn, layoutBulkMoveFolders;
    private RadioGroup rdGrpViewBy;
    private RadioButton rdBtnAll;
    private RadioButton rdBtnFolders;
    private TextView countInRecycler;

    private Toolbar toolbar;
    private CheckBox layoutMngSelectAll;

    //DATA/DATA PROVIDERS...
    //RUN TIME VARIABLE
    private ArrayList<LayoutFolderItem> breadcrumbList;
    private ArrayList<Object> layoutRecyclerList;
    private RecyclerAdaptor_LayoutFolderBreadCrumb breadCrumbViewAdaptor;
    private int viewMode;
    private Context context;

    public int visibleItemCount = 0;
    public int totalItemCount = 0;

    public int pastVisibleItems = 0;


    public boolean doFolder = true;
    public boolean isLoading;
    private int countSelected = 0;


    //DISPLAY DATA
    private RecyclerAdaptor_LayoutList layoutListAdaptor;
    //PLAYER DATA
    private String fSearchStr;
    private boolean searched, filtered;


    private boolean mDownloading;
    private String progressMessage;

    private String wildCard = "Any";

    private AppConfig appConfig;


    private LinearLayoutManager breadCrumbLayoutManager;
    private LayoutList_LinearLayoutManager mLayoutManager;
    private LayoutManager_Background_Fragment layoutManagerBackgroundListFragment;
    private boolean fab_isOpen;


    @Override
    protected void onResume() {

        super.onResume();
        try {
            if (viewMode == R.id.rdBtnAll) {
                rdBtnAll.setChecked(true);
            } else if (viewMode == R.id.rdBtnFolders) {
                rdBtnFolders.setChecked(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        layoutManagerBackgroundListFragment.buildMainList(parentFldrId, doFolder, getApplicationContext());
        rebuildBreadCrumbs(true);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Layout Manager");
        setContentView(R.layout.activity_layout_manager);
        appConfig = new AppConfig(this);
        context = this;
        //Set up views
        mainOverlay = findViewById(R.id.layout_overlay);
        layoutListRecyler = findViewById(R.id.layoutListRecyler);
        fab_LayoutManager = findViewById(R.id.fab_LayoutManager);
        fabAddFolderCardAddNewFolder = findViewById(R.id.fabAddFolderCardAddNewFolder);
        fabImportLayout = findViewById(R.id.fabImportLayout);
        fab_AddNewFolderContainer = findViewById(R.id.fab_AddNewFolderContainer);
        fab_ImportLayoutContainer = findViewById(R.id.fab_ImportLayoutContainer);
        layoutManagerFolderUp = findViewById(R.id.layoutManagerFolderUp);
        breadcrumbRecycler = findViewById(R.id.breadcrumbRecycler);
        imagesOnOff = findViewById(R.id.imagesOnOff);
        progress_overlay_bar = findViewById(R.id.progress_overlay_bar);
        progress_overlay_container = findViewById(R.id.progress_overlay_container_include);
        progress_overlay_message = findViewById(R.id.progress_overlay_message);
        rdGrpViewBy = findViewById(R.id.rdGrpViewBy);
        rdBtnFolders = findViewById(R.id.rdBtnFolders);
        rdBtnAll = findViewById(R.id.rdBtnAll);
        toolbar = findViewById(R.id.layoutManagerLayoutToolbar);
        countInRecycler = findViewById(R.id.countInRecycler);
        deleteSelectedLayoutsBtn = findViewById(R.id.deleteSelectedLayoutsBtn);
        layoutMngSelectAll = findViewById(R.id.layoutMngSelectAll);
        layoutBulkMoveFolders = findViewById(R.id.layoutBulkMoveFolders);

        deleteSelectedLayoutsBtn.setVisibility(View.GONE);
        layoutBulkMoveFolders.setVisibility(View.GONE);
        layoutMngSelectAll.setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //Instantiate anything that needs to be instantiated:
        //Breadcrumbs!
        breadcrumbList = new ArrayList<>();
        breadCrumbLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        breadCrumbLayoutManager.setReverseLayout(false);
        breadcrumbRecycler.setLayoutManager(breadCrumbLayoutManager);
        breadcrumbRecycler.setItemAnimator(new DefaultItemAnimator());
        breadcrumbRecycler.setHasFixedSize(false);
        breadCrumbViewAdaptor = new RecyclerAdaptor_LayoutFolderBreadCrumb(breadcrumbList, this);
        breadCrumbViewAdaptor.registerAdapterDataObserver(new BreadCrumbRecyclAdapDataObsvr());
        breadcrumbRecycler.setAdapter(breadCrumbViewAdaptor);

        //Layout List
        layoutRecyclerList = new ArrayList<>();
        mLayoutManager = new LayoutList_LinearLayoutManager(this);
        layoutListRecyler.setLayoutManager(mLayoutManager);
        layoutListRecyler.setHasFixedSize(false);
        layoutListRecyler.setItemAnimator(new DefaultItemAnimator());
        layoutListAdaptor = new RecyclerAdaptor_LayoutList(layoutRecyclerList, context);
        layoutListRecyler.setAdapter(layoutListAdaptor);

//        Get intent data/set default values of controls on Activity:
        playerId = getIntent().getStringExtra(BundleKeys.PLAYER_ID.toString());


        imagesOnOff.setChecked(appConfig.bLayoutImageOn());
        if (appConfig.bLayoutImageOn()) {
            imagesOnOff.setText("On");
        } else {
            imagesOnOff.setText("Off");
        }


        if (savedInstanceState != null) {
            //Get state data
            parentFldrId = savedInstanceState.getInt(BundleKeys.PARENT_LAYOUT_FOLDER_ID.toString());
            layoutId = savedInstanceState.getInt(BundleKeys.LAYOUT_ID.toString());
            fSearchStr = savedInstanceState.getString(BundleKeys.LAYOUT_SEARCHSTR.toString());
            searched = savedInstanceState.getBoolean(BundleKeys.LAYOUT_SEARCHED.toString());
            filtered = savedInstanceState.getBoolean(BundleKeys.LAYOUT_SEARCHED.toString());
            selectedFolder = savedInstanceState.getInt(BundleKeys.LAYOUT_FOLDER_ID.toString(), 0);
            folderToMove = savedInstanceState.getInt(BundleKeys.LAYOUT_FOLDER_TO_MOVE.toString(), 0);
            layoutVersionID = savedInstanceState.getInt(BundleKeys.LAYOUT_VERSION_ID.toString(), 0);
            mDownloading = savedInstanceState.getBoolean(BundleKeys.DOWNLOADING.toString());
            progressMessage = savedInstanceState.getString(BundleKeys.PROGRESS_MESSAGE.toString());
            fab_isOpen = savedInstanceState.getBoolean(BundleKeys.FAB_STATE.toString());
            viewMode = savedInstanceState.getInt(VIEWMODE, R.id.rdBtnAll);

            if (fab_isOpen) {
                fab_isOpen = false;
                fabController();
            }
            breadcrumbList = savedInstanceState.getParcelableArrayList(BundleKeys.LAYOUT_FOLDER_BREADCRUMB_ARRAY.toString());
            rebuildBreadCrumbs(false);
        } else { //This is a brand new instance, or one is not available so create from scratch with default views/settings
            viewMode = R.id.rdBtnFolders;
            parentFldrId = 0;
            selectedFolder = 0;
            LayoutFolderItem layoutFolderItem = new LayoutFolderItem(0, LayoutFolderHelper.ROOTNAME, 0, 0);
            breadcrumbList.add(layoutFolderItem);
            rebuildBreadCrumbs(true);
        }

        if (viewMode == R.id.rdBtnAll) {
            rdBtnAll.setChecked(true);
        } else if (viewMode == R.id.rdBtnFolders) {
            rdBtnFolders.setChecked(true);
        }
        //Define listeners:
        setListeners();
        deleteSelectedLayoutsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressLayout("Deleting...");
                layoutManagerBackgroundListFragment.deleteLayouts(parentFldrId, doFolder, fSearchStr, layoutRecyclerList, getApplicationContext());

            }
        });

        layoutMngSelectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    for (Object o : layoutRecyclerList) {
                        if (o instanceof LayoutRecord) {
                            ((LayoutRecord) o).setSelected(true);
                        }
                    }
                } else {
                    for (Object o : layoutRecyclerList) {
                        if (o instanceof LayoutRecord) {
                            ((LayoutRecord) o).setSelected(false);
                        }
                    }
                    layoutMngSelectAll.setVisibility(View.GONE);
                }
                layoutListAdaptor.notifyDataSetChanged();
            }
        });

        layoutBulkMoveFolders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                selectedFolder = folderId;
                Bundle bundle = new Bundle();
                bundle.putString(BundleKeys.DIALOG_TITLE.toString(), "Move layout to new folder");
                bundle.putString(BundleKeys.DIALOG_MESSAGE.toString(), "Give your new folder a name:");
                bundle.putString(BundleKeys.DIALOG_COMMAND.toString(), MOVELAYOUTFOLDERINBULK);
                bundle.putInt(BundleKeys.LAYOUT_FOLDER_TO_MOVE.toString(), parentFldrId);

                layoutFoldersFragment = LayoutFoldersFragment.getInstance(getSupportFragmentManager());
                layoutFoldersFragment.setArguments(bundle);
            }
        });
        layoutManagerBackgroundListFragment = LayoutManager_Background_Fragment.getInstance(getSupportFragmentManager());
    }

    private void setListeners() {
        layoutManagerFolderUp.setOnClickListener(new UpFolderClick());
        fab_LayoutManager.setOnClickListener(new FabClickListener());
        fabAddFolderCardAddNewFolder.setOnClickListener(new AddNewFolderClick());
        fabImportLayout.setOnClickListener(new ImportLayoutClick());
        mainOverlay.setOnClickListener(new FabClickListener());
        imagesOnOff.setOnCheckedChangeListener(new ImageOnOFf());
        rdGrpViewBy.setOnCheckedChangeListener(new ListViewRadioGroupListener());

        layoutListRecyler.addOnScrollListener(new LayoutListScrollListener());
    }

    @Override
    public void onBackPressed() {
        if (mDownloading) {
            finishDownloading();
        } else {
            if (breadcrumbList.size() > 1) {
                goUpBreadcrumb();
            } else {
                super.onBackPressed();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);

                this.finish();
            }
        }
    }

    //outstanding: when list is updated rather than new
    @Override
    public void finishDownloading() {
        super.finishDownloading();
        hideProgressView();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onStart() {
        super.onStart();
        showProgressLayout(YODA_PROGRESS);
        layoutManagerBackgroundListFragment.buildMainList(parentFldrId, doFolder, getApplicationContext());


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(BundleKeys.DOWNLOADING.toString(), mDownloading);
        outState.putString(BundleKeys.DIALOG_MESSAGE.toString(), progressMessage);
        outState.putBoolean(BundleKeys.LAYOUT_SEARCHED.toString(), searched);
        outState.putString(BundleKeys.LAYOUT_SEARCHSTR.toString(), fSearchStr);
        outState.putInt(BundleKeys.LAYOUT_FOLDER_ID.toString(), selectedFolder);
        outState.putInt(BundleKeys.LAYOUT_FOLDER_TO_MOVE.toString(), folderToMove);
        outState.putInt(BundleKeys.LAYOUT_VERSION_ID.toString(), layoutVersionID);
        outState.putInt(BundleKeys.LAYOUT_ID.toString(), layoutId);
        outState.putBoolean(BundleKeys.FAB_STATE.toString(), fab_isOpen);
        outState.putParcelableArrayList(BundleKeys.LAYOUT_FOLDER_BREADCRUMB_ARRAY.toString(), breadcrumbList);
        outState.putInt(VIEWMODE, viewMode);
        outState.putInt(BundleKeys.PARENT_LAYOUT_FOLDER_ID.toString(), parentFldrId);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted. You can now export your layout!", Toast.LENGTH_SHORT).show();
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.filters, menu);
        MenuItem menuItem = menu.findItem(R.id.layoutSearch);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(this);
        AppLoggerHelper.logStuff("onCreateOptionsMenu", "Done", this);
        return true;
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        newText = newText.toLowerCase();

        fSearchStr = newText;
        if (newText.length() >= 1) {

            layoutManagerBackgroundListFragment.searchLayouts(newText, getApplicationContext());

            searched = true;
        } else {
            layoutManagerBackgroundListFragment.buildMainList(parentFldrId, doFolder, getApplicationContext());
        }
        return false;
    }

    private void fabController() {
        Animation animFabOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        Animation animFabClose = AnimationUtils.loadAnimation(this, R.anim.fab_close);
        Animation animFabClockW = AnimationUtils.loadAnimation(this, R.anim.rotate_clockwise);
        Animation animFabAntiClockW = AnimationUtils.loadAnimation(this, R.anim.rotate_anticlockwise);
        if (fab_isOpen) { //then we are closing it...
            mainOverlay.setVisibility(View.GONE);

            fab_AddNewFolderContainer.startAnimation(animFabClose);
            fab_ImportLayoutContainer.startAnimation(animFabClose);
            fab_LayoutManager.startAnimation(animFabAntiClockW);

            fab_AddNewFolderContainer.setVisibility(View.GONE);
            fab_ImportLayoutContainer.setVisibility(View.GONE);

            fab_AddNewFolderContainer.setClickable(false);
            fab_ImportLayoutContainer.setClickable(false);

            fab_ImportLayoutContainer.clearAnimation();
            fab_AddNewFolderContainer.clearAnimation();

            fab_isOpen = false;
        } else { //then we are opening it... duh.
            mainOverlay.setVisibility(View.VISIBLE);
            fab_AddNewFolderContainer.startAnimation(animFabOpen);
            fab_ImportLayoutContainer.startAnimation(animFabOpen);
            fab_LayoutManager.startAnimation(animFabClockW);
//
            fab_AddNewFolderContainer.setVisibility(View.VISIBLE);
            fab_ImportLayoutContainer.setVisibility(View.VISIBLE);
            fab_AddNewFolderContainer.setClickable(true);
            fab_ImportLayoutContainer.setClickable(true);
            fab_isOpen = true;
        }
        AppLoggerHelper.logStuff("fabController", "Done", this);
    }


    @Override
    public void cancelFolderSelection() {

    }

    @Override
    public void addFolderFromFragment(String newFolder, int parentFldrId) {
        LayoutFolderHelper.addNewFolder(newFolder, parentFldrId, this);
        layoutFoldersFragment.setRecycler(parentFldrId);
        rebuildListController(true);
    }


    private void rebuildBreadCrumbs(boolean refreshData) {
        Log.d(TAG, "rebuildBreadCrumbs: --->" + parentFldrId);

        breadcrumbList.clear();
        breadcrumbList.addAll(LayoutFolderHelper.getFolderHeirarchy(parentFldrId, this));
        breadCrumbViewAdaptor = new RecyclerAdaptor_LayoutFolderBreadCrumb(breadcrumbList, this);
        breadcrumbRecycler.setAdapter(breadCrumbViewAdaptor);

        breadCrumbViewAdaptor.notifyDataSetChanged();

    }


    private void rebuildListController(boolean refreshData) {
        layoutManagerBackgroundListFragment.buildMainList(parentFldrId, doFolder, getApplicationContext());


    }


    @Override
    public void folderSelected(int folderId) {
        parentFldrId = folderId;
        LayoutFolderItem layoutFolderItem = LayoutFolderHelper.getLayoutFolder(parentFldrId, this);
        breadcrumbList.add(layoutFolderItem);
        breadCrumbViewAdaptor.notifyDataSetChanged();
        layoutManagerBackgroundListFragment.buildMainList(parentFldrId, doFolder, getApplicationContext());

    }


    @Override
    public void breadCrumbSelected(int id, int position) {
        while (breadcrumbList.size() > position + 1) {
            breadcrumbList.remove(breadcrumbList.size() - 1);
        }
        breadCrumbViewAdaptor.notifyDataSetChanged();
        parentFldrId = id;
        layoutManagerBackgroundListFragment.buildMainList(parentFldrId, doFolder, getApplicationContext());

    }


    private void goUpBreadcrumb() {
        if (breadcrumbList.size() > 1) {
            parentFldrId = LayoutFolderHelper.getParentFolderId(parentFldrId, getApplicationContext());
            breadcrumbList.remove(breadcrumbList.size() - 1);
            breadCrumbViewAdaptor.notifyDataSetChanged();
            //remove the last one

            rebuildListController(false);
            rebuildBreadCrumbs(false);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {

        if (requestCode == IMPORT_LAYOUT_JSON_CODE && resultCode == Activity.RESULT_OK) {
            MethodResult methodResponse = ImportUtils.processImportLayout(resultData, this);
            if (methodResponse.success) {
                ActivitySwitcher.launchSaveLayoutActivity_Save(methodResponse.getMessage(), "", "", SaveLayoutInterface.SAVE, this);
            } else {
                Toast.makeText(this, methodResponse.getMessage(), Toast.LENGTH_LONG);
            }

        }
    }


    class BreadCrumbRecyclAdapDataObsvr extends RecyclerView.AdapterDataObserver {
        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            breadCrumbLayoutManager.smoothScrollToPosition(breadcrumbRecycler, null, breadCrumbViewAdaptor.getItemCount());
        }

        @Override
        public void onChanged() {
            breadCrumbLayoutManager.smoothScrollToPosition(breadcrumbRecycler, null, breadCrumbViewAdaptor.getItemCount());
        }
    }

    ////******* CLICK LISTENERS
    class AddNewFolderClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (fab_isOpen) {
                callMessageTextViewFrag("New Folder Name", "Give your new folder a name:", ADD_FOLDER, null);
                fabController();
            }
        }
    }

    class ImportLayoutClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            if (fab_isOpen) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                startActivityForResult(intent, IMPORT_LAYOUT_JSON_CODE);
                fabController();
            }
        }
    }

    class UpFolderClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            goUpBreadcrumb();
        }
    }

    class ListViewRadioGroupListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            viewMode = i;
            if (viewMode == R.id.rdBtnAll) {
                doFolder = false;
                parentFldrId = 0;
                rebuildBreadCrumbs(true);
                layoutManagerBackgroundListFragment.getAllLayoutsNoFolder(getApplicationContext());
            } else {
                doFolder = true;
                layoutManagerBackgroundListFragment.buildMainList(parentFldrId, doFolder, getApplicationContext());
            }


        }
    }

    class FabClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            fabController();
        }
    }

    class ImageOnOFf implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            appConfig.setbLayoutImageON(b);
            layoutManagerBackgroundListFragment.buildMainList(parentFldrId, doFolder, getApplicationContext());


        }
    }

    @Override
    public void markFavourite(int layoutId, int position, String favouriteVal) {
        MethodResult setfav = LayoutHelper.setFavourite(layoutId, favouriteVal, this);
        if (setfav.success) {
            LayoutRecord layoutRecord = LayoutManagerListProvider.getLayoutRecord(layoutId, getApplicationContext());
            layoutRecyclerList.set(position, layoutRecord);
            layoutListAdaptor.notifyDataSetChanged();
        } else {
            showLongToast(setfav.getMessage());
        }

    }

    @Override
    protected void handleListUpdate(String activity_command, boolean success) {
        if (activity_command.equalsIgnoreCase(RENAME_FOLDER)) {
            layoutManagerBackgroundListFragment.buildMainList(parentFldrId, doFolder, getApplicationContext());

        } else {
            rebuildListController(true);
            rebuildBreadCrumbs(true);
        }
    }


    class LayoutListScrollListener extends RecyclerView.OnScrollListener {
        //pagination

        @Override
        public void onScrolled(@NonNull final RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            visibleItemCount = mLayoutManager.getChildCount();
            totalItemCount = mLayoutManager.getItemCount();
            pastVisibleItems = mLayoutManager.findFirstCompletelyVisibleItemPosition();

            //            if (!isLoading) {
            //                if (dy > 0) {
            //                    if ((totalItemCount) <= (pastVisibleItems + view_threshold)) {
            //                        recyclerView.stopScroll();
            ////                        mLayoutManager.setScrollEnabled(false);
            //                        page_number++;
            //                        GetNextPageOfLayouts getNextPageOfLayouts = new GetNextPageOfLayouts(parentFldrId, view_threshold, page_number, fSearchStr, LayoutManager.this);
            //                        getNextPageOfLayouts.execute();
            //
            //                        searched = true;
            //                    }
            //                }
            //            }
        }
    }


    @Override
    public void playerServiceResult(String command, MethodResult methodResult) {
        finishDownloading();
        showAlertFrag("Update Layout Result", methodResult.getMessage());
    }

    @Override
    public void layoutSelected(int layoutId, int position) {
        if (layoutRecyclerList.get(position) instanceof LayoutRecord) {
            ((LayoutRecord) layoutRecyclerList.get(position)).setSelected(true);
            layoutListAdaptor.notifyDataSetChanged();
            countSelected++;
            deleteSelectedLayoutsBtn.setVisibility(View.VISIBLE);
            layoutMngSelectAll.setVisibility(View.VISIBLE);
            layoutBulkMoveFolders.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void layoutDeSelected(int layoutId, int position) {
        if (layoutRecyclerList.get(position) instanceof LayoutRecord) {
            ((LayoutRecord) layoutRecyclerList.get(position)).setSelected(false);
            layoutListAdaptor.notifyDataSetChanged();
            if (countSelected > 0) {
                countSelected--;
            }
            if (countSelected == 0) {
                deleteSelectedLayoutsBtn.setVisibility(View.GONE);
                layoutMngSelectAll.setVisibility(View.GONE);
                layoutBulkMoveFolders.setVisibility(View.GONE);
            }

        }
    }

    @Override
    public void itemsDeleted(ArrayList<Object> objects) {
    }

    @Override
    public void receiveNewList(ArrayList<Object> objects) {
        deleteSelectedLayoutsBtn.setVisibility(View.GONE);
        layoutMngSelectAll.setVisibility(View.GONE);
        layoutBulkMoveFolders.setVisibility(View.GONE);

        int countLayouts = 0;
        if (layoutRecyclerList == null) {
            layoutRecyclerList = new ArrayList<>();
        }
        layoutListAdaptor.replaceListItems(objects);

        for (int i = 0; i < layoutRecyclerList.size(); i++) {
            if (layoutRecyclerList.get(i) instanceof LayoutRecord) {
                countLayouts++;
            }
        }
        countInRecycler.setText("Layouts: " + countLayouts);

        isLoading = false;
        finishDownloading();

    }

    @Override
    public void moveLayoutToNewFolder(int layoutID, int folderId) {
        this.layoutId = layoutID;
        selectedFolder = folderId;
        Bundle bundle = new Bundle();
        bundle.putString(BundleKeys.DIALOG_TITLE.toString(), "Move layout to new folder");
        bundle.putString(BundleKeys.DIALOG_MESSAGE.toString(), "Give your new folder a name:");
        bundle.putString(BundleKeys.DIALOG_COMMAND.toString(), MOVELAYOUTFOLDER);
        bundle.putInt(BundleKeys.LAYOUT_FOLDER_TO_MOVE.toString(), selectedFolder);

        layoutFoldersFragment = LayoutFoldersFragment.getInstance(getSupportFragmentManager());
        layoutFoldersFragment.setArguments(bundle);
    }

    @Override
    public void confirmFolderSelection(int folderId, String cmd) {
        parentFldrId = folderId;
        command = cmd;
        if (cmd.equalsIgnoreCase(MOVE_FOLDER)) {
            LayoutFolderHelper.moveFolder(folderToMove, folderId, this);
            showShortToast("Folder Moved!");
            handleListUpdate(command, true);
        } else if (cmd.equalsIgnoreCase(MOVELAYOUTFOLDER)) {
            LayoutHelper.updateLayoutFolder(layoutId, folderId, this);
            showShortToast("Layout Moved!");
            handleListUpdate(command, true);
        } else if (cmd.equalsIgnoreCase(MOVELAYOUTFOLDERINBULK)) {
            showProgressLayout("Moving...");
            rebuildBreadCrumbs(true);
            layoutManagerBackgroundListFragment.moveLayouts(parentFldrId, folderId, doFolder, fSearchStr, layoutRecyclerList, getApplicationContext());
        }
    }
}