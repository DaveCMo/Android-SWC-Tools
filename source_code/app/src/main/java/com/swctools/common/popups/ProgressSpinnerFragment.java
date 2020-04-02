package com.swctools.common.popups;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.swctools.R;
import com.swctools.common.enums.BundleKeys;

public class ProgressSpinnerFragment extends DialogFragment {
    public static final String TAG = "ProgressSpinnerFragment";
    private ProgressSpinnerInterface mActivityCallBack;
    private String message;
    private TextView progressMessage;
    private ProgressBar progressBar;
    private boolean indeterminate;

    //    public static String getTag(){
//        return TAG;
//    }
    public static ProgressSpinnerFragment getInstance(FragmentManager fragmentManager, String message) {
        ProgressSpinnerFragment progressSpinnerFragment = (ProgressSpinnerFragment) fragmentManager.findFragmentByTag(ProgressSpinnerFragment.TAG);
        if (progressSpinnerFragment == null) {
            progressSpinnerFragment = new ProgressSpinnerFragment();
            Bundle bundle = new Bundle();
            bundle.putString(BundleKeys.PROGRESS_MESSAGE.toString(), message);
            progressSpinnerFragment.setArguments(bundle);
//            progressSpinnerFragment.setRetainInstance(true);
            fragmentManager.beginTransaction().add(progressSpinnerFragment, TAG).commit();
        }
        return progressSpinnerFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        setRetainInstance(true);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (savedInstanceState == null) {
            try {
                message = getArguments().getString(BundleKeys.PROGRESS_MESSAGE.toString(), "");
            } catch (Exception e) {
                e.printStackTrace();
                message = "";
            }
            try {
                indeterminate = getArguments().getBoolean(BundleKeys.PROGRESS_INDETERMINATE.toString(), true);
            } catch (Exception e) {
                e.printStackTrace();
                indeterminate = true;
            }
        } else {
            message = savedInstanceState.getString(BundleKeys.PROGRESS_MESSAGE.toString());
            indeterminate = savedInstanceState.getBoolean(BundleKeys.PROGRESS_INDETERMINATE.toString());
        }


        View view = View.inflate(getActivity(), R.layout.fragment_progress_spinner, null);
        builder.setView(view);
        progressMessage = view.findViewById(R.id.progressMessage);
        progressBar = view.findViewById(R.id.fragment_progress_spinner);
        progressMessage.setText(message);
        Dialog dialog = builder.create();
        return dialog;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface ProgressSpinnerInterface {

        void setProgressIntederminate(boolean intederminate);
    }

    public void updateProgressMessage(String msg) {

        try {
            progressMessage.setText(msg);
            progressBar.setIndeterminate(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setProgress(int progress, int max) {
        try {
            progressBar.setIndeterminate(false);
            progressBar.setMax(max);
            progressBar.setProgress(progress);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mActivityCallBack = (ProgressSpinnerInterface) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(BundleKeys.PROGRESS_MESSAGE.toString(), message);
        outState.putBoolean(BundleKeys.PROGRESS_INDETERMINATE.toString(), indeterminate);
        super.onSaveInstanceState(outState);

    }
}
