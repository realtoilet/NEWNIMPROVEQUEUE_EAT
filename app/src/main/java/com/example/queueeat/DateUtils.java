package com.example.queueeat;

import com.google.firebase.Timestamp;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static Timestamp getStartOfDay(int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day, 0, 0, 0);
        Date start = c.getTime();
        return new Timestamp(start);
    }

    public static Timestamp getEndOfDay(int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day, 23, 59, 59);
        Date end = c.getTime();
        return new Timestamp(end);
    }
}

