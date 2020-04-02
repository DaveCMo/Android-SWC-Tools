package com.swctools.common.enums;

public enum Factions {
	EMPIRE("empire"),
	REBEL("rebel");
	
	private final String factionName;
	Factions(String factionName){
		this.factionName = factionName;
	}
	public final String getFactionName() {
		return this.factionName;
	}
	
	
	
}
