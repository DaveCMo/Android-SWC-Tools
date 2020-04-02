package com.swctools.activity_modules.multi_image_picker;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.swctools.R;
import com.swctools.activity_modules.multi_image_picker.models.SelectedImageModel;
import com.swctools.activity_modules.multi_image_picker.models.SelectedImagesDBHelper;
import com.swctools.activity_modules.multi_image_picker.view_adaptors.RecyclerAdaptor_ImageSelector;
import com.swctools.base.MessageTextViewInterface;
import com.swctools.common.enums.BundleKeys;
import com.swctools.common.enums.DatabaseMethods;
import com.swctools.common.helpers.ImageHelpers;
import com.swctools.common.popups.MessageTextViewFragment;
import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.UUID;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MultiImagePicker extends AppCompatActivity implements
        ImageSelectorRecyclerInterface,
        MessageTextViewInterface {
    //ACTIVITY STATIC VALUES
    private static final String TAG = "MultiImagePicker";
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final String CMD_EDIT_NAME = "CMD_EDIT_NAME";
    //Save state keys
    private static final String COUNTSELECTEDKEY = "COUNTSELECTEDKEY";
    private static final String ITEMSSELECTEDKEY = "ITEMSSELECTEDKEY";
    private static final String ITEMPOSITIONKEY = "ITEMPOSITIONKEY";
    //BUNDLE KEYS for following: int limit, int id, String table, String tableImageColumn, DatabaseMethods databaseMethods,
    public static final String IMAGE_LIMIT = "IMAGE_LIMIT";
    public static final String SOURCE_TABLE = "SOURCE_TABLE";
    public static final String SOURCE_TABLE_ID = "SOURCE_TABLE_ID";
    public static final String SOURCE_TABLE_ID_COL = "SOURCE_TABLE_ID_COL";
    public static final String SOURCE_TABLE_FIELD = "SOURCE_TABLE_FIELD";
    public static final String SOURCE_TABLE_LABEL = "SOURCE_TABLE_LABEL";
    public static final String DATABASE_METHOD = "DATABASE_METHOD";

    //UI
    private RecyclerView mutliImagePickerRecycler;
    private Button saveButton;
    private ImageView toolbarDelete;

    //RUNTIME VARIABLES
    ArrayList<SelectedImageModel> imageByteList;
    private RecyclerAdaptor_ImageSelector recyclerAdaptor_imageSelector;
    private boolean itemsSelected = false;
    private int countSelected = 0;
    private int itemPosition = 0;

    //Data source:
    private int image_limit;
    private String src_tbl;
    private int src_id;
    private String id_col;
    private String src_fld;
    private String src_lbl_fld;
    private DatabaseMethods databaseMethod;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_image_picker);

        final Context context = this;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mutliImagePickerRecycler = findViewById(R.id.mutliImagePickerRecycler);
        toolbarDelete = findViewById(R.id.toolbarDelete);
        saveButton = findViewById(R.id.saveButton);
        image_limit = getIntent().getIntExtra(IMAGE_LIMIT, 0);
        src_tbl = getIntent().getStringExtra(SOURCE_TABLE);
        src_id = getIntent().getIntExtra(SOURCE_TABLE_ID, 0);
        src_fld = getIntent().getStringExtra(SOURCE_TABLE_FIELD);
        src_lbl_fld = getIntent().getStringExtra(SOURCE_TABLE_LABEL);
        id_col = getIntent().getStringExtra(SOURCE_TABLE_ID_COL);

        final String dbM = getIntent().getStringExtra(DATABASE_METHOD);

        if (dbM.equalsIgnoreCase(DatabaseMethods.INSERT.name())) {
            databaseMethod = DatabaseMethods.INSERT;
        } else if (dbM.equalsIgnoreCase(DatabaseMethods.UPDATE.name())) {
            databaseMethod = DatabaseMethods.UPDATE;
        } else if(dbM.equalsIgnoreCase(DatabaseMethods.IMAGEPICK.name())) {
            databaseMethod = DatabaseMethods.IMAGEPICK;
        }

//        SelectedImagesDBHelper.fillWithImages(id_col, src_id, src_tbl, src_fld, this);


        if (savedInstanceState != null) {
            imageByteList = new ArrayList<>();
            imageByteList.addAll(SelectedImagesDBHelper.selectedImageModelArrayList(this));
            itemsSelected = savedInstanceState.getBoolean(ITEMSSELECTEDKEY);
            countSelected = savedInstanceState.getInt(COUNTSELECTEDKEY);
            itemPosition = savedInstanceState.getInt(ITEMPOSITIONKEY);
            if (itemsSelected) {
                toolbarDelete.setVisibility(View.VISIBLE);
            }
        } else {
            Intent getImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(getImageIntent, RESULT_LOAD_IMAGE);
            imageByteList = new ArrayList<>();
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mutliImagePickerRecycler.setLayoutManager(linearLayoutManager);
        recyclerAdaptor_imageSelector = new RecyclerAdaptor_ImageSelector(imageByteList, this);
        mutliImagePickerRecycler.setAdapter(recyclerAdaptor_imageSelector);
        recyclerAdaptor_imageSelector.notifyDataSetChanged();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageByteList.size() < image_limit) {
                    Intent getImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(getImageIntent, RESULT_LOAD_IMAGE);
                } else {
                    Snackbar.make(view, "You can only add " + image_limit + " image(s)", Snackbar.LENGTH_LONG).setAction("Already add the maximum number of images", null).show();
                }
            }
        });
        toolbarDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<SelectedImageModel> selectedImageModels = new ArrayList<>();

                for (int i = 0; i < imageByteList.size(); i++) {
                    if (!imageByteList.get(i).selected) {
                        selectedImageModels.add(imageByteList.get(i));
                    } else {
                        SelectedImagesDBHelper.deleteSelectedImage(imageByteList.get(i), context);
                    }
                }
                imageByteList.clear();
                imageByteList.addAll(selectedImageModels);
                recyclerAdaptor_imageSelector.notifyDataSetChanged();
                countSelected = 0;

                maintainSelected();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (databaseMethod.equals(DatabaseMethods.INSERT)) {


                    for (int i = 0; i < imageByteList.size(); i++) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(src_fld, imageByteList.get(i).bytes);
                        contentValues.put(id_col, src_id);
                        contentValues.put(src_lbl_fld, imageByteList.get(i).label);
                        DBSQLiteHelper.insertData(src_tbl, contentValues, context);
                    }
                } else if (databaseMethod.equals(DatabaseMethods.UPDATE)) {
                    for (int i = 0; i < imageByteList.size(); i++) {
                        Bitmap bitmap = ImageHelpers.bytesToBitmap(imageByteList.get(i).bytes);
                        String imageFileName = UUID.randomUUID() + "_" + ".jpg";
                        String whereClause = id_col + " = ?";
                        String[] whereArg = {String.valueOf(src_id)};
                        try {
                            FileOutputStream fileOutputStream = context.openFileOutput(imageFileName, Context.MODE_PRIVATE);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fileOutputStream);
                            String filePath = context.getFilesDir() + "/" + imageFileName;
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(src_fld, filePath);
                            DBSQLiteHelper.updateData(src_tbl, contentValues, whereClause, whereArg, context);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
//                    for (int i = 0; i < imageByteList.size(); i++) {
//                        String whereClause = id_col + " = ?";
//                        String[] whereArg = {String.valueOf(src_id)};
//                        ContentValues contentValues = new ContentValues();
//                        contentValues.put(src_fld, imageByteList.get(i).bytes);
//                        DBSQLiteHelper.updateData(src_tbl, contentValues, whereClause, whereArg, context);
//                    }
                } else if (databaseMethod.equals(DatabaseMethods.IMAGEPICK)) {
                    Log.d(TAG, "onClick: "+DatabaseMethods.IMAGEPICK);
                    for (int i = 0; i < imageByteList.size(); i++) {
                        Bitmap bitmap = ImageHelpers.bytesToBitmap(imageByteList.get(i).bytes);
                        String imageFileName = UUID.randomUUID() + "_" + ".jpg";
                        String whereClause = id_col + " = ?";
                        String[] whereArg = {String.valueOf(src_id)};
                        try {
                            FileOutputStream fileOutputStream = context.openFileOutput(imageFileName, Context.MODE_PRIVATE);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fileOutputStream);
                            String filePath = context.getFilesDir() + "/" + imageFileName;
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(src_fld, filePath);
                            contentValues.put(id_col, src_id);
                            DBSQLiteHelper.insertData(src_tbl, contentValues,  context);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }

                finish();
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            if (itemsSelected) {
                itemsSelected = false;
                for (int i = 0; i < imageByteList.size(); i++) {
                    imageByteList.get(i).selected = false;
                    recyclerAdaptor_imageSelector.notifyDataSetChanged();
                    countSelected = 0;
                }
            } else {
                onBackPressed();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
                Uri selectedImage = data.getData();
                Bitmap bp = ImageHelpers.decodeUriToBitmap(selectedImage, this, 400);
                byte[] bmBytes = ImageHelpers.bitmaptoByte(bp);
                long imageId = SelectedImagesDBHelper.addRecord(bmBytes, false, this);
                SelectedImageModel selectedImageModel = new SelectedImageModel(bmBytes, imageId);
                imageByteList.add(selectedImageModel);
                recyclerAdaptor_imageSelector.notifyDataSetChanged();
                saveButton.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(ITEMSSELECTEDKEY, itemsSelected);
        outState.putInt(COUNTSELECTEDKEY, countSelected);
        outState.putInt(ITEMPOSITIONKEY, itemPosition);
    }

    private void logSelected(int itemPosition) {
        imageByteList.get(itemPosition).selected = true;
        SelectedImagesDBHelper.updateSelected(imageByteList.get(itemPosition), this);
        recyclerAdaptor_imageSelector.notifyDataSetChanged();
        itemsSelected = true;
        countSelected++;
        toolbarDelete.setVisibility(View.VISIBLE);
    }


    @Override
    public void itemSelected(int itemPosition) {

        logSelected(itemPosition);
    }

    @Override
    public void itemShortPressed(int itemPosition) {
        if (itemsSelected) {
            logSelected(itemPosition);
        }
    }

    @Override
    public void itemDeselected(int itemPosition) {
        imageByteList.get(itemPosition).selected = false;
        SelectedImagesDBHelper.updateSelected(imageByteList.get(itemPosition), this);
        recyclerAdaptor_imageSelector.notifyDataSetChanged();
        countSelected--;
        maintainSelected();
    }

    @Override
    public void itemDelete(int itemPosition) {
        SelectedImagesDBHelper.deleteSelectedImage(imageByteList.get(itemPosition), this);
        imageByteList.remove(itemPosition);
        recyclerAdaptor_imageSelector.notifyDataSetChanged();
        countSelected--;
        maintainSelected();
    }

    private void maintainSelected() {
        if (countSelected == 0) {
            itemsSelected = false;
            toolbarDelete.setVisibility(View.GONE);
        }
        if (imageByteList.size() == 0) {
            saveButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void onTextViewDialogPositiveClick(String msg, String cmd) {
        imageByteList.get(itemPosition).label = msg;
        recyclerAdaptor_imageSelector.notifyDataSetChanged();
        SelectedImagesDBHelper.updateLabel(imageByteList.get(itemPosition), this);
    }

    private void callMessageTextViewFrag(String title, String message, String command, @Nullable String initialVal) {

        Bundle bundle = new Bundle();
        bundle.putString(BundleKeys.DIALOG_TITLE.toString(), title);
        bundle.putString(BundleKeys.DIALOG_MESSAGE.toString(), message);
        bundle.putString(BundleKeys.DIALOG_COMMAND.toString(), command);
        bundle.putString(BundleKeys.DIALOG_VALUE.toString(), initialVal);

        MessageTextViewFragment messageTextViewFragment = new MessageTextViewFragment();
        messageTextViewFragment.setArguments(bundle);
        messageTextViewFragment.show(getFragmentManager(), command);
    }

    @Override
    public void setLabel(int itemPosition) {
        this.itemPosition = itemPosition;
        callMessageTextViewFrag("Set Image Name", "", CMD_EDIT_NAME, imageByteList.get(itemPosition).label);

    }
}
