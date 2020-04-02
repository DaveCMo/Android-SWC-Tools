package com.swctools.swc_server_interactions.swc_commands;

import java.util.UUID;

public class Cmd_GuildWarTroopsRequest extends SWC_Command {
    private static final String TAG = "Cmd_GuildWarTroopsReque";


    private String playerId;
    private boolean payToSkip;
    private String message;


    public Cmd_GuildWarTroopsRequest(int requestId, String playerId, boolean payToSkip, String message) {
        super(requestId);
        this.playerId = playerId;
        this.payToSkip = payToSkip;
        this.message = message;
    }

    @Override
    protected void setCommandName() {
        this.commandName = TAG;
    }

    @Override
    protected void setAction() {
        this.action = "guild.war.troops.request";

    }

    @Override
    protected void setCommand() {
        this.token = UUID.randomUUID().toString();
        this.args = new CmdArgs_GuildTroopsRequest();
    }

    class CmdArgs_GuildTroopsRequest extends SWC_Args {

        @Override
        protected void buildArgs() {
            String msg = message.replace(" ", "+");
            argsObjectBuilder.add("playerId", playerId);
            argsObjectBuilder.add("payToSkip", payToSkip);
            argsObjectBuilder.add("message", msg);

        }
    }

}
