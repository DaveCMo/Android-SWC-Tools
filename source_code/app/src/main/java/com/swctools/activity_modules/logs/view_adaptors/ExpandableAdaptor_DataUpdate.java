package com.swctools.activity_modules.logs.view_adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.swctools.R;
import com.swctools.common.helpers.DateTimeHelper;
import com.swctools.activity_modules.logs.models.DataUpdateRow;

import java.util.List;

public class ExpandableAdaptor_DataUpdate extends BaseExpandableListAdapter {
    private static final String TAG = "DataUpdateExpdbleAdr";
    private Context mContext;
    private List<DataUpdateRow> dataUpgradeRowList;

    public ExpandableAdaptor_DataUpdate(List<DataUpdateRow> dataUpgradeRowList, Context context){
        this.dataUpgradeRowList = dataUpgradeRowList;
        this.mContext = context;



    }

    @Override
    public int getGroupCount() {
        return this.dataUpgradeRowList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return this.dataUpgradeRowList.get(i).updateNotesList.size();
    }

    @Override
    public Object getGroup(int i) {
        return this.dataUpgradeRowList.get(i);
    }

    @Override
    public Object getChild(int groupPosition, final int childPosition) {
        return this.dataUpgradeRowList.get(groupPosition).updateNotesList.get(childPosition);
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        final DataUpdateRow dataUpdateRow = (DataUpdateRow) getGroup(i);
        if(view==null){
            LayoutInflater inflater= (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item_data_update, null);
        }
        TextView data_update_table = (TextView) view.findViewById(R.id.data_update_table);
        TextView data_update_version = (TextView) view.findViewById(R.id.data_update_version);
        TextView data_update_date = (TextView) view.findViewById(R.id.data_update_date);
        data_update_table.setText(dataUpdateRow.tbl);
        data_update_version.setText(String.valueOf(dataUpdateRow.tbl_vers));
        data_update_date.setText(DateTimeHelper.shortDateTime(dataUpdateRow.updateOn, mContext));
        return view;
    }


    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean b, View view, ViewGroup viewGroup) {
        final String updateNote = (String) getChild(groupPosition, childPosition);
        if(view==null){
            LayoutInflater layoutInflater = (LayoutInflater)this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.list_item_single_text_child_item, null);
        }
        TextView data_update_note = (TextView) view.findViewById(R.id.list_item_child_text);
        data_update_note.setText(updateNote);

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
