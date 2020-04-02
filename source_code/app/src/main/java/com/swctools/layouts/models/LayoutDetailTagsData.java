package com.swctools.layouts.models;

import java.util.ArrayList;

public class LayoutDetailTagsData {
    private ArrayList<LayoutTag> layoutTagArrayList;

    public LayoutDetailTagsData(ArrayList<LayoutTag> layoutTags){
        this.layoutTagArrayList = layoutTags;
    }

    public ArrayList<LayoutTag> getLayoutTagArrayList() {
        return layoutTagArrayList;
    }
}
