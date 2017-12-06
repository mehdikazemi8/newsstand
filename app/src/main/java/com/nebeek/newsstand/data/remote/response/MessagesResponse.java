package com.nebeek.newsstand.data.remote.response;

import com.google.gson.annotations.SerializedName;
import com.nebeek.newsstand.data.models.Channel;
import com.nebeek.newsstand.data.models.Snippet;

import java.util.List;

public class MessagesResponse {
    private Integer count;
    private String next;
    private String previous;
    private List<Snippet> data = null;
    @SerializedName("included")
    private List<Channel> channels;

    public List<Snippet> getResults() {
        return data;
    }

    public void setResults(List<Snippet> results) {
        this.data = results;
    }

    public List<Channel> getChannels() {
        return channels;
    }

    public void setChannels(List<Channel> channels) {
        this.channels = channels;
    }
}
