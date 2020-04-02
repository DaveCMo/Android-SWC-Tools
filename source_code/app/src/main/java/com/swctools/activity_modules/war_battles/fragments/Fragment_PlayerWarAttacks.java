package com.swctools.activity_modules.war_battles.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swctools.R;
import com.swctools.activity_modules.war_battles.models.WarBattleListProvider;
import com.swctools.activity_modules.war_battles.models.War_BattleHeader;
import com.swctools.activity_modules.war_battles.view_adaptors.RecyclerAdaptor_WarBattle;
import com.swctools.common.enums.BundleKeys;
import com.swctools.common.enums.ScreenCommands.DeployableTypes;
import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Fragment_PlayerWarAttacks extends Fragment {
    private static final String TAG = "Fragment_PlayerWarAttac";
    private String playerId;
    private String warId;
    private String guildId;
    private RecyclerView warattackRecycler;
    private ArrayList<Object> itemList;

    public Fragment_PlayerWarAttacks() {
    }

    private Fragment_PlayerWarAttacks(String playerId, String warId, String guildId) {
        this.playerId = playerId;
        this.warId = warId;
        this.guildId = guildId;
    }

    public static Fragment_PlayerWarAttacks newInstance(String playerId, String warId, String guildId) {
        Log.d(TAG, "newInstance: ");
        return new Fragment_PlayerWarAttacks(playerId, warId, guildId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");

        if (savedInstanceState != null) {
            warId = savedInstanceState.getString(BundleKeys.WAR_ID.toString());
            playerId = savedInstanceState.getString(BundleKeys.PLAYER_ID.toString());
            guildId = savedInstanceState.getString(BundleKeys.GUILD_ID.toString());
        }


        View view = inflater.inflate(R.layout.fragment_war_attack, container, false);
        warattackRecycler = view.findViewById(R.id.warattackRecycler);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        warattackRecycler.setLayoutManager(mLayoutManager);
        itemList = getItemList();

        RecyclerAdaptor_WarBattle recyclerAdaptor_warBattle = new RecyclerAdaptor_WarBattle(itemList, getContext());
        warattackRecycler.setAdapter(recyclerAdaptor_warBattle);
        return view;
    }

    private ArrayList<Object> getItemList() {
        ArrayList<Object> objects = new ArrayList<>();

        ArrayList<War_BattleHeader> war_battleHeaders = new ArrayList<>();
        String whereClause = DatabaseContracts.WarDefense.PLAYERID + " = ? AND " + DatabaseContracts.WarDefense.WARID + " = ?";
        String[] whereArgs = {playerId, warId};
        Cursor cursor = DBSQLiteHelper.queryDB(DatabaseContracts.WarDefense.TABLE_NAME, null, whereClause, whereArgs, null, null, DatabaseContracts.WarDefense.DEFENSE_END + " DESC", null, getContext());
        while (cursor.moveToNext()) {
            String battleId = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.WarDefense.BATTLEID));
            String attackedBy = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.WarDefense.OPPONENT_NAME));
            int starsLeft = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.WarDefense.VICTORY_POINTS));
            int dateTime = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.WarDefense.DEFENSE_END));
            String withOps = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.WarDefense.ATTACKERWARBUFFS));
            String againstOps = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.WarDefense.DEFENDERWARBUFFS));
            War_BattleHeader war_battleHeader = new War_BattleHeader(battleId, attackedBy, starsLeft, dateTime, withOps, againstOps);
            objects.add(war_battleHeader);
            objects.add("Heroes");
            objects.addAll(WarBattleListProvider.getWarBattleDeployed(warId, battleId, DeployableTypes.HERO, getContext()));
            objects.add("Troops");
            objects.addAll(WarBattleListProvider.getWarBattleDeployed(warId, battleId, DeployableTypes.TROOP, getContext()));
            objects.add("Air");
            objects.addAll(WarBattleListProvider.getWarBattleDeployed(warId, battleId, DeployableTypes.AIR, getContext()));
            objects.add("Droidekas");
            objects.addAll(WarBattleListProvider.getWarBattleDeployed(warId, battleId, DeployableTypes.DROIDEKA, getContext()));
        }

        return objects;

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(BundleKeys.PLAYER_ID.toString(), playerId);
        outState.putString(BundleKeys.WAR_ID.toString(), warId);
        outState.putString(BundleKeys.GUILD_ID.toString(), guildId);
    }
}
