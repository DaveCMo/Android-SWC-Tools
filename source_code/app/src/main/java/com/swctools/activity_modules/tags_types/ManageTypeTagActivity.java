package com.swctools.activity_modules.tags_types;

import android.content.ContentValues;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.KeyEvent;
import android.widget.Toast;

import com.swctools.R;
import com.swctools.base.MessageTextViewInterface;
import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;
import com.swctools.common.enums.BundleKeys;
import com.swctools.common.sections_state_pager_adaptors.SectionsStatePagerAdapter;
import com.swctools.common.popups.MessageTextViewFragment;
import com.swctools.common.popups.YesNoFragment;
import com.swctools.interfaces.YesNoAlertCallBack;
import com.swctools.activity_modules.tags_types.fragments.FragmentManageTags;
import com.swctools.activity_modules.tags_types.fragments.FragmentManageTypes;
import com.swctools.activity_modules.tags_types.recycler_adaptors.RecyclerAdaptor_LayoutTypeTag;
import com.swctools.util.ActivitySwitcher;
import com.swctools.util.StringUtil;

public class ManageTypeTagActivity extends AppCompatActivity
        implements
        RecyclerAdaptor_LayoutTypeTag.LayoutTagTypeRowInterface,
        MessageTextViewInterface,
        YesNoAlertCallBack,
        InterfaceAddTypeTagFragment {
    private static final String TAG = "ManageTypeTagActivity";
    private FragmentManageTags mfragmentManageTags;
    private FragmentManageTypes mfragmentManageTypes;
    private YesNoFragment yesNoFragment;
    private SectionsStatePagerAdapter mSectionsStatePagerAdapter;
    private int tagId, typeId;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_typetag);
        Toolbar toolbar = (Toolbar) findViewById(R.id.layout_typetagToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Manage Layout Type/Tags");
        mSectionsStatePagerAdapter = new SectionsStatePagerAdapter(getSupportFragmentManager());
        mfragmentManageTags = new FragmentManageTags();
        mfragmentManageTypes = new FragmentManageTypes();
        mViewPager = (ViewPager) findViewById(R.id.layout_typetagViewPager);
        mSectionsStatePagerAdapter.addFragment(mfragmentManageTypes, "Types");
        mSectionsStatePagerAdapter.addFragment(mfragmentManageTags, "Tags");

        mViewPager.setAdapter(mSectionsStatePagerAdapter);

        TabLayout tabs = (TabLayout) findViewById(R.id.layout_typetagTabLayout);

        tabs.setupWithViewPager(mViewPager);
    }

    @Override
    public void editT(int tId, String typeTag, String currentValue) {
        if (typeTag.equalsIgnoreCase(RecyclerAdaptor_LayoutTypeTag.LayoutTagTypeRowInterface.TAG)) {
            tagId = tId;
            callMessageTextViewFrag("Edit Tag", "Set new tag name", Commands.EDIT_TAG.toString(), currentValue);
        } else if (typeTag.equalsIgnoreCase(RecyclerAdaptor_LayoutTypeTag.LayoutTagTypeRowInterface.TYPE)) {
            typeId = tId;
            callMessageTextViewFrag("Edit Type", "Set new type name", Commands.EDIT_TYPE.toString(), currentValue);

        }
    }

    @Override
    public void deleteT(int tId, String typeTag) {
        if (typeTag.equalsIgnoreCase(RecyclerAdaptor_LayoutTypeTag.LayoutTagTypeRowInterface.TAG)) {
            tagId = tId;

            Bundle bundle = new Bundle();
            bundle.putString(BundleKeys.DIALOG_TITLE.toString(), "Are you sure?");
            bundle.putString(BundleKeys.DIALOG_MESSAGE.toString(), "Are you sure you want to delete this Tag?");
            bundle.putString(BundleKeys.DIALOG_COMMAND.toString(), Commands.DELETE_TAG.toString());

            yesNoFragment = YesNoFragment.getInstance(getSupportFragmentManager(), bundle);
        } else if (typeTag.equalsIgnoreCase(RecyclerAdaptor_LayoutTypeTag.LayoutTagTypeRowInterface.TYPE)) {
            typeId = tId;

            Bundle bundle = new Bundle();
            bundle.putString(BundleKeys.DIALOG_TITLE.toString(), "Are you sure?");
            bundle.putString(BundleKeys.DIALOG_MESSAGE.toString(), "Are you sure you want to delete this Type?");
            bundle.putString(BundleKeys.DIALOG_COMMAND.toString(), Commands.DELETE_TYPE.toString());

            yesNoFragment = YesNoFragment.getInstance(getSupportFragmentManager(), bundle);


        }
    }

    private void callMessageTextViewFrag(String title, String message, String command, @Nullable String prevValue) {

        Bundle bundle = new Bundle();
        bundle.putString(BundleKeys.DIALOG_TITLE.toString(), title);
        bundle.putString(BundleKeys.DIALOG_MESSAGE.toString(), message);
        bundle.putString(BundleKeys.DIALOG_COMMAND.toString(), command);
        if (StringUtil.isStringNotNull(prevValue)) {
            bundle.putString(BundleKeys.DIALOG_VALUE.toString(), prevValue);
        }
        MessageTextViewFragment messageTextViewFragment = new MessageTextViewFragment();
        messageTextViewFragment.setArguments(bundle);
        messageTextViewFragment.show(getFragmentManager(), command);
    }

    @Override
    public void onTextViewDialogPositiveClick(String msg, String cmd) {
        ContentValues contentValues = new ContentValues();
        if (cmd.equalsIgnoreCase(Commands.EDIT_TAG.toString())) {
            contentValues.put(DatabaseContracts.LayoutTags.LAYOUT_TAG, msg);
            String whereClause = DatabaseContracts.LayoutTags.COLUMN_ID + " = ?";
            String[] whereArgs = {String.valueOf(tagId)};
            DBSQLiteHelper.updateData(DatabaseContracts.LayoutTags.TABLE_NAME, contentValues, whereClause, whereArgs, this);
            mfragmentManageTags.buildTagListForRecycler();
        } else if (cmd.equalsIgnoreCase(Commands.EDIT_TYPE.toString())) {
            contentValues.put(DatabaseContracts.LayoutTypes.LAYOUT_TYPE, msg);
            String whereClause = DatabaseContracts.LayoutTypes.COLUMN_ID + " = ?";
            String[] whereArgs = {String.valueOf(typeId)};
            DBSQLiteHelper.updateData(DatabaseContracts.LayoutTypes.TABLE_NAME, contentValues, whereClause, whereArgs, this);
            mfragmentManageTypes.buildTagListForRecycler();
        } else if (cmd.equalsIgnoreCase(InterfaceAddTypeTagFragment.ADD_TAG)) {
            contentValues.put(DatabaseContracts.LayoutTags.LAYOUT_TAG, msg);
            try {
                DBSQLiteHelper.insertData(DatabaseContracts.LayoutTags.TABLE_NAME, contentValues, this);
                mfragmentManageTags.buildTagListForRecycler();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (cmd.equalsIgnoreCase(InterfaceAddTypeTagFragment.ADD_TYPE)) {
            contentValues.put(DatabaseContracts.LayoutTypes.LAYOUT_TYPE, msg);
            try {
                DBSQLiteHelper.insertData(DatabaseContracts.LayoutTypes.TABLE_NAME, contentValues, this);
                mfragmentManageTypes.buildTagListForRecycler();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        contentValues.clear();
    }

    @Override
    public void onYesNoDialogYesClicked(String activity_command, Bundle bundle) {

        if (activity_command.equalsIgnoreCase(Commands.DELETE_TAG.toString())) {
            String whereClauseTag = DatabaseContracts.LayoutTags.COLUMN_ID + " = ? ";
            String[] whereArgsTag = {String.valueOf(tagId)};
            DBSQLiteHelper.deleteDbRows(DatabaseContracts.LayoutTags.TABLE_NAME, whereClauseTag, whereArgsTag, this);
            Toast.makeText(this, "Tag deleted.", Toast.LENGTH_SHORT).show();
        } else if (activity_command.equalsIgnoreCase(Commands.DELETE_TYPE.toString())) {
            String whereClauseType = DatabaseContracts.LayoutTypes.COLUMN_ID + " = ? ";
            String[] whereArgsType = {String.valueOf(typeId)};
            DBSQLiteHelper.deleteDbRows(DatabaseContracts.LayoutTypes.TABLE_NAME, whereClauseType, whereArgsType, this);
            Toast.makeText(this, "Type deleted.", Toast.LENGTH_SHORT).show();
        }


        mfragmentManageTypes.buildTagListForRecycler();
        mfragmentManageTags.buildTagListForRecycler();
    }

    @Override
    public void onYesNoDialogNoClicked(String activity_command) {

    }

    private void showTextEntryFragment(String title, String message, String command) {
        Bundle bundle = new Bundle();
        bundle.putString(BundleKeys.DIALOG_TITLE.toString(), title);
        bundle.putString(BundleKeys.DIALOG_MESSAGE.toString(), message);
        bundle.putString(BundleKeys.DIALOG_COMMAND.toString(), command);
        MessageTextViewFragment messageTextViewFragment = new MessageTextViewFragment();
        messageTextViewFragment.setArguments(bundle);
        messageTextViewFragment.show(getFragmentManager(), command);
    }

    @Override
    public void addNew(String type_tag) {
        switch (type_tag) {
            case InterfaceAddTypeTagFragment.ADD_TAG:
                showTextEntryFragment(InterfaceAddTypeTagFragment.ADD_TAG_TITLE, InterfaceAddTypeTagFragment.ADD_TAG_MSG, type_tag);
                break;
            case InterfaceAddTypeTagFragment.ADD_TYPE:
                showTextEntryFragment(InterfaceAddTypeTagFragment.ADD_TYPE_TITLE, InterfaceAddTypeTagFragment.ADD_TYPE_MSG, type_tag);
                break;
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
    enum Commands {
        EDIT_TAG, EDIT_TYPE, DELETE_TAG, DELETE_TYPE;

    }

}
