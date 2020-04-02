package com.swctools.activity_modules.layout_manager.recycler_adaptors;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toolbar;

import com.swctools.config.AppConfig;
import com.swctools.base.ApplyLayoutInterface;
import com.swctools.activity_modules.layout_manager.LayoutListInterface;
import com.swctools.activity_modules.layout_manager.views.ViewHolder_Layout;
import com.swctools.layouts.models.LayoutRecord;
import com.swctools.layouts.models.LayoutFolderItem;
import com.swctools.common.view_adaptors.view_adaptor_change_providers.LayoutListDiffCallback;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdaptor_LayoutList extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    private static final String TAG = "RecyclerA_Layist";
    private static final int LAYOUT_FOLDER = 0;
    private static final int LAYOUT_RECORD = 1;
    private static final int LAYOUT_SELECTED = 2;
    private AdaptorDelegate_LayoutFolder adaptorDelegateLayoutFolder;
    private AdaptorDelegate_LayoutRecord adaptorDelegateLayoutRecord;
    private AdaptorDelegate_LayoutRecord_Selected adaptorDelegate_layoutRecord_selected;
    private ArrayList<Object> itemList;
    private ArrayList<Object> itemListFull;
    protected Context mContext;
    private LayoutListInterface mInterface;
    private ApplyLayoutInterface applyLayoutInterface;
    private Toolbar toolbar;
    private AppConfig appConfig;

    private boolean versionsOpen = false;


    public RecyclerAdaptor_LayoutList(ArrayList<Object> layouts, Context context) {
        itemList = layouts;
        itemListFull = new ArrayList<>(layouts);
        mContext = context;
        mInterface = (LayoutListInterface) context;
        applyLayoutInterface = (ApplyLayoutInterface) context;
        appConfig = new AppConfig(mContext);
        adaptorDelegateLayoutFolder = new AdaptorDelegate_LayoutFolder(LAYOUT_FOLDER, context);
        adaptorDelegateLayoutRecord = new AdaptorDelegate_LayoutRecord(LAYOUT_RECORD, context);
        adaptorDelegate_layoutRecord_selected = new AdaptorDelegate_LayoutRecord_Selected(LAYOUT_SELECTED, context);
    }

    public static void setBottomMargin(View view, int bottomMargin) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            params.setMargins(params.leftMargin, params.topMargin, params.rightMargin, bottomMargin);
            view.requestLayout();
        }

    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        if (holder instanceof ViewHolder_Layout) {
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (adaptorDelegateLayoutFolder.isForViewType(itemList, position)) {
            return adaptorDelegateLayoutFolder.getViewType();
        } else if (adaptorDelegateLayoutRecord.isForViewType(itemList, position)) {
            return adaptorDelegateLayoutRecord.getItemViewType();
        } else if (adaptorDelegate_layoutRecord_selected.isForViewType(itemList, position)) {
            return adaptorDelegate_layoutRecord_selected.getItemViewType();
        }
        throw new IllegalArgumentException("No delegate found");
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (adaptorDelegateLayoutFolder.getViewType() == viewType) {
            return adaptorDelegateLayoutFolder.onCreateViewHolder(parent);
        } else if (adaptorDelegateLayoutRecord.getItemViewType() == viewType) {
            return adaptorDelegateLayoutRecord.onCreateViewHolder(parent);
        } else if (adaptorDelegate_layoutRecord_selected.getItemViewType() == viewType) {
            return adaptorDelegate_layoutRecord_selected.onCreateViewHolder(parent);
        }
        throw new IllegalArgumentException("No delegate found");
    }


    public void setRecordList(ArrayList<Object> recordList) {
//        this.itemList = recordList;
    }

    public List<Object> getmLayouts() {
        return itemList;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int position) {

        int viewType = viewHolder.getItemViewType();

        if (adaptorDelegateLayoutFolder.getViewType() == viewType) {
            adaptorDelegateLayoutFolder.onBindViewHolder(viewHolder, itemList, position);
        } else if (adaptorDelegateLayoutRecord.getViewType() == viewType) {
            adaptorDelegateLayoutRecord.onBindViewHolder(viewHolder, itemList, position);
        } else if (adaptorDelegate_layoutRecord_selected.getItemViewType() == viewType) {
            adaptorDelegate_layoutRecord_selected.onBindViewHolder(viewHolder, itemList, position);
        }

        if (position + 1 == getItemCount()) {
            setBottomMargin(viewHolder.itemView, (int) (80 * Resources.getSystem().getDisplayMetrics().density));
        } else {
            setBottomMargin(viewHolder.itemView, 0);
        }

    }


    public void updateList(ArrayList<Object> newList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new LayoutListDiffCallback(this.itemList, newList));
        this.itemList.clear();
        this.itemList.addAll(newList);
        this.itemListFull.clear();
        this.itemListFull.addAll(newList);
        diffResult.dispatchUpdatesTo(this);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    @Override
    public Filter getFilter() {
        return layoutListFilter;
    }

    public void showAll() {
        ArrayList<Object> filteredList = new ArrayList<>();
        filteredList.clear();
        for (Object obj : itemListFull) {
            if (obj instanceof LayoutRecord) {
                filteredList.add(obj);
            }
        }
        itemList.clear();
        itemList.addAll(filteredList);
        notifyDataSetChanged();
    }

    private void filterToFolder(int folderId) {
        ArrayList<Object> filteredList = new ArrayList<>();
        for (Object obj : itemListFull) {
            if (obj instanceof LayoutFolderItem) {
                LayoutFolderItem layoutFolderItem = (LayoutFolderItem) obj;
                if (layoutFolderItem.getParentFolderId() == folderId) {
                    filteredList.add(obj);
                }
            } else if (obj instanceof LayoutRecord) {
                LayoutRecord layoutRecord = (LayoutRecord) obj;
                if (layoutRecord.getLayoutFolderId() == folderId) {
                    filteredList.add(obj);
                }
            }
        }
        itemList.clear();
        itemList.addAll(filteredList);
        notifyDataSetChanged();
    }

    public Filter layoutListFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Object> filteredList = new ArrayList<>();
            String fitlerPattern = constraint.toString().toLowerCase();
            if (constraint == null | constraint.length() == 0) {
                filteredList.addAll(itemListFull);

            } else {
                for (Object obj : itemListFull) {
//                    if (obj instanceof LayoutFolderItem) {
//                        LayoutFolderItem layoutFolderItem = (LayoutFolderItem) obj;
//                        if (layoutFolderItem.getFolderName().toLowerCase().contains(fitlerPattern)) {
//                            filteredList.add(obj);
//                        }
                    if (obj instanceof LayoutRecord) {
                        LayoutRecord layoutRecord = (LayoutRecord) obj;
                        if (layoutRecord.containsString(fitlerPattern)) {
                            filteredList.add(obj);
                        }
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults filterResults) {
            itemList.clear();
            itemList.addAll((ArrayList<Object>) filterResults.values);


            notifyDataSetChanged();
        }
    };


    public void addLayoutRecords(List<Object> objects) {
        if (itemList == null) {
            itemList = new ArrayList<>();
        }
        itemList.addAll(objects);
        notifyDataSetChanged();
    }

    public void replaceListItems(List<Object> objects) {
        if (itemList == null) {
            itemList = new ArrayList<>();
        }
        itemList.clear();
        itemList.addAll(objects);
        notifyDataSetChanged();
    }


}
