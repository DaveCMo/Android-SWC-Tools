package com.swctools.swc_server_interactions.swc_commands;



import java.util.UUID;

public class Cmd_GetAuthToken extends SWC_Command {
	

	private String playerId;
	private String requestToken;
	public Cmd_GetAuthToken(int requestId, String playerId, String requestToken) {
		super(requestId);	
		this.playerId = playerId;
		this.requestToken = requestToken;
	}

	@Override
	protected void setCommand() {
		
		this.time = 0;
		this.token = UUID.randomUUID().toString();
		CmdArgs_getAuthToken getArgs = new CmdArgs_getAuthToken(this.playerId, this.requestToken);
		
		this.args = getArgs;

	}


	@Override
	protected void setAction() {
		this.action = "auth.getAuthToken";	
		
	}

	@Override
	protected void setCommandName() {
		this.commandName = "Get auth token";
	}

    public static class CmdArgs_getAuthToken extends SWC_Args {
        private static final String TAG ="CmdArgs_getAuthToken";
        private String playerId;
        private String requestToken;

        public  CmdArgs_getAuthToken(String playerId, String requestToken) {
            super();
            this.playerId =playerId;
            this.requestToken = requestToken;

        }

        @Override
        protected void buildArgs() {
            argsObjectBuilder.add("playerId", this.playerId);
            argsObjectBuilder.add("requestToken", this.requestToken);

        }

    }
}
