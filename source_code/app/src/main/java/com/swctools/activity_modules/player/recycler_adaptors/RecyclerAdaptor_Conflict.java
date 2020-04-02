package com.swctools.activity_modules.player.recycler_adaptors;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swctools.R;
import com.swctools.common.helpers.ImageHelpers;
import com.swctools.common.view_adaptors.recycler_adaptors.RecyclerAdaptor_TwoTextItem;
import com.swctools.activity_modules.player.models.Conflict_Data_Model;
import com.swctools.activity_modules.player.models.TwoTextItemData;
import com.swctools.activity_modules.player.views.ViewHolder_ConflictData;

import java.util.ArrayList;

public class RecyclerAdaptor_Conflict extends RecyclerView.Adapter<ViewHolder_ConflictData> {
    private static final String TAG = "RecyclerAdaptor_Conflict";
    private Context mContext;
    private ArrayList<Conflict_Data_Model> listItems;
    private ArrayList<Boolean> expanded;


    public RecyclerAdaptor_Conflict(ArrayList<Conflict_Data_Model> itemList, Context context) {
        //lol accidental mixup of itemlist/listitems creates distinction
        this.listItems = itemList;
        this.expanded = new ArrayList<>();
        for (int i = 0; i < listItems.size(); i++) {
            this.expanded.add(false);
        }
        this.mContext = context;
    }


    @NonNull
    @Override
    public ViewHolder_ConflictData onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_conflict_data, viewGroup, false);
        return new ViewHolder_ConflictData(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder_ConflictData _viewHolderConflictData, final int i) {

        final Conflict_Data_Model conflict_data_model = listItems.get(i);
        _viewHolderConflictData.planet_img.setImageDrawable(ImageHelpers.getPlanetImage(conflict_data_model.getPlanetName(), mContext));
        _viewHolderConflictData.planet_name.setText(conflict_data_model.getPlanetName());
        _viewHolderConflictData.conflictPerc.setText(conflict_data_model.getConflictPerc());
        _viewHolderConflictData.conflictListRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideShowExtra(_viewHolderConflictData, conflict_data_model, i);
            }
        });


    }

    private void hideShowExtra(ViewHolder_ConflictData _viewHolderConflictData, Conflict_Data_Model conflict_data_model, int i) {
        ArrayList<TwoTextItemData> twoTextItemData = new ArrayList<>();
        if(expanded.get(i)){
            expanded.add(i, false);
        }else {
            expanded.add(i, true);
            conflict_data_model.setTwoTextItems(mContext);
            twoTextItemData = conflict_data_model.getTwoTextItemData();
//            twoTextItemData.add(new TwoTextItemData("Start Date", conflict_data_model.getStartDateRaw(mContext)));
//            twoTextItemData.add(new TwoTextItemData("End Date", conflict_data_model.getEndDate(mContext)));
//            twoTextItemData.add(new TwoTextItemData("Conflict Points", String.valueOf(conflict_data_model.getConflictGears())));
//            twoTextItemData.add(new TwoTextItemData("Rank", String.valueOf(conflict_data_model.getConflictRank())));
//            twoTextItemData.add(new TwoTextItemData("Attacks Won / Lost", String.format(ApplicationMessageTemplates.PROGRESS_BAR_LABEL.getemplateString(),
//                    String.valueOf(conflict_data_model.getAttacksWon()),
//                    String.valueOf(conflict_data_model.getAttacksLost()))));
//            twoTextItemData.add(new TwoTextItemData("Defences Won / Lost", String.format(ApplicationMessageTemplates.PROGRESS_BAR_LABEL.getemplateString(),
//                    conflict_data_model.getDefensesWon(), conflict_data_model.getDefensesLost())));

        }
//        _viewHolderConflictData.conflictExtraRecycler.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));


        RecyclerAdaptor_TwoTextItem recyclerAdaptorTwoTextItem = new RecyclerAdaptor_TwoTextItem(twoTextItemData);
        _viewHolderConflictData.conflictExtraRecycler.setAdapter(recyclerAdaptorTwoTextItem);
        recyclerAdaptorTwoTextItem.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return this.listItems.size();
    }
}
