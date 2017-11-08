package com.nebeek.newsstand.data.models;

import java.util.List;

public class Photo {

    List<PhotoSize> sizes;

    public List<PhotoSize> getSizes() {
        return sizes;
    }

    public void setSizes(List<PhotoSize> sizes) {
        this.sizes = sizes;
    }
}
