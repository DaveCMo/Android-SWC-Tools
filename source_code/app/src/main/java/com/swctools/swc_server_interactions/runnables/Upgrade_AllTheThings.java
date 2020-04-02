package com.swctools.swc_server_interactions.runnables;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

import com.swctools.config.AppConfig;
import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;
import com.swctools.activity_modules.main.models.PlayerDAO;
import com.swctools.common.models.player_models.UpgradeItemModel;
import com.swctools.common.models.player_models.Building;
import com.swctools.common.models.player_models.Inventory;
import com.swctools.common.models.player_models.MapBuildings;
import com.swctools.common.models.player_models.UpgradeItems;
import com.swctools.common.models.player_models.Upgrades;
import com.swctools.swc_server_interactions.PlayerLoginSession;
import com.swctools.swc_server_interactions.SWCMessage;
import com.swctools.swc_server_interactions.results.SWCDefaultResponse;
import com.swctools.swc_server_interactions.results.SWCDefaultResultData;
import com.swctools.swc_server_interactions.results.SWCVisitResult;
import com.swctools.swc_server_interactions.swc_commands.Cmd_BuildingBuyOut;
import com.swctools.swc_server_interactions.swc_commands.Cmd_BuildingInstantUpgrade;
import com.swctools.swc_server_interactions.swc_commands.Cmd_BuildingUpgradeALL;
import com.swctools.swc_server_interactions.swc_commands.Cmd_Deployable_Upgrade;
import com.swctools.swc_server_interactions.swc_commands.Cmd_EquipmentUpgrade;
import com.swctools.swc_server_interactions.swc_commands.Cmd_KeepAlive;
import com.swctools.swc_server_interactions.swc_commands.Cmd_NeighborVisit;
import com.swctools.swc_server_interactions.swc_commands.SWC_Command;
import com.swctools.util.MethodResult;
import com.swctools.util.Utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Upgrade_AllTheThings extends SWC_Runnable_Base implements Runnable {
    private static final String TAG = "Upgrade_Troops";
    private static final int BUILDING = 1;
    private static final int TROOPS = 2;

    private String playerId;
    private Context context;
    private PlayerLoginSession botLoginSession;
    private long sleepTime = 500;
    private ArrayList<UpgradeItemModel> upgradeTroops;
    private ArrayList<UpgradeItemModel> upgradeEquip;
    private ArrayList<UpgradeItemModel> upgradeBuildings;
    private ArrayList<UpgradeItemModel> upgradeWalls;

    private PlayerLoginSession playerLoginSession;

    public Upgrade_AllTheThings(Handler swc_interaction_handler, String playerId, Context context) {
        super(swc_interaction_handler);
        this.playerId = playerId;
        this.context = context;

    }

    @Override
    public void run() {
        MethodResult methodResult = upgradeStuffs();
        Message msg = Message.obtain();

        msg.obj = methodResult;
        swc_interaction_handler.sendMessage(msg);

    }


    private MethodResult upgradeStuffs() {
        MethodResult methodResult = new MethodResult(true, "");
        int errorCount = 0;
        if (!Utils.isConnected(context)) {
            return new MethodResult(false, NO_INTERNET);
        } else {
            AppConfig appConfig = new AppConfig(context);
//            PlayerDAO dachmoDAO = new PlayerDAO("bcf4fc83-291a-11e7-9def-06948e004f29", context);
            botLoginSession = new PlayerLoginSession(appConfig.getVisitor_playerId(), appConfig.getVisitor_playersecret(), appConfig.getVisitorDeviceId(), true);
            botLoginSession.login(true, context);
            if (!botLoginSession.isLoggedIn()) {
                return botLoginSession.getLoginResult();
            }
            sendProgressUpdateToUI("Getting data...");
            try {
                SWCDefaultResponse visitResponse = new SWCMessage(botLoginSession.getAuthKey(), true, botLoginSession.getLoginTime(), new Cmd_NeighborVisit(botLoginSession.newRequestId(), botLoginSession.getPlayerId(), playerId), "visitNeighbour", context).getSwcMessageResponse(); // new SWCDefaultResponse(sendPlayerMessage(new SWCMessage(authKey, true, loginTime, new Cmd_NeighborVisit(visitRequestId, playerId, neighborId), "visitNeighbour", mContext), true, true).getMessage());
                SWCVisitResult disneyVisitResult = new SWCVisitResult(visitResponse.getResponseDataByRequestId(botLoginSession.getRequestId()));
                if (!disneyVisitResult.isSuccess()) {
                    return new MethodResult(false, disneyVisitResult.getStatusCodeAndName());
                }
                //Get the lab building id:
                MapBuildings mapBuildings = new MapBuildings(disneyVisitResult.mplayerModel().map());
                String labBuilding = mapBuildings.getResearchLab().getKey();

                upgradeTroops = getUpgradeTroops(disneyVisitResult);
                upgradeEquip = getUpgradeEquipment(disneyVisitResult);
                upgradeBuildings = getUpgradeBuildings(mapBuildings);
                upgradeWalls = getUpgradeWalls(mapBuildings);


                PlayerDAO playerDAO = new PlayerDAO(playerId, context);
                playerLoginSession = new PlayerLoginSession(playerId, playerDAO.getPlayerSecret(), playerDAO.getDeviceId(), true);
                playerLoginSession.login(true, context);
                if (!playerLoginSession.isLoggedIn()) {
                    return playerLoginSession.getLoginResult();
                }

                Inventory inventory = new Inventory(disneyVisitResult.mplayerModel(), context);
                sendProgressUpdateToUI("Logged in...Doing Buildings....");
                //Buildings...
                MethodResult buildingUpgradRes = upgradeBuildingsMethod();
                if (!buildingUpgradRes.success) {
                    errorCount++;
                    methodResult.addMessage(buildingUpgradRes.getMessage());
                }

                MethodResult upgradeEquipmRes = upgradeEquipmentMethod(upgradeEquip, playerLoginSession, labBuilding, inventory, appConfig);
                if (!upgradeEquipmRes.success) {
                    errorCount++;
                    methodResult.addMessage(upgradeEquipmRes.getMessage());
                }

                MethodResult upgradeTroopsRes = upgradeTroopsMethod(upgradeTroops, playerLoginSession, labBuilding, inventory, appConfig);
                if (!upgradeTroopsRes.success) {
                    errorCount++;
                    methodResult.addMessage(upgradeTroopsRes.getMessage());
                }


                return new MethodResult(true, "Complete!");

            } catch (Exception e) {
                return new MethodResult(false, e.getMessage());

            }
        }

    }

    private MethodResult keepAlive(PlayerLoginSession playerLoginSession) {

        try {
            SWCDefaultResponse swcDefaultResponse = new SWCMessage(playerLoginSession.getAuthKey(), true, playerLoginSession.getLoginTime(),
                    new Cmd_KeepAlive(playerLoginSession.newRequestId(), playerLoginSession.getPlayerId()), "", context).getSwcMessageResponse();
            SWCDefaultResultData swcDefaultResultData = new SWCDefaultResultData(swcDefaultResponse.getResponseDataByRequestId(playerLoginSession.getRequestId()));
            if (!swcDefaultResultData.isSuccess()) {
                return new MethodResult(false, swcDefaultResultData.getStatusCodeAndName());
            }
            return new MethodResult(true, "");
        } catch (Exception e) {
            e.printStackTrace();
            return new MethodResult(false, e.getLocalizedMessage());
        }
    }

    private MethodResult upgradeBuildingsMethod() {
        try {
            for (int u = 0; u < upgradeBuildings.size(); u++) {
                UpgradeItemModel upgradeItemModel = upgradeBuildings.get(u);
                int upgradeStart = upgradeItemModel.getCurrentLevel() + 1;
                for (int i = upgradeStart; i <= upgradeItemModel.getMaxLevel(); i++) {
                    sendProgressUpdateToUI("Upgrading " + upgradeItemModel.getLogName() + "|" + upgradeItemModel.getUid() + " to level " + i + "...(" + u + " of " + upgradeBuildings.size());
                    SWCDefaultResponse upgradeBuildingResponse = new SWCMessage(playerLoginSession.getAuthKey(), true, playerLoginSession.getLoginTime(),
                            new Cmd_BuildingInstantUpgrade(playerLoginSession.newRequestId(), playerId, upgradeItemModel.getUid(), playerLoginSession.getLoginTime()), "upgradeBuildings", context).getSwcMessageResponse();
                    SWCDefaultResultData upgradeResult = new SWCDefaultResultData(upgradeBuildingResponse.getResponseDataByRequestId(playerLoginSession.getRequestId()));

                    if (!upgradeResult.isSuccess()) {
                        return new MethodResult(false, upgradeResult.getStatusCodeAndName());//, buyOutResult.getStatusCodeAndName());
                    }
                    SystemClock.sleep(sleepTime);
                }

            }
            //Do walls...
            for (int u = 0; u < upgradeWalls.size(); u++) {
                UpgradeItemModel upgradeItemModel = upgradeWalls.get(u);
                int upgradeStart = upgradeItemModel.getCurrentLevel() + 1;
                for (int i = upgradeItemModel.getCurrentLevel(); i < upgradeItemModel.getMaxLevel(); i++) {
                    sendProgressUpdateToUI("Upgrading " + upgradeItemModel.getLogName() + "|" + upgradeItemModel.getUid() + " to level " + i + "...(" + u + " of " + upgradeWalls.size());
                    String buildingUid = upgradeItemModel.getUid() + i;
                    SWCDefaultResponse swcDefaultResponse = new SWCMessage(playerLoginSession.getAuthKey(), true, playerLoginSession.getLoginTime(),
                            new Cmd_BuildingUpgradeALL(playerLoginSession.newRequestId(), playerId, buildingUid, playerLoginSession.getLoginTime()),
                            "UpgradeWalls", context).getSwcMessageResponse();
                    SWCDefaultResultData upgradeWallsResult = new SWCDefaultResultData(swcDefaultResponse.getResponseDataByRequestId(playerLoginSession.getRequestId()));
                    if (!upgradeWallsResult.isSuccess()) {
                        return new MethodResult(false, upgradeWallsResult.getStatusCodeAndName());
                    }
                    SystemClock.sleep(sleepTime);
                }

            }


            return new MethodResult(true, "Buildings upgraded!");
        } catch (
                Exception e) {
            e.printStackTrace();
            return new MethodResult(false, e.getLocalizedMessage());
        }

    }

    private MethodResult upgradeEquipmentMethod(ArrayList<UpgradeItemModel> upgradeTroops, PlayerLoginSession playerLoginSession, String labBuilding, Inventory inventory, AppConfig appConfig) {

        try {

            for (UpgradeItemModel upgradeItemModel : upgradeTroops) {
                int upgradeStart = upgradeItemModel.getCurrentLevel() + 1;

                for (int i = upgradeStart; i <= upgradeItemModel.getMaxLevel(); i++) {
                    sendProgressUpdateToUI("Upgrading " + upgradeItemModel.getUid() + " to level " + i + "...");
                    String equipmentUid = upgradeItemModel.getUid().concat(String.valueOf(i));
                    int upgradeRequestId = playerLoginSession.newRequestId();
                    int buyOutRequestId = playerLoginSession.newRequestId();
                    ArrayList<SWC_Command> swc_commands = new ArrayList<>();
                    swc_commands.add(new Cmd_EquipmentUpgrade(upgradeRequestId, equipmentUid, playerId, labBuilding));
                    swc_commands.add(new Cmd_BuildingBuyOut(buyOutRequestId, playerId, labBuilding));

                    SWCDefaultResponse swcDefaultResponse = new SWCMessage(playerLoginSession.getAuthKey(), true, playerLoginSession.getLoginTime(),
                            swc_commands, "upgradeEquipment", context).getSwcMessageResponse();
                    SWCDefaultResultData swcDefaultResultData = new SWCDefaultResultData(swcDefaultResponse.getResponseDataByRequestId(playerLoginSession.getRequestId()));
                    if (!swcDefaultResultData.isSuccess()) {

                        break;
                    }
                    SystemClock.sleep(sleepTime);
                }
            }
            return new MethodResult(true, "Completed Equipment!");
        } catch (Exception e) {

            e.printStackTrace();
            return new MethodResult(false, e.getLocalizedMessage());
        }
    }


    private MethodResult upgradeTroopsMethod(ArrayList<UpgradeItemModel> upgradeTroops, PlayerLoginSession playerLoginSession, String labBuilding, Inventory inventory, AppConfig appConfig) {
        try {
            for (UpgradeItemModel upgradeItemModel : upgradeTroops) {
                int upgradeStart = upgradeItemModel.getCurrentLevel() + 1;
                for (int i = upgradeStart; i <= upgradeItemModel.getMaxLevel(); i++) {
                    sendProgressUpdateToUI("Upgrading " + upgradeItemModel.getUid() + " to level " + i + "...");

                    String deployableUid = upgradeItemModel.getUid().concat(String.valueOf(i));

                    int upgradeRequestId = playerLoginSession.newRequestId();
                    int buyOutRequestId = playerLoginSession.newRequestId();
                    ArrayList<SWC_Command> swc_commands = new ArrayList<>();
                    swc_commands.add(new Cmd_Deployable_Upgrade(upgradeRequestId, deployableUid, playerId, labBuilding));
                    swc_commands.add(new Cmd_BuildingBuyOut(buyOutRequestId, playerId, labBuilding));

                    SWCDefaultResponse swcDefaultResponse = new SWCMessage(playerLoginSession.getAuthKey(), true, playerLoginSession.getLoginTime(),
                            swc_commands, "upgradedeployable", context).getSwcMessageResponse();
                    SWCDefaultResultData upgradeRequestResultData = new SWCDefaultResultData(swcDefaultResponse.getResponseDataByRequestId(upgradeRequestId));
                    SWCDefaultResultData buyOutRequestResultData = new SWCDefaultResultData(swcDefaultResponse.getResponseDataByRequestId(buyOutRequestId));


                    if (!upgradeRequestResultData.isSuccess()) {
                        break;
                        //return new MethodResult(false, swcDefaultResultData.getStatusCodeAndName());
                    }
                    SystemClock.sleep(sleepTime);

                }

            }
            return new MethodResult(true, "Completed Troops!");
        } catch (Exception e) {
            e.printStackTrace();
            return new MethodResult(false, e.getLocalizedMessage());
        }
    }

    private ArrayList<UpgradeItemModel> getUpgradeEquipment(SWCVisitResult disneyVisitResult) {
        ArrayList<UpgradeItemModel> upgradeItemModels = new ArrayList<>();

        Upgrades upgrades = new Upgrades(disneyVisitResult.mplayerModel().upgrades(), disneyVisitResult.mplayerModel().faction());
        ///Do troops first....
        for (UpgradeItems upgradeItem : upgrades.getEquipment().getUpgradeItems()) {
            //because some units require some special prefix, not derived from the json object they are in:
            if (upgradeItem.itemLevel < 10) {
                upgradeItemModels.add(new UpgradeItemModel(upgradeItem.itemName, upgradeItem.itemLevel, 10));
            }
        }


        return upgradeItemModels;
    }

    private ArrayList<UpgradeItemModel> getUpgradeWalls(MapBuildings mapBuildings) {
        //Builds a list of wall levels to upgrade them in bulk.
        LinkedHashMap<Integer, UpgradeItemModel> wallHashMap = new LinkedHashMap<>();

        for (Building building : mapBuildings.getBuildings()) {
            //Get name without level...
            String baseBuildingName = building.getUid().replaceAll("[0-9]", "");
            int maxLevel = getMaxThingLevel(baseBuildingName, BUILDING);
            if (building.getBuildingProperties().getLevel() < maxLevel) {
                if (baseBuildingName.contains("Wall")) {
                    wallHashMap.put(building.getBuildingProperties().getLevel(), new UpgradeItemModel(baseBuildingName, building.getBuildingProperties().getLevel(), maxLevel));
                }
            }
        }

        ArrayList<UpgradeItemModel> upgradeItemModels = new ArrayList<>();
        upgradeItemModels.addAll(wallHashMap.values());



        return upgradeItemModels;
    }


    private ArrayList<UpgradeItemModel> getUpgradeBuildings(MapBuildings mapBuildings) {
        ArrayList<UpgradeItemModel> upgradeItemModels = new ArrayList<>();
        for (Building building : mapBuildings.getBuildings()) {
            //Get name without level...
            String baseBuildingName = building.getUid().replaceAll("[0-9]", "");
            int maxLevel = getMaxThingLevel(baseBuildingName, BUILDING);
            if (building.getBuildingProperties().getLevel() < maxLevel) {
                if (!baseBuildingName.contains("Wall")) {
                    UpgradeItemModel upgradeItemModel = new UpgradeItemModel(building.getKey(), building.getBuildingProperties().getLevel(), maxLevel);
                    upgradeItemModel.setLogName(baseBuildingName);
                    upgradeItemModels.add(upgradeItemModel);
                } else {

                }
            }
        }
        return upgradeItemModels;
    }

    private ArrayList<UpgradeItemModel> getUpgradeTroops(SWCVisitResult disneyVisitResult) {
        ArrayList<UpgradeItemModel> upgradeItemModels = new ArrayList<>();

        Upgrades upgrades = new Upgrades(disneyVisitResult.mplayerModel().upgrades(), disneyVisitResult.mplayerModel().faction());
        ///Do troops first....
        for (UpgradeItems upgradeItem : upgrades.getTroop().getUpgradeItems()) {
            //because some units require some special prefix, not derived from the json object they are in:
            String fullname = troopPrefix(upgradeItem.itemName);
            int maxLevel = getMaxThingLevel(fullname, TROOPS);
            if (upgradeItem.itemLevel < maxLevel) {
                if (!fullname.contains("Droideka")) {
//                    Log.d(TAG, "upgradeTroops: ADDING!" + fullname);
                    upgradeItemModels.add(new UpgradeItemModel(fullname, upgradeItem.itemLevel, maxLevel));
                }
            }
        }
        for (UpgradeItems upgradeItem : upgrades.getSpecialAttack().getUpgradeItems()) {
            //because some units require some special prefix, not derived from the json object they are in:
            String fullname = "specialAttack" + upgradeItem.itemName;
            int maxLevel = getMaxThingLevel(fullname, TROOPS);
            if (upgradeItem.itemLevel < maxLevel) {
                upgradeItemModels.add(new UpgradeItemModel(fullname, upgradeItem.itemLevel, maxLevel));
            }
        }

        return upgradeItemModels;
    }


    private int getMaxThingLevel(String name, int OPTION) {
        String table = "";
        switch (OPTION) {
            case BUILDING:
                table = DatabaseContracts.BuildingBaseData.TABLE_NAME;
                break;
            case TROOPS:
                table = DatabaseContracts.TroopBaseData.TABLE_NAME;
                break;
        }
        Cursor cursor = null;
        try {
            String whereClause = "game_name = ?";
            String[] whereArgs = {name};
            cursor = DBSQLiteHelper.queryDB(table, null, whereClause, whereArgs, context);
            while (cursor.moveToNext()) {
                int level = cursor.getInt(cursor.getColumnIndexOrThrow("maxLevel"));
                return level;
            }
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


    private String troopPrefix(String name) {
        //because .. stupid SWC
        if (name.contains("GamorreanWarrior") ||
                name.contains("TwilekIncinerator") ||
                name.contains("Rider") ||
                name.contains("Brute") ||
                name.contains("BetaTroop")
        ) {
            return "troopMercenary" + name;
        }
        if (name.contains("Johhar")) {
            return "troopHero" + name;
        }

        //return troop + name if does not match the above cases - all others follow this pattern
        return "troop" + name;
    }


}
