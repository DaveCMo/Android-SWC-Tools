package com.swctools.common.models.player_models;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonObject;

public class Scalars {
	public int attacksLost;
	public int attacksWon;
	public int defensesLost;
	public int defensesWon;
	public int attacksStarted;
	public int attacksCompleted;
	public int attackRating;
	public int defenseRating;
	//I CBA making getters/setters. 
	public Scalars(String jsonStr){
		JsonObject scalars = Json.createReader(new StringReader(jsonStr)).readObject();
		attacksLost = scalars.getInt("attacksLost");
		attacksWon = scalars.getInt("attacksWon");
		defensesLost = scalars.getInt("defensesLost");
		defensesWon = scalars.getInt("defensesWon");
		attacksStarted = scalars.getInt("attacksStarted");
		attacksCompleted = scalars.getInt("attacksCompleted");
		attackRating = scalars.getInt("attackRating");
		defenseRating = scalars.getInt("defenseRating");

		
	}
	
}
