package com.swctools.swc_server_interactions.swc_commands;

import java.util.UUID;

public class Cmd_PlayerContentGet extends SWC_Command {
    private String playerId;

    public Cmd_PlayerContentGet(int requestId, String playerId, long time) {
        super(requestId);
        this.playerId = playerId;
        this.time = time;
    }

    @Override
    protected void setAction() {
        this.action = "player.content.get";
    }

    @Override
    protected void setCommand() {

        this.token = UUID.randomUUID().toString();
        CmdArgs_PlayerContentGet getArgs = new CmdArgs_PlayerContentGet(this.playerId);

        this.args = getArgs;
    }

    @Override
    protected void setCommandName() {
        this.commandName = "Get player content";
    }

    public static class CmdArgs_PlayerContentGet extends SWC_Args {
        private String playerId;
        public CmdArgs_PlayerContentGet(String playerId){
            this.playerId = playerId;
        }
        @Override
        protected void buildArgs() {
            argsObjectBuilder.add("playerId", playerId);
        }
    }
}
