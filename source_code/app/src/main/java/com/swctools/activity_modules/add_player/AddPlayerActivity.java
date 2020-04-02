package com.swctools.activity_modules.add_player;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.swctools.base.BaseActivity;
import com.swctools.R;
import com.swctools.common.enums.BundleKeys;
import com.swctools.util.ActivitySwitcher;
import com.swctools.util.MethodResult;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;

public class AddPlayerActivity extends BaseActivity {
    private static final String TAG = "AddPlayer";
    private static final String PROGRESS = "PROGRESS";
    private static final String MAX = "MAX";
    private static final int READ_REQUEST_CODE = 42;

    boolean mBound = false;
    private String playerId;
    private String playerSecret;

    private boolean saveBtnClicked = false;

    private EditText playerId_editText;
    private EditText playerSecret_editText;
    private Button saveBtn;
    private CheckBox chkShowSecret;
    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putBoolean("saveBtnClicked", saveBtnClicked);
        outState.putBoolean(BundleKeys.DOWNLOADING.toString(), mDownloading);
        outState.putString(BundleKeys.PROGRESS_MESSAGE.toString(), progressMessage);
        outState.putInt(PROGRESS, progress);
        outState.putInt(MAX, max);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_player);
        Toolbar toolbar = (Toolbar) findViewById(R.id.addplayerToolbar);
        setSupportActionBar(toolbar);
        playerId_editText = findViewById(R.id.playerIdText);
        playerSecret_editText = findViewById(R.id.playerSecretText);
        progress_overlay_bar = findViewById(R.id.progress_overlay_bar);
        progress_overlay_container = findViewById(R.id.progress_overlay_container_include);
        progress_overlay_message = findViewById(R.id.progress_overlay_message);
        saveBtn = findViewById(R.id.saveButton);
        chkShowSecret = findViewById(R.id.chkShowSecret);
        if (savedInstanceState != null) {
            mDownloading = savedInstanceState.getBoolean(BundleKeys.DOWNLOADING.toString());
            progressMessage = savedInstanceState.getString(BundleKeys.PROGRESS_MESSAGE.toString());
            progress = savedInstanceState.getInt(PROGRESS);
            max = savedInstanceState.getInt(MAX);
            if (mDownloading) {
                playerId_editText.setEnabled(false);
                playerSecret_editText.setEnabled(false);


                progress_overlay_container.setVisibility(View.VISIBLE);
            } else {
                progress_overlay_container.setVisibility(View.GONE);
                playerId_editText.setEnabled(true);
                playerSecret_editText.setEnabled(true);
            }
        } else {
            progress_overlay_container.setVisibility(View.GONE);
            progressMessage = "";
            mDownloading = false;
            saveBtnClicked = false;
        }
        setTitle("Add Player");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveClick();
            }
        });
        chkShowSecret.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b){

                    playerSecret_editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }else {
                    playerSecret_editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                }
            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!mDownloading) {
            ActivitySwitcher.launchMainActivity(this);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

//
    }

    @Override
    protected void onStop() {

        super.onStop();


    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        saveBtnClicked = savedInstanceState.getBoolean("saveBtnClicked");
        mDownloading = savedInstanceState.getBoolean(BundleKeys.DOWNLOADING.toString());
    }


    public void onSaveClick() {
        if (!saveBtnClicked && !mDownloading) {
            saveBtnClicked = true;
            playerId = playerId_editText.getText().toString();
            playerSecret = playerSecret_editText.getText().toString();
            addPlayer(playerId, playerSecret);
            saveBtnClicked = false;
        } else {
            showToast("Already processing a request, stop clicking me!!!!");
        }
    }


    public void onClearClick(View view) {
        playerId_editText.setText("");
        playerSecret_editText.setText("");
        playerId_editText.setEnabled(true);
        playerSecret_editText.setEnabled(true);
        saveBtnClicked = false;
    }

    public void showToast(String msg) {
        Toast myToast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
        myToast.show();
    }

    public void performFileSearch(View view) {
        // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
        // browser.
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        // Filter to only show results that can be "opened", such as a
        // file (as opposed to a list of contacts or timezones)
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Filter to show only images, using the image MIME data type.
        // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
        // To search for all documents available via installed storage providers,
        // it would be "*/*".
        intent.setType("*/*");

        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {

        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                try {
                    getPlayerDetsFromSigma(uri);
                } catch (IOException e) {

                }

            } else {
                showToast("You didn't select anything!");
            }
        } else {
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                ActivitySwitcher.launchMainActivity(this);
                break;

        }

        return super.onOptionsItemSelected(item);
    }


    private void getPlayerDetsFromSigma(Uri uri) throws IOException {
        try {
            InputStream in_s = getContentResolver().openInputStream(uri);
            XmlPullParserFactory pullParserFactory;
            try {
                pullParserFactory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = pullParserFactory.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(in_s, "utf-8");

                int eventType = parser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    String name = null;

                    switch (eventType) {

                        case XmlPullParser.START_TAG:
                            name = parser.getName();
                            if (name.equalsIgnoreCase("add")) {
                                parser.require(XmlPullParser.START_TAG, null, "add");
                                String add = parser.getName();
                                String relType = parser.getAttributeValue(null, "key");
                                if (add.equalsIgnoreCase("add")) {
                                    if (relType.equalsIgnoreCase("lastPlayerId")) {
                                        playerId = parser.getAttributeValue(null, "value");
                                    } else if (relType.equalsIgnoreCase("lastPlayerSecret")) {
                                        playerSecret = parser.getAttributeValue(null, "value");
                                    }
                                }
                            }
                            break;

                    }
                    eventType = parser.next();
                }

            } catch (XmlPullParserException e) {

                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        EditText playerId_editText = (EditText) findViewById(R.id.playerIdText);
        EditText playerSec_editText = (EditText) findViewById(R.id.playerSecretText);
        playerId_editText.setText(playerId);
        playerSec_editText.setText(playerSecret);
    }

    @Override
    public void playerServiceResult(String command, MethodResult result) {
        if (result.success) {
//            String key = String.format(ApplicationMessageTemplates.VISIT_KEY.getemplateString(), BundleKeys.LOGIN_RESPONSE.toString(), playerId);
//            BundleHelper bundleHelper = new BundleHelper(key);
//            try {
//                SWCLoginResponseData jsonLogin = new SWCLoginResponseData(StringUtil.stringToJsonObject(bundleHelper.get_value(this)));
//
//                playerName = jsonLogin.name();
//                playerFaction = jsonLogin.mplayerModel().faction();
//
//                GuildModel guildModel = new GuildModel(jsonLogin.mplayerModel().guildInfo().toString());
//                playerGuild = guildModel.getGuildName();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            savedb();
        } else {
            if (result.getmException() != null) {
                result.getmException().printStackTrace();
            } else {
                showToast("Couldn't verify player credentials: " + result.getMessage());
            }


        }
        saveBtnClicked = false;
    }


    @Override
    public void finishDownloading() {

        super.finishDownloading();
        playerId_editText.setEnabled(true);
        playerSecret_editText.setEnabled(true);
        mDownloading = false;
        saveBtnClicked = false;
    }

    public void savedb() {

        try {

            finish();
            ActivitySwitcher.launchMainActivity(this);
            showToast("Added player successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            showToast(e.getMessage());

        }

    }

    public void handleListUpdate(String activity_command, boolean success) {

    }


//    public static class SyncStatusUpdaterFragment extends Fragment
//            implements DetachableResultReceiver.Receiver {
//        public static final String TAG = SyncStatusUpdaterFragment.class.getName();
//
//        private boolean mSyncing = false;
//        private DetachableResultReceiver mReceiver;
//
//        @Override
//        public void onCreate(Bundle savedInstanceState) {
//
//            super.onCreate(savedInstanceState);
//            setRetainInstance(true);
//            mReceiver = new DetachableResultReceiver(new Handler());
//            mReceiver.setReceiver(this);
//        }
//
//        /**
//         * {@inheritDoc}
//         */
//
//        public void updateProgress(String message, int progress, int max) {
//            AddVisitorPlayer activity = (AddVisitorPlayer) getActivity();
//            if (activity == null) {
//                return;
//            }
//            activity.updateRefreshStatus(true, message, true, progress, max);
//        }
//
//        public void onReceiveResult(int resultCode, Bundle resultData) {
//
//            AddVisitorPlayer activity = (AddVisitorPlayer) getActivity();
//            if (activity == null) {
//                return;
//            }
//
//
//            switch (resultCode) {
//                case ActualPlayer.ActualPlayerCallBackInterface.status_codes.STATUS_RUNNING:
//                    mSyncing = true;
//
//                    break;
//                case status_codes.COMPLETE:
//                    mSyncing = false;
//
//                    ((AddVisitorPlayer) getActivity()).hideProgressView();
//                    break;
//
//
//            }
//
//        }
//
//        @Override
//        public void onActivityCreated(Bundle savedInstanceState) {
//
//            super.onActivityCreated(savedInstanceState);
////            ((AddVisitorPlayer) getActivity()).updateRefreshStatus(mSyncing, "");
//        }
//    }

}
