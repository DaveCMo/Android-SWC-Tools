package com.swctools.activity_modules.layout_manager;

import com.swctools.layouts.models.LayoutRecord;

public interface LayoutListInterface {
//    void applySelectedLayout(int layoutId, int versId);
//
//    void applySelectedLayoutWar(int layoutId, int versId);
//
////        void editSelectedLayout(int dbId);

    void deleteSelectedLayout(int dbId);

    void loadLayout(LayoutRecord layoutRecord);

    void renameFolder(int folderId);

    void deleteFolder(int folderId);

    void moveFolder(int folderId);

    void folderSelected(int folderId);

    void moveLayoutToNewFolder(int layoutId, int folderId);

    void markFavourite(int layoutId, int position, String favouriteVal);

    void layoutSelected(int layoutId, int position);
    void layoutDeSelected(int layoutId, int position);
}
