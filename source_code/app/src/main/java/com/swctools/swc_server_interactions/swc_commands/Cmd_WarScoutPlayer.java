package com.swctools.swc_server_interactions.swc_commands;

import java.util.UUID;


public class Cmd_WarScoutPlayer extends SWC_Command {
    String playerId;
    String participantId;
    long cs;
    long credits;
    long materials;
    long contraband;
    long crystals;

    public Cmd_WarScoutPlayer(int requestId, String playerId, String participantId, long cs, long credits, long materials, long contraband, long crystals) {
        super(requestId);
        this.playerId = playerId;
        this.participantId = participantId;
        this.cs = cs;
        this.credits = credits;
        this.materials = materials;
        this.contraband = contraband;
        this.crystals = crystals;
    }

    public Cmd_WarScoutPlayer(int requestId, String playerId, String participantId, long time) {
        super(requestId);
        this.playerId = playerId;
        this.participantId = participantId;
        this.time = time;
    }

    @Override
    protected void setCommandName() {
        this.commandName = "Get War Player Details";
    }

    @Override
    protected void setAction() {
        this.action = "guild.war.scoutPlayer";

    }

    @Override
    protected void setCommand() {

        this.token = UUID.randomUUID().toString();
        CmdArgs_ScoutWarPlayer getArgs = new CmdArgs_ScoutWarPlayer(this.playerId, this.participantId);

        this.args = getArgs;

    }

    public static class CmdArgs_ScoutWarPlayer extends SWC_Args {

        String playerId;
        String participantId;
        long cs;
        long credits;
        long materials;
        long contraband;
        long crystals;

        public CmdArgs_ScoutWarPlayer(String playerId, String participantId, long cs, long credits, long materials, long contraband, long crystals) {
            super();
            this.playerId = playerId;
            this.participantId = participantId;
            this.cs = cs;
            this.credits = credits;
            this.materials = materials;
            this.contraband = contraband;
            this.crystals = crystals;

        }

        public CmdArgs_ScoutWarPlayer(String playerId, String participantId) {
            super();
            this.playerId = playerId;
            this.participantId = participantId;


        }

        @Override
        protected void buildArgs() {
            argsObjectBuilder.add("playerId", this.playerId);
    //		argsObjectBuilder.add("cs", this.cs);
    //		argsObjectBuilder.add("_credits", this.credits);
    //		argsObjectBuilder.add("_materials", this.materials);
    //		argsObjectBuilder.add("_contraband", this.contraband);
    //		argsObjectBuilder.add("_crystals", this.crystals);
            argsObjectBuilder.add("participantId", this.participantId);

        }

    }
}
