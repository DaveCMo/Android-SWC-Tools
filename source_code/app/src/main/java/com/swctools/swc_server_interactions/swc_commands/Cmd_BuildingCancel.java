package com.swctools.swc_server_interactions.swc_commands;

import java.util.UUID;

public class Cmd_BuildingCancel extends SWC_Command {
    private static final String TAG = "Cmd_BuildingCancel";
    private String playerId, buildingId;

    public Cmd_BuildingCancel(int requestId, String playerId, String buildingId){//}, int _credits, int _materials, int _contraband, int _crystals, long time) {
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
        this.action = "player.building.cancel";
    }

    @Override
    protected void setCommand() {
        this.token = UUID.randomUUID().toString();
        this.args = new CmdArgs_BuildingCancel();


    }

    class CmdArgs_BuildingCancel extends SWC_Args {
        @Override
        protected void buildArgs() {
            argsObjectBuilder.add("playerId", playerId);
            argsObjectBuilder.add("instanceId", buildingId);
//            argsObjectBuilder.add("_credits", _credits);
//            argsObjectBuilder.add("_materials", _materials);
//            argsObjectBuilder.add("_contraband", _contraband);
//            argsObjectBuilder.add("_crystals", _crystals);
        }
    }
}
