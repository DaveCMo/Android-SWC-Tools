package com.swctools.common.popups;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import com.google.android.material.button.MaterialButton;

import android.view.View;
import android.widget.TextView;

import com.swctools.R;
import com.swctools.common.enums.BundleKeys;

public class AlertFragment extends DialogFragment {
    private static final String TAG = "AlertFragment";
    private TextView alertMessage, alertTitle ;
    private MaterialButton alertOKAY;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = View.inflate(getActivity(), R.layout.fragment_alert_message, null);
        alertMessage = view.findViewById(R.id.alertMessage);
        alertTitle = view.findViewById(R.id.alertTitle);
        alertOKAY = view.findViewById(R.id.alertOKAY);
        String title = getArguments().getString(BundleKeys.DIALOG_TITLE.toString(), "Message");
        String message = getArguments().getString(BundleKeys.DIALOG_MESSAGE.toString(), "");

        alertMessage.setText(message);
        alertTitle.setText(title);
        builder.setView(view);
        alertOKAY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return builder.create();
    }



}
