package com.swctools.activity_modules.player.recycler_adaptors;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.swctools.R;
import com.swctools.activity_modules.player.Player_TacticalCallback;
import com.swctools.activity_modules.player.models.TacticalCapacityData;
import com.swctools.activity_modules.player.views.ViewHolder_SC;
import com.swctools.activity_modules.player.views.ViewHolder_TacticalCapacity;
import com.swctools.common.enums.ApplicationMessageTemplates;
import com.swctools.common.helpers.ImageHelpers;
import com.swctools.config.AppConfig;
import com.swctools.interfaces.SendMessageFromList;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AdaptorDelegate_SC {
    private int mExpandedPosition = -1;
    private final static String TAG = "TactCapAdapterDelegate";
    protected int viewType;
    protected Context mContext;
    protected SendMessageFromList mcallBack;
    protected Animation animClockW;
    protected Animation animAntiClockW;
    protected Drawable expandMore;
    protected Drawable expandLess;
    protected AppConfig appConfig;
    protected Drawable lockLocked;
    protected Drawable lockUnlocked;
    protected int COLOR_RED;
    protected int COLOR_GREEN;
    protected ArrayList<Boolean> rowsExpanded;
    protected ArrayList<Boolean> rowPrevBound;
    //    private int position;
    protected ArrayList<Object> itemList;
    protected Player_TacticalCallback player_tacticalCallback;

    public AdaptorDelegate_SC(int viewType, Context context, ArrayList<Object> itemList) {
        rowsExpanded = new ArrayList<>();
        for (int i = 0; i < itemList.size(); i++) {
            rowsExpanded.add(false);
        }
        rowPrevBound = new ArrayList<>();
        for (int i = 0; i < itemList.size(); i++) {
            rowPrevBound.add(false);
        }
        this.viewType = viewType;
        this.mContext = context;
        this.mcallBack = (SendMessageFromList) context;
        animClockW = AnimationUtils.loadAnimation(mContext, R.anim.rotate_180_cw);
        animAntiClockW = AnimationUtils.loadAnimation(mContext, R.anim.rotate_180_ccw);
        expandMore = ContextCompat.getDrawable(mContext, R.drawable.ic_expand_more_black_24dp);
        expandLess = ContextCompat.getDrawable(mContext, R.drawable.ic_expand_less_black_24dp);
        lockLocked = ContextCompat.getDrawable(mContext, R.drawable.ic_lock_black_24dp);
        lockUnlocked = ContextCompat.getDrawable(mContext, R.drawable.ic_lock_open_black_24dp);
        appConfig = new AppConfig(mContext);
        COLOR_RED = mContext.getResources().getColor(R.color.dark_red);
        COLOR_GREEN = mContext.getResources().getColor(R.color.green);
        player_tacticalCallback = (Player_TacticalCallback) context;

    }


    public int getViewType() {
        return this.viewType;
    }

    public int getItemViewType() {
        return this.viewType;
    }

    public boolean isForViewType(ArrayList<Object> items, int position) {
        if (items.get(position) instanceof TacticalCapacityData) {
            TacticalCapacityData tacticalCapacityData = (TacticalCapacityData) items.get(position);
            if(tacticalCapacityData.getTYPE()==TacticalCapacityData.SC){
                return true;
            }else return false;
        } else {
            return false;
        }    }


    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder tholder, final ArrayList<Object> itemList, final int position) {


        this.itemList = itemList;
        final TacticalCapacityData rowData = (TacticalCapacityData) itemList.get(position);
        final ViewHolder_SC holder = (ViewHolder_SC) tholder;
        boolean expanded = false;
        boolean locked = appConfig.getPlayerDetailExpanded(rowData.expandedSettingString);


        holder.cap_ProgressBar.setMax(rowData.getMaxCap());
        holder.cap_ProgressBar.setProgress(rowData.getCurCap());
        holder.cap_Title.setText(rowData.getTitle());
        holder.cap_Image.setImageDrawable(ImageHelpers.getImageFromStoredId(rowData.getTitleImage(), mContext));
        holder.cap_Number.setText(String.format(ApplicationMessageTemplates.PROGRESS_BAR_LABEL.getemplateString(), rowData.getCurCap(), rowData.getMaxCap()));


        RecyclerAdaptor_CapacityListViewAdaptor adaptor = new RecyclerAdaptor_CapacityListViewAdaptor(rowData.getCapacityList(), rowData.isShowQty(), mContext);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        holder.cap_List.setLayoutManager(mLayoutManager);
        holder.cap_List.setItemAnimator(new DefaultItemAnimator());
//        holder.cap_List.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));
        holder.cap_List.setAdapter(adaptor);
        adaptor.notifyDataSetChanged();

        if (rowPrevBound.get(position)) {
            expanded = rowsExpanded.get(position);
        } else {
        }
        expanded = rowData.locked;
        if (!expanded) {
            rowsExpanded.add(position, false);
            holder.imgMoreLessTacCapFlip.setImageDrawable(expandMore);
            holder.capListContainer.setVisibility(View.GONE);

        } else {
            holder.imgMoreLessTacCapFlip.setImageDrawable(expandLess);
            rowsExpanded.add(position, true);
            holder.capListContainer.setVisibility(View.VISIBLE);

        }

        if (locked) {
            holder.imgExpandLocked.setImageDrawable(lockLocked);
            holder.imgExpandLocked.setImageTintList(ColorStateList.valueOf(COLOR_GREEN));
        } else {
            holder.imgExpandLocked.setImageDrawable(lockUnlocked);
            holder.imgExpandLocked.setImageTintList(ColorStateList.valueOf(COLOR_RED));
        }


        holder.cap_Container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean visible = setVisible(holder, position);

                TacticalCapacityData tacticalCapacityData = (TacticalCapacityData) itemList.get(position);
                tacticalCapacityData.locked = visible;
            }
        });
        holder.imgExpandTacCapLess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean visible = setVisible(holder, position);

                TacticalCapacityData tacticalCapacityData = (TacticalCapacityData) itemList.get(position);
                tacticalCapacityData.locked = visible;
            }
        });

        holder.imgExpandLocked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean expanded;
                appConfig.setPlayerDetailsExpandedHintClicked();
                TacticalCapacityData rowData = (TacticalCapacityData) itemList.get(position);
                expanded = appConfig.getPlayerDetailExpanded(rowData.expandedSettingString);

                if (expanded) {
                    appConfig.setPlayerDetailExpanded(false, rowData.expandedSettingString);
                    holder.imgExpandLocked.setImageDrawable(lockUnlocked);
                    holder.imgExpandLocked.setImageTintList(ColorStateList.valueOf(COLOR_RED));
                } else {
                    appConfig.setPlayerDetailExpanded(true, rowData.expandedSettingString);
                    holder.imgExpandLocked.setImageDrawable(lockLocked);
                    holder.imgExpandLocked.setImageTintList(ColorStateList.valueOf(COLOR_GREEN));
                }
            }
        });
        holder.tacticalCalToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.pvp_request_sc) {
                    player_tacticalCallback.requestTroops(rowData.getMaxCap(), rowData.getCurCap());
                }
                return true;
            }
        });

        rowPrevBound.add(position, true);
    }

    private boolean setVisible(ViewHolder_SC holder, int position) {
        boolean visible = false;
        appConfig.setPlayerDetailsExpandedHintClicked();
        if (holder.capListContainer.getVisibility() == View.GONE) {
            holder.capListContainer.setVisibility(View.VISIBLE);
            holder.imgMoreLessTacCapFlip.setImageDrawable(expandLess);
            rowsExpanded.add(position, true);
            visible = true;

        } else {
            rowsExpanded.add(position, false);
            holder.capListContainer.setVisibility(View.GONE);
            holder.imgMoreLessTacCapFlip.setImageDrawable(expandMore);
            visible = false;

        }
        return visible;
    }


    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_player_sc_armoury, parent, false);
        return new ViewHolder_SC(view);
    }


}
