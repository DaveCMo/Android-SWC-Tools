package com.swctools.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.swctools.common.models.player_models.MapBuildings;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonArray;

public class ImportUtils {

    public static MethodResult processImportLayout(Intent resultData, Context context) {
        Uri uri = null;
        if (resultData != null) {
            uri = resultData.getData();
            try {
                InputStream in_s = context.getContentResolver().openInputStream(uri);
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in_s));
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                    reader.close();
                    try {
                        JsonArray readArray = Json.createReader(new StringReader(sb.toString())).readArray();
                        try {
                            MapBuildings layoutToLoad = new MapBuildings(readArray);
                            if (layoutToLoad.getBuildings().size() > 0) {
                                return new MethodResult(true, layoutToLoad.asOutputJSON());
                            } else {
                                return new MethodResult(false, "No buildings found in file!");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            return new MethodResult(false, "Not a layout file!");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        return new MethodResult(false, "Error with file!");
                    }
                } catch (IOException e) {
                    return new MethodResult(false, e.getMessage());

                }
            } catch (FileNotFoundException e) {
                return new MethodResult(false, e.getMessage());
            }
        } else {
            return new MethodResult(false, "You didn't select anything!");
        }
    }
}
