package com.swctools.interfaces;

import androidx.recyclerview.widget.DiffUtil;

import com.swctools.layouts.models.FavouriteLayoutItem;

import java.util.List;

public class LayoutFavouriteDiffCallBack extends DiffUtil.Callback {
    List<FavouriteLayoutItem> oldList;
    List<FavouriteLayoutItem> newList;

    public LayoutFavouriteDiffCallBack(List<FavouriteLayoutItem> newList, List<FavouriteLayoutItem> oldList) {
        this.newList = newList;
        this.oldList = oldList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getFavouriteId() == newList.get(newItemPosition).getFavouriteId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return false;
    }
}
