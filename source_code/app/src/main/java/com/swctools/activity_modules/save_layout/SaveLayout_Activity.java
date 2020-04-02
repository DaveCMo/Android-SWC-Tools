package com.swctools.activity_modules.save_layout;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.swctools.R;
import com.swctools.base.MessageTextViewInterface;
import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;
import com.swctools.common.enums.BundleKeys;
import com.swctools.activity_modules.save_layout.fragments.SaveLayoutPagerAdapter;
import com.swctools.common.popups.AlertFragment;
import com.swctools.layouts.fragments.LayoutFoldersFragment;
import com.swctools.layouts.fragments.LayoutTagFragment;
import com.swctools.common.popups.YesNoFragment;
import com.swctools.layouts.LayoutFolderHelper;
import com.swctools.layouts.LayoutHelper;
import com.swctools.interfaces.FolderBreadCrumbInterface;
import com.swctools.activity_modules.multi_image_picker.ImageListInterface;
import com.swctools.interfaces.LayoutFolderInterface;
import com.swctools.interfaces.LayoutFoldersFragmentInterface;
import com.swctools.interfaces.LayoutTagFragmentInterface;
import com.swctools.interfaces.LayoutTagListInterface;
import com.swctools.interfaces.LayoutTagListPillInterface;
import com.swctools.interfaces.YesNoAlertCallBack;
import com.swctools.layouts.models.LayoutFolderItem;
import com.swctools.layouts.models.LayoutTag;
import com.swctools.activity_modules.save_layout.fragments.FragmentSaveLayout;
import com.swctools.activity_modules.save_layout.fragments.FragmentUpdateLayout;
import com.swctools.util.ImportUtils;
import com.swctools.util.MethodResult;
import com.swctools.util.StringUtil;
import com.swctools.activity_modules.save_layout.recycler_adaptors.RecyclerAdaptor_UpdateLayout;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;


public class SaveLayout_Activity extends AppCompatActivity
        implements
        MessageTextViewInterface,
        FragmentSaveLayout.FragmentSaveLayoutInterface,
        RecyclerAdaptor_UpdateLayout.UpdateLayoutAdaptorInterface,
        FragmentUpdateLayout.FragmentUpdateLayoutInterface,
        YesNoAlertCallBack,
        LayoutTagFragmentInterface,
        LayoutTagListInterface,
        LayoutTagListPillInterface,
        LayoutFoldersFragmentInterface, LayoutFolderInterface,
        FolderBreadCrumbInterface,
        ImageListInterface {
    private static final String TAG = "SaveLayout_Activity";
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final String DIALOG_TAG = "DIALOG_TAG";
    private static final String ADD_LAYOUT_TYPE = "ADD_LAYOUT_TYPE";
    private static final String ADD_LAYOUT_TAG = "ADD_LAYOUT_TAG";
    private static final String MOVELAYOUTFOLDER = "MOVELAYOUTFOLDER";

    private YesNoFragment yesNoFragment;
    private String layoutJSON;
    private String playerId;
    private String lFaction;
    private String NONE = "None";
    private String wildCard = "%";
    private String m_Text = "";
    private boolean saveClicked;
    private String prev_screen;
    private int layoutId;


    //UI Components
    private ViewPager mViewPager;//
    private TabLayout tabs;
    private Toolbar toolbar;

    //Fragments
    private SaveLayoutPagerAdapter mSectionsStatePagerAdapter;
    private LayoutTagFragment layoutTagFragment;
    private int layoutVersion;

    private ArrayList<LayoutTag> selectedTags;
    private ArrayList<LayoutTag> oldselectedTags;
    private int selectedFolder;
    private int prevselectedFolder;
    private ArrayList<LayoutFolderItem> breadcrumbList;
    private ArrayList<LayoutFolderItem> tmp_breadcrumbList;


    //Saved data keys:
    private static final String LAYOUTJSON = "LAYOUTJSON";
    private static final String SELECTEDFOLDER = "SELECTEDFOLDER";
    private static final String SELECTEDTAGS = "SELECTEDTAGS";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_layout_);

        //UI ELEMENTS
        tabs = findViewById(R.id.tabs);
        toolbar = findViewById(R.id.saveLayoutToolbar);
        mViewPager = findViewById(R.id.saveLayoutViewPager);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Save Layout");

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSharedLayout(intent);
            }
        } else {
            if (savedInstanceState != null) {
                selectedFolder = savedInstanceState.getInt(SELECTEDFOLDER);
                layoutJSON = savedInstanceState.getString(BundleKeys.LAYOUT_JSON_STRING.toString());
                selectedTags = savedInstanceState.getParcelableArrayList(SELECTEDTAGS);
                playerId = savedInstanceState.getString(BundleKeys.PLAYER_ID.toString());
                lFaction = savedInstanceState.getString(BundleKeys.PLAYER_FACTION.toString());
                prev_screen = savedInstanceState.getString(BundleKeys.PREV_SCREEN.toString());


                prevselectedFolder = selectedFolder;
            } else {

                layoutJSON = getIntent().getStringExtra(BundleKeys.LAYOUT_JSON_STRING.toString());
                prev_screen = getIntent().getStringExtra(BundleKeys.PREV_SCREEN.toString());
                lFaction = getIntent().getStringExtra(BundleKeys.PLAYER_FACTION.toString());
                playerId = getIntent().getStringExtra(BundleKeys.PLAYER_ID.toString());
                selectedFolder = 0;
                prevselectedFolder = 0;
                selectedTags = new ArrayList<>();
                DBSQLiteHelper.deleteDbRows(DatabaseContracts.LayoutImagesTmp.TABLE_NAME, null, null, this);
                DBSQLiteHelper.deleteDbRows(DatabaseContracts.TmpImageSelection.TABLE_NAME, null, null, this);
            }
        }

        oldselectedTags = new ArrayList<>();


        breadcrumbList = new ArrayList<>();
        tmp_breadcrumbList = new ArrayList<>();
        tmp_breadcrumbList.addAll(LayoutFolderHelper.getFolderHeirarchy(selectedFolder, this));
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mSectionsStatePagerAdapter = new SaveLayoutPagerAdapter(getSupportFragmentManager(), playerId, lFaction, selectedFolder, selectedTags);
        mViewPager.setAdapter(mSectionsStatePagerAdapter);
        tabs.setupWithViewPager(mViewPager);
    }

    private void handleSharedLayout(Intent resultData) {
        MethodResult methodResponse = ImportUtils.processImportLayout(resultData, this);
        if (methodResponse.success) {
            layoutJSON = methodResponse.getMessage();
        } else {
            Toast.makeText(this, methodResponse.getMessage(), Toast.LENGTH_LONG);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SELECTEDFOLDER, selectedFolder);
        outState.putString(BundleKeys.LAYOUT_JSON_STRING.toString(), layoutJSON);
        outState.putParcelableArrayList(SELECTEDTAGS, selectedTags);
        outState.putString(BundleKeys.PLAYER_ID.toString(), playerId);
        outState.putString(BundleKeys.PLAYER_FACTION.toString(), lFaction);
        outState.putString(BundleKeys.PREV_SCREEN.toString(), prev_screen);
    }


    @Override
    public void triggerMessageFragment(String message, String title, String fragmentTag) {
        Bundle bundle = new Bundle();
        bundle.putString(BundleKeys.DIALOG_MESSAGE.toString(), message);
        bundle.putString(BundleKeys.DIALOG_TITLE.toString(), title);
        AlertFragment alertFragment = new AlertFragment();
        alertFragment.setArguments(bundle);
        alertFragment.show(getFragmentManager(), fragmentTag);
        mSectionsStatePagerAdapter.setSaveClicked(false);
    }


    @Override
    public void saveLayout(String lName, String lPlayer, String lFaction, byte[] bytes) {


        MethodResult saveLayoutResponse = LayoutHelper.saveNewLayout(lName, lPlayer, layoutJSON, lFaction, selectedTags, selectedFolder, bytes, this);
        showToast(saveLayoutResponse.getMessage());
        if (saveLayoutResponse.success) {
            finish();
        } else {
            mSectionsStatePagerAdapter.setSaveClicked(false);
        }


    }


    @Override
    public void setPlayer(String PLAYER_ID) {

    }

    @Override
    public void setFaction(String FACTION) {

    }


    public void showToast(String msg) {
        Toast myToast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        myToast.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.help, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.playerHelp) {
            Bundle bundle = new Bundle();
            bundle.putString(BundleKeys.DIALOG_MESSAGE.toString(), getResources().getString(R.string.save_layout_intro));
            bundle.putString(BundleKeys.DIALOG_TITLE.toString(), "Save Layout Help");
            AlertFragment alertFragment = new AlertFragment();
            alertFragment.setArguments(bundle);
            alertFragment.show(getFragmentManager(), "HELP");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Spinner functions:


    @Override
    public void onTextViewDialogPositiveClick(String msg, String cmd) {
//        m_Text = edt.getText().toString();
        m_Text = msg;
        String toastMsg = "";
        if (StringUtil.isStringNotNull(m_Text)) {
            try {
                if (cmd.equalsIgnoreCase(ADD_LAYOUT_TAG)) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DatabaseContracts.LayoutTags.LAYOUT_TAG, m_Text);
                    toastMsg = "Added new layout tag '" + m_Text + "'";
                    long newTagId = DBSQLiteHelper.insertData(DatabaseContracts.LayoutTags.TABLE_NAME, contentValues, getApplicationContext());
                } else if (cmd.equalsIgnoreCase(ADD_LAYOUT_TYPE)) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DatabaseContracts.LayoutTypes.LAYOUT_TYPE, m_Text);
                    long newTypeId = DBSQLiteHelper.insertData(DatabaseContracts.LayoutTypes.TABLE_NAME, contentValues, getApplicationContext());
                    toastMsg = "Added new layout type '" + m_Text + "'";

                }
                showToast(toastMsg);
//                Toast.makeText(getApplicationContext(), toastMsg, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromInputMethod(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void selectLayout(int LAYOUT_ID) {
        mSectionsStatePagerAdapter.setLayoutSelected(LAYOUT_ID);
    }


    @Override
    public void newVersion(int layoutId) {
        MethodResult newVersionResult = LayoutHelper.saveNewLayoutVersion(layoutJSON, Long.parseLong(String.valueOf(layoutId)), this);
        if (newVersionResult.success) {
            showToast("New version saved!");
            finish();
        } else {
            showToast(newVersionResult.getMessage());
        }
    }

    @Override
    public void overWriteLayoutVersion(int layoutID, int layoutVersion) {
        this.layoutId = layoutID;
        this.layoutVersion = layoutVersion;

        Bundle bundle = new Bundle();
        bundle.putString(BundleKeys.DIALOG_TITLE.toString(), "Are you sure?");
        bundle.putString(BundleKeys.DIALOG_MESSAGE.toString(), "Are you sure you want to update this layout version?");
        bundle.putInt(BundleKeys.DIALOG_COMMAND.toString(), 1);

        yesNoFragment = YesNoFragment.getInstance(getSupportFragmentManager(), bundle);


    }

    @Override
    public void getLayoutImage() {
        Intent getImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(getImageIntent, RESULT_LOAD_IMAGE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
                Uri selectedImage = data.getData();

                mSectionsStatePagerAdapter.setLayoutImage(selectedImage, true);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onYesNoDialogYesClicked(String activity_command, Bundle bundle) {
        MethodResult methodResponse = LayoutHelper.updateLayoutVersion(layoutId, layoutVersion, layoutJSON, this);
        if (methodResponse.success) {
            showToast(methodResponse.getMessage());
            finish();
        } else {
            showToast(methodResponse.getMessage());
        }

    }

    @Override
    public void onYesNoDialogNoClicked(String activity_command) {

    }

    @Override
    public void setTags() {
        Bundle bundle = new Bundle();
        oldselectedTags.addAll(selectedTags);
        bundle.putParcelableArrayList(LayoutTagFragment.CURRENTSELECTEDFRAGS, selectedTags);
        layoutTagFragment = LayoutTagFragment.getInstance(getSupportFragmentManager());
        layoutTagFragment.setArguments(bundle);

    }

    @Override
    public void setFolder() {

        Bundle bundle = new Bundle();
        bundle.putString(BundleKeys.DIALOG_TITLE.toString(), "Save layout to folder:");
        bundle.putString(BundleKeys.DIALOG_MESSAGE.toString(), "Select the new folder for your layout:");
        bundle.putString(BundleKeys.DIALOG_COMMAND.toString(), MOVELAYOUTFOLDER);
        bundle.putInt(BundleKeys.LAYOUT_FOLDER_ID.toString(), selectedFolder);

        bundle.putInt(BundleKeys.LAYOUT_FOLDER_TO_MOVE.toString(), selectedFolder);
        LayoutFoldersFragment layoutFoldersFragment = LayoutFoldersFragment.getInstance(getSupportFragmentManager());
        layoutFoldersFragment.setArguments(bundle);
    }

    @Override
    public void saveTagSelection(ArrayList<LayoutTag> newSelectedTags) {
        selectedTags = newSelectedTags;
        mSectionsStatePagerAdapter.setTagList(selectedTags);
    }

    @Override
    public void addTag(String tagLabel) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContracts.LayoutTags.LAYOUT_TAG, tagLabel);
        long newTagId = DBSQLiteHelper.insertData(DatabaseContracts.LayoutTags.TABLE_NAME, contentValues, getApplicationContext());
        LayoutTag layoutTag = new LayoutTag(Integer.parseInt(String.valueOf(newTagId)), tagLabel);
        selectedTags.add(layoutTag);
        layoutTagFragment.getInstance(getSupportFragmentManager()).addNewTag(layoutTag);
    }

    @Override
    public void cancelTagSelection() {
        selectedTags.clear();
        selectedTags.addAll(oldselectedTags);
    }

    @Override
    public void selectTag(LayoutTag layoutTag, int position) {
        selectedTags.add(layoutTag);

        layoutTagFragment = LayoutTagFragment.getInstance(getSupportFragmentManager());
        layoutTagFragment.addTagToSelected(layoutTag, position);

    }

    @Override
    public void removeTag(LayoutTag tag, int position) {
        selectedTags.remove(position);
        layoutTagFragment = LayoutTagFragment.getInstance(getSupportFragmentManager());
        layoutTagFragment.removeSelectedTag(tag, position);

    }

    @Override
    public void cancelFolderSelection() {
        selectedFolder = prevselectedFolder;
    }

    @Override
    public void confirmFolderSelection(int folderId, String cmd) {
        selectedFolder = folderId;
        mSectionsStatePagerAdapter.setFolderList(selectedFolder);

    }

    @Override
    public void addFolderFromFragment(String newFolder, int parentFolderId) {
        LayoutFoldersFragment layoutFoldersFragment = LayoutFoldersFragment.getInstance(getSupportFragmentManager());
        LayoutFolderHelper.addNewFolder(newFolder, selectedFolder, this);
        layoutFoldersFragment.setRecycler(selectedFolder);
    }

    @Override
    public void upFolderInFragment() {
        selectedFolder = LayoutFolderHelper.getParentFolderId(selectedFolder, this);

        LayoutFoldersFragment layoutFoldersFragment = LayoutFoldersFragment.getInstance(getSupportFragmentManager());

        layoutFoldersFragment.setSelectedFolder(LayoutFolderHelper.getLayoutFolder(selectedFolder, this));
        layoutFoldersFragment.setRecycler(selectedFolder);
    }

    @Override
    public void folderSelectedFragment(int id) {
        selectedFolder = id;
        LayoutFoldersFragment layoutFoldersFragment = LayoutFoldersFragment.getInstance(getSupportFragmentManager());

        layoutFoldersFragment.setSelectedFolder(LayoutFolderHelper.getLayoutFolder(selectedFolder, this));
        layoutFoldersFragment.setRecycler(selectedFolder);
    }

    @Override
    public void breadCrumbSelected(int id, int position) {

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void showGallery(int selected) {

    }

    @Override
    public void deleteImage(long selected) {

    }

    @Override
    public void editImageLabel(long selected, String label) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();        }
        return false;// super.onKeyDown(keyCode, event);
    }
}