package com.swctools.swc_server_interactions;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class Message {
	public JsonObjectBuilder messageBuilder;
	public JsonObject message;
	public JsonObject args;
	public long lastLoginTime;
	
	public Message(String authKey, JsonObject args, long lastLoginTime){
		//action, commands etc set in individual
		this.lastLoginTime = lastLoginTime;
		messageBuilder = Json.createObjectBuilder();
		messageBuilder.add("authKey", authKey);
		this.args = args;
	}	
	
	public String toString(){		
		JsonArrayBuilder _commands = Json.createArrayBuilder();
		_commands.add(args);
		messageBuilder.add("commands", _commands.build());
		messageBuilder.add("lastLoginTime", lastLoginTime);
		messageBuilder.add("pickupMessages", true);
		message = messageBuilder.build();
		return message.toString();
	}
	
}
