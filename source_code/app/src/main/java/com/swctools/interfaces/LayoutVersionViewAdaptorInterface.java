package com.swctools.interfaces;

public interface LayoutVersionViewAdaptorInterface {
    //
//    void applySelectedLayout(int layoutId, int versId);
//
//    void applySelectedLayoutWar(int layoutId, int versId);
    void deleteSelectedLayoutVersion(int rowId, int layoutId, int versionId);

    void exportSelectedLayout(int layoutId, int versId);

    void shareSelectedLayout(int layoutId, int versId);

    void editLayoutJson(int layoutId, int versId);



}
