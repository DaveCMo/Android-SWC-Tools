package com.swctools.swc_server_interactions.swc_commands;

import java.util.UUID;

public class Cmd_KeepAlive extends SWC_Command {
    private String playerId;

    public Cmd_KeepAlive(int requestId, String playerId) {
        super(requestId);
        this.playerId = playerId;
    }

    @Override
    protected void setAction() {
        this.action = "player.keepAlive";
    }

    @Override
    protected void setCommand() {
        this.time = 0;
        this.token = UUID.randomUUID().toString();
        CmdArgs_KeepAlive getArgs = new CmdArgs_KeepAlive();
        this.args = getArgs;
    }

    @Override
    protected void setCommandName() {
        this.commandName = "player.keepAlive";
    }

    public class CmdArgs_KeepAlive extends SWC_Args {
        @Override
        protected void buildArgs() {
            argsObjectBuilder.add("playerId", playerId);
        }
    }
}
