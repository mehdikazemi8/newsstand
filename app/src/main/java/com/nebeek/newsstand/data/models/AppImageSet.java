package com.nebeek.newsstand.data.models;

import java.util.ArrayList;
import java.util.List;

public class AppImageSet {
    private List<AppImage> sizes = new ArrayList<>();
    private AppImage original;

    public AppImageSet addImage(AppImage appImage) {
        sizes.add(appImage);
        return this;
    }

    public List<AppImage> getImages() {
        return sizes;
    }

    public void setImages(List<AppImage> images) {
        this.sizes = images;
    }
}
