package com.swctools.swc_server_interactions.legacy;

import com.swctools.common.enums.ServerConstants;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;

public class DisneySingleCommandResponse {
    private static final String TAG = "JSONResponse";
    public long serverTimestamp;
    public ResponseData data;
    public String response;
    public Exception mException;


    public DisneySingleCommandResponse(String response) {

        try {
            this.response = response;
            JsonObject responseJsonObj = Json.createReader(new StringReader(response)).readObject();
            data = new ResponseData(responseJsonObj);
            this.serverTimestamp = responseJsonObj.getInt("serverTimestamp");
        } catch (Exception e) {
            mException = e;
        }
    }

    public DisneySingleCommandResponse(Exception e) {
        mException = e;
        response = e.getMessage();
    }



    public class ResponseData {
        public int status;
        public String status_code;
        public JsonArray data;

        public ResponseData(Exception e){
            setStatusCode(-1);

        }
        public ResponseData(JsonObject responseJsonObj) {
            data = responseJsonObj.getJsonArray("data");
            status = Json.createReader(new StringReader(this.data.get(0).toString())).readObject().getInt("status");
            setStatusCode(status);
        }

        private void setStatusCode(int status) {
            this.status_code = "UNKNOWN_STATUS";
            for (ServerConstants serverconstant : ServerConstants.values()) {
                if (serverconstant.toInt() == status) {
                    this.status_code = serverconstant.toString();
                    break;
                }
            }
        }

        public boolean isSuccess() {
            try {
                if(mException==null){
                    if (status_code.equals(ServerConstants.RECEIPT_STATUS_COMPLETE.toString()) || status_code.equals(ServerConstants.SUCCESS.toString())) {
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

        public boolean isLoginIssue() {
            if (status == ServerConstants.LOGIN_TIME_MISMATCH.toInt() || status == ServerConstants.COMMAND_TIMESTAMP_ERROR.toInt() || status == ServerConstants.COMMAND_TIMESTAMP_ERROR2.toInt()) {
                return true;
            } else {
                return false;
            }
        }

        public JsonObject getResultByRequestId(int requestId) throws Exception {
            //iterate over result array to find the object matching the request id:
            for (int i = 0; i < data.size(); i++) {
                JsonObject dataArrayItem = data.getJsonObject(i);
                int objRequestId = dataArrayItem.getInt("resultId");
                if(objRequestId == requestId){
                    return dataArrayItem;
                }
            }
            throw new Exception("Could not find result object in response JSON!");

        }
        public JsonObject result() {
            return data.getJsonObject(0).getJsonObject("result");
        }

        public JsonObject messages() {
            return data.getJsonObject(0).getJsonObject("messages");
        }

        public String resultStr() {
            return Json.createReader(new StringReader(this.data.get(0).toString())).readObject().getString("result");
        }
    }


}
