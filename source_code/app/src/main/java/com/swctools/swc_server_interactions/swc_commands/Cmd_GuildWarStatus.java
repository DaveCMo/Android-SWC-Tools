package com.swctools.swc_server_interactions.swc_commands;

import java.util.UUID;

public class Cmd_GuildWarStatus extends SWC_Command {
    public static final String COMMAND = "guild.war.status";
    private String playerId;
    private String warId;

    public Cmd_GuildWarStatus(int requestId, String playerId, String warId, long time) {
        super(requestId);
        this.playerId = playerId;
        this.warId = warId;
        this.time = time;
    }

    @Override
    protected void setAction() {
        this.action = "guild.war.status";
    }

    @Override
    protected void setCommand() {

        this.token = UUID.randomUUID().toString();
        CmdArgs_GuildWarStatus getArgs = new CmdArgs_GuildWarStatus(this.playerId, this.warId);
        this.args = getArgs;
    }

    @Override
    protected void setCommandName() {
        this.commandName = "guild.war.status";
    }

    private class CmdArgs_GuildWarStatus extends SWC_Args {
        private String playerId;
        private String warId;

        public CmdArgs_GuildWarStatus(String playerId, String warId) {
            super();
            this.playerId = playerId;
            this.warId = warId;
        }

        @Override
        protected void buildArgs() {
            argsObjectBuilder.add("playerId", this.playerId);
            argsObjectBuilder.add("warId", this.warId);
        }
    }
}
