package com.swctools.common.view_adaptors.recycler_adaptors;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.swctools.R;

import java.util.List;

public class RecyclerAdaptor_BulletPoint extends RecyclerView.Adapter<RecyclerAdaptor_BulletPoint.BulletPointViewHolder> {

    private List<String> itemList;
    private Context mContext;

    public RecyclerAdaptor_BulletPoint(List<String> itemList, Context context){
        this.itemList = itemList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public BulletPointViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_single_text_bullet, parent, false);
        return new BulletPointViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BulletPointViewHolder holder, int position) {
        final String rowItem = itemList.get(position);
        holder.bulletPointText.setText(rowItem);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class BulletPointViewHolder extends RecyclerView.ViewHolder{
        public TextView bulletPointText;
        public BulletPointViewHolder(View itemView) {
            super(itemView);
            bulletPointText = (TextView) itemView.findViewById(R.id.bulletPointText);
        }
    }

}
