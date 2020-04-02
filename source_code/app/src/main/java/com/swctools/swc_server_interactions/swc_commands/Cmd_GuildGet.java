package com.swctools.swc_server_interactions.swc_commands;

import java.util.UUID;

public class Cmd_GuildGet extends SWC_Command {
    private String playerId;

    public Cmd_GuildGet(int requestId, String playerId, long time) {
        super(requestId);
        this.playerId = playerId;
        this.time = time;
    }

    @Override
    protected void setAction() {
        this.action = "guild.get";

    }

    @Override
    protected void setCommand() {

        this.token = UUID.randomUUID().toString();
        CmdArgs_GuildGet getArgs = new CmdArgs_GuildGet(this.playerId);

        this.args = getArgs;

    }

    @Override
    protected void setCommandName() {
        this.commandName = "Get squad info";
    }

    public static class CmdArgs_GuildGet extends SWC_Args {
        private String playerId;
        public CmdArgs_GuildGet(String playerId){
            this.playerId = playerId;
        }
        @Override
        protected void buildArgs() {
            argsObjectBuilder.add("playerId", this.playerId);

        }

    }
}
