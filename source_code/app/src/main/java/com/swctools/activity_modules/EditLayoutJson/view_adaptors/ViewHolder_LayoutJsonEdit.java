package com.swctools.activity_modules.EditLayoutJson.view_adaptors;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toolbar;

import com.swctools.R;

public class ViewHolder_LayoutJsonEdit extends RecyclerView.ViewHolder {

    public EditText layoutKey, layoutUID, layoutX, layoutZ;
    public ImageView savelayout_json;
    public Toolbar editJson_Toolbar;


    public ViewHolder_LayoutJsonEdit(@NonNull View itemView) {
        super(itemView);

        layoutKey = itemView.findViewById(R.id.layoutKey);
        layoutUID = itemView.findViewById(R.id.layoutUID);
        layoutX = itemView.findViewById(R.id.layoutX);
        layoutZ = itemView.findViewById(R.id.layoutZ);
        editJson_Toolbar = itemView.findViewById(R.id.editJson_Toolbar);

        savelayout_json = itemView.findViewById(R.id.savelayout_json);



        editJson_Toolbar.inflateMenu(R.menu.list_item_menu_edit_layout_json);
    }
}
