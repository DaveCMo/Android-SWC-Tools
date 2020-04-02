package com.swctools.layouts.models;

import android.os.Parcel;
import android.os.Parcelable;

public class LayoutVersion implements Parcelable {
    private int rowId;
    private int versionId;
    private int layoutId;

    public LayoutVersion(int ROW_ID, int VERSION_ID, int layoutId) {
        this.rowId = ROW_ID;
        this.versionId = VERSION_ID;
        this.layoutId = layoutId;
    }

    protected LayoutVersion(Parcel in) {
        rowId = in.readInt();
        versionId = in.readInt();
        layoutId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(rowId);
        dest.writeInt(versionId);
        dest.writeInt(layoutId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LayoutVersion> CREATOR = new Creator<LayoutVersion>() {
        @Override
        public LayoutVersion createFromParcel(Parcel in) {
            return new LayoutVersion(in);
        }

        @Override
        public LayoutVersion[] newArray(int size) {
            return new LayoutVersion[size];
        }
    };

    public String toString() {
        return String.valueOf(versionId);
    }

    public int getLayoutId() {
        return layoutId;
    }

    public int getRowId() {
        return rowId;
    }

    public int getVersionId() {
        return versionId;
    }
}
