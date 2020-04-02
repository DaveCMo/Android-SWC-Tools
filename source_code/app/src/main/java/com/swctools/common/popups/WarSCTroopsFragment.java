package com.swctools.common.popups;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.button.MaterialButton;
import com.swctools.R;
import com.swctools.activity_modules.war_room.models.WarRoomListProvider;
import com.swctools.activity_modules.war_room.models.War_SC_Contents;
import com.swctools.activity_modules.war_room.recycler_adaptors.RecyclerAdaptor_WarSC;
import com.swctools.common.enums.BundleKeys;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class WarSCTroopsFragment extends DialogFragment {
    private static final String TAG = "WarSCTroopsFragment";
    private static final String KEY_tacticalCapItems = "KEY_tacticalCapItems";
    private EditText edt;
    private MaterialButton closewarSCBtn;
    private RecyclerView sullustTroopsRecycler;
    private String playerId, warId;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = View.inflate(getActivity(), R.layout.fragment_war_sc, null);
        closewarSCBtn = view.findViewById(R.id.closewarSCBtn);
        sullustTroopsRecycler = view.findViewById(R.id.sullustTroopsRecycler);

        builder.setView(view);
        playerId = getArguments().getString(BundleKeys.PLAYER_ID.toString());
        warId = getArguments().getString(BundleKeys.WAR_ID.toString());


        closewarSCBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();

            }
        });

        ArrayList<War_SC_Contents> war_sc_contents = WarRoomListProvider.getWarScContents(warId, playerId, getActivity().getApplicationContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        sullustTroopsRecycler.setLayoutManager(linearLayoutManager);
        RecyclerAdaptor_WarSC recyclerAdaptor_warSC = new RecyclerAdaptor_WarSC(war_sc_contents);
        sullustTroopsRecycler.setAdapter(recyclerAdaptor_warSC);
        recyclerAdaptor_warSC.notifyDataSetChanged();

        Dialog dialog = builder.create();


        return dialog;
    }


}
