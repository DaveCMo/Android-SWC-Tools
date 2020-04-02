package com.swctools.database;

import android.provider.BaseColumns;

/**
 * Created by David on 16/02/2018.
 */

public final class DatabaseContracts {
    private DatabaseContracts() {
    }

    public static class WarLog implements BaseColumns {
        public static final String TABLE_NAME = "war_log";
        public static final String COLUMN_ID = "_id";
        public static final String WARID = "warId";
        public static final String PLAYERID = "playerId";
        public static final String GUILDID = "guildId";
        public static final String GUILDNAME = "guildName";
        public static final String GUILDFACTION = "guildFaction";
        public static final String RIVALGUILDID = "rivalGuildId";
        public static final String RIVALGUILDNAME = "rivalGuildName";
        public static final String RIVALGUILDFACTION = "rivalGuildFaction";
        public static final String GUILDRESULTDATA = "guildResultData";
        public static final String RIVALRESULTDATA = "rivalResultData";
        public static final String WARSTATUSRESULTDATA = "warStatusResultData";
        public static final String WARPARTICIPANTRESULTDATA = "warParticipantResultData";

        public static final String START_TIME = "startTime";
        public static final String PREPSTARTTIME = "prepGraceStartTime";
        public static final String PREPENDTIME = "prepEndTime";
        public static final String ACTIONSTARTTIME = "actionGraceStartTime";
        public static final String ACTIONENDTIME = "actionEndTime";
        public static final String COOLDOWNEND = "cooldownEndTime";

//        public static final String SQUADATTACKS = "squadAttacks";
//        public static final String RIVALATTACKS = "rivalAttacks";
//        public static final String SQUADSQUARE = "squadScore";
//        public static final String RIVALSCORE = "rivalScore";
//        public static final String SQUADWIPED = "squadWipes";
//        public static final String RIVALWIPES = "rivalWipes";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PLAYERID + " TEXT," +
                WARID + " TEXT," +
                GUILDID + " TEXT," +
                GUILDNAME + " TEXT," +
                GUILDFACTION + " TEXT," +
                RIVALGUILDID + " TEXT," +
                RIVALGUILDNAME + " TEXT," +
                RIVALGUILDFACTION + " TEXT," +
                GUILDRESULTDATA + " TEXT," +
                RIVALRESULTDATA + " TEXT," +
                WARSTATUSRESULTDATA + " TEXT," +
                WARPARTICIPANTRESULTDATA + " TEXT, " +

                START_TIME + " INTEGER, " +
                PREPSTARTTIME + " INTEGER, " +
                PREPENDTIME + " INTEGER, " +
                ACTIONSTARTTIME + " INTEGER, " +
                ACTIONENDTIME + " INTEGER, " +
                COOLDOWNEND + " INTEGER, " +
                "UNIQUE ( " + WARID + ", " + PLAYERID + "))";
    }

    public static class WarBuffBases implements BaseColumns {
        public static final String TABLE_NAME = "war_buff_bases";
        public static final String COLUMN_ID = "_id";
        public static final String PLAYERID = "playerId";
        public static final String WARID = "warId";
        public static final String BUFFUID = "buffUid";
        public static final String OUTPOST_NAME = "outpost_name";
        public static final String OWNERID = "ownerId";
        public static final String SQUADNAME = "squad_name";
        public static final String LEVEL = "level";
        public static final String ADJUSTED_LEVEL = "adjusted_level";
        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PLAYERID + " TEXT," +
                WARID + " TEXT," +
                BUFFUID + " TEXT," +
                OUTPOST_NAME + " TEXT," +
                OWNERID + " TEXT," +
                SQUADNAME + " TEXT," +
                LEVEL + " INTEGER," +
                ADJUSTED_LEVEL + " INTEGER, " +
                "UNIQUE(" + WARID + ", " + BUFFUID + "))";
    }

    public static class WarParticipantTable implements BaseColumns {
        public static final String TABLE_NAME = "war_participant";
        public static final String COLUMN_ID = "_id";
        public static final String WARID = "warId";
        public static final String PLAYERID = "playerId";
        public static final String GUILDID = "guildId";
        public static final String PLAYER_NAME = "playerName";
        public static final String IS_REQUESTER = "is_requester";
        public static final String BASE_SCORE = "baseScore";
        public static final String HQLEVEL = "hqLevel";
        public static final String TURNS = "turns";
        public static final String VICTORY_POINTS = "victoryPoints";
        public static final String SCORE = "score";
        public static final String SC_CAP = "sc_capacity";
        public static final String SC_CAP_DONATED = "sc_capacity_donated";
        public static final String SQUAD_CENTER_TROOPS = "squadCenterTroops";

        public static final String LAST_ATTACKED = "last_attacked";
        public static final String LAST_ATTACKED_BY_ID = "last_attacked_by_id";
        public static final String LAST_ATTACKED_BY_NAME = "last_attacked_by_name";
        public static final String LAST_BATTLE_ID = "last_battle_id";

        public static final String FACTION = "faction";
        public static final String ERRORWITHSC = "error_with_sc";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                WARID + " TEXT," +
                PLAYERID + " TEXT," +
                GUILDID + " TEXT," +
                PLAYER_NAME + " TEXT," +
                IS_REQUESTER + " TEXT," +
                BASE_SCORE + " INTERGER," +
                HQLEVEL + " INTEGER," +
                TURNS + " INTEGER," +
                VICTORY_POINTS + " INTEGER," +
                SCORE + " INTEGER," +
                SC_CAP + " INTEGER," +
                SC_CAP_DONATED + " INTEGER," +
                SQUAD_CENTER_TROOPS + " TEXT, " +
                LAST_ATTACKED + " INTEGER, " +
                LAST_ATTACKED_BY_ID + " TEXT, " +
                LAST_ATTACKED_BY_NAME + " TEXT, " +
                LAST_BATTLE_ID + " TEXT, " +
                FACTION + " TEXT, " +
                ERRORWITHSC + " TEXT, " +
                "UNIQUE(" + WARID + ", " + PLAYERID + ", " + GUILDID + "))";
    }

    public static class WarBattleDeployable implements BaseColumns {
        public static final String TABLE_NAME = "war_battle_deployed";
        public static final String COLUMN_ID = "_id";
        public static final String BATTLEID = "battleId";
        public static final String WARID = "warId";
        public static final String DEPLOYMENT_TYPE = "deployment_type";
        public static final String DEPLOYABLE = "deployable";
        public static final String DEPLOYMENT_COUNT = "deployment_count";
//        public static final String ATTACKER_GUILD = "attacker_guild";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                WARID + " TEXT," +
                BATTLEID + " TEXT," +
                DEPLOYMENT_TYPE + " TEXT," +
                DEPLOYABLE + " TEXT," +
                DEPLOYMENT_COUNT + " INTEGER, " +
                "UNIQUE(" + BATTLEID + ", " + WARID + ", " + DEPLOYMENT_TYPE + ", " + DEPLOYABLE + "))";

    }

    public static class WarDefense implements BaseColumns {
        public static final String TABLE_NAME = "war_defense";
        public static final String COLUMN_ID = "_id";
        public static final String WARID = "warId";
        public static final String PLAYERID = "playerId";
        public static final String PLAYER_NAME = "playerName";
        public static final String BATTLEID = "battleId";
        public static final String OPPONENTID = "opponentId";
        public static final String OPPONENT_NAME = "opponentName";
        public static final String DEFENSE_END = "defense_end";
        public static final String VICTORY_POINTS = "victoryPoints";
        public static final String ATTACKERWARBUFFS = "attackerWarBuffs";
        public static final String DEFENDERWARBUFFS = "defenderWarBuffs";
        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                WARID + " TEXT, " +
                PLAYERID + " TEXT, " +
                PLAYER_NAME + " TEXT, " +
                BATTLEID + " TEXT, " +
                OPPONENTID + " TEXT, " +
                OPPONENT_NAME + " TEXT, " +
                DEFENSE_END + " INTEGER, " +
                VICTORY_POINTS + " INTEGER, " +
                ATTACKERWARBUFFS + " TEXT, " +
                DEFENDERWARBUFFS + " TEXT, " +
                "UNIQUE(" + WARID + ", " + BATTLEID + "))";
    }

    public static class WarSCContents implements BaseColumns {
        public static final String TABLE_NAME = "war_sc_contents";
        public static final String COLUMN_ID = "_id";
        public static final String WARID = "warId";
        public static final String PLAYERID = "playerId";
        public static final String GAMENAME = "game_name";
        public static final String UINAME = "ui_name";
        public static final String QTY = "qty";
        public static final String CAP = "cap";
        public static final String LEVEL = "unit_level";
        public static final String DONATED_BY_ID = "donated_by_id";
        public static final String DONATED_BY_NAME = "donated_by_name";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                WARID + " TEXT," +
                PLAYERID + " TEXT," +
                GAMENAME + " TEXT," +
                UINAME + " TEXT," +
                QTY + " INTEGER," +
                CAP + " INTEGER," +
                LEVEL + " INTEGER," +
                DONATED_BY_ID + " TEXT," +
                DONATED_BY_NAME + " TEXT, " +
                "UNIQUE(" + WARID + "," + PLAYERID + ", " + UINAME + "))";
    }

    public static class PlayersTable implements BaseColumns {

        public static final String TABLE_NAME = "players";
        public static final String COLUMN_ID = "_id";
        public static final String PLAYERID = "playerId";
        public static final String PLAYERSECRET = "playerSecret";
        public static final String PLAYERNAME = "playerName";
        public static final String FACTION = "playerFaction";
        public static final String PLAYERGUILD = "playerGuild";
        public static final String NOTIFICATIONS = "notifications";
        public static final String REFRESH_INT = "refresh_interval";
        public static final String DEVICE_ID = "deviceId";
        public static final String DEFAULT_FAV = "default_favourite";
        public static final String CARD_EXPANDED = "card_expanded";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PLAYERID + " TEXT," +
                PLAYERSECRET + " TEXT,  " +
                PLAYERNAME + " TEXT, " +
                FACTION + " TEXT, " +
                PLAYERGUILD + " TEXT, " +
                NOTIFICATIONS + " TEXT, " +
                REFRESH_INT + " INTEGER, " +
                DEVICE_ID + " TEXT, " +
                DEFAULT_FAV + " TEXT,  " +
                CARD_EXPANDED + " INTEGER)";
    }

    public static class Planets implements BaseColumns {
        public static final String TABLE_NAME = "planets";
        public static final String COLUMN_ID = "_id";
        public static final String GAME_NAME = "game_name";
        public static final String UI_NAME = "ui_name";
        public static final String WAR_NAME = "war_name";
        public static final String TYPE = "type";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                GAME_NAME + " TEXT," +
                UI_NAME + " TEXT,  " +
                WAR_NAME + " TEXT,  " +
                TYPE + " TEXT)";
    }

    public static class Config implements BaseColumns {
        public static final String TABLE_NAME = "config";
        public static final String COLUMN_ID = "_id";
        public static final String SETTING = "setting";
        public static final String VALUE = "value";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SETTING + " TEXT," +
                VALUE + " TEXT)";
    }

    public static class TmpImageSelection implements BaseColumns {
        public static final String TABLE_NAME = "tmp_image_selection";
        public static final String IMAGE_NUMBER = "image_number";
        public static final String IMAGE_BLOB = "image_blob";
        public static final String IMAGE_SELECTED = "image_selected";
        public static final String IMAGE_LABEL = "image_label";
        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                IMAGE_NUMBER + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                IMAGE_BLOB + " BLOB, " +
                IMAGE_LABEL + " TEXT, " +
                IMAGE_SELECTED + " TEXT);";
    }


    public static class GameUnitTable {
        public static final String TABLE_NAME = "troops";
        public static final String COLUMN_ID = "_id";
        public static final String GAME_NAME = "game_name";
        public static final String UI_NAME = "ui_name";
        public static final String FACTION = "faction";
        public static final String CAPACITY = "cap";
        public static final String LEVEL = "level";
        public static final String CREATE_TABLE = "";
    }

    public static class TroopBaseData implements BaseColumns {
        public static final String TABLE_NAME = "troop_base_data";
        public static final String COLUMN_ID = "_id";
        public static final String GAME_NAME = "game_name";
        public static final String UI_NAME = "ui_name";
        public static final String FACTION = "faction";
        public static final String CAPACITY = "cap";
        public static final String MINLEVEL = "minLevel";
        public static final String MAXLEVEL = "maxLevel";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                GAME_NAME + " TEXT," +
                UI_NAME + " TEXT," +
                FACTION + " TEXT," +
                MINLEVEL + " INTEGER, " +
                MAXLEVEL + " INTEGER, " +
                CAPACITY + " INTEGER)";


    }

    public static class BuildingBaseData implements BaseColumns {
        public static final String TABLE_NAME = "building_base_data";
        public static final String COLUMN_ID = "_id";
        public static final String GAME_NAME = "game_name";
        public static final String UI_NAME = "ui_name";
        public static final String FACTION = "faction";
        public static final String CAPACITY = "cap";
        public static final String MINLEVEL = "minLevel";
        public static final String MAXLEVEL = "maxLevel";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                GAME_NAME + " TEXT," +
                UI_NAME + " TEXT," +
                FACTION + " TEXT," +
                MINLEVEL + " INTEGER, " +
                MAXLEVEL + " INTEGER, " +
                CAPACITY + " INTEGER)";


    }

    public static class TroopTable extends GameUnitTable implements BaseColumns {
        public static final String TABLE_NAME = "troops";
//        public static final String COLUMN_ID = "_id";
//        public static final String GAME_NAME = "game_name";
//        public static final String UI_NAME = "ui_name";
//        public static final String FACTION = "faction";
//        public static final String CAPACITY = "cap";
//        public static final String LEVEL = "level";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                GAME_NAME + " TEXT," +
                UI_NAME + " TEXT," +
                FACTION + " TEXT," +
                LEVEL + " INTEGER, " +
                CAPACITY + " INTEGER)";

    }

    public static class LayoutTagAssignmentTable implements BaseColumns {
        public static final String TABLE_NAME = "layout_tag_assignment";
        public static final String COLUMN_ID = "_id";
        public static final String LAYOUT_ID = "layout_id";
        public static final String TAG_ID = "tag_id";
        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                LAYOUT_ID + " INTEGER, " +
                TAG_ID + " INTEGER);";
    }

    public static class EquipmentTable extends GameUnitTable implements BaseColumns {
        public static final String TABLE_NAME = "armoury_equipment";
        //        public static final String COLUMN_ID = "_id";
//        public static final String GAME_NAME = "game_name";
//        public static final String UI_NAME = "ui_name";
//        public static final String FACTION = "faction";
//        public static final String CAPACITY = "cap";
//        public static final String LEVEL = "level";
        public static final String AVAILABLE_ON = "available_on";
        public static final String TYPE = "type";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                GAME_NAME + " TEXT," +
                UI_NAME + " TEXT," +
                FACTION + " TEXT," +
                TYPE + " TEXT," +
                AVAILABLE_ON + " TEXT," +
                LEVEL + " INTEGER, " +
                CAPACITY + " INTEGER)";
    }

    public static class Buildings extends GameUnitTable implements BaseColumns {
        public static final String TABLE_NAME = "buildings";
        public static final String ISTRAP = "isTrap";
        public static final String TYPE = "type";
        public static final String ISJUNK = "isJunk";
        public static final String GENERIC_NAME = "generic_name";
        public static final String CREATE_TABLE =
                "CREATE TABLE IF NOT EXISTS " +
                        TABLE_NAME + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        GAME_NAME + " TEXT," +
                        UI_NAME + " TEXT," +
                        FACTION + " TEXT," +
                        TYPE + " TEXT," +
                        ISJUNK + " TEXT," +
                        GENERIC_NAME + " TEXT, " +
                        ISTRAP + " INTEGER," +
                        LEVEL + " INTEGER, " +
                        CAPACITY + " INTEGER)";
    }


    public static class LayoutImages implements BaseColumns {
        public static final String TABLE_NAME = "layout_images";
        public static final String COLUMN_ID = "_id";
        public static final String LAYOUT_ID = "layout_id";
        public static final String IMAGE_BLOB = "image_blob";
        public static final String IMAGE_LOCATION = "image_location";
        public static final String IMAGE_LABEL = "image_label";
        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                LAYOUT_ID + " INTEGER, " +
                IMAGE_LABEL + " TEXT, " +
                IMAGE_LOCATION + " TEXT, " +
                IMAGE_BLOB + " BLOB);";
    }

    public static class LayoutImagesTmp extends LayoutImages {
        public static final String TABLE_NAME = "layout_images_tmp";
        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                LAYOUT_ID + " INTEGER, " +
                IMAGE_LABEL + " TEXT, " +
                IMAGE_LOCATION + " TEXT, " +
                IMAGE_BLOB + " BLOB);";
    }

    public static class LayoutTable implements BaseColumns {
        public static final String TABLE_NAME = "player_layouts";
        public static final String COLUMN_ID = "_id";
        public static final String LAYOUT_NAME = "layout_name";
        public static final String PLAYER_ID = "PLAYER_ID";
        public static final String LAYOUT_JSON = "layout_json";
        public static final String LAYOUT_TYPE = "layout_type";
        public static final String LAYOUT_TAG = "layout_tag";
        public static final String FACTION = "faction";
        public static final String LAYOUT_IMAGE = "layout_image";
        public static final String LAYOUT_ADDED = "layout_added";
        public static final String LAYOUT_FOLDER_ID = "layout_folder_id";
        public static final String LAYOUT_IS_FAVOURITE = "layout_is_favourite";
        public static final String LAYOUT_IMAGE_BLOB = "layout_image_blob";
        public static final String LAYOUT_VERSION_DEFAULT = "layout_version_default";
        public static final String LAYOUT_VERSION_DEFAULT_SET = "layout_version_default_set";
        public static final String LAYOUT_SELECTED = "layout_selected";


        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                LAYOUT_NAME + " TEXT, " +
                PLAYER_ID + " TEXT, " +
                LAYOUT_JSON + " TEXT, " +
                LAYOUT_TYPE + " INTEGER, " +
                LAYOUT_TAG + " INTEGER, " +
                FACTION + " TEXT, " +
                LAYOUT_IMAGE + " TEXT, " +
                LAYOUT_ADDED + " INTEGER, " +
                LAYOUT_FOLDER_ID + " INTEGER DEFAULT 0, " +
                LAYOUT_IS_FAVOURITE + " TEXT, " +
                LAYOUT_IMAGE_BLOB + " BLOB, " +
                LAYOUT_VERSION_DEFAULT + " INTEGER, " +
                LAYOUT_SELECTED + " INTEGER, " +
                LAYOUT_VERSION_DEFAULT_SET + " TEXT)";
    }

    public static final class LayoutTop implements BaseColumns {
        public static final String TABLE_NAME = "player_layout_top";
        public static final String COLUMN_ID = "_id";
        public static final String LAYOUT_ID = "layout_id";
        public static final String PLAYER_ID = "player_id";
        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                LAYOUT_ID + " INTEGER, " +
                PLAYER_ID + " TEXT); ";

    }

    public static class LayoutVersions implements BaseColumns {
        public static final String TABLE_NAME = "player_layout_versions";
        public static final String COLUMN_ID = "_id";
        public static final String LAYOUT_JSON = "layout_json";
        public static final String LAYOUT_VERSION = "layout_version";
        public static final String LAYOUT_ID = "layout_id";
        public static final String LAYOUT_IMAGE = "layout_image";
        public static final String LAYOUT_ADDED = "layout_added";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                LAYOUT_JSON + " TEXT, " +
                LAYOUT_VERSION + " INTEGER, " +
                LAYOUT_ID + " INTEGER, " +
                LAYOUT_IMAGE + " TEXT, " +
                LAYOUT_ADDED + " INTEGER)";


    }

    public static class LayoutTypes implements BaseColumns {
        public static final String TABLE_NAME = "layout_types";
        public static final String COLUMN_ID = "_id";
        public static final String LAYOUT_TYPE = "layout_type";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                LAYOUT_TYPE + " TEXT)";
    }


    public static class LayoutTags implements BaseColumns {
        public static final String TABLE_NAME = "layout_tags";
        public static final String COLUMN_ID = "_id";
        public static final String LAYOUT_TAG = "layout_tags";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                LAYOUT_TAG + " TEXT)";
    }

    public static class DatabaseUpgradeLog implements BaseColumns {
        public static final String TABLE_NAME = "database_upgrade_log";
        public static final String COLUMN_ID = "_id";
        public static final String DB_UPGRADE_VERSION = "db_upgrade_version";
        public static final String DB_UPGRADE_CODE = "db_upgrade_code";
        public static final String DB_UPGRADE_MESSAGE = "db_upgrade_message";
        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DB_UPGRADE_VERSION + " STRING, " +
                DB_UPGRADE_CODE + " STRING, " +
                DB_UPGRADE_MESSAGE + " STRING)";

    }

    public static class BundledValues implements BaseColumns {
        public static final String TABLE_NAME = "bundled_values";
        public static final String COLUMN_ID = "_id";
        public static final String BUNDLE_KEY = "bundle_key";
        public static final String BUNDLE_VALUE = "bundle_value";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BUNDLE_KEY + " TEXT, " +
                BUNDLE_VALUE + " TEXT)";
    }

    public static class ReleaseLog implements BaseColumns {
        public static final String TABLE_NAME = "release_log";
        public static final String COLUMN_ID = "_id";
        public static final String RELEASE_LABEL = "release_label";
        public static final String VERSION_CODE = "version_code";
        public static final String RELEASE_DATE = "release_date";
        public static final String NEW_FEATURES = "new_features";
        public static final String BUG_FIXES = "bug_fixes";
        public static final String READ = "read";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RELEASE_LABEL + " TEXT, " +
                VERSION_CODE + " INTEGER, " +
                NEW_FEATURES + " TEXT, " +
                BUG_FIXES + " TEXT, " +
                RELEASE_DATE + " INTEGER, " +
                READ + " INTEGER )";

    }


    public static class DataVersion implements BaseColumns {
        public static final String TABLE_NAME = "data_version";
        public static final String COLUMN_ID = "_id";
        public static final String APP_TABLE = "app_table";
        public static final String APP_TABLE_VERSION = "app_table_version";
        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                APP_TABLE + " TEXT, " +
                APP_TABLE_VERSION + " INTEGER)";
    }

    public static class DataVersionUpdateHistory implements BaseColumns {
        public static final String TABLE_NAME = "data_version";
        public static final String COLUMN_ID = "_id";
        public static final String APP_TABLE = "app_table";
        public static final String APP_TABLE_VERSION = "app_table_version";
        public static final String UPDATE_NOTES = "update_notes";
        public static final String APP_TABLE_UPDATED_ON = "app_table_updated_on";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                APP_TABLE + " TEXT, " +
                APP_TABLE_VERSION + " TEXT, " +
                UPDATE_NOTES + " TEXT, " +
                APP_TABLE_UPDATED_ON + " INTEGER)";
    }

    public class LayoutLog implements BaseColumns {
        public static final String TABLE_NAME = "layout_log";
        public static final String COLUMN_ID = "_id";
        public static final String LOG_DATE = "log_date";
        public static final String LAYOUT_ID = "layout_id";
        public static final String LAYOUT_VERSION = "layout_version";
        public static final String PLAYER_ID = "player_id";
        public static final String BASE_TYPE = "base_type";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                LOG_DATE + " INTEGER, " +
                LAYOUT_ID + " INTEGER, " +
                LAYOUT_VERSION + " INTEGER, " +
                PLAYER_ID + " TEXT, " +
                BASE_TYPE + " TEXT)";
    }

    public class LayoutFolders implements BaseColumns {
        public static final String TABLE_NAME = "layout_folders";
        public static final String COLUMN_ID = "_id";
        public static final String FOLDER_NAME = "folder_name";
        public static final String PARENT_FOLDER_ID = "parent_folder_id";
        public static final String CREATE_TABLE =
                "CREATE TABLE IF NOT EXISTS " +
                        TABLE_NAME + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        FOLDER_NAME + " TEXT, " +
                        PARENT_FOLDER_ID + " INTEGER DEFAULT 0)";
    }

    public class AppLog implements BaseColumns {
        public static final String TABLE_NAME = "app_log";
        public static final String COLUMN_ID = "_id";
        public static final String FUNCTION = "function";
        public static final String LOG = "log_data";
        public static final String LOG_DATE = "log_date";
        public static final String CREATE_TABLE =
                "CREATE TABLE IF NOT EXISTS " +
                        TABLE_NAME + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        FUNCTION + " TEXT, " +
                        LOG + " TEXT, " +
                        LOG_DATE + " INTEGER)";
    }

    public class SWC_HTTP_HEADERS implements BaseColumns {
        public static final String TABLE_NAME = "swc_http_headers";
        public static final String COLUMN_ID = "_id";
        public static final String HEADER_GROUP = "header_group";
        public static final String KEY = "_key";
        public static final String VALUE = "_value";
        public static final String CREATE_TABLE =
                "CREATE TABLE IF NOT EXISTS " +
                        TABLE_NAME + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        HEADER_GROUP + " TEXT, " +
                        KEY + " TEXT, " +
                        VALUE + " TEXT)";

    }

    public class VersionLog implements BaseColumns {
        public static final String TABLE_NAME = "versionlog";
        public static final String COLUMN_ID = "_id";
        public static final String VALUE = "value";
        public static final String CREATE_TABLE =
                "CREATE TABLE IF NOT EXISTS " +
                        TABLE_NAME + " (" +
                        COLUMN_ID + " INTEGER, " +
                        VALUE + " TEXT)";

    }

    public class BattleLog implements BaseColumns {
        public static final String TABLE_NAME = "battle_Log";
        public static final String COLUMN_ID = "_id";
        public static final String PLAYER_ID = "playerId";
        public static final String BATTLE_ID = "_battleId";
        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " ( " +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PLAYER_ID + " TEXT, " +
                BATTLE_ID + " TEXT);";
        public static final String INSERT_STMT = "INSERT INTO " + TABLE_NAME
                + "(" + PLAYER_ID + ", " + BATTLE_ID + ") VALUES(?, ?)";

    }

    public static class ConflictData implements BaseColumns {
        public static final String TABLE_NAME = "conflict_data";
        public static final String COLUMN_ID = "_id";
        public static final String UID = "uid";
        public static final String STARTINGRATING = "startingRating";
        public static final String STARTDATE = "startDate";
        public static final String PVPTEXTURENAME = "pvpTextureName";
        public static final String REWARDGROUPID = "rewardGroupId";
        public static final String PLANETID = "planetId";
        public static final String FOREGROUNDREBELTEXTURE = "foregroundRebelTextureName";
        public static final String FOREGROUNDEMPIRETEXTURENAME = "foregroundEmpireTextureName";
        public static final String ENDDATE = "endDate";
        public static final String BONUSENDTIME = "bonusEndTime";
        public static final String BACKGROUNDTEXTURENAME = "backgroundTextureName";
        public static final String[] DATACOLUMNS = {UID, STARTINGRATING, STARTDATE, PVPTEXTURENAME, REWARDGROUPID, PLANETID, FOREGROUNDEMPIRETEXTURENAME, FOREGROUNDREBELTEXTURE, ENDDATE,
                BONUSENDTIME, BACKGROUNDTEXTURENAME};
        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " ( " +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                UID + " TEXT UNIQUE, " +
                STARTINGRATING + " TEXT, " +
                STARTDATE + " TEXT, " +
                PVPTEXTURENAME + " TEXT, " +
                REWARDGROUPID + " TEXT, " +
                PLANETID + " TEXT, " +
                FOREGROUNDREBELTEXTURE + " TEXT, " +
                FOREGROUNDEMPIRETEXTURENAME + " TEXT, " +
                ENDDATE + " TEXT, " +
                BONUSENDTIME + " TEXT, " +
                BACKGROUNDTEXTURENAME + " TEXT " +
                ");";

    }

    public static class SWCMessageLog implements BaseColumns {
        public static final String TABLE_NAME = "swc_message_log";
        public static final String COLUMN_ID = "_id";
        public static final String FUNCTION = "app_function";
        public static final String MESSAGE = "message";
        public static final String RESPONSE = "response";
        public static final String DATELOGGED = "date_logged";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " ( " +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FUNCTION + " TEXT, " +
                MESSAGE + " TEXT, " +
                RESPONSE + " TEXT, " +
                DATELOGGED + " INTEGER)";
    }

    public static class LayoutEditor implements BaseColumns {
        public static final String TABLE_NAME = "layout_editor";
        public static final String COLUMN_ID = "_id";
        public static final String KEY = "_key";
        public static final String UID = "uid";
        public static final String X = "x";
        public static final String Z = "z";
        public static final String EDIT = "edit";


        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " ( " +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY + " TEXT, " +
                UID + " TEXT, " +
                X + " TEXT, " +
                Z + " TEXT, " +
                EDIT + " INTEGER)";


    }

    public static class ImageLoaderTest implements BaseColumns {
        public static final String TABLE_NAME = "image_loader_test";
        public static final String COLUMN_ID = "_id";
        public static final String IMG_URI = "img_uri";
        public static final String IMG_BLOB = "img_blob";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " ( " +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                IMG_URI + " TEXT, " +
                IMG_BLOB + " BLOB);";
    }
}
//    public class LayoutFolderAssignment implements BaseColumns {
//        public static final String TABLE_NAME = "layout_folder_assignment";
//        public static final String COLUMN_ID = "_id";
//        public static final String LAYOUT_ID = "layout_i d";
//        public static final String FOLDER_ID = "folder_id";
//        public static final String CREATE_TABLE =
//                "CREATE TABLE IF NOT EXISTS " +
//                        TABLE_NAME + " (" +
//                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                        LAYOUT_ID + " INTERGER, " +
//                        FOLDER_ID + " INTEGER)";
//    }


