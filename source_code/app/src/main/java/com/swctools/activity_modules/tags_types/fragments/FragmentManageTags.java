package com.swctools.activity_modules.tags_types.fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.swctools.R;
import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;
import com.swctools.activity_modules.tags_types.InterfaceAddTypeTagFragment;
import com.swctools.activity_modules.tags_types.models.LayoutTypeTagContainer;
import com.swctools.activity_modules.tags_types.recycler_adaptors.RecyclerAdaptor_LayoutTypeTag;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FragmentManageTags extends Fragment {
    private static final String TAG = "FragmentManageTags";
    //Defence log:
    private RecyclerAdaptor_LayoutTypeTag recyclerAdaptorLayoutTypeTag;
    private RecyclerView mTagRecyclerView;
    private List<LayoutTypeTagContainer> layoutTypeTagContainers;
    private InterfaceAddTypeTagFragment mMainActivityCallBack;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivityCallBack = (InterfaceAddTypeTagFragment) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // Clear reference to host Activity.
        mMainActivityCallBack = null;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        View view = inflater.inflate(R.layout.fragment_manage_tag, container, false);
        mTagRecyclerView = view.findViewById(R.id.managetagRecycler);
        Button addNewTypeTag = (Button) view.findViewById(R.id.addNewTypeTag);
        addNewTypeTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMainActivityCallBack.addNew(InterfaceAddTypeTagFragment.ADD_TAG);
            }
        });
        buildTagListForRecycler();
        return view;
    }

    public void buildTagListForRecycler() {

        Cursor cursor = DBSQLiteHelper.queryDB(DatabaseContracts.LayoutTags.TABLE_NAME, getActivity());
        try {
            layoutTypeTagContainers = new ArrayList<>();
            while (cursor.moveToNext()) {

                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutTags.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutTags.LAYOUT_TAG));
                LayoutTypeTagContainer layoutTypeTagContainer = new LayoutTypeTagContainer(id, name);
                layoutTypeTagContainers.add(layoutTypeTagContainer);


            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        recyclerAdaptorLayoutTypeTag = new RecyclerAdaptor_LayoutTypeTag(layoutTypeTagContainers, RecyclerAdaptor_LayoutTypeTag.LayoutTagTypeRowInterface.TAG, getActivity());

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mTagRecyclerView.setLayoutManager(mLayoutManager);
        mTagRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mTagRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        mTagRecyclerView.setAdapter(recyclerAdaptorLayoutTypeTag);


        recyclerAdaptorLayoutTypeTag.notifyDataSetChanged();
    }


}
