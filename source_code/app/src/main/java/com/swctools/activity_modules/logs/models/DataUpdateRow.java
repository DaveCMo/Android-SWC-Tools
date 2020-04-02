package com.swctools.activity_modules.logs.models;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;

public class DataUpdateRow {
    private static final String TAG = "DataUpdateRow";
    public int id;
    public String tbl;
    public int tbl_vers;
    public List<String> updateNotesList;
    public long updateOn;

    public DataUpdateRow(int id, String tbl, int tbl_vers, String updateNotes, long updateOn) {
        this.updateNotesList = new ArrayList<>();
        this.id = id;
        this.tbl = tbl;
        this.tbl_vers = tbl_vers;
        this.updateOn = updateOn;
        JsonArray updateNoteJArr = Json.createReader(new StringReader(updateNotes)).readArray();
        for (int i = 0; i < updateNoteJArr.size(); i++) {
            updateNotesList.add(updateNoteJArr.getJsonString(i).getString());
        }


    }
}
