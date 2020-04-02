package com.swctools.swc_server_interactions.swc_commands;

import java.util.UUID;

public class Cmd_GuildGetPublic extends SWC_Command {

    private static final String TAG = "guild.get.public";
    private String guildId;
    private String playerId;

    public Cmd_GuildGetPublic(int requestId, String playerId, String guildId, long time) {
        super(requestId);
        this.playerId = playerId;
        this.guildId = guildId;
        this.time = 0;//DateTimeHelper.swc_requestTimeStamp();
    }

    @Override
    protected void setCommand() {
        this.token = UUID.randomUUID().toString();
        CmdArgs_GuildGetPublic guildGetPublic = new CmdArgs_GuildGetPublic(this.playerId, this.guildId);
        this.args = guildGetPublic;
    }

    @Override
    public String getAction() {
        return "guild.get.public";
    }

    @Override
    protected void setAction() {
        this.action = "guild.get.public";
    }
    @Override
    protected void setCommandName() {
        this.commandName = "Get public squad information";
    }

    public static class CmdArgs_GuildGetPublic extends SWC_Args {
        private static final String TAG = "CmdArgs_GuildGetPublic";
        private String playerId;
        private String guildId;

        public CmdArgs_GuildGetPublic(String playerId, String guildId){
            super();

            this.playerId = playerId;
            this.guildId = guildId;

        }

        @Override
        protected void buildArgs(){
            argsObjectBuilder.add("playerId", this.playerId);
            argsObjectBuilder.add("guildId", this.guildId);
        }


    }
}
