package com.swctools.activity_modules.layout_detail.view_adaptors;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import com.swctools.R;
import com.swctools.base.ApplyLayoutInterface;
import com.swctools.interfaces.LayoutVersionViewAdaptorInterface;
import com.swctools.layouts.models.LayoutVersion;
import com.swctools.common.view_adaptors.view_holders.ViewHolder_LayoutSimple;

import java.util.List;

public class RecyclerAdaptor_LayoutVersion extends RecyclerView.Adapter<ViewHolder_LayoutSimple> {
    private static final String TAG = "LayoutVersionAdaptor";
    private List<LayoutVersion> layoutVersionList;
    private Context mContext;
    private LayoutVersionViewAdaptorInterface mCallBack;
    private ApplyLayoutInterface applyLayoutInterface;
    private Toolbar toolbar;

    public RecyclerAdaptor_LayoutVersion(List<LayoutVersion> layoutVersions, Context mContext) {
        this.layoutVersionList = layoutVersions;
        this.mContext = mContext;
        mCallBack = (LayoutVersionViewAdaptorInterface) mContext;
        applyLayoutInterface = (ApplyLayoutInterface) mContext;
    }

    @NonNull
    @Override
    public ViewHolder_LayoutSimple onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_favourite_layout_simple, parent, false);
        toolbar = (Toolbar) v.findViewById(R.id.favLayoutToolbar);
        toolbar.inflateMenu(R.menu.layout_version_menu);
        return new ViewHolder_LayoutSimple(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder_LayoutSimple holder, final int position) {
        final LayoutVersion layoutVersion = layoutVersionList.get(position);
        holder.favourite_layout_name.setText(String.valueOf(layoutVersion.getVersionId()));
        holder.updateWar_Pill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyLayoutInterface.applySelectedLayoutWar(layoutVersion.getLayoutId(), layoutVersion.getVersionId());
            }
        });

        holder.updatePVP_Pill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyLayoutInterface.applySelectedLayout(layoutVersion.getLayoutId(), layoutVersion.getVersionId());
            }
        });


        holder.favLayoutToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.layout_version_export) {
                    mCallBack.exportSelectedLayout(layoutVersion.getLayoutId(), layoutVersion.getVersionId());
                } else if (itemId == R.id.layout_version_delete) {
                    mCallBack.deleteSelectedLayoutVersion(layoutVersion.getRowId(), layoutVersion.getLayoutId(), layoutVersion.getVersionId());
                } else if (itemId == R.id.layout_version_share) {
                    mCallBack.shareSelectedLayout(layoutVersion.getLayoutId(), layoutVersion.getVersionId());
                } else if (itemId == R.id.layout_version_editJson) {
                    mCallBack.editLayoutJson(layoutVersion.getLayoutId(), layoutVersion.getVersionId());
                }

                return true;
            }
        });
    }


    @Override
    public int getItemCount() {
        return layoutVersionList.size();
    }


}
