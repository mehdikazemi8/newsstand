package com.nebeek.newsstand.data.local.converter;

import android.arch.persistence.room.TypeConverter;

import com.nebeek.newsstand.data.models.AppText;

import java.util.ArrayList;
import java.util.List;


public class ListAppTextConverter {

    @TypeConverter
    public static String toString(List<AppText> list) {
        if(list == null) {
            return null;
        }

        return list.get(0).getFa();
    }

    @TypeConverter
    public static List<AppText> toAppTextList(String str) {
        if(str == null) {
            return null;
        }

        List<AppText> result = new ArrayList<>();
        result.add(new AppText(str));
        return result;
    }
}
