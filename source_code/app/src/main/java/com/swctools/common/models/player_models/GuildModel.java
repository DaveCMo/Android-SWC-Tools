package com.swctools.common.models.player_models;

import android.os.Parcel;
import android.os.Parcelable;

import com.swctools.common.helpers.DateTimeHelper;

import org.joda.time.DateTime;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;


public class GuildModel implements Parcelable {
    private int mData;
	private String guildId;
	private String guildName;
	private String icon;
	private DateTime joinDate;
	private boolean playerHasOutstandingJoinRequest;
	
	public GuildModel(String jsonGuildModel) {
		//should accept json formatted guild data
		JsonReader reader = Json.createReader(new StringReader(jsonGuildModel));

		JsonObject jsonObject = reader.readObject();
		try {
			setGuildId(jsonObject.getString("guildId"));
		} catch (Exception e) {
			setGuildId("");
		}

		try {
			setGuildName(jsonObject.getString("guildName"));
		} catch (Exception e) {
			setGuildName("");
		}

		try {
			setIcon(jsonObject.getString("icon"));
		} catch (Exception e) {
			setIcon("");

		}
		try {
			setJoinDate(Long.parseLong(jsonObject.get("joinDate").toString()));
		} catch (Exception e) {
			setJoinDate(0);
		}

	}

	public static final Creator<GuildModel> CREATOR = new Creator<GuildModel>() {
        @Override
        public GuildModel createFromParcel(Parcel in) {
            return new GuildModel(in);
        }

        @Override
        public GuildModel[] newArray(int size) {
            return new GuildModel[size];
        }
    };

	public GuildModel(Parcel in){
        mData = in.readInt();
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(guildId);
        dest.writeString(guildName);
        dest.writeString(icon);
    }


    public int describeContents() {
        return 0;
    }
	public String getGuildId() {
		return guildId;
	}

	public void setGuildId(String guildId) {
		this.guildId = guildId;
	}

	public String getGuildName() {
		return guildName;
	}

	public void setGuildName(String guildName) {
		try {
			this.guildName = URLDecoder.decode(guildName, "UTF8");
		} catch (UnsupportedEncodingException e) {
			this.guildName = guildName;
		}

	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public DateTime getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(long serverTime) {
		this.joinDate = DateTimeHelper.serverUTCDate(serverTime);
	}



}
