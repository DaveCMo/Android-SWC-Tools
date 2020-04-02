package com.swctools.activity_modules.main.recycler_adaptors;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toolbar;

import com.swctools.R;
import com.swctools.common.base_adaptors.FavouriteLayoutBaseAdaptor;
import com.swctools.config.AppConfig;
import com.swctools.common.enums.PlayerFavPref;
import com.swctools.common.helpers.ImageHelpers;
import com.swctools.interfaces.PlayerListInterface;
import com.swctools.layouts.models.FavouriteLayoutItem;
import com.swctools.activity_modules.main.models.PlayerDAO_WithLayouts;
import com.swctools.util.StringUtil;
import com.swctools.common.view_adaptors.view_holders.ViewHolder_PlayerCard;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdaptor_PlayerList extends RecyclerView.Adapter<ViewHolder_PlayerCard> {
    private static final String TAG = "RecycAdpt_PlayList";
    private List<PlayerDAO_WithLayouts> mPlayers;
    private Context mContext;
    private PlayerListInterface mCallback;
    private boolean expanded;
    private AppConfig appConfig;
    private List<FavouriteLayoutItem> favouriteLayoutItemList;
    private int bottomMargin;

    private FavListOption currentList;

    public RecyclerAdaptor_PlayerList(List<PlayerDAO_WithLayouts> players, List<FavouriteLayoutItem> favouriteLayoutItemList, Context context) {
        mPlayers = players;
        mContext = context;
        mCallback = (PlayerListInterface) context;
        appConfig = new AppConfig(mContext);
        this.favouriteLayoutItemList = favouriteLayoutItemList;
//        this.bottomMargin = (int) mContext.getResources().getDimension(R.dimen.player_card_bottom_margin) / (int) mContext.getResources().getDisplayMetrics().density;
        this.bottomMargin = 32 / (int) mContext.getResources().getDisplayMetrics().density;
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
    public ViewHolder_PlayerCard onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_player_card, parent, false);
        ViewHolder_PlayerCard holder = new ViewHolder_PlayerCard(v);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        holder.favLayoutRecycler.setLayoutManager(mLayoutManager);
        holder.favLayoutRecycler.setItemAnimator(new DefaultItemAnimator());
        holder.favLayoutRecycler.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder_PlayerCard holder, final int position) {
        final PlayerDAO_WithLayouts playerDAO = mPlayers.get(position);


        final RecyclerAdaptor_FavouriteLayoutSimple favouriteLayoutListAdaptor = new RecyclerAdaptor_FavouriteLayoutSimple(favouriteLayoutItemList, 0, playerDAO.getPlayerId(), false, mContext);
        final RecyclerAdaptor_FavouriteLayoutSimple topLayoutListAdaptor = new RecyclerAdaptor_FavouriteLayoutSimple(playerDAO.getTopLayoutItemList(), 1, playerDAO.getPlayerId(), false, mContext);
        final RecyclerAdaptor_FavouriteLayoutSimple lastUsedLayoutListAdaptor = new RecyclerAdaptor_FavouriteLayoutSimple(playerDAO.getLastUsedLayoutItemList(), 2, playerDAO.getPlayerId(), false, mContext);
        final RecyclerAdaptor_FavouriteLayoutSimple mostUsedLayoutListAdaptor = new RecyclerAdaptor_FavouriteLayoutSimple(playerDAO.getMostUsedLayoutItemList(), 3, playerDAO.getPlayerId(), false, mContext);


        holder.playerList_Name.setText(StringUtil.getHtmlForTxtBox(playerDAO.getPlayerName()));
        holder.playerList_PlayerGuild.setText(StringUtil.getHtmlForTxtBox(playerDAO.getPlayerGuild()));

        if (playerDAO.getExpanded() == 1) {
            holder.expandRow.setVisibility(View.VISIBLE);
            holder.hideRow.setVisibility(View.GONE);
        } else {
            holder.hideRow.setVisibility(View.VISIBLE);
            holder.expandRow.setVisibility(View.GONE);
        }
        if (appConfig.bFavPref()) {
            try {
                if (playerDAO.getFavDefault().equalsIgnoreCase(PlayerFavPref.FAV.toString())) {
                    currentList = FavListOption.FAVOURITE;
                } else if (playerDAO.getFavDefault().equalsIgnoreCase(PlayerFavPref.LAST.toString())) {
                    currentList = FavListOption.LASTUSED;
                } else if (playerDAO.getFavDefault().equalsIgnoreCase(PlayerFavPref.MOST.toString())) {
                    currentList = FavListOption.MOSTUSED;
                }
            } catch (Exception e) {
                currentList = FavListOption.FAVOURITE;
            }
        } else {
            currentList = FavListOption.FAVOURITE;
            holder.favLayoutRecycler.setAdapter(favouriteLayoutListAdaptor);
        }


        try {
            holder.playerListFaction.setImageDrawable(ImageHelpers.factionIcon(playerDAO.getPlayerFaction(), mContext));
        } catch (Exception e) {
        }

        if (position + 1 == getItemCount()) {
            setBottomMargin(holder.itemView, (int) (72 * Resources.getSystem().getDisplayMetrics().density));
        } else {
            setBottomMargin(holder.itemView, bottomMargin);
        }

        holder.playerListViewPlayerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.viewPlayer(playerDAO.getPlayerId());

            }
        });
        holder.playerTopClickLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.viewPlayer(playerDAO.getPlayerId());
            }
        });
        holder.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id = playerDAO.getDb_rowId();
                int itemId = menuItem.getItemId();
                if (itemId == R.id.player_card_delete) {
                    mCallback.deletePlayer(id);
                } else if (itemId == R.id.player_view_details) {
                    mCallback.viewPlayerConfig(playerDAO.getPlayerId());
                } else if (itemId == R.id.player_war_room) {
                    mCallback.getWarStatus(playerDAO.getPlayerId());
                }
                return true;
            }
        });

        holder.playerGetPVPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.savePVP(playerDAO.getPlayerId());
            }
        });

        holder.playerGetWARBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.saveWar(playerDAO.getPlayerId());

            }
        });
        final FavouriteLayoutBaseAdaptor favouriteLayoutBaseAdaptor = new FavouriteLayoutBaseAdaptor(mContext);
        holder.favouriteLayoutListTypeSpinner.setAdapter(favouriteLayoutBaseAdaptor);
        for (int i = 0; i < favouriteLayoutBaseAdaptor.getCount(); i++) {
            if (favouriteLayoutBaseAdaptor.getFavouriteTypes()[i].equalsIgnoreCase(playerDAO.getFavDefault())) {
                holder.favouriteLayoutListTypeSpinner.setSelection(i);
                switch (i) {
                    case 0:
                        holder.favLayoutRecycler.setAdapter(favouriteLayoutListAdaptor);
                        favouriteLayoutListAdaptor.notifyDataSetChanged();
                        break;
                    case 1:
                        holder.favLayoutRecycler.setAdapter(topLayoutListAdaptor);
                        topLayoutListAdaptor.notifyDataSetChanged();
                        break;
                    case 2:
                        holder.favLayoutRecycler.setAdapter(lastUsedLayoutListAdaptor);
                        lastUsedLayoutListAdaptor.notifyDataSetChanged();
                        break;
                    case 3:
                        holder.favLayoutRecycler.setAdapter(mostUsedLayoutListAdaptor);
                        mostUsedLayoutListAdaptor.notifyDataSetChanged();
                        break;
                    default:
                }
                break;
            }
        }
//        }

        holder.expandCardImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.expandRow.setVisibility(View.VISIBLE);
                holder.hideRow.setVisibility(View.GONE);
                mCallback.setExpanded(playerDAO.getPlayerId(), 1);
            }
        });
        holder.collapseCardImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.expandRow.setVisibility(View.GONE);
                holder.hideRow.setVisibility(View.VISIBLE);
                mCallback.setExpanded(playerDAO.getPlayerId(), 0);
            }
        });


        holder.favouriteLayoutListTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                List<FavouriteLayoutItem> favouriteLayoutItemList;
                RecyclerAdaptor_FavouriteLayoutSimple recyclerAdaptorFavouriteLayout;
                String s = favouriteLayoutBaseAdaptor.getFavouriteTypes()[holder.favouriteLayoutListTypeSpinner.getSelectedItemPosition()];
                mCallback.setPlayerFavouriteList(playerDAO.getPlayerId(), s);

                switch (i) {
                    case 0:
                        holder.favLayoutRecycler.setAdapter(favouriteLayoutListAdaptor);
                        favouriteLayoutListAdaptor.notifyDataSetChanged();
                        break;
                    case 1:
                        holder.favLayoutRecycler.setAdapter(topLayoutListAdaptor);
                        topLayoutListAdaptor.notifyDataSetChanged();
                        break;
                    case 2:
                        holder.favLayoutRecycler.setAdapter(lastUsedLayoutListAdaptor);
                        lastUsedLayoutListAdaptor.notifyDataSetChanged();
                        break;
                    case 3:
                        holder.favLayoutRecycler.setAdapter(mostUsedLayoutListAdaptor);
                        mostUsedLayoutListAdaptor.notifyDataSetChanged();
                        break;
                    default:
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }



    @Override
    public int getItemCount() {
        return mPlayers.size();
    }


    private enum FavListOption {
        FAVOURITE, MOSTUSED, LASTUSED;
    }
}
