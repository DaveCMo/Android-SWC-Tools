package com.swctools.common.popups;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import com.google.android.material.button.MaterialButton;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import com.swctools.R;
import com.swctools.base.PlayerSelectFragInterface;
import com.swctools.common.base_adaptors.PlayerListBaseAdaptor;
import com.swctools.common.enums.BundleKeys;
import com.swctools.util.StringUtil;

public class PlayerSelectionFragment extends DialogFragment {
    private static final String TAG = "PlayerSelectionFragment";
    private Context mContext;
    private PlayerSelectFragInterface mActivityCallBack;
    private Spinner alert_edit_spinner;
    private String playerId;
    private TextView spinnerAlertTitle;
    private MaterialButton spinnerCancel, spinnerSet;

    private PlayerListBaseAdaptor playerListBaseAdaptor;

    public static PlayerSelectionFragment getInstance(FragmentManager fragmentManager, String playerId) {

        PlayerSelectionFragment fragment = (PlayerSelectionFragment) fragmentManager.findFragmentByTag(TAG);
        if (fragment == null) {

            fragment = new PlayerSelectionFragment();
            Bundle bundle = new Bundle();
            bundle.putString(BundleKeys.PLAYER_ID.toString(), playerId);
            fragment.setArguments(bundle);
            fragmentManager.beginTransaction().add(fragment, TAG).commit();
        } else {

        }
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mActivityCallBack = (PlayerSelectFragInterface) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        playerId = getArguments().getString(BundleKeys.PLAYER_ID.toString(), "");
        View view = View.inflate(getActivity(), R.layout.spinner_view, null);

        alert_edit_spinner = view.findViewById(R.id.alert_edit_spinner);
        spinnerAlertTitle = view.findViewById(R.id.spinnerAlertTitle);
        spinnerAlertTitle.setText("Apply Layout to Player:");
        spinnerCancel = view.findViewById(R.id.spinnerCancel);
        spinnerSet = view.findViewById(R.id.spinnerSet);
        spinnerCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        spinnerSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (alert_edit_spinner.getSelectedItemPosition() == 0) {
                    mActivityCallBack.sendMessageFromFragment("Select something, Commander!");
                } else {
                    int position = alert_edit_spinner.getSelectedItemPosition();
                    mActivityCallBack.playerSelected(playerListBaseAdaptor.getPlayerList().get(position).playerId);

                }
                dismiss();
            }
        });

        builder.setView(view);

//        builder
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                })
//                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        if (alert_edit_spinner.getSelectedItemPosition() == 0) {
//                            mActivityCallBack.sendMessageFromFragment("Select something, Commander!");
//                        } else {
//                            int position = alert_edit_spinner.getSelectedItemPosition();
//                            mActivityCallBack.playerSelected(playerListBaseAdaptor.getPlayerList().get(position).playerId);
//
//                        }
//                    }
//                });
        Dialog dialog = builder.create();

        buildPlayerList();
        return dialog;
    }

    private void buildPlayerList() {


        playerListBaseAdaptor = new PlayerListBaseAdaptor(mContext);
        alert_edit_spinner.setAdapter(playerListBaseAdaptor);
        if (StringUtil.isStringNotNull(playerId)) {
            for (int i = 0; i < alert_edit_spinner.getCount(); i++) {
                if (playerListBaseAdaptor.getPlayerList().get(i).playerId.equalsIgnoreCase(playerId)) {
                    alert_edit_spinner.setSelection(i);
                }
            }
        }


    }


}
