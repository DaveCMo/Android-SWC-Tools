package com.swctools.activity_modules.say_thanks;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.swctools.R;
import com.swctools.util.ActivitySwitcher;
import com.swctools.util.StringUtil;

public class SayThanksActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_say_thanks);
        Toolbar toolbar = (Toolbar) findViewById(R.id.sayThanksToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("THANKS");
        TextView sayThanksText = (TextView) findViewById(R.id.sayThanksText);
        sayThanksText.setText(StringUtil.getHtmlForTxtBox(getString(R.string.thanks_text)));


        final TextView sayThanksLink = (TextView) findViewById(R.id.sayThanksLink);
        sayThanksLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = sayThanksLink.getText().toString();
                if (StringUtil.isStringNotNull(url)) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                }

            }
        });
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
