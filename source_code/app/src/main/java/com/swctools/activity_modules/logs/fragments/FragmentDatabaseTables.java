package com.swctools.activity_modules.logs.fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.swctools.R;
import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;
import com.swctools.common.enums.BundleKeys;
import com.swctools.common.popups.AlertFragment;
import com.swctools.common.helpers.AppLoggerHelper;
import com.swctools.activity_modules.logs.models.DatabaseExpandableRow;
import com.swctools.activity_modules.logs.view_adaptors.ExpandableAdaptor_DatabaseTables;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentDatabaseTables extends Fragment {
    private static final String TAG = "FragmentDataBaseTables";
    ExpandableAdaptor_DatabaseTables expandableAdaptor;
    List<DatabaseExpandableRow> rowList;
    private ExpandableListView expandableListView;
    private Context mContext;
    private Button exportLogBtn, exportTblsBtn;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_database_tables, container, false);
        setDataUpdateRows();
//        setDataUpdateRows();
        exportLogBtn = (Button) view.findViewById(R.id.exportLogBtn);
        exportTblsBtn = (Button) view.findViewById(R.id.exportTblsBtn);

        exportTblsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StringBuilder stringBuilder = new StringBuilder();
                Cursor cursor = DBSQLiteHelper.queryDB(DatabaseContracts.LayoutVersions.TABLE_NAME, mContext);// database.query(DatabaseContracts.LayoutVersions.TABLE_NAME, null, null, null, null, null, null);
                try {
                    cursor.moveToFirst();
                    for (int i = 0; i < cursor.getColumnNames().length; i++) {

                        stringBuilder.append(cursor.getColumnName(i) + "|" + cursor.getType(i) + "\n");
                    }
                    AlertFragment af = new AlertFragment();
                    Bundle b = new Bundle();
                    b.putString(BundleKeys.DIALOG_MESSAGE.toString(), stringBuilder.toString());
                    b.putString(BundleKeys.DIALOG_TITLE.toString(), "Layout Version Table");
                    af.setArguments(b);
                    af.show(getActivity().getFragmentManager(), "LAYOUTVERSIONTABLE");
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
                //                AppLoggerHelper.startTableInfo(mContext);
//                Toast.makeText(mContext, "Done!", Toast.LENGTH_LONG).show();
            }
        });
        exportLogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppLoggerHelper.outPutLog(mContext);
            }
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public void setDataUpdateRows() {

        List<String> tableList = new ArrayList<>();
        rowList = new ArrayList<>();
        Cursor c = DBSQLiteHelper.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null, mContext);

        try {
            if (c.moveToFirst()) {
                while (!c.isAfterLast()) {
                    tableList.add(c.getString(0));
                    c.moveToNext();
                }
            }
        } finally {
            if (c != null) {
                c.close();
            }
        }


        for (String tableName : tableList) {
            Cursor rowCursor = DBSQLiteHelper.queryDB(tableName, mContext);///
            try {
                List<String> fieldList = new ArrayList<>();
                if (rowCursor.moveToFirst()) {
                    for (String colName : rowCursor.getColumnNames()) {
                        fieldList.add(colName);
                    }
                }
                rowList.add(new DatabaseExpandableRow(tableName, fieldList));
            } finally {
                if (rowCursor != null) {
                    rowCursor.close();
                }
            }

        }
    }
}
