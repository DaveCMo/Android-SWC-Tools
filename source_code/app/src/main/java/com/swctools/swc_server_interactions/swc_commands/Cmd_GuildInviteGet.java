package com.swctools.swc_server_interactions.swc_commands;

import java.util.UUID;

public class Cmd_GuildInviteGet extends SWC_Command {
    private String playerId;

    public Cmd_GuildInviteGet(int requestId, String playerId, long time) {
        super(requestId);
        this.playerId = playerId;
        this.time = time;
    }

    @Override
    protected void setAction() {
        this.action = "guild.invite.get";
    }

    @Override
    protected void setCommand() {

        this.token = UUID.randomUUID().toString();
        CmdArgs_GuildInviteGet getArgs = new CmdArgs_GuildInviteGet(this.playerId);
        this.args = getArgs;
    }
    @Override
    protected void setCommandName() {
        this.commandName = "Get Squad Invites";
    }

    public static class CmdArgs_GuildInviteGet extends SWC_Args {
        private String playerId;

        public CmdArgs_GuildInviteGet(String playerId) {
            super();
            this.playerId = playerId;
        }

        @Override
        protected void buildArgs() {
            argsObjectBuilder.add("playerId", this.playerId);
        }
    }
}
