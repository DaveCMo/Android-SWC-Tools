package com.swctools.activity_modules.war_room.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.swctools.R;
import com.swctools.activity_modules.war_room.WarDashSignupListInterface;
import com.swctools.config.AppConfig;
import com.swctools.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

public class FragmentWarSignup extends Fragment {
    private static final String TAG = "FragmentWarSignup";
    private CheckBox includeStartTime, inclRemainingULChk, splitClearsChk, includeScoreChk, includeBSCHk;
    private RadioGroup radioOpSelected;
    private RadioButton outpost_rbn, nooutpost_rbn;
    private Spinner tzSpinner;
    private TextView warList;
    private ImageView copy_img, share_img;
    private MaterialButton getListButton;
    private ConstraintLayout tzConstraint;

    //UI VARIABLES
    private String tzId;
    private boolean includeTz = false;
    private boolean includeOps = false;
    private boolean includeULS = false;
    private boolean splitList = false;
    private boolean includeScore = false;
    private boolean includeBS = false;
    private Context mContext;
    private WarDashSignupListInterface warDashSignupListInterface;
    AppConfig appConfig;

    public static FragmentWarSignup newInstance() {
        return new FragmentWarSignup();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        warDashSignupListInterface = (WarDashSignupListInterface) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mContext = null;
        this.warDashSignupListInterface = null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        appConfig = new AppConfig(getContext());

        View view = inflater.inflate(R.layout.fragment_war_dash_signup, container, false);
        setViews(view);


        return view;
    }


    private void setViews(View view) {
        includeStartTime = view.findViewById(R.id.includeStartTime);
        inclRemainingULChk = view.findViewById(R.id.inclRemainingULChk);
        includeScoreChk = view.findViewById(R.id.includeScoreChk);
        splitClearsChk = view.findViewById(R.id.splitClearsChk);
        radioOpSelected = view.findViewById(R.id.radioOpSelected);
        includeBSCHk = view.findViewById(R.id.includeBS);
        tzSpinner = view.findViewById(R.id.tzSpinner);
        warList = view.findViewById(R.id.warList);
        copy_img = view.findViewById(R.id.copy_img);
        share_img = view.findViewById(R.id.share_img);
        getListButton = view.findViewById(R.id.getListButton);
        tzConstraint = view.findViewById(R.id.tzConstraint);
        outpost_rbn = view.findViewById(R.id.outpost_rbn);
        nooutpost_rbn = view.findViewById(R.id.nooutpost_rbn);

        if (appConfig.getWarSignUpConfig(AppConfig.Settings.WARROOM_INCLUDESTART)) {
            includeTz = true;
            includeStartTime.setChecked(true);
        }
        if (appConfig.getWarSignUpConfig(AppConfig.Settings.WARROOM_INCLUDEUPLINKS)) {
            includeULS = true;
            inclRemainingULChk.setChecked(true);
        }
        if (appConfig.getWarSignUpConfig(AppConfig.Settings.WARROOM_INCLUDESCORE)) {
            includeScore = true;
            includeScoreChk.setChecked(true);
        }
        if (appConfig.getWarSignUpConfig(AppConfig.Settings.WARROOM_SPLITCLEARS)) {
            splitList = true;
            splitClearsChk.setChecked(true);
        }
        if (appConfig.getWarSignUpConfig(AppConfig.Settings.WARROOM_OUTPOSTWAR)) {
            includeOps = true;
            outpost_rbn.setChecked(true);
        } else if (!appConfig.getWarSignUpConfig(AppConfig.Settings.WARROOM_OUTPOSTWAR)) {
            nooutpost_rbn.setChecked(true);
        }
        if (appConfig.getWarSignUpConfig(AppConfig.Settings.WARROOM_INCLUDEBASESCORE)) {
            includeBS = true;
            includeBSCHk.setChecked(true);
        }


        setTzSpinner();
        includeStartTime.setOnCheckedChangeListener(new IncludeStartListener().invoke());
        getListButton.setOnClickListener(new GetListListener().invoke());
        radioOpSelected.setOnCheckedChangeListener(new OpSelectedListener().invoke());
        share_img.setOnClickListener(new ShareListener().invoke());
        copy_img.setOnClickListener(new CopyListener().invoke());
        inclRemainingULChk.setOnCheckedChangeListener(new IncludeULListener().invoke());
        splitClearsChk.setOnCheckedChangeListener(new SplitClearsListener().invoke());
        includeScoreChk.setOnCheckedChangeListener(new IncludeScoreListener().invoke());
        includeBSCHk.setOnCheckedChangeListener(new IncludeBSListener().invoke());

    }

    private void sendRebuildListCommand() {
        String selectedTzKey = tzSpinner.getSelectedItem().toString();
        tzId = StringUtil.getTZIds().get(selectedTzKey);
        if (!StringUtil.isStringNotNull(tzId)) {
            tzId = TimeZone.getDefault().getID();
        }
        warDashSignupListInterface.triggerRebuildList(tzId, includeTz, includeOps, includeULS, splitList, includeScore, includeBS);
    }

    private void setTzSpinner() {
        List<String> tzList = new ArrayList<>();
        tzList.add("Default");
        for (Map.Entry<String, String> entry : StringUtil.getTZIds().entrySet()) {
            tzList.add(entry.getKey());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, tzList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tzSpinner.setAdapter(adapter);
    }

    public void setHitList(String s) {
        warList.setText(s);
    }


    private class IncludeStartListener {
        public CompoundButton.OnCheckedChangeListener invoke() {
            return new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        tzConstraint.setVisibility(View.VISIBLE);
                        includeTz = true;
                    } else {
                        tzConstraint.setVisibility(View.GONE);
                        includeTz = false;
                    }
                    appConfig.setWar_SignUpConfig(includeTz, AppConfig.Settings.WARROOM_INCLUDESTART);

                    sendRebuildListCommand();
                }
            };
        }
    }

    private class OpSelectedListener {
        public RadioGroup.OnCheckedChangeListener invoke() {
            return new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    if (i == R.id.outpost_rbn) {
                        includeOps = true;
                    } else if (i == R.id.nooutpost_rbn) {
                        includeOps = false;
                    }
                    appConfig.setWar_SignUpConfig(includeOps, AppConfig.Settings.WARROOM_OUTPOSTWAR);

                    sendRebuildListCommand();
                }
            };
        }
    }

    private class ShareListener {
        public View.OnClickListener invoke() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    warDashSignupListInterface.shareText(warList.getText().toString());
                }
            };
        }
    }

    private class CopyListener {
        public View.OnClickListener invoke() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    warDashSignupListInterface.copyText(warList.getText().toString());
                }
            };
        }
    }

    private class IncludeULListener {
        public CompoundButton.OnCheckedChangeListener invoke() {
            return new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    includeULS = b;
                    appConfig.setWar_SignUpConfig(b, AppConfig.Settings.WARROOM_INCLUDEUPLINKS);
                    sendRebuildListCommand();
                }
            };
        }
    }

    private class SplitClearsListener {
        public CompoundButton.OnCheckedChangeListener invoke() {
            return new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    splitList = b;
                    appConfig.setWar_SignUpConfig(b, AppConfig.Settings.WARROOM_SPLITCLEARS);
                    sendRebuildListCommand();
                }
            };
        }
    }

    private class IncludeScoreListener {
        public CompoundButton.OnCheckedChangeListener invoke() {
            return new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    includeScore = b;
                    appConfig.setWar_SignUpConfig(b, AppConfig.Settings.WARROOM_INCLUDESCORE);

                    sendRebuildListCommand();
                }
            };
        }
    }

    private class IncludeBSListener {
        public CompoundButton.OnCheckedChangeListener invoke() {
            return new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean b) {
                    includeBS = b;
                    appConfig.setWar_SignUpConfig(b, AppConfig.Settings.WARROOM_INCLUDEBASESCORE);
                    sendRebuildListCommand();
                }
            };
        }
    }

    private class GetListListener {
        public View.OnClickListener invoke() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String selectedTzKey = tzSpinner.getSelectedItem().toString();
                    tzId = StringUtil.getTZIds().get(selectedTzKey);
                    if (!StringUtil.isStringNotNull(tzId)) {
                        tzId = TimeZone.getDefault().getID();
                    }

                    sendRebuildListCommand();
                }
            };
        }
    }
}
