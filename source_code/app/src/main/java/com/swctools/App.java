package com.swctools;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;

public class App extends Application {
    private static final String TAG = "App";
    public static final String IMAGE_NOTIFICATION_CHANNEL = "SWC Tools - Image Migration";
    public static final String SWC_SERVER_CALLS = "SWC_SERVER_CALLS";
    public static final String UPDATE_CHECKER_CHANNEL_ID = "SWC Tools Update Check";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel updateChannel = new NotificationChannel(
                    UPDATE_CHECKER_CHANNEL_ID,
                    "SWC Tools - Checking for updates...",
                    NotificationManager.IMPORTANCE_DEFAULT

            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(updateChannel);

        }
    }

}
