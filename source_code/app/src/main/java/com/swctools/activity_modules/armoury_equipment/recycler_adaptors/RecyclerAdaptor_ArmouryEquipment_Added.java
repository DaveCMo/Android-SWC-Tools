package com.swctools.activity_modules.armoury_equipment.recycler_adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.swctools.R;
import com.swctools.activity_modules.armoury_equipment.ArmouryEquipment_Callback;
import com.swctools.activity_modules.armoury_equipment.models.Armoury_Set_Item;
import com.swctools.activity_modules.armoury_equipment.views.ViewHolder_Armoury_Added;
import com.swctools.activity_modules.armoury_equipment.views.ViewHolder_Armoury_Added_Small;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.swctools.R.drawable.layout_editor;

public class RecyclerAdaptor_ArmouryEquipment_Added extends RecyclerView.Adapter<ViewHolder_Armoury_Added_Small> {
    private static final String TAG = "RecyclerAdaptor_Armoury";
    private static final String BASE_URL = "https://github.com/DaveCMo/Android-SWC-Tools/blob/master/equipment/";
    private Context context;
    private ArmouryEquipment_Callback armouryEquipment_callback;
    private ArrayList<Armoury_Set_Item> itemArrayList;


    public RecyclerAdaptor_ArmouryEquipment_Added(Context context, ArrayList<Armoury_Set_Item> itemArrayList) {
        this.context = context;
        this.armouryEquipment_callback = (ArmouryEquipment_Callback) context;
        this.itemArrayList = itemArrayList;
    }


    @NonNull
    @Override
    public ViewHolder_Armoury_Added_Small onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_armoury_added_small, parent, false);
        return new ViewHolder_Armoury_Added_Small(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder_Armoury_Added_Small holder, final int position) {
        final Armoury_Set_Item armoury_set_item = itemArrayList.get(position);
        holder.itemName.setText(armoury_set_item.getEquipUIName());

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(layout_editor);
        String imageUrl = BASE_URL + armoury_set_item.getFaction() + "/" + armoury_set_item.getGameNameNoLevel() + ".jpg?raw=true";
        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .load(imageUrl)
                .into(holder.armouryImage);
        holder.armouryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                armouryEquipment_callback.removeEquipment(armoury_set_item, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }
}
