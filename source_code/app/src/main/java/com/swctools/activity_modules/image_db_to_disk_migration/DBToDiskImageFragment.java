package com.swctools.activity_modules.image_db_to_disk_migration;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.swctools.common.helpers.ImageHelpers;
import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;
import com.swctools.database.raw_sql.LayoutSQL;
import com.swctools.layouts.LayoutHelper;
import com.swctools.util.Utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class DBToDiskImageFragment extends Fragment {
    private DBToDiskFragmentActivityInterface activityInterface;
    private MigrateImages migrateImages;
    private Context context;
    private static final String TAG = "DBToDiskImageFragment";


    public static DBToDiskImageFragment getInstance(FragmentManager fragmentManager) {
        DBToDiskImageFragment fragment = (DBToDiskImageFragment) fragmentManager.findFragmentByTag(TAG);
        if (fragment == null) {

            fragment = new DBToDiskImageFragment();

            fragmentManager.beginTransaction().add(fragment, TAG).commit();
        }
        return fragment;
    }


    private DBToDiskImageFragment() {
        setRetainInstance(true);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        activityInterface = (DBToDiskFragmentActivityInterface) context;

    }


    @Override
    public void onDetach() {
        super.onDetach();
        context = null;
        activityInterface = null;

    }

    public void doWork() {
        if (migrateImages != null) {
            migrateImages.cancel(true);
        }

        migrateImages = new MigrateImages();
        migrateImages.execute();
    }

    private class MigrateImages extends AsyncTask<Void, Integer, Boolean> {
        private int noToDo = 0;
        private FileOutputStream fileOutputStream = null;
        private FileInputStream fileInputStream = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Cursor cursor = DBSQLiteHelper.rawQuery(LayoutSQL.layoutsWithImages(), null, context);
            Cursor cursor1 = DBSQLiteHelper.queryDB(DatabaseContracts.LayoutImages.TABLE_NAME, context);
            Utils.printCurorContents(cursor1);
            cursor1 = DBSQLiteHelper.queryDB(DatabaseContracts.LayoutImages.TABLE_NAME, context);
            noToDo = cursor.getCount() + cursor1.getCount();
            int i = 1;
            try {
                while (cursor.moveToNext()) {
                    if (!isCancelled()) {
                        int layoutId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutTable.COLUMN_ID));
                        String layoutName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutTable.LAYOUT_NAME));
                        byte[] layoutImageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutTable.LAYOUT_IMAGE_BLOB));
                        Bitmap bitmap = ImageHelpers.bytesToBitmap(layoutImageBytes);
                        String imageFileName = layoutId + "_" + layoutName + ".jpg";
                        try {
                            fileOutputStream = context.openFileOutput(imageFileName, Context.MODE_PRIVATE);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fileOutputStream);
                            String filePath = context.getFilesDir() + "/" + imageFileName;
                            int result = LayoutHelper.updateLayoutImagePath(layoutId, filePath, context);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        publishProgress(i);
                        i++;
                    } else {
                        return false;
                    }

                }

                //now do the sub images:

                while (cursor1.moveToNext()) {
                    if (!isCancelled()) {
                        int rowId = cursor1.getInt(cursor1.getColumnIndexOrThrow(DatabaseContracts.LayoutImages.COLUMN_ID));

                        byte[] layoutSubImageBytes = cursor1.getBlob(cursor1.getColumnIndexOrThrow(DatabaseContracts.LayoutImages.IMAGE_BLOB));
                        if (layoutSubImageBytes != null) {
                            Bitmap bitmap = ImageHelpers.bytesToBitmap(layoutSubImageBytes);
                            String imageFileName = UUID.randomUUID() + ".jpg";

                            try {
                                fileOutputStream = context.openFileOutput(imageFileName, Context.MODE_PRIVATE);
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fileOutputStream);
                                String filePath = context.getFilesDir() + "/" + imageFileName;
                                int result = LayoutHelper.updateLayoutImageListItemPath(rowId, filePath, context);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                        publishProgress(i);
                        i++;
                    } else {
                        return false;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
                if (cursor1 != null) {
                    cursor1.close();
                }
            }


            //Now clear out tables
            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseContracts.LayoutTable.LAYOUT_IMAGE_BLOB, "");
            DBSQLiteHelper.updateData(DatabaseContracts.LayoutTable.TABLE_NAME, contentValues, context);

            contentValues.clear();
            contentValues.put(DatabaseContracts.LayoutImages.IMAGE_BLOB, "");
            DBSQLiteHelper.updateData(DatabaseContracts.LayoutImages.TABLE_NAME, contentValues, context);
            return true;
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            int progress = values[0]++;
            if (activityInterface != null) {
                activityInterface.sendProgress(progress, noToDo);

            }
        }


        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (activityInterface != null) {
                activityInterface.taskEnded(aBoolean);
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected void onCancelled(Boolean aBoolean) {
            super.onCancelled(aBoolean);
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
