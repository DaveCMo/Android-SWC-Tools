package com.swctools.activity_modules;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.swctools.R;
import com.swctools.activity_modules.image_db_to_disk_migration.DBToDiskFragmentActivityInterface;
import com.swctools.activity_modules.image_db_to_disk_migration.DBToDiskImageFragment;
import com.swctools.activity_modules.updates.DataUpdateHandler;
import com.swctools.activity_modules.updates.UpdateDataActivity;
import com.swctools.activity_modules.updates.UpdateService;
import com.swctools.activity_modules.updates.VersionJSON;
import com.swctools.common.enums.BundleKeys;
import com.swctools.common.http.DownloadHttp;
import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;
import com.swctools.util.ActivitySwitcher;
import com.swctools.util.MethodResult;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class Splash extends AppCompatActivity implements DBToDiskFragmentActivityInterface {
    private static final String TAG = "SPLASH";
    private TextView releaseText;
    private TextView progressMessage;
    private Button continue_bn;
    private ProgressBar imageProgress;

    private CheckBox chkDontShow;
    private Context context;
    private DBToDiskImageFragment dbToDiskImageFragment;
    private boolean clicked;
    private boolean imageDone = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context = this;
        releaseText = findViewById(R.id.releaseText);
        continue_bn = findViewById(R.id.continue_bn);
        chkDontShow = findViewById(R.id.chkDontShow);
        imageProgress = findViewById(R.id.imageProgress);
        progressMessage = findViewById(R.id.progressMessage);


        String t = readTxt();
        dbToDiskImageFragment = DBToDiskImageFragment.getInstance(getSupportFragmentManager());

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            releaseText.setText(Html.fromHtml(t, Html.FROM_HTML_MODE_LEGACY));
        } else {
            releaseText.setText(Html.fromHtml(t));
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }

        continue_bn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!clicked) {
                    if (!imageDone) {
                        clicked = true;
                        dbToDiskImageFragment.doWork();
                    } else {
                        processData();
                    }
                }
            }
        });
        skipOrStay();

    }

    private void processData() {
        GetDataUpdates getDataUpdates = new GetDataUpdates(this);
        getDataUpdates.execute(UpdateService.URL_KEY, String.valueOf(chkDontShow.isChecked()));
    }


    private void skipOrStay() {

        String value = "";
        Cursor cursor = null;

        try {
            String whereClause = DatabaseContracts.VersionLog.COLUMN_ID + " = ?";
            String[] whereArgs = {"1"};
            String[] columns = {DatabaseContracts.VersionLog.VALUE};

            cursor = DBSQLiteHelper.queryDB(DatabaseContracts.VersionLog.TABLE_NAME, columns, whereClause, whereArgs, this);
            while (cursor.moveToNext()) {
                value = cursor.getString(cursor.getColumnIndex(DatabaseContracts.VersionLog.VALUE));
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        if (!value.equalsIgnoreCase("1")) {
            finish();
            ActivitySwitcher.launchMainActivity(this);
        }

    }


    private String readTxt() {

        //getting the .txt file
        InputStream inputStream = getResources().openRawResource(R.raw.releasenote);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            int i = inputStream.read();
            while (i != -1) {
                byteArrayOutputStream.write(i);
                i = inputStream.read();
            }
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArrayOutputStream.toString();
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
    public void sendProgress(int progress, int max) {
        if (imageProgress.getVisibility() != View.VISIBLE) {
            imageProgress.setVisibility(View.VISIBLE);
        }
        progressMessage.setVisibility(View.VISIBLE);
        progressMessage.setText("Converting images: " + progress + " of " + max);
        imageProgress.setMax(max);
        imageProgress.setProgress(progress);
    }

    @Override
    public void taskEnded(Boolean result) {
        imageDone = true;
        clicked = false;
        if (result) {
            progressMessage.setText("Complete!");
            Log.d(TAG, "taskEnded: ");
            chkDontShow.setVisibility(View.VISIBLE);
            imageProgress.setMax(1);
            imageProgress.setProgress(1);
            continue_bn.setText("NEXT (2/2)");
        }
    }

    @Override
    public void postImage(Bitmap bitmap) {

    }


    private static class GetDataUpdates extends AsyncTask<String, Integer, MethodResult> {
        private WeakReference<Splash> weakReference;

        GetDataUpdates(Splash activity) {
            weakReference = new WeakReference<Splash>(activity);
        }


        @Override
        protected void onPreExecute() {
            Splash splash = weakReference.get();

            if (splash == null || splash.isFinishing()) {
                return;
            }
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) splash.getSystemService(splash.getApplicationContext().CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo == null || !networkInfo.isConnected() ||
                    (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                            && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
                cancel(true);
            }
        }

        @Override
        protected MethodResult doInBackground(String... strings) {
            Splash splash = weakReference.get();

            String urlToDownload = strings[0];
            boolean dontShowAgain = Boolean.parseBoolean(strings[1]);
            try {
                URL url = new URL(urlToDownload);
                try {
                    String resultString = DownloadHttp.downloadUrl(url);
                    Log.d(TAG, "run: " + resultString);
                    if (resultString != null) {
                        try {
                            DBSQLiteHelper.deleteDbRows(DatabaseContracts.DataVersionUpdateHistory.TABLE_NAME, null, null, splash);
                            DataUpdateHandler dataUpdateHandler = new DataUpdateHandler(new VersionJSON(resultString), false, splash);
                            dataUpdateHandler.setDataTablesDetailsList();
                            if (dontShowAgain) {
                                ContentValues contentValues = new ContentValues();
                                contentValues.put(DatabaseContracts.VersionLog.VALUE, 0);
                                DBSQLiteHelper.updateAllData(DatabaseContracts.VersionLog.TABLE_NAME, contentValues, splash);
                            }

                            return new MethodResult(true, dataUpdateHandler);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return new MethodResult(false, e.getMessage());
                        }
                    } else {
                        return new MethodResult(false, "Unable to download update data!");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return new MethodResult(false, e.getMessage());
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return new MethodResult(false, e.getMessage());
            }

        }


        @Override
        protected void onPostExecute(MethodResult methodResult) {
            Splash splash = weakReference.get();

            if (methodResult.success) {
                if (methodResult.getResultObject() instanceof DataUpdateHandler) {
                    DataUpdateHandler dataUpdateHandler = (DataUpdateHandler) methodResult.getResultObject();
                    Intent intent;
                    intent = new Intent(splash, UpdateDataActivity.class);
                    intent.putParcelableArrayListExtra(BundleKeys.DATA_TABLES_TO_UPDATE.toString(), dataUpdateHandler.getDataTablesDetails());
                    splash.startActivity(intent);
                    splash.finish();

                }
            }
        }
    }


}

