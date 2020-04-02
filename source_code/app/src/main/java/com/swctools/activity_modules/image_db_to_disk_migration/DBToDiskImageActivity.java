package com.swctools.activity_modules.image_db_to_disk_migration;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.swctools.R;

import androidx.appcompat.app.AppCompatActivity;

public class DBToDiskImageActivity extends AppCompatActivity
implements DBToDiskFragmentActivityInterface{
    private static final String TAG = "DBToDiskImageActivity";
    private ImageView dbImage;
    private ImageView diskImage;
    private EditText editText;
    private TextView progressUpdate;

    private ProgressBar progressBarHoriz;
    private Button doMigration;

    private DBToDiskImageFragment dbToDiskImageFragment;
    private boolean clicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbto_disk_image);
        dbImage = findViewById(R.id.dbImage);
        diskImage = findViewById(R.id.diskImage);
        editText = findViewById(R.id.editText);
        doMigration = findViewById(R.id.doMigration);
        progressUpdate = findViewById(R.id.progressUpdate);
        progressBarHoriz = findViewById(R.id.progressBarHoriz);

        dbToDiskImageFragment = DBToDiskImageFragment.getInstance(getSupportFragmentManager());


        doMigration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!clicked) {
                    clicked = true;
                    dbToDiskImageFragment.doWork();
                }
            }
        });

    }


    public void sendProgress(int progress, int max){
        if(progressBarHoriz.getVisibility()!= View.VISIBLE){
            progressBarHoriz.setVisibility(View.VISIBLE);
        }
        progressUpdate.setText("Processed: "+ progress + " of " + max);
        progressBarHoriz.setMax(max);
        progressBarHoriz.setProgress(progress);

    }

    @Override
    public void taskEnded(Boolean result) {
        clicked=false;
        progressBarHoriz.setVisibility(View.GONE);
        if (result) {
            progressUpdate.setText("Completed successfully!");
        } else {
            progressUpdate.setText("Something went wrong!");
        }
    }

    @Override
    public void postImage(Bitmap bitmap) {
//        dbImage.setImageBitmap(bitmap);
    }
}
