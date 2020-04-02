package com.swctools.activity_modules.defence_tracker.recycler_adaptor;

/**
 * Created by David on 09/03/2018.
 */


import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.swctools.R;
import com.swctools.config.AppConfig;
import com.swctools.activity_modules.main.models.PlayerDAO;
import com.swctools.util.StringUtil;
import com.swctools.common.view_adaptors.view_holders.ViewHolder_PlayerNotification;

import java.util.List;

public class RecyclerAdaptor_PlayerNotification extends RecyclerView.Adapter<ViewHolder_PlayerNotification> {
    private static final String TAG = "plNotifListAdapt";
    public List<PlayerDAO> playerList;
    private AppConfig appConfig;
    private Context context;
    private UpdatePlayerNotificationInterface mInterface;


    public RecyclerAdaptor_PlayerNotification(List<PlayerDAO> playerList, Context context) {
        this.playerList = playerList;
        this.context = context;
        appConfig = new AppConfig(context);
        mInterface = (UpdatePlayerNotificationInterface) context;
    }

    @Override
    public ViewHolder_PlayerNotification onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_player_notification, parent, false);

        return new ViewHolder_PlayerNotification(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder_PlayerNotification holder, int position) {
        final PlayerDAO playerDAO = playerList.get(position);
        holder.pNotification_Name.setText(StringUtil.htmlRemovedGameName(playerDAO.getPlayerName()));

        try {
            if (playerDAO.getNotifications().equalsIgnoreCase("1")) {
                holder.pNotification_Switch.setChecked(true);
            } else {
                holder.pNotification_Switch.setChecked(false);
            }

        } catch (Exception e) {
//            e.printStackTrace();
            holder.pNotification_Switch.setChecked(false);
        }

        holder.pNotification_Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                mInterface.togglePlayerNotification(playerDAO.getDb_rowId(), holder.pNotification_Switch.isChecked());
            }
        });

        holder.pNotification_Switch.setEnabled(appConfig.bNotificationsEnabled());
        holder.pNotification_Name.setEnabled(appConfig.bNotificationsEnabled());

    }

    public void toggleRowEnabled(int position, boolean enabled){
        playerList.get(position).setNotifications(String.valueOf(enabled));
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return playerList.size();
    }

    public void clear() {

    }


    public interface UpdatePlayerNotificationInterface {
        void togglePlayerNotification(int db_Id, Boolean isSelected);
    }
}
