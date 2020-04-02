package com.swctools.activity_modules.multi_image_picker.models;

public class SelectedImageModel {
    public long no;
    public byte[] bytes;
    public boolean selected;
    public String label;


    public SelectedImageModel(byte[] bytes, long i) {
        this.no = i;
        this.bytes = bytes;
        this.selected = false;
    }

    public SelectedImageModel(byte[] bytes, long i, boolean b, String s) {
        this.no = i;
        this.bytes = bytes;
        this.selected = b;
        this.label = s;

    }


}

