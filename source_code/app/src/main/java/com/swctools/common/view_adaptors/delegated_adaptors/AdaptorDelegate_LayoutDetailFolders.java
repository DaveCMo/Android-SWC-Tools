package com.swctools.common.view_adaptors.delegated_adaptors;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swctools.R;
import com.swctools.layouts.models.LayoutDetailFoldersData;
import com.swctools.common.view_adaptors.recycler_adaptors.RecyclerAdaptor_LayoutFolderBreadCrumb;
import com.swctools.layouts.models.LayoutFolderItem;
import com.swctools.common.view_adaptors.view_holders.ViewHolder_LayoutDetailFolder;

import java.util.ArrayList;

public class AdaptorDelegate_LayoutDetailFolders {
    private static final String TAG = "DETAILADAPTORDEL";

    private int viewType;
    private Context mContext;
    private RecyclerAdaptor_LayoutFolderBreadCrumb breadCrumbViewAdaptor;
    private LayoutDetailFolderInterface mActivityCallBack;

    public AdaptorDelegate_LayoutDetailFolders(int viewType, Context context) {
        this.viewType = viewType;
        this.mContext = context;
        this.mActivityCallBack = (LayoutDetailFolderInterface) context;
    }

    public int getViewType() {
        return this.viewType;
    }

    public int getItemViewType() {
        return this.viewType;
    }

    public boolean isForViewType(ArrayList<Object> items, int position) {

        return items.get(position) instanceof LayoutDetailFoldersData;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout_detail_folders, parent, false);
        return new ViewHolder_LayoutDetailFolder(itemView);
    }

    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder tholder, ArrayList<Object> itemList, int position) {
        final ViewHolder_LayoutDetailFolder viewHolder = (ViewHolder_LayoutDetailFolder) tholder;
        final LayoutDetailFoldersData itemData = (LayoutDetailFoldersData) itemList.get(position);


        final LinearLayoutManager breadCrumbLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, true);
        viewHolder.layoutDetailsFolder.setHasFixedSize(false);
        ArrayList<LayoutFolderItem> foldersList = itemData.getLayoutFolderItems();
//        Collections.sort(foldersList, Collections.reverseOrder());
        breadCrumbViewAdaptor = new RecyclerAdaptor_LayoutFolderBreadCrumb(foldersList, mContext);

        viewHolder.layoutDetailsFolder.setLayoutManager(breadCrumbLayoutManager);
        viewHolder.layoutDetailsFolder.setAdapter(breadCrumbViewAdaptor);
        breadCrumbViewAdaptor.notifyDataSetChanged();
        breadCrumbViewAdaptor.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                breadCrumbLayoutManager.smoothScrollToPosition(viewHolder.layoutDetailsFolder, null, breadCrumbViewAdaptor.getItemCount());
            }

            @Override
            public void onChanged() {
                breadCrumbLayoutManager.smoothScrollToPosition(viewHolder.layoutDetailsFolder, null, breadCrumbViewAdaptor.getItemCount());
            }
        });
        viewHolder.layoutDetailEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivityCallBack.moveFolder();

            }
        });


    }

    public interface LayoutDetailFolderInterface {
        void moveFolder();
    }

}
