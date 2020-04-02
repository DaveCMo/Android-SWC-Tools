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

public class Fragment_WarTimeStart extends Fragment {
    private EditText warStartTime;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.war_details_timestart, container, false);
        warStartTime = (EditText) view.findViewById(R.id.warStartTime);
        return view;

    }

    public String getStartTime() {
        return warStartTime.getText().toString();
    }
}
