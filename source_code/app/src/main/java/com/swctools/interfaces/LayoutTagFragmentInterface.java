package com.swctools.interfaces;

import com.swctools.layouts.models.LayoutTag;

import java.util.ArrayList;

public interface LayoutTagFragmentInterface {
    void saveTagSelection(ArrayList<LayoutTag> newSelectedTags);
    void addTag(String tagLabel);
    void cancelTagSelection();
}
