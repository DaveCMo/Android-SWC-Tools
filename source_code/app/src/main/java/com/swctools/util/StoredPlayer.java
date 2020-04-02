package com.swctools.util;

public class StoredPlayer{
	public String playerName;
	public String playerId;
	public String playerSecret;
	public StoredPlayer(String player, String id, String secret){
		this.playerName = player;
		this.playerId = id;
		this.playerSecret = secret;
	}
	public String toString(){
		return playerName;
	}
	
}
