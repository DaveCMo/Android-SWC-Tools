package com.swctools.activity_modules.logs.models;

import java.util.ArrayList;
import java.util.List;

public class DatabaseExpandableRow {
    private static final String TAG = "DatabaseExpandableRow";
    private String tableName;
    private List<String> tableRows;

    public DatabaseExpandableRow(String n, List<String> tRd){
        this.tableName = n;
        this.tableRows = new ArrayList<>();
        this.tableRows.addAll(tRd);
    }

    public String getTableName() {
        return tableName;
    }

    public List<String> getTableRows() {
        return tableRows;
    }
}
