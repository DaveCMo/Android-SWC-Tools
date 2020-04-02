package com.swctools.activity_modules.player.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.swctools.R;
import com.swctools.common.enums.BundleKeys;
import com.swctools.activity_modules.player.models.Conflict_Data_Model;
import com.swctools.activity_modules.player.recycler_adaptors.RecyclerAdaptor_Conflict;

import java.util.ArrayList;

public class FragmentPlayerConflictTracker extends Fragment {
    private static final String TAG = "FrgPlayerConflictTracker";
    private static final String CONFLICTDATA = "CONFLICTDATA";
    private Button refreshConflicts;
    private RecyclerView conflictRecycler;
    private FragementConflictTrackerInterface activityCallBack;
    private Context mContext;
    private ArrayList<Conflict_Data_Model> conflictDataModels;
    //Defence log:;


    public static FragmentPlayerConflictTracker newInstance() {
        return new FragmentPlayerConflictTracker();
    }


    public static FragmentPlayerConflictTracker getInstance(FragmentManager fragmentManager, String playerId) {
        FragmentPlayerConflictTracker fragmentPlayerConflictTracker = (FragmentPlayerConflictTracker) fragmentManager.findFragmentByTag(TAG);
        if (fragmentPlayerConflictTracker == null) {
            ;
            fragmentPlayerConflictTracker = new FragmentPlayerConflictTracker();
            Bundle args = new Bundle();
            args.putString(BundleKeys.PLAYER_ID.toString(), playerId);
            fragmentPlayerConflictTracker.setArguments(args);
            fragmentManager.beginTransaction().add(fragmentPlayerConflictTracker, TAG).commit();
        }
        return fragmentPlayerConflictTracker;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        activityCallBack = (FragementConflictTrackerInterface) context;
//        mCallback = (DefenceLogFragmentInterface) context;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (conflictDataModels != null) {
            outState.putParcelableArrayList(CONFLICTDATA, conflictDataModels);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
        activityCallBack =null;
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setRetainInstance(true);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);

        View view = inflater.inflate(R.layout.fragment_conflict_tracker, container, false);
        setViewItems(view);
        if (savedInstanceState != null) {
            this.conflictDataModels = savedInstanceState.getParcelableArrayList(CONFLICTDATA);
            updateConflictRecycler(this.conflictDataModels);
        }
        return view;
    }

    public void setViewItems(View view) {
//        playerDetails_battlelog_pager = (ViewPager) view.findViewById(R.id.playerDetails_battlelog_pager);
        refreshConflicts = view.findViewById(R.id.refreshConflicts);
        conflictRecycler = view.findViewById(R.id.conflictRecycler);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);// GridLayoutManager(mContext, 2, GridLayoutManager.HORIZONTAL, false);
        conflictRecycler.setLayoutManager(mLayoutManager);
        conflictRecycler.setItemAnimator(new DefaultItemAnimator());
        refreshConflicts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityCallBack.refreshConflictData();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void updateConflictRecycler(ArrayList<Conflict_Data_Model> conflictDataModels) {
        this.conflictDataModels = conflictDataModels;
        if(this.conflictDataModels !=null){

            RecyclerAdaptor_Conflict recyclerAdaptorConflict = new RecyclerAdaptor_Conflict(conflictDataModels, mContext);
            conflictRecycler.setAdapter(recyclerAdaptorConflict);
            recyclerAdaptorConflict.notifyDataSetChanged();
        }
    }

    public interface FragementConflictTrackerInterface {
        void refreshConflictData();
    }


}
