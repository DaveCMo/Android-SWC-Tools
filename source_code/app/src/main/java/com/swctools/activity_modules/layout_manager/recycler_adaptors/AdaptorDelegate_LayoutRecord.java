package com.swctools.activity_modules.layout_manager.recycler_adaptors;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.swctools.R;
import com.swctools.config.AppConfig;
import com.swctools.common.helpers.ImageHelpers;
import com.swctools.base.ApplyLayoutInterface;
import com.swctools.interfaces.LayoutVersionViewAdaptorInterface;
import com.swctools.activity_modules.layout_manager.LayoutListInterface;
import com.swctools.activity_modules.layout_manager.views.ViewHolder_Layout;
import com.swctools.layouts.LayoutHelper;
import com.swctools.layouts.models.LayoutRecord;
import com.swctools.util.StringUtil;

import java.io.File;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.swctools.R.drawable.layout_editor;

public class AdaptorDelegate_LayoutRecord {
    private static final String TAG = "LayoutRecordAdaptorDel";
    private static final String VERSIONSTMPL = "Versions: (%1$s)";
    private int viewType;
    private Context mContext;
    private Toolbar toolbar;
    private AppConfig appConfig;
    private LayoutListInterface mInterface;
    private ApplyLayoutInterface applyLayoutInterface;
    private LayoutVersionViewAdaptorInterface layoutVersionViewAdaptorInterface;


    public AdaptorDelegate_LayoutRecord(int viewType, Context context) {
        this.viewType = viewType;
        mContext = context;
        mInterface = (LayoutListInterface) context;
        applyLayoutInterface = (ApplyLayoutInterface) context;
        layoutVersionViewAdaptorInterface = (LayoutVersionViewAdaptorInterface) context;
        this.appConfig = new AppConfig(mContext);

    }

    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_card, parent, false);
        toolbar = (Toolbar) v.findViewById(R.id.layoutCard_Toolbar);
        toolbar.inflateMenu(R.menu.layout_card_menu);
        return new ViewHolder_Layout(v);
    }


    public int getViewType() {
        return this.viewType;
    }

    public int getItemViewType() {
        return this.viewType;
    }

    public boolean isForViewType(ArrayList<Object> items, int position) {
        if (items.get(position) instanceof LayoutRecord) {
            if (!(((LayoutRecord) items.get(position)).isSelected())) {
                return true;
            }
        }

        return false;//items.get(position) instanceof LayoutRecord;
    }


    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder tholder, ArrayList<Object> itemList, final int position) {
        final LayoutRecord layoutContainer = (LayoutRecord) itemList.get(position);
        final ViewHolder_Layout holder = (ViewHolder_Layout) tholder;
        int myLayoutId = layoutContainer.getLayoutId();
        holder.layoutCard_LayoutName.setText(layoutContainer.getLayoutName());
        if (StringUtil.isStringNotNull(layoutContainer.getPlayerName())) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                holder.layoutCard_Player.setText(Html.fromHtml(StringUtil.htmlformattedGameName(layoutContainer.getPlayerName()), Html.FROM_HTML_MODE_LEGACY));
            } else {
                holder.layoutCard_Player.setText(Html.fromHtml(StringUtil.htmlformattedGameName(layoutContainer.getPlayerName())));
            }
        } else {
            holder.layoutCard_Player.setText("");
        }


        if (appConfig.bLayoutImageOn()) {
            try {
                holder.layoutImage.setVisibility(View.VISIBLE);

                RequestOptions requestOptions = new RequestOptions()
                        .placeholder(layout_editor);
                if (StringUtil.isStringNotNull(layoutContainer.getImageURIStr())) {

                    File file = new File(layoutContainer.getImageURIStr());
                    Glide.with(tholder.itemView.getContext())
                            .setDefaultRequestOptions(requestOptions)
                            .load(file)
                            .into(holder.layoutImage);
//                    holder.layoutImage.setImageBitmap(ImageHelpers.bytesToBitmap(layoutContainer.getLayoutImageBytes()));
//                    File file = new File(layoutContainer.getImageURIStr());
//                    if (file.exists()) {
////                        holder.layoutImage.setImageURI(Uri.fromFile(file));
//                        holder.layoutImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                    } else {
//                        holder.layoutImage.setVisibility(View.GONE);
//                    }
                } else {
                    holder.layoutImage.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                holder.layoutImage.setVisibility(View.GONE);

            }
        } else {
            holder.layoutImage.setVisibility(View.GONE);
        }
        if (layoutContainer.getLayoutFaction() != null) {
            holder.layoutCard_factionImage.setImageDrawable(ImageHelpers.factionIcon(layoutContainer.getLayoutFaction(), mContext));
        } else {
            holder.layoutCard_factionImage.setImageDrawable(null);
        }

        holder.layoutFavourite.setImageDrawable(ImageHelpers.getFavouriteIcon(layoutContainer.getLayoutIsFavourite(), mContext));
        holder.noVersionsTxt.setText(String.format(VERSIONSTMPL, String.valueOf(layoutContainer.getCountVersions())));
        RecyclerAdaptor_LayoutTagPillList recyclerAdaptorLayoutTagPillList = new RecyclerAdaptor_LayoutTagPillList(layoutContainer.getLayoutTags(), false, mContext);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(mContext, 1, GridLayoutManager.HORIZONTAL, true);

        holder.layoutTagRecycler.setLayoutManager(mLayoutManager);
//        playerDetailsCapRecycler.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));
        holder.layoutTagRecycler.setItemAnimator(new DefaultItemAnimator());
        holder.layoutTagRecycler.setAdapter(recyclerAdaptorLayoutTagPillList);
        recyclerAdaptorLayoutTagPillList.notifyDataSetChanged();
        //Set listeners...

        holder.btnViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mInterface.loadLayout(layoutContainer);
            }
        });
        holder.layoutCard_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInterface.loadLayout(layoutContainer);
            }
        });
        holder.layoutCard_Layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mInterface.layoutSelected(layoutContainer.getLayoutId(), position);
                return true;
            }
        });

        holder.layoutCard_Toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.layout_card_delete) {
                    mInterface.deleteSelectedLayout(layoutContainer.getLayoutId());

                } else if (itemId == R.id.layout_card_moveFolder) {
                    mInterface.moveLayoutToNewFolder(layoutContainer.getLayoutId(), layoutContainer.getLayoutFolderId());
                } else if (itemId == R.id.layout_card_share) {
                    layoutVersionViewAdaptorInterface.shareSelectedLayout(layoutContainer.getLayoutId(), layoutContainer.getDefaultLayoutVersion());
                } else if (itemId == R.id.layout_card_export) {
                    layoutVersionViewAdaptorInterface.exportSelectedLayout(layoutContainer.getLayoutId(), layoutContainer.getDefaultLayoutVersion());
                }
                return true;
            }
        });

        holder.layoutCard_selectAndAppl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int versionID = LayoutHelper.getMaxVersion(layoutContainer.getLayoutId(), mContext);
                applyLayoutInterface.applySelectedLayout(layoutContainer.getLayoutId(), versionID);
            }
        });

        holder.layoutCard_UpdateWAR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int versionID = LayoutHelper.getMaxVersion(layoutContainer.getLayoutId(), mContext);
                applyLayoutInterface.applySelectedLayoutWar(layoutContainer.getLayoutId(), versionID);
            }
        });
        holder.layoutFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentValue = layoutContainer.getLayoutIsFavourite();
                String newFavValue = LayoutHelper.NO;
                if (StringUtil.isStringNotNull(currentValue)) {
                    if (currentValue.equalsIgnoreCase(LayoutHelper.NO)) {
                        newFavValue = LayoutHelper.YES;
                    }
                } else {
                    newFavValue = LayoutHelper.YES;
                }
                mInterface.markFavourite(layoutContainer.getLayoutId(), position, newFavValue);
            }
        });


    }

}
