package com.swctools.activity_modules.player.models;

import java.text.DecimalFormat;

public class Conflict_Data_Value {
    private static final int FORMAT_PERCENT = 0;
    private static final int FORMAT_THOUSAND_SEPERATOR = 1;
    private int dataValue;

    public Conflict_Data_Value(int val) {
        this.dataValue = val;
    }

    public String getDataTxt(int formatOption) {
        switch (formatOption) {
            case FORMAT_PERCENT:
                return String.format("%1$s%", dataValue);
            case FORMAT_THOUSAND_SEPERATOR:
                DecimalFormat formatter = new DecimalFormat("#,###,###");
                return formatter.format(dataValue);
            default:
                return String.valueOf(dataValue);
        }
    }
}
