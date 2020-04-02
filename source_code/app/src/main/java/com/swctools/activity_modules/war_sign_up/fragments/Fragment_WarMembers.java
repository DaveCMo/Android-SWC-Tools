package com.swctools.activity_modules.war_sign_up.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.swctools.R;
import com.swctools.common.enums.BundleKeys;
import com.swctools.swc_server_interactions.results.SWCGetPublicGuildResponseData;
import com.swctools.activity_modules.war_sign_up.models.GuildMember;
import com.swctools.activity_modules.war_sign_up.view_adaptors.RecyclerAdaptor_MemberList;
import com.swctools.activity_modules.war_sign_up.interfaces.WarMembersInterface;

import java.util.ArrayList;

public class Fragment_WarMembers extends Fragment {
    private static final String TAG = "FRAGWARMEMBERS";
    private static final String NUMSELECTED = "(%1$s/15)";
    private Context mContext;
    private RecyclerView memberSelect_ryclr;
    private Button memberSelectGo;
    private TextView numSelected;
    private ArrayList<GuildMember> guildMemberArrayLists;
    private RecyclerAdaptor_MemberList mAdaptor;
    private WarMembersInterface mCallBack;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        this.mCallBack = (WarMembersInterface) mContext;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
        this.mCallBack = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_war_signup_memberselect, container, false);
        try {
            String guildData = getArguments().getString(BundleKeys.HIT_LIST_GUILD.toString(), "");
            memberSelect_ryclr = (RecyclerView) view.findViewById(R.id.memberSelect_ryclr);
            memberSelectGo = (Button) view.findViewById(R.id.memberSelectGo);
            numSelected = (TextView) view.findViewById(R.id.numSelected);

            guildMemberArrayLists = new ArrayList<>();
            SWCGetPublicGuildResponseData guildResponseData = new SWCGetPublicGuildResponseData(guildData);
            for (int i = 0; i < guildResponseData.getMembers().size(); i++) {
                GuildMember guildMember = new GuildMember(guildResponseData.getMembers().getJsonObject(i));
                guildMemberArrayLists.add(guildMember);
            }

            mAdaptor = new RecyclerAdaptor_MemberList(guildMemberArrayLists, mContext);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
            memberSelect_ryclr.setLayoutManager(mLayoutManager);
            memberSelect_ryclr.setItemAnimator(new DefaultItemAnimator());
            memberSelect_ryclr.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));
            memberSelect_ryclr.setAdapter(mAdaptor);
            mAdaptor.notifyDataSetChanged();


            memberSelectGo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCallBack.completeList();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;

    }

    public void setNumSelected(int numSelected) {
        String s = String.format(NUMSELECTED, String.valueOf(numSelected));
        this.numSelected.setText(s);
//        mAdaptor.notifyDataSetChanged();
    }


}
