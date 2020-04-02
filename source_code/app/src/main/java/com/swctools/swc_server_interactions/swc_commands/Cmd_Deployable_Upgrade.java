package com.swctools.swc_server_interactions.swc_commands;

import java.util.UUID;

public class Cmd_Deployable_Upgrade extends SWC_Command {
    private static final String TAG = "Cmd_EquipmentUpgrade";


    private String troopUid, playerId, buildingId;
    private int _credits, _materials, _contraband, _crystals;

    public Cmd_Deployable_Upgrade(int requestId, String troopUid, String playerId, String buildingId) {
        super(requestId);
        this.troopUid = troopUid;
        this.playerId = playerId;
        this.buildingId = buildingId;
    }

    @Override
    protected void setCommandName() {
        this.commandName = TAG;
    }

    @Override
    protected void setAction() {
        this.action = "player.deployable.upgrade.start";
    }

    @Override
    protected void setCommand() {
        this.token = UUID.randomUUID().toString();
        this.args = new CmdArgs_Equipment_Upgrade();

    }

    class CmdArgs_Equipment_Upgrade extends SWC_Args {
        @Override
        protected void buildArgs() {
            argsObjectBuilder.add("playerId", playerId);
            argsObjectBuilder.add("troopUid", troopUid);
            argsObjectBuilder.add("buildingId", buildingId);
//            argsObjectBuilder.add("_credits", _credits);
//            argsObjectBuilder.add("_materials", _materials);
//            argsObjectBuilder.add("_contraband", _contraband);
//            argsObjectBuilder.add("_crystals", _crystals);
        }
    }
}
