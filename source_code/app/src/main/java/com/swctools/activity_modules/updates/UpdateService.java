package com.swctools.activity_modules.updates;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;

import com.swctools.R;
import com.swctools.activity_modules.main.MainActivity;
import com.swctools.common.enums.BundleKeys;
import com.swctools.util.MethodResult;

import java.io.IOException;
import java.net.URL;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import static com.swctools.App.UPDATE_CHECKER_CHANNEL_ID;
import static com.swctools.common.http.DownloadHttp.downloadUrl;

public class UpdateService extends Service {

    private static final int NOTIFICATION_ID = 1112062020;
    private static final String TAG = "UpdateService";
    public static final String URL_KEY = "https://raw.githubusercontent.com/DaveCMo/Android-SWC-Tools/master/swc_tools_version_info";
    private Context context;
    private final IBinder mBinder = new UpdateServiceBinder();
    private FetchAppUpdatesTask mDownloadTask;
    private boolean silentUpdate = false;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, UPDATE_CHECKER_CHANNEL_ID)
                .setContentTitle("SWC Tools Update Checker...")
                .setContentText("Checking for new versions of the app or data")
                .setSmallIcon(R.drawable.ic_stat_rebellionempire_symbol)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(NOTIFICATION_ID, notification);


        try {
            silentUpdate = intent.getBooleanExtra(BundleKeys.SILENT_UPDATE.toString(), false);
        } catch (Exception e) {
            silentUpdate = false;
        }
        context = getApplicationContext();
        mDownloadTask = new FetchAppUpdatesTask();
        mDownloadTask.execute(URL_KEY);
        return Service.START_REDELIVER_INTENT;
    }


    @Nullable

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public class UpdateServiceBinder extends Binder {
        public UpdateService getService() {
            return UpdateService.this;
        }
    }


    class FetchAppUpdatesTask extends AsyncTask<String, Integer, MethodResult> {
        @Override

        protected MethodResult doInBackground(String... urls) {
            MethodResult result = null;
            if (!isCancelled() && urls != null && urls.length > 0) {
                String urlString = urls[0];
                try {
                    URL url = new URL(urlString);
                    String resultString = downloadUrl(url);
                    if (resultString != null) {
                        result = new MethodResult(true, resultString);
                        try {
                            DataUpdateHandler dataUpdateHandler = new DataUpdateHandler(new VersionJSON(resultString), silentUpdate, context);
                            dataUpdateHandler.dataUpdateHandlerExecuteNotificationUpdate();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        throw new IOException("No response received.");
                    }
                } catch (Exception e) {
                    result = new MethodResult(false, e.getMessage());
                }
            }
            return result;
        }

        @Override
        protected void onPreExecute() {

            ConnectivityManager connectivityManager =
                    (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo == null || !networkInfo.isConnected() ||
                    (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                            && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
                cancel(true);
            }
        }

        @Override
        protected void onPostExecute(MethodResult s) {
            super.onPostExecute(s);
            stopForeground(true);
        }

        class Result {
            public String mResultValue;
            public Exception mException;

            public Result(String resultValue) {
                mResultValue = resultValue;
            }

            public Result(Exception exception) {
                mException = exception;
            }
        }


    }
}
