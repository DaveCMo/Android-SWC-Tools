package com.swctools.activity_modules.logs.models;

public class SWCMessageLogModel {
    public String function;
    public String message;
    public String response;
    public int timeStamp;

    public SWCMessageLogModel(String f, String m, String r, int ts) {
        this.function = f;
        this.message = m;
        this.response = r;
        this.timeStamp = ts;
    }

}
