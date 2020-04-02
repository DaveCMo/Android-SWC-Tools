package com.swctools.activity_modules.war_sign_up.interfaces;

import android.net.NetworkInfo;

import com.swctools.util.MethodResult;

public interface WarSignupInterface {
    interface ProgressMessage {
        String SEARCHING = "Searching...";

    }

    void finishDownloading();
    void publishProgress(String message);
    void receiveSearchResults(MethodResult result);

    void receiveGuildData(MethodResult result);

    NetworkInfo getActiveNetworkInfo();
}