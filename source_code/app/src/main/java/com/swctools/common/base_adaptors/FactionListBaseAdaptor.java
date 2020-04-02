package com.swctools.common.base_adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.swctools.R;
import com.swctools.common.helpers.ImageHelpers;

public class FactionListBaseAdaptor extends BaseAdapter {
    private static final String TAG = "PlayerListBAdaptr";
    private String[] factions;
    private LayoutInflater inflter;
    private Context mContext;


    public String[] getFactions(){
        return factions;
    }
    public FactionListBaseAdaptor(Context context) {
        this.mContext = context;
        factions = context.getResources().getStringArray(R.array.factions);
        inflter = (LayoutInflater.from(context));

    }

    @Override
    public int getCount() {
        return factions.length;
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
        view = inflter.inflate(R.layout.spinner_image_text, null);
        TextView spinner_text = view.findViewById(R.id.spinner_text);
        spinner_text.setText(factions[i]);
        ImageView spinner_img = view.findViewById(R.id.spinner_img);
        spinner_img.setImageDrawable(ImageHelpers.factionIcon(factions[i], mContext));
        return view;
    }
}
