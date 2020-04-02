package com.swctools.activity_modules.war_sign_up.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.swctools.R;
import com.swctools.common.enums.BundleKeys;
import com.swctools.util.StringUtil;
import com.swctools.activity_modules.war_sign_up.interfaces.HitListCallBack;

public class Fragment_WarHitList extends Fragment {
    private TextView hitList;
    private ImageView copy_img, share_img;
    private HitListCallBack mCallBack;
    private Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        this.mCallBack = (HitListCallBack) mContext;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mCallBack = null;
        this.mContext = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        String result = getArguments().getString(BundleKeys.HIT_LIST_RESULT.toString());
        View view = inflater.inflate(R.layout.fragment_war_signup_guildresults, container, false);
        hitList = (TextView) view.findViewById(R.id.hitListResult);
        copy_img = (ImageView) view.findViewById(R.id.copy_img);
        share_img = (ImageView) view.findViewById(R.id.share_img);
        if (StringUtil.isStringNotNull(result)) {
            hitList.setText(result);
        }
        copy_img.setOnClickListener(new CopyClick());
        share_img.setOnClickListener(new ShareClick());
        return view;
    }

    class ShareClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            mCallBack.shareText(hitList.getText().toString());
        }
    }

    class CopyClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            mCallBack.copyText(hitList.getText().toString());
        }
    }


}
