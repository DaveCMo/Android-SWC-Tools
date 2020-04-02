package com.swctools.activity_modules.about.view_adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.swctools.R;
import com.swctools.activity_modules.about.models.FAQData;

import java.util.ArrayList;

public class ExpandableAdaptor_FAQ extends BaseExpandableListAdapter {
    private static final String TAG = "DataUpdateExpdbleAdr";
    private Context mContext;
    private ArrayList<FAQData> listItems;

    public ExpandableAdaptor_FAQ(ArrayList<FAQData> items, Context context) {
        this.listItems = items;
        this.mContext = context;
    }

    @Override
    public int getGroupCount() {
        return this.listItems.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listItems.get(groupPosition).answers.size();
    }

    @Override
    public Object getGroup(int i) {
        return this.listItems.get(i);
    }

    @Override
    public Object getChild(int groupPosition, final int childPosition) {

        return this.listItems.get(groupPosition).answers.get(childPosition);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View itemView, ViewGroup viewGroup) {
        TextView question;

        final FAQData itemRow = (FAQData) getGroup(i);


        if (itemView == null) {
            LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = inflater.inflate(R.layout.list_item_single_text_group_item, null);
        }
        question = itemView.findViewById(R.id.list_item_group_text);
        question.setText(itemRow.q);
        return itemView;
    }


    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean b, View view, ViewGroup viewGroup) {
        final String answer = (String) getChild(groupPosition, childPosition);
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.list_item_single_text_child_item, null);
        }


        TextView data_update_note = view.findViewById(R.id.list_item_child_text);
        data_update_note.setText(answer);

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }


}
