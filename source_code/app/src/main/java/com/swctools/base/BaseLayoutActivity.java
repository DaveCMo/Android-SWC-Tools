package com.swctools.base;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.swctools.config.AppConfig;
import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;
import com.swctools.common.enums.BundleKeys;
import com.swctools.common.popups.AlertFragment;
import com.swctools.layouts.fragments.LayoutFoldersFragment;
import com.swctools.layouts.fragments.LayoutTagFragment;
import com.swctools.common.popups.YesNoFragment;
import com.swctools.layouts.LayoutFolderHelper;
import com.swctools.layouts.LayoutHelper;
import com.swctools.common.helpers.PlayerFavouriteLayoutHelper;
import com.swctools.interfaces.FavouriteLayoutRowItemInterface;
import com.swctools.interfaces.FolderBreadCrumbInterface;
import com.swctools.interfaces.LayoutFolderInterface;
import com.swctools.interfaces.LayoutFoldersFragmentInterface;
import com.swctools.activity_modules.layout_manager.LayoutListInterface;
import com.swctools.interfaces.LayoutTagFragmentInterface;
import com.swctools.interfaces.LayoutTagListInterface;
import com.swctools.interfaces.LayoutTagListPillInterface;
import com.swctools.interfaces.LayoutVersionViewAdaptorInterface;
import com.swctools.interfaces.YesNoAlertCallBack;
import com.swctools.layouts.models.LayoutFolderItem;
import com.swctools.layouts.models.LayoutRecord;
import com.swctools.layouts.models.LayoutTag;
import com.swctools.util.ActivitySwitcher;
import com.swctools.util.MethodResult;
import com.swctools.util.SaveJsonFile;
import com.swctools.common.view_adaptors.delegated_adaptors.AdaptorDelegate_LayoutDetailTags;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import androidx.annotation.CallSuper;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

public abstract class BaseLayoutActivity extends BaseActivity implements
        LayoutTagListInterface,
        LayoutTagListPillInterface,
        LayoutTagFragmentInterface,
        AdaptorDelegate_LayoutDetailTags.LayoutTagDelegateInterface,
        LayoutFoldersFragmentInterface,
        FolderBreadCrumbInterface,
        MessageTextViewInterface, LayoutFolderInterface,
        YesNoAlertCallBack,
        LayoutVersionViewAdaptorInterface,
        FavouriteLayoutRowItemInterface,
        LayoutListInterface {
    //CONSTANTS & COMMANDS
    private static final String TAG = "BaseLayoutActivity";
    protected static final String MOVELAYOUTFOLDER = "MOVELAYOUTFOLDER";
    protected static final String MOVELAYOUTFOLDERINBULK = "MOVELAYOUTFOLDERINBULK";
    protected static final String DELETE_LAYOUT = "DELETE_LAYOUT";
    protected static final String DELETE_VERSION = "DELETE_VERSION";
    protected static final String MARK_AS_FAVOURITE = "MARK_AS_FAVOURITE";
    protected static final String REMOVE_FAVOURITE = "REMOVE_FAVOURITE";
    protected static final String REMOVE_MOSTTOPLAYOUTLOG = "REMOVE_MOSTTOPLAYOUTLOG";
    protected static final String REMOVE_TOP = "REMOVE_TOP";
    protected static final String EXPORT_LAYOUT = "EXPORT_LAYOUT";
    protected static final String ADD_FOLDER = "ADD_FOLDER";
    protected static final String MOVE_FOLDER = "MOVE_FOLDER";
    protected static final String RENAME_FOLDER = "RENAME_FOLDER";
    protected static final String DELETE_FOLDER = "DELETE_FOLDER";

    //VARIABLES
    protected int layoutId;
    protected int layoutVersionID;
    protected int folderId;
    protected int folderToMove;
    protected int parentFldrId;
    protected ArrayList<LayoutTag> selectedTags;
    protected ArrayList<LayoutTag> oldselectedTags;
    protected int selectedFolder;
    protected int prevselectedFolder;
    protected ArrayList<LayoutFolderItem> breadcrumbList;
    protected ArrayList<LayoutFolderItem> tmp_breadcrumbList;

    //FRAGMENTS
    protected LayoutTagFragment layoutTagFragment;
    protected LayoutFoldersFragment layoutFoldersFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Create the arrays
        breadcrumbList = new ArrayList<>();
        tmp_breadcrumbList = new ArrayList<>();
        selectedTags = new ArrayList<>();
        oldselectedTags = new ArrayList<>();
    }


    protected void openSetTagFragment() {

        Bundle bundle = new Bundle();
        oldselectedTags.addAll(selectedTags);
        bundle.putParcelableArrayList(LayoutTagFragment.CURRENTSELECTEDFRAGS, selectedTags);
        layoutTagFragment = LayoutTagFragment.getInstance(getSupportFragmentManager());
        layoutTagFragment.setArguments(bundle);

    }

    protected void openSetFolderFragment(int f) {
        selectedFolder = f;
        Bundle bundle = new Bundle();
        bundle.putString(BundleKeys.DIALOG_TITLE.toString(), "Move Layout Folder");
        bundle.putString(BundleKeys.DIALOG_MESSAGE.toString(), "Select the new folder for your layout:");
        bundle.putString(BundleKeys.DIALOG_COMMAND.toString(), MOVELAYOUTFOLDER);
        bundle.putInt(BundleKeys.LAYOUT_FOLDER_ID.toString(), selectedFolder);

        bundle.putInt(BundleKeys.LAYOUT_FOLDER_TO_MOVE.toString(), selectedFolder);
        LayoutFoldersFragment layoutFoldersFragment = LayoutFoldersFragment.getInstance(getSupportFragmentManager());
        layoutFoldersFragment.setArguments(bundle);
    }

    @Override
    public void cancelFolderSelection() {
        selectedFolder = prevselectedFolder;
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
    @CallSuper
    public void saveTagSelection(ArrayList<LayoutTag> newSelectedTags) {
        selectedTags = newSelectedTags;
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
    public void editTags() {

    }

    @Override
    public void deleteSelectedLayoutVersion(int rowId, int layoutId, int versionId) {

        this.layoutVersionID = rowId;
        this.layoutId = layoutId;
        Bundle bundle = new Bundle();
        int versionCount = LayoutHelper.getCountVersions(layoutId, this);
        if (versionCount > 1) {
            bundle.putString(BundleKeys.DIALOG_TITLE.toString(), "Are you sure?");
            bundle.putString(BundleKeys.DIALOG_MESSAGE.toString(), "Are you sure you want to delete this layout version?");
            bundle.putString(BundleKeys.DIALOG_COMMAND.toString(), DELETE_VERSION);
        } else {
            bundle.putString(BundleKeys.DIALOG_TITLE.toString(), "Are you sure?");
            bundle.putString(BundleKeys.DIALOG_MESSAGE.toString(), "This is the only version left for this layout :( \nTHIS WILL DELETE THE LAYOUT RECORD! Are you sure?");
            bundle.putString(BundleKeys.DIALOG_COMMAND.toString(), DELETE_LAYOUT);
        }

        yesNoFragment = YesNoFragment.getInstance(getSupportFragmentManager(), bundle);

    }


    @Override
    public void exportSelectedLayout(int layoutId, int versId) {
        AppConfig appConfig = new AppConfig(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        } else {
            String proposedLayout = LayoutHelper.getLayoutJson(layoutId, versId, this);
            String layoutName = LayoutHelper.getLayoutName(layoutId, this) + "_" + versId;
            MethodResult exportLayout = SaveJsonFile.saveJsonFile(proposedLayout, layoutName, appConfig.layoutManagerExportFolder(), ".json");

            if (exportLayout.success) {
                showLongToast("Layout " + layoutName + " exported to " + appConfig.layoutManagerExportFolder() + "!");
            } else {
                AlertFragment alertFragment = new AlertFragment();
                Bundle bundle = new Bundle();
                bundle.putString(BundleKeys.DIALOG_TITLE.toString(), "Error!");
                bundle.putString(BundleKeys.DIALOG_MESSAGE.toString(), exportLayout.getMessage());
                alertFragment.setArguments(bundle);
                alertFragment.show(getFragmentManager(), "EXPORTERROR");
            }

        }

    }

    @Override
    public void shareSelectedLayout(int layoutId, int versId) {
        String proposedLayout = LayoutHelper.getLayoutJson(layoutId, versId, this);

        String layoutName = LayoutHelper.getLayoutName(layoutId, this) + "_" + versId + "_Shared";

        FileOutputStream fos = null;
        String FILE_NAME = layoutName + ".json";
        try {
            fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            fos.write(proposedLayout.getBytes());
            File f = new File(getFilesDir() + "/" + FILE_NAME);
            try {
                Uri path = FileProvider.getUriForFile(this, "com.swctools", f);
                MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                String type = MimeTypeMap.getFileExtensionFromUrl(f.getName());
                String fType = mimeTypeMap.getMimeTypeFromExtension(type);
                if (fType == null) {
                    fType = "*/*";
                }
                Intent shareIntent = new Intent();
                shareIntent.setType(fType);
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, path);
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                shareIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                shareIntent.addFlags(Intent.FLAG_GRANT_PREFIX_URI_PERMISSION);
                shareIntent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);

                startActivity(Intent.createChooser(shareIntent, "Share..."));
            } catch (Exception e) {
                e.printStackTrace();
                showLongToast(e.getMessage());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    @Override
    public void editLayoutJson(int layoutId, int layoutVersionID) {
        ActivitySwitcher.launchEditLayoutJson(layoutId, layoutVersionID, this);
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
        }
    }

    @Override
    public void breadCrumbSelected(int id, int position) {

    }


    @Override
    public void onYesNoDialogYesClicked(String activity_command, Bundle bundle) {
        super.onYesNoDialogYesClicked(activity_command, bundle);
        if (activity_command.equalsIgnoreCase(DELETE_LAYOUT)) {
            MethodResult methodResult = LayoutHelper.deleteLayoutRecord(layoutId, this);
            if (methodResult.success) {
                handleListUpdate(activity_command, methodResult.success);
            }
            showLongToast(methodResult.getMessage());
        } else if (activity_command.equalsIgnoreCase(DELETE_VERSION)) {
            MethodResult methodResponse = LayoutHelper.deleteLayoutVersion(layoutVersionID, layoutId, this);
            showLongToast(methodResponse.getMessage());
            handleListUpdate(activity_command, methodResponse.success);
        } else if (activity_command.equalsIgnoreCase(REMOVE_MOSTTOPLAYOUTLOG)) {
            if (LayoutHelper.deleteLayoutLog(layoutId, layoutVersionID, this).success) {
                handleListUpdate(activity_command, true);
                Toast.makeText(this, "Deleted!", Toast.LENGTH_SHORT).show();
            }
        } else if (activity_command.equalsIgnoreCase(REMOVE_FAVOURITE)) {
            if (LayoutHelper.setFavourite(layoutId, LayoutHelper.NO, this).success) {
                handleListUpdate(activity_command, true);
                Toast.makeText(this, "Done!", Toast.LENGTH_SHORT).show();
            }
        } else if (activity_command.equalsIgnoreCase(REMOVE_TOP)) {
            if (PlayerFavouriteLayoutHelper.removeTopLayout(layoutId, playerId, this).success) {
                handleListUpdate(activity_command, true);
                Toast.makeText(this, "Deleted!", Toast.LENGTH_SHORT).show();
            }
        } else if (activity_command.equalsIgnoreCase(DELETE_VERSION)) {
            MethodResult methodResponse = LayoutHelper.deleteLayoutVersion(layoutVersionID, this.layoutId, this);
            Toast.makeText(this, methodResponse.getMessage(), Toast.LENGTH_LONG).show();
            handleListUpdate(activity_command, methodResponse.success);

        } else if (activity_command.equalsIgnoreCase(DELETE_FOLDER)) {
            if (LayoutFolderHelper.deleteLayoutFolder(selectedFolder, this).success) {
                handleListUpdate(activity_command, true);
                Toast.makeText(this, "Folder deleted!", Toast.LENGTH_LONG).show();
            }
        }

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
    public void deleteTopLayout(String player, int layoutId) {
        this.layoutId = layoutId;
        this.playerId = player;
        Bundle bundle = new Bundle();
        bundle.putString(BundleKeys.DIALOG_TITLE.toString(), "Are you sure?");
        bundle.putString(BundleKeys.DIALOG_MESSAGE.toString(), "Are you sure you want to delete this?");
        bundle.putString(BundleKeys.DIALOG_COMMAND.toString(), REMOVE_TOP);
        yesNoFragment = YesNoFragment.getInstance(getSupportFragmentManager(), bundle);


    }


    @Override
    public void loadLayout(LayoutRecord layoutRecord) {
        ActivitySwitcher.launchLayoutDetails(layoutRecord, this);

    }

    @Override
    public void deleteSelectedLayout(int dbId) {
        this.layoutId = dbId;

        Bundle bundle = new Bundle();
        bundle.putString(BundleKeys.DIALOG_TITLE.toString(), "Are you sure?");
        bundle.putString(BundleKeys.DIALOG_MESSAGE.toString(), "Are you sure you want to delete this layout?");
        bundle.putString(BundleKeys.DIALOG_COMMAND.toString(), DELETE_LAYOUT);

//        yesNoFragment.setArguments(bundle);
        yesNoFragment = YesNoFragment.getInstance(getSupportFragmentManager(), bundle);

    }

    @Override
    public void renameFolder(int folderId) {
        selectedFolder = folderId;
        callMessageTextViewFrag("Rename Folder", "New name:", RENAME_FOLDER, null);
    }

    @Override
    public void markFavourite(int layoutId, int position, String favouriteVal) {
        MethodResult setfav = LayoutHelper.setFavourite(layoutId, favouriteVal, this);
        handleListUpdate(MARK_AS_FAVOURITE, setfav.success);
    }

    @Override
    public void deleteFolder(int folderId) {

        Bundle bundle = new Bundle();
        this.selectedFolder = folderId;
        bundle.putString(BundleKeys.DIALOG_TITLE.toString(), "Are you sure?");
        bundle.putString(BundleKeys.DIALOG_MESSAGE.toString(), "Are you sure you want to delete this folder? \n Layouts will be moved to the main folder.");
        bundle.putString(BundleKeys.DIALOG_COMMAND.toString(), DELETE_FOLDER);

        yesNoFragment = YesNoFragment.getInstance(getSupportFragmentManager(), bundle);
    }

    @Override
    public void moveFolder(int folderId) {
        folderToMove = folderId;
        selectedFolder = LayoutFolderHelper.getParentFolderId(folderToMove, this);
        Bundle bundle = new Bundle();

        bundle.putString(BundleKeys.DIALOG_TITLE.toString(), "Move folder");
        bundle.putString(BundleKeys.DIALOG_MESSAGE.toString(), "Give your new folder a name:");
        bundle.putString(BundleKeys.DIALOG_COMMAND.toString(), MOVE_FOLDER);
        bundle.putString(BundleKeys.LAYOUT_FOLDER_ID.toString(), MOVE_FOLDER);
        bundle.putInt(BundleKeys.LAYOUT_FOLDER_TO_MOVE.toString(), folderToMove);

        layoutFoldersFragment = LayoutFoldersFragment.getInstance(getSupportFragmentManager());
        layoutFoldersFragment.setArguments(bundle);
    }

    @Override
    public void folderSelected(int folderId) {

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
    public void onTextViewDialogPositiveClick(String msg, String cmd) {

        if (cmd.equalsIgnoreCase(ADD_FOLDER)) {
            LayoutFolderHelper.addNewFolder(msg, parentFldrId, this);
            handleListUpdate(cmd, true);
        } else if (cmd.equalsIgnoreCase(RENAME_FOLDER)) {
            LayoutFolderHelper.renameLayout(selectedFolder, msg, this);
            handleListUpdate(cmd, true);
        }

    }
}
