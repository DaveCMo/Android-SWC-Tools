package com.swctools.layouts.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.swctools.R;
import com.swctools.common.enums.BundleKeys;
import com.swctools.common.view_adaptors.recycler_adaptors.RecyclerAdaptor_LayoutFolderNoMargin;
import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;
import com.swctools.interfaces.LayoutFoldersFragmentInterface;
import com.swctools.layouts.LayoutFolderHelper;
import com.swctools.layouts.models.LayoutFolderItem;
import com.swctools.util.Utils;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LayoutFoldersFragment extends DialogFragment {
    private static final String TAG = "LayoutFoldersFragment";
    private LayoutFoldersFragmentInterface mCallBack;
    private EditText edt;
    private View view;
    private int parentFolderId;
    private int folderToMove;
    private ImageView up;
    private Button addNewButton;
    private String cmd;
    private String title;
    private TextView folderName;
    private Context mContext;
    private LayoutFolderItem selectedLayoutFolderItem;
    private MaterialButton folderFragCancel, folderFragSave;
    private TextView folderFragTitle;


    public static LayoutFoldersFragment getInstance(FragmentManager fragmentManager) {

        LayoutFoldersFragment fragment = (LayoutFoldersFragment) fragmentManager.findFragmentByTag(TAG);
        if (fragment == null) {

            fragment = new LayoutFoldersFragment();
            fragmentManager.beginTransaction().add(fragment, TAG).commit();
        } else {
                //??
        }
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(FolderFragInstanceKeys.EDTTEXT.toString(), edt.getText().toString());
        outState.putInt(FolderFragInstanceKeys.PARENTFOLDERID.toString(), parentFolderId);
        outState.putInt(FolderFragInstanceKeys.FOLDERTOMOVE.toString(), folderToMove);
        outState.putString(FolderFragInstanceKeys.CMD.toString(), cmd);
        outState.putString(FolderFragInstanceKeys.TITLE.toString(), title);
        outState.putParcelable(FolderFragInstanceKeys.SELECETEDLAYOUTFOLDERITEM.toString(), selectedLayoutFolderItem);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        view = View.inflate(getActivity(), R.layout.fragment_layout_folder, null);
        view.findViewById(R.id.listItemLayoutFolder).setVisibility(View.GONE);
        edt = (EditText) view.findViewById(R.id.newFolderName);
        folderName = (TextView) view.findViewById(R.id.folderName);
        folderFragCancel = view.findViewById(R.id.folderFragCancel);
        folderFragSave = view.findViewById(R.id.folderFragSave);
        folderFragTitle = view.findViewById(R.id.folderFragTitle);
        builder.setView(view);

        //Handle instance states...
        if (savedInstanceState == null) {

            title = getArguments().getString(BundleKeys.DIALOG_TITLE.toString(), "Message");
            folderToMove = getArguments().getInt(BundleKeys.LAYOUT_FOLDER_TO_MOVE.toString(), 0);
            parentFolderId = LayoutFolderHelper.getParentFolderId(folderToMove, mContext);
            cmd = getArguments().getString(BundleKeys.DIALOG_COMMAND.toString(), "");
            int countLayouts = LayoutFolderHelper.layoutsInFolder(parentFolderId, getActivity());
            selectedLayoutFolderItem = new LayoutFolderHelper().getLayoutFolder(folderToMove, mContext);
        } else {
            parentFolderId = savedInstanceState.getInt(FolderFragInstanceKeys.PARENTFOLDERID.toString());
            title = savedInstanceState.getString(FolderFragInstanceKeys.TITLE.toString());
            folderToMove = savedInstanceState.getInt(FolderFragInstanceKeys.FOLDERTOMOVE.toString());
            cmd = savedInstanceState.getString(FolderFragInstanceKeys.CMD.toString());
            selectedLayoutFolderItem = savedInstanceState.getParcelable(FolderFragInstanceKeys.SELECETEDLAYOUTFOLDERITEM.toString());
            edt.setText(savedInstanceState.getString(FolderFragInstanceKeys.EDTTEXT.toString()));
        }
        setSelectedFolder(selectedLayoutFolderItem);
        setRecycler(folderToMove);
        addNewButton = (Button) view.findViewById(R.id.addNewFolder);
        addNewButton.setOnClickListener(new AddNewBtnListener());
        up = (ImageView) view.findViewById(R.id.up);
        up.setOnClickListener(new UpClickListener());

        folderFragCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        folderFragSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallBack.confirmFolderSelection(parentFolderId, cmd);
                dismiss();
            }
        });
//        builder.setTitle(title);
        folderFragTitle.setText(title);
        Dialog dialog = builder.create();

        return dialog;
    }

    public void setRecycler(int parentFldrId) {

        this.parentFolderId = parentFldrId;
        RecyclerView layoutFolderRecycler = (RecyclerView) view.findViewById(R.id.layoutFolderRecycler);
        List<LayoutFolderItem> layoutFolderItems = new ArrayList<>();

        String selection = DatabaseContracts.LayoutFolders.PARENT_FOLDER_ID + " = ? ";
        String[] selectionArgs = {String.valueOf(parentFldrId)};
        Cursor cursor = DBSQLiteHelper.queryDB(DatabaseContracts.LayoutFolders.TABLE_NAME, null, selection, selectionArgs, mContext);
        try {

            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutFolders.COLUMN_ID));
                String n = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutFolders.FOLDER_NAME));
                int pid = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutFolders.PARENT_FOLDER_ID));
                int countLayouts = LayoutFolderHelper.layoutsInFolder(id, getActivity());
                layoutFolderItems.add(new LayoutFolderItem(id, n, pid, countLayouts));

            }

            RecyclerAdaptor_LayoutFolderNoMargin recyclerAdaptorLayoutFolder = new RecyclerAdaptor_LayoutFolderNoMargin(layoutFolderItems, getActivity());
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            layoutFolderRecycler.setLayoutManager(mLayoutManager);
            layoutFolderRecycler.setItemAnimator(new DefaultItemAnimator());
            layoutFolderRecycler.setAdapter(recyclerAdaptorLayoutFolder);
            recyclerAdaptorLayoutFolder.notifyDataSetChanged();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public void setSelectedFolder(LayoutFolderItem layoutFolderItem) {
        selectedLayoutFolderItem = layoutFolderItem;
        view.findViewById(R.id.listItemLayoutFolder).setVisibility(View.VISIBLE);
        TextView layoutName = (TextView) view.findViewById(R.id.folderName);
        TextView folderContentsCount = (TextView) view.findViewById(R.id.folderContentsCount);

        layoutName.setText(selectedLayoutFolderItem.getFolderName());
        folderContentsCount.setText(selectedLayoutFolderItem.getCountLayoutsStr());


    }

    public void setParentFolderId(int parentFolderId) {
        this.parentFolderId = parentFolderId;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity.getApplicationContext();
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mCallBack = (LayoutFoldersFragmentInterface) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    private enum FolderFragInstanceKeys {
        EDTTEXT, PARENTFOLDERID, FOLDERTOMOVE, CMD, TITLE, SELECETEDLAYOUTFOLDERITEM;
    }


    class UpClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
//            parentFolderId = LayoutFolderHelper.getParentFolderId(parentFolderId, getActivity());
            mCallBack.upFolderInFragment();
//            setRecycler(parentFolderId);
        }
    }

    class AddNewBtnListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String newFolderName = edt.getText().toString();
            edt.setText(null);
            mCallBack.addFolderFromFragment(newFolderName, parentFolderId);
            //FUCK OFF KEYBOARD
            edt.clearFocus();
            addNewButton.requestFocus();
            Utils.hideKeyboard(getActivity());
        }
    }

}
