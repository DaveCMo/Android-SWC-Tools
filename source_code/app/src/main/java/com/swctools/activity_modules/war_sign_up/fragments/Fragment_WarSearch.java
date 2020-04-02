package com.swctools.activity_modules.war_sign_up.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.swctools.R;
import com.swctools.activity_modules.war_sign_up.models.GuildSearchResultItem;
import com.swctools.util.StringUtil;
import com.swctools.util.Utils;
import com.swctools.activity_modules.war_sign_up.view_adaptors.RecyclerAdaptor_GuildSearchResult;
import com.swctools.activity_modules.war_sign_up.interfaces.FragmentWarSearchInterface;

import java.util.ArrayList;

import javax.json.JsonArray;
import javax.json.JsonObject;

public class Fragment_WarSearch extends Fragment {
    private static final String TAG = "Fragment_WarSearch";
    private static final String GUILDSEARCHRESULT = "GUILDSEARCHRESULT";
    private Button war_search_bn, clearBtn;
    private EditText warSearchTerm;
    private RecyclerView guild_results_recycler;
    private RecyclerAdaptor_GuildSearchResult mAdaptor;
    private Context mContext;
    private FragmentWarSearchInterface mCallBack;
    private String guildSearchJsonArray;
    private ArrayList<GuildSearchResultItem> guildSearchResultItems;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallBack = (FragmentWarSearchInterface) context;
        this.mContext = context;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(GUILDSEARCHRESULT, guildSearchJsonArray);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // Clear reference to host Activity.
        mCallBack = null;
        mContext = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_war_signup_search, container, false);
        war_search_bn = (Button) view.findViewById(R.id.war_search_bn);
        clearBtn = view.findViewById(R.id.clearBtn);
        guildSearchResultItems = new ArrayList<>();
        warSearchTerm = (EditText) view.findViewById(R.id.warSearchTerm);
        guild_results_recycler = (RecyclerView) view.findViewById(R.id.guild_results_recycler);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        guild_results_recycler.setLayoutManager(mLayoutManager);
        guild_results_recycler.setItemAnimator(new DefaultItemAnimator());
        guild_results_recycler.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));
        war_search_bn.setOnClickListener(new WarSearchClicked());
        if (savedInstanceState != null) {
            guildSearchJsonArray = savedInstanceState.getString(GUILDSEARCHRESULT);
            if (StringUtil.isStringNotNull(guildSearchJsonArray)) {
                setGuild_results_recycler(guildSearchJsonArray);
            }
        }
        clearBtn.setOnClickListener(new WarSearchReset());
        return view;
    }

    public void setGuild_results_recycler(String jsonArrayStr) {
        guildSearchJsonArray = jsonArrayStr;
        JsonArray guildSearchResultArray = null;
        guildSearchResultItems.clear();
        try {
            guildSearchResultArray = StringUtil.stringToJsonArray(jsonArrayStr);

            for (int i = 0; i < guildSearchResultArray.size(); i++) {
                JsonObject jsonObject1 = guildSearchResultArray.getJsonObject(i);
                GuildSearchResultItem guildSearchResultItem = new GuildSearchResultItem(jsonObject1);
                guildSearchResultItems.add(guildSearchResultItem);
            }
            mAdaptor = new RecyclerAdaptor_GuildSearchResult(guildSearchResultItems, mContext);

            guild_results_recycler.setAdapter(mAdaptor);
            mAdaptor.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    class WarSearchReset implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            warSearchTerm.setText("");
            if(guildSearchResultItems !=null){

                guildSearchResultItems.clear();
            }
            mCallBack.resetSelectedGuild();
//            mAdaptor = new RecyclerAdaptor_GuildSearchResult(null, mContext);
//            guild_results_recycler.setAdapter(mAdaptor);
            if (mAdaptor != null) {
                mAdaptor.notifyDataSetChanged();
            }
        }
    }

    class WarSearchClicked implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String valueSearched = warSearchTerm.getText().toString();
            mCallBack.searchSquad(valueSearched);
            warSearchTerm.clearFocus();
            war_search_bn.requestFocus();
            Utils.hideKeyboard(getActivity());
        }
    }

}
