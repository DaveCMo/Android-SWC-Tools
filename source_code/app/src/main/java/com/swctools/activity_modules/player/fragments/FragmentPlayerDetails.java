package com.swctools.activity_modules.player.fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.swctools.R;
import com.swctools.common.enums.ApplicationMessageTemplates;
import com.swctools.common.enums.BundleKeys;
import com.swctools.common.enums.Factions;
import com.swctools.common.helpers.DateTimeHelper;
import com.swctools.activity_modules.player.PlayerDetailsFragmentInterface;
import com.swctools.activity_modules.player.models.ResourceDataItem;
import com.swctools.activity_modules.player.models.TacticalCapacityData;
import com.swctools.activity_modules.player.models.TwoTextItemData;
import com.swctools.common.models.player_models.ActiveArmoury;
import com.swctools.common.models.player_models.DekoDeka;
import com.swctools.common.models.player_models.DonatedTroops;
import com.swctools.common.models.player_models.GuildModel;
import com.swctools.common.models.player_models.Inventory;
import com.swctools.common.models.player_models.PlayerModel;
import com.swctools.common.models.player_models.PlayerTrap;
import com.swctools.activity_modules.player.recycler_adaptors.RecyclerAdaptor_PlayerDetails;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;


public class FragmentPlayerDetails extends Fragment {
    private static final String TAG = "fragmentPlayerDetails";
    private Context mContext;

    //playerInfo
    private TextView openORclosed;
    private TextView protectionUntil;
    private PlayerModel playerModel;
    //Other formatting stuff...
    private int COLOR_GREEN;
    private int COLOR_RED;
    private int CREDIT_GOLD;
    private int ALLOY_BLUE;
    private int CONTRA_PINK;
    private Drawable factionImage;


    //NEw extra shit...

    private RecyclerView playerDetailsCapRecycler;


    private SwipeRefreshLayout mainSwipeRefresh;

    //interfaces...
    private PlayerDetailsFragmentInterface mCallback;
    //Data that will be stored and restored during fragment lifecycle

    private String faction;
    private String playerNameStr;
    private String protectedStr;
    private long protectedUntil;
    private String creditFullTxt;
    private String alloyFullTxt;
    private String contraFullTxt;
    private int creditCap;
    private int creditAMT;
    private int alloyCap;
    private int alloyAMT;
    private int contraCap;
    private int contraAMT;
    private ArrayList<DekoDeka> dekoDekaList;
    private ArrayList<PlayerTrap> playerTraps;
    private String guildName;
    private int armouryImageId;
    private int scImageId;
    private TacticalCapacityData armoury;
    private TacticalCapacityData squadCenter;
    private TacticalCapacityData troops;
    private DonatedTroops donatedTroops;
    private TacticalCapacityData heroData;
    private TacticalCapacityData airData;
    private TwoTextItemData baseScoreDetail;
    private TwoTextItemData medalCountDetail;
    private TwoTextItemData crystals;
    private ResourceDataItem reputation;
    private TwoTextItemData attacksWon;
    private TwoTextItemData defencesWon;
    private ArrayList<ResourceDataItem> trapList;

    public static FragmentPlayerDetails newInstance() {
        return new FragmentPlayerDetails();
    }

    public static FragmentPlayerDetails getInstance(FragmentManager fragmentManager, String playerId) {
        FragmentPlayerDetails fragmentPlayerDetails = (FragmentPlayerDetails) fragmentManager.findFragmentByTag(TAG);
        if (fragmentPlayerDetails == null) {
            ;
            fragmentPlayerDetails = new FragmentPlayerDetails();
            Bundle args = new Bundle();
            args.putString(BundleKeys.PLAYER_ID.toString(), playerId);
            fragmentPlayerDetails.setArguments(args);
            fragmentManager.beginTransaction().add(fragmentPlayerDetails, TAG).commit();
        }
        return fragmentPlayerDetails;
    }

    @Override
    public void onResume() {
        super.onResume();
//        mCallback.getPlayerModel(true, false, false);
    }


    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setRetainInstance(true);
//        this.playerDetails_sectionStatePagerAdapter = ()


    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        outState.putString(BundleKeys.PLAYER_FACTION.toString(), this.faction);
        outState.putString(BundleKeys.PROTECTED_UNTIL_STR.toString(), protectedStr);

        outState.putLong(BundleKeys.PROTECTED_UNTIL_LNG.toString(), protectedUntil);

        outState.putString(BundleKeys.CREDIT_AMT_TXT.toString(), creditFullTxt);
        outState.putString(BundleKeys.ALLOY_AMT_TXT.toString(), alloyFullTxt);
        outState.putString(BundleKeys.CONTRA_AMT_TXT.toString(), contraFullTxt);

        outState.putInt(BundleKeys.CREDIT_CAP.toString(), creditCap);
        outState.putInt(BundleKeys.CREDIT_AMT.toString(), creditAMT);

        outState.putInt(BundleKeys.ALLOY_CAP.toString(), alloyCap);
        outState.putInt(BundleKeys.ALLOY_AMT.toString(), alloyAMT);

        outState.putInt(BundleKeys.CONTRA_CAP.toString(), contraCap);
        outState.putInt(BundleKeys.CONTRA_AMT.toString(), contraAMT);

        outState.putParcelableArrayList(BundleKeys.DEKOARRAYLIST.toString(), dekoDekaList);

        outState.putString(BundleKeys.GUILDNAME.toString(), guildName);
        outState.putInt(BundleKeys.SCIMAGEID.toString(), scImageId);
        outState.putInt(BundleKeys.ARMOURYIMAGEID.toString(), armouryImageId);

        outState.putParcelable(BundleKeys.ARMOURYTACTITEM.toString(), armoury);
        outState.putParcelable(BundleKeys.SCTACTITEM.toString(), squadCenter);

        outState.putParcelable(BundleKeys.PLAYERHEROES.toString(), heroData);
        outState.putParcelable(BundleKeys.PLAYERTROOPS.toString(), troops);
        outState.putParcelable(BundleKeys.PLAYERAIR.toString(), airData);
        outState.putParcelableArrayList(BundleKeys.PLAYERTRAPS.toString(), trapList);
        outState.putParcelable(BundleKeys.PLAYERMEDALS.toString(), medalCountDetail);
        outState.putParcelable(BundleKeys.PLAYERBASESCORE.toString(), baseScoreDetail);
        outState.putParcelable(BundleKeys.PLAYERREPUTATION.toString(), reputation);
        outState.putParcelable(BundleKeys.PLAYERCRYSTALS.toString(), crystals);

        outState.putParcelable(BundleKeys.ATTACKSWON.toString(), attacksWon);
        outState.putParcelable(BundleKeys.DEFENCESWON.toString(), defencesWon);
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        setRetainInstance(true);
        View view = inflater.inflate(R.layout.fragment_playerdetails_layout, container, false);
        setAllControls(view);
//        String playerId = getArguments().getString(BundleKeys.PLAYER_ID.toString());
        if (savedInstanceState != null) {
            faction = savedInstanceState.getString(BundleKeys.PLAYER_FACTION.toString());
            protectedStr = savedInstanceState.getString(BundleKeys.PROTECTED_UNTIL_STR.toString());
            protectedUntil = savedInstanceState.getLong(BundleKeys.PROTECTED_UNTIL_LNG.toString());
            creditFullTxt = savedInstanceState.getString(BundleKeys.CREDIT_AMT_TXT.toString());
            alloyFullTxt = savedInstanceState.getString(BundleKeys.ALLOY_AMT_TXT.toString());
            contraFullTxt = savedInstanceState.getString(BundleKeys.CONTRA_AMT_TXT.toString());

            creditCap = savedInstanceState.getInt(BundleKeys.CREDIT_CAP.toString());
            creditAMT = savedInstanceState.getInt(BundleKeys.CREDIT_AMT.toString());
            alloyCap = savedInstanceState.getInt(BundleKeys.ALLOY_CAP.toString());
            alloyAMT = savedInstanceState.getInt(BundleKeys.ALLOY_AMT.toString());
            contraCap = savedInstanceState.getInt(BundleKeys.CONTRA_CAP.toString());
            contraAMT = savedInstanceState.getInt(BundleKeys.CONTRA_AMT.toString());
            dekoDekaList = savedInstanceState.getParcelableArrayList(BundleKeys.DEKOARRAYLIST.toString());
            guildName = savedInstanceState.getString(BundleKeys.GUILDNAME.toString());
            armoury = savedInstanceState.getParcelable(BundleKeys.ARMOURYTACTITEM.toString());
            squadCenter = savedInstanceState.getParcelable(BundleKeys.SCTACTITEM.toString());
            trapList = savedInstanceState.getParcelableArrayList(BundleKeys.PLAYERTRAPS.toString());

            heroData = savedInstanceState.getParcelable(BundleKeys.PLAYERHEROES.toString());
            troops = savedInstanceState.getParcelable(BundleKeys.PLAYERTROOPS.toString());
            airData = savedInstanceState.getParcelable(BundleKeys.PLAYERAIR.toString());
            baseScoreDetail = savedInstanceState.getParcelable(BundleKeys.PLAYERBASESCORE.toString());
            medalCountDetail = savedInstanceState.getParcelable(BundleKeys.PLAYERMEDALS.toString());
            reputation = savedInstanceState.getParcelable(BundleKeys.PLAYERREPUTATION.toString());
            crystals = savedInstanceState.getParcelable(BundleKeys.PLAYERCRYSTALS.toString());

            attacksWon = savedInstanceState.getParcelable(BundleKeys.ATTACKSWON.toString());
            defencesWon = savedInstanceState.getParcelable(BundleKeys.DEFENCESWON.toString());
            addAllToRecycler();

        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mCallback = (PlayerDetailsFragmentInterface) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // Clear reference to host Activity.
        mContext =null;
        mCallback = null;

    }

    public void processPlayerModel(PlayerModel playerModel) {
        this.playerModel = playerModel;
        setPlayerInfo(playerModel);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();


    }

    private void setFactionImages() {


        if (faction.equalsIgnoreCase(Factions.EMPIRE.toString())) {

            armouryImageId = R.mipmap.ic_empire_armoury_foreground;
            scImageId = R.mipmap.ic_empire_sc_foreground;

        } else if (faction.equalsIgnoreCase(Factions.REBEL.toString())) {

            armouryImageId = R.mipmap.ic_rebel_armoury_foreground;
            scImageId = R.mipmap.ic_rebel_sc_foreground;
        }
    }

    private void setPlayerInfo(PlayerModel playerModel) {
        playerNameStr = "";
        try {
            playerNameStr = URLDecoder.decode(playerModel.playerName, "UTF8");
        } catch (UnsupportedEncodingException e) {
            playerNameStr = playerModel.playerName;
        }

        faction = playerModel.playerFaction;
        setFactionImages();
        GuildModel guildModel = playerModel.guildModel;// new GuildModel(jsonVisitor.mplayerModel().guildInfo().toString());
        guildName = guildModel.getGuildName();

        if (playerModel.mplayerModel.protectedUntil() != 0) {

            protectedUntil = playerModel.mplayerModel.playerModel.getInt("protectedUntil");
            protectedStr = "Protected Until: " + DateTimeHelper.longDateTime(protectedUntil, mContext);
            protectionUntil.setText(protectedStr);
            openORclosed.setText("Closed");
            openORclosed.setTextColor(COLOR_RED);
        } else {
            protectionUntil.setText("");
            openORclosed.setText("None (Open)");
            openORclosed.setTextColor(COLOR_GREEN);
        }

        Inventory inventory = playerModel.inventory;//new Inventory(jsonVisitor.mplayerModel(), mContext);
        creditFullTxt = String.format(ApplicationMessageTemplates.PROGRESS_BAR_LABEL.getemplateString(), inventory.credits.getAmountComma(), inventory.credits.geCapacityComma());
        alloyFullTxt = String.format(ApplicationMessageTemplates.PROGRESS_BAR_LABEL.getemplateString(), inventory.materials.getAmountComma(), inventory.materials.geCapacityComma());
        contraFullTxt = String.format(ApplicationMessageTemplates.PROGRESS_BAR_LABEL.getemplateString(), inventory.contraband.getAmountComma(), inventory.contraband.geCapacityComma());
        creditCap = inventory.credits.getCapacity();
        creditAMT = inventory.credits.getAmount();
        alloyCap = inventory.materials.getCapacity();
        alloyAMT = inventory.materials.getAmount();
        contraCap = inventory.contraband.getCapacity();
        contraAMT = inventory.contraband.getAmount();

        dekoDekaList = new ArrayList<>();
        if (inventory.getDroidekaSent().built) {
            dekoDekaList.add(inventory.getDroidekaSent());
        }
        if (inventory.getDroidekaOp().built) {
            dekoDekaList.add(inventory.getDroidekaOp());
        }

        try {
            playerTraps = playerModel.playerTraps.getPlayerTraps();
        } catch (Exception e) {
            e.printStackTrace();
        }
        donatedTroops = playerModel.donatedTroops;
        ActiveArmoury activeArmoury = playerModel.activeArmoury;
        squadCenter = new TacticalCapacityData("SQUAD CENTER", scImageId, donatedTroops.getSquadBuildingCap(), donatedTroops.capDonated(), donatedTroops.getTroopsList(), "Donated Troops", donatedTroops.getNumInSC(), true, TacticalCapacityData.SC, mContext);

        armoury = new TacticalCapacityData("ARMORY", armouryImageId, activeArmoury.capacity(), activeArmoury.get_activatedCapacity(), activeArmoury.getActivatedEquipment(), "", activeArmoury.noActivated(), false, TacticalCapacityData.ARMOURY, mContext);
        try {
            troops = playerModel.troopStorage.getTroopData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            heroData = playerModel.troopStorage.getHeroesData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            airData = playerModel.troopStorage.getAirData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            baseScoreDetail = playerModel.baseScoreDetail;
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            medalCountDetail = playerModel.medalCountDetail;
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            reputation = playerModel.reputationCapacity;
        } catch (Exception e) {
            e.printStackTrace();
        }
        crystals = playerModel.crystals;

        attacksWon = playerModel.attacksWonDetail;
        defencesWon = playerModel.defencesWonDetail;
        trapList = playerModel.playerTraps.getTrapResourceDataItems();
        addAllToRecycler();

    }

    private void addAllToRecycler() {
        //Build SC info:

        ArrayList<Object> listItems = new ArrayList<>();

        listItems.add(squadCenter);
        listItems.add(armoury);
        listItems.addAll(dekoDekaList);
//        listItems.add("RESOURCES");
        listItems.addAll(trapList);
        listItems.add(heroData);
        listItems.add(troops);
        listItems.add(airData);

        listItems.add(new ResourceDataItem("CREDITS", creditCap, creditAMT, CREDIT_GOLD));
        listItems.add(new ResourceDataItem("ALLOY", alloyCap, alloyAMT, ALLOY_BLUE));
        listItems.add(new ResourceDataItem("CONTRABAND", contraCap, contraAMT, CONTRA_PINK));
        listItems.add(crystals);
//        listItems.add("TRAPS");
//        listItems.add("HEROES");
//        listItems.add("ARMY");
//        listItems.add("PLAYER STATS");
        listItems.add(medalCountDetail);
        listItems.add(baseScoreDetail);
        listItems.add(attacksWon);
        listItems.add(defencesWon);
        listItems.add(reputation);
        RecyclerAdaptor_PlayerDetails adaptor = new RecyclerAdaptor_PlayerDetails(listItems, mContext);


        playerDetailsCapRecycler.setAdapter(adaptor);


        adaptor.notifyDataSetChanged();
        mCallback.finishedRendering();
    }


    public void stopRefreshing() {
        try {
            mainSwipeRefresh.setRefreshing(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setAllControls(View view) {

        openORclosed = (TextView) view.findViewById(R.id.playerInfo_protectionOpenClosed);
        protectionUntil = (TextView) view.findViewById(R.id.protectionUntil);

        mainSwipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.playerSwipeRefresh);
        mainSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mCallback.sendRefreshCommand();
                mainSwipeRefresh.setRefreshing(false);

            }
        });


        COLOR_GREEN = view.getResources().getColor(R.color.green);
        COLOR_RED = view.getResources().getColor(R.color.red);

        CREDIT_GOLD = view.getResources().getColor(R.color.creditsColor);
        ALLOY_BLUE = view.getResources().getColor(R.color.alloyColor);
        CONTRA_PINK = view.getResources().getColor(R.color.contra_purple);

        playerDetailsCapRecycler = (RecyclerView) view.findViewById(R.id.playerDetailsCapRecycler);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
//        {
//            @Override
//            public boolean canScrollVertically() {
//                return false;
//            }
//        };
        playerDetailsCapRecycler.setLayoutManager(mLayoutManager);
//        playerDetailsCapRecycler.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));
        playerDetailsCapRecycler.setItemAnimator(new DefaultItemAnimator());


    }

private void originalOrder(){
//    listItems.add(squadCenter);
//    listItems.add(armoury);
//    listItems.addAll(dekoDekaList);
////        listItems.add("RESOURCES");
//    listItems.add(new ResourceDataItem("CREDITS", creditCap, creditAMT, CREDIT_GOLD));
//    listItems.add(new ResourceDataItem("ALLOY", alloyCap, alloyAMT, ALLOY_BLUE));
//    listItems.add(new ResourceDataItem("CONTRABAND", contraCap, contraAMT, CONTRA_PINK));
//    listItems.add(crystals);
////        listItems.add("TRAPS");
//    listItems.addAll(trapList);
////        listItems.add("HEROES");
////        listItems.add("ARMY");
//    listItems.add(heroData);
//    listItems.add(troops);
//    listItems.add(airData);
////        listItems.add("PLAYER STATS");
//    listItems.add(medalCountDetail);
//    listItems.add(baseScoreDetail);
//    listItems.add(attacksWon);
//    listItems.add(defencesWon);
//    listItems.add(reputation);
}
}
