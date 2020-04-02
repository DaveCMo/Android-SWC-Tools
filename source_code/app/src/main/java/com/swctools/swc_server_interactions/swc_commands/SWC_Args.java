package com.swctools.swc_server_interactions.swc_commands;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public abstract class SWC_Args {

	
	protected JsonObjectBuilder argsObjectBuilder;

	public SWC_Args(){
		argsObjectBuilder = Json.createObjectBuilder();		
	}
	
	public JsonObject args(){
		buildArgs();
		return argsObjectBuilder.build();
	}
	
	protected abstract void buildArgs();
}
