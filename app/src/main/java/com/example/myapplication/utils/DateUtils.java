package com.example.myapplication.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    // 获取今天的日期字符串
    public static String getTodayDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        return sdf.format(new Date());
    }

    // 判断是否是今天
    public static boolean isToday(String dateStr) {
        return getTodayDate().equals(dateStr);
    }

    // 格式化日期显示
    public static String formatDate(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
            Date date = sdf.parse(dateStr);
            SimpleDateFormat displayFormat = new SimpleDateFormat("MM月dd日", Locale.CHINA);
            return displayFormat.format(date);
        } catch (Exception e) {
            return dateStr;
        }
    }
}

