package com.swctools.common.base_adaptors;

import android.content.Context;
import android.database.Cursor;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.swctools.R;
import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;
import com.swctools.common.helpers.PlayerHelper;
import com.swctools.util.StringUtil;

import java.util.ArrayList;

public class PlayerListBaseAdaptor extends BaseAdapter {
    private static final String TAG = "PlayerListBAdaptr";

    private LayoutInflater inflter;
    private Context mContext;
    private ArrayList<String> spinnerArray;
    private ArrayList<PlayerHelper> playerList;

    public ArrayList<PlayerHelper> getPlayerList() {
        return playerList;
    }

    public PlayerListBaseAdaptor(Context context) {
        this.mContext = context;

        inflter = (LayoutInflater.from(context));

        String[] columns = {
                DatabaseContracts.PlayersTable.PLAYERNAME,
                DatabaseContracts.PlayersTable.PLAYERID,
                DatabaseContracts.PlayersTable.PLAYERSECRET};
        spinnerArray = new ArrayList<>();
        playerList = new ArrayList<>();
        playerList.add(new PlayerHelper("None", "None", "None"));
        spinnerArray.add("- Select Player -");
        Cursor cursor = DBSQLiteHelper.queryDB(DatabaseContracts.PlayersTable.TABLE_NAME, columns, mContext);
        try {
            while (cursor.moveToNext()) {
                String pId = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.PlayersTable.PLAYERID));
                String pName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.PlayersTable.PLAYERNAME));
                String pSec = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.PlayersTable.PLAYERSECRET));

                PlayerHelper player = new PlayerHelper(pId, pSec, pName);

                spinnerArray.add(pName);
                playerList.add(player);

            }
        } catch (Exception e) {

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

    }

    @Override
    public int getCount() {
        return spinnerArray.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.spinner_text_view, null);
        TextView spinnerText = view.findViewById(R.id.spinner_txt);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            spinnerText.setText(Html.fromHtml(StringUtil.htmlformattedGameName(spinnerArray.get(i)), Html.FROM_HTML_MODE_LEGACY));
        } else {
            spinnerText.setText(Html.fromHtml(StringUtil.htmlformattedGameName(spinnerArray.get(i))));
        }


        return view;
    }
}
