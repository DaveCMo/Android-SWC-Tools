package com.swctools.common.popups;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.swctools.R;
import com.swctools.common.enums.BundleKeys;

import java.util.Arrays;
import java.util.List;

public class MessageSpinnerViewFragment extends DialogFragment {
    private static final String TAG = "MessageSpinnerViewFragment";
    private MessageSpinnerFragmentInterface mListener;
    private Spinner edt;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if(savedInstanceState==null){

        }
//        setContentView(R.layout.activity_save_layout_);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = View.inflate(getActivity(), R.layout.spinner_view, null);
        edt = (Spinner) view.findViewById(R.id.alert_edit_spinner);
        builder.setView(view);
        String title = getArguments().getString(BundleKeys.DIALOG_TITLE.toString(), "Message");
        String message = getArguments().getString(BundleKeys.DIALOG_MESSAGE.toString(), "");
        final String cmd = getArguments().getString(BundleKeys.DIALOG_COMMAND.toString(), "");
        String[] myResArray = getArguments().getStringArray(BundleKeys.DIALOG_SPINNER_STRING_ARRAY.toString());
        setSpinner(myResArray);
        builder.setMessage(message)
                .setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mListener.onSpinnerDialogPositiveClick(edt.getSelectedItem().toString(), cmd);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener()

                {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //pass
//                        mListener.onDialogNegativeClick(MessageTextViewFragment.this);
                    }
                })
                .setTitle(title);
        Dialog dialog = builder.create();
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }


public void setSpinner(String[] myResArray){
//    String[] myResArray = getResources().getStringArray(R.array.short_date_formats);
    List<String> spinnerArray = Arrays.asList(myResArray);
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, spinnerArray);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    edt.setAdapter(adapter);



}

    public interface MessageSpinnerFragmentInterface {
        void onSpinnerDialogPositiveClick(String msg, String cmd);

//        public void onDialogNegativeClick(DialogFragment dialog);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (MessageSpinnerFragmentInterface) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }
}
