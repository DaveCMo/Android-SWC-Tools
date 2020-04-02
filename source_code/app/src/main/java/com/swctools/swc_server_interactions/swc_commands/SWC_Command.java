package com.swctools.swc_server_interactions.swc_commands;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;


public abstract class SWC_Command {

    protected String action;
    protected int requestId;
    protected long time;
    protected String token;
    protected SWC_Args args;
    protected JsonObjectBuilder commandObjectBuilder;
    protected String commandName;


    public SWC_Command(int requestId) {
        this.time = time;
        this.requestId = requestId;
        setAction();
        setCommandName();
    }


    protected abstract void setCommandName() ;

    protected abstract void setAction();

    protected abstract void setCommand();

    public String getCommandName() {
        return commandName;
    }

    public void resetTime(long loginTime) {
        if (this.time != 0) {
            this.time = loginTime;
        }
    }

    public void setRequestId(int i) {
        this.requestId = i;
    }

    public String getAction() {
        return action;
    }

    public int getRequestId() {

        return requestId;
    }

    public JsonObject buildCommand() {
        commandObjectBuilder = Json.createObjectBuilder();
        setCommand();
        commandObjectBuilder.add("action", action);
        commandObjectBuilder.add("args", args.args());
        commandObjectBuilder.add("requestId", requestId);
        commandObjectBuilder.add("time", time);
        commandObjectBuilder.add("token", token);
        return commandObjectBuilder.build();

    }

}
