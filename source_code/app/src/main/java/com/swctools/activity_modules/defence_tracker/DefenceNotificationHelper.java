package com.swctools.activity_modules.defence_tracker;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.core.content.ContextCompat;
import android.widget.RemoteViews;

import com.swctools.R;
import com.swctools.common.helpers.BundleHelper;
import com.swctools.common.helpers.DateTimeHelper;
import com.swctools.activity_modules.main.MainActivity;
import com.swctools.activity_modules.player.PlayerActivity;
import com.swctools.common.enums.BundleKeys;
import com.swctools.activity_modules.player.models.Battle;
import com.swctools.activity_modules.player.models.BattleLogs;
import com.swctools.common.models.player_models.DonatedTroops;
import com.swctools.common.models.player_models.PlayerModel;
import com.swctools.common.notifications.NotificationInterface;
import com.swctools.common.notifications.NotificationDismissedReceiver;
import com.swctools.config.AppConfig;
import com.swctools.util.StringUtil;

import java.util.ArrayList;
import java.util.Map;

public class DefenceNotificationHelper {
    private static final String TAG = "DefenceNotifHelper";
    private static final String KEY_TEMPLATE = "DEFENCELOG|%1$s|%2$s";
    private String playerId;
    private Context context;
    private int battles, medals, wins, defeats, credits, alloy, contra;

    private static AppConfig appConfig;
    private boolean jobCancelled = false;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder builder;
    private String NOTIFICATION_CHANNEL_ID = NotificationInterface.CHANNEL_NAME;
    private String NOTIFICATION_CHANNEL_NAME = NotificationInterface.DESCRIPTION;
    private String ATTACK_SUMMARY = "%1$s new attacks (%2$s wins | %3$s defeats)";
    private String TROOPS_IN_SC = "Troops in SC: %1$s";
    private String SC_CAP = "%1$s/%2$s";
    private String PROTECETED_UNTIL = "Protected Until %1$s";
    private String ATTACKED_BY_RESULT = "%1$s %2$s | %3$s%% %4$s stars";
    private String LAST_UPDATED = "Updated %1$s";


    public DefenceNotificationHelper(String pId, Context c) {
        playerId = pId;
        context = c;

    }

    public void saveNewDefenceNotificationData(int battles, int medals, int wins, int defeats, int credits, int alloy, int contra, Context context) {


        this.battles = battles;
        saveValue(DefenceNotificationKeyEnum.NEW_BATTLES.getKeyName(), battles);
        this.medals = medals;
        saveValue(DefenceNotificationKeyEnum.MEDALS.getKeyName(), medals);
        this.wins = wins;
        saveValue(DefenceNotificationKeyEnum.WINS.getKeyName(), wins);
        this.defeats = defeats;
        saveValue(DefenceNotificationKeyEnum.DEFEATS.getKeyName(), defeats);
        this.credits = credits;
        saveValue(DefenceNotificationKeyEnum.CREDITS.getKeyName(), credits);
        this.alloy = alloy;
        saveValue(DefenceNotificationKeyEnum.ALLOY.getKeyName(), alloy);
        this.contra = contra;
        saveValue(DefenceNotificationKeyEnum.CONTRA.getKeyName(), contra);
    }


    public void processLog(int NOTIFICATION_ID) {

        PlayerModel playerModel = new PlayerModel(playerId, context);
        playerModel.buildModel();
        playerModel.buildBattleLog();
        fireZeNotificationMissles(playerModel, NOTIFICATION_ID);


    }


    private void fireZeNotificationMissles(PlayerModel playerModel, int NOTIFICATION_ID) {
        ArrayList<Integer> battleListViews = new ArrayList<>();
        battleListViews.add(R.id.notifExpd_Battle1);
        battleListViews.add(R.id.notifExpd_Battle2);
        battleListViews.add(R.id.notifExpd_Battle3);
        battleListViews.add(R.id.notifExpd_Battle4);
        ArrayList<String> battleList = new ArrayList<>();


        int wins = 0;
        int defeats = 0;
        int medals = 0;
        BattleLogs newBattleLog;
        DonatedTroops donatedTroops;

        int scCap;
        int scDonated;

        String playerName = StringUtil.htmlRemovedGameName(playerModel.playerName);
        prepNotificationForLaunch();
        Intent resultIntent = new Intent(context, PlayerActivity.class);
        resultIntent.putExtra(BundleKeys.PLAYER_ID.toString(), playerId);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        resultIntent.setAction(Long.toString(System.currentTimeMillis()));
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT);

        String protectedUntil = "";
        if (playerModel.mplayerModel.protectedUntil() == 0) {
            protectedUntil = "None (Open)";
        } else {
            protectedUntil = String.format(PROTECETED_UNTIL, DateTimeHelper.longDateTime(playerModel.mplayerModel.protectedUntil(), context));
        }

        RemoteViews collapsedLayout = new RemoteViews(context.getPackageName(), R.layout.notification_collapsed);
        collapsedLayout.setTextViewText(R.id.notifClpsdTitle, playerName);
        collapsedLayout.setTextViewText(R.id.notifClpsdProtUntil, protectedUntil);

        RemoteViews expandedLayout = new RemoteViews(context.getPackageName(), R.layout.notification_expanded);

        expandedLayout.setTextViewText(R.id.notifExpdTitle, playerName);
        expandedLayout.setTextViewText(R.id.notifExpdProtUntil, protectedUntil);
        expandedLayout.setTextViewText(R.id.notifExpdTroopsInSC, String.format(TROOPS_IN_SC, playerModel.donatedTroops.getNumInSC()));
        expandedLayout.setProgressBar(R.id.notifExpdprogressBar, playerModel.donatedTroops.getSquadBuildingCap(), playerModel.donatedTroops.capDonated(), false);
        expandedLayout.setTextViewText(R.id.notifExpdSCCap, String.format(SC_CAP, playerModel.donatedTroops.getSquadBuildingCap(), playerModel.donatedTroops.capDonated()));
//        expandedLayout.setTextViewText(R.id.notifExpdMedals, String.valueOf(medals));
        long addedDate = (DateTimeHelper.userIDTimeStamp() / 1000);
        String updatedString = String.format(LAST_UPDATED, DateTimeHelper.longDateTime(addedDate, context));
        expandedLayout.setTextViewText(R.id.notifExpdUpdated, updatedString);

        defeats = playerModel.battleLogs.getLosses();
        wins = playerModel.battleLogs.getWins();
        int i = 0;
        for (Map.Entry<Long, Battle> entry : playerModel.battleLogs.getDefenceLogs().entrySet()) {

            Battle value = entry.getValue();

            String attackerGuild = " ";
            if (StringUtil.isStringNotNull(value.getAttacker().getGuildName())) {
                attackerGuild = "(" + StringUtil.htmlRemovedGameName(value.getAttacker().getGuildName()) + ")";
            }

            expandedLayout.setTextViewText(battleListViews.get(i), String.format(ATTACKED_BY_RESULT, StringUtil.htmlRemovedGameName(value.getAttacker().getPlayerName()), attackerGuild, value.getBaseDamagePercent(), value.getStars()));
            i++;
            if (i == battleListViews.size()) {
                break;
            }
        }


        builder.setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(collapsedLayout)
                .setCustomBigContentView(expandedLayout)
                .setContentIntent(resultPendingIntent)
                .setDeleteIntent(createOnDismissedIntent(context, NOTIFICATION_ID, playerId))
        ;
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private PendingIntent createOnDismissedIntent(Context context, int notificationId, String playerId) {
        Intent intent = new Intent(context, NotificationDismissedReceiver.class);
        intent.setAction("ACTION");
        intent.putExtra(BundleKeys.PLAYER_ID.toString(), playerId);

        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(context.getApplicationContext(),
                        notificationId, intent, 0);
        return pendingIntent;
    }

    private void prepNotificationForLaunch() {
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
                .setColor(ContextCompat.getColor(context, R.color.colorAccent))
                .setAutoCancel(true);
    }


    private void saveValue(String keyPart, int val) {

        String bundleKey = String.format(KEY_TEMPLATE, playerId, keyPart);
        BundleHelper bundleHelper = new BundleHelper(bundleKey);
        bundleHelper._value = String.valueOf(val);
        bundleHelper.commit(context);


    }

    public static void clearAll(String playerId, Context context) {
        BundleHelper bundleHelper = new BundleHelper(null);
        for (DefenceNotificationKeyEnum keyEnum : DefenceNotificationKeyEnum.values()) {
            bundleHelper._key = String.format(KEY_TEMPLATE, playerId, keyEnum.getKeyName());
            bundleHelper.deleteBundleValue(context);
        }
    }

    public String getAggregateValue(String playerId, String keyPart, Context context) {
        String bundleKey = String.format(KEY_TEMPLATE, playerId, keyPart);
        BundleHelper bundleHelper = new BundleHelper(bundleKey);
        return bundleHelper.get_value(context);
    }


    public enum DefenceNotificationKeyEnum {
        NEW_BATTLES("NEW_BATTLES"),
        MEDALS("MEDALS"),
        WINS("WINS"),
        DEFEATS("DEFEATS"),
        CREDITS("CREDITS"),
        ALLOY("ALLOY"),
        CONTRA("CONTRA");


        private final String KeyName;

        DefenceNotificationKeyEnum(String KeyName) {
            this.KeyName = KeyName;
        }

        public final String getKeyName() {
            return this.KeyName;
        }
    }

}
