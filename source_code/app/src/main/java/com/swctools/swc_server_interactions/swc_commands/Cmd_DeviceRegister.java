package com.swctools.swc_server_interactions.swc_commands;

import java.util.UUID;

public class Cmd_DeviceRegister extends SWC_Command {
    private String playerId;
    public Cmd_DeviceRegister(int requestId, String playerId, long time) {
        super(requestId);
        this.playerId = playerId;
        this.time = time;
    }

    @Override
    protected void setAction() {
        this.action = "player.device.register";
    }

    @Override
    protected void setCommand() {
        this.token = UUID.randomUUID().toString();
        CmdArgs_DeviceRegister getArgs = new CmdArgs_DeviceRegister(this.playerId);
        this.args = getArgs;
    }
    @Override
    protected void setCommandName() {
        this.commandName = "Register device Id";
    }

    public static class CmdArgs_DeviceRegister extends SWC_Args {
        private String playerId;
        public CmdArgs_DeviceRegister(String playerId){
            super();
            this.playerId = playerId;
        }
        @Override
        protected void buildArgs() {
            argsObjectBuilder.add("playerId", playerId);
            argsObjectBuilder.add("deviceToken", "");
            argsObjectBuilder.add("deviceType", "f");
        }
    }
}
