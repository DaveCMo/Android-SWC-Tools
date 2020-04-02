package com.swctools.activity_modules.defence_tracker;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.widget.RemoteViews;

import com.swctools.R;
import com.swctools.activity_modules.player.PlayerActivity;
import com.swctools.config.AppConfig;
import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;
import com.swctools.common.enums.BattleOutcome;
import com.swctools.common.enums.BundleKeys;
import com.swctools.common.helpers.DateTimeHelper;
import com.swctools.activity_modules.main.MainActivity;
import com.swctools.activity_modules.player.models.Battle;
import com.swctools.activity_modules.player.models.BattleLogs;
import com.swctools.common.models.player_models.DonatedTroops;
import com.swctools.common.models.player_models.PlayerModel;
import com.swctools.common.notifications.NotificationDismissedReceiver;
import com.swctools.common.notifications.NotificationInterface;
import com.swctools.swc_server_interactions.runnables.SWC_COMMON;
import com.swctools.util.MethodResult;
import com.swctools.util.StringUtil;

import java.util.ArrayList;
import java.util.Map;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

public class DefenceLogJobService extends com.firebase.jobdispatcher.JobService {
    private static final String TAG = "DefenceLogJobService";
    private static AppConfig appConfig;
    private boolean jobCancelled = false;
    private Context context;
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

//    private int newBattles = 0;
//    private int wins = 0;
//    private int defeats = 0;
//    private int medals = 0;
//    private BattleLogs newBattleLog;
//    private DonatedTroops donatedTroops;
//    private int troopsInSC;
//    private int scCap = 0;
//    private int scDonated = 0;
//    private List<String> battleList;
//    private List<Integer> battleListViews;


    @Override
    public boolean onStartJob(com.firebase.jobdispatcher.JobParameters jobParameters) {

        this.context = getApplicationContext();
        appConfig = new AppConfig(context);
        doBackgroundWork(jobParameters);


        return true;
    }

    private void doBackgroundWork(final com.firebase.jobdispatcher.JobParameters jobParameters) {

        new Thread(new Runnable() {
            @Override
            public void run() {


                String[] columns = {DatabaseContracts.PlayersTable.PLAYERID, DatabaseContracts.PlayersTable.NOTIFICATIONS};
                String selection = DatabaseContracts.PlayersTable.NOTIFICATIONS + " = ? ";
                String[] selectionArgs = {"1"};

                Cursor cursor = null;
                try {

                    cursor = DBSQLiteHelper.queryDB(DatabaseContracts.PlayersTable.TABLE_NAME, null, selection, selectionArgs, context);

                    while (cursor.moveToNext()) {
                        if (jobCancelled) {
                            return;
                        }
                        String playerId = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.PlayersTable.PLAYERID));
                        try {
                            //Visit the player:
                            MethodResult visitResult = SWC_COMMON.visitNeighbour(playerId, false, getApplicationContext());

                            if (visitResult.success) {
                                DefenceNotificationHelper defenceNotificationHelper = new DefenceNotificationHelper(playerId, context);
                                int notifId = NotificationInterface.DEFENCE_NOTIF + cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.PlayersTable.COLUMN_ID));
                                defenceNotificationHelper.processLog(notifId);
//                                PlayerModel playerModel = new PlayerModel(playerId, context);
//                                playerModel.buildModel();
//                                playerModel.buildBattleLog();
////                                //Retrieve the data from DB
//                                String bundleKey = String.format(ApplicationMessageTemplates.VISIT_KEY.getemplateString(),
//                                        BundleKeys.VISIT_RESPONSE.toString(), playerId);
//                                BundleHelper bundleHelper = new BundleHelper(bundleKey);
//                                SWCVisitResult disneyVisitResult = new SWCVisitResult(bundleHelper.get_value(context));
//                                String playerName = StringUtil.htmlRemovedGameName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.PlayersTable.PLAYERNAME)));
//                                processDefenceLog(playerModel, playerId, playerName, notifId);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        try { //to prevent server thinking this is hack, or DDOS??? Well that's the intention anyways.
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }

                }
                jobFinished(jobParameters, true);

            }
        }).start();
    }


    private void processDefenceLog(PlayerModel playerModel, String playerId, String playerName, int notifId) {

//
//        BattleLogs newTmpBattleLogs = new BattleLogs(playerId, jsonVisitor.mplayerModel().battleLogs(), context);
//


        //****THIS SECTION IS GOOD
        DefenceNotificationHelper defenceNotificationHelper = new DefenceNotificationHelper(playerId, context);

        fireZeNotificationMissles(playerId, playerName, playerModel, notifId);
        //****THIS SECTION IS GOOD


//
//

    }

    @Override
    public boolean onStopJob(com.firebase.jobdispatcher.JobParameters jobParameters) {
        jobCancelled = true;
        return false;
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

    private void fireZeNotificationMissles(String playerId, String playerName, PlayerModel playerModel, int NOTIFICATION_ID) {
        ArrayList<Integer> battleListViews = new ArrayList<>();
        battleListViews.add(R.id.notifExpd_Battle1);
        battleListViews.add(R.id.notifExpd_Battle2);
        battleListViews.add(R.id.notifExpd_Battle3);
        battleListViews.add(R.id.notifExpd_Battle4);
        ArrayList<String> battleList = new ArrayList<>();

        int newBattles = 0;
        int wins = 0;
        int defeats = 0;
        int medals = 0;
        BattleLogs newBattleLog;
        DonatedTroops donatedTroops;
        int troopsInSC;
        int scCap = 0;
        int scDonated = 0;

        playerName = StringUtil.htmlRemovedGameName(playerName);
        prepNotificationForLaunch();
        Intent resultIntent = new Intent(getApplicationContext(), PlayerActivity.class);
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
//        expandedLayout.setTextViewText(R.id.notifExpdAttackSummary, String.format(ATTACK_SUMMARY, newBattles, wins, defeats));
        expandedLayout.setTextViewText(R.id.notifExpdTroopsInSC, String.format(TROOPS_IN_SC, playerModel.donatedTroops.getNumInSC()));
        expandedLayout.setProgressBar(R.id.notifExpdprogressBar, scCap, scDonated, false);
        expandedLayout.setTextViewText(R.id.notifExpdSCCap, String.format(SC_CAP, scDonated, scCap));
        expandedLayout.setTextViewText(R.id.notifExpdMedals, String.valueOf(medals));
        long addedDate = (DateTimeHelper.userIDTimeStamp() / 1000);
        String updatedString = String.format(LAST_UPDATED, DateTimeHelper.longDateTime(addedDate, context));
        expandedLayout.setTextViewText(R.id.notifExpdUpdated, updatedString);


        for (Map.Entry<Long, Battle> entry : playerModel.battleLogs.getDefenceLogs().entrySet()) {

            Battle value = entry.getValue();
            if (value.isNewBattle()) {
                newBattles++;
                if (value.getOutcome().toString().equalsIgnoreCase(BattleOutcome.DEFEAT.toString())) {
                    defeats++;
                } else if (value.getOutcome().toString().equalsIgnoreCase(BattleOutcome.VICTORY.toString())) {
                    wins++;
                }
                medals = medals + value.getBattleDelta();
            }
            if (battleList.size() <= battleListViews.size()) {
                String attackerGuild = " ";
                if (StringUtil.isStringNotNull(value.getAttacker().getGuildName())) {
                    attackerGuild = "(" + StringUtil.htmlRemovedGameName(value.getAttacker().getGuildName()) + ")";
                }
                battleList.add(String.format(ATTACKED_BY_RESULT, StringUtil.htmlRemovedGameName(value.getAttacker().getPlayerName()), attackerGuild, value.getBaseDamagePercent(), value.getStars()));
            }

        }

        for (int i = 0; i < playerModel.battleLogs.getBattleLogArray().size() - 2; i++) {
            expandedLayout.setTextViewText(battleListViews.get(i), battleList.get(i));
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

}
//try {
//            newBattles = Integer.parseInt(defenceNotificationHelper.getAggregateValue(playerId, DefenceNotificationHelper.DefenceNotificationKeyEnum.NEW_BATTLES.getKeyName(), context));
//        } catch (NumberFormatException e) {
////            e.printStackTrace();
//        }
//        try {
//            wins = Integer.parseInt(defenceNotificationHelper.getAggregateValue(playerId, DefenceNotificationHelper.DefenceNotificationKeyEnum.WINS.getKeyName(), context));
//        } catch (NumberFormatException e) {
////            e.printStackTrace();
//        }
//        try {
//            defeats = Integer.parseInt(defenceNotificationHelper.getAggregateValue(playerId, DefenceNotificationHelper.DefenceNotificationKeyEnum.DEFEATS.getKeyName(), context));
//        } catch (NumberFormatException e) {
////            e.printStackTrace();
//        }
//        try {
//            medals = Integer.parseInt(defenceNotificationHelper.getAggregateValue(playerId, DefenceNotificationHelper.DefenceNotificationKeyEnum.MEDALS.getKeyName(), context));
//        } catch (NumberFormatException e) {
////            e.printStackTrace();
//        }
//
//        String bundleKey = String.format(ApplicationMessageTemplates.VISIT_KEY.getemplateString(), BundleKeys.DEFENCE_NOTIFICATION_LOG, playerId);
//        BundleHelper bundleHelper = new BundleHelper(bundleKey);
//        String oldBattleLogStr = bundleHelper.get_value(context);
//
//
//        if (StringUtil.isStringNotNull(oldBattleLogStr)) {
//            JsonArray oldBattlogArr = Json.createReader(new StringReader(oldBattleLogStr)).readArray();
//            BattleLogs oldBattleLog = new BattleLogs(playerId, oldBattlogArr, context);
//            newBattleLog = BattleLogHelper.newBattleLog(oldBattleLog, newTmpBattleLogs);
//        } else {
//            newBattleLog = newTmpBattleLogs;
//        }
//
//        bundleHelper._value = newBattleLog.getBattleLogArray().toString();
//        bundleHelper.commit(context);
//        battleList = new ArrayList<>();
//        for (Map.Entry<Long, Battle> entry : newBattleLog.getDefenceLogs().entrySet()) {
//
//            Battle value = entry.getValue();
//            if (value.isNewBattle()) {
//                newBattles++;
//                if (value.getOutcome().toString().equalsIgnoreCase(BattleOutcome.DEFEAT.toString())) {
//                    defeats++;
//                } else if (value.getOutcome().toString().equalsIgnoreCase(BattleOutcome.VICTORY.toString())) {
//                    wins++;
//                }
//                medals = medals + value.getBattleDelta();
//            }
//            if (battleList.size() <= battleListViews.size()) {
//                String attackerGuild = " ";
//                if (StringUtil.isStringNotNull(value.getAttacker().getGuildName())) {
//                    attackerGuild = "(" + StringUtil.htmlRemovedGameName(value.getAttacker().getGuildName()) + ")";
//                }
//                battleList.add(String.format(ATTACKED_BY_RESULT, StringUtil.htmlRemovedGameName(value.getAttacker().getPlayerName()), attackerGuild, value.getBaseDamagePercent(), value.getStars()));
//            }
//
//        }
//
//
//        MapBuildings buildings = new MapBuildings(jsonVisitor.mplayerModel().map());
//        GuildModel guildModel = new GuildModel(jsonVisitor.mplayerModel().guildInfo().toString());
//        String guildId = guildModel.getGuildId();
////        donatedTroops = new DonatedTroops(jsonVisitor.mplayerModel().donatedTroops(), jsonVisitor.mplayerModel().faction(), buildings.getSquadBuilding(), guildId, context);
//        troopsInSC = donatedTroops.getNumInSC();
//        scCap = donatedTroops.getSquadBuildingCap();
//        scDonated = donatedTroops.capDonated();

//        defenceNotificationHelper.saveNewDefenceNotificationData(newBattles, medals, wins, defeats, 0, 0, 0, context);