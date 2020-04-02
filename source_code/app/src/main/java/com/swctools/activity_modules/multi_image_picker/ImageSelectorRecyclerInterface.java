package com.swctools.activity_modules.multi_image_picker;

public interface ImageSelectorRecyclerInterface {
    void itemSelected(int itemPosition);

    void itemShortPressed(int itemPosition);

    void itemDeselected(int itemPosition);

    void itemDelete(int itemPosition);
    void setLabel(int itemPosition);
}
