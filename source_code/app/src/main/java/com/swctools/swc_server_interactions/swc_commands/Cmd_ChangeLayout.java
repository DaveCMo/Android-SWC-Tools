package com.swctools.swc_server_interactions.swc_commands;

import com.swctools.common.enums.PlayerBase;

import java.util.UUID;

import javax.json.JsonObject;

public class Cmd_ChangeLayout extends SWC_Command {
    private String playerId;
    private JsonObject layoutPositions;
    private PlayerBase base;

    public Cmd_ChangeLayout(int requestId, String playerId, JsonObject layoutPositions, PlayerBase base) {
        super(requestId);
        this.playerId = playerId;
        this.layoutPositions = layoutPositions;
        this.time = 0;
        this.base = base;
    }

    @Override
    protected void setCommandName() {
        this.commandName = "Cmd_ChangeLayout";
    }

    @Override
    protected void setAction() {

    }

    @Override
    protected void setCommand() {
        switch (this.base) {
            case WAR:
                this.action = "guild.war.base.save";
                break;
            default:
                this.action = "player.building.multimove";
        }
        this.token = UUID.randomUUID().toString();
        CmdArgs_BuildingsMultimove getArgs = new CmdArgs_BuildingsMultimove(this.playerId, this.layoutPositions);
        this.args = getArgs;
    }

    public static class CmdArgs_BuildingsMultimove extends SWC_Args {
        private JsonObject layoutPositions;
        private String playerId;

        public CmdArgs_BuildingsMultimove(String pid, JsonObject layoutPositions) {
            super();
            this.playerId = pid;
            this.layoutPositions = layoutPositions;

        }

        @Override
        public JsonObject args() {
            return super.args();
        }

        @Override
        protected void buildArgs() {
            argsObjectBuilder.add("playerId", playerId);
            argsObjectBuilder.add("positions", layoutPositions);
        }
    }
}
