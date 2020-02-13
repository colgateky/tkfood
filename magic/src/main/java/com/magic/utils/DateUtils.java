package com.magic.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by xyz on 2015/8/3.
 */
public class DateUtils {
    private static SimpleDateFormat getDf() {
        return new SimpleDateFormat("yyyy-MM-dd");
    }
    private static SimpleDateFormat getDf2() {
        return new SimpleDateFormat("yyyyMMdd");
    }
    private static SimpleDateFormat getDfTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
    private static SimpleDateFormat getDfTimePart() {
        return new SimpleDateFormat("HH:mm:ss");
    }
    private static SimpleDateFormat getDfTimeTow() { return  new SimpleDateFormat("yyyy-MM-dd HH");}
    private static ThreadLocal<SimpleDateFormat> threadLocal = new ThreadLocal<SimpleDateFormat>();

    public static SimpleDateFormat getDateFormat()
    {
        SimpleDateFormat df = threadLocal.get();
        if(df==null){
            df = new SimpleDateFormat("yyyy-MM-dd");
            threadLocal.set(df);
        }
        return df;
    }

    public static String getDateTimeStr(Date d) {

            return getDfTime().format(d);

    }

    public static String getDateTimeStrPart(Date d) {

        return getDfTimePart().format(d);

    }

    public static Date convertToZone(Date d, int zone) {
        int timeZoneOffset = TimeZone.getDefault().getRawOffset()/3600000;
        int diff = zone - timeZoneOffset;
        d = new Date(d.getTime() + diff * 3600000);
        return d;
    }
    public static Date convertFromZone(Date d, int zone) {
        int timeZoneOffset = TimeZone.getDefault().getRawOffset()/3600000;
        int diff = timeZoneOffset - zone;
        d = new Date(d.getTime() + diff * 3600000);
        return d;
    }


    public static String getDateStr(Date d) {
        return getDf().format(d);
    }
    public static String getDateStr2(Date d) {
        return getDf2().format(d);
    }
    public static Date getToday() {

        Date ret = new Date();
        try {
            ret =  getDateFormat().parse(getDateFormat().format(ret));
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return ret;
    }
    public static Date parseDate(String dateStr) {
        try {
            return new DateDeserializer().deserialize(dateStr);
        } catch (Exception exp) {
            exp.printStackTrace();
            return null;
        }
    }

    public static Date parseDateTime(String dateStr) {
        try {
            return new DateDeserializer().deserialize(dateStr);
        } catch (Exception exp) {
            exp.printStackTrace();
            return null;
        }
    }

    public static Date getDate(Date d) {
        Date ret = null;
        try {
            ret = getDf().parse(getDf().format(d));
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return ret;
    }
    public static Date getHourFormatTime(Date d) {
        Date ret = null;
        try {
            ret = getDfTimeTow().parse(getDfTimeTow().format(d));
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return ret;
    }
    public static Date addDay(Date d, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(Calendar.DATE, days);
        return c.getTime();
    }

    public static Date addWeek(Date d, int weekCount) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(Calendar.DATE, weekCount * 7);
        return c.getTime();
    }

    public static Date addMonth(Date d, int monthCount) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(Calendar.MONTH, monthCount);
        return c.getTime();
    }

    public static Date getWeekStartDate(Date d) {
        int weekDay = d.getDay();
        if (weekDay == 0) {
            weekDay = 7;
        }
        Date ret = addDay(d, -weekDay + 1);
        return ret;
    }

    public static Date getMonthStartDate(Date d) {
        int day = d.getDate();
        Date ret = addDay(d, -day + 1);
        return ret;
    }

    public static Date getYearStartDate(Date d) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.set(Calendar.DAY_OF_YEAR, 1);
        return c.getTime();
    }

    public static Date addYear(Date d, int year) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(Calendar.YEAR, year);
        return c.getTime();
    }


    public static Date addMinute(Date d, int minutes) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(Calendar.MINUTE, minutes);
        return c.getTime();
    }
    public static Date addSeconds(Date d, int seconds) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(Calendar.SECOND, seconds);
        return c.getTime();
    }


}
