package com.swctools.swc_server_interactions.swc_commands;

import java.util.UUID;

public class Cmd_Equipment_Activate extends SWC_Command {
    private static final String TAG = "Cmd_Equipment_Activate";
    private String playerId;
    private String equipmentId;

    public Cmd_Equipment_Activate(int requestId, String playerId, String equipmentId) {
        super(requestId);
        this.playerId = playerId;
        this.equipmentId = equipmentId;
    }

    @Override
    protected void setCommandName() {
        this.commandName = TAG;
    }

    @Override
    protected void setAction() {
        this.action = "player.equipment.activate";
    }

    @Override
    protected void setCommand() {
        this.token = UUID.randomUUID().toString();
        this.args = new CmdArgs_Equipment_Activate();

    }

    class CmdArgs_Equipment_Activate extends SWC_Args {
        @Override
        protected void buildArgs() {
            argsObjectBuilder.add("playerId", playerId);
            argsObjectBuilder.add("equipmentId", equipmentId);
        }
    }
}
