package com.swctools.activity_modules.player_config;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Toast;

import com.swctools.R;
import com.swctools.common.enums.BundleKeys;

public class PlayerConfig extends AppCompatActivity implements PlayerConfigFragment.PlayerConfigInterface {
    private static final String TAG = "PlayerConfig";
    private String playerId;
    private PlayerConfigFragment playerConfigFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_config);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        playerId = getIntent().getStringExtra(BundleKeys.PLAYER_ID.toString());
        Bundle args = new Bundle();
        args.putString(BundleKeys.PLAYER_ID.toString(), playerId);
        playerConfigFragment = (PlayerConfigFragment) getSupportFragmentManager().findFragmentById(R.id.PlayerConfigFragment);
        playerConfigFragment.setViews(playerId);
        setTitle("Player Configuration");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void copyText(String option, String str) {

        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("SWC TOOLS HITLIST", str);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "Text Copied!", Toast.LENGTH_LONG).show();
    }
}
