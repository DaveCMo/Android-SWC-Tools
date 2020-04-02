package com.swctools.activity_modules.gallery;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.swctools.R;
import com.swctools.common.popups.YesNoFragment;
import com.swctools.interfaces.YesNoAlertCallBack;
import com.swctools.activity_modules.layout_detail.models.LayoutDetail_ImageListItem;
import com.swctools.activity_modules.layout_detail.models.LayoutImage_ListProvider;

import java.util.ArrayList;

public class ImageGalleryActivity extends AppCompatActivity implements YesNoAlertCallBack, GalleryFragementInterface {

    private static final String TAG = "ImageGalleryActivity";
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private static final String DELETE_IMAGE = "DELETE_IMAGE";

    private ViewPager mViewPager;
    public static final String LAYOUT_ID = "LAYOUT_ID";
    public static final String POSITION_ID = "POSITION_ID";
    private ArrayList<LayoutDetail_ImageListItem> imagebytesArray;
    protected YesNoFragment yesNoFragment;
    public int layoutId;
    public int positionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_gallery);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        imagebytesArray = new ArrayList<>();
        layoutId = getIntent().getIntExtra(LAYOUT_ID, 0);
        positionId = getIntent().getIntExtra(POSITION_ID, 0);
        imagebytesArray = LayoutImage_ListProvider.getLayoutDetail_imageListItems(layoutId, this);
        mViewPager = findViewById(R.id.container);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(positionId, true);

        // Set up the ViewPager with the sections adapter.
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image_gallery, menu);
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


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            LayoutDetail_ImageListItem selectedImageModel = imagebytesArray.get(position);
            return GalleryFragment.newInstance(selectedImageModel.getFilePath(), selectedImageModel.getNo(), selectedImageModel.getLabel());
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return imagebytesArray.size();
        }
    }

    @Override
    public void onYesNoDialogYesClicked(String activity_command, Bundle bundle) {

    }

    @Override
    public void onYesNoDialogNoClicked(String activity_command) {

    }

    @Override
    public void deleteImage(long id) {

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
//            ActivitySwitcher.launchMainActivity(this);
        }
        return false;// super.onKeyDown(keyCode, event);
    }
}
