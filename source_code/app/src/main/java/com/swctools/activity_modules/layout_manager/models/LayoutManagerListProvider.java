package com.swctools.activity_modules.layout_manager.models;

import android.content.Context;
import android.database.Cursor;

import com.swctools.config.AppConfig;
import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;
import com.swctools.database.raw_sql.LayoutSQL;
import com.swctools.layouts.LayoutFolderHelper;
import com.swctools.layouts.models.LayoutRecord;
import com.swctools.layouts.models.LayoutTag;
import com.swctools.layouts.models.LayoutFolderItem;
import com.swctools.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class LayoutManagerListProvider {
    private static final String TAG = "LayoutList";
    private List<Object> baseList;
    private List<Object> displayList;
    private Context mContext;

    public LayoutManagerListProvider(Context context) {
        baseList = new ArrayList<>();
        this.mContext = context;
    }

    private List<LayoutFolderItem> getLayoutFolders(Context context) {
        List<LayoutFolderItem> layoutFolderItems = new ArrayList<>();
        layoutFolderItems.addAll(LayoutFolderHelper.layoutFolderItemList(context));
        return layoutFolderItems;
    }

    public static ArrayList<LayoutFolderItem> getAllLayoutFolders(int folder, Context context) {
        Cursor cursor = null;
        ArrayList<LayoutFolderItem> items = new ArrayList<>();
        LayoutFolderItem layoutFolderItem;

        String whereClause = DatabaseContracts.LayoutFolders.PARENT_FOLDER_ID + " = ? ";
        String[] whereArgs = {String.valueOf(folder)};

        try {
            cursor = DBSQLiteHelper.queryDB(DatabaseContracts.LayoutFolders.TABLE_NAME, null, whereClause, whereArgs, null, null, DatabaseContracts.LayoutFolders.FOLDER_NAME, null, context);
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(DatabaseContracts.LayoutFolders.COLUMN_ID));
                String folderName = cursor.getString(cursor.getColumnIndex(DatabaseContracts.LayoutFolders.FOLDER_NAME));
                int parentId = cursor.getInt(cursor.getColumnIndex(DatabaseContracts.LayoutFolders.PARENT_FOLDER_ID));
                layoutFolderItem = new LayoutFolderItem(id, folderName, parentId, 0);
                items.add(layoutFolderItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return items;

    }


    public static LayoutRecord getLayoutRecord(int id, Context context) {
        LayoutRecord layoutRecord = null;
        int layoutId = 0;
        String layoutPlayerId;
        String layoutName;
        String playerName;
        String layoutFaction;
        long layoutAdded;
        String layoutImageURIStr;
        int layoutFolderId;
        String layoutIsFavourite;
        String defaultSet;
        int defaultLayoutVersion;
        int countVersions;

        Cursor cursor = DBSQLiteHelper.rawQuery(LayoutSQL.layoutListSQLWithId(), new String[]{String.valueOf(id)}, context);

        try {
            int prevId = 0;
            while (cursor.moveToNext()) {
                int i = 0;
                layoutId = cursor.getInt(cursor.getColumnIndexOrThrow("layoutId"));
                layoutPlayerId = cursor.getString(cursor.getColumnIndexOrThrow("PLAYER_ID"));
                layoutName = cursor.getString(cursor.getColumnIndexOrThrow("layout_name"));
                playerName = cursor.getString(cursor.getColumnIndexOrThrow("playerName"));
                layoutFaction = cursor.getString(cursor.getColumnIndexOrThrow("faction"));
                layoutAdded = cursor.getInt(cursor.getColumnIndexOrThrow("layout_added"));
                layoutImageURIStr = cursor.getString(cursor.getColumnIndexOrThrow("layout_image"));
                layoutFolderId = cursor.getInt(cursor.getColumnIndexOrThrow("layout_folder_id"));
                layoutIsFavourite = cursor.getString(cursor.getColumnIndexOrThrow("layout_is_favourite"));
                defaultSet = cursor.getString(cursor.getColumnIndexOrThrow("layout_version_default_set"));
                countVersions = cursor.getInt(cursor.getColumnIndexOrThrow("Count"));
                defaultLayoutVersion = cursor.getInt(cursor.getColumnIndexOrThrow("Max"));
                int tagId = cursor.getInt(cursor.getColumnIndexOrThrow("tagId"));
                String tagName = cursor.getString(cursor.getColumnIndexOrThrow("tagName"));

                if (prevId == layoutId) {
                    if (tagId != 0) {
                        layoutRecord.addTag(new LayoutTag(tagId, tagName));
                    }

                } else {

                    layoutRecord = new LayoutRecord(
                            layoutId,
                            layoutName,
                            layoutPlayerId,
                            playerName,
                            layoutImageURIStr,
                            layoutAdded,
                            layoutFaction,
                            layoutFolderId,
                            layoutIsFavourite,
                            defaultSet,
                            defaultLayoutVersion,
                            countVersions);
                    if (tagId != 0) {

                        layoutRecord.addTag(new LayoutTag(tagId, tagName));
                    }
                }
                prevId = layoutId;

            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }

        return layoutRecord;
    }

    public static ArrayList<LayoutRecord> getLayoutLayoutList(int folder, Context context) {

        String layoutRAWSQL = LayoutSQL.layoutListSQL();
        Cursor cursor = DBSQLiteHelper.rawQuery(layoutRAWSQL, new String[]{String.valueOf(folder)}, context);

        return processLayoutCursor(cursor);
    }

    public static ArrayList<LayoutRecord> getAllLayouts(Context context ){
        String layoutRAWSQL = LayoutSQL.allLayoutsListSQL();
        Cursor cursor = DBSQLiteHelper.rawQuery(layoutRAWSQL, null, context);

        return processLayoutCursor(cursor);

    }

    public static ArrayList<LayoutRecord> getLayoutSearchResults( @Nullable String search, Context context) {
        ArrayList<LayoutRecord> layoutAdaptorList = new ArrayList<LayoutRecord>();
        AppConfig appConfig = new AppConfig(context);

        if (!StringUtil.isStringNotNull(search)) {
            search = "%";
        } else {
            search = "%" + search + "%";
        }

        String layoutRAWSQL = LayoutSQL.layoutListWithSearch();
        Cursor cursor = DBSQLiteHelper.rawQuery(layoutRAWSQL, new String[]{search, search, search, search}, context);


        return processLayoutCursor(cursor);
    }


    private static ArrayList<LayoutRecord> processLayoutCursor(Cursor cursor) {

        ArrayList<LayoutRecord> layoutAdaptorList = new ArrayList<LayoutRecord>();

        int layoutId = 0;
        String layoutPlayerId;
        String layoutName;
        String playerName;
        String layoutFaction;
        long layoutAdded;
        String layoutImageURIStr;
        int layoutFolderId;
        String layoutIsFavourite;
        String defaultSet;
        int defaultLayoutVersion;
        int countVersions;
        LayoutRecord layoutRecord = null;

        try {
            int prevId = 0;
            while (cursor.moveToNext()) {
                int i = 0;
                layoutId = cursor.getInt(cursor.getColumnIndexOrThrow("layoutId"));
                layoutPlayerId = cursor.getString(cursor.getColumnIndexOrThrow("PLAYER_ID"));
                layoutName = cursor.getString(cursor.getColumnIndexOrThrow("layout_name"));
                playerName = cursor.getString(cursor.getColumnIndexOrThrow("playerName"));
                layoutFaction = cursor.getString(cursor.getColumnIndexOrThrow("faction"));
                layoutAdded = cursor.getInt(cursor.getColumnIndexOrThrow("layout_added"));
                layoutImageURIStr = cursor.getString(cursor.getColumnIndexOrThrow("layout_image"));
                layoutFolderId = cursor.getInt(cursor.getColumnIndexOrThrow("layout_folder_id"));
                layoutIsFavourite = cursor.getString(cursor.getColumnIndexOrThrow("layout_is_favourite"));
                defaultSet = cursor.getString(cursor.getColumnIndexOrThrow("layout_version_default_set"));
                countVersions = cursor.getInt(cursor.getColumnIndexOrThrow("Max"));
                defaultLayoutVersion = cursor.getInt(cursor.getColumnIndexOrThrow("Count"));
                int tagId = cursor.getInt(cursor.getColumnIndexOrThrow("tagId"));
                String tagName = cursor.getString(cursor.getColumnIndexOrThrow("tagName"));

                if (prevId == layoutId) {
                    if (tagId != 0) {
                        layoutRecord.addTag(new LayoutTag(tagId, tagName));
                    }

                } else {

                    layoutRecord = new LayoutRecord(
                            layoutId,
                            layoutName,
                            layoutPlayerId,
                            playerName,
                            layoutImageURIStr,
                            layoutAdded,
                            layoutFaction,
                            layoutFolderId,
                            layoutIsFavourite,
                            defaultSet,
                            defaultLayoutVersion,
                            countVersions);
                    if (tagId != 0) {

                        layoutRecord.addTag(new LayoutTag(tagId, tagName));
                    }
                    layoutAdaptorList.add(layoutRecord);
                }
                prevId = layoutId;

            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return layoutAdaptorList;
    }

    public Cursor getLayoutListCursor(String playerId, String faction, String layoutType, String layoutTag, Context context) {

        String selection =
                getSelectionStr(DatabaseContracts.LayoutTable.PLAYER_ID, playerId) + " AND " +
                        getSelectionStr(DatabaseContracts.LayoutTable.FACTION, faction) + " AND " +
                        getSelectionStr(DatabaseContracts.LayoutTable.LAYOUT_TYPE, layoutType) + " AND " +
                        getSelectionStr(DatabaseContracts.LayoutTable.LAYOUT_TAG, layoutTag);


        String[] selectionArgs = {playerId, faction, layoutType, layoutTag};

        return DBSQLiteHelper.queryDB(DatabaseContracts.LayoutTable.TABLE_NAME, null, selection, selectionArgs, context);
    }

    private String getSelectionStr(String column, String value) {
        if (value.equals("%")) {
            return "(" + column + " like ? OR " + column + " IS NULL)";
        } else {
            return "(" + column + " like ?)";
        }//
    }

}
