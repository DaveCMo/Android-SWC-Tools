package com.swctools.activity_modules.player.models;

import android.content.Context;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.swctools.config.AppConfig;
import com.swctools.database.DBSQLiteHelper;
import com.swctools.database.DatabaseContracts;
import com.swctools.common.enums.BattleOutcome;
import com.swctools.common.enums.BattleType;
import com.swctools.common.helpers.DateTimeHelper;
import com.swctools.common.models.player_models.ActiveArmoury;
import com.swctools.common.models.player_models.ArmouryEquipment;
import com.swctools.common.models.player_models.Troop;
import com.swctools.util.StringUtil;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.json.JsonObject;
import javax.json.JsonValue;

import static com.swctools.common.enums.ApplicationMessageTemplates.TROOP_FORMAT;

public class Battle implements Parcelable {
    private static final String TAG = "Battle";
    private final String ATTACKED_BY = "%1$s (%2$s) on %3$s";
    private final String ATTACKED_NO_SQUAD = "%1$s on %3$s";
    private final String STARS = "(%1$s stars)";
    private final String PERCENT = "%1$s%%";
    private DecimalFormat formatter = new DecimalFormat("#,###,###");

    private String battleId;
    private String playerId;
    private String planetId;
    private String planetName;
    private int battleDelta;
    private int baseDamagePercent;
    private int stars;
    private long attackDateSec;
    private DateTime attackDate;
    private boolean revenged;
    private boolean newBattle;
    private BattleLogLoot looted;
    private BattleLogLoot maxLootable;
    private BattleLogPlayer attacker;
    private BattleLogPlayer defender;
    private BattleOutcome outcome;
    private BattleType battleType;
    private JsonObject troopsExpended;
    private JsonObject battleLog;
    private JsonObject attackerGuildTroopsExpended;
    private ArrayList<Troop> troopsExpendedList;
    private ArrayList<Troop> guildTroopsExpendedList;
    private ActiveArmoury attackerArmoury;
    private ActiveArmoury defenderArmoury;
    private ArrayList<Troop> numVisitors;
    private HashMap<String, Troop> getMasterTroopList;

    public static final Creator<Battle> CREATOR = new Creator<Battle>() {
        @Override
        public Battle createFromParcel(Parcel in) {
            return new Battle(in);
        }

        @Override
        public Battle[] newArray(int size) {
            return new Battle[size];
        }
    };

    public Battle(Parcel in) {
        battleId = in.readString();
        playerId = in.readString();
        planetId = in.readString();
        planetName = in.readString();
        battleDelta = in.readInt();
        baseDamagePercent = in.readInt();
        stars = in.readInt();
        attackDateSec = in.readLong();
        setAttackDate(attackDateSec);
        revenged = Boolean.parseBoolean(in.readString());
        newBattle = Boolean.parseBoolean(in.readString());
        looted = in.readParcelable(BattleLogLoot.class.getClassLoader());
        maxLootable = in.readParcelable(BattleLogLoot.class.getClassLoader());
        attacker = in.readParcelable(BattleLogPlayer.class.getClassLoader());
        defender = in.readParcelable(BattleLogPlayer.class.getClassLoader());
        String battleString = in.readString();
        troopsExpendedList = in.readArrayList(Troop.class.getClassLoader());
        guildTroopsExpendedList = in.readArrayList(Troop.class.getClassLoader());
        attackerArmoury = in.readParcelable(ActiveArmoury.class.getClassLoader());
        defenderArmoury = in.readParcelable(ActiveArmoury.class.getClassLoader());


        if (battleString.equalsIgnoreCase(BattleType.ATTACK.toString())) {
            battleType = BattleType.ATTACK;
        } else {
            battleType = BattleType.DEFENCE;
        }

//        try {
//            troopsExpended = StringUtil.stringToJsonObject(in.readString());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        try {
//            battleLog = StringUtil.stringToJsonObject(in.readString());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        try {
//            attackerGuildTroopsExpended = StringUtil.stringToJsonObject(in.readString());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(battleId);
        dest.writeString(playerId);
        dest.writeString(planetId);
        dest.writeString(planetName);
        dest.writeInt(battleDelta);
        dest.writeInt(baseDamagePercent);
        dest.writeInt(stars);
        dest.writeLong(attackDateSec);
        dest.writeString(String.valueOf(revenged));
        dest.writeString(String.valueOf(newBattle));
        dest.writeParcelable(looted, Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
        dest.writeParcelable(maxLootable, Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
        dest.writeParcelable(attacker, Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
        dest.writeParcelable(defender, Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
        dest.writeString(battleType.toString());
        dest.writeList(troopsExpendedList);
        dest.writeList(guildTroopsExpendedList);
        dest.writeParcelable(attackerArmoury, Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
        dest.writeParcelable(defenderArmoury, Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
//        dest.writeString(battleLog.toString());

//        dest.writeString(troopsExpended.toString());

//        if (attackerGuildTroopsExpended != null) {
//            if (StringUtil.isStringNotNull(attackerGuildTroopsExpended.toString())) {
//                dest.writeString(attackerGuildTroopsExpended.toString());
//            } else {
//                dest.writeString("");
//            }
//        } else {
//            dest.writeString("");
//        }
    }

    private Context context;

    public Battle(String playerId, JsonObject battleLog, boolean isNew, HashMap<String, Troop> getMasterTroopList, HashMap<String, ArmouryEquipment> getMasterArmourList, Context context) {
        this.getMasterTroopList = getMasterTroopList;
        this.context = context;
        this.playerId = playerId;
        this.battleLog = battleLog;
        this.newBattle = isNew;
        this.battleId = battleLog.getString("battleId");
        setAttackDate(Long.valueOf(battleLog.getInt("attackDate")));
        setBaseDamagePercent(battleLog.getInt("baseDamagePercent"));
        setStars(battleLog.getInt("stars"));
        setRevenged(battleLog.getBoolean("revenged"));
        setPlanetId(battleLog.getString("planetId"));
        setPlanetName(context, planetId);
        looted = new BattleLogLoot(battleLog.getJsonObject("looted"));
        maxLootable = new BattleLogLoot(battleLog.getJsonObject("maxLootable"));
        attacker = new BattleLogPlayer(battleLog.getJsonObject("attacker"));
        defender = new BattleLogPlayer(battleLog.getJsonObject("defender"));

        setBattleType(this.playerId, this.attacker);
        setOutcome(this.battleType, this.stars);
        setBattleDelta(this.battleType);

        troopsExpendedList = new ArrayList<>();
        guildTroopsExpendedList = new ArrayList<>();
        attackerArmoury = new ActiveArmoury(battleLog.getJsonArray("attackerEquipment"), attacker.getFaction(), getMasterArmourList, context);
        defenderArmoury = new ActiveArmoury(battleLog.getJsonArray("defenderEquipment"), defender.getFaction(), getMasterArmourList, context);
        numVisitors = new ArrayList<>();
        setTroopsExpendedv2();
    }


    private void setTroopsExpendedv2() {

        try {
            troopsExpended = this.battleLog.getJsonObject("troopsExpended");
            setTroopsExpendedList(troopsExpended, attacker.getFaction(), context);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            attackerGuildTroopsExpended = battleLog.getJsonObject("attackerGuildTroopsExpended");
            setAttackerGuildTroopsExpended(attackerGuildTroopsExpended, attacker.getFaction(), context);
        } catch (Exception e) {
//            e.printStackTrace();
        }


    }

    private void setTroopsExpendedList(JsonObject troopsExpended, String attackerFaction, Context context) {
        for (Map.Entry<String, JsonValue> value : troopsExpended.entrySet()) {
            int tmpCount = 0;
            tmpCount = Integer.parseInt(value.getValue().toString());
            Troop troop;
            try {
                troop = new Troop(getMasterTroopList.get(value.getKey()), tmpCount);// new Troop(value.getKey(), attackerFaction, tmpCount, context);
            } catch (Exception e) {
                troop = new Troop(value.getKey(), value.getKey(), attackerFaction, 0, 0, tmpCount);
                troop.setNumTroops(tmpCount);
            }
            troopsExpendedList.add(troop);
        }
    }

    public ActiveArmoury getAttackerArmoury() {
        return attackerArmoury;
    }

    public ActiveArmoury getDefenderArmoury() {
        return defenderArmoury;
    }

    private void setAttackerGuildTroopsExpended(JsonObject troopsExpended, String attackerFaction, Context context) {
        for (Map.Entry<String, JsonValue> value : troopsExpended.entrySet()) {
            int tmpCount = 0;
            tmpCount = Integer.parseInt(value.getValue().toString());
            try {
                Troop troop = new Troop(getMasterTroopList.get(value.getKey()), tmpCount);// new Troop(value.getKey(), attackerFaction, tmpCount, context);
                guildTroopsExpendedList.add(troop);
            } catch (Exception e) {
                Troop troop = new Troop(value.getKey(), value.getKey(), attackerFaction, 0, 0);
                troop.setNumTroops(tmpCount);
                guildTroopsExpendedList.add(troop);
            }
        }
    }

    public ArrayList<Troop> getTroopsExpendedList() {
        return troopsExpendedList;
    }

    public ArrayList<Troop> getGuildTroopsExpendedList() {
        return guildTroopsExpendedList;
    }

    public String getGuildToopsList() {
        StringBuilder sb = new StringBuilder("");
        int i = 0;
        for (Troop troop : guildTroopsExpendedList) {
            sb.append(String.format(TROOP_FORMAT.getemplateString(), "x" + troop.getNumTroops(), troop.uiName(), troop.formattedTroopLevelString()));
            if (i <= guildTroopsExpendedList.size()) {
                sb.append("\n");
            }
            i++;
        }
        return sb.toString();
    }

    public String getTroopList() {
        StringBuilder sb = new StringBuilder("");
        int i = 0;
        try {
            for (Troop troop : troopsExpendedList) {
                sb.append(String.format(TROOP_FORMAT.getemplateString(), "x" + troop.getNumTroops(), troop.uiName(), troop.formattedTroopLevelString()));
                if (i <= troopsExpendedList.size()) {
                    sb.append("\n");
                }
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }


    public String getBattleId() {
        return battleId;
    }

    public long getAttackDateSec() {
        return attackDateSec;
    }

    public void setAttackDateSec(long attackDateSec) {
        try {
            this.attackDateSec = attackDateSec;
        } catch (Exception e) {
            this.attackDateSec = 0;
        }
    }

    public boolean isNewBattle() {
        return this.newBattle;
    }

    public void setNewBattle(boolean isNew) {
        this.newBattle = isNew;
    }

    public DateTime getAttackDate() {
        return attackDate;
    }

    public void setAttackDate(Long seconds) {
        this.attackDate = DateTimeHelper.serverUTCDate(seconds);
        setAttackDateSec(seconds);
    }

    public void setAttackDate(DateTime attackDate) {
        this.attackDate = attackDate;
    }

    public int getBattleDelta() {
        return battleDelta;
    }

    public void setBattleDelta(BattleType battleType) {
        if (battleType.equals(BattleType.ATTACK)) {
            this.battleDelta = this.attacker.getAttackRatingDelta();
        } else {
            this.battleDelta = this.defender.getDefenseRatingDelta();
        }
    }

    public void setBattleDelta(int battleDelta) {
        this.battleDelta = battleDelta;
    }

    public int getBaseDamagePercent() {
        return baseDamagePercent;
    }

    public void setBaseDamagePercent(int baseDamagePercent) {
        this.baseDamagePercent = baseDamagePercent;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public boolean isRevenged() {
        return revenged;
    }

    public void setRevenged(boolean revenged) {
        this.revenged = revenged;
    }

    public BattleLogLoot getLooted() {
        return looted;
    }

    public void setLooted(BattleLogLoot looted) {
        this.looted = looted;
    }

    public BattleLogLoot getMaxLootable() {
        return maxLootable;
    }

    public void setMaxLootable(BattleLogLoot maxLootable) {
        this.maxLootable = maxLootable;
    }

    public BattleLogPlayer getAttacker() {
        return attacker;
    }

    public void setAttacker(BattleLogPlayer attacker) {
        this.attacker = attacker;
    }

    public BattleLogPlayer getDefender() {
        return defender;
    }

    public void setDefender(BattleLogPlayer defender) {
        this.defender = defender;
    }

    public BattleOutcome getOutcome() {
        return outcome;
    }

    public void setOutcome(BattleType battleType, int stars) {
        if (battleType == BattleType.DEFENCE) {
            if (stars == 0) {
                this.outcome = BattleOutcome.VICTORY;
            } else {
                this.outcome = BattleOutcome.DEFEAT;
            }
        } else if (battleType == BattleType.ATTACK) {
            if (stars == 0) {
                this.outcome = BattleOutcome.DEFEAT;
            } else {
                this.outcome = BattleOutcome.VICTORY;
            }
        }
        //        if (stars > 0) {
//            this.outcome = BattleOutcome.VICTORY;
//        } else {
//            this.outcome = BattleOutcome.DEFEAT;
//        }
    }

    public void setOutcome(BattleOutcome outcome) {
        this.outcome = outcome;
    }

    public BattleType getBattleType() {
        return battleType;
    }

    public void setBattleType(BattleType battleType) {
        this.battleType = battleType;
    }

    public void setBattleType(String playerId, BattleLogPlayer attacker) {
        if (playerId.equals(attacker.getPlayerId())) {
            this.battleType = BattleType.ATTACK;
        } else {
            this.battleType = BattleType.DEFENCE;
        }
    }

    public String getPlanetId() {
        return planetId;
    }

    public void setPlanetId(String planetId) {
        this.planetId = planetId;
    }

    public String getPlanetName() {
        return planetName;
    }

    public void setPlanetName(Context context, String planetName) {

        Cursor cursor = null;
        String selection = DatabaseContracts.Planets.GAME_NAME + " = ?";
        String[] selectionArgs = {planetName};
        String[] projection = {DatabaseContracts.Planets.UI_NAME};
        try {

            cursor = DBSQLiteHelper.queryDB(DatabaseContracts.Planets.TABLE_NAME, projection, selection, selectionArgs, context);

            while (cursor.moveToNext()) {
                this.planetName = cursor.getString(cursor.getColumnIndex(DatabaseContracts.Planets.UI_NAME));

            }
        } catch (Exception e) {
            this.planetName = "Error parsing planet name!";
        } finally {
            if (cursor != null) {
                cursor.close();
            }

        }

    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getCONFLICTGEARS() {

        if (getBattleType().equals(BattleType.DEFENCE)) {
            return String.valueOf(getDefender().getTournamentRatingDelta());
        } else {
            return String.valueOf(getAttacker().getTournamentRatingDelta());
        }
    }

    public int getConflictGearsInt() {
        if (getBattleType().equals(BattleType.DEFENCE)) {
            return getDefender().getTournamentRatingDelta();
        } else {
            return getAttacker().getTournamentRatingDelta();
        }
    }

    public String getSTARS() {
        return String.format(STARS, getStars());
    }

    public String getPERCENT() {
        return String.format(PERCENT, getBaseDamagePercent());
    }

    public String getRESULT() {

        return getOutcome().toString();
    }

    public String getHTMLAttackedBy() {
        String formatString;
        if (getBattleType().equals(BattleType.ATTACK)) {
            if (!StringUtil.isStringNotNull(getDefender().getGuildName())) {
                formatString = ATTACKED_NO_SQUAD;
            } else {
                formatString = ATTACKED_BY;
            }
            return String.format(formatString, StringUtil.htmlformattedGameName(getDefender().getPlayerName()), StringUtil.htmlformattedGameName(getDefender().getGuildName()), getPlanetName());
        } else {
            if (!StringUtil.isStringNotNull(getAttacker().getGuildName())) {
                formatString = ATTACKED_NO_SQUAD;
            } else {
                formatString = ATTACKED_BY;
            }
            return String.format(formatString, StringUtil.htmlformattedGameName(getAttacker().getPlayerName()), StringUtil.htmlformattedGameName(getAttacker().getGuildName()), getPlanetName());
        }

    }

    public String getAttackedDate(Context context) {
        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss:SSS'Z'");
        DateTime dt = getAttackDate();
        AppConfig appConfig = new AppConfig(context);
        DateTimeFormatter dtfOut = DateTimeFormat.forPattern(appConfig.dateFormat());

        return dtfOut.print(dt);

    }

    public String getMEDALS() {

        return String.valueOf(getBattleDelta());

    }

    public String getCREDITS() {
        try {
            return formatter.format(getLooted().getCredits());
        } catch (Exception e) {
            return "0";
        }
    }

    public String getALLOY() {

        try {
            return formatter.format(getLooted().getMaterial());
        } catch (Exception e) {
            return "0";
        }
    }

    public String getCONTRABAND() {

        try {
            return formatter.format(getLooted().getContraband());
        } catch (Exception e) {
            return "0";
        }
    }
}
