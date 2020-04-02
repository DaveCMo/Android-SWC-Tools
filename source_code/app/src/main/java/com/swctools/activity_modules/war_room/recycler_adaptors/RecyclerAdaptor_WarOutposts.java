package com.swctools.activity_modules.war_room.recycler_adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swctools.R;
import com.swctools.activity_modules.war_room.models.War_Room_OutPost;
import com.swctools.common.helpers.ImageHelpers;
import com.swctools.activity_modules.war_room.WarOutpostCallBack;
import com.swctools.activity_modules.war_room.processing_models.WarRoomData_Outposts;
import com.swctools.activity_modules.war_room.views.ViewHolder_WarOutpost;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdaptor_WarOutposts extends RecyclerView.Adapter<ViewHolder_WarOutpost> {
    private static final String TAG = "RecyclerAdaptor_WarOP";
    private ArrayList<War_Room_OutPost> itemList;
    private Context mContext;
    private WarOutpostCallBack mCallback;

    public RecyclerAdaptor_WarOutposts(ArrayList<War_Room_OutPost> data, Context context) {
        this.itemList = data;
        this.mContext = context;
        this.mCallback = (WarOutpostCallBack) context;
    }

    @NonNull
    @Override
    public ViewHolder_WarOutpost onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_war_outpost, viewGroup, false);
        return new ViewHolder_WarOutpost(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder_WarOutpost viewHolder, int i) {
        final War_Room_OutPost rowData = itemList.get(i);
        viewHolder.war_outpostImg.setImageDrawable(ImageHelpers.getPlanetImage(rowData.getOutPostName(), mContext));
        viewHolder.opLevel.setText(String.valueOf(rowData.getLevel()));
        viewHolder.war_outpostImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.showOPNameAndLevel(rowData.getOutPostName() + " Level " + rowData.getLevel());
            }
        });
        viewHolder.opLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.showOPNameAndLevel(rowData.getOutPostName() + " Level " + rowData.getLevel());
            }
        });

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
