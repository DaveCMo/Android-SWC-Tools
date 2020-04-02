package com.swctools.common.popups;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.swctools.common.enums.BundleKeys;

public class AlertFragmentWithTextView extends DialogFragment {
    private static final String TAG = "ALERTFRAGTEXTVIEW";
    private TextView textMessage;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String title = getArguments().getString(BundleKeys.DIALOG_TITLE.toString(), "");
        String message = getArguments().getString(BundleKeys.DIALOG_MESSAGE.toString(), "");
        builder.setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setTitle(title);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            builder.setMessage(Html.fromHtml(message, Html.FROM_HTML_MODE_LEGACY));
        } else {
            builder.setMessage(Html.fromHtml(message));
        }
        return builder.create();
    }

}
