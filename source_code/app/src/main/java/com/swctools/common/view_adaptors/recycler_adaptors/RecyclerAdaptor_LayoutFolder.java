package com.swctools.common.view_adaptors.recycler_adaptors;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swctools.R;
import com.swctools.interfaces.LayoutFolderInterface;
import com.swctools.layouts.models.LayoutFolderItem;
import com.swctools.activity_modules.layout_manager.views.ViewHolder_Folder;

import java.util.List;

public class RecyclerAdaptor_LayoutFolder extends RecyclerView.Adapter<ViewHolder_Folder> {
    private static final String TAG = "RecyclerAdaptor_LayoutFolder";
    private List<LayoutFolderItem> folderItems;
    private Context mContext;
    private LayoutFolderInterface mCallback;

    public RecyclerAdaptor_LayoutFolder(List<LayoutFolderItem> list, Context context) {
        this.folderItems = list;
        this.mContext = context;
        mCallback = (LayoutFolderInterface) context;
    }

    @NonNull
    @Override
    public ViewHolder_Folder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout_folder, parent, false);
        return new ViewHolder_Folder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder_Folder holder, int position) {
        final LayoutFolderItem layoutFolderItem = folderItems.get(position);

        holder.folderName.setText(layoutFolderItem.getFolderName());
        holder.folderContentsCount.setText((layoutFolderItem.getCountLayoutsStr()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mCallback.folderSelectedFragment(layoutFolderItem.getFolderId());
            }
        });

        holder.folderClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mCallback.folderSelectedFragment(layoutFolderItem.getFolderId());
            }
        });
    }


    @Override
    public int getItemCount() {
        return folderItems.size();
    }



}
