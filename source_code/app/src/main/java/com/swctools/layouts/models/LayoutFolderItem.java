package com.swctools.layouts.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.swctools.common.enums.ApplicationMessageTemplates;

public class LayoutFolderItem implements Parcelable {

    private int folderId;
    private String folderName;
    private int parentFolderId;
    private int countLayouts;

    public static final Parcelable.Creator<LayoutFolderItem> CREATOR = new Parcelable.Creator<LayoutFolderItem>(){
        @Override
        public LayoutFolderItem createFromParcel(Parcel in){
            return new LayoutFolderItem(in);
        }
        @Override
        public LayoutFolderItem[] newArray(int size){
            return new LayoutFolderItem[size];
        }
    };

//    public LayoutFolderItem (int id, String n, int pId){}

    public LayoutFolderItem (int id, String n, int parentId, int noLayouts){
        this.folderId = id;
        this.folderName = n;
        this.parentFolderId = parentId;
        this.countLayouts = noLayouts;
    }

    public int getFolderId() {
        return folderId;
    }

    public String getFolderName() {
        return folderName;
    }

    public int getParentFolderId() {
        return parentFolderId;
    }

    public String getCountLayoutsStr(){
        String s = String.format(ApplicationMessageTemplates.SEMI_COLON_ITEM.getemplateString(), "Layout", this.countLayouts);
        return s;
    }

    public int getCountLayouts() {
        return countLayouts;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(folderId);
        parcel.writeString(folderName);
        parcel.writeInt(parentFolderId);
        parcel.writeInt(countLayouts);


    }
    private LayoutFolderItem(Parcel in){
        this.folderId = in.readInt();
        this.folderName = in.readString();
        this.parentFolderId = in.readInt();
        this.countLayouts = in.readInt();

    }
}
