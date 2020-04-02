package com.swctools.common.models.player_models;

import android.util.Log;

import com.swctools.common.enums.BuildingGeneric;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

public class MapBuildings implements Serializable {
    private static final String TAG = "MAPBUILDGINGS";
    private final String[] junkTypes = {"rockSmall", "rockMedium", "rockLarge", "junkSmall", "junkMedium", "junkLarge"};
    private ArrayList<Building> buildings = new ArrayList<>();
    private HashMap<String, Building> buildingHashMap = new HashMap<>();
    private ArrayList<Building> junk = new ArrayList<>();
    private ArrayList<Building> traps = new ArrayList<>();

    public MapBuildings() {

    }

    public MapBuildings(JsonObject jBuilding) {
        JsonArray jBuildingArr = jBuilding.getJsonArray("buildings");
        addBuildings(jBuildingArr);
    }

    public MapBuildings(JsonArray jBuildingArr) {
        addBuildings(jBuildingArr);
    }

    public MapBuildings(ArrayList<Building> building) {
        setBuildings(building);
    }


    public void addBuildings(JsonArray jBuildingArr) {
        for (JsonValue jsonValue : jBuildingArr) {
            JsonObject obj = (JsonObject) jsonValue;
            Building b = new Building(obj);
            if (b.getBuildingProperties().isJunk()) {
                junk.add(b);
            } else {
                buildings.add(b);
                buildingHashMap.put(b.getKey(), b);
                if (b.getBuildingProperties().isTrap()) {
                    traps.add(b);
                }
            }
        }
    }

    public ArrayList<Building> getBuildings() {
        return buildings;
    }

    public void setBuildings(ArrayList<Building> buildings) {
        this.buildings = buildings;
    }

    public void setUnassigned() {
        for (Building building : buildings) {
            building.setAssigned(false);
        }
    }

    public Building getSquadBuilding() {
        Building b = null;
        for (Building building : buildings) {
            if (building.getBuildingProperties().getGenericName().equalsIgnoreCase(BuildingGeneric.SQUADBUILDING.getName())) {
                b = building;
                break;
            }
        }
        return b;
    }

    public Building getResearchLab() {
        Building b = null;
        for (Building building : buildings) {
            if (building.getBuildingProperties().getGenericName().equalsIgnoreCase(BuildingGeneric.OFFENSELAB.getName())) {
                b = building;
                break;
            }
        }
        return b;
    }


    public String delimitForDebug() {
        StringBuilder sb = new StringBuilder();
        for (Building building : buildings) {
            sb.append(building.getKey() + "|" + building.getBuildingProperties().getGenericName() + "|" + building.getX() + "|" + building.getZ());
            sb.append("\n");
        }
        return sb.toString();

    }

    public String delimitForDebugv2() {
        StringBuilder sb = new StringBuilder();
        for (Building building : buildings) {
            sb.append(building.getKey() + "|" + building.getX() + "|" + building.getZ() + "|IS JUNK" + building.getBuildingProperties().isJunk());
            sb.append("\n");
        }
        return sb.toString();

    }

    public int junkCount() {
        if (junk != null) {
            return junk.size();
        } else {
            return 0;
        }
    }

    public String asOutputJSON() {
        JsonArrayBuilder buildingArrBuilder = Json.createArrayBuilder();

        for (Building building : buildings) {
            JsonObjectBuilder buildingObj = Json.createObjectBuilder();
            buildingObj.add("key", building.getKey());
            buildingObj.add("x", building.getX());
            buildingObj.add("z", building.getZ());
            buildingObj.add("uid", building.getUid());
            buildingArrBuilder.add(buildingObj);
        }
        return buildingArrBuilder.build().toString();
    }

    public JsonObject layoutPositions() {
//			Comparator<Building> byBuildingX = new Comparator<Building>() {
//				@Override
//				public int compare(Building b1, Building b2) {
//					Integer x1 = b1.getZ();
//					Integer x2 = b2.getZ();
//					int sComp = x2.compareTo(x1);
//
////					if (sComp != 0) {
//						return sComp;
////					} else {
////						Integer z1 = b1.getX();
////						Integer z2 = b2.getX();
////						return z2.compareTo(z1);
////					}
//				}
//			};
//
//			buildings.sort(byBuildingX);
        JsonObjectBuilder buildingsBuilder = Json.createObjectBuilder();
        for (Building building : buildings) {
            JsonObjectBuilder positionsBuilder = Json.createObjectBuilder();
            positionsBuilder.add("x", building.getX());
            positionsBuilder.add("z", building.getZ());
            buildingsBuilder.add(building.getKey(), positionsBuilder);
        }
        return buildingsBuilder.build();
    }

    public HashMap<String, Building> getBuildingHashMap() {
        return buildingHashMap;
    }

    public ArrayList<Building> getTraps() {
        return traps;
    }


    public int getSquadBuildingCap() {
        switch (getSquadBuilding().getBuildingProperties().getLevel()) {
            case 1:
                return 4;
            case 2:
                return 8;
            case 3:
                return 12;
            case 4:
                return 16;
            case 5:
                return 20;
            case 6:
                return 24;
            case 7:
                return 28;
            case 8:
                return 32;
            case 9:
                return 36;
            case 10:
                return 40;
            case 11:
                return 42;
            default:
                return 0;
        }
    }


    //	public SendMessageResult cmd_updateLayout(MapBuildings proposedLayout, PlayerBase base, Context context){
//		getPlayerModel().buildNewLayout(proposedLayout);
//		Response response = null;
//		SendMessageResult sendMessageResult = null;
//		Message message= new Message(getAuthKey(), updateLayoutArgs(getPlayerModel().getNewLayout().getBuildings(), base), getLoginTime());
//		try {
//			response = new Response(MessageManager.sendMessage(message.toString(), context));
//			if(response.getData().getStatus() == ServerConstants.RECEIPT_STATUS_COMPLETE.toInt() ||response.getData().getStatus() == ServerConstants.SUCCESS.toInt()){
//				//now for whatever reason... we need to iterate through the fax/rax and place those buildings in individual messages:
//				for(Building building : getPlayerModel().getNewLayout().getBuildings()){
//					if(building.getBuildingProperties().getGenericName().equals(BuildingGeneric.BARRACKS) || building.getBuildingProperties().getGenericName().equals(BuildingGeneric.FACTORY)){
//						ArrayList<Building> tmpFaxRax = new ArrayList<>();
//						tmpFaxRax.add(building);
//						Message raxFaxMsg = new Message(getAuthKey(), updateLayoutArgs(tmpFaxRax, base), getLoginTime());
//						Response raxFaxResponse = new Response(MessageManager.sendMessage(raxFaxMsg.toString(), context));
//						if(raxFaxResponse.getData().getStatus() == ServerConstants.RECEIPT_STATUS_COMPLETE.toInt() ||raxFaxResponse.getData().getStatus() == ServerConstants.SUCCESS.toInt()){
//
//						} else {
//
//							break;//break out of the loop.
//						}
//					}
//				}
//			}
//			sendMessageResult = new SendMessageResult(response.getData().getStatusCode(), response.getResponse());
//		} catch (IOException e){
//			sendMessageResult = new SendMessageResult(ServerConstants.APP_BLEW_UP.toString(), e.getMessage());
//			ErrorLogger.LogError(e.getMessage(), "Player", "cmd_login", "Error getting auth response");
//		}
//		return sendMessageResult;
//	}
}
