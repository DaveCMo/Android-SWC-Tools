package com.swctools.swc_server_interactions.swc_commands;

import java.util.UUID;

public class Cmd_DeployableTrain extends SWC_Command {
    private static final String TAG = "Cmd_DeployableTrain";
    private String playerId, constructor;
    private String unitTypeId;
    private int quantity;

    public Cmd_DeployableTrain(int requestId, String playerId, String constructor, String unitTypeId, int quantity) {
        super(requestId);
        this.playerId = playerId;
        this.constructor = constructor;
        this.unitTypeId = unitTypeId;
        this.quantity = quantity;
    }

    @Override
    protected void setCommandName() {
        this.commandName = TAG;
    }

    @Override
    protected void setAction() {
        this.action = "player.deployable.train";
    }

    @Override
    protected void setCommand() {
        this.token = UUID.randomUUID().toString();
        this.args = new CmdArgs_DeployableTrain();


    }

    class CmdArgs_DeployableTrain extends SWC_Args {
        @Override
        protected void buildArgs() {
            argsObjectBuilder.add("playerId", playerId);
            argsObjectBuilder.add("constructor", constructor);
            argsObjectBuilder.add("unitTypeId", unitTypeId);
            argsObjectBuilder.add("quantity", quantity);
//            argsObjectBuilder.add("_credits", _credits);
//            argsObjectBuilder.add("_materials", _materials);
//            argsObjectBuilder.add("_contraband", _contraband);
//            argsObjectBuilder.add("_crystals", _crystals);
        }
    }
}
