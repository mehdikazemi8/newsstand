package com.nebeek.newsstand.util;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateManager {

    public static String convertStringToDate(String dateStr) {

        Log.d("TAG", "abcdddd " + dateStr);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");

        try {
            Date inputDate = dateFormat.parse(dateStr);
            long date = inputDate.getTime() / 1000;
            long now = System.currentTimeMillis() / 1000;

            Log.d("TAG", "abcdddd " + now + " " + date);

            long diff = now - date;

            // todo
            diff -= 3600 * 4 + 1800;

            if (diff > 86400) {
                return String.valueOf(diff / 86400) + " روز پیش ";
            } else if (diff > 3600) {
                return String.valueOf(diff / 3600) + " ساعت پیش ";
            } else if (diff > 500) {
                return String.valueOf(diff / 60) + " دقیقه پیش ";
            } else {
                return "لحظاتی پیش";
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return "لحظاتی پیش";
        }
    }
}
