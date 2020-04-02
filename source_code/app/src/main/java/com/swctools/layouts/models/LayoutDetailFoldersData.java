package com.swctools.layouts.models;

import java.util.ArrayList;

public class LayoutDetailFoldersData {
    private ArrayList<LayoutFolderItem> layoutFolderItems;

    public LayoutDetailFoldersData(ArrayList<LayoutFolderItem> items){
        this.layoutFolderItems = items;
    }

    public ArrayList<LayoutFolderItem> getLayoutFolderItems() {
        return layoutFolderItems;
    }
}
