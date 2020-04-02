package com.swctools.activity_modules.logs.fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.swctools.R;
import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;
import com.swctools.activity_modules.logs.view_adaptors.ExpandableAdaptor_DataUpdate;
import com.swctools.activity_modules.logs.models.DataUpdateRow;

import java.util.ArrayList;
import java.util.List;

public class FragmentDataUpdate extends Fragment {
    private static final String TAG = "FragmentDataUpdate";
    ExpandableAdaptor_DataUpdate expandableAdaptor;
    List<DataUpdateRow> dataUpdateRows;
    private ExpandableListView expandableListView;
    private Context mContext;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_data_update, container, false);
        expandableListView = (ExpandableListView) view.findViewById(R.id.data_update_expandableList);
        setDataUpdateRows();

        expandableAdaptor = new ExpandableAdaptor_DataUpdate(dataUpdateRows, getContext());
        expandableListView.setAdapter(expandableAdaptor);
        return view;
    }


    public void setDataUpdateRows() {
        dataUpdateRows = new ArrayList<>();
        Cursor cursor = DBSQLiteHelper.queryDB(DatabaseContracts.DataVersionUpdateHistory.TABLE_NAME, mContext);
        try {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.DataVersionUpdateHistory.COLUMN_ID));
                String tblName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.DataVersionUpdateHistory.APP_TABLE));
                int versNo = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.DataVersionUpdateHistory.APP_TABLE_VERSION));
                String updateNotes = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.DataVersionUpdateHistory.UPDATE_NOTES));
                long updateOn = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.DataVersionUpdateHistory.APP_TABLE_UPDATED_ON));
                DataUpdateRow dataUpdateRow = new DataUpdateRow(id, tblName, versNo, updateNotes, updateOn);
                dataUpdateRows.add(dataUpdateRow);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
