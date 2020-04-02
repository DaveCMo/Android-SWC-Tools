package com.swctools.activity_modules.player.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.swctools.R;
import com.swctools.common.base_adaptors.FavouriteLayoutBaseAdaptor;
import com.swctools.config.AppConfig;
import com.swctools.common.enums.BundleKeys;
import com.swctools.common.helpers.PlayerFavouriteLayoutHelper;
import com.swctools.activity_modules.main.models.FavouriteLayoutListProvider;
import com.swctools.layouts.models.FavouriteLayoutItem;
import com.swctools.activity_modules.main.models.PlayerDAO;
import com.swctools.common.view_adaptors.recycler_adaptors.RecyclerAdaptor_FavouriteLayout;
import com.swctools.activity_modules.main.recycler_adaptors.RecyclerAdaptor_FavouriteLayoutSimple;

import java.util.List;

public class FragmentFavouriteLayout extends Fragment {
    private final static String TAG = "FragmentFavouriteLayout";
    private RecyclerView favourite_layout_recycler;
    private RecyclerAdaptor_FavouriteLayout recyclerAdaptorFavouriteLayout;

    private TextView hintTextView;
    private Spinner playerFavLayoutTypeSpinner;
    private String hintText;
    private String playerId;
    private Context mContext;
    private AppConfig appConfig;
    private PlayerDAO playerDAO;
    private FavListOption currentList;

    private List<FavouriteLayoutItem> topLayoutItemList;
    private List<FavouriteLayoutItem> lastUsedLayoutItemList;
    private List<FavouriteLayoutItem> favouriteLayoutItemList;
    private List<FavouriteLayoutItem> mostUsedLayoutItemList;
    private RecyclerAdaptor_FavouriteLayoutSimple favouriteLayoutListAdaptor;
    private RecyclerAdaptor_FavouriteLayoutSimple topLayoutListAdaptor;
    private RecyclerAdaptor_FavouriteLayoutSimple lastUsedLayoutListAdaptor;
    private RecyclerAdaptor_FavouriteLayoutSimple mostUsedLayoutListAdaptor;

    private int optionSelected;
    public static FragmentFavouriteLayout newInstance() {
        return new FragmentFavouriteLayout();
    }

    public static FragmentFavouriteLayout getInstance(FragmentManager fragmentManager, String playerId) {
        FragmentFavouriteLayout fragmentFavouriteLayout = (FragmentFavouriteLayout) fragmentManager.findFragmentByTag(TAG);
        if (fragmentFavouriteLayout == null) {
            ;
            fragmentFavouriteLayout = new FragmentFavouriteLayout();
            Bundle args = new Bundle();
            args.putString(BundleKeys.PLAYER_ID.toString(), playerId);
            fragmentFavouriteLayout.setArguments(args);
            fragmentManager.beginTransaction().add(fragmentFavouriteLayout, TAG).commit();
        }
        return fragmentFavouriteLayout;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        appConfig = new AppConfig(mContext);
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setRetainInstance(true);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);

        playerId = getArguments().getString(BundleKeys.PLAYER_ID.toString(), "");
        playerDAO = new PlayerDAO(playerId, mContext);
//        hintText = getResources().getString(R.string.player_favourite_layout_hint);
//setLists()

        //Set Up UI Components:
        View view = inflater.inflate(R.layout.fragment_favourite_layouts, container, false);
        hintTextView = view.findViewById(R.id.hintText);
        hintTextView.setText(hintText);
        favourite_layout_recycler = view.findViewById(R.id.favourite_layout_recycler);
        playerFavLayoutTypeSpinner = view.findViewById(R.id.playerFavLayoutTypeSpinner);

        //Recycler View rendering shizzle:
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        favourite_layout_recycler.setLayoutManager(mLayoutManager);
        favourite_layout_recycler.setItemAnimator(new DefaultItemAnimator());
        favourite_layout_recycler.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));
        //Spinner:
        final FavouriteLayoutBaseAdaptor favouriteLayoutBaseAdaptor = new FavouriteLayoutBaseAdaptor(mContext);
        playerFavLayoutTypeSpinner.setAdapter(favouriteLayoutBaseAdaptor);

        for (int i = 0; i < favouriteLayoutBaseAdaptor.getCount(); i++) {
            if (favouriteLayoutBaseAdaptor.getFavouriteTypes()[i].equalsIgnoreCase(playerDAO.getCard_default())) {
                playerFavLayoutTypeSpinner.setSelection(i);
                break;
            }
        }


        playerFavLayoutTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                optionSelected = i;
                setSelectedList(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        buildListsAndAdaptors();
        return view;
    }


    public void refreshList() {
        buildListsAndAdaptors();
        setSelectedList(optionSelected);


    }

    private void setSelectedList(int i) {
        switch (i) {
            case 0:
                favourite_layout_recycler.setAdapter(favouriteLayoutListAdaptor);
                favouriteLayoutListAdaptor.notifyDataSetChanged();
                break;
            case 1:
                favourite_layout_recycler.setAdapter(topLayoutListAdaptor);
                topLayoutListAdaptor.notifyDataSetChanged();
                break;
            case 2:
                favourite_layout_recycler.setAdapter(lastUsedLayoutListAdaptor);
                lastUsedLayoutListAdaptor.notifyDataSetChanged();
                break;
            case 3:
                favourite_layout_recycler.setAdapter(mostUsedLayoutListAdaptor);
                mostUsedLayoutListAdaptor.notifyDataSetChanged();
                break;
            default:
        }
    }


    private void buildListsAndAdaptors() {
        favouriteLayoutItemList = FavouriteLayoutListProvider.selectedFavs(0, mContext);
        topLayoutItemList = PlayerFavouriteLayoutHelper.getPlayerTopLayouts(playerDAO.getPlayerId(), mContext);
        lastUsedLayoutItemList = FavouriteLayoutListProvider.autoFavouriteLayoutList(playerDAO.getPlayerId(), FavouriteLayoutListProvider.AutoType.LAST_USED, 5, mContext);
        mostUsedLayoutItemList = FavouriteLayoutListProvider.autoFavouriteLayoutList(playerDAO.getPlayerId(), FavouriteLayoutListProvider.AutoType.MOST_USED, 5, mContext);
        favouriteLayoutListAdaptor = new RecyclerAdaptor_FavouriteLayoutSimple(favouriteLayoutItemList, 0, playerDAO.getPlayerId(), true, mContext);
        topLayoutListAdaptor = new RecyclerAdaptor_FavouriteLayoutSimple(topLayoutItemList, 1, playerDAO.getPlayerId(), true, mContext);
        lastUsedLayoutListAdaptor = new RecyclerAdaptor_FavouriteLayoutSimple(lastUsedLayoutItemList, 2, playerDAO.getPlayerId(), true, mContext);
        mostUsedLayoutListAdaptor = new RecyclerAdaptor_FavouriteLayoutSimple(mostUsedLayoutItemList, 3, playerDAO.getPlayerId(), true, mContext);
    }


    private enum FavListOption {
        FAVOURITE, MOSTUSED, LASTUSED;
    }
}
