package com.swctools.common.view_adaptors.view_adaptor_change_providers;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.swctools.layouts.models.LayoutFolderItem;
import com.swctools.layouts.models.LayoutRecord;

import java.util.List;

public class LayoutListDiffCallback extends DiffUtil.Callback {
    private static final String TAG = "LayoutListDiffCallback";
    List<Object> oldList;
    List<Object> newList;

    public LayoutListDiffCallback(List<Object> oldList, List<Object> newList) {
        this.oldList = oldList;
        this.newList = newList;

    }

    @Override
    public int getOldListSize() {
        return this.oldList.size();
    }

    @Override
    public int getNewListSize() {
        return this.newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        boolean result = false;
        Object oldObject = oldList.get(oldItemPosition);
        Object newObject = oldList.get(oldItemPosition);
        LayoutRecord oldLayoutRecord = null;
        LayoutRecord newLayoutRecord = null;
        LayoutFolderItem oldLayoutFolderItem = null;
        LayoutFolderItem newLayoutFolderItem = null;

        String oldType = oldObject.getClass().toString();
        String newType = newObject.getClass().toString();

        if (oldType.equalsIgnoreCase(newType)) {
            if ((oldObject instanceof LayoutRecord) && (newObject instanceof LayoutRecord)) {
                oldLayoutRecord = (LayoutRecord) oldObject;
                newLayoutRecord = (LayoutRecord) newObject;
                if (oldLayoutRecord.getLayoutIsFavourite().equalsIgnoreCase(newLayoutRecord.getLayoutIsFavourite())) {
                    result = true;
                } else {
                    result = false;
                }
            } else if ((oldObject instanceof LayoutFolderItem) && (newObject instanceof LayoutFolderItem)) {
                oldLayoutFolderItem = (LayoutFolderItem) oldObject;
                newLayoutFolderItem = (LayoutFolderItem) newObject;
                if (oldLayoutFolderItem.getFolderId() == newLayoutFolderItem.getFolderId()) {
                    result = true;
                } else {
                    result = false;
                }
            }
        } else {
            result = false;
        }
        return result;// result;



    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {

        return true;//oldItemPosition == newItemPosition;

    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
