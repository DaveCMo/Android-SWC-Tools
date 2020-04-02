package com.swctools.activity_modules.war_room.recycler_adaptors;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swctools.R;
import com.swctools.activity_modules.war_room.WarParticipantInterface;
import com.swctools.activity_modules.war_room.models.War_WarParticipant;
import com.swctools.activity_modules.war_room.views.ViewHolder_WarParticipant;
import com.swctools.common.helpers.DateTimeHelper;
import com.swctools.common.helpers.ImageHelpers;
import com.swctools.util.StringUtil;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdaptorDelegate_WarParticipant_Mmbr {
    private static final String TAG = "AdapdelWarPart";
    private WarParticipantInterface warParticipantInterface;

    private int viewType;
    private Context mContext;
    private boolean mySquad;

    public AdaptorDelegate_WarParticipant_Mmbr(int viewType, boolean mySquad, Context context) {
        this.viewType = viewType;
        this.mContext = context;
        warParticipantInterface = (WarParticipantInterface) context;
        this.mySquad = mySquad;

    }

    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_war_participant, parent, false);
        return new ViewHolder_WarParticipant(itemView);
    }


    public int getViewType() {
        return this.viewType;
    }


    public boolean isForViewType(ArrayList<War_WarParticipant> items, int position) {

        if (items.get(position).getIs_requester().equalsIgnoreCase("0")) {
            return true;
        } else {
            return false;
        }
    }

    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder tholder, ArrayList<War_WarParticipant> listItems, int position) {
        final ViewHolder_WarParticipant _viewHolderWarParticipant = (ViewHolder_WarParticipant) tholder;
        final War_WarParticipant warParticipant = listItems.get(position);
        _viewHolderWarParticipant.warPart_Attacks.setText(String.valueOf(warParticipant.getTurns()));

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            _viewHolderWarParticipant.warPartPlrName.setText(Html.fromHtml(StringUtil.htmlformattedGameName(warParticipant.getPlayerName()), Html.FROM_HTML_MODE_LEGACY));
        } else {
            _viewHolderWarParticipant.warPartPlrName.setText(Html.fromHtml(StringUtil.htmlformattedGameName(warParticipant.getPlayerName())));
        }


        _viewHolderWarParticipant.warPart_Stars.setRating(Float.parseFloat(String.valueOf(warParticipant.getVictoryPoints())));

        try {
            _viewHolderWarParticipant.warPart_FactionImg.setImageDrawable(ImageHelpers.factionIcon(warParticipant.getFaction(), mContext));
        } catch (Exception e) {
//            e.printStackTrace();
        }


        _viewHolderWarParticipant.warPartBG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                warParticipantInterface.showLastDefence(warParticipant.getPlayerId(), warParticipant.getGuildId());
            }
        });
        _viewHolderWarParticipant.lastAttackedBy.setVisibility(View.GONE);
        _viewHolderWarParticipant.hqBaseScore.setText("HQ" + warParticipant.getHqLevel() + " - " + warParticipant.getBaseScore());
        if (StringUtil.isStringNotNull(warParticipant.getLastAttackedByName())) {
            _viewHolderWarParticipant.lastAttackedBy.setVisibility(View.VISIBLE);
            _viewHolderWarParticipant.lastAttackedBy.setText("Attacked by " + warParticipant.getLastAttackedByName() + " (" + DateTimeHelper.timeFromEPOCH(warParticipant.getLastAttacked(),mContext) +")");
        }

    }
}
