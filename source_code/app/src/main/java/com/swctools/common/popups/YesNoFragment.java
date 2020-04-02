package com.swctools.common.popups;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.swctools.R;
import com.swctools.common.enums.BundleKeys;
import com.swctools.interfaces.YesNoAlertCallBack;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

public class YesNoFragment extends DialogFragment {
    private static final String TAG = "YesNoFragment";
    private String ACTIVITY_COMMAND;
    private YesNoAlertCallBack yesNoAlertCallBack;
    private TextView yesNoMessage, yesnotitle;

    private Context context;
    private MaterialButton yesNoCancel, yesNoSave;

    public static YesNoFragment getInstance(FragmentManager fragmentManager, Bundle bundle) {
        YesNoFragment progressSpinnerFragment = (YesNoFragment) fragmentManager.findFragmentByTag(ProgressSpinnerFragment.TAG);
        if (progressSpinnerFragment == null) {

            progressSpinnerFragment = new YesNoFragment();
            progressSpinnerFragment.setArguments(bundle);
            progressSpinnerFragment.setRetainInstance(true);
            fragmentManager.beginTransaction().add(progressSpinnerFragment, TAG).commitAllowingStateLoss();
        }
        return progressSpinnerFragment;
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = View.inflate(getActivity(), R.layout.fragment_yes_no, null);
        String title = getArguments().getString(BundleKeys.DIALOG_TITLE.toString(), "Message");
        String message = getArguments().getString(BundleKeys.DIALOG_MESSAGE.toString(), "");

        ACTIVITY_COMMAND = getArguments().getString(BundleKeys.DIALOG_COMMAND.toString(), "");

        yesNoMessage = view.findViewById(R.id.yesNoMessage);
        yesNoMessage.setText(message);
        yesNoCancel = view.findViewById(R.id.yesNoCancel);
        yesNoSave = view.findViewById(R.id.yesNoSave);
        yesnotitle = view.findViewById(R.id.yesnotitle);


        yesNoCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


        yesNoSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yesNoAlertCallBack.onYesNoDialogYesClicked(ACTIVITY_COMMAND, savedInstanceState);
                dismiss();
            }
        });
        yesnotitle.setText(title);

        builder.setView(view);

        return builder.create();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            yesNoAlertCallBack = (YesNoAlertCallBack) context;
            this.context = context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement YesNoAlertCallBack");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        this.context = null;
        yesNoAlertCallBack = null;

    }
}
