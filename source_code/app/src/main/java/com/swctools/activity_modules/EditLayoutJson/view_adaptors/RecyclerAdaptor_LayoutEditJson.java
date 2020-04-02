package com.swctools.activity_modules.EditLayoutJson.view_adaptors;

import android.content.Context;
import android.content.res.Resources;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toolbar;

import com.swctools.activity_modules.EditLayoutJson.EditJsonRecyclerInterface;
import com.swctools.activity_modules.EditLayoutJson.models.EditBuilding;
import com.swctools.R;
import com.swctools.common.models.player_models.Building;

import java.util.ArrayList;

public class RecyclerAdaptor_LayoutEditJson extends RecyclerView.Adapter<ViewHolder_LayoutJsonEdit> implements Filterable {
    private static boolean doneSetting = false;
    private Context mContext;
    private ArrayList<EditBuilding> buildingList;
    private ArrayList<EditBuilding> buildingListFull;
    private EditJsonRecyclerInterface mCallBack;

    @Override
    public Filter getFilter() {
        return layoutJsonFilter;
    }


    public Filter layoutJsonFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Building> filteredList = new ArrayList<>();
            if (constraint != null) {
                if (constraint == null | constraint.length() == 0) {
                    filteredList.addAll(buildingListFull);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (Building building : buildingListFull) {
                        if (building.getUid().toLowerCase().contains(filterPattern) || building.getKey().toLowerCase().contains(filterPattern)) {
                            filteredList.add(building);
                        }
                    }
                }
            } else {
                filteredList.addAll(buildingListFull);
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;

        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            buildingList.clear();
            buildingList.addAll((ArrayList<EditBuilding>) filterResults.values);


            notifyDataSetChanged();
        }
    };

    public RecyclerAdaptor_LayoutEditJson(ArrayList<EditBuilding> buildingList, Context context) {
        this.buildingList = buildingList;
        this.buildingListFull = new ArrayList<>(buildingList);
        this.mContext = context;
        this.mCallBack = (EditJsonRecyclerInterface) mContext;

    }

    public ArrayList<EditBuilding> getBuildingList() {
        return this.buildingListFull;
    }

    public void setArrayList(ArrayList<EditBuilding> arrayList) {
        this.buildingList.clear();
        this.buildingListFull.clear();

        this.buildingList.addAll(arrayList);
        this.buildingListFull = new ArrayList<>(arrayList);
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder_LayoutJsonEdit onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout_item, parent, false);

        return new ViewHolder_LayoutJsonEdit(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder_LayoutJsonEdit vholder, final int position) {
        final EditBuilding building = buildingList.get(position);

        if (building.edit == 1) {
            vholder.layoutKey.setEnabled(true);
            vholder.layoutUID.setEnabled(true);
            vholder.layoutX.setEnabled(true);
            vholder.layoutZ.setEnabled(true);
            vholder.savelayout_json.setVisibility(View.VISIBLE);

        } else {
            vholder.layoutKey.setEnabled(false);
            vholder.layoutUID.setEnabled(false);
            vholder.layoutX.setEnabled(false);
            vholder.layoutZ.setEnabled(false);
            vholder.savelayout_json.setVisibility(View.GONE);
        }
        vholder.layoutKey.setText(building.getKey());
        vholder.layoutUID.setText(building.getUid());
        vholder.layoutX.setText(String.valueOf(building.getX()));
        vholder.layoutZ.setText(String.valueOf(building.getZ()));


        vholder.editJson_Toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int menuId = menuItem.getItemId();

                switch (menuId) {
                    case R.id.mnuitem_edit_layout_json_edit:
                        vholder.layoutKey.setEnabled(true);
                        vholder.layoutUID.setEnabled(true);
                        vholder.layoutX.setEnabled(true);
                        vholder.layoutZ.setEnabled(true);
                        vholder.savelayout_json.setVisibility(View.VISIBLE);
                        break;
                    case R.id.mnuitem_layout_json_delete:
                        mCallBack.removeRow(building.getId());
                        break;
                }
                return true;
            }
        });
        vholder.savelayout_json.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int x = Integer.parseInt(vholder.layoutX.getEditableText().toString());
                int z = Integer.parseInt(vholder.layoutZ.getEditableText().toString());
                vholder.layoutKey.setEnabled(false);
                vholder.layoutUID.setEnabled(false);
                vholder.layoutX.setEnabled(false);
                vholder.layoutZ.setEnabled(false);
                vholder.savelayout_json.setVisibility(View.GONE);
                mCallBack.saveRow(building.getId(), vholder.layoutKey.getEditableText().toString(), vholder.layoutUID.getEditableText().toString(), x, z);
            }
        });

        if (position + 1 == getItemCount()) {
            setBottomMargin(vholder.itemView, (int) (80 * Resources.getSystem().getDisplayMetrics().density));
        } else {
            setBottomMargin(vholder.itemView, 0);
        }
    }


    public static void setBottomMargin(View view, int bottomMargin) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            params.setMargins(params.leftMargin, params.topMargin, params.rightMargin, bottomMargin);
            view.requestLayout();
        }

    }


    @Override
    public int getItemCount() {
        return buildingList.size();
    }


}
