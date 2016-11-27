package me.fromgate.elytra.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Time {
    public static Long timeToTicks(Long time) {
        //1000 ms = 20 ticks
        return Math.max(1, (time / 50));
    }

    public static Long parseTime(String time) {
        if (time == null || time.isEmpty()) return 0L;
        int hh = 0; // часы
        int mm = 0; // минуты
        int ss = 0; // секунды
        int tt = 0; // тики
        int ms = 0; // миллисекунды
        if (isInteger(time)) {
            ss = Integer.parseInt(time);
        } else if (time.matches("^[0-5][0-9]:[0-5][0-9]$")) {
            String[] ln = time.split(":");
            if (isInteger(ln[0])) mm = Integer.parseInt(ln[0]);
            if (isInteger(ln[1])) ss = Integer.parseInt(ln[1]);
        } else if (time.matches("^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$")) {
            String[] ln = time.split(":");
            if (isInteger(ln[0])) hh = Integer.parseInt(ln[0]);
            if (isInteger(ln[1])) mm = Integer.parseInt(ln[1]);
            if (isInteger(ln[2])) ss = Integer.parseInt(ln[2]);
        } else if (time.matches("^\\d+ms")) {
            ms = Integer.parseInt(time.replace("ms", ""));
        } else if (time.matches("^\\d+h")) {
            hh = Integer.parseInt(time.replace("h", ""));
        } else if (time.matches("^\\d+m$")) {
            mm = Integer.parseInt(time.replace("m", ""));
        } else if (time.matches("^\\d+s$")) {
            ss = Integer.parseInt(time.replace("s", ""));
        } else if (time.matches("^\\d+t$")) {
            tt = Integer.parseInt(time.replace("t", ""));
        }
        return (hh * 3600000L) + (mm * 60000L) + (ss * 1000L) + (tt * 50L) + ms;
    }

    public static boolean isInteger(String intStr) {
        return intStr.matches("\\d+");
    }

    public static String fullTimeToString(long time, String format) {
        Date date = new Date(time);
        DateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    public static String timeHHMMSS(long time) {
        long t = time / 1000; // перевели в секунды
        int sec = (int) t % 60;
        t = t / 60;
        int min = (int) t % 60;
        t = t / 60;
        int hour = (int) t % 60;
        int days = (int) t / 60;
        return ((days > 0) ? days + "d " : "") + String.format("%02d:%02d:%02d", hour, min, sec);
    }

    public static String timeToString(long time) {
        long t = time / 1000; // перевели в секунды
        int sec = (int) t % 60;
        t = t / 60;
        int min = (int) t % 60;
        t = t / 60;
        int hour = (int) t % 60;
        int days = (int) t / 60;

        StringBuilder sb = new StringBuilder();
        if (days > 0) sb.append(days).append("d ");
        if (hour > 0 || sb.length() > 0) sb.append(hour).append("h ");
        if (min > 0 || sb.length() > 0) sb.append(min).append("min ");
        sb.append(sec).append("sec ");
        return sb.toString();
    }
}

