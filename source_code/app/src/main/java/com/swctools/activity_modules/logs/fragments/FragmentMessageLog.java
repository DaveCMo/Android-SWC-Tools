package com.swctools.activity_modules.logs.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.Switch;

import com.swctools.R;
import com.swctools.config.AppConfig;
import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;
import com.swctools.activity_modules.logs.models.SWCMessageLog_ListProvider;
import com.swctools.activity_modules.logs.models.SWCMessageLogModel;
import com.swctools.activity_modules.logs.view_adaptors.ExpandableAdaptor_SWCMessageLog;

import java.util.List;

public class FragmentMessageLog extends Fragment {
    private static final String TAG = "FragmentDataUpdate";
    //    ExpandableAdaptor_DatabaseTables expandableAdaptor;
    ExpandableAdaptor_SWCMessageLog expandableAdaptor;
    //    List<DatabaseExpandableRow> dataUpdateRows;
    List<SWCMessageLogModel> dataUpdateRows;
    private ExpandableListView expandableListView;
    private Button deleteLog;
    private Context mContext;
    private Switch logOnOff;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mContext = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_message_log, container, false);
        expandableListView = (ExpandableListView) view.findViewById(R.id.messagelog_expandableList);
        deleteLog = view.findViewById(R.id.deleteLog);
        logOnOff = view.findViewById(R.id.logOnOff);
        dataUpdateRows = SWCMessageLog_ListProvider.getSWCMessageLogList(mContext);
        final AppConfig appConfig = new AppConfig(mContext);
        logOnOff.setChecked(appConfig.bLogSWCMessage());
        deleteLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBSQLiteHelper.deleteDbRows(DatabaseContracts.SWCMessageLog.TABLE_NAME, null, null, mContext);
                dataUpdateRows = SWCMessageLog_ListProvider.getSWCMessageLogList(mContext);
                expandableAdaptor.notifyDataSetChanged();
            }
        });

        logOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                appConfig.setbLogSWCMessage(b);
            }
        });


        expandableAdaptor = new ExpandableAdaptor_SWCMessageLog(SWCMessageLog_ListProvider.getSWCMessageLogList(mContext), getContext());
        expandableListView.setAdapter(expandableAdaptor);
        return view;
    }


}
