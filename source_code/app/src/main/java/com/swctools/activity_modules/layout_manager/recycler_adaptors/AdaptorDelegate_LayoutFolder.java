package com.swctools.activity_modules.layout_manager.recycler_adaptors;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import com.swctools.R;
import com.swctools.common.enums.ApplicationMessageTemplates;
import com.swctools.activity_modules.layout_manager.LayoutListInterface;
import com.swctools.config.AppConfig;
import com.swctools.layouts.models.LayoutFolderItem;
import com.swctools.common.view_adaptors.view_holders.ViewHolder_Folder_wToolbar;

import java.util.ArrayList;

public class AdaptorDelegate_LayoutFolder {
    private static final String TAG = "LAYOUTFOLDERDEFL";
    private int viewType;
    private Context mContext;
    private LayoutListInterface mInterface;

    private Toolbar toolbar;
    private AppConfig appConfig;


    public AdaptorDelegate_LayoutFolder(int viewType, Context context) {
        viewType = viewType;
        mContext = context;
        mInterface = (LayoutListInterface) context;


    }

    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout_folder, parent, false);
        toolbar = (Toolbar) itemView.findViewById(R.id.layoutFolder_Toolbar);
        toolbar.inflateMenu(R.menu.layout_folder_menu);
        return new ViewHolder_Folder_wToolbar(itemView);
    }


    public int getViewType() {
        return viewType;
    }

    public int getItemViewType() {
        return viewType;
    }

    public boolean isForViewType(ArrayList<Object> items, int position) {

        if(items.get(position) instanceof LayoutFolderItem){
            return true;
        } else {
            return false;
        }
//        return items.get(position) instanceof ViewHolder_Folder_wToolbar;
    }


    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder tholder, ArrayList<Object> itemList, int position) {


        ViewHolder_Folder_wToolbar holder = (ViewHolder_Folder_wToolbar) tholder;
        final LayoutFolderItem layoutFolderItem = (LayoutFolderItem) itemList.get(position);
        holder.folderName.setText(layoutFolderItem.getFolderName());
        holder.folderContentsCount.setText(String.format(ApplicationMessageTemplates.SEMI_COLON_ITEM.getemplateString(), "Layouts", layoutFolderItem.getCountLayouts()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInterface.folderSelected(layoutFolderItem.getFolderId());
            }
        });
        holder.folderClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInterface.folderSelected(layoutFolderItem.getFolderId());
            }
        });

        holder.layoutFolder_Toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.layout_folderRename) {
                    mInterface.renameFolder(layoutFolderItem.getFolderId());
                } else if (itemId == R.id.layout_folderMove) {
                    mInterface.moveFolder(layoutFolderItem.getFolderId());
                } else if (itemId == R.id.layout_folderDelete) {
                    mInterface.deleteFolder(layoutFolderItem.getFolderId());


                }

                return true;
            }
        });


    }
}
