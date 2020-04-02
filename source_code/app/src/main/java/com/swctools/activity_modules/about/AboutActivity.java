package com.swctools.activity_modules.about;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.swctools.R;
import com.swctools.activity_modules.about.models.FAQData;
import com.swctools.activity_modules.about.network.DownloadCallback;
import com.swctools.activity_modules.about.network.NetworkFragment;
import com.swctools.util.ActivitySwitcher;
import com.swctools.util.StringUtil;
import com.swctools.activity_modules.about.view_adaptors.ExpandableAdaptor_FAQ;

import java.io.StringReader;
import java.util.ArrayList;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AboutActivity extends AppCompatActivity implements DownloadCallback {
    private static final String TAG = "AboutActivity";
    private TextView buildNumber, versionName, gitLink, dlProgress, ytLink;
    private ImageView gitLinkIcon, ytLinkIcon;
    private ExpandableListView faqRecycler;
    private NetworkFragment mNetworkFragment;
    private boolean mDownloading = false;
    private ExpandableAdaptor_FAQ expandableAdaptor_faq;
    private ArrayList<FAQData> faqData;

    @Override
    protected void onStart() {
        super.onStart();
        startDownload();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar toolbar = findViewById(R.id.aboutToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("About");
        buildNumber = findViewById(R.id.buildNumber);
        versionName = findViewById(R.id.versionName);
        gitLink = findViewById(R.id.gitLink);
        gitLinkIcon = findViewById(R.id.gitLinkIcon);
        ytLinkIcon = findViewById(R.id.ytLinkIcon);
        faqRecycler = findViewById(R.id.faqRecycler);
        dlProgress = findViewById(R.id.dlProgress);
        ytLink = findViewById(R.id.ytLink);

        faqData = new ArrayList<>();
        try {
            PackageInfo pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName.setText(pinfo.versionName);
            buildNumber.setText(String.valueOf(pinfo.versionCode));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        gitLink.setOnClickListener(new OpenGitWiki());
        gitLinkIcon.setOnClickListener(new OpenGitWiki());

        ytLink.setOnClickListener(new OpenYT());
        ytLinkIcon.setOnClickListener(new OpenYT());
        mNetworkFragment = NetworkFragment.getInstance(getSupportFragmentManager(), "https://raw.githubusercontent.com/DaveCMo/Android-SWC-Tools/master/faq.json");


    }

    class OpenYT implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String gitLink = getResources().getString(R.string.youtubelink);
            if (StringUtil.isStringNotNull(gitLink)) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(gitLink));
                startActivity(browserIntent);
            }
        }
    }


    class OpenGitWiki implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String gitLink = getResources().getString(R.string.wiki_git_link);
            if (StringUtil.isStringNotNull(gitLink)) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(gitLink));
                startActivity(browserIntent);
            }
        }
    }

    private void startDownload() {
        if (!mDownloading && mNetworkFragment != null) {
            // Execute the async download.
            mNetworkFragment.startDownload();
            mDownloading = true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) // Press Back Icon
        {
            finish();
            ActivitySwitcher.launchMainActivity(this);
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateFromDownload(String result) {
        try {
            if (result != null) {
                faqData = new ArrayList<>();
                faqData.clear();
                JsonObject responseJsonObj = Json.createReader(new StringReader(result)).readObject();
                JsonArray contentArray = responseJsonObj.getJsonArray("content");
                for (int i = 0; i < contentArray.size(); i++) {
                    JsonObject entry = contentArray.getJsonObject(i);
                    faqData.add(new FAQData(entry.getString("question"), entry.getString("answer")));
                }
                expandableAdaptor_faq = new ExpandableAdaptor_FAQ(faqData, this);
                faqRecycler.setAdapter(expandableAdaptor_faq);
                expandableAdaptor_faq.notifyDataSetChanged();
            } else {
                dlProgress.setText("Failed :(");
            }
        } catch (Exception e) {
            e.printStackTrace();
            dlProgress.setText("");
        }
    }

    @Override
    public NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }

    @Override
    public void onProgressUpdate(int progressCode, int percentComplete) {
        switch (progressCode) {
            // You can add UI behavior for progress updates here.
            case Progress.ERROR:
                break;
            case Progress.CONNECT_SUCCESS:
                break;
            case Progress.GET_INPUT_STREAM_SUCCESS:
                break;
            case Progress.PROCESS_INPUT_STREAM_IN_PROGRESS:
                dlProgress.setText("Downloading FAQ..." + percentComplete + "%");
                break;
            case Progress.PROCESS_INPUT_STREAM_SUCCESS:
                break;
        }
    }

    @Override
    public void finishDownloading() {
        mDownloading = false;
        if (mNetworkFragment != null) {
            mNetworkFragment.cancelDownload();
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


}
