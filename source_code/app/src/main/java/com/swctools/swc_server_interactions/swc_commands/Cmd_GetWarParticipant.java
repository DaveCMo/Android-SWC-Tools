package com.swctools.swc_server_interactions.swc_commands;

import java.util.UUID;

public class Cmd_GetWarParticipant extends SWC_Command {
    public static final String COMMAND = "guild.war.getParticipant";
    private String playerId;

    public Cmd_GetWarParticipant(int requestId, String playerId, long time) {
        super(requestId);
        this.playerId = playerId;
        this.time = time;
    }

    @Override
    protected void setAction() {
        this.action = "guild.war.getParticipant";
    }

    @Override
    protected void setCommand() {

        this.token = UUID.randomUUID().toString();
        CmdArgs_GetWarParticipant getArgs = new CmdArgs_GetWarParticipant(this.playerId);
        this.args = getArgs;
    }

    @Override
    protected void setCommandName() {
        this.commandName = "Get war participant";
    }

    public static class CmdArgs_GetWarParticipant extends SWC_Args {
        private String playerId;

        public CmdArgs_GetWarParticipant(String playerId) {
            this.playerId = playerId;
        }

        @Override
        protected void buildArgs() {
            argsObjectBuilder.add("playerId", this.playerId);
        }
    }
}
