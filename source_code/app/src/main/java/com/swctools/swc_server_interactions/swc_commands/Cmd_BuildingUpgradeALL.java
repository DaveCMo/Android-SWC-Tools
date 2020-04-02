package com.swctools.swc_server_interactions.swc_commands;

import java.util.UUID;

public class Cmd_BuildingUpgradeALL extends SWC_Command {
    private static final String TAG = "Cmd_BuildingUpgradeALL";
    private String playerId, buildingUid;
    private int _credits, _materials, _contraband, _crystals;

    public Cmd_BuildingUpgradeALL(int requestId, String playerId, String buildingUid, long time){//}, int _credits, int _materials, int _contraband, int _crystals, long time) {
        super(requestId);

        this.playerId = playerId;
        this.buildingUid = buildingUid;
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
        this.action = "player.building.upgradeAll";
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
            argsObjectBuilder.add("buildingUid", buildingUid);
//            argsObjectBuilder.add("_credits", _credits);
//            argsObjectBuilder.add("_materials", _materials);
//            argsObjectBuilder.add("_contraband", _contraband);
//            argsObjectBuilder.add("_crystals", _crystals);
        }
    }
}
