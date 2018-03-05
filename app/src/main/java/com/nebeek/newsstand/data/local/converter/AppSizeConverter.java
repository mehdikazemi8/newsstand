package com.nebeek.newsstand.data.local.converter;

import android.arch.persistence.room.TypeConverter;

import com.nebeek.newsstand.data.models.AppSize;

public class AppSizeConverter {

    @TypeConverter
    public static Integer toInteger(AppSize appSize) {
        try {
            return appSize.getSize();
        } catch (Exception e) {
            return 0;
        }
    }

    @TypeConverter
    public static AppSize toAppSize(Integer size) {
        try {
            return new AppSize(size);
        } catch (Exception e) {
            return new AppSize(0);
        }
    }
}
