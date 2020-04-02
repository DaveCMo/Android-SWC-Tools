package com.swctools.interfaces;

public interface LayoutFoldersFragmentInterface {

    void cancelFolderSelection();

    void confirmFolderSelection(int folderId, String cmd);

    void addFolderFromFragment(String newFolder, int parentFolderId);

    void upFolderInFragment();
//        public void onDialogNegativeClick(DialogFragment dialog);
}
