package com.swctools.layouts.models;

import java.util.ArrayList;

public class LayoutDetailVersionsData {

    private ArrayList<LayoutVersion> layoutVersions;

    public LayoutDetailVersionsData(ArrayList<LayoutVersion> layoutVersions) {
        this.layoutVersions = layoutVersions;
    }

    public ArrayList<LayoutVersion> getLayoutVersions() {
        return layoutVersions;
    }
}
