package com.swctools.activity_modules.war_battles.view_adaptors;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swctools.R;
import com.swctools.activity_modules.war_battles.models.War_BattleHeader;
import com.swctools.activity_modules.war_battles.views.ViewHolder_WarAttackHeader;
import com.swctools.common.helpers.DateTimeHelper;
import com.swctools.util.StringUtil;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdaptorDelegate_WarAttackHeader {
    private static final String TAG = "AdaptorDelegate_WarAtta";
    private int viewType;
    private Context context;

    public AdaptorDelegate_WarAttackHeader(int viewType, Context context) {
        this.viewType = viewType;
        this.context = context;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_war_attack_header, parent, false);
        return new ViewHolder_WarAttackHeader(itemView);
    }

    public int getViewType() {
        return this.viewType;
    }

    public boolean isForViewType(ArrayList<Object> itemList, int position) {
        if (itemList.get(position) instanceof War_BattleHeader) {
            return true;
        } else {
            return false;
        }
    }

    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, ArrayList<Object> itemList, int position) {
        final ViewHolder_WarAttackHeader view = (ViewHolder_WarAttackHeader) viewHolder;
        final War_BattleHeader war_battleHeader = (War_BattleHeader) itemList.get(position);
        view.againstOps.setVisibility(View.GONE);
        view.withOps.setVisibility(View.GONE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            view.attackedByName.setText(Html.fromHtml(StringUtil.htmlformattedGameName(war_battleHeader.getAttackedBy()), Html.FROM_HTML_MODE_LEGACY));
        } else {
            view.attackedByName.setText(Html.fromHtml(StringUtil.htmlformattedGameName(war_battleHeader.getAttackedBy())));
        }

        String starsLeftTxt = "";
        if (war_battleHeader.getStarsLeft() == 1) {
            starsLeftTxt = " Star left";
        } else {
            starsLeftTxt = " Stars left";
        }
        view.attackResult.setText(" (" + war_battleHeader.getStarsLeft() + starsLeftTxt + ")");

        if (StringUtil.isStringNotNull(war_battleHeader.getWithOps())) {
            view.withOps.setText("Attacked with: " + war_battleHeader.getWithOps());
            view.withOps.setVisibility(View.VISIBLE);
        }
        if (StringUtil.isStringNotNull(war_battleHeader.getAgainstOps())) {
            view.againstOps.setText("Against Outposts: " + war_battleHeader.getAgainstOps());
            view.againstOps.setVisibility(View.VISIBLE);
        }

        view.attackedByDate.setText(DateTimeHelper.longDateTime(war_battleHeader.getDateTimeOfAttack(), context));


    }
}
