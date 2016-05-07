package com.mianshibang.main.utils;

import java.util.Locale;

public class TimeUtils {

	public static String convert(long time) {
		long interval = System.currentTimeMillis() - time;
		
		 long second = 1000;
         long minute = second * 60;
         long hour = minute * 60;
         long day = hour * 24;
         long week = day * 7;
         long month = day * 30;

        int intervalMonths = (int) (interval / month);
        int intervalWeeks = (int) (interval / week) ;
        int intervalDays = (int) (interval / day);
        int intervalHours = (int) (interval / hour);
        int intervalMinutes = (int) (interval / minute);
        int intervalSeconds = (int) (interval / second);

        if (intervalMonths > 12) { // 1年前
            return "1年前";
        } else if (intervalMonths > 0 ) { // %d月前
        	return String.format(Locale.US, "%d个月前", intervalMonths);
        } else if (intervalWeeks > 0 ) { // %d周前
        	return String.format(Locale.US, "%d周前", intervalWeeks);
        } else if (intervalDays > 0 ) { // %d天前
        	return String.format(Locale.US, "%d天前", intervalDays);
        } else if (intervalHours > 0) {
        	return String.format(Locale.US, "%d小时前", intervalHours);
        } else if (intervalMinutes > 0) {
        	return String.format(Locale.US, "%d分钟前", intervalMinutes);
        } else if (intervalSeconds > 3) {
        	return String.format(Locale.US, "%d秒钟前", intervalSeconds);
        } else {
            return "刚刚";
        }

	}
}
