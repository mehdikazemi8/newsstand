package com.nebeek.newsstand.data.models;

public class Media {
    private Photo photo;
    private String caption;

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
}
