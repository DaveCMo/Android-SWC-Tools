package com.swctools.swc_server_interactions.swc_commands;

import java.util.UUID;

public class Cmd_BuildingUpgrade extends SWC_Command {
    private static final String TAG = "Cmd_BuildingUpgrade";
    private String playerId, buildingId;
    private int _credits, _materials, _contraband, _crystals;

    public Cmd_BuildingUpgrade(int requestId, String playerId, String buildingId,long time){//}, int _credits, int _materials, int _contraband, int _crystals, long time) {
        super(requestId);

        this.playerId = playerId;
        this.buildingId = buildingId;
        this._credits = _credits;
        this._materials = _materials;
        this._contraband = _contraband;
        this._crystals = _crystals;
        this.time = time;
    }

    @Override
    protected void setCommandName() {
        this.commandName = TAG;
    }

    @Override
    protected void setAction() {
        this.action = "player.building.upgrade";
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
//            argsObjectBuilder.add("_credits", _credits);
//            argsObjectBuilder.add("_materials", _materials);
//            argsObjectBuilder.add("_contraband", _contraband);
//            argsObjectBuilder.add("_crystals", _crystals);
        }
    }
}
