package com.swctools.activity_modules.war_sign_up.fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.swctools.R;
import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;
import com.swctools.common.helpers.DateTimeHelper;
import com.swctools.util.StringUtil;
import com.swctools.activity_modules.war_sign_up.interfaces.FragmentWarDetailsInterface;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

//import static junit.framework.Assert.assertEquals;

public class Fragment_WarDetails extends Fragment {
    private static final String TAG = "Fragment_WarDetails";
    private static final String STARTTIMEFORMAT = "START: approx %1s\n\n";
    private FragmentWarDetailsInterface mCallBack;
    private Context mContext;
    private List<String> planets;
    private RadioButton tillSt_rdbn, stTim_rdbn, outpost_rbn, nooutpost_rbn, stSkip_rdbn;
    private Spinner tzSpinner;
    private CheckBox op1, op2, op3, op4, op5, op6;
    private Button war_signup_go_bn;
    private String HHMM = "HHMM";
    private String STARTTIME = "STARTTIME";
    private String whichFrag;
    private FragmentManager fm;
    private LinearLayout outposts_linlayout;
    private List<CheckBox> outpostChks;
    private int countPlanetsChecked = 0;

    //
    private HashSet<String> selectedPlanets;
    private int startHrs;
    private int startMin;
    private String startTime;
    private String tzId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_war_signup_set_details, container, false);
        tillSt_rdbn = (RadioButton) view.findViewById(R.id.tillSt_rdbn);
        stTim_rdbn = (RadioButton) view.findViewById(R.id.stTim_rdbn);
        outpost_rbn = (RadioButton) view.findViewById(R.id.outpost_rbn);
        nooutpost_rbn = (RadioButton) view.findViewById(R.id.nooutpost_rbn);
        stSkip_rdbn = (RadioButton) view.findViewById(R.id.stSkip_rdbn);
        tzSpinner = (Spinner) view.findViewById(R.id.tzSpinner);
        outposts_linlayout = (LinearLayout) view.findViewById(R.id.outposts_linlayout);
        war_signup_go_bn = (Button) view.findViewById(R.id.war_signup_go_bn);

        op1 = (CheckBox) view.findViewById(R.id.op1_chk);
        op2 = (CheckBox) view.findViewById(R.id.op2_chk);
        op3 = (CheckBox) view.findViewById(R.id.op3_chk);
        op4 = (CheckBox) view.findViewById(R.id.op4_chk);
        op5 = (CheckBox) view.findViewById(R.id.op5_chk);
        op6 = (CheckBox) view.findViewById(R.id.op6_chk);

        selectedPlanets = new HashSet<>();
        outpostChks = new ArrayList<>();
        outpostChks.add(op1);
        outpostChks.add(op2);
        outpostChks.add(op3);
        outpostChks.add(op4);
        outpostChks.add(op5);
        outpostChks.add(op6);

        planets = new ArrayList<>();

        fm = getActivity().getSupportFragmentManager();

        if (savedInstanceState == null) {
            setFragment(new Fragment_WarHHMM(), HHMM);

        } else {
            countPlanetsChecked = savedInstanceState.getInt(StateKeys.NOCHECKED.toString(), 0);
            whichFrag = savedInstanceState.getString(StateKeys.WHICHFRAG.toString());
            Fragment fragment = fm.findFragmentByTag(whichFrag);

        }


        CompoundButton.OnCheckedChangeListener planetCheckedListenr = new PlanetsChecked();
        nooutpost_rbn.setOnCheckedChangeListener(new RdChng());
        outpost_rbn.setOnCheckedChangeListener(new RdChng());
        stTim_rdbn.setOnCheckedChangeListener(new RdChng());
        tillSt_rdbn.setOnCheckedChangeListener(new RdChng());
        tzSpinner.setOnItemSelectedListener(new TzSelected());
        op1.setOnCheckedChangeListener(planetCheckedListenr);
        op2.setOnCheckedChangeListener(planetCheckedListenr);
        op3.setOnCheckedChangeListener(planetCheckedListenr);
        op4.setOnCheckedChangeListener(planetCheckedListenr);
        op5.setOnCheckedChangeListener(planetCheckedListenr);
        op6.setOnCheckedChangeListener(planetCheckedListenr);
        war_signup_go_bn.setOnClickListener(new GoButton());
        setPlanets();
        setPlanetSpinnerDefaults();
        setTzSpinner();
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(StateKeys.NOCHECKED.toString(), countPlanetsChecked);
        //Save the fragment's instance

    }

    private void setPlanetSpinnerDefaults() {
        for (int i = 0; i < outpostChks.size(); i++) {
            outpostChks.get(i).setText(planets.get(i));
            outpostChks.get(i).setSelected(false);
        }
    }

    private void setFragment(Fragment fragment, String tag) {
        whichFrag = tag;
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.war_timeOption, fragment, tag);
        ft.commit();
    }

    private void setTzSpinner() {
        List<String> tzList = new ArrayList<>();
        tzList.add("Select");
        for (Map.Entry<String, String> entry : StringUtil.getTZIds().entrySet()) {
            tzList.add(entry.getKey());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, tzList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tzSpinner.setAdapter(adapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallBack = (FragmentWarDetailsInterface) context;
        this.mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // Clear reference to host Activity.
        mCallBack = null;
        mContext = null;
    }

    private void setPlanets() {

        Cursor cursor = DBSQLiteHelper.queryDB(DatabaseContracts.Planets.TABLE_NAME, mContext);
        try {

            while (cursor.moveToNext()) {
                String planetName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.Planets.UI_NAME));
                planets.add(planetName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }


    }

    public enum StateKeys {
        WHICHFRAG, NOCHECKED, HH, MM, STARTTIME,
    }



    class GoButton implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            String selectedTzKey = tzSpinner.getSelectedItem().toString();
            tzId = StringUtil.getTZIds().get(selectedTzKey);
            if (!StringUtil.isStringNotNull(tzId)) {
                tzId = TimeZone.getDefault().getID();
            }
            if (!stSkip_rdbn.isChecked()) {
                if (whichFrag.equalsIgnoreCase(HHMM)) {
                    Fragment_WarHHMM fragment_warHHMM = (Fragment_WarHHMM) fm.findFragmentByTag(whichFrag);

                    try {
                        startHrs = Integer.parseInt(fragment_warHHMM.getHH());
                    } catch (NumberFormatException e) {
                        startHrs = 0;
                    }
                    try {
                        startMin = Integer.parseInt(fragment_warHHMM.getMin());
                    } catch (NumberFormatException e) {
                        startMin = 0;
                    }
                    startTime = DateTimeHelper.getTimeFromNow(startHrs, startMin, tzId, mContext);
                } else if (whichFrag.equalsIgnoreCase(STARTTIME)) {
                    Fragment_WarTimeStart fragmentWarTimeStart = (Fragment_WarTimeStart) fm.findFragmentByTag(whichFrag);
                    startTime = fragmentWarTimeStart.getStartTime();
                }
                startTime = String.format(STARTTIMEFORMAT, startTime + " (" + tzId + ")");
            } else {
                startTime = "";
            }
            List<String> p = new ArrayList<>();

            if (outpost_rbn.isChecked()) {
                Iterator<String> planetSetIterator = selectedPlanets.iterator();
                while (planetSetIterator.hasNext()) {
                    p.add(planetSetIterator.next().toString());
                }
            } else if (nooutpost_rbn.isChecked()) {
                p.add("NO OUTPOST WAR");
            }


            mCallBack.getList(startTime, p);

        }
    }

    class PlanetsChecked implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (countPlanetsChecked > 3) { //dirty fix for if user presses back
                countPlanetsChecked = 3;
            }
            CheckBox checkBox = (CheckBox) compoundButton;
            if (b) {
                countPlanetsChecked++;
                selectedPlanets.add(checkBox.getText().toString());
            } else {
                countPlanetsChecked = countPlanetsChecked - 1;
                selectedPlanets.remove(checkBox.getText().toString());
            }
            for (CheckBox chk : outpostChks) {
                if (!chk.isChecked() && countPlanetsChecked == 3) {
                    chk.setEnabled(false);
                } else {
                    chk.setEnabled(true);
                }
            }
        }
    }

    class TzSelected implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    class RdChng implements RadioButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            int checkedId = buttonView.getId();

            if (isChecked) {
                switch (checkedId) {
                    case R.id.tillSt_rdbn:
                        setFragment(new Fragment_WarHHMM(), HHMM);
                        break;
                    case R.id.stTim_rdbn:
                        setFragment(new Fragment_WarTimeStart(), STARTTIME);
                        break;
                    case R.id.nooutpost_rbn:
                        setPlanetSpinnerDefaults();
                        outposts_linlayout.setVisibility(View.GONE);
                        break;
                    case R.id.outpost_rbn:
                        outposts_linlayout.setVisibility(View.VISIBLE);
                        break;
                }
            }
        }

    }
}
