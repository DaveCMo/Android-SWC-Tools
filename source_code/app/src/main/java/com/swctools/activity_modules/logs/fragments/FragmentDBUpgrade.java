package com.swctools.activity_modules.logs.fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swctools.R;
import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;
import com.swctools.activity_modules.logs.view_adaptors.RecyclerAdaptor_DBUpgrade;
import com.swctools.activity_modules.logs.models.DBUpgradeRowData;

import java.util.ArrayList;
import java.util.List;

public class FragmentDBUpgrade extends Fragment {
    private static final String TAG = "FragmentDBUpgrade";
    private RecyclerView mDbUpgradeRecyclerView;
    private RecyclerAdaptor_DBUpgrade mdRecyclerAdaptorDBUpgrade;
    private List<DBUpgradeRowData> dbUpgradeRowDataList;
    private Context mContext;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_db_update_log, container, false);
        mDbUpgradeRecyclerView = view.findViewById(R.id.dbUpdateLogRecycler);

        SetdbUpgradeRowDataList();
        mdRecyclerAdaptorDBUpgrade = new RecyclerAdaptor_DBUpgrade(dbUpgradeRowDataList, getContext());

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mDbUpgradeRecyclerView.setLayoutManager(mLayoutManager);
        mDbUpgradeRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mDbUpgradeRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        mDbUpgradeRecyclerView.setAdapter(mdRecyclerAdaptorDBUpgrade);
        mdRecyclerAdaptorDBUpgrade.notifyDataSetChanged();
        return view;
    }


    public void SetdbUpgradeRowDataList() {
        dbUpgradeRowDataList = new ArrayList<>();
        Cursor cursor = DBSQLiteHelper.queryDB(DatabaseContracts.DatabaseUpgradeLog.TABLE_NAME, mContext);// database.query(DatabaseContracts.DatabaseUpgradeLog.TABLE_NAME, null, null, null, null, null, null);
        try {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.DatabaseUpgradeLog.COLUMN_ID));
                int versId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.DatabaseUpgradeLog.DB_UPGRADE_VERSION));
                String appCode = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.DatabaseUpgradeLog.DB_UPGRADE_CODE));
                String message = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.DatabaseUpgradeLog.DB_UPGRADE_MESSAGE));
                DBUpgradeRowData dbUpgradeRowData = new DBUpgradeRowData(id, versId, appCode, message);
                dbUpgradeRowDataList.add(dbUpgradeRowData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
