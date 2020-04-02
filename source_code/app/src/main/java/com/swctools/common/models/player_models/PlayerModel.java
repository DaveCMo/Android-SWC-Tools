package com.swctools.common.models.player_models;

import android.content.Context;
import android.database.Cursor;

import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;
import com.swctools.common.enums.ApplicationMessageTemplates;
import com.swctools.common.enums.BundleKeys;
import com.swctools.common.helpers.BundleHelper;
import com.swctools.activity_modules.player.models.BattleLogs;
import com.swctools.swc_server_interactions.results.JSONplayerModel;
import com.swctools.swc_server_interactions.results.SWCVisitResult;
import com.swctools.common.model_list_providers.GameUnitConversionListProvider;
import com.swctools.activity_modules.player.models.ResourceDataItem;
import com.swctools.activity_modules.player.models.BuildingDAO;
import com.swctools.activity_modules.player.models.TwoTextItemData;

import java.text.DecimalFormat;
import java.util.HashMap;

public class PlayerModel {
    private static final String TAG = "PlayerModel";
    public String playerId;
    public String playerName;
    public String guildId;
    public String playerFaction;
    public Context mContext;
    public JSONplayerModel mplayerModel;
    public ActiveArmoury activeArmoury;
    public DonatedTroops donatedTroops;
    public GuildModel guildModel;
    public Inventory inventory;
    public MapBuildings mapBuildings;
    public String visitResponse;
    public BattleLogs battleLogs;
    private SWCVisitResult jsonVisitor;
    public PlayerTraps playerTraps;
    public String planetName;
    public TroopStorage troopStorage;
    public TwoTextItemData baseScoreDetail;
    public TwoTextItemData medalCountDetail;
    public TwoTextItemData attacksWonDetail;
    public TwoTextItemData defencesWonDetail;
    public TwoTextItemData crystals;
    private DecimalFormat formatter;
    public ResourceDataItem reputationCapacity;
    public HashMap<String, Troop> getMasterTroopList;
    public HashMap<String, ArmouryEquipment> getMasterArmourList;
    public HashMap<String, BuildingDAO> getMasterBuildingList;

    public PlayerModel(String playerId, Context context) {
        this.formatter = new DecimalFormat("#,###,###");
        this.playerId = playerId;
        this.mContext = context;
        getMasterTroopList = GameUnitConversionListProvider.getMasterTroopList(mContext);
        getMasterArmourList = GameUnitConversionListProvider.getMasterArmourList(mContext);
        getMasterBuildingList = GameUnitConversionListProvider.getMasterBuildingList(mContext);
        try {

            String bundleKey = String.format(ApplicationMessageTemplates.VISIT_KEY.getemplateString(),
                    BundleKeys.VISIT_RESPONSE.toString(), playerId);
            BundleHelper bundleHelper = new BundleHelper(bundleKey);
            String visitResponse = bundleHelper.get_value(mContext);
            jsonVisitor = new SWCVisitResult(visitResponse);

        } catch (Exception e) {

        } finally {

        }
    }

    public void buildModel() {

        try {

            playerName = jsonVisitor.name();
            playerFaction = jsonVisitor.mplayerModel().faction();
            mplayerModel = jsonVisitor.mplayerModel();
            guildModel = new GuildModel(mplayerModel.guildInfo().toString());
            guildId = guildModel.getGuildId();
            try {

                activeArmoury = new ActiveArmoury(mplayerModel.activeArmory(), playerFaction, getMasterArmourList, mContext);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {

            }
            mapBuildings = new MapBuildings(mplayerModel.map());
            try {

                donatedTroops = new DonatedTroops(mplayerModel.donatedTroops(), playerFaction, mapBuildings.getSquadBuilding(), guildId, getMasterTroopList, mContext);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {

            }
            inventory = new Inventory(mplayerModel, mContext);
            try {

                planetName = getPlanetName(mplayerModel.map().getString("planet"), mContext);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
            }
            try {
                playerTraps = new PlayerTraps(mapBuildings, playerFaction, getMasterBuildingList, mContext);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {

            }
            try {

                troopStorage = new TroopStorage(mplayerModel.inventory(), playerFaction, getMasterTroopList, mContext);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {

            }
            String baseScoreVal = "";
            try {
                baseScoreVal = formatter.format(mplayerModel.inventory().getJsonObject("storage").getJsonObject("xp").getInt("amount"));
            } catch (Exception e) {
                baseScoreVal = "Error parsing base score";
            }
            baseScoreDetail = new TwoTextItemData("Base Score", baseScoreVal);
            int medalCount = jsonVisitor.scalars().attackRating + jsonVisitor.scalars().defenseRating;
            medalCountDetail = new TwoTextItemData("Medal Count", formatter.format(medalCount));
            reputationCapacity = new ResourceDataItem("Reputation", inventory.reputation.getCapacity(), inventory.reputation.getAmount(), 0);
            attacksWonDetail = new TwoTextItemData("Attacks Won", formatter.format(jsonVisitor.scalars().attacksWon));
            defencesWonDetail = new TwoTextItemData("Defences Won", formatter.format(jsonVisitor.scalars().defensesWon));
            crystals = new TwoTextItemData("CRYSTALS", inventory.crystals.getAmountComma());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }


    private String getPlanetName(String lookup, Context context) {
        String planetName = lookup;
        String[] columns = {DatabaseContracts.Planets.UI_NAME, DatabaseContracts.Planets.GAME_NAME};
        String selectionStr = DatabaseContracts.Planets.GAME_NAME + " = ?";
        String[] selectionArgs = {lookup};
        Cursor cursor = DBSQLiteHelper.queryDB(DatabaseContracts.Planets.TABLE_NAME, columns, selectionStr, selectionArgs, context);
        while (cursor.moveToNext()) {
            planetName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.Planets.UI_NAME));
        }
        return planetName;
    }

    public void buildBattleLog() {

        try {

            battleLogs = new BattleLogs(playerId, mplayerModel.battleLogs(), getMasterTroopList, getMasterArmourList, mContext);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

}
