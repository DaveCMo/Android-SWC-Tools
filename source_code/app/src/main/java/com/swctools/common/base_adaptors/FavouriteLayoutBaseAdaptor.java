package com.swctools.common.base_adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.swctools.R;

public class FavouriteLayoutBaseAdaptor extends BaseAdapter {
    private static final String TAG = "FavLayoutBAdaptr";
    private String[] favouriteTypes;
    private LayoutInflater inflter;


    public String[] getFavouriteTypes() {
        return favouriteTypes;
    }

    public FavouriteLayoutBaseAdaptor(Context context) {

        favouriteTypes = context.getResources().getStringArray(R.array.layout_favourite_types);
        inflter = (LayoutInflater.from(context));

    }

    @Override
    public int getCount() {
        return favouriteTypes.length;
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
        TextView spinner_text = view.findViewById(R.id.spinner_txt);
        spinner_text.setText(favouriteTypes[i]);
        return view;
    }
}
