package com.swctools.activity_modules.player.recycler_adaptors;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.swctools.R;
import com.swctools.activity_modules.player.Player_Details_Callback;
import com.swctools.common.enums.Statuses;
import com.swctools.common.helpers.DateTimeHelper;
import com.swctools.common.helpers.ImageHelpers;
import com.swctools.common.models.player_models.DekoDeka;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdaptorDelegate_Droideka {
    private static final String TAG = "AdaptorDelegate_Droidek";
    private Player_Details_Callback player_details_callback;
    private int viewType;
    private Context mContext;


    public AdaptorDelegate_Droideka(int viewType, Context context) {
        this.viewType = viewType;
        this.mContext = context;
        this.player_details_callback = (Player_Details_Callback) context;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_droid_card, parent, false);
        return new DroidekaViewHolder(itemView);
    }


    public int getViewType() {
        return this.viewType;
    }

    public int getItemViewType() {
        return this.viewType;
    }

    public boolean isForViewType(ArrayList<Object> items, int position) {
        return items.get(position) instanceof DekoDeka;
    }

    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder tholder, ArrayList<Object> itemList, int position) {
        final DroidekaViewHolder holder = (DroidekaViewHolder) tholder;
        final DekoDeka dekoDeka = (DekoDeka) itemList.get(position);
        Log.d(TAG, "onBindViewHolder: " + dekoDeka.deployable);
        final String DEKO_RDY = "Ready: %1$s";
        final Float brokenDroidAlpha = 0.25f;
        final Float workingDroidAlpha = 1.0f;
        final int COLOR_GREEN = mContext.getResources().getColor(R.color.green);
        final int COLOR_RED = mContext.getResources().getColor(R.color.red);
        final int COLOR_YELLOW = mContext.getResources().getColor(R.color.creditsColor);


        holder.droidLevel.setText("Level " + (dekoDeka.level - 1));
        holder.repairDroidBtn.setVisibility(View.GONE);
        holder.droidCard_Title.setText(dekoDeka.droidekasType.toString());
        holder.droidCard_Status.setText(dekoDeka.state.toString());
        holder.readyLbl.setVisibility(View.VISIBLE);
        holder.droidCard_Image.setImageDrawable(ImageHelpers.getDroidImage(dekoDeka.level, dekoDeka.droidekasType, mContext));
        if (dekoDeka.state.equalsIgnoreCase(Statuses.DROIDEKA_DOWN.toString())) {
            holder.droidCard_ReadyBy.setText("");
            holder.droidCard_Status.setBackgroundColor(COLOR_RED);
            holder.droidCard_Image.setAlpha(brokenDroidAlpha);
            holder.droidCard_Image.setBorderColor(COLOR_RED);
            holder.repairDroidBtn.setVisibility(View.VISIBLE);
        } else if (dekoDeka.state.equalsIgnoreCase(Statuses.DROIDEKA_READY.toString())) {
            holder.droidCard_ReadyBy.setText("Now");
            holder.droidCard_Status.setBackgroundColor(COLOR_GREEN);
            holder.droidCard_Image.setAlpha(workingDroidAlpha);
            holder.droidCard_Image.setBorderColor(COLOR_GREEN);
        } else if (dekoDeka.state.equalsIgnoreCase(Statuses.DROIDEKA_REPAIRING.toString()) || dekoDeka.state.equalsIgnoreCase(Statuses.DROIDEKA_UPGRADING.toString())) {

            holder.droidCard_ReadyBy.setText(DateTimeHelper.longDateTime(dekoDeka.readyTime, mContext));
            holder.droidCard_Status.setBackgroundColor(COLOR_YELLOW);
            holder.droidCard_Image.setBorderColor(COLOR_YELLOW);
        }

        holder.repairDroidBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player_details_callback.repairDroideka(dekoDeka.deployable, dekoDeka.padBuilding);
            }
        });

    }

    class DroidekaViewHolder extends RecyclerView.ViewHolder {
        public TextView droidCard_Title, droidCard_Status, droidCard_ReadyBy, readyLbl, droidLevel;
        public CircleImageView droidCard_Image;
        public Button repairDroidBtn;

        public DroidekaViewHolder(View itemView) {
            super(itemView);
            droidCard_Title = itemView.findViewById(R.id.droidCard_Title);
            droidCard_Status = itemView.findViewById(R.id.droidCard_Status);
            droidCard_ReadyBy = itemView.findViewById(R.id.droidCard_ReadyBy);
            droidCard_Image = itemView.findViewById(R.id.planet_img);
            readyLbl = itemView.findViewById(R.id.readyLbl);
            droidLevel = itemView.findViewById(R.id.droidLevel);
            repairDroidBtn = itemView.findViewById(R.id.repairDroidBtn);
        }
    }
}
