package com.swctools.swc_server_interactions.swc_commands;


import java.util.UUID;


public class Cmd_TournamentGetRank extends SWC_Command {
    private String playerId;
    private String planetId;

    public Cmd_TournamentGetRank(int requestId, String playerId, String planetId) {
        super(requestId);
        this.playerId = playerId;
        this.planetId = planetId;
    }

    @Override
    protected void setAction() {
        this.action = "player.leaderboard.tournament.getRank";
    }

    @Override
    protected void setCommand() {
        this.time = 0;// DateTimeHelper.swc_requestTimeStamp();
        this.token = UUID.randomUUID().toString();
        CmdArgs_TournamentGetRanks getArgs = new CmdArgs_TournamentGetRanks(this.playerId, this.planetId);

        this.args = getArgs;

    }

    @Override
    protected void setCommandName() {
        this.commandName = "Get player rank";
    }


    public static class CmdArgs_TournamentGetRanks extends SWC_Args {

        private String playerId;
        private String planetId;

        public CmdArgs_TournamentGetRanks(String playerId, String planetId) {
            super();
            this.playerId = playerId;
            this.planetId = planetId;
        }

        @Override
        protected void buildArgs() {
            argsObjectBuilder.add("playerId", playerId);
            argsObjectBuilder.add("planetId", planetId);
        }

    }
}
