package com.swctools.database.database_updates;

import android.content.Context;

public class DatabaseVersionUpdate {
    private static final String TAG = "DatabaseVersionUpdate";
    protected Context context;
    protected int newVersion;

    public DatabaseVersionUpdate(int newVersion, Context context) {
        this.newVersion = newVersion;
        this.context = context;
    }

    public void executeUpdates() {


    }
}
