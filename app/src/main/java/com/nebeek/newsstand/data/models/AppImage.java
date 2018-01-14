package com.nebeek.newsstand.data.models;

public class AppImage {
    private String url;
    private Integer width;
    private Integer height;

    public AppImage(String url) {
        this.url = url;
    }

    public String getData() {
        return url;
    }

    public void setData(String url) {
        this.url = url;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }
}
