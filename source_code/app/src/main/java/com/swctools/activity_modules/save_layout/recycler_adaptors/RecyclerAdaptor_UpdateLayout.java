package com.swctools.activity_modules.save_layout.recycler_adaptors;

/**
 * Created by David on 09/03/2018.
 */


import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swctools.R;
import com.swctools.common.enums.Factions;
import com.swctools.util.StringUtil;
import com.swctools.layouts.models.LayoutRecord;
import com.swctools.common.view_adaptors.view_holders.ViewHolder_UpdateLayout;

import java.util.List;

public class RecyclerAdaptor_UpdateLayout extends RecyclerView.Adapter<ViewHolder_UpdateLayout> {
    private static final String TAG = "RecyclerAdaptor_UpdateL";
    public List<LayoutRecord> layoutRecordList;

    private Context context;
    private UpdateLayoutAdaptorInterface mInterface;
    private int defence_list_row;


    public RecyclerAdaptor_UpdateLayout(List<LayoutRecord> layoutRecords, Context context) {
        this.layoutRecordList = layoutRecords;
        this.context = context;
        mInterface = (UpdateLayoutAdaptorInterface) context;
    }

    @Override
    public ViewHolder_UpdateLayout onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.update_layout_list_item, parent, false);

        return new ViewHolder_UpdateLayout(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder_UpdateLayout holder, int position) {
        final LayoutRecord layoutRecord = layoutRecordList.get(position);
        try {
            holder.updateLayout_Name.setText(layoutRecord.getLayoutName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (StringUtil.isStringNotNull(layoutRecord.getPlayerName())) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    holder.updateLayoutPlayer.setText(Html.fromHtml(StringUtil.htmlformattedGameName(layoutRecord.getPlayerName()) ,Html.FROM_HTML_MODE_LEGACY));
                } else {
                    holder.updateLayoutPlayer.setText(Html.fromHtml(StringUtil.htmlformattedGameName(layoutRecord.getPlayerName())));
                }
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            if (this.layoutRecordList.get(position).getLayoutFaction().equalsIgnoreCase(Factions.EMPIRE.getFactionName())) {
                holder.updateLayoutFaction.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.ic_empire_foreground));
            } else if (this.layoutRecordList.get(position).getLayoutFaction().equalsIgnoreCase(Factions.REBEL.getFactionName())) {
                holder.updateLayoutFaction.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.ic_rebel_foreground));
            }
        } catch (Exception e) {

        }

        holder.updateLayoutSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInterface.selectLayout(layoutRecord.getLayoutId());
            }
        });

//        holde
//        holder.result.setText(defenceResult.getRESULT());

    }

    @Override
    public int getItemCount() {
        return layoutRecordList.size();
    }

    public void clear() {

    }

    public interface UpdateLayoutAdaptorInterface{
        void selectLayout(int LAYOUT_ID);
    }
}
