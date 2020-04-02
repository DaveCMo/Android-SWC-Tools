package com.swctools.activity_modules.save_layout.fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.swctools.R;
import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;
import com.swctools.common.enums.BundleKeys;
import com.swctools.layouts.LayoutHelper;
import com.swctools.activity_modules.layout_manager.models.LayoutManagerListProvider;
import com.swctools.layouts.models.LayoutRecord;
import com.swctools.layouts.models.LayoutVersion;
import com.swctools.activity_modules.save_layout.recycler_adaptors.RecyclerAdaptor_UpdateLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class FragmentUpdateLayout extends Fragment {
    private static final String TAG = "FragmentUpdateLayout";
    String playerId = "%";
    String faction = "%";
    String layoutType = "%";
    String layoutTag = "%";
    private TextView updateLayout_SelectedLayoutName;
    private Button updateLayout_Update, updateLayout_NewVers;
    private Spinner updateLayout_Versions;
    private RecyclerAdaptor_UpdateLayout recyclerAdaptorUpdateLayout;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView recyclerViewecyclerView;
    private List<LayoutRecord> layoutDisplays = new ArrayList<>();
    private FragmentUpdateLayoutInterface mCallback;
    private String layoutJSON;
    private int layoutId = 0;
    // Clear reference to host Activity.
    private Context mContext;

    public static FragmentUpdateLayout newInstance() {
        return new FragmentUpdateLayout();
    }

    public static FragmentUpdateLayout getInstance(FragmentManager fragmentManager, String playerId) {
        FragmentUpdateLayout fragmentPlayerDetails = (FragmentUpdateLayout) fragmentManager.findFragmentByTag(TAG);
        if (fragmentPlayerDetails == null) {
            ;
            fragmentPlayerDetails = new FragmentUpdateLayout();
            Bundle args = new Bundle();
            args.putString(BundleKeys.PLAYER_ID.toString(), playerId);
            fragmentPlayerDetails.setArguments(args);
            fragmentManager.beginTransaction().add(fragmentPlayerDetails, TAG).commit();
        }
        return fragmentPlayerDetails;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        View view = inflater.inflate(R.layout.update_layout_fragment, container, false);
        try {
            layoutJSON = getArguments().getString(BundleKeys.LAYOUT_JSON_STRING.toString());
        } catch (Exception e) {

        }
        setAllControls(view);
        return view;
    }

    private void setAllControls(View view) {
        updateLayout_SelectedLayoutName = (TextView) view.findViewById(R.id.updateLayout_SelectedLayoutName);
        updateLayout_Versions = (Spinner) view.findViewById(R.id.updateLayout_Versions);
        updateLayout_Update = (Button) view.findViewById(R.id.updateLayout_Update);
        updateLayout_Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(layoutId == 0)) {
                    try {
                        int layoutVersion = Integer.parseInt(updateLayout_Versions.getSelectedItem().toString());
                        mCallback.overWriteLayoutVersion(layoutId, layoutVersion);
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "Error selecting version number to update: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        updateLayout_NewVers = (Button) view.findViewById(R.id.updateLayout_NewVers);
        updateLayout_NewVers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(layoutId == 0)) {

                    mCallback.newVersion(layoutId);
                }
            }
        });

        recyclerViewecyclerView = (RecyclerView) view.findViewById(R.id.updateLayout_RecyclerView);


        setLayoutList(playerId, faction, layoutType, layoutTag);


    }

    public void setLayoutSelected(int LAYOUT_ID) {

        layoutId = LAYOUT_ID;
        String whereClause = DatabaseContracts.LayoutTable.COLUMN_ID + " = ?";
        String[] whereArgs = {String.valueOf(LAYOUT_ID)};
        Cursor cursor = DBSQLiteHelper.queryDB(DatabaseContracts.LayoutTable.TABLE_NAME, null, whereClause, whereArgs, mContext);
        try {
            while (cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutTable.COLUMN_ID));

                LayoutRecord layoutRecord = LayoutManagerListProvider.getLayoutRecord(layoutId, mContext);
                updateLayout_SelectedLayoutName.setText(layoutRecord.getLayoutName());
                setVersionList(layoutRecord);
                break;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void setVersionList(LayoutRecord layoutRecord) {
        ArrayAdapter<LayoutVersion> adapter = new ArrayAdapter<LayoutVersion>(getActivity(),
                android.R.layout.simple_spinner_item, LayoutHelper.getLayoutVersions(layoutRecord.getLayoutId(), mContext));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        updateLayout_Versions.setAdapter(adapter);
        updateLayout_Versions.setSelection(updateLayout_Versions.getCount() - 1);
    }

    public void setLayoutList(String playerId, String faction, String layoutType, String layoutTag) {
        layoutDisplays.clear();

        LayoutManagerListProvider layoutManagerListProvider = new LayoutManagerListProvider(mContext);
        Cursor cursor = layoutManagerListProvider.getLayoutListCursor(playerId, faction, layoutType, layoutTag, mContext);

        try {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutTable.COLUMN_ID));

                LayoutRecord layoutRecord = LayoutManagerListProvider.getLayoutRecord(id, mContext);
                layoutDisplays.add(layoutRecord);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        recyclerAdaptorUpdateLayout = new RecyclerAdaptor_UpdateLayout(layoutDisplays, getActivity());
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewecyclerView.setLayoutManager(mLayoutManager);
        recyclerViewecyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerViewecyclerView.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));
        recyclerViewecyclerView.setAdapter(recyclerAdaptorUpdateLayout);
        recyclerAdaptorUpdateLayout.notifyDataSetChanged();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mCallback = (FragmentUpdateLayoutInterface) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
        mCallback = null;

    }

    public interface FragmentUpdateLayoutInterface {


        void overWriteLayoutVersion(int layoutID, int layoutVersion);

        void newVersion(int layoutId);


    }

}
