package com.swctools.activity_modules.war_sign_up.view_adaptors;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.swctools.R;

public class ViewHolder_GuildSearch extends RecyclerView.ViewHolder {
    public ImageView guildFaction_Img;
    public TextView guildName, guildMembers;


    public ViewHolder_GuildSearch(View itemView) {
        super(itemView);
        guildFaction_Img = (ImageView) itemView.findViewById(R.id.guildFaction_Img);
        guildName = (TextView) itemView.findViewById(R.id.guildName);
        guildMembers = (TextView) itemView.findViewById(R.id.guildMembers);
    }
}
