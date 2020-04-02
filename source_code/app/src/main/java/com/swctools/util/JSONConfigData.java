package com.swctools.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;

public class JSONConfigData {
    private static final String TAG = "JSONCONFIGDATA";
    private JsonObject _jsonOBj;
    private JsonArray _data;
    private int _version;
    private boolean _level;
    private JsonArray notesArray;

    public JSONConfigData(InputStream is) {
        _jsonOBj = Json.createReader(new StringReader(getJsonFromInputStream(is))).readObject();
        setData();


    }

    private void setData() {
        _version = _jsonOBj.getInt("version");
        _data = _jsonOBj.getJsonArray("data");
        _level = _jsonOBj.getBoolean("level");
        notesArray = _jsonOBj.getJsonArray("notes");
    }

    public JsonArray getNotesArray() {
        return notesArray;
    }

    public JSONConfigData(String jsonStr) {
        _jsonOBj = Json.createReader(new StringReader(jsonStr)).readObject();
        setData();
    }

    public JsonArray get_data() {
        return _data;
    }

    public boolean getLevel() {
        return _level;
    }

    private String getJsonFromInputStream(InputStream is) {
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];

        try {

            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }

        } catch (Exception e) {

        } finally {
            try {
                is.close();
            } catch (Exception e1) {

            }
        }
        return writer.toString();
    }
}
