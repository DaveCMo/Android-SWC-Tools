package com.swctools.activity_modules.war_sign_up.view_adaptors;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.swctools.R;

public class ViewHolder_MemberList extends RecyclerView.ViewHolder {
    private RecyclerAdaptor_MemberList recyclerAdaptor_memberList;
    public TextView war_pty_name;
    public CheckBox war_pty_chk;

    public ViewHolder_MemberList(RecyclerAdaptor_MemberList recyclerAdaptor_memberList, View itemView) {
        super(itemView);
        this.recyclerAdaptor_memberList = recyclerAdaptor_memberList;
        war_pty_name = (TextView) itemView.findViewById(R.id.war_pty_name);
        war_pty_chk = (CheckBox) itemView.findViewById(R.id.war_pty_chk);
//            war_pty_chk.setSelected(guildMemberArrayLists.get(getLayoutPosition()).selected);

        war_pty_chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                int position = getLayoutPosition();

            }
        });
    }

}
