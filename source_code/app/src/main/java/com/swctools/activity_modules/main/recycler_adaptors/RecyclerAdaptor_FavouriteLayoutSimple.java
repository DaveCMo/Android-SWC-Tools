package com.swctools.activity_modules.main.recycler_adaptors;

import android.content.Context;
import android.content.res.Resources;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import com.swctools.R;
import com.swctools.base.ApplyLayoutInterface;
import com.swctools.interfaces.FavouriteLayoutRowItemInterface;
import com.swctools.interfaces.LayoutVersionViewAdaptorInterface;
import com.swctools.layouts.models.FavouriteLayoutItem;
import com.swctools.common.view_adaptors.view_holders.ViewHolder_LayoutSimple;

import java.util.List;

public class RecyclerAdaptor_FavouriteLayoutSimple extends RecyclerView.Adapter<ViewHolder_LayoutSimple> {
    private static final String TAG = "RecyclerAdaptor_FavouriteLayoutSimple";
    List<FavouriteLayoutItem> favouriteLayoutItemList;
    private Context mContext;
    private FavouriteLayoutRowItemInterface mCallBack;
    private ApplyLayoutInterface applyLayoutInterface;
    private LayoutVersionViewAdaptorInterface layoutVersionViewAdaptorInterface;
    private String playerId;
    private int bottomMargin;
    private int favType;
    private boolean adjustforFab;


    public RecyclerAdaptor_FavouriteLayoutSimple(List<FavouriteLayoutItem> favouriteLayoutItemList, int favType, String playerId, boolean adjustforFab, Context context) {
        this.mContext = context;
        this.favouriteLayoutItemList = favouriteLayoutItemList;
        mCallBack = (FavouriteLayoutRowItemInterface) mContext;
        applyLayoutInterface = (ApplyLayoutInterface) mContext;
        layoutVersionViewAdaptorInterface = (LayoutVersionViewAdaptorInterface) mCallBack;
        this.playerId = playerId;
        this.favType = favType;
        this.bottomMargin = 32 / (int) mContext.getResources().getDisplayMetrics().density;
        this.adjustforFab = adjustforFab;

    }

    public void setData(List<FavouriteLayoutItem> favouriteLayoutItemList) {
        this.favouriteLayoutItemList = favouriteLayoutItemList;
    }


    public static void setBottomMargin(View view, int bottomMargin) {

        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            params.setMargins(params.leftMargin, params.topMargin, params.rightMargin, bottomMargin);
            view.requestLayout();
        }

    }

    @NonNull
    @Override
    public ViewHolder_LayoutSimple onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_favourite_layout_simple, parent, false);
        Toolbar toolbar = itemView.findViewById(R.id.favLayoutToolbar);
        toolbar.inflateMenu(R.menu.layout_fav_menu);

        return new ViewHolder_LayoutSimple(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder_LayoutSimple holder, final int position) {
        final FavouriteLayoutItem favouriteLayoutItem = favouriteLayoutItemList.get(position);
        String versionString = "v%1$s";
        holder.favourite_layout_version.setText(String.format(versionString, String.valueOf(favouriteLayoutItem.getLayoutVersion())));
        holder.favourite_layout_name.setText(favouriteLayoutItem.getLayoutName());

        holder.updatePVP_Pill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyLayoutInterface.applySelectedLayout(getFavouriteLayoutItem(position).getLayoutId(), getFavouriteLayoutItem(position).getLayoutVersion(), playerId);
            }
        });

        holder.updateWar_Pill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyLayoutInterface.applySelectedLayoutWar(getFavouriteLayoutItem(position).getLayoutId(), getFavouriteLayoutItem(position).getLayoutVersion(), playerId);

            }
        });
        holder.favLayoutToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int menuItemId = menuItem.getItemId();
                if (menuItemId == R.id.layout_fav_share) {
                    layoutVersionViewAdaptorInterface.shareSelectedLayout(getFavouriteLayoutItem(position).getLayoutId(), getFavouriteLayoutItem(position).getLayoutVersion());
                } else if (menuItemId == R.id.layout_fav_export) {
                    layoutVersionViewAdaptorInterface.exportSelectedLayout(getFavouriteLayoutItem(position).getLayoutId(), getFavouriteLayoutItem(position).getLayoutVersion());
                } else if (menuItemId == R.id.layout_fav_delete) {
                    switch (favType) {
                        case 0:
                            mCallBack.removeFavourite(getFavouriteLayoutItem(position).getLayoutId());
                            break;
                        case 1:
                            mCallBack.deleteTopLayout(playerId, getFavouriteLayoutItem(position).getLayoutId());
                            break;
                        default:
                            mCallBack.deleteMostLastUsedLayoutLog(getFavouriteLayoutItem(position).getLayoutId(), getFavouriteLayoutItem(position).getLayoutVersion());
                            break;
                    }
                }
                return true;
            }
        });

        if (adjustforFab) {
            if (position + 1 == getItemCount()) {
                setBottomMargin(holder.itemView, (int) (72 * Resources.getSystem().getDisplayMetrics().density));
            } else {
                setBottomMargin(holder.itemView, bottomMargin);
            }
        }
    }

    @Override
    public int getItemCount() {
        return favouriteLayoutItemList.size();
    }

    private FavouriteLayoutItem getFavouriteLayoutItem(int position) {
        return favouriteLayoutItemList.get(position);
    }


}
