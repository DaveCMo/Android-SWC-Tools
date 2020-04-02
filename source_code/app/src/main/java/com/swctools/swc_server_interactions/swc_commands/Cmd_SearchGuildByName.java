package com.swctools.swc_server_interactions.swc_commands;

import java.util.UUID;

public class Cmd_SearchGuildByName extends SWC_Command {
    private String playerId;
    private String searchTerm;
    public Cmd_SearchGuildByName(int requestId, String playerId, String searchTerm) {
        super(requestId);
        this.playerId = playerId;
        this.searchTerm = searchTerm;
    }

    @Override
    protected void setAction() {
        this.action = "guild.search.byName";
    }

    @Override
    protected void setCommand() {
        this.time = 0;
        this.token = UUID.randomUUID().toString();
        CmdArgs_SearchGuildByName getArgs = new CmdArgs_SearchGuildByName(this.playerId, this.searchTerm);

        this.args = getArgs;
    }

    @Override
    protected void setCommandName() {
        this.commandName = "Search for guild by name";
    }


    public static class CmdArgs_SearchGuildByName extends SWC_Args {
        private String playerId;
        private String searchTerm;

        public CmdArgs_SearchGuildByName(String playerId, String searchTerm) {
            super();
            this.playerId = playerId;
            this.searchTerm = searchTerm;
        }

        @Override
        protected void buildArgs() {
            argsObjectBuilder.add("playerId", this.playerId);
            argsObjectBuilder.add("searchTerm", this.searchTerm);
        }
    }
}
