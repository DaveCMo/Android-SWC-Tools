package com.swctools.activity_modules.updates;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.swctools.R;
import com.swctools.common.enums.BundleKeys;
import com.swctools.util.ActivitySwitcher;
import com.swctools.util.MethodResult;

import java.util.ArrayList;

public class UpdateDataActivity extends AppCompatActivity implements DataUpdateInterface {
    private static final String TAG = "UpdateDataActivity";
    private static final String PROGRESSMESSAGE = "PROGRESSMESSAGE";
    private static final String PROGRESSDETAIL = "PROGRESSDETAIL";
    private static final String PROGRESSINTERD = "PROGRESSINTERD";
    private static final String PROGRESSISUPDATING = "PROGRESSISUPDATING";
    private UpdateProcessor updateProcessor;
    private ArrayList<DataTablesDetails> dataTablesDetails;
    private TextView dataUpdateProgressTxt, dataUpdateDetailTxt;
    private ProgressBar dataUpdateProgressBar;
    private Button doDataUpdateBtn;
    private boolean isUpdating = false;
    private boolean finished = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_data);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setTitle("Data Update Utility");
        dataUpdateProgressBar = findViewById(R.id.dataUpdateProgressBar);
        dataUpdateProgressTxt = findViewById(R.id.dataUpdateProgressTxt);
        dataUpdateDetailTxt = findViewById(R.id.dataUpdateDetailTxt);
        doDataUpdateBtn = findViewById(R.id.doDataUpdateBtn);
        setSupportActionBar(toolbar);
        dataTablesDetails = getIntent().getParcelableArrayListExtra(BundleKeys.DATA_TABLES_TO_UPDATE.toString());


        updateProcessor = UpdateProcessor.getInstance(getSupportFragmentManager());


        doDataUpdateBtn.setOnClickListener(new btnClickLstnr());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        StringBuilder introText = new StringBuilder();
        introText.append("The following tables have updates waiting: \n");
        for (DataTablesDetails dataTablesDetail : dataTablesDetails) {
            introText.append(dataTablesDetail.tableName + "\n");

        }
        dataUpdateDetailTxt.setText(introText.toString());

        if (savedInstanceState != null) {
            dataUpdateProgressBar.setIndeterminate(savedInstanceState.getBoolean(PROGRESSINTERD));
            dataUpdateProgressTxt.setText(savedInstanceState.getString(PROGRESSMESSAGE));
            dataUpdateDetailTxt.setText(savedInstanceState.getString(PROGRESSDETAIL));
            isUpdating = savedInstanceState.getBoolean(PROGRESSISUPDATING);
        }


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(PROGRESSMESSAGE, dataUpdateProgressTxt.getText().toString());
        outState.putString(PROGRESSDETAIL, dataUpdateDetailTxt.getText().toString());
        outState.putBoolean(PROGRESSINTERD, dataUpdateProgressBar.isIndeterminate());
        outState.putBoolean(PROGRESSISUPDATING, isUpdating);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
        }
        return false;// super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (isUpdating) {
            updateProcessor.cancelUpdate();
            isUpdating = false;
        } else {
            finish();
            ActivitySwitcher.launchMainActivity(this);
            super.onBackPressed();
        }
    }

    @Override
    public void postUpdateToActivity(final String msg) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                dataUpdateProgressTxt.setText(msg);
            }
        });

    }

    @Override
    public void postErrorBacktoActivity(final Exception e) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                dataUpdateDetailTxt.setText(e.getLocalizedMessage());
            }
        });

    }

    public void updateCompleted(MethodResult methodResult) {
//        Toast.makeText(this, methodResult.getMessage(), Toast.LENGTH_LONG).show();
        isUpdating = false;
        if (methodResult.success) {
            dataUpdateProgressBar.setIndeterminate(false);
            dataUpdateProgressBar.setMax(1);
            dataUpdateProgressBar.setProgress(1);
            dataUpdateProgressTxt.setText("Done");
            dataUpdateDetailTxt.setText(methodResult.getMessage());
            doDataUpdateBtn.setText("Finished");
            finished = true;
        } else {

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                updateProcessor.cancelUpdate();
                isUpdating = false;
                onBackPressed();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    class btnClickLstnr implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (!finished) {
                dataUpdateProgressTxt.setText("Processing data updates.... ");
                dataUpdateProgressBar.setIndeterminate(true);
                isUpdating = true;
                updateProcessor.processUpdate(dataTablesDetails);
            } else {
                onBackPressed();
            }
        }
    }
}
