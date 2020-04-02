package com.swctools.activity_modules;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.swctools.R;
import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;
import com.swctools.common.helpers.ImageHelpers;
import com.swctools.util.ImageFilePath;

import java.io.File;

import androidx.appcompat.app.AppCompatActivity;

public class TestShit extends AppCompatActivity {
    private static final String TAG = "TestShit";
    private TextView msgTxt;
    private Button button;
    private ImageView selectedImageView, databaseImage, byteImage;
    private static final int RESULT_LOAD_IMAGE = 1;
    private Context context;
    private String imageString;
    private Uri selectedImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_test_shit);
        button = findViewById(R.id.button);
        selectedImageView = findViewById(R.id.selectedImageView);
        databaseImage = findViewById(R.id.databaseImage);
        byteImage = findViewById(R.id.byteImage);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLayoutImage();
            }
        });
    }

    public void getLayoutImage() {
        Intent getImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(getImageIntent, RESULT_LOAD_IMAGE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {

            this.selectedImage = data.getData();
            selectedImageView.setImageURI(selectedImage);
            try {
                imageString = ImageFilePath.getPath(context, selectedImage);

                ContentValues contentValues = new ContentValues();
                contentValues.put(DatabaseContracts.ImageLoaderTest.COLUMN_ID, 1);
                contentValues.put(DatabaseContracts.ImageLoaderTest.IMG_URI, imageString);
                DBSQLiteHelper.insertReplaceData(DatabaseContracts.ImageLoaderTest.TABLE_NAME, contentValues, context);
                String whereClause = DatabaseContracts.ImageLoaderTest.COLUMN_ID + " = ?";
                String[] whereArgs = {"1"};
                Cursor cursor = DBSQLiteHelper.queryDB(DatabaseContracts.ImageLoaderTest.TABLE_NAME, null, whereClause, whereArgs, context);
                while (cursor.moveToNext()) {
                    String dbImageString = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.ImageLoaderTest.IMG_URI));
                    File file = new File(dbImageString);
                    Uri imageUriFromDB = Uri.fromFile(file);
                    databaseImage.setImageURI(imageUriFromDB);
                    Bitmap bp = ImageHelpers.decodeUriToBitmap(imageUriFromDB, this, 400);
                    byte[] bmBytes = ImageHelpers.bitmaptoByte(bp);
                    Bitmap bitmap = ImageHelpers.bytesToBitmap(bmBytes);
                    byteImage.setImageBitmap(bitmap);
//
//                    long imageId = SelectedImagesDBHelper.addRecord(bmBytes, false, this);
//                    SelectedImageModel selectedImageModel = new SelectedImageModel(bmBytes, imageId);
//                    imageByteList.add(selectedImageModel);
//                    recyclerAdaptor_imageSelector.notifyDataSetChanged();
//                    saveButton.setVisibility(View.VISIBLE);


                }
            } catch (Exception e) {
                e.printStackTrace();
            }

//


        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }


    @Override
    protected void onStop() {
        super.onStop();

    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
