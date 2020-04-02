package com.swctools.activity_modules.layout_detail.models;

public class LayoutDetail_ImageListItem {

    private long no;
    private String filePath;
    private boolean selected;
    private String label;

    public LayoutDetail_ImageListItem(long no, String filePath, boolean selected, String label) {
        this.no = no;
        this.filePath = filePath;
        this.selected = selected;
        this.label = label;
    }


    public long getNo() {
        return no;
    }

    public String getFilePath() {
        return filePath;
    }

    public boolean isSelected() {
        return selected;
    }

    public String getLabel() {
        return label;
    }
}
