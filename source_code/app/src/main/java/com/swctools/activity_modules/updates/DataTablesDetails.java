package com.swctools.activity_modules.updates;

import android.os.Parcel;
import android.os.Parcelable;

public class DataTablesDetails implements Parcelable {
    public String tableName;
    public String dataUrl;
    public int dataVersion;


    public DataTablesDetails(String tableName, String dataUrl, int dataVersion) {
        this.tableName = tableName;
        this.dataVersion = dataVersion;
        this.dataUrl = dataUrl;
    }

    public static final Creator<DataTablesDetails> CREATOR = new Creator<DataTablesDetails>() {
        @Override
        public DataTablesDetails createFromParcel(Parcel in) {
            return new DataTablesDetails(in);
        }

        @Override
        public DataTablesDetails[] newArray(int size) {
            return new DataTablesDetails[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public DataTablesDetails(Parcel in) {
        tableName = in.readString();
        dataUrl = in.readString();
        dataVersion = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(tableName);
        parcel.writeString(dataUrl);
        parcel.writeInt(dataVersion);
    }
}
