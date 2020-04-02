package com.swctools.activity_modules;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.TextView;

import com.swctools.R;
import com.swctools.util.ActivitySwitcher;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ReleaseNotesActivity extends Activity {
    private TextView releaseNoteText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_notes);
        releaseNoteText = findViewById(R.id.releaseNoteText);
        try {
            String t = readTxt();

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                releaseNoteText.setText(Html.fromHtml(t, Html.FROM_HTML_MODE_LEGACY));
            } else {
                releaseNoteText.setText(Html.fromHtml(t));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String readTxt() {

        //getting the .txt file
        InputStream inputStream = getResources().openRawResource(R.raw.releasenote);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            int i = inputStream.read();
            while (i != -1) {
                byteArrayOutputStream.write(i);
                i = inputStream.read();
            }
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArrayOutputStream.toString();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) // Press Back Icon
        {
            finish();
        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            ActivitySwitcher.launchMainActivity(this);
        }
        return false;// super.onKeyDown(keyCode, event);
    }
}
