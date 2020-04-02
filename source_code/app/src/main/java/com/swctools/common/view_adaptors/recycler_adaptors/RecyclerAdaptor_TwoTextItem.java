package com.swctools.common.view_adaptors.recycler_adaptors;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swctools.R;
import com.swctools.activity_modules.player.models.TwoTextItemData;
import com.swctools.common.view_adaptors.view_holders.ViewHolder_TwoText;

import java.util.ArrayList;

public class RecyclerAdaptor_TwoTextItem extends RecyclerView.Adapter<ViewHolder_TwoText> {
    private ArrayList<TwoTextItemData> itemList;

    public RecyclerAdaptor_TwoTextItem(ArrayList<TwoTextItemData> twoTextItemData) {
        this.itemList = twoTextItemData;
    }

    @NonNull
    @Override
    public ViewHolder_TwoText onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_2_text_group_item, viewGroup, false);
        return new ViewHolder_TwoText(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder_TwoText viewHolderTwoText, int i) {
        final TwoTextItemData twoTextItemData = itemList.get(i);
        viewHolderTwoText.list_item_group_3_left.setText(twoTextItemData.text1);
        viewHolderTwoText.list_item_group_3_right.setText(twoTextItemData.text2);
    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
