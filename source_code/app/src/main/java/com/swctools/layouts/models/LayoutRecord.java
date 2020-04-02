package com.swctools.layouts.models;

import android.content.ContentValues;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;
import com.swctools.layouts.LayoutHelper;
import com.swctools.util.StringUtil;

import java.util.ArrayList;

public class LayoutRecord implements Parcelable {
    private static final String TAG = "LayoutRecord";
    private int layoutId;
    private String layoutName;
    private String playerName;
    private String layoutPlayerId;
    private String layoutImageURIStr;
    private long layoutAdded;
    private String layoutFaction;
    private int layoutFolderId;
    private String layoutIsFavourite;
    private int countVersions;
    private ArrayList<LayoutTag> layoutTags;
    private String defaultSet;
    private int defaultLayoutVersion;
    private boolean selected = false;

    public ArrayList<LayoutTag> getLayoutTags() {
        return layoutTags;
    }


    public LayoutRecord(int layoutId, String layoutName, String layoutPlayerId, String playerName, String layoutImageURIStr, long layoutAdded, String layoutFaction, int layoutFolderId, String layoutIsFavourite, String defaultSet, int defaultLayoutVersion, int countVersions) {
        this.layoutId = layoutId;
        this.layoutName = layoutName;
        this.layoutPlayerId = layoutPlayerId;
        this.playerName = playerName;
        this.layoutImageURIStr = layoutImageURIStr;
        this.layoutAdded = layoutAdded;
        this.layoutFaction = layoutFaction;
        this.layoutFolderId = layoutFolderId;
        this.layoutIsFavourite = layoutIsFavourite;
        this.defaultSet = defaultSet;
        this.defaultLayoutVersion = defaultLayoutVersion;
        this.layoutTags = new ArrayList<>();
        this.countVersions = countVersions;
    }


    protected LayoutRecord(Parcel in) {
        layoutId = in.readInt();
        layoutName = in.readString();
        layoutPlayerId = in.readString();
        layoutImageURIStr = in.readString();
        layoutAdded = in.readLong();
        layoutFaction = in.readString();
        layoutFolderId = in.readInt();
        layoutIsFavourite = in.readString();
        layoutTags = in.readArrayList(LayoutTag.class.getClassLoader());
        defaultSet = in.readString();
        defaultLayoutVersion = in.readInt();
        countVersions = in.readInt();
        selected = Boolean.parseBoolean(in.readString());
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(layoutId);
        parcel.writeString(layoutName);
        parcel.writeString(layoutPlayerId);
        parcel.writeString(layoutImageURIStr);
        parcel.writeLong(layoutAdded);
        parcel.writeString(layoutFaction);
        parcel.writeInt(layoutFolderId);
        parcel.writeString(layoutIsFavourite);
        parcel.writeTypedList(layoutTags);
        parcel.writeString(defaultSet);
        parcel.writeInt(defaultLayoutVersion);
        parcel.writeInt(countVersions);
        parcel.writeString(String.valueOf(selected));

    }

    public static final Creator<LayoutRecord> CREATOR = new Creator<LayoutRecord>() {
        @Override
        public LayoutRecord createFromParcel(Parcel in) {
            return new LayoutRecord(in);
        }

        @Override
        public LayoutRecord[] newArray(int size) {
            return new LayoutRecord[size];
        }
    };


    public void addTag(LayoutTag layoutTag) {
        if (layoutTags == null) {
            layoutTags = new ArrayList<>();
        }
        layoutTags.add(layoutTag);
    }

    public void addTag(LayoutTag layoutTag, Context context) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseContracts.LayoutTagAssignmentTable.TAG_ID, layoutTag.tagId);
        contentValues.put(DatabaseContracts.LayoutTagAssignmentTable.LAYOUT_ID, this.layoutId);
        DBSQLiteHelper.insertData(DatabaseContracts.LayoutTagAssignmentTable.TABLE_NAME, contentValues, context);
    }

    public void removeTag(LayoutTag layoutTag, Context context) {
        String whereClause = DatabaseContracts.LayoutTagAssignmentTable.LAYOUT_ID + " = ? AND " + DatabaseContracts.LayoutTagAssignmentTable.TAG_ID + " = ?";
        String[] whereArgs = {String.valueOf(this.layoutId), String.valueOf(layoutTag.tagId)};
        DBSQLiteHelper.deleteDbRows(DatabaseContracts.LayoutTagAssignmentTable.TABLE_NAME, whereClause, whereArgs, context);
    }



    public long getLayoutAdded() {
        return layoutAdded;
    }

    public int getLayoutId() {
        return layoutId;
    }

    public String getLayoutName() {
        return layoutName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getLayoutPlayerId() {
        return layoutPlayerId;
    }

    public String getLayoutFaction() {
        return layoutFaction;
    }


    public String getImageURIStr() {
        return this.layoutImageURIStr;
    }


    public int getLayoutFolderId() {
        return layoutFolderId;
    }


    @Override
    public int describeContents() {
        return 0;
    }


    public String getLayoutIsFavourite() {
        if (StringUtil.isStringNotNull(this.layoutIsFavourite)) {
            return layoutIsFavourite;
        } else {
            return LayoutHelper.NO;
        }
    }

    public String getDefaultSet() {
        return defaultSet;
    }

    public int getDefaultLayoutVersion() {
        return defaultLayoutVersion;
    }

    public int getCountVersions() {
        return countVersions;
    }

    public void setLayoutFolderId(int layoutFolderId) {
        this.layoutFolderId = layoutFolderId;
    }

    public boolean containsString(String searchString) {
        boolean containsIt = false;
        if (layoutName.toLowerCase().contains(searchString.toLowerCase())) {
            containsIt = true;
        }

        if (getPlayerName().toLowerCase().contains(searchString.toLowerCase())) {
            containsIt = true;
        }

        if (layoutFaction.toLowerCase().contains(searchString.toLowerCase())) {
            containsIt = true;
        }

        for (int i = 0; i < layoutTags.size(); i++) {
            if (layoutTags.get(i).tagString.toLowerCase().contains(searchString.toLowerCase())) {
                containsIt = true;
            }
        }
        return containsIt;
    }

    public void setLayoutName(String layoutName) {
        this.layoutName = layoutName;
    }

    public void setLayoutTags(ArrayList<LayoutTag> layoutTags) {
        this.layoutTags.clear();
        this.layoutTags = layoutTags;
    }


    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }


    @Override
    public String toString() {
        return "LayoutRecord{" +
                "layoutId=" + layoutId +
                ", layoutName='" + layoutName + '\'' +
                ", playerName='" + playerName + '\'' +
                ", layoutPlayerId='" + layoutPlayerId + '\'' +
                ", layoutImageURIStr='" + layoutImageURIStr + '\'' +
                ", layoutAdded=" + layoutAdded +
                ", layoutFaction='" + layoutFaction + '\'' +
                ", layoutFolderId=" + layoutFolderId +
                ", layoutIsFavourite='" + layoutIsFavourite + '\'' +
                ", countVersions=" + countVersions +
                ", layoutTags=" + layoutTags +
                ", defaultSet='" + defaultSet + '\'' +
                ", defaultLayoutVersion=" + defaultLayoutVersion +
                ", selected=" + selected +
                '}';
    }
}
