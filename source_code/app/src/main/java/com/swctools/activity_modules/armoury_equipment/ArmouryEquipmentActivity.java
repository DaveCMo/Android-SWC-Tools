package com.swctools.activity_modules.armoury_equipment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.swctools.R;
import com.swctools.activity_modules.armoury_equipment.models.Armoury_EquipmentListProvider;
import com.swctools.activity_modules.armoury_equipment.models.Armoury_Set_Item;
import com.swctools.activity_modules.armoury_equipment.recycler_adaptors.RecyclerAdaptor_ArmouryEquipment_Added;
import com.swctools.activity_modules.armoury_equipment.recycler_adaptors.RecyclerAdaptor_ArmouryEquipment_Available;
import com.swctools.common.enums.ApplicationMessageTemplates;
import com.swctools.common.enums.BundleKeys;
import com.swctools.common.helpers.BundleHelper;
import com.swctools.common.model_list_providers.GameUnitConversionListProvider;
import com.swctools.common.models.player_models.ArmouryEquipment;
import com.swctools.swc_server_interactions.fragments.SWC_Server_Armoury_Equipment_Fragment;
import com.swctools.swc_server_interactions.results.SWCVisitResult;
import com.swctools.util.ActivitySwitcher;
import com.swctools.util.MethodResult;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ArmouryEquipmentActivity extends AppCompatActivity implements ArmouryEquipment_Callback, ArmouryEquip_BackgroundInterface {
    private static final String TAG = "ArmouryEquipmentActivit";
    protected static final String YODA_PROGRESS = "Patience you must have <(-_-)>";

    ///UI COMPONENTS
    private RecyclerView addedEquipRecycler;
    private RecyclerView availableEquipRecycler;
    private ProgressBar capacityProgress;
    private TextView activeCap;
    private Button resetArmouryBtn;
    private Button ClearAddedArmouryBtn;
    protected ConstraintLayout progress_overlay_container;
    protected ProgressBar progress_overlay_bar;
    protected TextView progress_overlay_message;
    private Spinner equipTypeSpinner;
    private SWC_Server_Armoury_Equipment_Fragment swc_server_armoury_equipment_fragment;
    private String playerId;
    private String playerFaction;
    private int capacityActive = 0;
    private int capacity;
    private SWCVisitResult jsonVisitor;
    private String visitResponse;
    private HashMap<String, ArmouryEquipment> getMasterArmourList;

    private RecyclerAdaptor_ArmouryEquipment_Added recyclerAdaptor_Added;
    private RecyclerAdaptor_ArmouryEquipment_Available recyclerAdaptor_Available;


    private ArrayList<Armoury_Set_Item> addedList = new ArrayList<>();
    private ArrayList<Armoury_Set_Item> masterAddedList = new ArrayList<>();
    private ArrayList<Armoury_Set_Item> availableList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_armoury_equipment_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Set Armoury Equipment");
        addedEquipRecycler = findViewById(R.id.addedEquipRecycler);
        availableEquipRecycler = findViewById(R.id.availableEquipRecycler);
        capacityProgress = findViewById(R.id.capacityProgress);
        activeCap = findViewById(R.id.activeCap);
        resetArmouryBtn = findViewById(R.id.resetArmouryBtn);
        ClearAddedArmouryBtn = findViewById(R.id.ClearAddedArmouryBtn);
        equipTypeSpinner = findViewById(R.id.equipTypeSpinner);

        //for the progress indicator\/\/\/\/
        progress_overlay_container = findViewById(R.id.progress_overlay_container_include);
        progress_overlay_bar = findViewById(R.id.progress_overlay_bar);
        progress_overlay_message = findViewById(R.id.progress_overlay_message);
        //for the progress indicator^^^^
        swc_server_armoury_equipment_fragment = SWC_Server_Armoury_Equipment_Fragment.getInstance(getSupportFragmentManager());

        getMasterArmourList = GameUnitConversionListProvider.getMasterArmourList(this);


        playerId = getIntent().getStringExtra(BundleKeys.PLAYER_ID.toString());

        String bundleKey = String.format(ApplicationMessageTemplates.VISIT_KEY.getemplateString(),
                BundleKeys.VISIT_RESPONSE.toString(), playerId);
        BundleHelper bundleHelper = new BundleHelper(bundleKey);
        visitResponse = bundleHelper.get_value(this);
        jsonVisitor = new SWCVisitResult(visitResponse);
        playerFaction = jsonVisitor.mplayerModel().faction();
        capacity = jsonVisitor.mplayerModel().activeArmory().getInt("capacity");

        capacityProgress.setMax(capacity);

        try {
            for (int i = 0; i < jsonVisitor.mplayerModel().activeArmory().getJsonArray("equipment").size(); i++) {
                String equipString = jsonVisitor.mplayerModel().activeArmory().getJsonArray("equipment").getString(i);
                addedList.add(new Armoury_Set_Item(equipString,
                        getMasterArmourList.get(equipString).uiName(),
                        playerFaction,
                        getMasterArmourList.get(equipString).cap(),
                        getMasterArmourList.get(equipString).getEquipLevel(),
                        getMasterArmourList.get(equipString).get_availableOn(),
                        getMasterArmourList.get(equipString).getType()));
                capacityActive = capacityActive + getMasterArmourList.get(equipString).cap();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        masterAddedList.addAll(addedList);


        recyclerAdaptor_Added = new RecyclerAdaptor_ArmouryEquipment_Added(this, addedList);
        addedEquipRecycler.setAdapter(recyclerAdaptor_Added);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        addedEquipRecycler.setLayoutManager(linearLayoutManager);
        recyclerAdaptor_Added.notifyDataSetChanged();

        availableList.addAll(Armoury_EquipmentListProvider.getAvailableEquipment(jsonVisitor, getMasterArmourList, getApplicationContext()));
        Log.d(TAG, "onCreate: ----->" + availableList.size());
        recyclerAdaptor_Available = new RecyclerAdaptor_ArmouryEquipment_Available(this, availableList);
        availableEquipRecycler.setAdapter(recyclerAdaptor_Available);
        LinearLayoutManager availableLinearLM = new LinearLayoutManager(this);
        availableEquipRecycler.setLayoutManager(availableLinearLM);
        recyclerAdaptor_Available.notifyDataSetChanged();

        setCapacityOfArmoury();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressLayout(YODA_PROGRESS);
                swc_server_armoury_equipment_fragment.setEquipment(playerId, masterAddedList, addedList, getApplicationContext());
            }
        });

        resetArmouryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetRecyclers();
            }
        });

        ClearAddedArmouryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Armoury_Set_Item item : addedList) {
                    availableList.add(item);
                }
                capacityActive = 0;
                addedList.clear();
                recyclerAdaptor_Added.notifyDataSetChanged();
                recyclerAdaptor_Available.notifyDataSetChanged();
                setCapacityOfArmoury();
            }
        });
        equipTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemSelected: ");
                String s = (String) equipTypeSpinner.getItemAtPosition(position);
                Log.d(TAG, "onItemSelected: " + s);
                recyclerAdaptor_Available.filterType(s);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void resetRecyclers() {
        capacityActive = 0;
        addedList.clear();
        availableList.clear();

        addedList.addAll(masterAddedList);


        recyclerAdaptor_Added = new RecyclerAdaptor_ArmouryEquipment_Added(this, addedList);
        addedEquipRecycler.setAdapter(recyclerAdaptor_Added);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        addedEquipRecycler.setLayoutManager(linearLayoutManager);
        recyclerAdaptor_Added.notifyDataSetChanged();

        availableList.addAll(Armoury_EquipmentListProvider.getAvailableEquipment(jsonVisitor, getMasterArmourList, getApplicationContext()));
        Log.d(TAG, "onCreate: ----->" + availableList.size());
        recyclerAdaptor_Available = new RecyclerAdaptor_ArmouryEquipment_Available(this, availableList);
        availableEquipRecycler.setAdapter(recyclerAdaptor_Available);
        LinearLayoutManager availableLinearLM = new LinearLayoutManager(this);
        availableEquipRecycler.setLayoutManager(availableLinearLM);
        recyclerAdaptor_Available.notifyDataSetChanged();

        setCapacityOfArmoury();
    }

    private void setCapacityOfArmoury() {
        capacityProgress.setProgress(capacityActive);
        activeCap.setText("(" + capacityActive + "/" + capacity);
    }


    @Override
    public void addEquipment(Armoury_Set_Item armoury_set_item, int position) {
        int spaceLeft = capacity - capacityActive;
        Log.d(TAG, "addEquipment: " + spaceLeft);
        if (armoury_set_item.getCapacity() < spaceLeft) {
            availableList.remove(position);
            addedList.add(armoury_set_item);
            recyclerAdaptor_Added.notifyDataSetChanged();
            recyclerAdaptor_Available.notifyDataSetChanged();
            capacityActive = capacityActive + armoury_set_item.getCapacity();
        }
        setCapacityOfArmoury();


    }

    @Override
    public void removeEquipment(Armoury_Set_Item armoury_set_item, int position) {
        addedList.remove(position);
        availableList.add(armoury_set_item);
        recyclerAdaptor_Added.notifyDataSetChanged();
        recyclerAdaptor_Available.notifyDataSetChanged();
        capacityActive = capacityActive - armoury_set_item.getCapacity();
        setCapacityOfArmoury();
    }

    protected void showProgressLayout(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progress_overlay_container.setVisibility(View.VISIBLE);
                progress_overlay_message.setText(msg);
                progress_overlay_bar.setIndeterminate(true);
            }
        });
    }

    protected void hideProgressView() {
        progress_overlay_container.setVisibility(View.GONE);
        progress_overlay_message.setText("");
        progress_overlay_bar.setProgress(0);
        progress_overlay_bar.setMax(0);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
            }
        });

    }

    @Override
    public void progressUpdate(String msg) {
        showProgressLayout(msg);
    }

    @Override
    public void receiveResult(String command, MethodResult methodResult) {
        hideProgressView();
        if (methodResult.success) {
            if (command.equalsIgnoreCase(SWC_Server_Armoury_Equipment_Fragment.EQUIPMENT)) {
                Toast.makeText(this, methodResult.getMessage(), Toast.LENGTH_LONG).show();
                showProgressLayout("Refreshing player data");
                swc_server_armoury_equipment_fragment.visitPlayer(playerId, getApplicationContext());
            } else if (command.equalsIgnoreCase(SWC_Server_Armoury_Equipment_Fragment.VISIT)) {
                ActivitySwitcher.launchPlayerDetails(playerId, this);
            }
        } else {
            Toast.makeText(this, methodResult.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
