package com.nebeek.newsstand.data.local.converter;

import android.arch.persistence.room.TypeConverter;

import com.nebeek.newsstand.data.models.AppText;

public class AppTextConverter {

    @TypeConverter
    public static String toString(AppText appText) {
        // todo, only takes FA
        return appText.getFa();
    }

    @TypeConverter
    public static AppText toAppText(String str) {
        return new AppText(str);
    }
}
