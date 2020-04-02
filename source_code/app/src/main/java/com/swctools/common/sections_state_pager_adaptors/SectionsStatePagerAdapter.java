package com.swctools.common.sections_state_pager_adaptors;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class SectionsStatePagerAdapter extends FragmentStatePagerAdapter {
    private final List<Fragment> mFragementList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    private long baseId = 0;
    public SectionsStatePagerAdapter(FragmentManager fm) {
        super(fm);

    }

    public void addFragment(Fragment fragment, String title){
        mFragementList.add(fragment);
        mFragmentTitleList.add(title);
    }
    @Override
    public Fragment getItem(int position) {
        return mFragementList.get(position);
    }

    @Override
    public int getCount() {
        return mFragementList.size();
    }

    @Override
    public int getItemPosition(Object object){
        return PagerAdapter.POSITION_NONE;
    }

    public void deleteAt(int position){
        mFragmentTitleList.remove(position);
        mFragementList.remove(position);
        notifyDataSetChanged();
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
//    public void clearProgressUpTo(int progress) {
//        boolean deleted = false;
//
//        for (int i = mFragementList.size() - 1; i > 0; i--) {
//            int key = mFragementList.g .keyAt(i);
//            if (key != progress) {
//                mFragementList.delete(key);
//                deleted = true;
//            } else {
//                break;
//            }
//        }
//
//        if (deleted)
//            notifyDataSetChanged(); //prevents recursive call (and IllegalStateException: Recursive entry to executePendingTransactions)
//
//        while (mSavedState.size() > progress) {
//            mSavedState.remove(mSavedState.size()-1);
//        }
//
//    }
}
