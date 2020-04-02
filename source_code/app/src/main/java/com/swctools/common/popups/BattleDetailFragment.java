package com.swctools.common.popups;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.swctools.R;
import com.swctools.common.enums.BattleOutcome;
import com.swctools.common.helpers.ImageHelpers;
import com.swctools.activity_modules.player.models.StatItemData;
import com.swctools.activity_modules.player.models.TwoTextItemData;
import com.swctools.activity_modules.player.models.Battle;
import com.swctools.common.models.player_models.TacticalCapItem;
import com.swctools.common.models.player_models.Troop;
import com.swctools.activity_modules.player.recycler_adaptors.RecyclerAdaptor_BattleDetail;

import java.util.ArrayList;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class BattleDetailFragment extends DialogFragment {
    private static final String TAG = "BattleDetailFragment";

    public static final String TITLE = "TITLE";
    public static final String DEFENCERESULT = "DEFENCERESULT";
    public static final String DMG = "DMG";
    public static final String STARS = "STARS";
    public static final String BATTLEDATETIME = "BATTLEDATETIME";
    public static final String BATTLELANET = "BATTLEPLANET";
    public static final String BATTLEENEMY = "BATTLEENEMY";
    public static final String TROOPSEXPENDED = "TROOPSEXPENDED";
    public static final String SCSEXPENDED = "SCSEXPENDED";
    public static final String MEDALS = "MEDALS";
    public static final String CONFLICTPOINTS = "CONFLICTPOINTS";
    public static final String BATTLE_RESULT = "BATTLE_RESULT";
    public static final String ATTACKER_ARMOURY = "ATTACKER_ARMOURY";
    public static final String DEFENDER_ARMOURY = "DEFENDER_ARMOURY";
    public static final String PLANET = "PLANET";


    private TextView battleResult, dmgstars, attackDateTime, opponentNameSqd, planettxt, battleLogTitle;
    private CircleImageView imgplanet;
    private String title;
    private RecyclerView battleDetailRecycler;
    private Context mContext;
    private MaterialButton closeBattleDetail;
    private Battle defenceResult;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        title = getArguments().getString(TITLE, "Battle Detail");
        outState.putString(TITLE, title);
        outState.putParcelable(DEFENCERESULT, defenceResult);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = View.inflate(getActivity(), R.layout.fragment_battle_detail, null);

        battleResult = view.findViewById(R.id.battleResult);
        dmgstars = view.findViewById(R.id.dmgstars);
        attackDateTime = view.findViewById(R.id.attackDateTime);
        opponentNameSqd = view.findViewById(R.id.opponentNameSqd);
        planettxt = view.findViewById(R.id.planettxt);
        imgplanet = view.findViewById(R.id.imgplanet);
        battleDetailRecycler = view.findViewById(R.id.battleDetailRecycler);
        closeBattleDetail = view.findViewById(R.id.closeBattleDetail);
        battleLogTitle = view.findViewById(R.id.battleLogTitle);
        builder.setView(view);

        defenceResult = getArguments().getParcelable(DEFENCERESULT);

        title = getArguments().getString(TITLE, "Battle Detail");

        imgplanet.setImageDrawable(ImageHelpers.getPlanetImage(defenceResult.getPlanetName(), mContext));

        battleLogTitle.setText(title);
        closeBattleDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        Dialog dialog = builder.create();
        try {
            attackDateTime.setText(defenceResult.getAttackedDate(mContext));
            battleResult.setText(defenceResult.getRESULT());

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                opponentNameSqd.setText(Html.fromHtml(defenceResult.getHTMLAttackedBy(), Html.FROM_HTML_MODE_LEGACY));
            } else {
                opponentNameSqd.setText(Html.fromHtml(defenceResult.getHTMLAttackedBy()));
            }
            dmgstars.setText(defenceResult.getPERCENT() + " " + defenceResult.getSTARS() + " Stars");

            setBattleDetailRecycler();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dialog;
    }


    private void setBattleDetailRecycler() {
        int COLOR_GREEN = mContext.getResources().getColor(R.color.green);
        int COLOR_RED = mContext.getResources().getColor(R.color.dark_red);
        final String ITEM_TEMPLATE = "%1$s Level %2$s";
        int capDeployed = 0;
        ArrayList<Object> itemsList = new ArrayList<>();
        int battleColour = 0;
        if (defenceResult.getRESULT().equalsIgnoreCase(BattleOutcome.DEFEAT.toString())) {
            battleColour = COLOR_RED;
        } else {
            battleColour = COLOR_GREEN;
        }
        itemsList.add(new StatItemData(defenceResult.getPERCENT(), "% Damage", battleColour));
        itemsList.add(new StatItemData(String.valueOf(defenceResult.getStars()), "Stars", battleColour));
        itemsList.add(new StatItemData(String.valueOf(defenceResult.getConflictGearsInt()), "Gears", battleColour));
        itemsList.add(new StatItemData(String.valueOf(defenceResult.getMEDALS()), "Medals", battleColour));
        itemsList.add("Troops Deployed");
        for (Troop troop : defenceResult.getTroopsExpendedList()) {
            itemsList.add(new TwoTextItemData(troop.descriptionAndLevel(), "x" + String.valueOf(troop.getNumTroops())));
        }
        if (defenceResult.getGuildTroopsExpendedList().size() > 0) {
            itemsList.add("SC Troops Deployed");
            for (Troop troop : defenceResult.getGuildTroopsExpendedList()) {
                itemsList.add(new TwoTextItemData(troop.descriptionAndLevel(), "x" + String.valueOf(troop.getNumTroops())));
            }
        }

        itemsList.add("Attacker Armoury");
        for (TacticalCapItem activeArm : defenceResult.getAttackerArmoury().getActivatedEquipment()) {
            itemsList.add(new TwoTextItemData(String.format(ITEM_TEMPLATE, activeArm.getItemName(), activeArm.getItemLevel()), ""));
        }
        itemsList.add("Defender Armoury");
        for (TacticalCapItem defendArm : defenceResult.getDefenderArmoury().getActivatedEquipment()) {
            itemsList.add(new TwoTextItemData(String.format(ITEM_TEMPLATE, defendArm.getItemName(), defendArm.getItemLevel()), ""));
        }
        final RecyclerAdaptor_BattleDetail recyclerAdaptorBattleDetail = new RecyclerAdaptor_BattleDetail(itemsList, getActivity());

        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 4, RecyclerView.VERTICAL, false);
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                try {
                    switch (recyclerAdaptorBattleDetail.getItemViewType(position)) {
                        case RecyclerAdaptor_BattleDetail.SPACER:
                            return 4;
                        case RecyclerAdaptor_BattleDetail.STATTWOTEXT:
                            return 1;
                        default:
                            return 4;
                    }
                } catch (Exception e) {
                    return -1;
                }
            }
        });

        battleDetailRecycler.setLayoutManager(mLayoutManager);
        battleDetailRecycler.setItemAnimator(new DefaultItemAnimator());

        battleDetailRecycler.setAdapter(recyclerAdaptorBattleDetail);


        recyclerAdaptorBattleDetail.notifyDataSetChanged();
    }

}
