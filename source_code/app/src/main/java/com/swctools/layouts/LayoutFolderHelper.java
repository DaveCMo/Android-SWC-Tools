package com.swctools.layouts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;
import com.swctools.layouts.models.LayoutFolderItem;
import com.swctools.util.MethodResult;

import java.util.ArrayList;
import java.util.List;

public class LayoutFolderHelper {
    public static final String ROOTNAME = "Layouts";
    private static final String TAG = "LAYOUFOLDERHELPER";

    public static int getParentFolderId(int currentFldrId, Context context) {
        int parentFolder = 0;

        Cursor cursor = null;
        String[] columns = {DatabaseContracts.LayoutFolders.PARENT_FOLDER_ID};
        String selection = DatabaseContracts.LayoutFolders.COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(currentFldrId)};
        try {

            cursor = DBSQLiteHelper.queryDB(DatabaseContracts.LayoutFolders.TABLE_NAME, columns, selection, selectionArgs, context);

            while (cursor.moveToNext()) {
                parentFolder = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutFolders.PARENT_FOLDER_ID));
            }

        } catch (Exception e) {
            parentFolder = 0;
        } finally {

            if (cursor != null) {
                cursor.close();
            }
        }
        return parentFolder;
    }

    public static List<LayoutFolderItem> layoutFolderItemList(Context context) {

        Cursor cursor = null;
        List<LayoutFolderItem> layoutFolderItems = null;
        try {

            cursor = DBSQLiteHelper.queryDB(DatabaseContracts.LayoutFolders.TABLE_NAME, context);

            layoutFolderItems = new ArrayList<>();
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutFolders.COLUMN_ID));
                String n = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutFolders.FOLDER_NAME));
                int pid = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutFolders.PARENT_FOLDER_ID));
                int countLayouts = layoutsInFolder(id, context);
                layoutFolderItems.add(new LayoutFolderItem(id, n, pid, countLayouts));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (cursor != null) {
                cursor.close();
            }
        }
        return layoutFolderItems;
    }

    public static List<LayoutFolderItem> layoutFolderItemList(int parentFldrId, Context context) {

        Cursor cursor = null;
        String selection = DatabaseContracts.LayoutFolders.PARENT_FOLDER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(parentFldrId)};
        List<LayoutFolderItem> layoutFolderItems = new ArrayList<>();
        try {

            cursor = DBSQLiteHelper.queryDB(DatabaseContracts.LayoutFolders.TABLE_NAME, null, selection, selectionArgs, context);

            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutFolders.COLUMN_ID));
                String n = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutFolders.FOLDER_NAME));
                int pid = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutFolders.PARENT_FOLDER_ID));
                int countLayouts = layoutsInFolder(id, context);
                layoutFolderItems.add(new LayoutFolderItem(id, n, pid, countLayouts));
            }
        } catch (Exception e) {

        } finally {

            if (cursor != null) {
                cursor.close();
            }
        }

        return layoutFolderItems;
    }

    public static int layoutsInFolder(int folderId, Context context) {
        int countFolders = 0;

        Cursor cursor = null;
        try {

            String[] columns = {"count(" + DatabaseContracts.LayoutTable.COLUMN_ID + ")"};
            String selection = DatabaseContracts.LayoutTable.LAYOUT_FOLDER_ID + " = ?";
            String[] selectionArgs = {String.valueOf(folderId)};


            cursor = DBSQLiteHelper.queryDB(
                    DatabaseContracts.LayoutTable.TABLE_NAME, columns, selection, selectionArgs, DatabaseContracts.LayoutTable.LAYOUT_FOLDER_ID, null, null, null, context);


            while (cursor.moveToNext()) {
                countFolders = cursor.getInt(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (cursor != null) {
                cursor.close();
            }
        }
        return countFolders;
    }


    public static MethodResult deleteLayoutFolder(int folderId, Context context) {

        String folderWhereClause = DatabaseContracts.LayoutFolders.COLUMN_ID + " = ?";
        String[] folderWhereArgs = {String.valueOf(folderId)};
        //Delete Folder:
        DBSQLiteHelper.deleteDbRows(DatabaseContracts.LayoutFolders.TABLE_NAME, folderWhereClause, folderWhereArgs, context);
        //Delete assignment in layouts
        folderWhereClause = DatabaseContracts.LayoutTable.LAYOUT_FOLDER_ID + " = ? ";
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContracts.LayoutTable.LAYOUT_FOLDER_ID, 0);
        DBSQLiteHelper.updateData(DatabaseContracts.LayoutTable.TABLE_NAME, contentValues, folderWhereClause, folderWhereArgs, context);
        return new MethodResult(true, "");

    }

    public static MethodResult renameLayout(int folderId, String newname, Context context) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContracts.LayoutFolders.FOLDER_NAME, newname);
        String whereClause = DatabaseContracts.LayoutFolders.COLUMN_ID + " = ?";
        String[] whereArgs = {String.valueOf(folderId)};

        DBSQLiteHelper.updateData(DatabaseContracts.LayoutFolders.TABLE_NAME, contentValues, whereClause, whereArgs, context);
        return new MethodResult(true, "");
    }

    public static MethodResult assignLayoutToFolder(int layoutId, int folderId, Context context) {
        ContentValues contentValues = new ContentValues();
        String whereClause = DatabaseContracts.LayoutTable.COLUMN_ID + " = ?";
        String[] whereArgs = {String.valueOf(layoutId)};
        contentValues.put(DatabaseContracts.LayoutTable.LAYOUT_FOLDER_ID, folderId);

        try {
            if (DBSQLiteHelper.updateData(DatabaseContracts.LayoutTable.TABLE_NAME, contentValues, whereClause, whereArgs, context) > 0) {
                return new MethodResult(true, "Layout added to folder!");
            } else {
                return new MethodResult(false, "Layout not added");
            }
        } catch (Exception e) {
            return new MethodResult(false, e.getMessage());
        }
    }

    public static long addNewFolder(String folderName, int parentFldrId, Context context) {
        ContentValues c = new ContentValues();
        c.put(DatabaseContracts.LayoutFolders.FOLDER_NAME, folderName);
        c.put(DatabaseContracts.LayoutFolders.PARENT_FOLDER_ID, parentFldrId);
        try {
            long newId = DBSQLiteHelper.insertData(DatabaseContracts.LayoutFolders.TABLE_NAME, c, context);
            return newId;
        } catch (Exception e) {
            return 0;
        }
    }


    public static MethodResult moveFolder(int folderId, int newFolderId, Context context) {

        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseContracts.LayoutFolders.PARENT_FOLDER_ID, newFolderId);
            String whereClause = DatabaseContracts.LayoutFolders.COLUMN_ID + " = ?";
            String[] whereArgs = {String.valueOf(folderId)};
            if (DBSQLiteHelper.updateData(DatabaseContracts.LayoutFolders.TABLE_NAME, contentValues, whereClause, whereArgs, context) == 1) {
                return new MethodResult(true, "Folder moved");
            } else {
                return new MethodResult(false, "Failed to move the folder :/");
            }
        } catch (Exception e) {
            return new MethodResult(false, e.getMessage());
        }

    }

    public static LayoutFolderItem getLayoutFolder(int folderId, Context context) {
        LayoutFolderItem layoutFolderItem = null;

        if (folderId == 0) {
            layoutFolderItem = new LayoutFolderItem(0, ROOTNAME, 0, layoutsInFolder(folderId, context));
        } else {
            String whereClause = DatabaseContracts.LayoutFolders.COLUMN_ID + " = ?";
            String[] whereArgs = {String.valueOf(folderId)};
            Cursor cursor = null;
            try {

                cursor = DBSQLiteHelper.queryDB(DatabaseContracts.LayoutFolders.TABLE_NAME, null, whereClause, whereArgs, context);

                while (cursor.moveToNext()) {
                    int parent = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutFolders.PARENT_FOLDER_ID));
                    String fldName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutFolders.FOLDER_NAME));
                    int countLayouts = layoutsInFolder(folderId, context);
                    layoutFolderItem = new LayoutFolderItem(folderId, fldName, parent, countLayouts);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        return layoutFolderItem;


    }

    public static ArrayList<LayoutFolderItem> getFolderHeirarchy(int folderId, Context context) {
        ArrayList<LayoutFolderItem> layoutFolderItems = new ArrayList<>();

        //First add the lowest level folder.
        if (folderId > 0) {
            LayoutFolderItem originalFolder = getLayoutFolder(folderId, context);
            layoutFolderItems.add(originalFolder);
            LayoutFolderItem tmpFolder = originalFolder;
            try {
                while (tmpFolder.getParentFolderId() > 0) {
                    tmpFolder = getLayoutFolder(tmpFolder.getParentFolderId(), context);
                    layoutFolderItems.add(tmpFolder);
                }
            } catch (Exception e) {
            }
        }
        layoutFolderItems.add(new LayoutFolderItem(0, "Layouts", 0, 0));
        ArrayList<LayoutFolderItem> reversedList = new ArrayList<>();
        for (int i = layoutFolderItems.size() - 1; i >= 0; i--) {
            reversedList.add(layoutFolderItems.get(i));
        }
        return reversedList;
    }

}
