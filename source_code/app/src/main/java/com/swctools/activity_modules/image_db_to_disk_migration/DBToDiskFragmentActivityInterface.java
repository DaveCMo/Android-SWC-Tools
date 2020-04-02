package com.swctools.activity_modules.image_db_to_disk_migration;

import android.graphics.Bitmap;

public interface DBToDiskFragmentActivityInterface {


    void sendProgress(int progress, int max);
    void taskEnded(Boolean result);
    void postImage(Bitmap bitmap);
}
