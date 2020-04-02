package com.swctools.common.popups;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import com.google.android.material.button.MaterialButton;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.swctools.R;
import com.swctools.base.MessageTextViewInterface;
import com.swctools.common.enums.BundleKeys;

public class MessageTextViewFragment extends DialogFragment {
    private static final String TAG = "MessageTextViewFragment";
    private MessageTextViewInterface mListener;
    private EditText edt;
    private String cmd, title, message, prevValue;
    private MaterialButton fragTextCancel, fragTextSave;
    private TextView messageTextViewTitle;
    private Context context;
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(MsgTxtFrag.EDTTEXT.toString(), edt.getText().toString());
        outState.putString(MsgTxtFrag.CMD.toString(), cmd);
        outState.putString(MsgTxtFrag.TITLE.toString(), title);
        outState.putString(MsgTxtFrag.MESSAGE.toString(), message);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.AppTheme_FragmentPopup);


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = View.inflate(getActivity(), R.layout.fragment_text_view, null);
        edt =  view.findViewById(R.id.alert_edit_text);
        fragTextCancel = view.findViewById(R.id.fragTextCancel);
        fragTextSave = view.findViewById(R.id.fragTextSave);
        messageTextViewTitle = view.findViewById(R.id.messageTextViewTitle);

        builder.setView(view);

        if (savedInstanceState == null) {
            title = getArguments().getString(BundleKeys.DIALOG_TITLE.toString(), "Message");
            message = getArguments().getString(BundleKeys.DIALOG_MESSAGE.toString(), "");
            cmd = getArguments().getString(BundleKeys.DIALOG_COMMAND.toString(), "");
            prevValue = getArguments().getString(BundleKeys.DIALOG_VALUE.toString(), "");
        } else {
            title = savedInstanceState.getString(MsgTxtFrag.TITLE.toString(), "Message");
            message = savedInstanceState.getString(MsgTxtFrag.MESSAGE.toString(), "");
            cmd = savedInstanceState.getString(MsgTxtFrag.CMD.toString(), "");
            prevValue = savedInstanceState.getString(MsgTxtFrag.EDTTEXT.toString(), "");
        }

        messageTextViewTitle.setText(title);
        fragTextSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edt.getWindowToken(), 0);
                mListener.onTextViewDialogPositiveClick(edt.getText().toString(), cmd);

                dismiss();
            }
        });
        fragTextCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();

            }
        });
//        builder.setMessage(message)
//                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                        mListener.onTextViewDialogPositiveClick(edt.getText().toString(), cmd);
//                    }
//                })
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
//
//                {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//
//                    }
//                });
//        builder.setTitle(title);
        Dialog dialog = builder.create();

        edt.setText(prevValue);

        return dialog;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        context = null;
        mListener = null;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        context = activity;
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (MessageTextViewInterface) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    enum MsgTxtFrag {
        EDTTEXT,
        TITLE,
        MESSAGE,
        CMD;
    }


}
