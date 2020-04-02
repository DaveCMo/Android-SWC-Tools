package com.swctools.activity_modules.war_sign_up.view_adaptors;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swctools.R;
import com.swctools.common.helpers.ImageHelpers;
import com.swctools.activity_modules.war_sign_up.models.GuildSearchResultItem;
import com.swctools.util.StringUtil;
import com.swctools.activity_modules.war_sign_up.interfaces.GuildSearchResultRowInterface;

import java.util.List;

public class RecyclerAdaptor_GuildSearchResult extends RecyclerView.Adapter<ViewHolder_GuildSearch> {
    private List<GuildSearchResultItem> guildSearchResultItems;
    private Context mContext;
    private GuildSearchResultRowInterface mCallBack;

    public RecyclerAdaptor_GuildSearchResult(List<GuildSearchResultItem> guildSearchResultItems, Context context) {
        this.guildSearchResultItems = guildSearchResultItems;
        this.mContext = context;
        this.mCallBack = (GuildSearchResultRowInterface) this.mContext;
    }

    @NonNull
    @Override
    public ViewHolder_GuildSearch onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_guild, parent, false);
        return new ViewHolder_GuildSearch(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder_GuildSearch holder, final int position) {
        final GuildSearchResultItem guildSearchResultItem = guildSearchResultItems.get(position);
        holder.guildFaction_Img.setImageDrawable(ImageHelpers.factionIcon(guildSearchResultItem.faction, mContext));
        holder.guildName.setText(StringUtil.htmlRemovedGameName(guildSearchResultItem.guildName));
        holder.guildMembers.setText(String.valueOf(guildSearchResultItem.members));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallBack.guildSelected(guildSearchResultItems.get(position));

            }
        });
    }

    @Override
    public int getItemCount() {
        return guildSearchResultItems.size();
    }


}
