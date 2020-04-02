package com.swctools.activity_modules.save_layout.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.swctools.R;
import com.swctools.common.base_adaptors.FactionListBaseAdaptor;
import com.swctools.common.base_adaptors.PlayerListBaseAdaptor;
import com.swctools.common.enums.BundleKeys;
import com.swctools.common.enums.DatabaseMethods;
import com.swctools.common.model_list_providers.LayoutImageBytesListProvider;
import com.swctools.common.view_adaptors.recycler_adaptors.RecyclerAdaptor_LayoutFolderBreadCrumb;
import com.swctools.database.DatabaseContracts;
import com.swctools.layouts.LayoutFolderHelper;
import com.swctools.layouts.models.LayoutFolderItem;
import com.swctools.layouts.models.LayoutTag;
import com.swctools.activity_modules.layout_manager.recycler_adaptors.RecyclerAdaptor_LayoutTagPillList;
import com.swctools.activity_modules.multi_image_picker.models.SelectedImageModel;
import com.swctools.activity_modules.save_layout.recycler_adaptors.RecyclerAdaptor_SaveImage;
import com.swctools.util.ActivitySwitcher;
import com.swctools.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class FragmentSaveLayout extends Fragment {
    public static final String TAG = "FragmentSaveLayout";
    //KEYS
    private static final String LAYOUT_NAME = "LAYOUT_NAME";
    private static final String LAYOUT_FACTION = "LAYOUT_FACTION";
    private static final String LAYOUT_PLAYER = "LAYOUT_PLAYER";

    //UI
    private EditText save_layout_name;
    private Spinner layoutFactionSpinner;
    private Spinner layoutPlayerSpinner;
    private Button saveLayoutButton, setFolderNewLayoutBtn, setNewLayoutTagBtn;
    private ImageView img_TagSet, img_FldrSet;
    private FloatingActionButton floatingActionButton;
    private RecyclerView layoutImageRecyclerView;
    private RecyclerAdaptor_SaveImage recyclerAdaptor_image;
    private int selectedFolder;

    private Context mContext;

    private boolean picked;
    private ArrayList<LayoutTag> layoutTagArrayList;
    private boolean saveClicked = false;
    private PlayerListBaseAdaptor playerListBaseAdaptor;
    private FactionListBaseAdaptor factionListBaseAdaptor;

    //data:
    private ArrayList<SelectedImageModel> imagebytesArray;
    private String lName, lPlayer, lFaction;
    private RecyclerView tagRecycler, folderRecycler;

    private FragmentSaveLayoutInterface mCallback;

    public static FragmentSaveLayout newInstance() {
        return new FragmentSaveLayout();
    }

    public static FragmentSaveLayout getInstance(FragmentManager fragmentManager, String playerId) {
        FragmentSaveLayout fragmentPlayerDetails = (FragmentSaveLayout) fragmentManager.findFragmentByTag(TAG);
        if (fragmentPlayerDetails == null) {
            ;
            fragmentPlayerDetails = new FragmentSaveLayout();
            Bundle args = new Bundle();
            args.putString(BundleKeys.PLAYER_ID.toString(), playerId);

            fragmentPlayerDetails.setArguments(args);
            fragmentManager.beginTransaction().add(fragmentPlayerDetails, TAG).commit();
        }
        return fragmentPlayerDetails;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        lName = save_layout_name.getText().toString();
        lPlayer = playerListBaseAdaptor.getPlayerList().get(layoutPlayerSpinner.getSelectedItemPosition()).playerId.equalsIgnoreCase("None") ? "" : playerListBaseAdaptor.getPlayerList().get(layoutPlayerSpinner.getSelectedItemPosition()).playerId;
        lFaction = factionListBaseAdaptor.getFactions()[layoutFactionSpinner.getSelectedItemPosition()];// layoutFactionSpinner.getSelectedItem().toString().equalsIgnoreCase("None") ? "" : layoutFactionSpinner.getSelectedItem().toString();
        outState.putString(LAYOUT_NAME, lName);
        outState.putString(LAYOUT_PLAYER, lPlayer);
        outState.putString(LAYOUT_FACTION, lFaction);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            //Restore the fragment's state here
            lName = savedInstanceState.getString(LAYOUT_NAME);
            lFaction = savedInstanceState.getString(LAYOUT_FACTION);
            lPlayer = savedInstanceState.getString(LAYOUT_PLAYER);
        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            //Restore the fragment's state here
            lName = savedInstanceState.getString(LAYOUT_NAME);
            lFaction = savedInstanceState.getString(LAYOUT_FACTION);
            lPlayer = savedInstanceState.getString(LAYOUT_PLAYER);
        } else {
            try {
                lPlayer = getArguments().getString(BundleKeys.PLAYER_ID.toString());
                if (!StringUtil.isStringNotNull(lPlayer)) {
                    lPlayer = "None";
                }
            } catch (Exception e) {
            }
            try {
                lFaction = getArguments().getString(BundleKeys.PLAYER_FACTION.toString());
            } catch (Exception e) {

            }

            try {
                selectedFolder = getArguments().getInt(BundleKeys.LAYOUT_FOLDER_ID.toString());

            } catch (Exception e) {
                selectedFolder = 0;
            }

            try {
                layoutTagArrayList = getArguments().getParcelableArrayList(BundleKeys.LAYOUT_TAG.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        setPlayerSpinner(lPlayer);
        setLayoutFactionSpinner(lFaction);

        setFolderRecycler(selectedFolder);
        if (layoutTagArrayList != null) {
            setTagRecycler(layoutTagArrayList);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        View view = inflater.inflate(R.layout.fragment_save_layout, container, false);
        setAllControls(view);


        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mCallback = (FragmentSaveLayoutInterface) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // Clear reference to host Activity.
        mCallback = null;

    }


    private void setSpinnerSelected(@Nullable String arg, Spinner spinner) {
        try {
            if (StringUtil.isStringNotNull(arg)) {
                for (int i = 0; i < spinner.getCount(); i++) {
                    if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(arg)) {
                        spinner.setSelection(i);
                    }
                }
            }
        } catch (Exception e) {

        }
    }


    private void setLayoutFactionSpinner(@Nullable String playerFaction) {
        String[] list = getResources().getStringArray(R.array.factions);
        List<String> spinnerArray = Arrays.asList(list);

        factionListBaseAdaptor = new FactionListBaseAdaptor(getActivity());
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        layoutFactionSpinner.setAdapter(factionListBaseAdaptor);
        setSpinnerSelected(playerFaction, layoutFactionSpinner);
    }

    private void setPlayerSpinner(@Nullable String playerId) {

        playerListBaseAdaptor = new PlayerListBaseAdaptor(mContext);

        layoutPlayerSpinner.setAdapter(playerListBaseAdaptor);
        if (StringUtil.isStringNotNull(playerId)) {
            for (int i = 0; i < layoutPlayerSpinner.getCount(); i++) {
                if (playerListBaseAdaptor.getPlayerList().get(i).playerId.equalsIgnoreCase(playerId)) {
                    layoutPlayerSpinner.setSelection(i);
                }
            }
        }
    }


    public void setSaveClicked(boolean saveClicked) {
        this.saveClicked = saveClicked;
    }

    @Override
    public void onResume() {
        super.onResume();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        layoutImageRecyclerView.setLayoutManager(linearLayoutManager);
        imagebytesArray = LayoutImageBytesListProvider.getTmpImageSelectedModelList(mContext);
        recyclerAdaptor_image = new RecyclerAdaptor_SaveImage(imagebytesArray, mContext);
        layoutImageRecyclerView.setAdapter(recyclerAdaptor_image);
        recyclerAdaptor_image.notifyDataSetChanged();
    }

    private void setAllControls(View view) {
        save_layout_name = (EditText) view.findViewById(R.id.save_layout_name);
        layoutFactionSpinner = (Spinner) view.findViewById(R.id.layoutFactionSpinner);
        layoutPlayerSpinner = (Spinner) view.findViewById(R.id.layoutPlayerSpinner);
        saveLayoutButton = view.findViewById(R.id.saveLayoutButton);
        floatingActionButton = view.findViewById(R.id.floatingActionButton);
        layoutImageRecyclerView = view.findViewById(R.id.layoutImageRecyclerView);
        img_FldrSet = view.findViewById(R.id.img_FldrSet);
        img_TagSet = view.findViewById(R.id.img_TagSet);
        tagRecycler = view.findViewById(R.id.tagRecycler);
        folderRecycler = view.findViewById(R.id.folderRecycler);
        setFolderNewLayoutBtn = view.findViewById(R.id.setFolderNewLayoutBtn);
        setNewLayoutTagBtn = view.findViewById(R.id.setNewLayoutTagBtn);

        //IMAGES
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        layoutImageRecyclerView.setLayoutManager(linearLayoutManager);
        imagebytesArray = LayoutImageBytesListProvider.getTmpImageSelectedModelList(mContext);
        recyclerAdaptor_image = new RecyclerAdaptor_SaveImage(imagebytesArray, mContext);
        layoutImageRecyclerView.setAdapter(recyclerAdaptor_image);
        recyclerAdaptor_image.notifyDataSetChanged();


        LayoutFolderItem layoutFolderItem = new LayoutFolderItem(0, LayoutFolderHelper.ROOTNAME, 0, LayoutFolderHelper.layoutsInFolder(0, mContext));
        ArrayList<LayoutFolderItem> folderList = new ArrayList<>();
        folderList.add(layoutFolderItem);
        setFolderRecycler(0);
//
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivitySwitcher.launchPickImages(6, DatabaseContracts.LayoutImagesTmp.LAYOUT_ID, 1, DatabaseContracts.LayoutImagesTmp.TABLE_NAME, DatabaseContracts.LayoutImagesTmp.IMAGE_BLOB, DatabaseContracts.LayoutImagesTmp.IMAGE_LABEL, DatabaseMethods.INSERT, mContext);
            }
        });
        saveLayoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!saveClicked) {
                    saveClicked = true;
                    if (StringUtil.isStringNotNull(save_layout_name.getText().toString())) {
                        lName = save_layout_name.getText().toString();
                        lPlayer = playerListBaseAdaptor.getPlayerList().get(layoutPlayerSpinner.getSelectedItemPosition()).playerId.equalsIgnoreCase("None") ? "" : playerListBaseAdaptor.getPlayerList().get(layoutPlayerSpinner.getSelectedItemPosition()).playerId;
                        lFaction = factionListBaseAdaptor.getFactions()[layoutFactionSpinner.getSelectedItemPosition()];// layoutFactionSpinner.getSelectedItem().toString().equalsIgnoreCase("None") ? "" : layoutFactionSpinner.getSelectedItem().toString();
                        if (imagebytesArray.size() > 0) {
                            mCallback.saveLayout(lName, lPlayer, lFaction, imagebytesArray.get(0).bytes);
                        } else {
                            mCallback.saveLayout(lName, lPlayer, lFaction, null);
                        }
                        String imageString = "";
//                        if (selectedImage != null) {
//                            if (picked) {
//                                imageString = ImageFilePath.getPath(mContext, selectedImage);//.substring(0, selectedImage.getPath().indexOf("/ORIGINAL")); //    selectedImage.getPath();//ImageHelpers.getImageUrlWithAuthority(mContext, selectedImage);//newUri.getPath();
//                            } else {
//                                imageString = selectedImage.toString();
//                            }
//                        }
//
                    } else {
                        mCallback.triggerMessageFragment("Enter a name for your layout!", "Save Layout Help", "ERROR");
                    }
                }


            }
        });


        img_FldrSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.setFolder();
            }
        });
        setFolderNewLayoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.setFolder();
            }
        });
        img_TagSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mCallback.setTags();
            }
        });
        setNewLayoutTagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.setTags();
            }
        });


    }

    public void setFolderRecycler(int selectedFolder) {
        ArrayList<LayoutFolderItem> layoutFolderItems = new ArrayList<>();
        layoutFolderItems.addAll(LayoutFolderHelper.getFolderHeirarchy(selectedFolder, mContext));
        final LinearLayoutManager breadCrumbLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        folderRecycler.setHasFixedSize(false);

//        Collections.sort(foldersList, Collections.reverseOrder());
        final RecyclerAdaptor_LayoutFolderBreadCrumb breadCrumbViewAdaptor = new RecyclerAdaptor_LayoutFolderBreadCrumb(layoutFolderItems, mContext);

        folderRecycler.setLayoutManager(breadCrumbLayoutManager);
        folderRecycler.setAdapter(breadCrumbViewAdaptor);
        breadCrumbViewAdaptor.notifyDataSetChanged();
        breadCrumbViewAdaptor.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                breadCrumbLayoutManager.smoothScrollToPosition(folderRecycler, null, breadCrumbViewAdaptor.getItemCount());
            }

            @Override
            public void onChanged() {
                breadCrumbLayoutManager.smoothScrollToPosition(folderRecycler, null, breadCrumbViewAdaptor.getItemCount());
            }
        });
    }

    public void setTagRecycler(ArrayList<LayoutTag> itemList) {
        RecyclerAdaptor_LayoutTagPillList recyclerAdaptorLayoutTagPillList = new RecyclerAdaptor_LayoutTagPillList(itemList, false, mContext);
//
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
//
        tagRecycler.setLayoutManager(mLayoutManager);
//        playerDetailsCapRecycler.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));
        tagRecycler.setItemAnimator(new DefaultItemAnimator());
        tagRecycler.setAdapter(recyclerAdaptorLayoutTagPillList);
        recyclerAdaptorLayoutTagPillList.notifyDataSetChanged();
    }

//    public void setLayoutImage(Uri selectedImage, boolean picked) {
//        this.picked = picked;
//        this.selectedImage = selectedImage;
//        layoutImage.setImageURI(selectedImage);
//    }

    public interface FragmentSaveLayoutInterface {
        //outgoing

        void setTags();

        void setFolder();

        void saveLayout(String lName, String lPlayer, String lFaction, byte[] bytes);

        void triggerMessageFragment(String message, String title, String fragmentTag);

        //incoming
        void setPlayer(final String PLAYER_ID);

        void setFaction(final String FACTION);


        void getLayoutImage();

    }


}
