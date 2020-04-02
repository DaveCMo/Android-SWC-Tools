package com.swctools.swc_server_interactions.swc_commands;

import java.util.HashMap;
import java.util.UUID;

public class Cmd_DeployableRemove extends SWC_Command {
    private static final String TAG = "Cmd_BuildingCancel";
    private String playerId;
    private HashMap<String, Integer> deployablesToRemove;

    public Cmd_DeployableRemove(int requestId, String playerId, HashMap<String, Integer> deployablesToRemove) {
        super(requestId);
        this.playerId = playerId;
        this.deployablesToRemove = deployablesToRemove;
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
        this.args = new CmdArgs_DeployableRemove();


    }

    class CmdArgs_DeployableRemove extends SWC_Args {
        @Override
        protected void buildArgs() {
            argsObjectBuilder.add("playerId", playerId);


        }
    }
}
