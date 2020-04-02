package com.swctools.swc_server_interactions.swc_commands;

import java.util.UUID;

public class Cmd_Login extends SWC_Command {
	private String playerId;
	private String deviceId;
	private String locale;
	private long tzOffset;

	public Cmd_Login(int requestId, String playerId, String deviceId, String locale, long tzOffset){
		super(requestId);
		this.playerId = playerId;
		this.deviceId = deviceId;
		this.locale = locale;
		this.tzOffset = tzOffset;
	}
	@Override
	protected void setAction() {
		this.action = "player.login";
	}

	@Override
	protected void setCommand() {
		this.time = 0;
		this.token = UUID.randomUUID().toString();
		CmdArgs_Login getArgs = new CmdArgs_Login(this.playerId, deviceId, locale, tzOffset);
		
		this.args = getArgs;
	}

	@Override
	public String getAction() {
		// TODO Auto-generated method stub
		return this.action;
	}
	@Override
	protected void setCommandName() {
		this.commandName = "Login command";
	}

    public static class CmdArgs_Login extends SWC_Args {
        private String playerId;
        private String deviceId;
        private String locale;
        private long tzOffset;
        public CmdArgs_Login(String playerId, String deviceId, String locale, long tzOffset ) {
            super();
            this.playerId = playerId;
            this.deviceId = deviceId;
            this.locale = locale;
            this.tzOffset = tzOffset;

        }
        @Override
        protected void buildArgs() {

            argsObjectBuilder.add("playerId", this.playerId);

            argsObjectBuilder.add("locale", locale);
            argsObjectBuilder.add("deviceToken", "");
            argsObjectBuilder.add("deviceType", "f");
            argsObjectBuilder.add("timeZoneOffset", tzOffset);
            argsObjectBuilder.add("clientVersion", "6.2.1.0");
            argsObjectBuilder.add("model", "All Series (ASUS)");
            argsObjectBuilder.add("os", "fbgr");
            argsObjectBuilder.add("osVersion", "1010.0.064");
            argsObjectBuilder.add("platform", "fb");
            argsObjectBuilder.add("sessionId", UUID.randomUUID().toString());
            argsObjectBuilder.add("deviceId", deviceId);
            argsObjectBuilder.add("deviceIdType", "ply");

        }

    }
}
