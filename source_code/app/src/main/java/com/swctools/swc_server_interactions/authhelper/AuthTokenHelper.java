package com.swctools.swc_server_interactions.authhelper;

import android.util.Base64;

import com.swctools.common.helpers.DateTimeHelper;
import com.swctools.util.ErrorLogger;
import com.swctools.util.EscapeChars;
import com.swctools.util.StringUtil;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import static java.nio.charset.StandardCharsets.UTF_8;


public class AuthTokenHelper {
    private static final String TAG = "AuthTokenHelper";
    private String playerID;
    private String playerSecret;
//private SimpleDateFormat sdfMilliSec = new SimpleDateFormat("Ms");

    private String token;

    public AuthTokenHelper(String pid, String pSec) {
        this.playerID = pid;
        this.playerSecret = pSec;
    }

    private static String hashStr(String msg, String playerSecret) {
        String shaBStr = "";
        try {
            //Hex the {"userId":"%%","expires":%%} part
            String hexMsg = StringUtil.stringToHex(msg);
            // Create a SecretKeySpec Object, with the playerSecret as a byte array and the HmacSHA256 algo
            SecretKeySpec secret_key = new SecretKeySpec(playerSecret.getBytes(UTF_8), "HmacSHA256");
            // get an instance of the Mac object using HmacSHA256
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            //initialise the mac object with the secretkey object:
            sha256_HMAC.init(secret_key);
            //produce the bytes of
            byte[] sha = sha256_HMAC.doFinal(StringUtil.hexStringToByteArray(hexMsg));
            shaBStr = StringUtil.bytesToHex(sha);
        } catch (InvalidKeyException e) {
            ErrorLogger.LogError(e.getMessage(), "AuthTokenHelper", "hashStr", "");
        } catch (NoSuchAlgorithmException e) {
            ErrorLogger.LogError(e.getMessage(), "AuthTokenHelper", "hashStr", "");
        } catch (UnsupportedEncodingException e) {
            ErrorLogger.LogError(e.getMessage(), "AuthTokenHelper", "hashStr", "");
        } catch (IllegalStateException e) {
            ErrorLogger.LogError(e.getMessage(), "AuthTokenHelper", "hashStr", "");
        }
        return shaBStr;
    }

    public String getToken() throws Exception {
        //Get the message...
        String message = userIDTimeStamp();
        //Encode it with the secret and return the base64...
        this.token = String.format(AuthComponentTemplates.TOKENSTR.toString(), hashStr(message, this.playerSecret), message);
        return Base64.encodeToString(token.getBytes(UTF_8), Base64.NO_WRAP);

    }

    public String userIDTimeStamp() {
        return String.format(AuthComponentTemplates.USERTIMESTAMP.toString(), EscapeChars.DOUBLE_QUOTES, playerID, DateTimeHelper.userIDTimeStamp());
    }

    public static String getAuthToken(String playerId, String playerSecret) {
        //Get the message...
        String message = String.format(AuthComponentTemplates.USERTIMESTAMP.toString(), EscapeChars.DOUBLE_QUOTES, playerId, DateTimeHelper.userIDTimeStamp());
        String token = String.format(AuthComponentTemplates.TOKENSTR.toString(), hashStr(message, playerSecret), message);
        return Base64.encodeToString(token.getBytes(StandardCharsets.UTF_8), Base64.NO_WRAP);
    }

}

	
