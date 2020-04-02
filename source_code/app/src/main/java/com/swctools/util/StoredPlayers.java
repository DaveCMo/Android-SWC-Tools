package com.swctools.util;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

public class StoredPlayers {
	private final String fileLoc = "JSON/users.json";
	private ArrayList<StoredPlayer> users;
	public StoredPlayers(){
		refreshUsers();
	}
	
	public void refreshUsers(){
		ArrayList<StoredPlayer>_users = new ArrayList<>();
		try {
			JsonObject jsonObject = Json.createReader(new StringReader(TextFileReader.readLargerTextFile(fileLoc))).readObject();
			JsonObject users = jsonObject.getJsonArray("users").getJsonObject(0);
		} catch (IOException e) {
			ErrorLogger.LogError(e.getMessage(), "Users", "UsersArraylist", "");
		}		
			this.users = _users;
	}
	public void saveUsers(){
		JsonObjectBuilder storedPlayersJsonObj = Json.createObjectBuilder(); 
		JsonObjectBuilder usersJsonObj = Json.createObjectBuilder(); 
		
		JsonArrayBuilder usersJsonArray = Json.createArrayBuilder();
		for(StoredPlayer storedPlayer : users){
			JsonObjectBuilder userRow = Json.createObjectBuilder();
			userRow.add("playerId", storedPlayer.playerId);
			userRow.add("playerSecret", storedPlayer.playerSecret);
			usersJsonObj.add(storedPlayer.playerName, userRow);
		}
		usersJsonArray.add(usersJsonObj);
		storedPlayersJsonObj.add("users", usersJsonArray);

		List<String> lines = Arrays.asList(storedPlayersJsonObj.build().toString());
//		Path file = Paths.get(fileLoc);
//		try {
//			Files.write(file, lines, Charset.forName("UTF-8"));
//		} catch (IOException e) {
//
//			e.printStackTrace();
//		}
	}
	public boolean playerExists(String searchPlayerId){
		boolean result = false;
		for(StoredPlayer storedPlayer : users){
			if(storedPlayer.playerId.equals(searchPlayerId)){
				result = true;
				break;
			}
		}		
		return result;
	}
	
	public ArrayList<StoredPlayer> getUsers() {
		return users;
	}
}
