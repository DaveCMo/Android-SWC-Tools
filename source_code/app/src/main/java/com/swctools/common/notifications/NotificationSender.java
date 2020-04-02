package com.swctools.common.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import com.swctools.R;
import com.swctools.activity_modules.main.MainActivity;

public class NotificationSender {
    private static final String TAG = "NotificationSender";
    private Context context;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder builder;
    private String NOTIFICATION_CHANNEL_ID = NotificationInterface.CHANNEL_NAME;
    private String NOTIFICATION_CHANNEL_NAME = NotificationInterface.DESCRIPTION;

    public NotificationSender(Context context) {
        this.context = context;


        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notificationManager.createNotificationChannel(notificationChannel);
        }

        builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_stat_rebellionempire_symbol)
                .setAutoCancel(true);


    }

    public void showSimpleNotification(String title, String content, Intent resultIntent, int NOTIFICATION_ID) {
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(resultPendingIntent);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }


    public void showPersistentNotification(String title, String content, Intent resultIntent, int NOTIFICATION_ID) {
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder
                .setContentTitle(title)
                .setOngoing(true)
                .setContentText(content)
                .setContentIntent(resultPendingIntent);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }




}
