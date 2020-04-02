package com.swctools.common.view_adaptors.recycler_adaptors;

import android.content.Context;
import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.swctools.R;
import com.swctools.base.ApplyLayoutInterface;
import com.swctools.interfaces.FavouriteLayoutRowItemInterface;
import com.swctools.interfaces.LayoutVersionViewAdaptorInterface;
import com.swctools.layouts.models.FavouriteLayoutItem;
import com.swctools.util.StringUtil;
import com.swctools.activity_modules.main.views.ViewHolder_FavouriteLayout;

import java.io.File;
import java.util.List;

import static com.swctools.R.drawable.layout_editor;

public class RecyclerAdaptor_FavouriteLayout extends RecyclerView.Adapter<ViewHolder_FavouriteLayout> {
    private static final String TAG = "RecyclerAdaptor_FavouriteLayout";
    List<FavouriteLayoutItem> favouriteLayoutItemList;
    private Context mContext;
    private FavouriteLayoutRowItemInterface mCallBack;
    private ApplyLayoutInterface applyLayoutInterface;
    private LayoutVersionViewAdaptorInterface layoutVersionViewAdaptorInterface;
    private boolean adjustForFab, autoGenFav;
    private final Float DEFAULTIMAGEALPHA = 0.25f;
    private final Float REALIMAGEALPHA = 1.0f;
    private String playerId;
    private boolean showImages;
    private int bottomMargin;


    public RecyclerAdaptor_FavouriteLayout(List<FavouriteLayoutItem> favouriteLayoutItemList, boolean adjustForFab, boolean autoGenFav, boolean showImages, String playerId, Context context) {
        this.mContext = context;
        this.favouriteLayoutItemList = favouriteLayoutItemList;
        mCallBack = (FavouriteLayoutRowItemInterface) mContext;
        applyLayoutInterface = (ApplyLayoutInterface) mContext;
        layoutVersionViewAdaptorInterface = (LayoutVersionViewAdaptorInterface) mCallBack;
        this.adjustForFab = adjustForFab;
        this.autoGenFav = autoGenFav;
        this.playerId = playerId;
        this.showImages = showImages;
        this.bottomMargin = 32 / (int) mContext.getResources().getDisplayMetrics().density;

    }

    public void setData(List<FavouriteLayoutItem> favouriteLayoutItemList) {
        this.favouriteLayoutItemList = favouriteLayoutItemList;
    }

    public void setIsAutoGen(boolean autoGenFav) {
        this.autoGenFav = autoGenFav;
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
    public ViewHolder_FavouriteLayout onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_favourite_layout, parent, false);

        return new ViewHolder_FavouriteLayout(itemView, autoGenFav);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder_FavouriteLayout holder, final int position) {
        final FavouriteLayoutItem favouriteLayoutItem = favouriteLayoutItemList.get(position);
        String versionString = "v%1$s";
        holder.favourite_layout_version.setText(String.format(versionString, String.valueOf(favouriteLayoutItem.getLayoutVersion())));
        holder.favourite_layout_name.setText(favouriteLayoutItem.getLayoutName());
        if (position + 1 == getItemCount()) {
            setBottomMargin(holder.itemView, (int) (72 * Resources.getSystem().getDisplayMetrics().density));
        } else {
            setBottomMargin(holder.itemView, bottomMargin);
        }


        if (showImages) {
            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(layout_editor);
            if (StringUtil.isStringNotNull(favouriteLayoutItem.getImagePath())) {

                File file = new File(favouriteLayoutItem.getImagePath());


//                    RequestOptions requestOptions = new RequestOptions();

                Glide.with(holder.itemView.getContext())
                        .setDefaultRequestOptions(requestOptions)
                        .load(file)
                        .into(holder.layoutFavImg);
            } else {
                holder.layoutFavImg.setImageDrawable(null);
                holder.layoutFavImg.setAlpha(DEFAULTIMAGEALPHA);
            }
        } else {
            holder.layoutFavImg.setImageDrawable(null);
            holder.layoutFavImg.setAlpha(DEFAULTIMAGEALPHA);
        }


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
                    if (autoGenFav) {
                        mCallBack.deleteMostLastUsedLayoutLog(getFavouriteLayoutItem(position).getLayoutId(), getFavouriteLayoutItem(position).getLayoutVersion());
                    } else {
                        mCallBack.removeFavourite(getFavouriteLayoutItem(position).getLayoutId());
                    }
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return favouriteLayoutItemList.size();
    }

    private FavouriteLayoutItem getFavouriteLayoutItem(int position) {
        return favouriteLayoutItemList.get(position);
    }


}
