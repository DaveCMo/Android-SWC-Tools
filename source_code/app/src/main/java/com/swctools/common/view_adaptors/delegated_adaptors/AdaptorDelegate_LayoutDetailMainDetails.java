package com.swctools.common.view_adaptors.delegated_adaptors;

import android.content.Context;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;

import com.swctools.R;
import com.swctools.common.base_adaptors.FactionListBaseAdaptor;
import com.swctools.common.base_adaptors.PlayerListBaseAdaptor;
import com.swctools.common.helpers.ImageHelpers;
import com.swctools.common.helpers.PlayerHelper;
import com.swctools.util.StringUtil;
import com.swctools.layouts.models.LayoutDetailMainData;
import com.swctools.common.view_adaptors.view_holders.ViewHolder_LayoutDetailMain;

import java.io.File;
import java.util.ArrayList;

public class AdaptorDelegate_LayoutDetailMainDetails {
    private static final String TAG = "DETAILADAPTORDEL";

    private int viewType;
    private Context mContext;
    private LayoutDetailListMainDataInterface mActivityCallBack;
    private ViewHolder_LayoutDetailMain viewHolder;
    private ArrayList<String> spinnerArray;
    private ArrayList<PlayerHelper> playerList;
    private FactionListBaseAdaptor factionListBaseAdaptor;
    private PlayerListBaseAdaptor playerListBaseAdaptor;
    private boolean spinnersPopulated;

    public AdaptorDelegate_LayoutDetailMainDetails(int viewType, Context context) {
        this.viewType = viewType;
        this.mContext = context;
        this.mActivityCallBack = (LayoutDetailListMainDataInterface) context;
    }


    public int getViewType() {
        return this.viewType;
    }

    public int getItemViewType() {
        return this.viewType;
    }

    public boolean isForViewType(ArrayList<Object> items, int position) {
        boolean b = items.get(position) instanceof LayoutDetailMainData;

        return items.get(position) instanceof LayoutDetailMainData;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout_detail_main, parent, false);
        return new ViewHolder_LayoutDetailMain(itemView);
    }

    public void setNameEnabled() {
        if (viewHolder.layoutName.isEnabled()) {
            viewHolder.layoutName.setEnabled(false);
        } else {
            viewHolder.layoutName.setEnabled(true);
        }
    }

    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder tholder, ArrayList<Object> itemList, int position) {
        spinnersPopulated = false;
        viewHolder = (ViewHolder_LayoutDetailMain) tholder;
        LayoutDetailMainData itemData = (LayoutDetailMainData) itemList.get(position);


        viewHolder.layoutName.setText(itemData.getLayoutName());
        viewHolder.layoutName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                mActivityCallBack.nameChanged(s.toString());


            }
        });

        try {
            if (itemData.getImageStr() != null) {
                File file = new File(itemData.getImageStr());
                if (file.exists()) {
                    viewHolder.layoutDetailImg.setImageURI(Uri.fromFile(file));
                    viewHolder.layoutDetailImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }
            }
        } catch (Exception e) {

        }
        viewHolder.layoutDetailImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivityCallBack.chooseLayoutImage();
            }
        });
        viewHolder.layoutFavImg.setImageDrawable(ImageHelpers.getFavouriteIcon(itemData.getLayoutIsFav(), mContext));
        viewHolder.layoutFavImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivityCallBack.setFav();
            }
        });

        setPlayerSpinner(itemData.getPlayerId(), viewHolder.playerSpinner);
        setLayoutFactionSpinner(itemData.getLayoutFaction(), viewHolder.factionSpinner);

        viewHolder.factionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (spinnersPopulated) {
                    mActivityCallBack.factionChanged(factionListBaseAdaptor.getFactions()[position]);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                //meh
            }

        });

        viewHolder.playerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (spinnersPopulated) {
                    mActivityCallBack.playerChanged(playerListBaseAdaptor.getPlayerList().get(position).playerId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                //meh
            }

        });
        viewHolder.img_layoutDetaildeleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.layoutDetailImg.setImageDrawable(null);
                mActivityCallBack.clearImage();
            }
        });

        spinnersPopulated = true;

    }


    public interface LayoutDetailListMainDataInterface {
        void setFav();

        void nameChanged(String s);

        void playerChanged(String playerId);

        void factionChanged(String faction);

        void chooseLayoutImage();

        void clearImage();
    }

    private void setLayoutFactionSpinner(@Nullable String playerFaction, Spinner layoutFactionSpinner) {

        factionListBaseAdaptor = new FactionListBaseAdaptor(mContext);
        layoutFactionSpinner.setAdapter(factionListBaseAdaptor);
        try {
            if (StringUtil.isStringNotNull(playerFaction)) {
                for (int i = 0; i < layoutFactionSpinner.getCount(); i++) {
                    if (factionListBaseAdaptor.getFactions()[i].equalsIgnoreCase(playerFaction)) {
                        layoutFactionSpinner.setSelection(i);
                    }
                }
            }
        } catch (Exception e) {

        }

    }

    private void setPlayerSpinner(@Nullable String playerId, Spinner layoutPlayerSpinner) {

        playerListBaseAdaptor = new PlayerListBaseAdaptor(mContext);
        layoutPlayerSpinner.setAdapter(playerListBaseAdaptor);
        if (StringUtil.isStringNotNull(playerId)) {
            for (int i = 0; i < layoutPlayerSpinner.getCount(); i++) {
                if (playerListBaseAdaptor.getPlayerList().get(i).playerId.equalsIgnoreCase(playerId)) {
                    layoutPlayerSpinner.setSelection(i);
                }
            }
        }
    }
}
