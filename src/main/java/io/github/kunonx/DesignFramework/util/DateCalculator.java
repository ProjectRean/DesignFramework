package io.github.kunonx.DesignFramework.util;

import java.text.SimpleDateFormat;
import java.util.*;

public final class DateCalculator
{
    public enum DAY
    {
        SUNDAY(1, "SUNDAY"),
        MONDAY(2, "MONDAY"),
        TUESDAY(3, "TUESDAY"),
        WEDNESDAY(4, "WEDNESDAY"),
        THURSDAY(5, "THURSDAY"),
        FRIDAY(6, "FRIDAY"),
        SATURDAY(7, "SATURDAY"),
        UNKNOWN(-1, "UNKNOWN");

        private int value;
        private String name;

        DAY(int value, String name)
        {
            this.value = value;
            this.name = name;
        }

        public int getValue()
        {
            return this.value;
        }

        public String getName()
        {
            return this.name;
        }


        public static DAY getDay(int value)
        {
            for(DAY day : DAY.values())
            {
                if(day.getValue() == value) return day;
            }
            return UNKNOWN;
        }

        public static DAY getDay(String name)
        {
            for(DAY day : DAY.values())
            {
                if(day.getName().equalsIgnoreCase(name)) return day;
            }
            return UNKNOWN;
        }
    }

    private static Calendar oCalendar;

    static
    {
        oCalendar = Calendar.getInstance();
    }

    public static Calendar getCalendarMapping(Date d)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        return c;
    }

    public static String getStandardDate()
{
    return DateCalculator.getDate("yyyy-MM-dd hh:mm:ss");
}

    public static String getDate(String format)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date()).toString();
    }

    public static String getDate(long time)
    {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return sdf.format(date).toString();
    }

    public static String getDate(int yearAgo, int monthAgo, int dayAgo)
    {
        Calendar cal = Calendar.getInstance();
        cal.add(cal.YEAR, (-1) * yearAgo);
        cal.add(cal.MONTH, (-1) * monthAgo);
        cal.add(cal.DAY_OF_MONTH, (-1) * dayAgo);
       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
       return sdf.format(cal.getTime()).toString();
    }

    public static Calendar changeCalendar(int yearAgo, int monthAgo, int dayAgo)
    {
        Calendar cal = Calendar.getInstance();
        cal.add(cal.YEAR, (-1) * yearAgo);
        cal.add(cal.MONTH, (-1) * monthAgo);
        cal.add(cal.DAY_OF_MONTH, (-1) * dayAgo);
        return cal;
    }

    public static Calendar changeCalendar(Calendar cal, int yearAgo, int monthAgo, int dayAgo)
    {
        cal.add(cal.YEAR, (-1) * yearAgo);
        cal.add(cal.MONTH, (-1) * monthAgo);
        cal.add(cal.DAY_OF_MONTH, (-1) * dayAgo);
        return cal;
    }

    public static int getYear() { return oCalendar.get(Calendar.YEAR); }
    public static int getYear(Calendar c1, Calendar c2)
    {
        if(c1.getTime().getTime() < c2.getTime().getTime())
        {
            Calendar temp = c1;
            c1 = c2;
            c2 = temp;
        }
        int year = (int)((c1.getTime().getTime() - c2.getTime().getTime())) / 1000;
        return year;
    }

    public static int getMonth() { return oCalendar.get(Calendar.MONTH + 1); }
    public static int getDay() { return oCalendar.get(Calendar.DAY_OF_MONTH); }
    public static int getDay(Calendar c1, Calendar c2)
    {
        if(c1.getTime().getTime() < c2.getTime().getTime())
        {
            Calendar temp = c1;
            c1 = c2;
            c2 = temp;
        }
        int day = (int)((c1.getTime().getTime() - c2.getTime().getTime())) / (60*60*24);
        return day;
    }
    public static int getHour() { return oCalendar.get(Calendar.HOUR_OF_DAY); }
    public static int getMinute() { return oCalendar.get(Calendar.MINUTE); }
    public static int getSecond() { return oCalendar.get(Calendar.SECOND); }
    public static int getMillSecond() { return oCalendar.get(Calendar.MILLISECOND); }
    public static int getDayOfWeek() { return oCalendar.get(Calendar.DAY_OF_WEEK); }
    public static DAY getDayValue() { return DAY.getDay(DateCalculator.getDayOfWeek()); }
    public static int getDayOfYear() { return oCalendar.get(Calendar.DAY_OF_YEAR); }
    public static int getWeekOfYear() { return oCalendar.get(Calendar.WEEK_OF_YEAR); }
    public static int getWeekOfMonth() { return oCalendar.get(Calendar.WEEK_OF_MONTH); }
}
