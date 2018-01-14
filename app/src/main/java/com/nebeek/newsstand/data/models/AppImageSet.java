package com.nebeek.newsstand.data.models;

import java.util.ArrayList;
import java.util.List;

public class AppImageSet {
    private List<AppImage> set = new ArrayList<>();

    public AppImageSet addImage(AppImage appImage) {
        set.add(appImage);
        return this;
    }

    public List<AppImage> getImages() {
        return set;
    }

    public void setImages(List<AppImage> images) {
        this.set = images;
    }
}
