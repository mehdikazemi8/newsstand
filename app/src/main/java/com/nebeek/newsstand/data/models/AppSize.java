package com.nebeek.newsstand.data.models;

import android.arch.persistence.room.TypeConverters;

import com.nebeek.newsstand.data.local.converter.AppSizeConverter;

@TypeConverters({AppSizeConverter.class})
public class AppSize {
    private Integer size;

    public AppSize(Integer size) {
        this.size = size;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
