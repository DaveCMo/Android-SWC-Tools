package com.swctools.activity_modules.player.recycler_adaptors;

/**
 * Created by David on 09/03/2018.
 */


import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swctools.R;
import com.swctools.common.enums.BattleOutcome;

import com.swctools.activity_modules.player.models.Battle;
import com.swctools.common.view_adaptors.view_holders.ViewHolder_Defence;

import java.util.List;

public class RecyclerAdaptor_Defence extends RecyclerView.Adapter<ViewHolder_Defence> {
    private static final String TAG = "RecyclerAdaptor_Defence";
    public List<Battle> defenceList;
    protected int COLOR_GREEN;
    protected int COLOR_RED;
    private Context context;
    private BattleLogCallback mCallBack;


    public RecyclerAdaptor_Defence(List<Battle> defenceList, Context context) {
        this.defenceList = defenceList;
        this.context = context;
        this.mCallBack = (BattleLogCallback) context;

        COLOR_GREEN = context.getResources().getColor(R.color.green);
        COLOR_RED = context.getResources().getColor(R.color.dark_red);
    }

    public static void setBottomMargin(View view, int bottomMargin) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            params.setMargins(params.leftMargin, params.topMargin, params.rightMargin, bottomMargin);
            view.requestLayout();
        }

    }

    @Override
    public ViewHolder_Defence onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_defence_list_row, parent, false);
        return new ViewHolder_Defence(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder_Defence holder, final int position) {
        final Battle defenceResult = defenceList.get(position);
        final boolean extraOpen;

        Log.d(TAG, "onBindViewHolder: " + defenceResult.getDefender().getPlayerId() + "|" + defenceResult.getDefender().getPlayerName());
        Log.d(TAG, "onBindViewHolder: " + defenceResult.getAttacker().getPlayerId() + "|" + defenceResult.getAttacker().getPlayerName());
        String DAMAGE_FORMAT = "%1$s %2$s";
        holder.result.setText(defenceResult.getRESULT());
        holder.percentDamage.setText(String.format(DAMAGE_FORMAT, defenceResult.getPERCENT(), defenceResult.getSTARS()));
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            holder.attackedBy.setText(Html.fromHtml(defenceResult.getHTMLAttackedBy(), Html.FROM_HTML_MODE_LEGACY));
        } else {
            holder.attackedBy.setText(Html.fromHtml(defenceResult.getHTMLAttackedBy()));
        }
        holder.attackedDate.setText(defenceResult.getAttackedDate(context));
        holder.credits.setText(defenceResult.getCREDITS());
        holder.alloy.setText(defenceResult.getALLOY());
        holder.contra.setText(defenceResult.getCONTRABAND());

        holder.medals.setText(defenceResult.getMEDALS());
        holder.gears.setText(defenceResult.getCONFLICTGEARS());

        if (defenceResult.getRESULT().equals(BattleOutcome.DEFEAT.toString())) {
            holder.medals.setTextColor(COLOR_RED);
            holder.gears.setTextColor(COLOR_RED);
            holder.percentDamage.setTextColor(COLOR_RED);
        } else if (defenceResult.getRESULT().equals(BattleOutcome.VICTORY.toString())) {
            holder.medals.setTextColor(COLOR_GREEN);
            holder.gears.setTextColor(COLOR_GREEN);
            holder.percentDamage.setTextColor(COLOR_GREEN);
        }
        if (defenceResult.isNewBattle()) {
            holder.newDefenceIcon.setVisibility(View.VISIBLE);
        }


//        holder.defenceTroopsUsedList.setLines(defenceResult.getTroopsExpendedList().size() + 1);
//        holder.defenceTroopsUsedList.setMaxLines(defenceResult.getTroopsExpendedList().size() + 1);
//        holder.defenceTroopsUsedList.setText(defenceResult.getTroopList());


//        if (defenceResult.getGuildTroopsExpendedList().size() > 0) {
//            holder.defenceExtra_SCUSedContainer.setVisibility(View.VISIBLE);
//            holder.def_ExtraSCTroopList.setVisibility(View.VISIBLE);
//            holder.def_ExtraSCTroopList.setLines(defenceResult.getGuildTroopsExpendedList().size() + 1);
//            holder.def_ExtraSCTroopList.setMaxLines(defenceResult.getGuildTroopsExpendedList().size() + 1);
//            holder.def_ExtraSCTroopList.setText(defenceResult.getGuildToopsList());
//            holder.def_SCUsed.setText("Yes");
//        }
//
//        if (defenceResult.getAttackerArmoury().noActivated() > 0) {
//            holder.defenceArmouryUsed.setMaxLines(defenceResult.getAttackerArmoury().noActivated() + 1);
//            holder.defenceArmouryUsed.setLines(defenceResult.getAttackerArmoury().noActivated() + 1);
//            holder.defenceArmouryUsed.setText(defenceResult.getAttackerArmoury().getArmouryList());
//        }
//        if (position + 1 == getItemCount()) {
//            setBottomMargin(holder.itemView, (int) (72 * Resources.getSystem().getDisplayMetrics().density));
//        } else {
//            setBottomMargin(holder.itemView, 0);
//        }

        holder.defenceRowMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                    controlExtraDetail();

                mCallBack.viewBattleDetail(defenceResult);
            }
        });

    }

    @Override
    public int getItemCount() {
        return defenceList.size();
    }


    public interface BattleLogCallback {
        void viewBattleDetail(Battle defenceResult);
    }


}
