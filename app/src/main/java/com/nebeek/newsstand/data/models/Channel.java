package com.nebeek.newsstand.data.models;

import java.util.List;

public class Channel {
    private List<AppImageSet> imageSets = null;
    private List<AppText> names = null;
    private String id;
    private String likes;

    public List<AppImageSet> getImageSets() {
        return imageSets;
    }

    public void setImageSets(List<AppImageSet> imageSets) {
        this.imageSets = imageSets;
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


