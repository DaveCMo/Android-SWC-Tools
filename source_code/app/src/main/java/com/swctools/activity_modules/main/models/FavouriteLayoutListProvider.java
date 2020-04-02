package com.swctools.activity_modules.main.models;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;
import com.swctools.layouts.LayoutHelper;
import com.swctools.layouts.models.FavouriteLayoutItem;
import com.swctools.layouts.models.LastUsedLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class FavouriteLayoutListProvider {
    private static final String TAG = "FAVLAYOUTLISTPROVIDER";
    private static final String AUTO_FAV_SQL =
            "SELECT *, %1$s as %2$s " +
                    "FROM " + DatabaseContracts.LayoutLog.TABLE_NAME + " " +
                    "INNER JOIN " + DatabaseContracts.LayoutTable.TABLE_NAME + " " +
                    "ON " + DatabaseContracts.LayoutLog.TABLE_NAME + "." + DatabaseContracts.LayoutLog.LAYOUT_ID + " = " +
                    DatabaseContracts.LayoutTable.TABLE_NAME + "." + DatabaseContracts.LayoutTable.COLUMN_ID + " " +
                    "WHERE " + DatabaseContracts.LayoutLog.TABLE_NAME + "." + DatabaseContracts.LayoutLog.PLAYER_ID +
                    " LIKE  ? " +
                    "GROUP BY " +
                    DatabaseContracts.LayoutLog.LAYOUT_ID + ", " +
                    DatabaseContracts.LayoutLog.LAYOUT_VERSION + " " +
                    "ORDER BY %2$s DESC " +
                    "%3$s";

    public static List<LastUsedLayout> getLastUsedLayouts(String playerId, int limit, Context context) {
        List<LastUsedLayout> favouriteLayoutItemList = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append(LastUsedLayout.SQL());
        if (limit > 0) {
            sql.append("LIMIT " + limit);
        }
        Cursor cursor = null;
        try {
            cursor = DBSQLiteHelper.rawQuery(sql.toString(), new String[]{playerId}, context);
            while (cursor.moveToNext()) {
                int logId = cursor.getInt(0);
                int layoutId = cursor.getInt(1);
                int layoutVersion = cursor.getInt(2);
                String layoutName = cursor.getString(3);
                int lastUsed = cursor.getInt(4);

                favouriteLayoutItemList.add(new LastUsedLayout(layoutId, layoutVersion, layoutName, playerId, lastUsed));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return favouriteLayoutItemList;
    }

    public static List<FavouriteLayoutItem> autoFavouriteLayoutList(@Nullable String playerId, AutoType type, int limit, Context context) {
        List<FavouriteLayoutItem> favouriteLayoutItemList = new ArrayList<>();
        String aggregateField;
        String aggregateFieldLabel;
        if (type == AutoType.MOST_USED) {
            aggregateField = " COUNT( " + DatabaseContracts.LayoutLog.LAYOUT_ID + ")";
            aggregateFieldLabel = "count";
        } else {
            aggregateField = " MAX( " + DatabaseContracts.LayoutLog.LOG_DATE + ")";
            aggregateFieldLabel = "LastUsed";
        }
        String limitBy = "";
        if (limit > 0) {
            limitBy = "LIMIT " + limit;
        }
        String sql = String.format(AUTO_FAV_SQL, aggregateField, aggregateFieldLabel, limitBy);

        Cursor cursor = DBSQLiteHelper.rawQuery(String.format(AUTO_FAV_SQL, aggregateField, aggregateFieldLabel, limitBy), new String[]{playerId}, context);
        try {

            while (cursor.moveToNext()) {
                int favId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutLog.COLUMN_ID));
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutLog.LAYOUT_ID));
                int version = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutLog.LAYOUT_VERSION));
                String n = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutTable.LAYOUT_NAME));
                String imagePath = cursor.getString(cursor.getColumnIndex(DatabaseContracts.LayoutTable.LAYOUT_IMAGE));
                favouriteLayoutItemList.add(new FavouriteLayoutItem(favId, id, version, n, imagePath, FavouriteLayoutItem.LASTUSED));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return favouriteLayoutItemList;
    }

    public static List<FavouriteLayoutItem> selectedFavs(int limit, Context mContext) {
        List<FavouriteLayoutItem> favouriteLayoutItemList = new ArrayList<>();

        String[] columns = {DatabaseContracts.LayoutTable.COLUMN_ID, DatabaseContracts.LayoutTable.COLUMN_ID, DatabaseContracts.LayoutTable.LAYOUT_NAME, DatabaseContracts.LayoutTable.LAYOUT_IS_FAVOURITE, DatabaseContracts.LayoutTable.LAYOUT_IMAGE};
        String selection = DatabaseContracts.LayoutTable.LAYOUT_IS_FAVOURITE + " = ?";
        String[] selectionArgs = {LayoutHelper.YES};
        String limitBy = "";
        if (limit > 0) {
            limitBy = String.valueOf(limit);
        }
        Cursor cursor;
        cursor = DBSQLiteHelper.queryDB(DatabaseContracts.LayoutTable.TABLE_NAME, columns, selection, selectionArgs, null, null, null, limitBy, mContext);
        try {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutTable.COLUMN_ID));
                int version = LayoutHelper.getMaxVersion(id, mContext);
                String n = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutTable.LAYOUT_NAME));
                String image = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutTable.LAYOUT_IMAGE));
                favouriteLayoutItemList.add(new FavouriteLayoutItem(0, id, version, n, image, FavouriteLayoutItem.ALLFAV));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return favouriteLayoutItemList;
    }

    public enum AutoType {
        MOST_USED, LAST_USED;
    }


    public static ArrayList<FavouriteLayoutItem> getAllFavourites(Context context) {
        ArrayList<FavouriteLayoutItem> favouriteLayoutItemList = new ArrayList<>();
        String whereClause = DatabaseContracts.LayoutTable.LAYOUT_IS_FAVOURITE + " = ?";
        String[] whereArgs = {"1"};

        Cursor cursor = DBSQLiteHelper.queryDB(DatabaseContracts.LayoutTable.TABLE_NAME, null, whereClause, whereArgs, context);
        try {

            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutTable.COLUMN_ID));
                int version = LayoutHelper.getMaxVersion(id, context);
                String n = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutTable.LAYOUT_NAME));
                String image = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutTable.LAYOUT_IMAGE));
                favouriteLayoutItemList.add(new FavouriteLayoutItem(0, id, version, n, image, FavouriteLayoutItem.ALLFAV));
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return favouriteLayoutItemList;
    }


}
