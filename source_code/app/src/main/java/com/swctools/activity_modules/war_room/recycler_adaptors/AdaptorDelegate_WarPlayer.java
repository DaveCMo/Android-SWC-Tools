package com.swctools.activity_modules.war_room.recycler_adaptors;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import com.swctools.R;
import com.swctools.activity_modules.war_room.WarParticipantInterface;
import com.swctools.activity_modules.war_room.models.War_WarParticipant;
import com.swctools.activity_modules.war_room.views.ViewHolder_WarPlayer;
import com.swctools.common.enums.ApplicationMessageTemplates;
import com.swctools.common.helpers.DateTimeHelper;
import com.swctools.common.helpers.ImageHelpers;
import com.swctools.util.StringUtil;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdaptorDelegate_WarPlayer {
    private static final String TAG = "AdaptorDel_WarPlayer";
    private WarParticipantInterface warParticipantInterface;
    private int viewType;
    private Context mContext;
    private boolean mySquad;

    public AdaptorDelegate_WarPlayer(int viewType, Context context) {
        this.viewType = viewType;
        this.mContext = context;
        warParticipantInterface = (WarParticipantInterface) context;
        this.mySquad = mySquad;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_war_player, parent, false);

        return new ViewHolder_WarPlayer(itemView);
    }


    public int getViewType() {
        return this.viewType;
    }


    public boolean isForViewType(ArrayList<War_WarParticipant> items, int position) {
        if (items.get(position).getIs_requester().equalsIgnoreCase("1")) {
            return true;
        } else {
            return false;
        }
    }

    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder tholder, ArrayList<War_WarParticipant> listItems, int position) {
        final ViewHolder_WarPlayer viewHolder = (ViewHolder_WarPlayer) tholder;
        final War_WarParticipant warParticipant = listItems.get(position);
        viewHolder.txtAttacksLeft.setText(String.valueOf(warParticipant.getTurns()));
        viewHolder.requestButton.setVisibility(View.GONE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            viewHolder.warPlayerName.setText(Html.fromHtml(StringUtil.htmlformattedGameName(warParticipant.getPlayerName()), Html.FROM_HTML_MODE_LEGACY));
        } else {
            viewHolder.warPlayerName.setText(Html.fromHtml(StringUtil.htmlformattedGameName(warParticipant.getPlayerName())));
        }

        viewHolder.plystatus_links.setRating(Float.parseFloat(String.valueOf(warParticipant.getVictoryPoints())));
        viewHolder.plystatus_links.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                viewHolder.plystatus_links.setRating(Float.parseFloat(String.valueOf(warParticipant.getVictoryPoints())));
            }
        });
        try {
            viewHolder.warPlayerFactionImg.setImageDrawable(ImageHelpers.factionIcon(warParticipant.getFaction(), mContext));
        } catch (Exception e) {
//            e.printStackTrace();
        }

        try {
            viewHolder.sullustSCFullBar.setMax(warParticipant.getScCap_Donated());
            viewHolder.sullustSCFullBar.setProgress(warParticipant.getScCap());
        } catch (Exception e) {

        }


        try {
            if (!(StringUtil.isStringNotNull(warParticipant.getIsErrorWithSC()))) {
                viewHolder.sullustSCText.setText(String.format(ApplicationMessageTemplates.PROGRESS_BAR_LABEL.getemplateString(), String.valueOf(warParticipant.getScCap_Donated()), String.valueOf(warParticipant.getScCap())));
            } else if (warParticipant.getIsErrorWithSC().equalsIgnoreCase("0")) {
                viewHolder.sullustSCText.setText(String.format(ApplicationMessageTemplates.PROGRESS_BAR_LABEL.getemplateString(), String.valueOf(warParticipant.getScCap_Donated()), String.valueOf(warParticipant.getScCap())));
                if (warParticipant.getScCap() != warParticipant.getScCap_Donated()) {
                    viewHolder.requestButton.setVisibility(View.VISIBLE);
                }
            } else {
                viewHolder.sullustSCText.setText("Error!");
            }
        } catch (Exception e) {
//            e.printStackTrace();
        }


        viewHolder.hqBaseScore.setText("HQ" + warParticipant.getHqLevel() + " - " + warParticipant.getBaseScore());

        viewHolder.warPlayerBG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                warParticipantInterface.showLastDefence(warParticipant.getPlayerId(), warParticipant.getGuildId());
            }
        });
        viewHolder.viewSCBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                warParticipantInterface.showDonatedTroops();
            }
        });

        viewHolder.attackedBy.setVisibility(View.GONE);

        if (StringUtil.isStringNotNull(warParticipant.getLastAttackedByName())) {
            viewHolder.attackedBy.setVisibility(View.VISIBLE);
            viewHolder.attackedBy.setText("Attacked by " + warParticipant.getLastAttackedByName() + " (" + DateTimeHelper.timeFromEPOCH(warParticipant.getLastAttacked(), mContext) + ")");
        }

        viewHolder.requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                warParticipantInterface.requestSC(warParticipant.getPlayerId());
            }
        });
    }
}
