package com.swctools.layouts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;

import com.swctools.config.AppConfig;
import com.swctools.common.helpers.DateTimeHelper;
import com.swctools.common.helpers.ImageHelpers;
import com.swctools.common.model_list_providers.LayoutImageBytesListProvider;
import com.swctools.common.models.player_models.Building;
import com.swctools.common.models.player_models.MapBuildings;
import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;
import com.swctools.layouts.models.LayoutRecord;
import com.swctools.layouts.models.LayoutTag;
import com.swctools.layouts.models.LayoutVersion;
import com.swctools.activity_modules.multi_image_picker.models.SelectedImageModel;
import com.swctools.activity_modules.multi_image_picker.models.SelectedImagesDBHelper;
import com.swctools.util.MethodResult;
import com.swctools.util.StringUtil;

import java.io.FileOutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.UUID;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonReader;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class LayoutHelper {
    private static final String TAG = "LayoutHelper";
    public static final String YES = "YES";
    public static final String NO = "NO";


    private static MapBuildings newLayout;
    private static MapBuildings _currentLayout;


    public int dbId;
    public String layoutName;
    public String playerId;
    public String layoutFaction;
    public String layoutType;
    public String layoutTag;
    public String layoutImageURIStr;


    public LayoutHelper(int dbId, Context context) {
        this.dbId = dbId;
        Cursor cursor = null;
        String[] columns = {
                DatabaseContracts.LayoutTable.LAYOUT_NAME,
                DatabaseContracts.LayoutTable.PLAYER_ID,
                DatabaseContracts.LayoutTable.FACTION,
                DatabaseContracts.LayoutTable.LAYOUT_TYPE,
                DatabaseContracts.LayoutTable.LAYOUT_TAG,
                DatabaseContracts.LayoutTable.LAYOUT_IMAGE

        };
        String selection = DatabaseContracts.LayoutTable.COLUMN_ID + " like ?";
        String[] selectionArgs = {String.valueOf(dbId)};
        try {
            cursor = DBSQLiteHelper.queryDB(DatabaseContracts.LayoutTable.TABLE_NAME, columns, selection, selectionArgs, context);
            while (cursor.moveToNext()) {
                layoutName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutTable.LAYOUT_NAME));
                playerId = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutTable.PLAYER_ID));
                layoutFaction = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutTable.FACTION));
                layoutType = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutTable.LAYOUT_TYPE));
                layoutTag = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutTable.LAYOUT_TAG));
                layoutImageURIStr = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutTable.LAYOUT_IMAGE));
            }
        } catch (Exception e) {
            layoutName = "";

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static boolean updateLayoutName(int layoutId, String newName, Context context) {
        try {
            String whereStr = DatabaseContracts.LayoutTable.COLUMN_ID + " = ?";
            String[] whereArgs = {String.valueOf(layoutId)};
            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseContracts.LayoutTable.LAYOUT_NAME, newName);
            DBSQLiteHelper.updateData(DatabaseContracts.LayoutTable.TABLE_NAME, contentValues, whereStr, whereArgs, context);
        } catch (Exception e) {
            return false;
        }
        return true;


    }

    public static boolean updateLayoutFaction(int layoutId, String faction, Context context) {
        try {
            String whereStr = DatabaseContracts.LayoutTable.COLUMN_ID + " = ?";
            String[] whereArgs = {String.valueOf(layoutId)};
            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseContracts.LayoutTable.FACTION, faction);
            DBSQLiteHelper.updateData(DatabaseContracts.LayoutTable.TABLE_NAME, contentValues, whereStr, whereArgs, context);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean updateLayoutPlayerId(int layoutId, String playerId, Context context) {
        try {
            String whereStr = DatabaseContracts.LayoutTable.COLUMN_ID + " = ?";
            String[] whereArgs = {String.valueOf(layoutId)};
            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseContracts.LayoutTable.PLAYER_ID, playerId);
            DBSQLiteHelper.updateData(DatabaseContracts.LayoutTable.TABLE_NAME, contentValues, whereStr, whereArgs, context);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static String getLayoutJson(int layoutId, int versionID, Context context) {
        String layoutJson = "";
        Cursor cursor = null;
        String[] columns = {
                DatabaseContracts.LayoutVersions.LAYOUT_ID,
                DatabaseContracts.LayoutVersions.LAYOUT_VERSION,
                DatabaseContracts.LayoutVersions.LAYOUT_JSON};
        String selection = DatabaseContracts.LayoutVersions.LAYOUT_ID + " = ? AND " + DatabaseContracts.LayoutVersions.LAYOUT_VERSION + " = ? ";
        String[] selectionArgs = {String.valueOf(layoutId), String.valueOf(versionID)};
        try {
            cursor = DBSQLiteHelper.queryDB(DatabaseContracts.LayoutVersions.TABLE_NAME, columns, selection, selectionArgs, context);
            while (cursor.moveToNext()) {
                layoutJson = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutVersions.LAYOUT_JSON));


            }
        } catch (Exception e) {
            e.printStackTrace();
            layoutJson = "";
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return layoutJson;
    }

    public static int getMaxVersion(long LAYOUT_ID, Context context) {
        String[] columns = {"MAX(" + DatabaseContracts.LayoutVersions.LAYOUT_VERSION + ")"};
        String selection = DatabaseContracts.LayoutVersions.LAYOUT_ID + " = ?";
        String[] selectionArgs = {String.valueOf(LAYOUT_ID)};
        int maxVersion = 0;
        Cursor cursor = null;
        try {
            cursor = DBSQLiteHelper.queryDB(DatabaseContracts.LayoutVersions.TABLE_NAME, columns, selection, selectionArgs, context);
            while (cursor.moveToNext()) {
                maxVersion = cursor.getInt(0);
            }

        } catch (Exception e) {
            e.printStackTrace();
            maxVersion = 0;
        } finally {
            if (cursor != null) {
                cursor.close();
            }

        }
        return maxVersion;
    }

    public static String getLayoutTagName(int tagId, Context context) {

        String searchCol = DatabaseContracts.LayoutTags.COLUMN_ID;
        String returnColumn = DatabaseContracts.LayoutTags.LAYOUT_TAG;
        String searchTbl = DatabaseContracts.LayoutTags.TABLE_NAME;
        String tagName = "";
        Cursor cursor = null;
        String[] columns = {
                returnColumn};
        String selection = searchCol + " = ?";
        String[] selectionArgs = {String.valueOf(tagId)};
        try {
            cursor = DBSQLiteHelper.queryDB(searchTbl, columns, selection, selectionArgs, context);

            while (cursor.moveToNext()) {
                tagName = cursor.getString(cursor.getColumnIndexOrThrow(returnColumn));

            }
        } catch (Exception e) {
            tagName = "";
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return tagName;
    }

    public static String getLayoutTypeName(int typeId, Context context) {

        String searchCol = DatabaseContracts.LayoutTypes.COLUMN_ID;
        String returnColumn = DatabaseContracts.LayoutTypes.LAYOUT_TYPE;
        String searchTbl = DatabaseContracts.LayoutTypes.TABLE_NAME;
        String tagName = "";
        Cursor cursor = null;
        String[] columns = {
                returnColumn};
        String selection = searchCol + " = ?";
        String[] selectionArgs = {String.valueOf(typeId)};
        try {
            cursor = DBSQLiteHelper.queryDB(searchTbl, columns, selection, selectionArgs, context);
            while (cursor.moveToNext()) {
                tagName = cursor.getString(cursor.getColumnIndexOrThrow(returnColumn));

            }
        } catch (Exception e) {
            tagName = "";
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return tagName;
    }

    public static String getLayoutName(int dbId, Context context) {
        String layoutName = "";
        Cursor cursor = null;
        String[] columns = {
                DatabaseContracts.LayoutTable.LAYOUT_NAME};
        String selection = DatabaseContracts.LayoutTable.COLUMN_ID + " like ?";
        String[] selectionArgs = {String.valueOf(dbId)};
        try {
            cursor = DBSQLiteHelper.queryDB(DatabaseContracts.LayoutTable.TABLE_NAME, columns, selection, selectionArgs, context);
            while (cursor.moveToNext()) {
                layoutName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutTable.LAYOUT_NAME));

            }
        } catch (Exception e) {
            layoutName = "";
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return layoutName;
    }

    public static MethodResult newLayoutStr(MapBuildings currentLayout, String proposedLayoutStr, Context context) {

        MethodResult response;
        JsonReader jReader = Json.createReader(new StringReader(proposedLayoutStr));
        JsonArray array = jReader.readArray();
        _currentLayout = currentLayout;
        MapBuildings proposedLayout = new MapBuildings(array);
        newLayout = new MapBuildings();
//        if (proposedLayout.getBuildings().size() >= currentLayout.getBuildings().size()) {
        for (Building propBuilding : proposedLayout.getBuildings()) {
            if (!matchBuilding(propBuilding, context)) {
                //then time to try and
                try {
                    String replaceAble[] = Building.getReplaceableBuildingList().get(propBuilding.getBuildingProperties().getGenericName());
                    for (int i = 0; i < replaceAble.length; i++) {
                        Building propBuildingReplace = new Building(propBuilding.getKey(), replaceAble[i], propBuilding.getX(), propBuilding.getZ());
                        if (matchBuilding(propBuildingReplace, context)) {
                            break;
                        }
                    }
                } catch (Exception e) {
                    response = new MethodResult(false, e.getMessage());
                }
            }
        }

        response = new MethodResult(true, newLayout.asOutputJSON());
        return response;
    }

    private static boolean matchBuilding(Building propBuilding, Context context) {
        boolean matched = false;
        AppConfig appConfig = new AppConfig(context);

        int propBuildingLevel = propBuilding.getBuildingProperties().getLevel();
        int levelThreshold = appConfig.iLayoutLevelThreshold();
        //First 
        for (Building playerBuilding : _currentLayout.getBuildings()) {
            if (!playerBuilding.getAssigned()) {
                if (playerBuilding.getBuildingProperties().getGenericName().equals(propBuilding.getBuildingProperties().getGenericName()) &&
                        playerBuilding.getBuildingProperties().getLevel() == propBuildingLevel) {
                    playerBuilding.setAssigned(true);
                    playerBuilding.setX(propBuilding.getX());
                    playerBuilding.setZ(propBuilding.getZ());
                    Building newLayoutBuilding = new Building(playerBuilding.getKey(), playerBuilding.getUid(), propBuilding.getX(), propBuilding.getZ());
                    newLayout.getBuildings().add(newLayoutBuilding);
                    return true;
                }
            }
        }

        //second try to match by exact level:


        for (Building playerBuilding : _currentLayout.getBuildings()) {
            if (!playerBuilding.getAssigned()) {
                if (playerBuilding.getBuildingProperties().getGenericName().equals(propBuilding.getBuildingProperties().getGenericName()) &&
                        playerBuilding.getBuildingProperties().getLevel() == propBuildingLevel) {
                    playerBuilding.setAssigned(true);
                    playerBuilding.setX(propBuilding.getX());
                    playerBuilding.setZ(propBuilding.getZ());
                    Building newLayoutBuilding = new Building(playerBuilding.getKey(), playerBuilding.getUid(), propBuilding.getX(), propBuilding.getZ());
                    newLayout.getBuildings().add(newLayoutBuilding);
                    return true;
                }
            }
        }
        //then by +/- a level if not matched by exact:
        for (Building playerBuilding : _currentLayout.getBuildings()) {
            if (!playerBuilding.getAssigned()) {
                if (playerBuilding.getBuildingProperties().getGenericName().equals(propBuilding.getBuildingProperties().getGenericName()) &&
                        ((playerBuilding.getBuildingProperties().getLevel() - propBuildingLevel) <= levelThreshold)) {
                    playerBuilding.setAssigned(true);
                    playerBuilding.setX(propBuilding.getX());
                    playerBuilding.setZ(propBuilding.getZ());
                    Building newLayoutBuilding = new Building(playerBuilding.getKey(), playerBuilding.getUid(), propBuilding.getX(), propBuilding.getZ());
                    newLayout.getBuildings().add(newLayoutBuilding);
                    return true;
                }
            }
        }
        //finally any that are left
        for (Building playerBuilding : _currentLayout.getBuildings()) {
            if (!playerBuilding.getAssigned()) {
                if (playerBuilding.getBuildingProperties().getGenericName().equals(propBuilding.getBuildingProperties().getGenericName())) {

                    playerBuilding.setAssigned(true);
                    playerBuilding.setX(propBuilding.getX());
                    playerBuilding.setZ(propBuilding.getZ());
                    Building newLayoutBuilding = new Building(playerBuilding.getKey(), playerBuilding.getUid(), propBuilding.getX(), propBuilding.getZ());
                    newLayout.getBuildings().add(newLayoutBuilding);
                    return true;
                }
            }
        }

        return matched;
    }

    public static MethodResult saveNewLayout(String LAYOUT_NAME, String PLAYER_ID, String LAYOUT_JSON, String LAYOUT_FACTION, ArrayList<LayoutTag> layoutTags, int layoutFolder, @Nullable byte[] imageBytes, @Nullable Context context) {
        if (StringUtil.isStringNotNull(LAYOUT_JSON)) {
            long addedDate = (DateTimeHelper.userIDTimeStamp() / 1000);
            String imageFileName = UUID.randomUUID() + "_" + LAYOUT_NAME + ".jpg";
            Bitmap bitmap;
            bitmap = ImageHelpers.bytesToBitmap(imageBytes);
            String filePath = "";
            try {
                FileOutputStream fileOutputStream = context.openFileOutput(imageFileName, Context.MODE_PRIVATE);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fileOutputStream);
                filePath = context.getFilesDir() + "/" + imageFileName;

            } catch (Exception e) {
                e.printStackTrace();
            }
            long newLayoutID = 0;
            try {
                ContentValues newLayoutRecordValues = new ContentValues();
                newLayoutRecordValues.put(DatabaseContracts.LayoutTable.LAYOUT_NAME, LAYOUT_NAME);
                newLayoutRecordValues.put(DatabaseContracts.LayoutTable.LAYOUT_JSON, LAYOUT_JSON);
                newLayoutRecordValues.put(DatabaseContracts.LayoutTable.PLAYER_ID, PLAYER_ID);
                newLayoutRecordValues.put(DatabaseContracts.LayoutTable.FACTION, LAYOUT_FACTION);
                newLayoutRecordValues.put(DatabaseContracts.LayoutTable.LAYOUT_ADDED, addedDate);
                newLayoutRecordValues.put(DatabaseContracts.LayoutTable.LAYOUT_FOLDER_ID, layoutFolder);
                newLayoutRecordValues.put(DatabaseContracts.LayoutTable.LAYOUT_IMAGE, filePath);


                //INSERT IT!
                newLayoutID = DBSQLiteHelper.insertData(DatabaseContracts.LayoutTable.TABLE_NAME, newLayoutRecordValues, context);
                if (newLayoutID > 0) {
                    MethodResult saveVersion = saveNewLayoutVersion(LAYOUT_JSON, newLayoutID, context);
                    if (saveVersion.success) {
                        saveLayoutTags(layoutTags, context, newLayoutID);
                        //Add images:
                        ArrayList<SelectedImageModel> tmpImageSelectedModelList = LayoutImageBytesListProvider.getTmpImageSelectedModelList(context);
                        for (int i = 0; i < tmpImageSelectedModelList.size(); i++) {

                            bitmap = ImageHelpers.bytesToBitmap(tmpImageSelectedModelList.get(i).bytes);

                            try {
                                imageFileName = UUID.randomUUID() + "_" + LAYOUT_NAME + ".jpg";
                                FileOutputStream fileOutputStream = context.openFileOutput(imageFileName, Context.MODE_PRIVATE);
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fileOutputStream);
                                filePath = context.getFilesDir() + "/" + imageFileName;
                                ContentValues contentValues = new ContentValues();
                                contentValues.put(DatabaseContracts.LayoutImages.IMAGE_LOCATION, filePath);
                                contentValues.put(DatabaseContracts.LayoutImages.LAYOUT_ID, newLayoutID);
                                contentValues.put(DatabaseContracts.LayoutImages.IMAGE_LABEL, tmpImageSelectedModelList.get(i).label);
                                DBSQLiteHelper.insertData(DatabaseContracts.LayoutImages.TABLE_NAME, contentValues, context);
                                contentValues.clear();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        SelectedImagesDBHelper.clearTmp(context);
                        DBSQLiteHelper.deleteDbRows(DatabaseContracts.LayoutImagesTmp.TABLE_NAME, null, null, context);
                        return new MethodResult(true, "New layout '" + LAYOUT_NAME + "' saved!");
                    } else {
                        return saveVersion;
                    }
                } else {
                    return new MethodResult(false, "Layout not added!");
                }
            } catch (Exception e) {
                return new MethodResult(false, e.getMessage());
            }
        } else {
            return new MethodResult(false, "Layout JSON is empty!");
        }

    }


    public static void saveLayoutTags(ArrayList<LayoutTag> layoutTags, @Nullable Context context, long newLayoutID) {
        //Add tags:
        for (int i = 0; i < layoutTags.size(); i++) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseContracts.LayoutTagAssignmentTable.LAYOUT_ID, newLayoutID);
            contentValues.put(DatabaseContracts.LayoutTagAssignmentTable.TAG_ID, layoutTags.get(i).tagId);
            long newId = DBSQLiteHelper.insertData(DatabaseContracts.LayoutTagAssignmentTable.TABLE_NAME, contentValues, context);

        }
    }

    public static MethodResult setSelected(int layoutId, String selected, Context context) {
        String[] whereArgs = {String.valueOf(layoutId)};
        String whereClause = DatabaseContracts.LayoutTable.COLUMN_ID + " = ?";
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContracts.LayoutTable.LAYOUT_SELECTED, selected);
        int i = DBSQLiteHelper.updateData(DatabaseContracts.LayoutTable.TABLE_NAME, contentValues, whereClause, whereArgs, context);
        if (i > 0) {
            return new MethodResult(true, "");
        } else {
            return new MethodResult(false, "");
        }
    }

    public static MethodResult deleteLayoutRecord(int LAYOUT_ID, Context context) {
        //need to delete version and record values

        String[] whereArgs = {String.valueOf(LAYOUT_ID)};
        //Delete tag assignments:
        String tagsWhereClause = DatabaseContracts.LayoutTagAssignmentTable.LAYOUT_ID + " = ?";
        DBSQLiteHelper.deleteDbRows(DatabaseContracts.LayoutTagAssignmentTable.TABLE_NAME, tagsWhereClause, whereArgs, context);

        //Delete 'top layouts':
        String topWhereClause = DatabaseContracts.LayoutTop.LAYOUT_ID + " = ?";
        DBSQLiteHelper.deleteDbRows(DatabaseContracts.LayoutTop.TABLE_NAME, topWhereClause, whereArgs, context);

        //Delete images
        String imagesWhereClause = DatabaseContracts.LayoutImages.COLUMN_ID + " = ?";
        DBSQLiteHelper.deleteDbRows(DatabaseContracts.LayoutImages.TABLE_NAME, imagesWhereClause, whereArgs, context);

        //Delete from the logs:
        String layoutLogWhere = DatabaseContracts.LayoutLog.LAYOUT_ID + " = ?";
        DBSQLiteHelper.deleteDbRows(DatabaseContracts.LayoutLog.TABLE_NAME, layoutLogWhere, whereArgs, context);

        //Delete version(s):
        String versions_whereClause = DatabaseContracts.LayoutVersions.LAYOUT_ID + " = ?";
        DBSQLiteHelper.deleteDbRows(DatabaseContracts.LayoutVersions.TABLE_NAME, versions_whereClause, whereArgs, context);

        //Delete Layout
        String whereClause = DatabaseContracts.LayoutTable.COLUMN_ID + " = ?";
        DBSQLiteHelper.deleteDbRows(DatabaseContracts.LayoutTable.TABLE_NAME, whereClause, whereArgs, context);


        return new MethodResult(true, "Layout deleted!");

    }


    @NonNull
    public static MethodResult saveNewLayoutVersion(String LAYOUT_JSON, long LAYOUT_ID, Context context) {
        int LAYOUT_VERSION = getMaxVersion(LAYOUT_ID, context) + 1;

        long addedDate = (DateTimeHelper.userIDTimeStamp() / 1000);

        try {
            ContentValues layoutVersionValues = new ContentValues();
            layoutVersionValues.put(DatabaseContracts.LayoutVersions.LAYOUT_ID, LAYOUT_ID);
            layoutVersionValues.put(DatabaseContracts.LayoutVersions.LAYOUT_JSON, LAYOUT_JSON);
            layoutVersionValues.put(DatabaseContracts.LayoutVersions.LAYOUT_VERSION, LAYOUT_VERSION);
            layoutVersionValues.put(DatabaseContracts.LayoutVersions.LAYOUT_ADDED, addedDate);

            long addedRow = DBSQLiteHelper.insertData(DatabaseContracts.LayoutVersions.TABLE_NAME, layoutVersionValues, context);

            if (addedRow > 0) {
                //we added something..
                return new MethodResult(true, "Added new version (version " + LAYOUT_VERSION + ").");
            } else {
                return new MethodResult(false, new Exception("Layout version not added!").getMessage());
            }
        } catch (Exception e) {
            return new MethodResult(false, e.getMessage());
        } finally {
        }
    }


    public static MethodResult setFavourite(int layoutId, boolean isFav, Context context) {
        //first get what it is:
        try {
            String[] cols = {DatabaseContracts.LayoutTable.COLUMN_ID, DatabaseContracts.LayoutTable.LAYOUT_IS_FAVOURITE};
            String where = DatabaseContracts.LayoutTable.COLUMN_ID + " = ?";
            String[] args = {String.valueOf(layoutId)};
            ContentValues contentValues = new ContentValues();

            contentValues.put(DatabaseContracts.LayoutTable.LAYOUT_IS_FAVOURITE, isFav);
            DBSQLiteHelper.updateData(DatabaseContracts.LayoutTable.TABLE_NAME, contentValues, where, args, context);
            return new MethodResult(true, "Updated");
        } catch (Exception e) {
            return new MethodResult(false, e);
        }


    }


    public static MethodResult setFavourite(int layoutId, Context context) {
        //first get what it is:
        String[] cols = {DatabaseContracts.LayoutTable.COLUMN_ID, DatabaseContracts.LayoutTable.LAYOUT_IS_FAVOURITE};
        String where = DatabaseContracts.LayoutTable.COLUMN_ID + " = ?";
        String[] args = {String.valueOf(layoutId)};

        try {
            Cursor cursor = DBSQLiteHelper.queryDB(DatabaseContracts.LayoutTable.TABLE_NAME, cols, where, args, context);
            ContentValues contentValues = new ContentValues();

            String newVal;
            try {
                newVal = "";
                while (cursor.moveToNext()) {
                    String currValue = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutTable.LAYOUT_IS_FAVOURITE));
                    if (StringUtil.isStringNotNull(currValue)) {
                        if (currValue.equalsIgnoreCase(NO)) {
                            newVal = YES;
                        } else {
                            newVal = NO;
                        }
                    } else {
                        newVal = YES;
                    }
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
            contentValues.put(DatabaseContracts.LayoutTable.LAYOUT_IS_FAVOURITE, newVal);
            DBSQLiteHelper.updateData(DatabaseContracts.LayoutTable.TABLE_NAME, contentValues, where, args, context);
            return new MethodResult(true, newVal);
        } catch (Exception e) {
            return new MethodResult(false, e.getMessage());
        }

    }


    public static MethodResult markAsFavourite(int layoutId, Context context) {
        try {
            String where = DatabaseContracts.LayoutTable.COLUMN_ID + " = ?";
            String[] args = {String.valueOf(layoutId)};
            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseContracts.LayoutTable.LAYOUT_IS_FAVOURITE, YES);
            DBSQLiteHelper.updateData(DatabaseContracts.LayoutTable.TABLE_NAME, contentValues, where, args, context);
            return new MethodResult(true, "Marked as favourite!");
        } catch (Exception e) {
            return new MethodResult(false, e.getMessage());
        }
    }

    public static MethodResult setFavourite(int layoutId, String newValue, Context context) {
        try {
            String favouriteValue = newValue;
            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseContracts.LayoutTable.LAYOUT_IS_FAVOURITE, favouriteValue);
            String whereClause = DatabaseContracts.LayoutTable.COLUMN_ID + " = ?";
            String[] whereArgs = {String.valueOf(layoutId)};

            DBSQLiteHelper.updateData(DatabaseContracts.LayoutTable.TABLE_NAME, contentValues, whereClause, whereArgs, context);

            return new MethodResult(true, "Layout set as a favourite");
        } catch (Exception e) {
            return new MethodResult(false, e.getMessage());
        } finally {
        }

    }

    public static MethodResult deleteLayoutLog(int layoutId, int versionId, Context context) {
        try {
            String whereClause = DatabaseContracts.LayoutLog.LAYOUT_ID + " = ? AND " + DatabaseContracts.LayoutLog.LAYOUT_VERSION + " = ?";
            String[] whereargs = {String.valueOf(layoutId), String.valueOf(versionId)};
            DBSQLiteHelper.deleteDbRows(DatabaseContracts.LayoutLog.TABLE_NAME, whereClause, whereargs, context);
            return new MethodResult(true, "");
        } catch (Exception e) {
            return new MethodResult(false, "");
        }

    }

    public static MethodResult updateLayoutRecord(LayoutRecord layoutRecord, Context context) {

        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseContracts.LayoutTable.LAYOUT_NAME, layoutRecord.getLayoutName());
            contentValues.put(DatabaseContracts.LayoutTable.PLAYER_ID, layoutRecord.getLayoutPlayerId());
            contentValues.put(DatabaseContracts.LayoutTable.FACTION, layoutRecord.getLayoutFaction());
            contentValues.put(DatabaseContracts.LayoutTable.LAYOUT_VERSION_DEFAULT, layoutRecord.getDefaultLayoutVersion());
            contentValues.put(DatabaseContracts.LayoutTable.LAYOUT_VERSION_DEFAULT_SET, layoutRecord.getDefaultSet());
            contentValues.put(DatabaseContracts.LayoutTable.LAYOUT_IS_FAVOURITE, layoutRecord.getLayoutIsFavourite());

            String whereClause = DatabaseContracts.LayoutTable.COLUMN_ID + " = ? ";
            String[] whereArgs = {String.valueOf(layoutRecord.getLayoutId())};
            int updateResult = DBSQLiteHelper.updateData(DatabaseContracts.LayoutTable.TABLE_NAME, contentValues, whereClause, whereArgs, context);

            if (updateResult == 1) {
                return new MethodResult(true, "Layout updated!");
            } else {
                return new MethodResult(true, "Layout NOT updated!");
            }
        } catch (Exception e) {
            return new MethodResult(false, e.getMessage());
        }

    }

    public static MethodResult updateLayoutVersion(int layoutId, int layoutVersion, String layoutJSON, Context context) {
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseContracts.LayoutVersions.LAYOUT_JSON, layoutJSON);
            String whereClause = DatabaseContracts.LayoutVersions.LAYOUT_ID + " = ? AND " + DatabaseContracts.LayoutVersions.LAYOUT_VERSION + " = ?";
            String[] whereArgs = {String.valueOf(layoutId), String.valueOf(layoutVersion)};
            int updateResult = DBSQLiteHelper.updateData(DatabaseContracts.LayoutVersions.TABLE_NAME, contentValues, whereClause, whereArgs, context);
            if (updateResult == 1) {
                return new MethodResult(true, "Layout version updated!");
            } else {
                return new MethodResult(true, "Layout version NOT updated!");
            }

        } catch (Exception e) {
            return new MethodResult(false, e.getMessage());
        }


    }

    public static MethodResult updateLayoutFolder(int layoutId, int folderId, Context context) {
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseContracts.LayoutTable.LAYOUT_FOLDER_ID, folderId);
            String whereClause = DatabaseContracts.LayoutTable.COLUMN_ID + " = ?";
            String[] whereArgs = {String.valueOf(layoutId)};
            if (DBSQLiteHelper.updateData(DatabaseContracts.LayoutTable.TABLE_NAME, contentValues, whereClause, whereArgs, context) == 1) {
                return new MethodResult(true, "Layout Moved");
            } else {
                return new MethodResult(false, "Something went wrong updating the folder for this layout :(");
            }
        } catch (Exception e) {
            return new MethodResult(false, e.getMessage());
        }
    }

    public static void logLayoutUsed(int layoutId, int layoutVersion, String playerId, String baseType, Context context) {
        try {
            ContentValues contentValues = new ContentValues();
            long addedDate = (DateTimeHelper.userIDTimeStamp() / 1000);
            contentValues.put(DatabaseContracts.LayoutLog.LAYOUT_ID, layoutId);
            contentValues.put(DatabaseContracts.LayoutLog.LOG_DATE, addedDate);
            contentValues.put(DatabaseContracts.LayoutLog.LAYOUT_VERSION, layoutVersion);
            contentValues.put(DatabaseContracts.LayoutLog.PLAYER_ID, playerId);
            contentValues.put(DatabaseContracts.LayoutLog.BASE_TYPE, baseType);
            DBSQLiteHelper.insertData(DatabaseContracts.LayoutLog.TABLE_NAME, contentValues, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static MethodResult deleteLayoutVersion(int rowId, int layoutId, Context context) {
        try {
            String whereClause = DatabaseContracts.LayoutVersions.COLUMN_ID + " = ?";
            String[] whereArgs = {String.valueOf(rowId)};
            DBSQLiteHelper.deleteDbRows(DatabaseContracts.LayoutVersions.TABLE_NAME, whereClause, whereArgs, context);

            if (getCountVersions(layoutId, context) >= 1) {
            } else {
                deleteLayoutRecord(layoutId, context);
            }
            return new MethodResult(true, "Version deleted!");
        } catch (Exception e) {
            return new MethodResult(false, e.getMessage());
        }
    }

    public static int getCountVersions(int layoutId, Context context) {
        int versionCount = 0;
        Cursor cursor = null;
        String whereClause = DatabaseContracts.LayoutVersions.LAYOUT_ID + " = ?";
        String[] whereArgs = {String.valueOf(layoutId)};
        try {
            cursor = DBSQLiteHelper.queryDB(DatabaseContracts.LayoutVersions.TABLE_NAME, null, whereClause, whereArgs, context);
            versionCount = cursor.getCount();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (cursor != null) {
                cursor.close();
            }
        }
        return versionCount;

    }

    public static MethodResult updateTags(int layoutId, ArrayList<LayoutTag> layoutTagArrayList, Context context) {
        try {
            String twhereClause = DatabaseContracts.LayoutTagAssignmentTable.LAYOUT_ID + " = ? ";
            String[] twhereArgs = {String.valueOf(layoutId)};

            DBSQLiteHelper.deleteDbRows(DatabaseContracts.LayoutTagAssignmentTable.TABLE_NAME, twhereClause, twhereArgs, context);
            for (LayoutTag newLayoutTag : layoutTagArrayList) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(DatabaseContracts.LayoutTagAssignmentTable.LAYOUT_ID, layoutId);
                contentValues.put(DatabaseContracts.LayoutTagAssignmentTable.TAG_ID, newLayoutTag.tagId);
                DBSQLiteHelper.insertData(DatabaseContracts.LayoutTagAssignmentTable.TABLE_NAME, contentValues, context);
            }
            return new MethodResult(true, "Tags Updated!");
        } catch (Exception e) {
            return new MethodResult(false, e);
        }
    }

    public static MethodResult updateLayoutVersionJson(int layoutId, int layoutVersionId, String layoutJson, Context context) {
        String whereClause = DatabaseContracts.LayoutVersions.LAYOUT_ID + " = ? AND " + DatabaseContracts.LayoutVersions.LAYOUT_VERSION + " = ?";
        String[] whereArgs = {String.valueOf(layoutId), String.valueOf(layoutVersionId)};

        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseContracts.LayoutVersions.LAYOUT_JSON, layoutJson);
            int upct = DBSQLiteHelper.updateData(DatabaseContracts.LayoutVersions.TABLE_NAME, contentValues, whereClause, whereArgs, context);
            if (upct > 0) {
                return new MethodResult(true, "Layout JSON Updated");
            } else {
                return new MethodResult(false, "Layout JSON NOT UPDATED!");
            }
        } catch (Exception e) {
            return new MethodResult(false, e.getMessage());
        }

    }

    public static ArrayList<LayoutVersion> getLayoutVersions(int layoutId, Context context) {
        ArrayList<LayoutVersion> layoutVersions = new ArrayList<>();
        String[] projection = {
                DatabaseContracts.LayoutVersions.COLUMN_ID,
                DatabaseContracts.LayoutVersions.LAYOUT_VERSION,
                DatabaseContracts.LayoutVersions.LAYOUT_JSON,
                DatabaseContracts.LayoutVersions.LAYOUT_ID
        };
        String selection = DatabaseContracts.LayoutVersions.LAYOUT_ID + " = ? ";
        String[] selectionArgs = {String.valueOf(layoutId)};
        Cursor cursor = DBSQLiteHelper.queryDB(DatabaseContracts.LayoutVersions.TABLE_NAME, projection, selection, selectionArgs, context);
        layoutVersions = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                int rowId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutVersions.COLUMN_ID));
                int versID = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutVersions.LAYOUT_VERSION));
                layoutVersions.add(new LayoutVersion(rowId, versID, layoutId));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return layoutVersions;
    }

    public static int updateLayoutImagePath(int layoutId, String path, Context context) {

        String selection = DatabaseContracts.LayoutTable.COLUMN_ID + " = ? ";
        String[] selectionArgs = {String.valueOf(layoutId)};
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContracts.LayoutTable.LAYOUT_IMAGE, path);

        return DBSQLiteHelper.updateData(DatabaseContracts.LayoutTable.TABLE_NAME, contentValues, selection, selectionArgs, context);

    }

    public static int updateLayoutImageListItemPath(int id, String path, Context context) {
        String selection = DatabaseContracts.LayoutImages.COLUMN_ID + " = ? ";
        String[] selectionArgs = {String.valueOf(id)};
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContracts.LayoutImages.IMAGE_LOCATION, path);

        return DBSQLiteHelper.updateData(DatabaseContracts.LayoutImages.TABLE_NAME, contentValues, selection, selectionArgs, context);


    }

}
