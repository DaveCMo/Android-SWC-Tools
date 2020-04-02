package com.swctools.activity_modules.war_sign_up.view_adaptors;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.swctools.R;
import com.swctools.activity_modules.war_sign_up.models.GuildMember;
import com.swctools.activity_modules.war_sign_up.interfaces.WarMembersListCallBack;

import java.util.ArrayList;

public class RecyclerAdaptor_MemberList extends RecyclerView.Adapter<ViewHolder_MemberList> {
    private static final String TAG = "MEMBERVH";
    private ArrayList<GuildMember> guildMemberArrayLists;
    private Context mContext;
    private int selected;
    private WarMembersListCallBack mCallback;

    public RecyclerAdaptor_MemberList(ArrayList<GuildMember> guildMemberArrayLists, Context context) {
        this.guildMemberArrayLists = guildMemberArrayLists;
        this.mCallback = (WarMembersListCallBack) context;
    }

    @NonNull
    @Override
    public ViewHolder_MemberList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_war_party_members, parent, false);
        return new ViewHolder_MemberList(this, itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder_MemberList holder, final int position) {
        final GuildMember guildMember = guildMemberArrayLists.get(position);
        holder.war_pty_name.setText(guildMember.memberName);
        holder.war_pty_chk.setChecked(guildMember.selected);
        holder.war_pty_chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mCallback.memberChkChanged(guildMember, guildMember.selected);
            }
        });

    }

    @Override
    public int getItemCount() {
        return guildMemberArrayLists.size();
    }




}
