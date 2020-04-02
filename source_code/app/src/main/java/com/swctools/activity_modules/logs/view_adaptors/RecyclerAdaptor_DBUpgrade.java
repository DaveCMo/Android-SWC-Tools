package com.swctools.activity_modules.logs.view_adaptors;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swctools.R;
import com.swctools.activity_modules.logs.models.DBUpgradeRowData;
import com.swctools.activity_modules.logs.views.ViewHolder_DBUpgrade;

import java.util.List;

public class RecyclerAdaptor_DBUpgrade extends RecyclerView.Adapter<ViewHolder_DBUpgrade> {
    private static final String TAG = "RecyclerAdaptor_DBUpgrade";
    List<DBUpgradeRowData> dbUpgradeRowDataList;
    private Context mContext;
    public RecyclerAdaptor_DBUpgrade(List<DBUpgradeRowData> dbUpgradeRowDataList, Context context){
        this.dbUpgradeRowDataList = dbUpgradeRowDataList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder_DBUpgrade onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_db_upgrade_log, parent, false);

        return new ViewHolder_DBUpgrade(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder_DBUpgrade holder, int position) {

        final DBUpgradeRowData dbUpgradeRowData = dbUpgradeRowDataList.get(position);

        holder.db_upgrade_id.setText(String.valueOf(dbUpgradeRowData.id));
        holder.db_upgrade_version.setText(String.valueOf(dbUpgradeRowData.dbVersion));
        holder.db_upgrade_code.setText(dbUpgradeRowData.appCode);
        holder.db_upgrade_message.setText(dbUpgradeRowData.codeMessage);
    }

    @Override
    public int getItemCount() {
        return dbUpgradeRowDataList.size();
    }

}
