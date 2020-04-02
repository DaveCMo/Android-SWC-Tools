package com.swctools.activity_modules.multi_image_picker;

public interface ImageListInterface {
    void showGallery(int selected);

    void deleteImage(long id);

    void editImageLabel(long selected, String label);

}
