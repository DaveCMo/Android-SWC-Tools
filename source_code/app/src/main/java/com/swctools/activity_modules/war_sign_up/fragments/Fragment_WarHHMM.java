package com.swctools.activity_modules.war_sign_up.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.swctools.R;

public class Fragment_WarHHMM extends Fragment {
    private EditText warsStart_hh, warsStart_mm;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.war_details_time_hrmin, container, false);
        warsStart_hh = (EditText) view.findViewById(R.id.warsStart_hh);
        warsStart_mm = (EditText) view.findViewById(R.id.warStart_mm);
        return view;

    }

    public String getHH(){
        return warsStart_hh.getText().toString();
    }
    public String getMin(){
        return warsStart_mm.getText().toString();
    }

}
