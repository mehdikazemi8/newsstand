package com.nebeek.newsstand.util;

import android.util.Log;

public class DateManager {

    public static String convertLongToDate(Long date) {
        long now = System.currentTimeMillis();
        long diff = now - date;

        diff /= 1000000;
        if(diff > 86400) {
            return String.valueOf(diff / 86400) + " روز پیش ";
        } else if(diff > 3600) {
            return String.valueOf(diff / 3600) + " ساعت پیش ";
        } else if(diff > 500) {
            return String.valueOf(diff / 60) + " دقیقه پیش ";
        } else {
            return "لحظاتی پیش";
        }
    }
}
