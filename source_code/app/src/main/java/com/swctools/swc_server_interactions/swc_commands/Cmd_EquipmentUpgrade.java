package com.swctools.swc_server_interactions.swc_commands;

import java.util.UUID;

public class Cmd_EquipmentUpgrade extends SWC_Command {
    private static final String TAG = "Cmd_EquipmentUpgrade";


    private String equipmentUid, playerId, buildingId;
    private int _credits, _materials, _contraband, _crystals;

    public Cmd_EquipmentUpgrade(int requestId, String equipmentUid, String playerId, String buildingId) {
        super(requestId);
        this.equipmentUid = equipmentUid;
        this.playerId = playerId;
        this.buildingId = buildingId;
//        this._credits = _credits;
//        this._materials = _materials;
//        this._contraband = _contraband;
//        this._crystals = _crystals;
    }

    @Override
    protected void setCommandName() {
        this.commandName = TAG;
    }

    @Override
    protected void setAction() {
        this.action = "player.equipment.upgrade.start";
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
            argsObjectBuilder.add("equipmentUid", equipmentUid);
            argsObjectBuilder.add("buildingId", buildingId);
//            argsObjectBuilder.add("_credits", _credits);
//            argsObjectBuilder.add("_materials", _materials);
//            argsObjectBuilder.add("_contraband", _contraband);
//            argsObjectBuilder.add("_crystals", _crystals);
        }
    }
}
