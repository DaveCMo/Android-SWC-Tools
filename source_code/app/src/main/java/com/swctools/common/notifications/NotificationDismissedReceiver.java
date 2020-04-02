package com.swctools.common.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.swctools.common.enums.BundleKeys;
import com.swctools.activity_modules.defence_tracker.DefenceNotificationHelper;
import com.swctools.util.StringUtil;

public class NotificationDismissedReceiver extends BroadcastReceiver {
    private static final String TAG = "NotifDissedRrr";
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            String playerId = intent.getStringExtra(BundleKeys.PLAYER_ID.toString());
            if (StringUtil.isStringNotNull(playerId)){
                DefenceNotificationHelper.clearAll(playerId, context);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
