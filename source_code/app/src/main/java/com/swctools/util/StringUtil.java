package com.swctools.util;

import android.text.Html;
import android.text.Spanned;
import android.text.format.Formatter;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;

//import static junit.framework.Assert.assertTrue;

public class StringUtil {
    private final static String TAG = "StringUtil";
    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
    private final static String uuidPattern = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}";
    private final static TreeMap<Integer, String> romanNumeralMap = new TreeMap<Integer, String>();
//    //		a50c7b93-0756-4b03-9ecc-6d3ff911c72c
//    String uuidPattern = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}";
//    String gId = "5e4b4a3c-5a0b-11e4-a93f-0663e4004f92";
//		System.out.println(gId.matches(uuidPattern));
//		System.out.println(UUID.randomUUID().toString());
//    ArrayList<Integer> list = new ArrayList<>();
//		list.add(1);
//		System.out.println(list.size());


    static {

        romanNumeralMap.put(1000, "M");
        romanNumeralMap.put(900, "CM");
        romanNumeralMap.put(500, "D");
        romanNumeralMap.put(400, "CD");
        romanNumeralMap.put(100, "C");
        romanNumeralMap.put(90, "XC");
        romanNumeralMap.put(50, "L");
        romanNumeralMap.put(40, "XL");
        romanNumeralMap.put(10, "X");
        romanNumeralMap.put(9, "IX");
        romanNumeralMap.put(5, "V");
        romanNumeralMap.put(4, "IV");
        romanNumeralMap.put(1, "I");

    }

    public static Spanned getHtmlForTxtBox(String s) {
        //Correct to something
        if (!StringUtil.isStringNotNull(s)) {
            s = "";
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return Html.fromHtml(StringUtil.htmlformattedGameName(s), Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(StringUtil.htmlformattedGameName(s));

        }
    }

    public static boolean stringInStrArray(String[] array, String compare) {
        for (String s : array) {
            if (s.equalsIgnoreCase(compare)) {
                return true;
            }
        }
        return false;
    }

    public static String getDeviceIPHostAddress() {

        return "";
    }

    public static String getDeviceIP() {

        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String ip = Formatter.formatIpAddress(inetAddress.hashCode());
                        Log.i(TAG, "***** IP=" + ip);
                        return ip;
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e(TAG, ex.toString());
        }
        return null;

    }

    public static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }

    public static boolean stringIsUUID(String var) {
        return var.matches(uuidPattern);
    }

    public static JsonObject stringToJsonObject(String inputString) throws Exception {
        return Json.createReader(new StringReader(inputString)).readObject();
    }

    public static JsonArray stringToJsonArray(String inputString) throws Exception {
        return Json.createReader(new StringReader(inputString)).readArray();
    }

    public static LinkedHashMap<String, String> getTZIds() {
        HashMap<String, TimeZone> tzMap = new HashMap<String, TimeZone>();
        String[] ids = TimeZone.getAvailableIDs();
        ArrayList<TimeZone> timeZones = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {

            TimeZone d = TimeZone.getTimeZone(ids[i]);
            if (StringUtil.isStringNotNull(tzDisplayToIds().get(d.getID()))) {
                timeZones.add(d);
                if (!ids[i].matches(".*/.*")) {
                    continue;
                }
                String region = d.getID().replaceAll(".*/", "").replaceAll("_", " ");
                int hours = Math.abs(d.getRawOffset()) / 3600000;
                int minutes = Math.abs(d.getRawOffset() / 60000) % 60;
                String sign = d.getRawOffset() >= 0 ? "+" : "-";
                String timeZonePretty = String.format("(UTC %s %02d:%02d) %s", sign, hours, minutes, region);
                tzMap.put(timeZonePretty, d);

            }
        }

        List<Map.Entry<String, TimeZone>> entries = new ArrayList<>(tzMap.entrySet());
        Collections.sort(entries, new Comparator<Map.Entry<String, TimeZone>>() {
            @Override
            public int compare(Map.Entry<String, TimeZone> tz1, Map.Entry<String, TimeZone> tz2) {
                Integer offset1 = tz1.getValue().getRawOffset();
                Integer offset2 = tz2.getValue().getRawOffset();
                return offset1.compareTo(offset2);
            }
        });

        LinkedHashMap<String, String> sortedtZMap = new LinkedHashMap<>();
        for (Map.Entry<String, TimeZone> entry : entries) {
            TimeZone d = entry.getValue();
            String region = d.getID().replaceAll(".*/", "").replaceAll("_", " ");
            int hours = Math.abs(d.getRawOffset()) / 3600000;
            int minutes = Math.abs(d.getRawOffset() / 60000) % 60;
            String sign = d.getRawOffset() >= 0 ? "+" : "-";
            String timeZonePretty = String.format("(UTC %s %02d:%02d) %s", sign, hours, minutes, region);
            sortedtZMap.put(timeZonePretty, entry.getValue().getID());
        }
        return sortedtZMap;
    }

    private static HashMap<String, String> tzDisplayToIds() {
        HashMap<String, String> tzDisplayToIds = new HashMap<>();
        tzDisplayToIds.put("Pacific/Majuro", "Marshall Islands");
        tzDisplayToIds.put("Pacific/Midway", "Midway Island");
        tzDisplayToIds.put("Pacific/Honolulu", "Hawaii");
        tzDisplayToIds.put("America/Anchorage", "Alaska");
        tzDisplayToIds.put("America/Los_Angeles", "Pacific Time");
        tzDisplayToIds.put("America/Tijuana", "Tijuana");
        tzDisplayToIds.put("America/Phoenix", "Arizona");
        tzDisplayToIds.put("America/Chihuahua", "Chihuahua");
        tzDisplayToIds.put("America/Denver", "Mountain Time");
        tzDisplayToIds.put("America/Costa_Rica", "Central America");
        tzDisplayToIds.put("America/Chicago", "Central Time");
        tzDisplayToIds.put("America/Mexico_City", "Mexico City");
        tzDisplayToIds.put("America/Regina", "Saskatchewan");
        tzDisplayToIds.put("America/Bogota", "Bogota");
        tzDisplayToIds.put("America/New_York", "Eastern Time");
        tzDisplayToIds.put("America/Caracas", "Venezuela");
        tzDisplayToIds.put("America/Barbados", "Atlantic Time (Barbados)");
        tzDisplayToIds.put("America/Halifax", "Atlantic Time (Canada)");
        tzDisplayToIds.put("America/Manaus", "Manaus");
        tzDisplayToIds.put("America/Santiago", "Santiago");
        tzDisplayToIds.put("America/St_Johns", "Newfoundland");
        tzDisplayToIds.put("America/Sao_Paulo", "Brasilia");
        tzDisplayToIds.put("America/Argentina/Buenos_Aires", "Buenos Aires");
        tzDisplayToIds.put("America/Godthab", "Greenland");
        tzDisplayToIds.put("America/Montevideo", "Montevideo");
        tzDisplayToIds.put("Atlantic/South_Georgia", "Mid-Atlantic");
        tzDisplayToIds.put("Atlantic/Azores", "Azores");
        tzDisplayToIds.put("Atlantic/Cape_Verde", "Cape Verde Islands");
        tzDisplayToIds.put("Africa/Casablanca", "Casablanca");
        tzDisplayToIds.put("Europe/London", "London, Dublin");
        tzDisplayToIds.put("Europe/Amsterdam", "Amsterdam, Berlin");
        tzDisplayToIds.put("Europe/Belgrade", "Belgrade");
        tzDisplayToIds.put("Europe/Brussels", "Brussels");
        tzDisplayToIds.put("Europe/Sarajevo", "Sarajevo");
        tzDisplayToIds.put("Africa/Windhoek", "Windhoek");
        tzDisplayToIds.put("Africa/Brazzaville", "W. Africa Time");
        tzDisplayToIds.put("Asia/Amman", "Amman, Jordan");
        tzDisplayToIds.put("Europe/Athens", "Athens, Istanbul");
        tzDisplayToIds.put("Asia/Beirut", "Beirut, Lebanon");
        tzDisplayToIds.put("Africa/Cairo", "Cairo");
        tzDisplayToIds.put("Europe/Helsinki", "Helsinki");
        tzDisplayToIds.put("Asia/Jerusalem", "Jerusalem");
        tzDisplayToIds.put("Europe/Minsk", "Minsk");
        tzDisplayToIds.put("Africa/Harare", "Harare");
        tzDisplayToIds.put("Asia/Baghdad", "Baghdad");
        tzDisplayToIds.put("Europe/Moscow", "Moscow");
        tzDisplayToIds.put("Asia/Kuwait", "Kuwait");
        tzDisplayToIds.put("Africa/Nairobi", "Nairobi");
        tzDisplayToIds.put("Asia/Tehran", "Tehran");
        tzDisplayToIds.put("Asia/Baku", "Baku");
        tzDisplayToIds.put("Asia/Tbilisi", "Tbilisi");
        tzDisplayToIds.put("Asia/Yerevan", "Yerevan");
        tzDisplayToIds.put("Asia/Dubai", "Dubai");
        tzDisplayToIds.put("Asia/Kabul", "Kabul");
        tzDisplayToIds.put("Asia/Karachi", "Islamabad, Karachi");
        tzDisplayToIds.put("Asia/Oral", "Ural'sk");
        tzDisplayToIds.put("Asia/Yekaterinburg", "Yekaterinburg");
        tzDisplayToIds.put("Asia/Calcutta", "Kolkata");
        tzDisplayToIds.put("Asia/Colombo", "Sri Lanka");
        tzDisplayToIds.put("Asia/Katmandu", "Kathmandu");
        tzDisplayToIds.put("Asia/Almaty", "Astana");
        tzDisplayToIds.put("Asia/Rangoon", "Yangon");
        tzDisplayToIds.put("Asia/Krasnoyarsk", "Krasnoyarsk");
        tzDisplayToIds.put("Asia/Bangkok", "Bangkok");
        tzDisplayToIds.put("Asia/Jakarta", "Jakarta");
        tzDisplayToIds.put("Asia/Shanghai", "Beijing");
        tzDisplayToIds.put("Asia/Hong_Kong", "Hong Kong");
        tzDisplayToIds.put("Asia/Irkutsk", "Irkutsk");
        tzDisplayToIds.put("Asia/Kuala_Lumpur", "Kuala Lumpur");
        tzDisplayToIds.put("Australia/Perth", "Perth");
        tzDisplayToIds.put("Asia/Taipei", "Taipei");
        tzDisplayToIds.put("Asia/Seoul", "Seoul");
        tzDisplayToIds.put("Asia/Tokyo", "Tokyo, Osaka");
        tzDisplayToIds.put("Asia/Yakutsk", "Yakutsk");
        tzDisplayToIds.put("Australia/Adelaide", "Adelaide");
        tzDisplayToIds.put("Australia/Darwin", "Darwin");
        tzDisplayToIds.put("Australia/Brisbane", "Brisbane");
        tzDisplayToIds.put("Australia/Hobart", "Hobart");
        tzDisplayToIds.put("Australia/Sydney", "Sydney, Canberra");
        tzDisplayToIds.put("Asia/Vladivostok", "Vladivostok");
        tzDisplayToIds.put("Pacific/Guam", "Guam");
        tzDisplayToIds.put("Asia/Magadan", "Magadan");
        tzDisplayToIds.put("Pacific/Auckland", "Auckland");
        tzDisplayToIds.put("Pacific/Fiji", "Fiji");
        tzDisplayToIds.put("Pacific/Tongatapu", "Tonga");

        return tzDisplayToIds;
    }

    public final static String toRoman(int number) {
        try {
            int l = romanNumeralMap.floorKey(number);
            if (number == l) {
                return romanNumeralMap.get(number);
            }
            return romanNumeralMap.get(l) + toRoman(number - l);
        } catch (Exception e) {
            return "";
        }
    }

    public static String htmlformattedGameName(String rawString) {
        try {
            String DOUBLE_QUOTES = "\"";
            String SINGLE_QUOTE = "\'";
            String FWDSLASH = "/";
            String spanStartStr = "<span style=" + DOUBLE_QUOTES + "color: #$1" + DOUBLE_QUOTES + ">";
            String formatted2 = rawString.replaceAll("\\[([0-9A-Fa-f]{6})\\]", spanStartStr);
            String output = "";
            if (formatted2.equalsIgnoreCase(rawString)) {
                output = rawString;
            } else {
                output = formatted2 + "</span>";
            }
            return output;
        } catch (Exception e) {
            return rawString;
        }
    }

    public static String htmlRemovedGameName(String rawString) {
        try {
            return rawString.replaceAll("\\[([0-9A-Fa-f]{6})\\]", "");
        } catch (Exception e) {
            return rawString;
        }
    }

    public static byte[] hexStringToByteArray(String s) {
        byte[] b = new byte[s.length() / 2];
        for (int i = 0; i < b.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(s.substring(index, index + 2), 16);
            b[i] = (byte) v;
        }
        return b;
    }

    public static String stringToHex(String base) throws UnsupportedEncodingException {
        return String.format("%040X", new BigInteger(1, base.getBytes(StandardCharsets.UTF_8)));
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static boolean isStringNotNull(String str) {
        if (str != null && !str.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isNumeric(String s) {
        return java.util.regex.Pattern.matches("\\d+", s);
    }


    private String returnNotNullVal(String str) {
        if (str != null && !str.isEmpty()) {
            return str;
        } else {
            return "";
        }
    }

    public static String getInsertColumns(String[] sArry) {
        String tmpl = "`%1$s`, ";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sArry.length; i++) {
            if (!sArry[i].equalsIgnoreCase("_id")) {

                sb.append(String.format(tmpl, sArry[i]));
            }
        }
        return sb.toString().substring(0, (sb.toString().length() - 2));
    }

    public static String getInsertQMs(int sArry) {
        String tmpl = "%1$s, ";
        StringBuilder sb = new StringBuilder();
        if (sArry > 1) {
            for (int i = 0; i < sArry; i++) {
                sb.append(String.format(tmpl, "?"));
            }
            return sb.toString().substring(0, (sb.toString().length() - 2));
        } else {
            sb.append("?");
            return sb.toString();
        }

    }


}
