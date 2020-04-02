package com.swctools.layouts.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.swctools.R;
import com.swctools.common.enums.BundleKeys;
import com.swctools.common.helpers.PlayerHelper;
import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;
import com.swctools.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.Nullable;

public class LayoutFilterFragment extends DialogFragment {
    private static final String TAG = "LayoutFilterFragment";
    private LayoutFilterMessageTextViewInterface mListener;
    private Spinner layoutFilterPlayerSpinner, layoutFilterTagSpinner, layoutFilterTypeSpinner, factionFilterSpinner, favFilterSpinner;
    private String fPlayer, fFaction, fType, fTag, fFavYesNo;
    private List<Spinner> spinnerList;
    private List<PlayerHelper> playerList;
    private List<Integer> tagList;
    private List<Integer> typeList;
    private String wildCard;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (savedInstanceState == null) {

        }
        fPlayer = getArguments().getString(BundleKeys.LAYOUT_FILTER_PLAYER.toString());
        fFaction = getArguments().getString(BundleKeys.LAYOUT_FILTER_FACTION.toString());
        fType = getArguments().getString(BundleKeys.LAYOUT_FILTER_TYPE.toString());
        fTag = getArguments().getString(BundleKeys.LAYOUT_FILTER_TAG.toString());
        fFavYesNo = getArguments().getString(BundleKeys.LAYOUT_FILTER_FAV.toString());
        wildCard = getArguments().getString(BundleKeys.LAYOUT_FILTER_WILDCARD.toString());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = View.inflate(getActivity(), R.layout.layout_filter, null);
        spinnerList = new ArrayList<>();
        builder.setView(view);
        layoutFilterPlayerSpinner = (Spinner) view.findViewById(R.id.playerFilterSpinner);
        layoutFilterTagSpinner = (Spinner) view.findViewById(R.id.tagFilter);
        layoutFilterTypeSpinner = (Spinner) view.findViewById(R.id.typeFilter);
        factionFilterSpinner = (Spinner) view.findViewById(R.id.factionFilter);
        favFilterSpinner = (Spinner) view.findViewById(R.id.favFilter);
        spinnerList.add(layoutFilterPlayerSpinner);
        spinnerList.add(layoutFilterTagSpinner);
        spinnerList.add(layoutFilterTypeSpinner);
        spinnerList.add(factionFilterSpinner);
        spinnerList.add(favFilterSpinner);
        setSpinnerSelected(fFavYesNo, favFilterSpinner);
        setPlayerSpinner(fPlayer);
        setTagSpinner(fTag);
        setTypeSpinner(fType);
        setFactionSpinner(fFaction);
        String title = "FILTER LAYOUTS";
        final String cmd = getArguments().getString(BundleKeys.DIALOG_COMMAND.toString(), "");
        builder
                .setPositiveButton("APPLY", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        fPlayer = playerList.get(layoutFilterPlayerSpinner.getSelectedItemPosition()).playerId;
                        fFaction = factionFilterSpinner.getSelectedItem().toString();
                        if (layoutFilterTypeSpinner.getSelectedItemPosition() == 0) {
                            fType = wildCard;
                        } else {
                            fType = String.valueOf(typeList.get(layoutFilterTypeSpinner.getSelectedItemPosition()));
                        }
                        if (layoutFilterTagSpinner.getSelectedItemPosition() == 0) {
                            fTag = wildCard;
                        } else {
                            fTag = String.valueOf(tagList.get(layoutFilterTagSpinner.getSelectedItemPosition()));
                        }

                        fFavYesNo = favFilterSpinner.getSelectedItem().toString();
                        Bundle outState = new Bundle();
                        outState.putString(BundleKeys.LAYOUT_FILTER_PLAYER.toString(), fPlayer);
                        outState.putString(BundleKeys.LAYOUT_FILTER_FACTION.toString(), fFaction);
                        outState.putString(BundleKeys.LAYOUT_FILTER_TAG.toString(), fTag);
                        outState.putString(BundleKeys.LAYOUT_FILTER_TYPE.toString(), fType);
                        outState.putString(BundleKeys.LAYOUT_FILTER_FAV.toString(), fFavYesNo);
                        mListener.onFilterDialogPositiveClick(outState);
                    }
                })
                .setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //pass
//                        mListener.onDialogNegativeClick(MessageTextViewFragment.this);
                    }
                })
                .setNeutralButton("RESET", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        for (Spinner spinner : spinnerList) {
                            spinner.setSelection(0);
                        }
                        fPlayer = playerList.get(layoutFilterPlayerSpinner.getSelectedItemPosition()).playerId; // layoutFilterPlayerSpinner.getSelectedItem().toString();
                        fFaction = factionFilterSpinner.getSelectedItem().toString();
                        fType = layoutFilterTypeSpinner.getSelectedItem().toString();
                        fTag = layoutFilterTagSpinner.getSelectedItem().toString();
                        Bundle outState = new Bundle();
                        outState.putString(BundleKeys.LAYOUT_FILTER_PLAYER.toString(), fPlayer);
                        outState.putString(BundleKeys.LAYOUT_FILTER_FACTION.toString(), fFaction);
                        outState.putString(BundleKeys.LAYOUT_FILTER_TAG.toString(), fTag);
                        outState.putString(BundleKeys.LAYOUT_FILTER_TYPE.toString(), fType);
                        mListener.onFilterDialogResetClick(outState);
                        //pass
//                        mListener.onDialogNegativeClick(MessageTextViewFragment.this);
                    }
                })
                .setTitle(title);
        Dialog dialog = builder.create();
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    public interface LayoutFilterMessageTextViewInterface {
        void onFilterDialogPositiveClick(Bundle bundle);

        void onFilterDialogResetClick(Bundle bundle);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (LayoutFilterMessageTextViewInterface) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }


    private void setSpinnerSelected(@Nullable String arg, Spinner spinner) {
        if (StringUtil.isStringNotNull(arg)) {
            for (int i = 0; i < spinner.getCount(); i++) {
                if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(arg)) {
                    spinner.setSelection(i);
                }
            }
        }
    }

    private void setFactionSpinner(@Nullable String faction) {
        String[] list = getResources().getStringArray(R.array.factions);
        List<String> spinnerArray = new ArrayList<>();
        spinnerArray.add(wildCard);
        spinnerArray.addAll(Arrays.asList(list));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        factionFilterSpinner.setAdapter(adapter);
        setSpinnerSelected(faction, factionFilterSpinner);
    }

    private void setPlayerSpinner(@Nullable String playerId) {
        String[] columns = {
                DatabaseContracts.PlayersTable.PLAYERNAME,
                DatabaseContracts.PlayersTable.PLAYERID,
                DatabaseContracts.PlayersTable.PLAYERSECRET
        };
        List<String> spinnerArray = new ArrayList<>();
        playerList = new ArrayList<>();
        spinnerArray.add(wildCard);//
        playerList.add(new PlayerHelper(wildCard, wildCard, wildCard));
        Cursor cursor = DBSQLiteHelper.queryDB(DatabaseContracts.PlayersTable.TABLE_NAME, columns, getActivity());
        try {
            while (cursor.moveToNext()) {
                String pId = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.PlayersTable.PLAYERID));
                String pName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.PlayersTable.PLAYERNAME));
                String pSec = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.PlayersTable.PLAYERSECRET));
                PlayerHelper player = new PlayerHelper(pId, pSec, pName);
                spinnerArray.add(StringUtil.htmlRemovedGameName(pName));
                playerList.add(player);
            }
        } catch (Exception e) {

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        layoutFilterPlayerSpinner.setAdapter(adapter);
        if (StringUtil.isStringNotNull(playerId)) {
            for (int i = 0; i < layoutFilterPlayerSpinner.getCount(); i++) {
                if (playerList.get(i).playerId.equalsIgnoreCase(playerId)) {
                    layoutFilterPlayerSpinner.setSelection(i);
                }
            }
        }
    }


    private void setTagSpinner(@Nullable String tag) {

        String[] columns = {
                DatabaseContracts.LayoutTags.COLUMN_ID,
                DatabaseContracts.LayoutTags.LAYOUT_TAG};
        List<String> spinnerArray = new ArrayList<>();
        spinnerArray.add(wildCard);
        tagList = new ArrayList<>();
        tagList.add(0);
        Cursor cursor = DBSQLiteHelper.queryDB(DatabaseContracts.LayoutTags.TABLE_NAME, columns, getActivity());
        try {
            while (cursor.moveToNext()) {
                spinnerArray.add(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutTags.LAYOUT_TAG)));
                tagList.add(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutTags.COLUMN_ID)));
            }
        } catch (Exception e) {

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        layoutFilterTagSpinner.setAdapter(adapter);
        setSpinnerSelected(tag, layoutFilterTagSpinner);

        try {
            if (StringUtil.isNumeric(tag)) {
                int prevSelectedType = Integer.parseInt(tag);
                for (int i = 0; i < tagList.size(); i++) {
                    if (tagList.get(i) == prevSelectedType) {
                        layoutFilterTagSpinner.setSelection(i);
                    }
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }


    private void setTypeSpinner(@Nullable String type) {

        String[] columns = {
                DatabaseContracts.LayoutTypes.COLUMN_ID,
                DatabaseContracts.LayoutTypes.LAYOUT_TYPE};
        List<String> spinnerArray = new ArrayList<>();
        spinnerArray.add(wildCard);
        typeList = new ArrayList<>();
        typeList.add(0);
        Cursor cursor = DBSQLiteHelper.queryDB(DatabaseContracts.LayoutTypes.TABLE_NAME, columns, getActivity());
        try {
            while (cursor.moveToNext()) {
                spinnerArray.add(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutTypes.LAYOUT_TYPE)));
                typeList.add(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.LayoutTypes.COLUMN_ID)));
            }
        } catch (Exception e) {
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        layoutFilterTypeSpinner.setAdapter(adapter);

        try {
            if (StringUtil.isNumeric(type)) {
                int prevSelectedType = Integer.parseInt(type);
                for (int i = 0; i < typeList.size(); i++) {
                    if (typeList.get(i) == prevSelectedType) {
                        layoutFilterTypeSpinner.setSelection(i);
                    }
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

}
