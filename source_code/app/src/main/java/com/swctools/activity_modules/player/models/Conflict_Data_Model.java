package com.swctools.activity_modules.player.models;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.swctools.common.enums.ApplicationMessageTemplates;
import com.swctools.common.helpers.DateTimeHelper;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Conflict_Data_Model implements Parcelable {
    private static final String TAG = "Conflict_Data_Model";
    private BigDecimal conflictPerc;
    private int conflictRank;
    private int conflictGears;
    private int attacksWon;
    private int attacksLost;
    private int defensesWon;
    private int defensesLost;
    private String planetName;
    private String startDateRaw;
    private String endDateRaw;
    private ArrayList<BigDecimal> conflictLeagues;
    private SimpleDateFormat format = new SimpleDateFormat("HH:mm,dd-MM-yyyy");

    private ArrayList<TwoTextItemData> twoTextItemData;//used to hold all the data in the expanded section

    public String getConflictPerc() {
        BigDecimal hund = new BigDecimal(100);
        BigDecimal actualPerc = hund.subtract(conflictPerc);


        return actualPerc.round(new MathContext(4, RoundingMode.HALF_UP)) + "%";
    }

    private void setConflictLeagues() {
        conflictLeagues = new ArrayList<>();
        conflictLeagues.add(new BigDecimal(2));
        conflictLeagues.add(new BigDecimal(5));
        conflictLeagues.add(new BigDecimal(10));
        conflictLeagues.add(new BigDecimal(20));
        conflictLeagues.add(new BigDecimal(30));
        conflictLeagues.add(new BigDecimal(50));
        conflictLeagues.add(new BigDecimal(80));
    }

    public void setTwoTextItems(Context mContext) {
        MathContext mc = new MathContext(2, RoundingMode.HALF_UP);
        twoTextItemData = new ArrayList<>();
        setConflictLeagues();
        twoTextItemData.add(new TwoTextItemData("Start Date", getStartDateRaw(mContext)));
        try {
            twoTextItemData.add(new TwoTextItemData("End Date", getEndDate(mContext)));
            twoTextItemData.add(new TwoTextItemData("Conflict Points", String.valueOf(getConflictGears())));
            twoTextItemData.add(new TwoTextItemData("Rank", String.valueOf(getConflictRank())));
            twoTextItemData.add(new TwoTextItemData("Attacks Won / Lost", String.format(ApplicationMessageTemplates.PROGRESS_BAR_LABEL.getemplateString(),
                    String.valueOf(getAttacksWon()),
                    String.valueOf(getAttacksLost()))));
            twoTextItemData.add(new TwoTextItemData("Defences Won / Lost", String.format(ApplicationMessageTemplates.PROGRESS_BAR_LABEL.getemplateString(),
                    getDefensesWon(), getDefensesLost())));
            BigDecimal hund = new BigDecimal(100);
            BigDecimal actualPerc = hund.subtract(conflictPerc).divide(hund);

            BigDecimal rank = new BigDecimal(conflictRank);
            BigDecimal totalPool = rank.divide(actualPerc, 0, RoundingMode.HALF_UP);

            for (int i = 0; i < conflictLeagues.size(); i++) {
                BigDecimal leagueRank = totalPool.multiply(conflictLeagues.get(i).divide(hund));
//                Integer lR = Integer.parseInt(leagueRank.toString());
                twoTextItemData.add(new TwoTextItemData("Top " + conflictLeagues.get(i).toPlainString() + "% Rank", String.valueOf(leagueRank.intValue())));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public int getConflictRank() {
        return conflictRank;
    }

    public int getConflictGears() {
        return conflictGears;
    }

    public int getAttacksWon() {
        return attacksWon;
    }

    public int getAttacksLost() {
        return attacksLost;
    }

    public int getDefensesWon() {
        return defensesWon;
    }

    public int getDefensesLost() {
        return defensesLost;
    }

    public String getPlanetName() {
        return planetName;
    }

    public String getStartDateRaw(Context cq) {
        try {
            Date date = format.parse(this.startDateRaw);
            long t = (date.getTime()) / 1000;
            return DateTimeHelper.longDateTime(t, cq.getApplicationContext());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return startDateRaw;
    }

    public String getEndDate(Context cq) {
        try {
            Date date = format.parse(this.endDateRaw);
            long t = (date.getTime()) / 1000;
            return DateTimeHelper.longDateTime(t, cq.getApplicationContext());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return endDateRaw;
    }


    public Conflict_Data_Model(BigDecimal conflictPerc, int conflictRank, int conflictGears,
                               int attacksWon, int attacksLost, int defensesWon, int defensesLost,
                               String planetName, String startDateRaw, String endDateRaw) {
        this.conflictPerc = conflictPerc;
        this.conflictRank = conflictRank;
        this.conflictGears = conflictGears;
        this.attacksWon = attacksWon;
        this.attacksLost = attacksLost;
        this.defensesWon = defensesWon;
        this.defensesLost = defensesLost;
        this.planetName = planetName;
        this.startDateRaw = startDateRaw;
        this.endDateRaw = endDateRaw;

    }

    public static final Creator<Conflict_Data_Model> CREATOR = new Creator<Conflict_Data_Model>() {
        @Override
        public Conflict_Data_Model createFromParcel(Parcel in) {
            return new Conflict_Data_Model(in);
        }

        @Override
        public Conflict_Data_Model[] newArray(int size) {
            return new Conflict_Data_Model[size];
        }
    };

    public Conflict_Data_Model(Parcel in) {
        this.conflictPerc = new BigDecimal(in.readString());
        this.conflictRank = in.readInt();
        this.conflictGears = in.readInt();
        this.attacksWon = in.readInt();
        this.attacksLost = in.readInt();
        this.defensesWon = in.readInt();
        this.defensesLost = in.readInt();
        this.planetName = in.readString();
        this.startDateRaw = in.readString();
        this.endDateRaw = in.readString();


    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(conflictPerc.toString());
        dest.writeInt(conflictRank);
        dest.writeInt(conflictGears);
        dest.writeInt(attacksWon);
        dest.writeInt(attacksLost);
        dest.writeInt(defensesWon);
        dest.writeInt(defensesLost);
        dest.writeString(planetName);
        dest.writeString(startDateRaw);
        dest.writeString(endDateRaw);

    }

    public ArrayList<TwoTextItemData> getTwoTextItemData() {
        return twoTextItemData;
    }

}
