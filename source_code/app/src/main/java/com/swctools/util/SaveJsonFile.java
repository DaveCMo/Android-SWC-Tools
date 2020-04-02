package com.swctools.util;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;

public class SaveJsonFile {
    private static final String TAG = "SaveJsonFile";
    private static boolean mCanWrite = false;
    private static boolean mCanRead = false;

    public static MethodResult saveJsonFile(String fileData, String fileName, String folderName, String ext) {

        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File(sdCard.getAbsolutePath() + "/" + folderName + "/");

        canIWrite();
        if (mCanWrite) {

            if (dir.mkdir() || dir.exists()) {

                File file = new File(dir, fileName + ext);
                try {
                    FileWriter wr = new FileWriter(file.getAbsoluteFile());
                    wr.write(fileData);
                    wr.flush();
                    wr.close();


                    return new MethodResult(true, file.getPath());
                } catch (Exception e) {
                    return new MethodResult(false, e.getMessage());
                }
            } else {
                return new MethodResult(false, "Something went wrong there.");
            }
        }else {
            return new MethodResult(false, "Failed to create file. Check storage permissions!");
        }


    }

    private static String jsonExtension(String absolutePath) {
        String extension = "";
        int i = absolutePath.lastIndexOf('.');
        if (i > 0) {
            extension = absolutePath.substring(i);
            if (!extension.equals(".json")) {
                return absolutePath.replace(extension, ".json");
            } else {
                return absolutePath;
            }
        } else {
            return absolutePath + ".json";
        }
    }


    public static void canIWrite() {

        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            //should be able to read and write
            mCanWrite = mCanRead = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            mCanRead = true;
            mCanWrite = false;
        } else {
            mCanWrite = mCanRead = false;
        }
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public static File getPublicFileStorageDir(String subFolder) {
        File file = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), subFolder);
        if (!file.mkdir()) {
            Log.e(TAG, "getPublicFileStorageDir: directory not created!");
        }
        return file;
    }

}
