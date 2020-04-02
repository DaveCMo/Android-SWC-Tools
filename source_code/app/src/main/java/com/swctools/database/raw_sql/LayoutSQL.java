package com.swctools.database.raw_sql;

public class LayoutSQL {

    public static String layoutListSQL() {
        String layoutRAWSQL = "SELECT \n" +
                "     player_layouts._id 'layoutId',\n" +
                "    player_layouts.PLAYER_ID,\n" +
                "    player_layouts.layout_name,\n" +
                "    players.playerName 'playerName',\n" +
                "    player_layouts.faction,\n" +
                "    player_layouts.layout_added,\n" +
                "    player_layouts.layout_image,\n" +
                "    player_layouts.layout_folder_id,\n" +
                "    player_layouts.layout_is_favourite,\n" +
                "    player_layouts.layout_version_default_set,\n" +
                "    (SELECT MAX(player_layout_versions.layout_version) FROM player_layout_versions where player_layout_versions.layout_id = player_layouts._id ) 'Max',\n" +
                "    (SELECT COUNT(player_layout_versions._id) FROM player_layout_versions where player_layout_versions.layout_id = player_layouts._id ) 'Count',\n" +
                "       layout_tags._id 'tagId',\n" +
                "    layout_tags.layout_tags 'tagName'\n" +
                "FROM\n" +
                "    player_layouts\n" +
                "        LEFT JOIN\n" +
                "    players ON player_layouts.PLAYER_ID = players.playerId\n" +
                "    left JOIN layout_tag_assignment ON \n" +
                "    layout_tag_assignment.layout_id = player_layouts._id\n" +
                "    LEFT JOIN layout_tags ON\n" +
                "    layout_tags._id = layout_tag_assignment.tag_id " +
                " WHERE player_layouts.layout_folder_id = ? ";
//                " LIMIT ? OFFSET ? ";
        return layoutRAWSQL;
//
    }

    public static String allLayoutsListSQL() {
        String layoutRAWSQL = "SELECT \n" +
                "     player_layouts._id 'layoutId',\n" +
                "    player_layouts.PLAYER_ID,\n" +
                "    player_layouts.layout_name,\n" +
                "    players.playerName 'playerName',\n" +
                "    player_layouts.faction,\n" +
                "    player_layouts.layout_added,\n" +
                "    player_layouts.layout_image,\n" +
                "    player_layouts.layout_folder_id,\n" +
                "    player_layouts.layout_is_favourite,\n" +
                "    player_layouts.layout_version_default_set,\n" +
                "    (SELECT MAX(player_layout_versions.layout_version) FROM player_layout_versions where player_layout_versions.layout_id = player_layouts._id ) 'Max',\n" +
                "    (SELECT COUNT(player_layout_versions._id) FROM player_layout_versions where player_layout_versions.layout_id = player_layouts._id ) 'Count',\n" +
                "       layout_tags._id 'tagId',\n" +
                "    layout_tags.layout_tags 'tagName'\n" +
                "FROM\n" +
                "    player_layouts\n" +
                "        LEFT JOIN\n" +
                "    players ON player_layouts.PLAYER_ID = players.playerId\n" +
                "    left JOIN layout_tag_assignment ON \n" +
                "    layout_tag_assignment.layout_id = player_layouts._id\n" +
                "    LEFT JOIN layout_tags ON\n" +
                "    layout_tags._id = layout_tag_assignment.tag_id ";
//                " LIMIT ? OFFSET ? ";
        return layoutRAWSQL;
//
    }

    public static String layoutListWithSearch(){
        String layoutRAWSQL = "SELECT \n" +
                "     player_layouts._id 'layoutId',\n" +
                "    player_layouts.PLAYER_ID,\n" +
                "    player_layouts.layout_name,\n" +
                "    players.playerName 'playerName',\n" +
                "    player_layouts.faction,\n" +
                "    player_layouts.layout_added,\n" +
                "    player_layouts.layout_image,\n" +
                "    player_layouts.layout_folder_id,\n" +
                "    player_layouts.layout_is_favourite,\n" +
                "    player_layouts.layout_version_default_set,\n" +
                "    (SELECT MAX(player_layout_versions.layout_version) FROM player_layout_versions where player_layout_versions.layout_id = player_layouts._id ) 'Max',\n" +
                "    (SELECT COUNT(player_layout_versions._id) FROM player_layout_versions where player_layout_versions.layout_id = player_layouts._id ) 'Count',\n" +
                "       layout_tags._id 'tagId',\n" +
                "    layout_tags.layout_tags 'tagName'\n" +
                "FROM\n" +
                "    player_layouts\n" +
                "        LEFT JOIN\n" +
                "    players ON player_layouts.PLAYER_ID = players.playerId\n" +
                "    left JOIN layout_tag_assignment ON \n" +
                "    layout_tag_assignment.layout_id = player_layouts._id\n" +
                "    LEFT JOIN layout_tags ON\n" +
                "    layout_tags._id = layout_tag_assignment.tag_id " +
                " WHERE  " +
                "(player_layouts.faction LIKE ? \n" +
                "OR playerName LIKE ? \n" +
                "OR player_layouts.layout_name LIKE ? \n" +
                "or layout_tags.layout_tags LIKE ?\n" +
                ")";
        /* +
                " LIMIT ? OFFSET ? ";*/
        return layoutRAWSQL;
    }

    public static String layoutListSQLWithId() {
        String layoutRAWSQL = "SELECT \n" +
                "     player_layouts._id 'layoutId',\n" +
                "    player_layouts.PLAYER_ID,\n" +
                "    player_layouts.layout_name,\n" +
                "    players.playerName 'playerName',\n" +
                "    player_layouts.faction,\n" +
                "    player_layouts.layout_added,\n" +
                "    player_layouts.layout_image,\n" +
                "    player_layouts.layout_folder_id,\n" +
                "    player_layouts.layout_is_favourite,\n" +
                "    player_layouts.layout_version_default_set,\n" +
                "    (SELECT MAX(player_layout_versions.layout_version) FROM player_layout_versions where player_layout_versions.layout_id = player_layouts._id ) 'Max',\n" +
                "    (SELECT COUNT(player_layout_versions._id) FROM player_layout_versions where player_layout_versions.layout_id = player_layouts._id ) 'Count',\n" +
                "       layout_tags._id 'tagId',\n" +
                "    layout_tags.layout_tags 'tagName'\n" +
                "FROM\n" +
                "    player_layouts\n" +
                "        LEFT JOIN\n" +
                "    players ON player_layouts.PLAYER_ID = players.playerId\n" +
                "    left JOIN layout_tag_assignment ON \n" +
                "    layout_tag_assignment.layout_id = player_layouts._id\n" +
                "    LEFT JOIN layout_tags ON\n" +
                "    layout_tags._id = layout_tag_assignment.tag_id" ;
        return layoutRAWSQL.concat(" WHERE  player_layouts._id " + " = ?");
    }
    public static String insertLayoutTagForTest(){
        return "INSERT INTO layout_tag_assignment (layout_id, tag_id) SELECT ?, tag_id FROM layout_tag_assignment WHERE layout_id = ?";
    }


    public static String layoutsWithImages(){
        return "SELECT _id, layout_name, layout_image_blob FROM player_layouts WHERE layout_image_blob is not null;";
    }

    public static String lastUsedLayoutsByPlayer(){
        return "SELECT \n" +
                "    layout_log._id,\n" +
                "    player_layouts._id as 'layout_id',\n" +
                "    layout_log.layout_version,\n" +
                "    player_layouts.layout_name,\n" +
                "    player_layouts.layout_image\n" +
                "FROM\n" +
                "    layout_log\n" +
                "        INNER JOIN\n" +
                "    player_layouts ON player_layouts._id = layout_log.layout_id\n" +
                "        INNER JOIN\n" +
                "    player_layout_versions ON player_layout_versions._id = layout_log.layout_version\n" +
                "WHERE\n" +
                "    layout_log.player_id = ? \n" +
                "ORDER BY layout_log.log_date DESC ";
    }
}
//    String layoutRAWSQL = "SELECT DISTINCT " +
//                DatabaseContracts.LayoutTable.TABLE_NAME + "." + DatabaseContracts.LayoutTable.COLUMN_ID + " 'layoutId', " +
//                DatabaseContracts.LayoutTable.TABLE_NAME + "." + DatabaseContracts.LayoutTable.PLAYER_ID + ", " +
//                DatabaseContracts.LayoutTable.TABLE_NAME + "." + DatabaseContracts.LayoutTable.LAYOUT_NAME + ", " +
//                DatabaseContracts.PlayersTable.TABLE_NAME + "." + DatabaseContracts.PlayersTable.PLAYERNAME + " 'playerName', " +
//                DatabaseContracts.LayoutTable.TABLE_NAME + "." + DatabaseContracts.LayoutTable.FACTION + ", " +
//                DatabaseContracts.LayoutTable.TABLE_NAME + "." + DatabaseContracts.LayoutTable.LAYOUT_ADDED + ", " +
//                DatabaseContracts.LayoutTable.TABLE_NAME + "." + DatabaseContracts.LayoutTable.LAYOUT_IMAGE + ", " +
//                DatabaseContracts.LayoutTable.TABLE_NAME + "." + DatabaseContracts.LayoutTable.LAYOUT_IMAGE_BLOB + ", " +
//                DatabaseContracts.LayoutTable.TABLE_NAME + "." + DatabaseContracts.LayoutTable.LAYOUT_FOLDER_ID + ", " +
//                DatabaseContracts.LayoutTable.TABLE_NAME + "." + DatabaseContracts.LayoutTable.LAYOUT_IS_FAVOURITE + ", " +
//                DatabaseContracts.LayoutTable.TABLE_NAME + "." + DatabaseContracts.LayoutTable.LAYOUT_VERSION_DEFAULT_SET + ", " +
//                "count(" + DatabaseContracts.LayoutVersions.TABLE_NAME + "." + DatabaseContracts.LayoutVersions.COLUMN_ID + ") 'countVers', " +
//                "max(" + DatabaseContracts.LayoutVersions.TABLE_NAME + "." + DatabaseContracts.LayoutVersions.COLUMN_ID + ") 'maxVers', " +
//                DatabaseContracts.LayoutTags.TABLE_NAME + "." + DatabaseContracts.LayoutTags.COLUMN_ID + " 'tagId', " +
//                DatabaseContracts.LayoutTags.TABLE_NAME + "." + DatabaseContracts.LayoutTags.LAYOUT_TAG + " 'tagName' " +
//                "FROM " + DatabaseContracts.LayoutTable.TABLE_NAME +
//                " INNER JOIN " + DatabaseContracts.LayoutVersions.TABLE_NAME + " ON " +
//                DatabaseContracts.LayoutTable.TABLE_NAME + "." + DatabaseContracts.LayoutTable.COLUMN_ID + " = " + DatabaseContracts.LayoutVersions.TABLE_NAME + "." + DatabaseContracts.LayoutVersions.LAYOUT_ID +
//                " LEFT JOIN " + DatabaseContracts.PlayersTable.TABLE_NAME + " ON " +
//                DatabaseContracts.LayoutTable.TABLE_NAME + "." + DatabaseContracts.LayoutTable.PLAYER_ID + " = " + DatabaseContracts.PlayersTable.TABLE_NAME + "." + DatabaseContracts.PlayersTable.PLAYERID +
//                " LEFT JOIN " + DatabaseContracts.LayoutTagAssignmentTable.TABLE_NAME + " ON " +
//                DatabaseContracts.LayoutTagAssignmentTable.TABLE_NAME + "." + DatabaseContracts.LayoutTagAssignmentTable.LAYOUT_ID + " = " + DatabaseContracts.LayoutTable.TABLE_NAME + "." + DatabaseContracts.LayoutTable.COLUMN_ID +
//                " LEFT JOIN " + DatabaseContracts.LayoutTags.TABLE_NAME + " ON " +
//                DatabaseContracts.LayoutTags.TABLE_NAME + "." + DatabaseContracts.LayoutTags.COLUMN_ID + " = " + DatabaseContracts.LayoutTagAssignmentTable.TABLE_NAME + "." + DatabaseContracts.LayoutTagAssignmentTable.TAG_ID +
//                " GROUP BY " + DatabaseContracts.LayoutTable.TABLE_NAME + "." + DatabaseContracts.LayoutTable.COLUMN_ID;
