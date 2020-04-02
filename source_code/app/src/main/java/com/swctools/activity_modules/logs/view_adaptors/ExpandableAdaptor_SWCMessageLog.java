package com.swctools.activity_modules.logs.view_adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.swctools.R;
import com.swctools.activity_modules.logs.models.SWCMessageLogModel;

import java.util.List;

public class ExpandableAdaptor_SWCMessageLog extends BaseExpandableListAdapter {
    private static final String TAG = "DataUpdateExpdbleAdr";
    private Context mContext;
    private List<SWCMessageLogModel> listItems;

    public ExpandableAdaptor_SWCMessageLog(List<SWCMessageLogModel> items, Context context){
        this.listItems = items;
        this.mContext = context;



    }

    @Override
    public int getGroupCount() {
        return this.listItems.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return this.listItems.get(i);
    }

    @Override
    public Object getChild(int groupPosition, final int childPosition) {
        return this.listItems.get(groupPosition).response;
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
    public View getGroupView(int i, boolean b, View itemView, ViewGroup viewGroup) {
        TextView swcmessagelog_function, swcmessagelog_message, swcmessagelog_timeStamp;

        final SWCMessageLogModel itemRow = (SWCMessageLogModel) getGroup(i);
        if(itemView==null){
            LayoutInflater inflater= (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = inflater.inflate(R.layout.list_item_message_log_row, null);
        }
        swcmessagelog_function = itemView.findViewById(R.id.swcmessagelog_function);
        swcmessagelog_message = itemView.findViewById(R.id.swcmessagelog_message);
        swcmessagelog_timeStamp = itemView.findViewById(R.id.swcmessagelog_timeStamp);
        swcmessagelog_function.setText(itemRow.function);
        swcmessagelog_message.setText(itemRow.message);
        swcmessagelog_timeStamp.setText(String.valueOf(itemRow.timeStamp));
        return itemView;
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
