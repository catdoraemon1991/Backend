package com.laioffer.botlogistics;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class DataService {
    public static String convertTime(Long milisecond){
        DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");

        Calendar calendar = Calendar.getInstance();
        TimeZone tz = TimeZone.getTimeZone("PST");
        calendar.setTimeZone(tz);
        calendar.setTimeInMillis(milisecond);

        return formatter.format(calendar.getTime());
    }
}
