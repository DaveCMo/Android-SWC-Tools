package com.swctools.activity_modules.logs.views;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.swctools.R;

public class ViewHolder_DBUpgrade extends RecyclerView.ViewHolder{
    public TextView db_upgrade_id, db_upgrade_version, db_upgrade_code, db_upgrade_message;
    public ViewHolder_DBUpgrade(View itemView) {
        super(itemView);
        db_upgrade_id = (TextView) itemView.findViewById(R.id.db_upgrade_idH);
        db_upgrade_version = (TextView) itemView.findViewById(R.id.db_upgrade_versionH);
        db_upgrade_code = (TextView) itemView.findViewById(R.id.db_upgrade_codeH);
        db_upgrade_message = (TextView) itemView.findViewById(R.id.db_upgrade_message);

    }
}
