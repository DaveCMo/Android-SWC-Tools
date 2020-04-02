package com.swctools.activity_modules.import_json;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

import com.swctools.R;
import com.swctools.common.enums.ScreenCommands.SaveLayoutInterface;
import com.swctools.common.models.player_models.MapBuildings;
import com.swctools.util.ActivitySwitcher;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonArray;

public class ImportJsonActivity extends AppCompatActivity {
    private static final String TAG = "ImportJsonActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_json);
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if (Intent.ACTION_SEND.equals(action) && type != null) {

            handleSharedLayout(intent);

        }
    }

    private void handleSharedLayout(Intent resultData) {
        try {
            Uri imageUri = (Uri) resultData.getParcelableExtra(Intent.EXTRA_STREAM);
            if (imageUri != null) {
                try {
                    InputStream in_s = this.getContentResolver().openInputStream(imageUri);
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
                                    ActivitySwitcher.launchSaveLayoutActivity_Save(layoutToLoad.asOutputJSON(), "", "", SaveLayoutInterface.SAVE, this);
                                } else {
                                    Toast.makeText(this, "Empty layout file", Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                                Toast.makeText(this, "Error reading layout file", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(this, "Error reading JSON", Toast.LENGTH_LONG).show();
                        }
                    } catch (IOException e) {
                        Toast.makeText(this, "Error reading file", Toast.LENGTH_LONG).show();
                    }
                } catch (FileNotFoundException e) {
                    Toast.makeText(this, "File not found!", Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "This is not the layout file you are looking for.", Toast.LENGTH_LONG).show();
        }
        finish();
    }


}