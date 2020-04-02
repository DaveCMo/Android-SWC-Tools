package com.swctools.swc_server_interactions.swc_commands;


import java.util.UUID;


public class Cmd_NeighborVisit extends SWC_Command {
	private String playerId;
	private String neighborId;
	public Cmd_NeighborVisit(int requestId, String playerId, String neighborId) {
		super(requestId);
		this.playerId = playerId;
		this.neighborId = neighborId;
	}

	@Override
	protected void setAction() {
		this.action = "player.neighbor.visit";

	}

	@Override
	protected void setCommand() {
		this.time = 0;//DateTimeHelper.swc_requestTimeStamp();
		this.token = UUID.randomUUID().toString();
		CmdArgs_NeighborVisit getArgs = new CmdArgs_NeighborVisit(this.playerId, this.neighborId);
		
		this.args = getArgs;

	}

	@Override
	protected void setCommandName() {
		this.commandName = "Get player data";
	}


    public static class CmdArgs_NeighborVisit extends SWC_Args {

        private String playerId;
        private String neighborId;
        public CmdArgs_NeighborVisit(String playerId, String neighborId) {
            super();
            this.playerId = playerId;
            this.neighborId = neighborId;

        }
        @Override
        protected void buildArgs() {
            argsObjectBuilder.add("playerId", playerId);
            argsObjectBuilder.add("neighborId", neighborId);
        }

    }
}
