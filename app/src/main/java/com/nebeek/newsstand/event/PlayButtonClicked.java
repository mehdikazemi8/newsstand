package com.nebeek.newsstand.event;

public class PlayButtonClicked {
    private String url;

    public PlayButtonClicked(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
