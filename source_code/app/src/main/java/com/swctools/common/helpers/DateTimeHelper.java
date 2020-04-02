package com.swctools.common.helpers;

import android.app.Application;
import android.content.Context;

import com.swctools.config.AppConfig;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class DateTimeHelper extends Application {
    private static String TAG = "DateTimeHelper";
    private static String DATE_TIME_FORMAT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss:SSS'Z'";
    private static String TIME_FORMAT_PATTERN = "HH:mm";

    public static long userIDTimeStamp() {
//		return  Math.round(new DateTime().getMillisOfSecond());
        return new DateTime().getMillis();
    }

    public static long swc_requestTimeStamp() {
        return userIDTimeStamp() / 1000;
    }

    public static DateTime serverUTCDate(long seconds) {

        return new DateTime((seconds * 1000));
    }

    public static long getTZOffsetHRs() {
        Calendar mCalendar = new GregorianCalendar();
        TimeZone mTimeZone = mCalendar.getTimeZone();
        int mGMTOffset = mTimeZone.getRawOffset();
        return TimeUnit.HOURS.convert(mGMTOffset, TimeUnit.MILLISECONDS);
    }

    public static String shortDateTime(long seconds, Context context) {
        AppConfig appConfig = new AppConfig(context);

        DateTime dt = serverUTCDate(seconds);
        DateTimeFormatter dtf = DateTimeFormat.forPattern(DATE_TIME_FORMAT_PATTERN);
        DateTimeFormatter dtfOut = DateTimeFormat.forPattern(appConfig.shortDateFormat());

        return dtfOut.print(dt);
    }

    public static String timeFromEPOCH(long seconds, Context context) {
        AppConfig appConfig = new AppConfig(context);

        DateTime dt = serverUTCDate(seconds);
        DateTimeFormatter dtfOut = DateTimeFormat.forPattern(TIME_FORMAT_PATTERN);

        return dtfOut.print(dt);
    }

    public static long calculateTimeOut(long seconds, Context context) {
        AppConfig appConfig = new AppConfig(context);
        DateTime loginDT = serverUTCDate(seconds);
        DateTime timeOut = loginDT.plusMinutes(appConfig.lServerTimeOut());
        return timeOut.getMillis() / 1000;

    }

    public static String getTimeFromNow(int hrs, int mins, String tzId, Context context) {
        AppConfig appConfig = new AppConfig(context);
        DateTime dateTime = new DateTime();
        int minToAdd = (hrs * 60) + mins;
        DateTime resultDate = dateTime.plusMinutes(minToAdd);
        DateTimeZone returnZone = DateTimeZone.forID(tzId);
        DateTimeFormatter dtfOut = DateTimeFormat.forPattern(TIME_FORMAT_PATTERN);
        return dtfOut.print(resultDate.withZone(returnZone));
    }


    public static String longDateTimeByTZ(long seconds, String tzId, Context context) {
        AppConfig appConfig = new AppConfig(context);

        DateTime dt = serverUTCDate(seconds);
        DateTimeZone returnZone = DateTimeZone.forID(tzId);
        DateTimeFormatter dtfOut = DateTimeFormat.forPattern(appConfig.dateFormat());
        return dtfOut.print(dt.withZone(returnZone));
    }

    public static String longDateTime(long seconds, Context context) {
        AppConfig appConfig = new AppConfig(context);

        DateTime dt = serverUTCDate(seconds);
//		DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss:SSS'Z'");
        DateTimeFormatter dtfOut = DateTimeFormat.forPattern(appConfig.dateFormat());

        return dtfOut.print(dt);
    }
}