package com.swctools.layouts.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import com.google.android.material.button.MaterialButton;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.view.View;
import android.widget.Button;

import com.swctools.R;
import com.swctools.layouts.LayoutTagTypeHelper;
import com.swctools.interfaces.LayoutTagFragmentInterface;
import com.swctools.layouts.models.LayoutTag;
import com.swctools.activity_modules.layout_manager.recycler_adaptors.RecyclerAdaptor_LayoutTagPillList;
import com.swctools.common.view_adaptors.recycler_adaptors.RecyclerAdaptor_LayoutTagTextList;

import java.util.ArrayList;

public class LayoutTagFragment extends DialogFragment implements SearchView.OnQueryTextListener {
    public static final String TAG = "LayoutTagFragment";
    public static final String CURRENTSELECTEDFRAGS = "CURRENTSELECTEDFRAGS";
    private Button addTagButton;
    private SearchView searchTagView;
    private RecyclerView selectedTags, layoutTagList;
    private RecyclerView.LayoutManager horizLayoutManager;
    private RecyclerAdaptor_LayoutTagTextList availableListAdaptor;
    private ArrayList<LayoutTag> selectedTagsList;
    private ArrayList<LayoutTag> availableTagList;
    private ArrayList<LayoutTag> searchedList;
    private RecyclerAdaptor_LayoutTagPillList selectedListAdaptor;
    private Context mContext;
    private LayoutTagFragmentInterface mActivityCallBack;
    private MaterialButton manageTagCancel, manageTagSave;


    public static LayoutTagFragment getInstance(FragmentManager fragmentManager) {

        LayoutTagFragment layoutTagFragment = (LayoutTagFragment) fragmentManager.findFragmentByTag(TAG);
        if (layoutTagFragment == null) {

            layoutTagFragment = new LayoutTagFragment();
            fragmentManager.beginTransaction().add(layoutTagFragment, TAG).commit();
        }

        return layoutTagFragment;
    }

    private void setSelectedTag() {
        selectedListAdaptor = new RecyclerAdaptor_LayoutTagPillList(selectedTagsList, true, mContext);
        selectedTags.setAdapter(selectedListAdaptor);
        selectedListAdaptor.notifyDataSetChanged();


        selectedListAdaptor.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                horizLayoutManager.smoothScrollToPosition(selectedTags, null, selectedListAdaptor.getItemCount());
            }

            @Override
            public void onChanged() {
                horizLayoutManager.smoothScrollToPosition(selectedTags, null, selectedListAdaptor.getItemCount());
            }
        });
    }

    public void addNewTag(LayoutTag layoutTag) {
        selectedTagsList.add(layoutTag);
        selectedListAdaptor.notifyDataSetChanged();

    }

    public void addTagToSelected(LayoutTag layoutTag, int i) {
        try {

            selectedTagsList.add(layoutTag);
            selectedListAdaptor.notifyDataSetChanged();

            availableTagList.remove(i);
            availableListAdaptor.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void removeSelectedTag(LayoutTag layoutTag, int i) {
        try {

            selectedTagsList.remove(i);
            selectedListAdaptor.notifyDataSetChanged();

            availableTagList.add(layoutTag);
            availableListAdaptor.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        //convert to lower case to match on case
        newText = newText.toLowerCase();
        if (newText.length() > 0) {
            addTagButton.setVisibility(View.VISIBLE);
            searchedList = new ArrayList<>();
            for (int i = 0; i < availableTagList.size(); i++) {
                if (availableTagList.get(i).tagString.toLowerCase().contains(newText)) {
                    searchedList.add(availableTagList.get(i));
                }
            }
            availableListAdaptor = new RecyclerAdaptor_LayoutTagTextList(searchedList, mContext);
            layoutTagList.setAdapter(availableListAdaptor);
            availableListAdaptor.notifyDataSetChanged();
        } else {
            addTagButton.setVisibility(View.GONE);
            availableListAdaptor = new RecyclerAdaptor_LayoutTagTextList(availableTagList, mContext);
            layoutTagList.setAdapter(availableListAdaptor);
            availableListAdaptor.notifyDataSetChanged();
        }

        return false;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mActivityCallBack = (LayoutTagFragmentInterface) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        selectedTagsList = new ArrayList<>();
        availableTagList = new ArrayList<>();
        ArrayList<LayoutTag> receivedList = getArguments().getParcelableArrayList(CURRENTSELECTEDFRAGS);
        selectedTagsList.addAll(receivedList);
        View view = View.inflate(getActivity(), R.layout.fragment_add_layout_tag, null);
        searchTagView = view.findViewById(R.id.searchTagView);
        selectedTags = view.findViewById(R.id.selectedTags);
        layoutTagList = view.findViewById(R.id.layoutTagList);
        addTagButton = view.findViewById(R.id.addTagButton);
        addTagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivityCallBack.addTag(searchTagView.getQuery().toString());
            }
        });
        manageTagCancel = view.findViewById(R.id.manageTagCancel);
        manageTagSave = view.findViewById(R.id.manageTagSave);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        layoutTagList.setLayoutManager(mLayoutManager);
        layoutTagList.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));
        layoutTagList.setItemAnimator(new DefaultItemAnimator());

        horizLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        selectedTags.setLayoutManager(horizLayoutManager);
        selectedTags.setItemAnimator(new DefaultItemAnimator());


        builder.setView(view);
        manageTagCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                availableTagList.clear();
                selectedTagsList.clear();
                mActivityCallBack.cancelTagSelection();
                dismiss();
            }
        });

        manageTagSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivityCallBack.saveTagSelection(selectedTagsList);
                dismiss();
            }
        });
        Dialog dialog = builder.create();
        setTagList();
        setVew(selectedTagsList);
        searchTagView.setOnQueryTextListener(this);
        return dialog;
    }

    public void setVew(ArrayList<LayoutTag> selectedTagsList) {
        this.selectedTagsList = selectedTagsList;
        setTagList();
        setSelectedTag();
    }


    private void setTagList() {

        availableTagList = LayoutTagTypeHelper.getLayoutList(selectedTagsList, mContext);
        availableListAdaptor = new RecyclerAdaptor_LayoutTagTextList(availableTagList, mContext);
        layoutTagList.setAdapter(availableListAdaptor);
        availableListAdaptor.notifyDataSetChanged();
    }


}
