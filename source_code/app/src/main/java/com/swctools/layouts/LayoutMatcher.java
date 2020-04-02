package com.swctools.layouts;

import android.content.Context;
import android.database.Cursor;

import com.swctools.config.AppConfig;
import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;
import com.swctools.common.enums.Factions;
import com.swctools.common.models.player_models.Building;
import com.swctools.common.models.player_models.MapBuildings;
import com.swctools.util.MethodResult;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.json.Json;
import javax.json.JsonArray;

public class LayoutMatcher {
    private static final String TAG = "LAYOUTMATCHER";
    private static final String MESSAGETEMPLATE = "Matching %1$s...(%2$s of %3$s)";
    private Iterator<Map.Entry<String, Building>> proposeLayoutItr;
    private Iterator<Map.Entry<String, Building>> currentLayoutItr;
    private HashMap<String, Building> currentLayoutMap;
    private HashMap<String, Building> proposeLayoutMap;

    private MapBuildings currentLayout, proposedLayout, newLayout;
    private Context mContext;
    private static AppConfig appConfig;


    private static int maxMatchLoop = 6;
    private static int publishInterval = 5;
    private String faction;
    private static HashMap<String, String> buildingMaster;
    private boolean showLog = false;
    private ArrayList<String> message = new ArrayList<>();

    public LayoutMatcher(String currentLayoutStr, String proposedLayoutStr, String pFaction, Context c) {

        JsonArray currentLayoutJson = Json.createReader(new StringReader(currentLayoutStr)).readArray();
        JsonArray proposedLayoutJson = Json.createReader(new StringReader(proposedLayoutStr)).readArray();


        this.currentLayout = new MapBuildings(currentLayoutJson);// currentLayout;
        this.proposedLayout = new MapBuildings(proposedLayoutJson);// proposedLayout;

        currentLayoutMap = currentLayout.getBuildingHashMap();
        proposeLayoutMap = proposedLayout.getBuildingHashMap();

        mContext = c;
        appConfig = new AppConfig(mContext);
        currentLayoutItr = currentLayout.getBuildingHashMap().entrySet().iterator();
        newLayout = new MapBuildings();
        faction = pFaction;

        buildingMaster = getBuildingMaster(faction, mContext);
    }


    public static MethodResult matchedLayout(String proposedLayoutStr, String currentLayoutStr, String faction, Context mContext) {
        AppConfig appConfig = new AppConfig(mContext);
        int progressPublished = 0;
        try {
            JsonArray currentLayoutJson = Json.createReader(new StringReader(currentLayoutStr)).readArray();
            JsonArray proposedLayoutJson = Json.createReader(new StringReader(proposedLayoutStr)).readArray();


            MapBuildings proposedLayout = new MapBuildings(proposedLayoutJson);// proposedLayout;
            MapBuildings currentLayout = new MapBuildings(currentLayoutJson);// currentLayout;
            MapBuildings newLayout = new MapBuildings();
            buildingMaster = getBuildingMaster(faction, mContext);

            ArrayList<Building> currentBuildingArray = currentLayout.getBuildings();
            ArrayList<Building> proposedBuildingArray = proposedLayout.getBuildings();

            int numberOfBuildings = currentBuildingArray.size();
            int matchedCount = 0;
//            StringBuilder stringBuilder = new StringBuilder();
            //go through them all and find if they are the same buildings in each layout:
            for (Building currentBuilding : currentBuildingArray) {
//                stringBuilder.append(currentBuilding.printCurrentSQL());
                if (!currentBuilding.getAssigned()) {
                    boolean matchFound = false;
                    for (Building proposedBuilding : proposedBuildingArray) {

                        if (matchByKey(proposedBuilding, currentBuilding)) {
                            newLayout.getBuildings().add(getBuildingToadd(proposedBuilding, currentBuilding));
                            currentBuilding.setAssigned(true);
                            proposedBuilding.setAssigned(true);
                            matchedCount++;
                            matchFound = true;
                        }
                        if (matchFound) {
                            break;
                        }
                    }
                }
            }
//            SaveJsonFile.saveJsonFile(stringBuilder.toString(), "CurrentInsert", "layout_manager_exports", ".json");
            //Do turrets:

            for (Building currentBuilding : currentBuildingArray) {
                if (!currentBuilding.getAssigned()) {
                    boolean matchFound = false;

                    for (int i = 1; i <= maxMatchLoop; i++) {
                        for (Building proposedBuilding : proposedBuildingArray) {

                            if (!proposedBuilding.getAssigned()) {
                                if (matchBuilding(proposedBuilding, currentBuilding, i, appConfig.iLayoutLevelThreshold())) {
                                    newLayout.getBuildings().add(getBuildingToadd(proposedBuilding, currentBuilding));
                                    currentBuilding.setAssigned(true);
                                    proposedBuilding.setAssigned(true);
                                    matchedCount++;
                                    if (progressPublished >= publishInterval) {
                                        progressPublished = 0;
                                    } else {
                                        progressPublished++;
                                    }
                                    matchFound = true;
                                }
                            }
                            if (matchFound) {
                                break;
                            }
                        }
                        if (matchFound) {
                            break;
                        }
                    }
                } else {
                }
            }
            //What is not matched
            StringBuilder message = new StringBuilder();
            StringBuilder unmatchedMessage = new StringBuilder();
            message.append("");
            int notMatched = 0;

            for (Building currentBuilding : currentBuildingArray) {
                if (!currentBuilding.getAssigned()) {
                    unmatchedMessage.append(currentBuilding.printDetail() + "\n");
                    notMatched++;
                }
            }

            if (notMatched > 0) {
                message.append("Some buildings unmatched: \n" + unmatchedMessage);
            } else {
                message.append("All buildings matched!");
            }
            return new MethodResult(true, newLayout, message.toString());
        } catch (Exception e) {
            return new MethodResult(false, e);
        } finally {
        }
    }


    private static boolean matchByKey(Building proposedLayoutBuilding, Building currentLayoutBuilding) {
        if (currentLayoutBuilding.getKey().equalsIgnoreCase(proposedLayoutBuilding.getKey())) {
            String currentType = currentLayoutBuilding.getBuildingProperties().getGenericName();
            String proposeType = proposedLayoutBuilding.getBuildingProperties().getGenericName();
            if (currentType.equalsIgnoreCase(proposeType) && currentLayoutBuilding.getBuildingProperties().getLevel() >= proposedLayoutBuilding.getBuildingProperties().getLevel()) {
                return true;
            }
        }
        return false;
    }

    private static boolean matchBuilding(Building proposedLayoutBuilding, Building currentLayoutBuilding, int matchType, int threshold) {
        boolean matched = false;
        int logLevel = -1;//for debug

        try {
            switch (matchType) {

                case 1:
                    matched = matchGeneric_SameLevel(proposedLayoutBuilding, currentLayoutBuilding);
                    break;
                case 2:
                    matched = matchGeneric_LevelThreshold(proposedLayoutBuilding, currentLayoutBuilding, threshold);
                    break;
                case 3:
                    matched = match_Generic(proposedLayoutBuilding, currentLayoutBuilding);
                    break;
                case 4:
                    matched = match_Type(proposedLayoutBuilding, currentLayoutBuilding);
                    break;
                case 5:
                    matched = match_TypeLevelThreshold(proposedLayoutBuilding, currentLayoutBuilding);
                    break;
                case 6:
                    matched = matchToAlternativeBuilding(proposedLayoutBuilding, currentLayoutBuilding);
                default:
                    return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return matched;
    }

    private static Building getBuildingToadd(Building matchedBuilding, Building playerBuilding) {

        return new Building(playerBuilding.getKey(), playerBuilding.getUid(), matchedBuilding.getX(), matchedBuilding.getZ());
    }


    private static boolean matchToAlternativeBuilding(Building propBuilding, Building
            currentBuildingEntry) {
        boolean matched = false;
        try {
            String replaceAble[] = Building.getReplaceableBuildingList().get(currentBuildingEntry.getBuildingProperties().getGenericName());
            for (int i = 0; i < replaceAble.length; i++) {
                if (propBuilding.getBuildingProperties().getGenericName().equalsIgnoreCase(replaceAble[i])) {
                    matched = true;
                    break;
                }
            }
        } catch (Exception e) {
            matched = false;
        }
        return matched;
    }

    private static String getBuildingTypeFromMaster(String lookupValue) {
        try {
            String returnVal = buildingMaster.get(lookupValue);
            return returnVal;
        } catch (Exception e) {
            e.printStackTrace();
            return lookupValue;
        }
    }

    private static boolean match_Generic(Building propBuilding, Building currentBuildingEntry) {
        if (propBuilding.getBuildingProperties().getGenericName().equalsIgnoreCase(currentBuildingEntry.getBuildingProperties().getGenericName())) {
            return true;
        } else {
            return false;
        }
    }


    private static boolean match_Type(Building proposedLayoutBuilding, Building currentLayoutBuilding) {

        try {
            String currentType = getBuildingTypeFromMaster(currentLayoutBuilding.getBuildingProperties().getGenericName());
            String proposeType = getBuildingTypeFromMaster(proposedLayoutBuilding.getBuildingProperties().getGenericName());
            if (proposeType.equalsIgnoreCase(currentType)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
//            e.printStackTrace();
            return false;
        }

    }

    private static boolean match_TypeLevelThreshold(Building propBuilding, Building
            currentLayoutBuilding) {
        int levelDiff = 0;
        String currentType;
        String proposeType;
        try {
            levelDiff = (propBuilding.getBuildingProperties().getLevel() - currentLayoutBuilding.getBuildingProperties().getLevel());

            currentType = getBuildingTypeFromMaster(currentLayoutBuilding.getBuildingProperties().getGenericName());
            proposeType = getBuildingTypeFromMaster(propBuilding.getBuildingProperties().getGenericName());
            if (proposeType.equalsIgnoreCase(currentType) && levelDiff <= appConfig.iLayoutLevelThreshold()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }


    }

    private static boolean matchGeneric_LevelThreshold(Building propBuilding, Building currentBuildingEntry, int threshold) {
        if (propBuilding.getBuildingProperties().getGenericName().equalsIgnoreCase(currentBuildingEntry.getBuildingProperties().getGenericName()) &&
                ((propBuilding.getBuildingProperties().getLevel() - currentBuildingEntry.getBuildingProperties().getLevel()) <= threshold)) { //match based on the same name and within a level threshold
            return true;
        } else {
            return false;
        }
    }


    private static boolean matchGeneric_SameLevel(Building propBuilding, Building currentBuildingEntry) {
        if (propBuilding.getBuildingProperties().getGenericName().equalsIgnoreCase(currentBuildingEntry.getBuildingProperties().getGenericName()) &&
                propBuilding.getBuildingProperties().getLevel() <= currentBuildingEntry.getBuildingProperties().getLevel()) {
            return true;

        } else {
            return false;
        }
    }

    private static HashMap<String, String> getBuildingMaster(String faction, Context mContext) {

        HashMap<String, String> buildingMaster = new HashMap<>();
        String[] columns = {DatabaseContracts.Buildings.GENERIC_NAME, DatabaseContracts.Buildings.FACTION, DatabaseContracts.Buildings.TYPE};
        String whereStr = DatabaseContracts.Buildings.FACTION + " = ? AND " + DatabaseContracts.Buildings.LEVEL + " = ?";
        String[] whereArgs = {faction, "1"};
        Cursor cursor = DBSQLiteHelper.queryDB(DatabaseContracts.Buildings.TABLE_NAME, null, whereStr, whereArgs, mContext);

        try {
            while (cursor.moveToNext()) {
                String gName = cursor.getString(cursor.getColumnIndex(DatabaseContracts.Buildings.GENERIC_NAME));
                String bType = cursor.getString(cursor.getColumnIndex(DatabaseContracts.Buildings.TYPE));
                buildingMaster.put(gName, bType);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return buildingMaster;
    }

    private String buildUnmatchedList() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Building b : currentLayout.getBuildings()) {
            if (!b.getAssigned()) {
                stringBuilder.append(b.getKey() + "|" + b.getUid() + "\n");
            }
        }
        return stringBuilder.toString();
    }

    private boolean attemptMatchUnDefinedBuilding(Building proposedLayoutBuilding, Building
            currentLayoutBuilding) {
        boolean matchSuccess = false;
        Matcher matcher;
        String tmpCurrentName = currentLayoutBuilding.getUid();
        String tmpProposdName = proposedLayoutBuilding.getUid();

        //Strip factions from both buildings:
        if (tmpCurrentName.startsWith(Factions.EMPIRE.getFactionName())) {
            tmpCurrentName = tmpCurrentName.replaceAll(Factions.EMPIRE.getFactionName(), "");
        } else if (tmpCurrentName.startsWith(Factions.REBEL.getFactionName())) {
            tmpCurrentName = tmpCurrentName.replaceAll(Factions.REBEL.getFactionName(), "");
        }

        if (tmpProposdName.startsWith(Factions.EMPIRE.getFactionName())) {
            tmpProposdName = tmpProposdName.replaceAll(Factions.EMPIRE.getFactionName(), "");
        } else if (tmpProposdName.startsWith(Factions.REBEL.getFactionName())) {
            tmpProposdName = tmpProposdName.replaceAll(Factions.REBEL.getFactionName(), "");
        }

        //Strip levels from both buildings:
        try {
            matcher = Pattern.compile("[^0-9]*([0-9]+).*").matcher(tmpCurrentName);
            if (matcher.matches()) {
                tmpCurrentName = tmpCurrentName.replaceAll(matcher.group(1), "");
            }
            matcher = Pattern.compile("[^0-9]*([0-9]+).*").matcher(tmpProposdName);
            if (matcher.matches()) {
                tmpProposdName = tmpProposdName.replaceAll(matcher.group(1), "");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (tmpCurrentName.equalsIgnoreCase(tmpProposdName)) {
            matchSuccess = true;
        }

        return matchSuccess;
    }
}
