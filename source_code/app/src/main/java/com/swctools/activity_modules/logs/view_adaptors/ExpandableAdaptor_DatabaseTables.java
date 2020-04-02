package com.swctools.activity_modules.logs.view_adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.swctools.R;
import com.swctools.activity_modules.logs.models.DatabaseExpandableRow;

import java.util.List;

public class ExpandableAdaptor_DatabaseTables extends BaseExpandableListAdapter {
    private static final String TAG = "DatabaseExpAdap";
    private Context mContext;
    private List<DatabaseExpandableRow> databaseTableList;

    public ExpandableAdaptor_DatabaseTables(List<DatabaseExpandableRow> list, Context context) {
        this.databaseTableList = list;
        this.mContext = context;
    }

    @Override
    public int getGroupCount() {
        return this.databaseTableList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return this.databaseTableList.get(i).getTableRows().size();
    }

    @Override
    public Object getGroup(int i) {

        return this.databaseTableList.get(i);
    }

    @Override
    public Object getChild(int groupPosition, final int childPosition) {
        return this.databaseTableList.get(groupPosition).getTableRows().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {

        return 0;
//        return groupPosition;
    }


    @Override
    public long getChildId(int groupPosition, int childPosition) {
//        return childPosition;
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        final DatabaseExpandableRow dataUpgradeRowItem = (DatabaseExpandableRow) getGroup(i);
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item_single_text_group_item, null);
        }
        TextView list_item_group_text = (TextView) view.findViewById(R.id.list_item_group_text);
        list_item_group_text.setText(dataUpgradeRowItem.getTableName());
        return view;
    }


    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean b, View view, ViewGroup viewGroup) {
        final String childItem = (String) getChild(groupPosition, childPosition);
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.list_item_single_text_child_item, null);
        }
        TextView leftText = (TextView) view.findViewById(R.id.list_item_child_text);
        leftText.setText(childItem);

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
