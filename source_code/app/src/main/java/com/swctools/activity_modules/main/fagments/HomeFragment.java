package com.swctools.activity_modules.main.fagments;




import android.os.Bundle;
import androidx.annotation.Nullable;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.swctools.R;
import com.swctools.common.helpers.PlayerHelper;
import com.swctools.activity_modules.player.models.Battle;
import com.swctools.activity_modules.player.recycler_adaptors.RecyclerAdaptor_Defence;


import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    public static final String TAG = "SUMMARY";
    private PlayerHelper player;
//    private PlayerVisitor playerVisitor;
    private RecyclerView recyclerView;
    private HomeFragment myFragment;
    private RecyclerAdaptor_Defence mAdapter;
    private Bundle savedState = null;
    private TextView vstup;
    private final String STAV = "STAV?";
    private final String VSTUP = "VSUP";
    private List<Battle> battleList = new ArrayList<>();


    private TextView tv_PlayerName, tv_GuildName ;

    @Override

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        // Retain this Fragment across configuration changes in the host Activity.
        setRetainInstance(true);
    }
    public void setArguments(Bundle args) {
        super.setArguments(args);

    }

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
//        vstup = (TextView)v.findViewById(R.id.card_playerName);
        if(savedInstanceState != null && savedState == null) {
            savedState = savedInstanceState.getBundle(STAV);
        }
        if(savedState != null) {
            vstup.setText(savedState.getCharSequence(VSTUP));
        }
        savedState = null;

//        return v;


//        return inflater.inflate(R.layout.fragment_home, container, false);
        return  v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//
//                mAdapter = new RecyclerAdaptor_Defence(battleList, getActivity().getApplication());
//        recyclerView = (RecyclerView) getView().findViewById(R.id.home_defence_recycler_view);
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
//        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
//        recyclerView.setAdapter(mAdapter);
//        prepDefence();
//
//        tv_PlayerName = (TextView) getView().findViewById(R.id.card_playerName);
//
//        Bundle args = getArguments();
//        tv_PlayerName.setText("Here is a name");
////        tv_PlayerName.setText(args.getString("PlayerName"));

    }

//    public void updateObjs(PlayerVisitor playerVisitor){
//
//        String playerName = "[00ff00] Salvel";
//        tv_PlayerName.setText(playerName);
//    }


    public void prepDefence() {


//        Battle b = new Battle();
//        if(battleList.isEmpty()){
//            battleList.add(new DefenceResult(b));
//        } else {
//            battleList.set(0, new DefenceResult(b));
//        }
//        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        savedState = saveState(); /* vstup defined here for sure */
        vstup = null;
    }

    private Bundle saveState() { /* called either from onDestroyView() or onSaveInstanceState() */
        Bundle state = new Bundle();
        state.putCharSequence(VSTUP, vstup.getText());
        return state;
    }



}
