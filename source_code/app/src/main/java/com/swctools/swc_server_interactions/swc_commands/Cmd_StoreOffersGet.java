package com.swctools.swc_server_interactions.swc_commands;

import java.util.UUID;

public class Cmd_StoreOffersGet extends SWC_Command {
    private String playerId;

    public Cmd_StoreOffersGet(int requestId, String playerId) {
        super(requestId);
        this.playerId = playerId;
    }

    @Override
    protected void setAction() {
        this.action = "player.store.offers.get";
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
        this.commandName = "player.store.offers.get";
    }

    public class CmdArgs_ConfigEndPoints extends SWC_Args {
        @Override
        protected void buildArgs() {
            argsObjectBuilder.add("playerId", playerId);
        }
    }
}
