package com.swctools.swc_server_interactions;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.swctools.config.AppConfig;
import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;
import com.swctools.common.enums.ApplicationMessageTemplates;
import com.swctools.common.enums.BundleKeys;
import com.swctools.common.helpers.BundleHelper;
import com.swctools.common.helpers.DateTimeHelper;
import com.swctools.swc_server_interactions.authhelper.AuthTokenHelper;
import com.swctools.swc_server_interactions.results.SWCAuthResult;
import com.swctools.swc_server_interactions.results.SWCDefaultResponse;
import com.swctools.swc_server_interactions.results.SWCLoginResponseData;
import com.swctools.swc_server_interactions.services.ManifestUpdateService;
import com.swctools.swc_server_interactions.swc_commands.Cmd_ConfigEndPoints;
import com.swctools.swc_server_interactions.swc_commands.Cmd_DeviceRegister;
import com.swctools.swc_server_interactions.swc_commands.Cmd_GetAuthToken;
import com.swctools.swc_server_interactions.swc_commands.Cmd_Login;
import com.swctools.swc_server_interactions.swc_commands.Cmd_PlayerContentGet;
import com.swctools.util.MethodResult;
import com.swctools.util.StringUtil;

import java.util.Locale;
import java.util.UUID;

import javax.json.JsonObject;

public class PlayerLoginSession {

    private String playerId;
    private String playerSecret;
    private String deviceId;
    private long loginTime;
    private String authKey;
    private boolean isLoggedIn;
    private int requestId = 0;
    private String locale = "en_US";
    private long timeZoneOffset = 1;
    private long serverTimeStamp = 0;
    private long lastRequestTime = 0;
    private MethodResult loginResult;
    private boolean realPlayer = false;


    //    protected String locale = "en_US";
//    protected long timeZoneOffset = 1;
//    public LoginPlayerObject(String playerId, String playerSecret, String deviceId) {
//        this.playerId = playerId;
//        this.playerSecret = playerSecret;
//        this.deviceId = deviceId;
//    }


    public PlayerLoginSession(String playerId, String playerSecret, String deviceId, boolean setLocalTime) {
        this.playerId = playerId;
        this.playerSecret = playerSecret;
        this.deviceId = deviceId;
        if (setLocalTime) {
            this.realPlayer = true;
            this.locale = Locale.getDefault().toString();
            this.timeZoneOffset = DateTimeHelper.getTZOffsetHRs();
        }
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getLocale() {
        return locale;
    }

    public long getTimeZoneOffset() {
        return timeZoneOffset;
    }

    public int getRequestId() {
        return requestId;
    }

    public int newRequestId() {
        requestId++;
        return requestId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public String getPlayerSecret() {
        return playerSecret;
    }

    public long getLoginTime() {
        return loginTime;
    }

    public String getAuthKey() {
        return authKey;
    }

    public String getRequestToken() {
        return AuthTokenHelper.getAuthToken(this.playerId, this.playerSecret);
    }

    public MethodResult getLoginResult() {
        return loginResult;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }


    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    public void setLoggedIn(boolean loggedIn, long loginTime) {
        isLoggedIn = loggedIn;
        this.loginTime = loginTime;
    }

    private void setDefaults() {
        this.isLoggedIn = false;
        this.requestId = 0;
        this.lastRequestTime = 0;
        this.authKey = "";
        this.serverTimeStamp = 0;
        this.loginTime = 0;
    }

    public MethodResult login(boolean ancils, Context mContext) {
        //First set values to default:
        setDefaults();
        //get player info
        if (StringUtil.isStringNotNull(playerId) && StringUtil.isStringNotNull(playerSecret) && StringUtil.isStringNotNull(deviceId)) {
            //then generate an auth key:
            try {
                SWCDefaultResponse authResponse = new SWCMessage("", true, 0, new Cmd_GetAuthToken(newRequestId(), playerId, getRequestToken()), "PlayerLoginSession|Login|authResponse", mContext).getSwcMessageResponse();
                try {
                    SWCAuthResult authResult = new SWCAuthResult(authResponse.getResponseDataByRequestId(getRequestId()));
                    if (authResult.isSuccess()) {
                        setAuthKey(authResult.getAuthKey());
                        requestConfigEndPoints(mContext);

                        SWCDefaultResponse loginResponse = new SWCMessage(getAuthKey(), true, getLoginTime(), new Cmd_Login(newRequestId(), playerId, deviceId, getLocale(), getTimeZoneOffset()), "SWCMessageFunctions|login", mContext).getSwcMessageResponse();
                        SWCLoginResponseData loginResponseData = new SWCLoginResponseData(loginResponse.getResponseDataByRequestId(getRequestId()));
                        if (loginResponseData.isSuccess()) {
                            setLoggedIn(true, loginResponseData.loginTime());
                            if (ancils) {
                                ancillaryLoginCommands(mContext);
                            }
                            if (realPlayer) {
                                fullActualPlayerCommands(mContext);
                            }
                            //Save data to db in case it is needed:
                            String key = String.format(ApplicationMessageTemplates.VISIT_KEY.getemplateString(), BundleKeys.LOGIN_RESPONSE.toString(), playerId);
                            BundleHelper bundleHelper = new BundleHelper(key, loginResponseData.resultData.toString());
                            bundleHelper.commit(mContext);
                            loginResult = new MethodResult(true, this, loginResponseData.resultData.toString());
                        } else {
                            loginResult = new MethodResult(false, loginResponseData.getStatusCodeAndName());
                        }
                    } else {
                        loginResult = new MethodResult(false, authResult.getStatusCodeAndName());
                    }
                } catch (Exception e) {
                    loginResult = new MethodResult(false, e.getMessage());
                }
            } catch (Exception e) {
                loginResult = new MethodResult(false, e.getMessage());
            }

        } else {
            loginResult = new MethodResult(false, "Application Error! No player id/secret id or device id passed into the login request!");
        }

        return loginResult;
    }

    private void fullActualPlayerCommands(Context context) {


     /*   try {
            SWCDefaultResponse swcDefaultResponse = new SWCMessage(authKey, true, loginTime,
                    new Cmd_GetWarParticipant(newRequestId(), playerId, loginTime), "fullActualPlayerCommands", context).getSwcMessageResponse();
            SWCDefaultResponse guildInviteGet = new SWCMessage(authKey, true, loginTime,
                    new Cmd_GuildInviteGet(newRequestId(), playerId, loginTime), "fullActualPlayerCommands", context).getSwcMessageResponse();

        } catch (Exception e) {
            e.printStackTrace();
        }*/


    }

    private void ancillaryLoginCommands(Context mContext) {

        try {
            int playerContentReqId = newRequestId();
            SWCDefaultResponse contentResponse = new SWCMessage(
                    authKey,
                    true,
                    loginTime,
                    new Cmd_PlayerContentGet(playerContentReqId, this.playerId, loginTime),
                    "ancillaryLoginCommands|Cmd_PlayerContentGet",
                    mContext
            ).getSwcMessageResponse();
            processPlayerContent(contentResponse.getResponseDataByRequestId(playerContentReqId), mContext);

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            SWCMessage swcMessage = new SWCMessage(
                    authKey,
                    true,
                    loginTime,
                    new Cmd_DeviceRegister(newRequestId(), this.playerId, loginTime),
                    "ancillaryLoginCommands",
                    mContext);
            swcMessage.getSwcMessageResponse();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void processPlayerContent(JsonObject contentResult, Context mContext) {
        AppConfig appConfig = new AppConfig(mContext);
        int manifestVersionFound = contentResult.getJsonObject("result").getInt("manifestVersion");
        if (manifestVersionFound > appConfig.getKnownSwcManifest()) { // then we have some work to do... gather the data and hand it off to the ManifestUpdateService.

//            appConfig.setKnownSWCManifest(manifestVersionFound); //Update the manifest version
            String baseUrlFound = contentResult.getJsonObject("result").getJsonArray("secureCdnRoots").getString(0);
            if (!baseUrlFound.equalsIgnoreCase(appConfig.getSWCPatchesURL())) {

                appConfig.setSWCPatchesURL(baseUrlFound);
            }

            if (manifestVersionFound != appConfig.getKnownSwcManifest()) {//update the values in config and download the new data
                String manifestPath = contentResult.getJsonObject("result").getString("manifest");
                String manifestUrl = baseUrlFound + manifestPath;
                Intent i = new Intent(mContext, ManifestUpdateService.class);
                i.putExtra(BundleKeys.DIALOG_TITLE.toString(), "Downloading Conflict Data");
                i.putExtra(BundleKeys.MANIFESTURL.toString(), manifestUrl);
                i.putExtra(BundleKeys.PATCHESFILE.toString(), "patches/cae.json");
                i.putExtra(BundleKeys.MANIFESTVERSION.toString(), manifestVersionFound);
                mContext.startService(i);
            }
        }

    }

    private void requestConfigEndPoints(Context mContext) {
        try {
            SWCMessage swcMessage = new SWCMessage(
                    authKey,
                    true,
                    0,
                    new Cmd_ConfigEndPoints(newRequestId()),
                    "requestConfigEndPoints",
                    mContext);
            swcMessage.getSwcMessageResponse();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    protected MethodResult getPlayerFromDb(Context mContext) {

        MethodResult methodResult;
        String[] columns = {
                DatabaseContracts.PlayersTable.DEVICE_ID,
                DatabaseContracts.PlayersTable.PLAYERSECRET
        };
        String whereClause = DatabaseContracts.PlayersTable.PLAYERID + " = ? ";
        String[] whereArgs = {playerId};
        Cursor cursor = null;
        try {

            cursor = DBSQLiteHelper.queryDB(DatabaseContracts.PlayersTable.TABLE_NAME, columns, whereClause, whereArgs, mContext);
            while (cursor.moveToNext()) {
                this.playerSecret = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.PlayersTable.PLAYERSECRET));
                this.deviceId = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.PlayersTable.DEVICE_ID));
            }

            if (!StringUtil.isStringNotNull(deviceId)) {
                deviceId = UUID.randomUUID().toString();
                ContentValues updDeviceID = new ContentValues();
                updDeviceID.put(DatabaseContracts.PlayersTable.DEVICE_ID, deviceId);
                DBSQLiteHelper.updateData(DatabaseContracts.PlayersTable.TABLE_NAME, updDeviceID, DatabaseContracts.PlayersTable.PLAYERID + " = ?", playerId, mContext);
            }

            methodResult = new MethodResult(true, "Setup player");
        } catch (Exception e) {
            methodResult = new MethodResult(false, e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return methodResult;

    }

}
