package com.swctools.base;

import com.swctools.util.MethodResult;

public interface PlayerServiceCallBackInterface {
    String LOGIN = "LOGIN";
    String GETCONFLICTRANKS = "GETCONFLICTRANKS";
    String CHANGENAME = "CHANGENAME";
    String GETWARSTATUS = "GETWARSTATUS";
    String UPDATELAYOUT = "UPDATELAYOUT";
    String VISITPVP = "VISITPVP";
    String ADDPLAYER = "ADDPLAYER";
    String GETGUILD = "GETGUILD";
    String UPGRADEEQUIPMENT = "UPGRADEEQUIPMENT";
    String REPAIRDROID = "REPAIRDROID";
    String REQUEST_WAR = "REQUEST_WAR";
    String REQUEST_PVP= "REQUEST_SC";


    interface PlayerBaseSelection {

        String SAVEPVP = "SAVEPVP";
        String UPDATEPVP = "UPDATEPVP";
        String UPDATEWAR = "UPDATEWAR";
        String UPDATEWAROVERRIDE = "UPDATEWAROVERRIDE";
        String SAVEWAR = "SAVEWAR";

    }

    interface ProgressCallback {

        String gettingToken = "Generating authorisation token";
        String gettingAuthKey = "Getting authorisation key from SWC Servers";

        String loggingIn = "Sending login request to SWC Servers...";
        String sendingCommand = "Sending command to SWC Servers";
        String matchingBuildingsInLayout = "Matching your buildings to your selected layout....";
        String processingServerResponse = "Processing response from SWC Servers";
        String GETTINGPVPLAYOUT = "Getting pvp layout...";
    }


    void playerServiceResult(String command, MethodResult methodResult);

    void publishProgress(String msg);

    void publishProgress(String msg, int progress, int max);

    void setDownloading();

    void finishDownloading();

}
