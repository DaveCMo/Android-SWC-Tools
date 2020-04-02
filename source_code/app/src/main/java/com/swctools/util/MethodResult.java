package com.swctools.util;

import java.util.ArrayList;
import java.util.List;

public class MethodResult {
    private static final String TAG = "METHODRESULT";
    //dirty class to pass around messages from methods
    public boolean success;
    private Exception mException;
    private List<String> message = new ArrayList<>();
    private Object resultObject;

    public MethodResult(boolean suc, String... messages) {

        this.success = suc;
        this.message = new ArrayList<>();
        for (int i = 0; i < messages.length; i++) {
            this.message.add(messages[i]);
        }

    }

    public MethodResult(boolean suc, Object obj, String... messages) {

        this.success = suc;
        message = new ArrayList<>();
        for (int i = 0; i < messages.length; i++) {
            this.message.add(messages[i]);
        }
        this.resultObject = obj;

    }

    public MethodResult(boolean suc, Object obj, ArrayList<String> messages) {
        this.success = suc;
        this.resultObject = obj;
        this.message = messages;
    }

    public MethodResult(boolean suc, Exception e) {
        this.success = suc;
        this.mException = e;
    }

    public Object getResultObject() {
        return resultObject;
    }

    public void setResultObject(Object resultObject) {
        this.resultObject = resultObject;
    }

    public void addMessage(String msg) {
        //Accept a message from the code referencing and using this method object to then return.
        if (this.message == null) {//Check this.message array has been insantiated, if not do so.
            this.message = new ArrayList<>();
        }
        this.message.add(msg);
    }

    public Exception getmException() {
        return mException;
    }

    public String getMessage() {
        StringBuilder stringBuilder = new StringBuilder();
        if (message.size() == 1) {
            return message.get(0);
        } else {
            for (String s : message) {
                if (StringUtil.isStringNotNull(s)) {
                    stringBuilder.append(s + "\n");
                }
            }
//            for (int i = 0; i < message.size(); i++) {
//                if (StringUtil.isStringNotNull(message.get(i))) {
//                    stringBuilder.append(message.get(i) + "\n");
//                }
//            }
            return stringBuilder.toString();
        }
    }

    public List<String> messageList() {
        return this.message;
    }

//    public void addMessage(String newMessage){
//        if(message == null){
//            message = new ArrayList<>();
//        }
//        message.add(newMessage + "\n");
//    }
}
