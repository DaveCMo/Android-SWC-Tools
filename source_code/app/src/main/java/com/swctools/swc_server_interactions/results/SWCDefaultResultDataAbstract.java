package com.swctools.swc_server_interactions.results;

import android.util.Log;

import com.swctools.common.enums.ServerConstants;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonObject;

public abstract class SWCDefaultResultDataAbstract {
    private static final String TAG = "ABSTRACTDEFAULTDATA";
    public int requestId;
    public int status;
    public JsonObject messages;
    public Exception mException;
    public String status_code = "";
    public String status_code_name = "";
    public JsonObject resultData;


    public SWCDefaultResultDataAbstract() {
    }

    public SWCDefaultResultDataAbstract(String dataObjString) {
        JsonObject responseJsonObj = Json.createReader(new StringReader(dataObjString)).readObject();
        resultData = responseJsonObj;
        setDataObj();
//        setResult();

    }

    public SWCDefaultResultDataAbstract(JsonObject dataObject) {
        this.resultData = dataObject;
        setDataObj();
        setResult();
    }

    abstract void setResult();

    protected void setDataObj() {
        try {
            requestId = this.resultData.getInt("requestId");
            status = this.resultData.getInt("status");
            setStatusCode(this.status);
            messages = this.resultData.getJsonObject("messages");
        } catch (Exception e) {
            e.printStackTrace();
            mException = e;
        }
    }

    private void setStatusCode(int status) {
        status_code = "UNKNOWN_STATUS";
        status_code_name = "";
        for (ServerConstants serverconstant : ServerConstants.values()) {
            if (serverconstant.toInt() == status) {
                status_code = serverconstant.toString();
                status_code_name = serverconstant.name();
                break;
            }
        }
    }

    public String getStatusCodeAndName() {
        return "Server returned: " + status_code_name;
    }

    public boolean isSuccess() {
        try {
            if (mException == null) {

                if (ServerConstants.RECEIPT_STATUS_COMPLETE.toString().equalsIgnoreCase(status_code)|| ServerConstants.SUCCESS.toString().equalsIgnoreCase(status_code)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isLogInIssue() {
        try {
            if (mException == null) {
                if (status == ServerConstants.STATUS_AUTHENTICATION_FAILED.toInt() ||
                        status == ServerConstants.STATUS_AUTHORIZATION_FAILED.toInt() ||
                        status == ServerConstants.LOGIN_TIME_MISMATCH.toInt()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

}
