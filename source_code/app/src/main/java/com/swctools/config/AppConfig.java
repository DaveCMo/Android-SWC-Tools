package com.swctools.config;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.swctools.util.StringUtil;

import java.util.UUID;


public class AppConfig extends Application {
    private static final String TAG = "APPCONFIG";
    private static final String PREF_NAME = "_settings";
    private static SharedPreferences pref;
    private static int PRIVATE_MODE = 0;
    private Context _context;
    private Editor editor;

    public AppConfig(Context context) {
        this._context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE); // 0 - for private mode
        editor = pref.edit();
    }

    public void setSWCPatchesURL(String newUrl) {
        editor.putString(Settings.SWCPATCHESBASEURL.toString(), newUrl);
        editor.commit();
    }


    public String getSWCPatchesURL() {
        return pref.getString(Settings.SWCPATCHESBASEURL.toString(), "https://zynga-swc-prod-1-seed.akamaized.net/");
    }

    public int getKnownSwcCAEVersion() {
        return pref.getInt(Settings.SWCCAEVERSION.toString(), 0);
    }

    public void setKnownCAEVersion(int version) {
        editor.putInt(Settings.SWCCAEVERSION.toString(), version);
        editor.commit();
    }

    public int getKnownSwcManifest() {
        return pref.getInt(Settings.SWCMANIFESTVERSION.toString(), 0);
    }

    public void setKnownSWCManifest(int version) {
        editor.putInt(Settings.SWCMANIFESTVERSION.toString(), version);
        editor.commit();
    }

    public int lServerTimeOut() {
        return pref.getInt(Settings.SERVER_TIMEOUT.toString(), 5);
    }

    public void setLServerTimeOut(int timeOut) {
        editor.putInt(Settings.SERVER_TIMEOUT.toString(), timeOut);
        editor.commit();
    }

    public Boolean bNotificationsEnabled() {
        return pref.getBoolean(Settings.NOTIFICATIONS_ON.toString(), false);
    }

    public boolean bLogSWCMessage() {
        return pref.getBoolean(Settings.LOGSWCMESSAGES.toString(), false);
    }

    public void setbLogSWCMessage(boolean b) {
        editor.putBoolean(Settings.LOGSWCMESSAGES.toString(), b);
        editor.commit();
    }

    public Boolean bFavPref() {
        return pref.getBoolean(Settings.LAYOUT_FAV_PREF.toString(), false);
    }

    public void set_bFavPref(boolean b) {
        editor.putBoolean(Settings.LAYOUT_FAV_PREF.toString(), b);
        editor.commit();
    }

    public boolean getPlayerDetailsExpandedHint() {
        return pref.getBoolean(Settings.PLAYERDETAILEXPANDHINT.toString(), true);
    }

    private void set_PlayerDetailsExpandedHint(boolean b) {
        editor.putBoolean(Settings.PLAYERDETAILEXPANDHINT.toString(), b);
        editor.commit();
    }


    public void setPlayerDetailsExpandedHintClicked() {
        int currentVal = pref.getInt(Settings.PLAYERDETAILEXPANDHINTCOUNT.toString(), 0);
        int newVal = currentVal + 1;
        if (!getPlayerDetailsExpandedHint()) {
            editor.putInt(Settings.PLAYERDETAILEXPANDHINTCOUNT.toString(), newVal);
            editor.commit();
        }
        if (newVal > 3) {
            set_PlayerDetailsExpandedHint(false);
        } else {
            set_PlayerDetailsExpandedHint(true);
        }
    }


    public void setPlayerDetailExpanded(boolean b, String setting) {
        editor.putBoolean(setting, b);
        editor.commit();
    }

    public boolean getPlayerDetailExpanded(String setting) {
        return pref.getBoolean(setting, true);
    }


    public int iLayoutLevelThreshold() {
        return pref.getInt(Settings.LAYOUT_LEVEL_THRESHOLD.toString(), 2);
    }

    public Boolean bLayoutImageOn() {
        return pref.getBoolean(Settings.LAYOUT_IMAGE_ON.toString(), true);
    }

    public void setbLayoutImageON(boolean option) {
        editor.putBoolean(Settings.LAYOUT_IMAGE_ON.toString(), option);
        editor.commit();
    }

    public String getBot2ID() {
        return pref.getString(Settings.BOT2_ID.toString(), "00ecf2c7-a8ac-11e8-99dc-0a580a2c102a");
//        return pref.getString(Settings.BOT2_ID.toString(), "ac97bd9b-1179-11e7-8ee3-06f07c004eb5");
    }

    public String getBot2Secret() {
//        return pref.getString(Settings.BOT2_SECRET.toString(), "2316208c826d6c37a443a0546429e831");
        return pref.getString(Settings.BOT2_SECRET.toString(), "2316208c826d6c37a443a0546429e831");
    }


    public String layoutManagerExportFolder() {
        return pref.getString(Settings.LAYOUT_EXPORT_FOLDER.toString(), "layout manager exports");
    }

    public void setbNotificationsEnabled(Boolean bNotificationsEnableded) {
        editor.putBoolean(Settings.NOTIFICATIONS_ON.toString(), bNotificationsEnableded);
        editor.commit();
    }

    public int getNotificationInterval() {
        return pref.getInt(Settings.NOTIFICATIONS_INTERVAL.toString(), 5);
    }

    public void setNotificationInterval(int i) {
        editor.putInt(Settings.NOTIFICATIONS_INTERVAL.toString(), i);
        editor.commit();
    }

    public String getServerAddress() {
//        return pref.getString(Settings.SERVERADDRESS.toString(), "https://starts-app-prod.disney.io/starts/batch/json");
//        return pref.getString(Settings.SERVERADDRESS.toString(), "https://swc-app-prod.apps.starwarscommander.com/starts/batch/json");
        return pref.getString(Settings.SERVERADDRESS.toString(), "https://swc-app-prod.apps.starwarscommander.com/starts/batch/json");
    }

    public void setServerAddress(String serverAddress) {
        editor.putString(Settings.SERVERADDRESS.toString(), serverAddress);
        editor.commit();
    }

    public String shortDateFormat() {
        return pref.getString(Settings.SHORTDATEFORMAT.toString(), "dd-MM-yyyy");
    }

    public String mediumDateFormat() {
        return pref.getString(Settings.LONGDATEFORMAT.toString(), "dd-MMM-yyyy");
    }

    public String dateFormat() {
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");
        return pref.getString(Settings.LONGDATEFORMAT.toString(), "dd-MMM-yyyy HH:mm:ss");
    }

    public int getRetryCount() {
        return pref.getInt(Settings.RETRYCOUNT.toString(), 3);
    }


    public String getVisitorDeviceId() {
        return pref.getString(Settings.VISITOR_DEVICE_ID.toString(), "d3e8b454-fc71-496b-b25e-66a2df4d4717");
    }

    public void setVisitorDeviceID(String s) {
        editor.putString(Settings.VISITOR_DEVICE_ID.toString(), s);
        editor.commit();
    }


    public String getVisitor_playerId() {
        return pref.getString(Settings.VISITORPLAYERID.toString(), "064d8a4f-9441-11e8-bc44-0a580a2c1023");
    }

    public void setVisitor_playerId(String newPID) {
        editor.putString(Settings.VISITORPLAYERID.toString(), newPID);
        editor.commit();
    }

    public void setVisitor_Secret(String newSec) {
        editor.putString(Settings.VISITORPLAYERSECRET.toString(), newSec);
        editor.commit();
    }

    public String getVisitor_playersecret() {
        return pref.getString(Settings.VISITORPLAYERSECRET.toString(), "36b32abaffc4a2f59275172f6cf766c7");
    }

    public void setDateFormat(String dateFormat) {
        String longDateFormat = dateFormat + " HH:mm:ss";
        editor.putString(Settings.LONGDATEFORMAT.toString(), longDateFormat);
        editor.putString(Settings.SHORTDATEFORMAT.toString(), dateFormat);
        editor.commit();
    }

    public boolean getWarDashDisclaimerSetting() {
        return pref.getBoolean(Settings.WARDASHDISCLAIMER.toString(), true);
    }

    public void setGetWarDashDisclaimerSetting(boolean b) {
        editor.putBoolean(Settings.WARDASHDISCLAIMER.toString(), b);
        editor.commit();
    }

    /*WARROOM_INCLUDESTART("WARROOM_INCLUDESTART"),
        WARROOM_OUTPOSTWAR("WARROOM_OUTPOSTWAR"),
        WARROOM_INCLUDEUPLINKS("WARROOM_INCLUDEUPLINKS"),
        WARROOM_SPLITCLEARS("WARROOM_SPLITCLEARS"),
        WARROOM_INCLUDESCORE("WARROOM_INCLUDESCORE"),
        WARROOM_INCLUDEBASESCORE("WARROOM_INCLUDEBASESCORE")
        */

    public boolean getWarSignUpConfig(Settings setting) {
        return pref.getBoolean(setting.toString(), false);
    }

    public void setWar_SignUpConfig(boolean b, Settings setting) {
        editor.putBoolean(setting.toString(), b);
        editor.commit();
    }


    public String getUniqueDeviceID() {
        String s = pref.getString(Settings.UNIQUEDEVICEID.toString(), "");
        if (StringUtil.isStringNotNull(s)) {
            return s;
        } else {
            String newUid = UUID.randomUUID().toString();
            editor.putString(Settings.UNIQUEDEVICEID.toString(), newUid);
            editor.commit();
            return newUid;
        }

    }


    public enum Settings {
        SERVERADDRESS("server_address"),
        RETRYCOUNT("retry_count"),
        LASTPLAYERNAME("last_playerName"),
        LASTPLAYERID("last_playerId"),
        LASTPLAYERSECRET("last_playerSecret"),
        LASTPLAYERFACTION("last_playerFaction"),
        VISITORPLAYERID("visitor_playerId"),
        VISITORPLAYERSECRET("visitor_playersecret"),
        SHORTDATEFORMAT("short_date_format"),
        LONGDATEFORMAT("long_date_format"),
        NOTIFICATIONS_ON("notifications_on"),
        NOTIFICATIONS_INTERVAL("notification_interval"),
        SERVER_TIMEOUT("SERVER_TIMEOUT"),
        LAYOUT_IMAGE_ON("LAYOUT_IMAGE_ON"),
        LAYOUT_LEVEL_THRESHOLD("LAYOUT_LEVEL_THRESHOLD"),
        VISITOR_DEVICE_ID("VISITOR_DEVICE_ID"),//d3e8b454-fc71-496b-b25e-66a2df4d4717
        LAYOUT_EXPORT_FOLDER("LAYOUT_EXPORT_FOLDER"),
        LAYOUT_FAV_PREF("LAYOUT_FAV_PREF"),
        BOT2_ID("BOT2_ID"),
        BOT2_SECRET("BOT2_SECRET"),
        SCDETAILEXPAND("SCDETAILEXPAND"),
        ARMOURYDETAILEXPAND("ARMOURYDETAILEXPAND"),
        TROOPTRANSPORTEXPAND("TROOPTRANSPORTEXPAND"),
        TROOPHEROEXPAND("TROOPHEROEXPAND"),
        TROOPAIREXPAND("TROOPAIREXPAND"),
        PLAYERDETAILEXPANDHINT("PLAYERDETAILEXPANDHINT"),
        PLAYERDETAILEXPANDHINTCOUNT("PLAYERDETAILEXPANDHINTCOUNT"),
        SWCMANIFESTVERSION("SWCMANIFESTVERSION"),
        SWCCAEVERSION("SWCCAEVERSION"),
        SWCPATCHESBASEURL("SWCPATCHESBASEURL"),
        LOGSWCMESSAGES("LOGSWCMESSAGES"),
        WARDASHDISCLAIMER("WARDASHDISCLAIMER"),
        UNIQUEDEVICEID("UNIQUEDEVICEID"),
        WARROOM_INCLUDESTART("WARROOM_INCLUDESTART"),
        WARROOM_OUTPOSTWAR("WARROOM_OUTPOSTWAR"),
        WARROOM_INCLUDEUPLINKS("WARROOM_INCLUDEUPLINKS"),
        WARROOM_SPLITCLEARS("WARROOM_SPLITCLEARS"),
        WARROOM_INCLUDESCORE("WARROOM_INCLUDESCORE"),
        WARROOM_INCLUDEBASESCORE("WARROOM_INCLUDEBASESCORE");


        private String setting;

        Settings(String setting) {
            this.setting = setting;
        }

        public String toString() {
            return setting;

        }
    }
}
