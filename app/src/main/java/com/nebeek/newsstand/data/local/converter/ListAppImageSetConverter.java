package com.nebeek.newsstand.data.local.converter;

import android.arch.persistence.room.TypeConverter;

import com.nebeek.newsstand.data.models.AppImage;
import com.nebeek.newsstand.data.models.AppImageSet;

import java.util.ArrayList;
import java.util.List;

public class ListAppImageSetConverter {

    @TypeConverter
    public static List<AppImageSet> toList(String str) {
        if (str == null) {
            return null;
        }

        List<AppImageSet> list = new ArrayList<>();
        list.add(new AppImageSet().addImage(new AppImage(str)));
        return list;
    }

    @TypeConverter
    public static String toString(List<AppImageSet> list) {
        if (list == null || list.size() == 0 || list.get(0).getImages() == null) {
            return null;
        }

        return list.get(0).getImages().get(0).getData();
    }
}
