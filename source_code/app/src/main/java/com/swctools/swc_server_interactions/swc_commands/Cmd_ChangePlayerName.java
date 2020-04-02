package com.swctools.swc_server_interactions.swc_commands;

import java.util.UUID;

public class Cmd_ChangePlayerName extends SWC_Command {
    private String playerId;
    private String newName;
    private long cmdTime;
    public Cmd_ChangePlayerName(int requestId, String playerId, String newName, long cmdTime) {
        super(requestId);
        this.playerId = playerId;
        this.newName = newName;
        this.time = cmdTime;
    }

    @Override
    protected void setCommandName() {
        this.commandName = "Change player name command";
    }

    @Override
    protected void setAction() {
        this.action = "player.name.set";
    }

    @Override
    protected void setCommand() {

        this.token = UUID.randomUUID().toString();
        CmdArgs_ChangePlayerName getArgs = new CmdArgs_ChangePlayerName(this.playerId, this.newName);
        this.args = getArgs;
    }

    public static class CmdArgs_ChangePlayerName extends SWC_Args {
        private String playerId;
        private String newName;

        public CmdArgs_ChangePlayerName(String playerId, String newName) {
            super();
            this.playerId = playerId;
            this.newName = newName;
        }

        @Override
        protected void buildArgs() {
            argsObjectBuilder.add("playerId", this.playerId);
            argsObjectBuilder.add("playerName", this.newName);
        }
    }
}
