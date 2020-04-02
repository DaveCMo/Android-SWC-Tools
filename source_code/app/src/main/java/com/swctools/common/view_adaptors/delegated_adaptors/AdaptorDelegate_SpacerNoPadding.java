package com.swctools.common.view_adaptors.delegated_adaptors;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.swctools.R;

import java.util.ArrayList;

public class AdaptorDelegate_SpacerNoPadding {
    private int viewType;
    private Context mContext;
    private int COLOR_RED;
    private int COLOR_GREEN;

    public AdaptorDelegate_SpacerNoPadding(int viewType, Context context) {
        this.viewType = viewType;
        this.mContext = context;
        COLOR_RED = mContext.getResources().getColor(R.color.dark_red);
        COLOR_GREEN = mContext.getResources().getColor(R.color.green);
    }


    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_spacer_no_padding, parent, false);
        return new TrapViewHolder(itemView);
    }


    public int getViewType() {
        return this.viewType;
    }

    public int getItemViewType() {
        return this.viewType;
    }

    public boolean isForViewType(ArrayList<Object> items, int position) {
        return items.get(position) instanceof String;
    }


    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder tholder, ArrayList<Object> itemList, int position) {
        final String string = (String) itemList.get(position);
        final TrapViewHolder viewHolder = (TrapViewHolder) tholder;
        viewHolder.spacerTitle.setText(string);


    }

    class TrapViewHolder extends RecyclerView.ViewHolder {
        public TextView spacerTitle;

        public TrapViewHolder(View itemView) {
            super(itemView);
            spacerTitle = itemView.findViewById(R.id.spacerTitle);
        }

    }

}
