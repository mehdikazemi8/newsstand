package com.nebeek.newsstand.data.models;

import java.util.ArrayList;
import java.util.List;

public class Channel {
    private List<AppImageSet> images = new ArrayList<>();
    private List<AppText> names = new ArrayList<>();
    private String id;
    private String likes;

    public List<AppImageSet> getImages() {
        return images;
    }

    public void setImages(List<AppImageSet> images) {
        this.images = images;
    }

    public List<AppText> getNames() {
        return names;
    }

    public void setNames(List<AppText> names) {
        this.names = names;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }
}


