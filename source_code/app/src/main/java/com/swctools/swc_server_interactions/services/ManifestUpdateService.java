package com.swctools.swc_server_interactions.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.swctools.BuildConfig;
import com.swctools.activity_modules.main.MainActivity;
import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;
import com.swctools.common.enums.BundleKeys;
import com.swctools.common.http.DownloadHttp;
import com.swctools.activity_modules.updates.DisneyCAEFile;
import com.swctools.config.AppConfig;
import com.swctools.util.SaveJsonFile;
import com.swctools.util.StringUtil;

import java.io.IOException;
import java.net.URL;

import javax.json.JsonArray;
import javax.json.JsonObject;

public class ManifestUpdateService extends IntentService {
    private static final String TAG = "ManifestUpdateService";
    private static final String AUTHORITY =
            BuildConfig.APPLICATION_ID + ".provider";
    private static int NOTIFY_ID = 8000;
    private static int FOREGROUND_ID = 8001;
    private Context mContext;
    private AppConfig appConfig;
    private static final String NOTIFICATION_CHANNEL_ID = "com.swctools.swc_server_interactions.services.ManifestUpdateService";
    private static final String channelName = "Manifest Update Service";

    public ManifestUpdateService() {
        super(TAG);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyOwnForeground() {

        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(android.R.drawable.stat_sys_download_done)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startMyOwnForeground();
        } else {
            startForeground(1, new Notification());
        }
        startForeground(2, notification);
    }

    @Override
    protected void onHandleIntent(Intent i) {
        String title = i.getStringExtra(BundleKeys.DIALOG_TITLE.toString());
        int manifestVersion = i.getIntExtra(BundleKeys.MANIFESTVERSION.toString(), 0);
        String manifestUrl = i.getStringExtra(BundleKeys.MANIFESTURL.toString());
        String patchFile = i.getStringExtra(BundleKeys.PATCHESFILE.toString());
        mContext = getApplicationContext();
        appConfig = new AppConfig(mContext);
        startForeground(FOREGROUND_ID,
                buildForegroundNotification(title));


        //First step, download the latest manifest data, and get the relevant version numbers for the files we are going to download:
        try

        {
            URL downloadPth = new URL(manifestUrl);
            String manifestData = DownloadHttp.downloadUrl(downloadPth);
            try {
                JsonObject manifestJsonObj = StringUtil.stringToJsonObject(manifestData);
                SaveJsonFile.saveJsonFile(manifestJsonObj.toString(), "Manifest Data", "Manifest", ".json");
                int latestCAEVersion = manifestJsonObj.getJsonObject("paths").getJsonObject("patches/cae.json").getInt("v");
                if (latestCAEVersion > appConfig.getKnownSwcCAEVersion()) {//then we begin!
                    String CAEdownloadUrl = appConfig.getSWCPatchesURL().concat("/").concat(String.valueOf(latestCAEVersion)).concat("/patches/cae.json");
                    URL caeFile = new URL(CAEdownloadUrl);
                    String caeDataJsonString = DownloadHttp.downloadUrl(caeFile);

                    DisneyCAEFile disneyCAEFile = new DisneyCAEFile(caeDataJsonString);
                    processData(DatabaseContracts.ConflictData.DATACOLUMNS, DatabaseContracts.ConflictData.TABLE_NAME, disneyCAEFile.getTournamentData());
                    appConfig.setKnownCAEVersion(latestCAEVersion);
                }
            } catch (Exception e) {
                e.printStackTrace();
                raiseNotification("Error", "Error downloading conflict data", e);
            }
            appConfig.setKnownSWCManifest(manifestVersion);
            raiseNotification("Complete", "Conflict data updated succesfully!", null);
        } catch (
                IOException e)

        {
            raiseNotification("Error", "Error downloading conflict data", e);
            e.printStackTrace();
        } finally

        {
            stopForeground(true);
        }

    }


    private void processData(String[] columns, String tableName, JsonArray jsonValues) {
        ContentValues contentValues = new ContentValues();
        for (int i = 0; i < jsonValues.size(); i++) {
            for (int j = 0; j < columns.length; j++) {
                contentValues.put(columns[j], jsonValues.getJsonObject(i).getString(columns[j]));
            }
            try {

                DBSQLiteHelper.insertReplaceData(tableName, contentValues, mContext);

            } catch (Exception e) {

            } finally {
            }
        }

    }

    private void raiseNotification(String title, String contenrText,
                                   Exception e) {

        NotificationCompat.Builder b = new NotificationCompat.Builder(this, String.valueOf(FOREGROUND_ID));

        b.setAutoCancel(true).setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis());

        if (e == null) {
            b.setContentTitle(title)
                    .setContentText(contenrText)
                    .setSmallIcon(android.R.drawable.stat_sys_download_done)
                    .setTicker("Complete");

            Intent outbound = new Intent(mContext, MainActivity.class);
            outbound.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            PendingIntent pi = PendingIntent.getActivity(this, 0,
                    outbound, PendingIntent.FLAG_UPDATE_CURRENT);

            b.setContentIntent(pi);
        } else {
            b.setContentTitle("Exception")
                    .setContentText(e.getMessage())
                    .setSmallIcon(android.R.drawable.stat_notify_error)
                    .setTicker("Exception");
        }

        NotificationManager mgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        mgr.notify(NOTIFY_ID, b.build());
    }

    private Notification buildForegroundNotification(String contentText) {

        NotificationCompat.Builder b;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            b = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(notificationChannel);
        } else {
            b = new NotificationCompat.Builder(this, String.valueOf(FOREGROUND_ID));
        }

        b.setOngoing(true)
                .setContentTitle(contentText)
                .setContentText(contentText)
                .setSmallIcon(android.R.drawable.stat_sys_download)
                .setTicker("Downloading");

        return (b.build());
    }
}
