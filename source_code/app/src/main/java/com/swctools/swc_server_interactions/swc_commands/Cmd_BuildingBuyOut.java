package com.swctools.swc_server_interactions.swc_commands;

import java.util.UUID;

public class Cmd_BuildingBuyOut extends SWC_Command {
    private static final String TAG = "Cmd_BuildingBuyOut";
    private String playerId, buildingId;

    public Cmd_BuildingBuyOut(int requestId, String playerId, String buildingId) {
        super(requestId);
        this.playerId = playerId;
        this.buildingId = buildingId;

    }

    @Override
    protected void setCommandName() {
        this.commandName = TAG;
    }

    @Override
    protected void setAction() {
        this.action = "player.building.buyout";
    }

    @Override
    protected void setCommand() {
        this.token = UUID.randomUUID().toString();
        this.args = new CmdArgs_BuildingUpgrade();


    }

    class CmdArgs_BuildingUpgrade extends SWC_Args {
        @Override
        protected void buildArgs() {
            argsObjectBuilder.add("playerId", playerId);
            argsObjectBuilder.add("instanceId", buildingId);
        }
    }
}
