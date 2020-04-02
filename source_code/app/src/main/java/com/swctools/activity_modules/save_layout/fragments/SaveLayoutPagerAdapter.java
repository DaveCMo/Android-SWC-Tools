package com.swctools.activity_modules.save_layout.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import android.view.ViewGroup;

import com.swctools.common.enums.BundleKeys;
import com.swctools.layouts.models.LayoutTag;

import java.util.ArrayList;

public class SaveLayoutPagerAdapter extends FragmentStatePagerAdapter {
    private static final String TAG = "SaveLayoutPagerAdapter";
    private long baseId = 0;
    private FragmentSaveLayout fragmentSaveLayout;
    private FragmentUpdateLayout fragmentUpdateLayout;

    private int selectedFolder;
    private ArrayList<LayoutTag> layoutTagArrayList;
    private String playerId;
    private String lFaction;

    public SaveLayoutPagerAdapter(FragmentManager fm, String pId, String lFaction, int selectedFolder, ArrayList<LayoutTag> layoutTags) {
        super(fm);
        this.playerId = pId;
        this.lFaction = lFaction;
        this.selectedFolder = selectedFolder;


        this.layoutTagArrayList = layoutTags;
    }


    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }


    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:

                Bundle args = new Bundle();
                args.putString(BundleKeys.PLAYER_ID.toString(), playerId);
                args.putString(BundleKeys.PLAYER_FACTION.toString(), lFaction);
                args.putInt(BundleKeys.LAYOUT_FOLDER_ID.toString(), selectedFolder);
                args.putParcelableArrayList(BundleKeys.LAYOUT_TAG.toString(), layoutTagArrayList);
                fragmentSaveLayout = FragmentSaveLayout.newInstance();
                fragmentSaveLayout.setArguments(args);
                return fragmentSaveLayout;
            case 1:
                fragmentUpdateLayout = FragmentUpdateLayout.newInstance();
                return fragmentUpdateLayout;
            default:
                return null;
        }
    }

    public void setLayoutImage(Uri selectedImage, boolean b) {
//        fragmentSaveLayout.setLayoutImage(selectedImage, b);

    }

    public void setSaveClicked(boolean b) {
        fragmentSaveLayout.setSaveClicked(b);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment createdFragment = (Fragment) super.instantiateItem(container, position);

        switch (position) {
            case 0:
                fragmentSaveLayout = (FragmentSaveLayout) createdFragment;
                Bundle args = new Bundle();
                args.putString(BundleKeys.PLAYER_ID.toString(), playerId);
                args.putString(BundleKeys.PLAYER_FACTION.toString(), lFaction);
                args.putInt(BundleKeys.LAYOUT_FOLDER_ID.toString(), selectedFolder);
                args.putParcelableArrayList(BundleKeys.LAYOUT_TAG.toString(), layoutTagArrayList);
                fragmentSaveLayout.setArguments(args);
                break;
            case 1:
                fragmentUpdateLayout = (FragmentUpdateLayout) createdFragment;
                break;
        }
        return createdFragment;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Add New Layout";
            case 1:
                return "Update Existing";
        }
        return "";//mFragmentTitleList.get(position);
    }

    public void setFolderList(int selectedFolder) {
        fragmentSaveLayout.setFolderRecycler(selectedFolder);
    }

    public void setTagList(ArrayList<LayoutTag> layoutTags) {
        fragmentSaveLayout.setTagRecycler(layoutTags);
    }

    public void setLayoutSelected(int layoutID) {

        fragmentUpdateLayout.setLayoutSelected(layoutID);
    }


    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
        super.restoreState(state, loader);
//        state
    }

    @Override
    public Parcelable saveState() {
        return super.saveState();
    }
}
