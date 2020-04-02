package com.swctools.activity_modules.player_config;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.swctools.R;
import com.swctools.activity_modules.main.models.PlayerDAO;
import com.swctools.util.StringUtil;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlayerConfigFragment extends Fragment {
    private static final String TAG = "PlayerConfigFragment";
    private TextView txtVw_PlayerName, txtVw_Faction, txtVw_Squad, txtVw_DeviceId, txtVw_PlyId;
    private EditText edTxt_Secret;
    private ImageView imgCopyPlyId, imgViewSecret, img_DeviceIdCopy;
    private PlayerConfigInterface mActivityCallBack;
    private Context mContext;
    private String playerId;

    public PlayerConfigFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player_config, container, false);
        setViewItems(view);
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mActivityCallBack = (PlayerConfigInterface) mContext;
    }

    private void setViewItems(View view) {
        txtVw_PlayerName = view.findViewById(R.id.txtVw_PlayerName);
        txtVw_Faction = view.findViewById(R.id.txtVw_Faction);
        txtVw_Squad = view.findViewById(R.id.txtVw_Squad);
        txtVw_DeviceId = view.findViewById(R.id.txtVw_DeviceId);
        txtVw_PlyId = view.findViewById(R.id.txtVw_PlyId);

        imgCopyPlyId = view.findViewById(R.id.imgCopyPlyId);
        img_DeviceIdCopy = view.findViewById(R.id.img_DeviceIdCopy);
        imgViewSecret = view.findViewById(R.id.imgCopySecret);

        edTxt_Secret = view.findViewById(R.id.edTxt_Secret);

        imgViewSecret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivityCallBack.copyText("SWC - Player Secret", edTxt_Secret.getText().toString());
            }
        });

        img_DeviceIdCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivityCallBack.copyText("SWC - Device Id", txtVw_DeviceId.getText().toString());
            }
        });

        imgCopyPlyId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivityCallBack.copyText("SWC - Player Id", txtVw_PlyId.getText().toString());
            }
        });
    }


    public interface PlayerConfigInterface {
        void copyText(String option, String str);
    }

    public void setViews(String playerId) {
        PlayerDAO playerDAO = new PlayerDAO(playerId, mContext);
        txtVw_PlayerName.setText(StringUtil.getHtmlForTxtBox(playerDAO.getPlayerName()));
        txtVw_Faction.setText(playerDAO.getPlayerFaction());
        txtVw_Squad.setText(playerDAO.getPlayerGuild());
        txtVw_DeviceId.setText(playerDAO.getDeviceId());
        txtVw_PlyId.setText(playerDAO.getPlayerId());
        edTxt_Secret.setText(playerDAO.getPlayerSecret());
    }
}
