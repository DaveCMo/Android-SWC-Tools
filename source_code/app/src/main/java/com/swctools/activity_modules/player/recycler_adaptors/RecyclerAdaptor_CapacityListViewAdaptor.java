package com.swctools.activity_modules.player.recycler_adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swctools.R;
import com.swctools.activity_modules.player.views.ViewHolder_CapListRow;
import com.swctools.common.models.player_models.TacticalCapItem;
import com.swctools.interfaces.SendMessageFromList;
import com.swctools.util.StringUtil;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.res.Resources.getSystem;

public class RecyclerAdaptor_CapacityListViewAdaptor extends RecyclerView.Adapter<ViewHolder_CapListRow> {
    private SendMessageFromList mcallBack;

    private static final String TAG = "CapacityListViewAdaptor";
    List<TacticalCapItem> capacityList;

    private boolean showQty;


    public RecyclerAdaptor_CapacityListViewAdaptor(List<TacticalCapItem> capacityList, boolean showQty, Context context) {
        this.capacityList = capacityList;
        this.showQty = showQty;
        this.mcallBack = (SendMessageFromList) context;
    }

    @NonNull
    @Override
    public ViewHolder_CapListRow onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_2_text_group_item_nonbold, parent, false);
        return new ViewHolder_CapListRow(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder_CapListRow holder, final int position) {
        final TacticalCapItem rowItem = capacityList.get(position);
        final String ITEM_TEMPLATE = "%1$s Level %2$s";
        String levelToRoman = StringUtil.toRoman(rowItem.getItemLevel());
        if (!levelToRoman.equalsIgnoreCase("")) {
            holder.list_item_group_3_left.setText(String.format("%1$s Level %2$s", rowItem.getItemName(), StringUtil.toRoman(rowItem.getItemLevel())));
        } else {
            holder.list_item_group_3_left.setText(rowItem.getItemName());

        }

        if (showQty) {
            holder.list_item_group_3_right.setText(String.format(" x%1$s", rowItem.getItemQty()));
        }

        if (position + 1 == getItemCount()) {
            setBottomMargin(holder.itemView, (int) (8 * getSystem().getDisplayMetrics().density));
        } else if (position == 0) {
//                setTopMargin(holder.itemView, 8);
            setBottomMargin(holder.itemView, 0);
        } else {
            setBottomMargin(holder.itemView, 0);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String ITEM_TEMPLATE = "%1$s Level %2$s ";
                TacticalCapItem item = capacityList.get(position);
                String levelToRoman = StringUtil.toRoman(item.getItemLevel());
                String title = "";
                if (!levelToRoman.equalsIgnoreCase("")) {
                    title = String.format(ITEM_TEMPLATE, item.getItemName(), StringUtil.toRoman(item.getItemLevel()));
                } else {
                    title = item.getItemName();
                }
                if (item.getItemDetail() != null) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(title + "\n");
                    if (item.getItemDetail() != null) {
                        for (String detail : item.getItemDetail()) {
                            stringBuilder.append(StringUtil.htmlRemovedGameName(detail) + "\n");
                        }
                    }
                    mcallBack.showMessage(title, stringBuilder.toString());
                }

            }
        });



    }

    @Override
    public int getItemCount() {
        try {
            return capacityList.size();
        } catch (Exception e) {
            return 0;
        }
    }

    void setBottomMargin(View view, int bottomMargin) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            params.setMargins(params.leftMargin, params.topMargin, params.rightMargin, bottomMargin);
            view.requestLayout();
        }

    }

    void setTopMargin(View view, int topMargin) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            params.setMargins(params.leftMargin, topMargin, params.rightMargin, params.rightMargin);
            view.requestLayout();
        }

    }
}
