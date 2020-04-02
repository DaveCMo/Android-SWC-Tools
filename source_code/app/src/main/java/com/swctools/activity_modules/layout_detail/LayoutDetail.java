package com.swctools.activity_modules.layout_detail;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.swctools.base.BaseLayoutActivity;
import com.swctools.R;
import com.swctools.common.popups.YesNoFragment;
import com.swctools.common.base_adaptors.FactionListBaseAdaptor;
import com.swctools.common.base_adaptors.PlayerListBaseAdaptor;
import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;
import com.swctools.common.enums.BundleKeys;
import com.swctools.common.enums.DatabaseMethods;
import com.swctools.layouts.LayoutFolderHelper;
import com.swctools.layouts.LayoutHelper;
import com.swctools.common.helpers.PlayerFavouriteLayoutHelper;
import com.swctools.activity_modules.layout_detail.models.LayoutDetail_ImageListItem;
import com.swctools.activity_modules.layout_detail.models.LayoutImage_ListProvider;
import com.swctools.activity_modules.multi_image_picker.ImageListInterface;
import com.swctools.interfaces.PlayerTopLayoutSelectionInterface;
import com.swctools.activity_modules.layout_manager.models.LayoutManagerListProvider;
import com.swctools.layouts.models.LayoutFolderItem;
import com.swctools.layouts.models.LayoutRecord;
import com.swctools.layouts.models.LayoutTag;
import com.swctools.util.ActivitySwitcher;
import com.swctools.util.MethodResult;
import com.swctools.util.StringUtil;
import com.swctools.activity_modules.layout_detail.view_adaptors.RecyclerAdaptor_Image;
import com.swctools.common.view_adaptors.recycler_adaptors.RecyclerAdaptor_LayoutFolderBreadCrumb;
import com.swctools.activity_modules.layout_manager.recycler_adaptors.RecyclerAdaptor_LayoutTagPillList;
import com.swctools.activity_modules.layout_detail.view_adaptors.RecyclerAdaptor_LayoutVersion;
import com.swctools.common.view_adaptors.recycler_adaptors.RecyclerAdaptor_PlayerSelected;

import java.io.File;
import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LayoutDetail extends BaseLayoutActivity implements
        ImageListInterface,
        PlayerTopLayoutSelectionInterface {

    //ACTIVITY CONSTANTS:
    private static final String TAG = "LayoutDetail";
    private static final String CMD_EDIT_NAME = "EDIT_NAME";
    private static final String CMD_EDIT_LABEL = "CMD_EDIT_LABEL";
    private static final String KEY_LAYOUTRECORD = "KEY_LAYOUTRECORD";
    private static final String KEY_LAYOUTID = "KEY_LAYOUTID";
    private static final String DELETE_IMAGE = "DELETE_IMAGE";

    //Runtime temp values:
    private LayoutRecord layoutRecord;
    private boolean changed = false;
    private FactionListBaseAdaptor factionListBaseAdaptor;
    private PlayerListBaseAdaptor playerListBaseAdaptor;
    private RecyclerAdaptor_Image recyclerAdaptor_image;
    private boolean spinnersPopulated = false;
    private Context mContext;
    private ArrayList<LayoutFolderItem> layoutFolderItems;
    private long selectedImageId;

    //UI
    private TextView layoutDetail_Name;
    private ImageView layoutImage;
    private Spinner spnrlayoutPlayer, spnrlayoutFaction;
    private RecyclerView imageRecycler, layout_detail_folder_recycler, layout_detail_tag_recycler, layoutVersionRecycler, layoutPlayerTopRecycler;
    private FloatingActionButton fab_main_pic;
    private Button addGallery, editFolderBtn, editTagBtn;
    private Menu mOptionsMenu;


    //RECYCLER ADAPTORS S ETC
    private RecyclerAdaptor_LayoutFolderBreadCrumb recyclerAdaptor_layoutFolderBreadCrumb;
    private RecyclerAdaptor_LayoutTagPillList recyclerAdaptor_layoutTagPillList;
    private LinearLayoutManager folderLinearLayoutManager, topLayoutsLinearLayoutManager, versionsLinearLayoutManager, tagLinearLayoutManager;
    private GridLayoutManager tagLayoutManager;
    private GridLayoutManager gridImageLayoutManager;
    private RecyclerAdaptor_LayoutVersion recyclerAdaptor_layoutVersion;
    private RecyclerAdaptor_PlayerSelected recyclerAdaptor_playerSelected;

    @Override
    protected void onResume() {
        super.onResume();
        //General stuff
        layoutRecord = LayoutManagerListProvider.getLayoutRecord(layoutId, this);
        if (StringUtil.isStringNotNull(layoutRecord.getImageURIStr())) {
            File file = new File(layoutRecord.getImageURIStr());
            layoutImage.setImageURI(Uri.fromFile(file));

        }
        layoutDetail_Name.setText(layoutRecord.getLayoutName());
        setPlayerSpinner(layoutRecord.getLayoutPlayerId());
        setLayoutFactionSpinner(layoutRecord.getLayoutFaction());
        spinnersPopulated = true;
        setTitle("Layout Detail ID:" + layoutId);
//        setTitle("Layout Detail");

        imageRecycler.setLayoutManager(gridImageLayoutManager);
        ArrayList<LayoutDetail_ImageListItem> imagebytesArray = LayoutImage_ListProvider.getLayoutDetail_imageListItems(layoutId, this);
        recyclerAdaptor_image = new RecyclerAdaptor_Image(imagebytesArray, this);
        imageRecycler.setAdapter(recyclerAdaptor_image);
        recyclerAdaptor_image.notifyDataSetChanged();

        //Folders:
        layout_detail_folder_recycler.setLayoutManager(folderLinearLayoutManager);
        layoutFolderItems = LayoutFolderHelper.getFolderHeirarchy(layoutRecord.getLayoutFolderId(), this);
        recyclerAdaptor_layoutFolderBreadCrumb = new RecyclerAdaptor_LayoutFolderBreadCrumb(layoutFolderItems, this);
        layout_detail_folder_recycler.setAdapter(recyclerAdaptor_layoutFolderBreadCrumb);
        recyclerAdaptor_layoutFolderBreadCrumb.notifyDataSetChanged();

        //Tags:
//        layout_detail_tag_recycler.setLayoutManager(tagLayoutManager); for grid
        layout_detail_tag_recycler.setLayoutManager(tagLinearLayoutManager);
        selectedTags = layoutRecord.getLayoutTags();
        recyclerAdaptor_layoutTagPillList = new RecyclerAdaptor_LayoutTagPillList(selectedTags, false, this);
        layout_detail_tag_recycler.setAdapter(recyclerAdaptor_layoutTagPillList);
        recyclerAdaptor_layoutTagPillList.notifyDataSetChanged();

        //Versions:
        layoutVersionRecycler.setLayoutManager(versionsLinearLayoutManager);
        recyclerAdaptor_layoutVersion = new RecyclerAdaptor_LayoutVersion(LayoutHelper.getLayoutVersions(layoutId, this), this);
        layoutVersionRecycler.setAdapter(recyclerAdaptor_layoutVersion);
        recyclerAdaptor_layoutVersion.notifyDataSetChanged();

        //Top selected:

        layoutPlayerTopRecycler.setLayoutManager(topLayoutsLinearLayoutManager);
        recyclerAdaptor_playerSelected = new RecyclerAdaptor_PlayerSelected(PlayerFavouriteLayoutHelper.getPlayersforLayout(layoutId, this), this);
        layoutPlayerTopRecycler.setAdapter(recyclerAdaptor_playerSelected);
        recyclerAdaptor_playerSelected.notifyDataSetChanged();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mContext = this;
//Set up UI elements:
        layoutImage = findViewById(R.id.layoutImage);
        layoutDetail_Name = findViewById(R.id.layoutDetail_Name);
        spnrlayoutPlayer = findViewById(R.id.spnrlayoutPlayer);
        spnrlayoutFaction = findViewById(R.id.spnrlayoutFaction);
        fab_main_pic = findViewById(R.id.fab_main_pic);
        imageRecycler = findViewById(R.id.imageRecycler);
        editFolderBtn = findViewById(R.id.editFolderBtn);
        editTagBtn = findViewById(R.id.editTagBtn);
        addGallery = findViewById(R.id.addGallery);
        layout_detail_folder_recycler = findViewById(R.id.layout_detail_folder_recycler);
        layout_detail_tag_recycler = findViewById(R.id.layout_detail_tag_recycler);
        layoutVersionRecycler = findViewById(R.id.layoutVersionRecycler);
        layoutPlayerTopRecycler = findViewById(R.id.layoutPlayerTopRecycler);

        gridImageLayoutManager = new GridLayoutManager(this, 2);

        folderLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        tagLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        tagLayoutManager = new GridLayoutManager(this, 5, RecyclerView.VERTICAL, false);
        versionsLinearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        topLayoutsLinearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);

        layoutPlayerTopRecycler.addItemDecoration(new DividerItemDecoration(layoutPlayerTopRecycler.getContext(), DividerItemDecoration.VERTICAL));


        //for the progress indicator:
        progress_overlay_container = findViewById(R.id.progress_overlay_container_include);
        progress_overlay_bar = findViewById(R.id.progress_overlay_bar);
        progress_overlay_message = findViewById(R.id.progress_overlay_message);
        if (savedInstanceState != null) {
            layoutId = savedInstanceState.getInt(KEY_LAYOUTID);
        } else {
            layoutId = getIntent().getIntExtra(BundleKeys.LAYOUT_ID.toString(), 0);
        }
        setControlListeners();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        mOptionsMenu = menu;
        getMenuInflater().inflate(R.menu.layout_detail_toolbar_menu, menu);
        setLayoutFavouriteIcon();
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.layout_detail_favourite) {

            if (layoutRecord.getLayoutIsFavourite().equalsIgnoreCase(LayoutHelper.YES)) {
                LayoutHelper.setFavourite(layoutId, LayoutHelper.NO, this);
            } else {
                LayoutHelper.setFavourite(layoutId, LayoutHelper.YES, this);
            }
            setLayoutFavouriteIcon();
        } else if (item.getItemId() == android.R.id.home) {// Press Back Icon
            finish();
        }


        return super.onOptionsItemSelected(item);
    }

    private void setLayoutFavouriteIcon() {

        MenuItem menuItem = mOptionsMenu.findItem(R.id.layout_detail_favourite);
        if (layoutRecord.getLayoutIsFavourite().equalsIgnoreCase(LayoutHelper.YES)) {
            menuItem.setIcon(R.drawable.ic_star_white_24dp);
        } else {
            menuItem.setIcon(R.drawable.ic_star_borderwhite_24dp);
        }
    }

    private void setControlListeners() {

        layoutDetail_Name.setOnClickListener(new NameClickListener());
        spnrlayoutPlayer.setOnItemSelectedListener(new PlayerSpinnerChanged());
        spnrlayoutFaction.setOnItemSelectedListener(new FactionSpinnerChanged());
        fab_main_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivitySwitcher.launchPickImages(1, DatabaseContracts.LayoutTable.COLUMN_ID, layoutId, DatabaseContracts.LayoutTable.TABLE_NAME, DatabaseContracts.LayoutTable.LAYOUT_IMAGE, "", DatabaseMethods.UPDATE, mContext);
            }
        });
        addGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivitySwitcher.launchPickImages(6, DatabaseContracts.LayoutImages.LAYOUT_ID, layoutId, DatabaseContracts.LayoutImages.TABLE_NAME, DatabaseContracts.LayoutImages.IMAGE_LOCATION, DatabaseContracts.LayoutImages.IMAGE_LABEL, DatabaseMethods.IMAGEPICK, mContext);
            }
        });

        editFolderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSetFolderFragment(layoutRecord.getLayoutFolderId());
            }
        });
        editTagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSetTagFragment();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_LAYOUTID, layoutId);
    }


    private void setPlayerSpinner(@Nullable String playerId) {
        spinnersPopulated = false;
        playerListBaseAdaptor = new PlayerListBaseAdaptor(this);
        spnrlayoutPlayer.setAdapter(playerListBaseAdaptor);
        if (StringUtil.isStringNotNull(playerId)) {
            for (int i = 0; i < spnrlayoutPlayer.getCount(); i++) {
                if (playerListBaseAdaptor.getPlayerList().get(i).playerId.equalsIgnoreCase(playerId)) {
                    spnrlayoutPlayer.setSelection(i);
                }
            }
        }
    }

    private void setLayoutFactionSpinner(@Nullable String playerFaction) {
        spinnersPopulated = false;
        factionListBaseAdaptor = new FactionListBaseAdaptor(this);
        spnrlayoutFaction.setAdapter(factionListBaseAdaptor);
        try {
            if (StringUtil.isStringNotNull(playerFaction)) {
                for (int i = 0; i < spnrlayoutFaction.getCount(); i++) {
                    if (factionListBaseAdaptor.getFactions()[i].equalsIgnoreCase(playerFaction)) {
                        spnrlayoutFaction.setSelection(i);
                    }
                }
            }
        } catch (Exception e) {

        }

    }

    @Override
    public void onTextViewDialogPositiveClick(String msg, String cmd) {
        changed = true;
        if (cmd.equalsIgnoreCase(CMD_EDIT_NAME)) {
            layoutRecord.setLayoutName(msg);
            LayoutHelper.updateLayoutName(layoutId, msg, context);
            layoutDetail_Name.setText(msg);
        } else if (cmd.equalsIgnoreCase(CMD_EDIT_LABEL)) {
            String whereClause = DatabaseContracts.LayoutImages.COLUMN_ID + " = ?";
            String[] whereArg = {String.valueOf(selectedImageId)};
            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseContracts.LayoutImages.IMAGE_LABEL, msg);
            DBSQLiteHelper.updateData(DatabaseContracts.LayoutImages.TABLE_NAME, contentValues, whereClause, whereArg, this);
            ArrayList<LayoutDetail_ImageListItem> imagebytesArray = LayoutImage_ListProvider.getLayoutDetail_imageListItems(layoutId, this);
            recyclerAdaptor_image = new RecyclerAdaptor_Image(imagebytesArray, this);
            imageRecycler.setAdapter(recyclerAdaptor_image);
            recyclerAdaptor_image.notifyDataSetChanged();
        }
    }


    class NameClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            callMessageTextViewFrag("Set Layout Name", "", CMD_EDIT_NAME, layoutRecord.getLayoutName());
        }
    }

    class PlayerSpinnerChanged implements AdapterView.OnItemSelectedListener {
        int selectedTally = 0;

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

            if (selectedTally > 0) {
                changed = true;
                LayoutHelper.updateLayoutPlayerId(layoutId, playerListBaseAdaptor.getPlayerList().get(position).playerId, getApplicationContext());
            }
            selectedTally++;
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    class FactionSpinnerChanged implements AdapterView.OnItemSelectedListener {
        int selectedTally = 0;

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

            if (selectedTally > 0) {
                changed = true;
                LayoutHelper.updateLayoutFaction(layoutId, factionListBaseAdaptor.getFactions()[position], getApplicationContext());
            }
            selectedTally++;
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    @Override
    public void showGallery(int selected) {
        ActivitySwitcher.launchLayoutImageGallery(selected, layoutId, this);
    }

    @Override
    public void breadCrumbSelected(int id, int position) {

    }

    @Override
    public void confirmFolderSelection(int folderId, String cmd) {
        selectedFolder = folderId;
        LayoutHelper.updateLayoutFolder(layoutId, folderId, this);
        layoutRecord.setLayoutFolderId(folderId);
        layoutFolderItems = LayoutFolderHelper.getFolderHeirarchy(folderId, this);
        recyclerAdaptor_layoutFolderBreadCrumb = new RecyclerAdaptor_LayoutFolderBreadCrumb(layoutFolderItems, this);
        layout_detail_folder_recycler.setAdapter(recyclerAdaptor_layoutFolderBreadCrumb);
        recyclerAdaptor_layoutFolderBreadCrumb.notifyDataSetChanged();
    }

    @Override
    public void saveTagSelection(ArrayList<LayoutTag> newSelectedTags) {
        super.saveTagSelection(newSelectedTags);

        layoutRecord.setLayoutTags(newSelectedTags);
        if (LayoutHelper.updateTags(layoutId, selectedTags, this).success) {
            selectedTags = layoutRecord.getLayoutTags();
            recyclerAdaptor_layoutTagPillList = new RecyclerAdaptor_LayoutTagPillList(selectedTags, false, this);
            layout_detail_tag_recycler.setAdapter(recyclerAdaptor_layoutTagPillList);
            recyclerAdaptor_layoutTagPillList.notifyDataSetChanged();
        } else {
            selectedTags = oldselectedTags;
        }

    }


    @Override
    protected void handleListUpdate(String activity_command, boolean success) {
        if (activity_command.equalsIgnoreCase(DELETE_LAYOUT)) {
            if (success) {
                finish();
            }
        } else if (activity_command.equalsIgnoreCase(DELETE_VERSION)) {
            layoutVersionRecycler.setLayoutManager(versionsLinearLayoutManager);
            recyclerAdaptor_layoutVersion = new RecyclerAdaptor_LayoutVersion(LayoutHelper.getLayoutVersions(layoutId, this), this);
            layoutVersionRecycler.setAdapter(recyclerAdaptor_layoutVersion);
            recyclerAdaptor_layoutVersion.notifyDataSetChanged();
        }

    }


    @Override
    public void playerSelected(String player, boolean selected) {

        MethodResult methodResult;
        if (selected) {
            methodResult = PlayerFavouriteLayoutHelper.addTopLayout(layoutId, player, this);
            LayoutHelper.setFavourite(layoutId, LayoutHelper.YES, this);
            setLayoutFavouriteIcon();
        } else {
            methodResult = PlayerFavouriteLayoutHelper.removeTopLayout(layoutId, player, this);
            if (PlayerFavouriteLayoutHelper.countPlayerFavouritesForLayout(layoutId, this) <= 0) {
                LayoutHelper.setFavourite(layoutId, LayoutHelper.NO, this);
                setLayoutFavouriteIcon();
            }

        }
        if (!methodResult.success) {
            showLongToast(methodResult.getMessage());
        }


        recyclerAdaptor_playerSelected = new RecyclerAdaptor_PlayerSelected(PlayerFavouriteLayoutHelper.getPlayersforLayout(layoutId, this), this);
        layoutPlayerTopRecycler.setAdapter(recyclerAdaptor_playerSelected);
        recyclerAdaptor_playerSelected.notifyDataSetChanged();
    }

    @Override
    public void deleteImage(long selected) {
        selectedImageId = selected;
        Bundle bundle = new Bundle();
        bundle.putString(BundleKeys.DIALOG_TITLE.toString(), "Are you sure?");
        bundle.putString(BundleKeys.DIALOG_MESSAGE.toString(), "Are you sure you want to delete this image?");
        bundle.putString(BundleKeys.DIALOG_COMMAND.toString(), DELETE_IMAGE);
        yesNoFragment = YesNoFragment.getInstance(getSupportFragmentManager(), bundle);
    }

    @Override
    public void editImageLabel(long selected, String label) {
        selectedImageId = selected;
        callMessageTextViewFrag("Set Image Label", "", CMD_EDIT_LABEL, label);

    }

    @Override
    public void onYesNoDialogYesClicked(String activity_command, Bundle bundle) {
        super.onYesNoDialogYesClicked(activity_command, bundle);
        if (activity_command.equalsIgnoreCase(DELETE_IMAGE)) {
            String whereClause = DatabaseContracts.LayoutImages.COLUMN_ID + " = ?";
            String[] whereArg = {String.valueOf(selectedImageId)};
            DBSQLiteHelper.deleteDbRows(DatabaseContracts.LayoutImages.TABLE_NAME, whereClause, whereArg, this);
            ArrayList<LayoutDetail_ImageListItem> imagebytesArray = LayoutImage_ListProvider.getLayoutDetail_imageListItems(layoutId, this);
            recyclerAdaptor_image = new RecyclerAdaptor_Image(imagebytesArray, this);
            imageRecycler.setAdapter(recyclerAdaptor_image);
            recyclerAdaptor_image.notifyDataSetChanged();
        }
    }

    @Override
    public void playerServiceResult(String command, MethodResult methodResult) {
        finishDownloading();
        showAlertFrag("Update Layout Result", methodResult.getMessage());
    }

    @Override
    public void layoutSelected(int layoutId, int position) {

    }

    @Override
    public void layoutDeSelected(int layoutId, int position) {

    }
}
