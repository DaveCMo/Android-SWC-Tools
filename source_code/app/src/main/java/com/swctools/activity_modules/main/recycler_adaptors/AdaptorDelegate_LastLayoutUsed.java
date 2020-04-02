package com.swctools.activity_modules.main.recycler_adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import com.swctools.R;
import com.swctools.base.ApplyLayoutInterface;
import com.swctools.interfaces.FavouriteLayoutRowItemInterface;
import com.swctools.interfaces.LayoutVersionViewAdaptorInterface;
import com.swctools.layouts.models.LastUsedLayout;
import com.swctools.common.view_adaptors.delegated_adaptors.AdaptorDelegate;
import com.swctools.common.view_adaptors.view_holders.ViewHolder_LayoutSimple;

import java.lang.reflect.Type;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdaptorDelegate_LastLayoutUsed extends AdaptorDelegate {
    private static final String TAG = "AdaptorDelegate_LastLay";
    private String versionString = "v%1$s";
    private FavouriteLayoutRowItemInterface mCallBack;
    private ApplyLayoutInterface applyLayoutInterface;
    private LayoutVersionViewAdaptorInterface layoutVersionViewAdaptorInterface;

    public AdaptorDelegate_LastLayoutUsed(int viewType, Context mContext) {
        super(viewType);
        this.viewType = viewType;
        this.mCallBack = (FavouriteLayoutRowItemInterface) mContext;
        this.applyLayoutInterface = (ApplyLayoutInterface) mContext;
        this.layoutVersionViewAdaptorInterface = (LayoutVersionViewAdaptorInterface) mCallBack;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_favourite_layout_simple, parent, false);
        Toolbar toolbar = itemView.findViewById(R.id.favLayoutToolbar);
        toolbar.inflateMenu(R.menu.layout_fav_menu);
        return new ViewHolder_LayoutSimple(itemView);
    }

    @Override
    public boolean isForViewType(ArrayList<Object> itemList, int position) {
        if (itemList.get(position) instanceof LastUsedLayout) {
            return true;
        }
        return false;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, ArrayList<Type> itemList, int position) {
        final ViewHolder_LayoutSimple viewHolder = (ViewHolder_LayoutSimple) holder;
        if (itemList.get(position) instanceof LastUsedLayout) {
            final LastUsedLayout lastUsedLayout = (LastUsedLayout) itemList.get(position);
            viewHolder.favourite_layout_name.setText(lastUsedLayout.getLayoutName());
            viewHolder.favourite_layout_version.setText(String.format(versionString, String.valueOf(lastUsedLayout.getVersionId())));

            viewHolder.updatePVP_Pill.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    applyLayoutInterface.applySelectedLayout(lastUsedLayout.getLayoutId(), lastUsedLayout.getVersionId(), lastUsedLayout.getPlayerId());
                }
            });

            viewHolder.updateWar_Pill.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    applyLayoutInterface.applySelectedLayoutWar(lastUsedLayout.getLayoutId(), lastUsedLayout.getVersionId(), lastUsedLayout.getPlayerId());
                }
            });
            viewHolder.favLayoutToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    int menuItemId = menuItem.getItemId();
                    if (menuItemId == R.id.layout_fav_share) {
                        layoutVersionViewAdaptorInterface.shareSelectedLayout(lastUsedLayout.getLayoutId(), lastUsedLayout.getVersionId());
                    } else if (menuItemId == R.id.layout_fav_export) {
                        layoutVersionViewAdaptorInterface.exportSelectedLayout(lastUsedLayout.getLayoutId(), lastUsedLayout.getVersionId());
                    } else if (menuItemId == R.id.layout_fav_delete) {
                        mCallBack.deleteMostLastUsedLayoutLog(lastUsedLayout.getLayoutId(), lastUsedLayout.getVersionId());
                    }
                    return true;
                }
            });

        }

    }
}
