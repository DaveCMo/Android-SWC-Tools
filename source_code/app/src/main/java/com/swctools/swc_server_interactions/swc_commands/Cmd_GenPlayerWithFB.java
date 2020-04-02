package com.swctools.swc_server_interactions.swc_commands;

import java.util.Locale;
import java.util.UUID;

public class Cmd_GenPlayerWithFB extends SWC_Command {
    private String facebookId;
    private String facebookAuthToken;
    public Cmd_GenPlayerWithFB(int requestId, String facebookId, String facebookAuthToken) {
        super(requestId);
        this.facebookId = facebookId;
        this.facebookAuthToken= facebookAuthToken;
    }

    @Override
    protected void setAction() {
        this.action = "auth.preauth.generatePlayerWithFacebook";
    }

    @Override
    protected void setCommand() {
        this.time = 0;
        this.token = UUID.randomUUID().toString();
        CmdArgs_GenPlayerWithFB getArgs = new CmdArgs_GenPlayerWithFB(this.facebookId, this.facebookAuthToken);
        this.args = getArgs;
    }

    @Override
    protected void setCommandName() {
        this.commandName = "Generate player with Facebook";
    }

    public static class CmdArgs_GenPlayerWithFB extends SWC_Args {
        private String facebookId;
        private String facebookAuthToken;

        public CmdArgs_GenPlayerWithFB(String facebookId, String facebookAuthToken) {
            super();
            this.facebookId = facebookId;
            this.facebookAuthToken = facebookAuthToken;
        }


        @Override
        protected void buildArgs() {
            argsObjectBuilder.add("locale", Locale.getDefault().toString());
            argsObjectBuilder.add("facebookId", this.facebookId);
            argsObjectBuilder.add("facebookAuthToken", this.facebookAuthToken);
        }
    }
}
