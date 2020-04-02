package com.swctools.swc_server_interactions.swc_commands;

import java.util.UUID;

public class Cmd_ConfigEndPoints extends SWC_Command {
    public Cmd_ConfigEndPoints(int requestId) {
        super(requestId);
    }

    @Override
    protected void setAction() {
        this.action = "config.endpoints.get";
    }

    @Override
    protected void setCommand() {
        this.time = 0;
        this.token = UUID.randomUUID().toString();
        CmdArgs_ConfigEndPoints getArgs = new CmdArgs_ConfigEndPoints();
        this.args = getArgs;
    }
    @Override
    protected void setCommandName() {
        this.commandName = "Get Config End Points.";
    }

    public static class CmdArgs_ConfigEndPoints extends SWC_Args {
        @Override
        protected void buildArgs() {
            //no args here at the moment! The request doesn't include any
        }
    }
}
