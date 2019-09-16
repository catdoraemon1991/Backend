package com.laioffer.botlogistics;

import com.google.android.gms.common.util.Hex;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import static org.apache.commons.codec.binary.Hex.encodeHex;

/**
 * Util class stores util static method
 */
public class Utils {
    public static final String BEFORE_SHIP_MESG = "order processing";
    public static final String DEPART_MESG = "departed, on its way to pickup";
    public static final String PICKUP_MESG = "picked up, on its way to destination";
    public static final String DELIVER_MESG = "delivered";
    /**
     * Md5 encryption, encode string
     * @param input the string to be encoded
     * @return encoded string
     */
    public static String md5Encryption(final String input){
        String result = "";
        try{
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(input.getBytes(Charset.forName("UTF8")));
            byte[] resultByte = messageDigest.digest();
            result = new String(Hex.bytesToStringLowercase(resultByte)); //
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return result;
    }

    public static String convertTime(Long milisecond){
        DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");

        Calendar calendar = Calendar.getInstance();
        TimeZone tz = TimeZone.getTimeZone("PST");
        calendar.setTimeZone(tz);
        calendar.setTimeInMillis(milisecond);

        return formatter.format(calendar.getTime());
    }
}

