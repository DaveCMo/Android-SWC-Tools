package com.swctools.layouts.models;

public class LastUsedLayout {

    private int layoutId;
    private int versionId;
    private String layoutName;
    private String playerId;
    private int lastUsed;

    public LastUsedLayout(int layoutId, int versionId, String layoutName, String playerId, int lastUsed) {
        this.layoutId = layoutId;
        this.versionId = versionId;
        this.layoutName = layoutName;
        this.playerId = playerId;
        this.lastUsed = lastUsed;
    }

    public int getLayoutId() {
        return layoutId;
    }

    public int getVersionId() {
        return versionId;
    }

    public String getLayoutName() {
        return layoutName;
    }

    public String getPlayerId() {
        return playerId;
    }

    public int getLastUsed() {
        return lastUsed;
    }

    public static final String SQL() {

        return "SELECT \n" +
                "    layout_log._id,\n" +
                "    player_layouts._id AS 'layoutId',\n" +
                "    layout_version,\n" +
                "    MAX(log_date) AS LastUsed\n" +
                "FROM\n" +
                "    layout_log\n" +
                "        INNER JOIN\n" +
                "    player_layouts ON layout_log.layout_id = player_layouts._id\n" +
                "WHERE\n" +
                "    layout_log.player_id LIKE ?\n" +
                "GROUP BY layout_id , layout_version\n" +
                "ORDER BY LastUsed DESC \n";
    }

    @Override
    public String toString() {
        return "LastUsedLayout{" +
                "layoutId=" + layoutId +
                ", versionId=" + versionId +
                ", layoutName='" + layoutName + '\'' +
                ", playerId='" + playerId + '\'' +
                ", lastUsed=" + lastUsed +
                '}';
    }
}
