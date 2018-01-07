package com.nebeek.newsstand.data.remote.response;

import com.google.gson.annotations.SerializedName;
import com.nebeek.newsstand.data.models.Channel;
import com.nebeek.newsstand.data.models.TelegramMessage;

import java.util.List;

public class MessagesResponse {
    private Integer count;
    private String next;
    private String previous;
    private List<TelegramMessage> data = null;
    @SerializedName("included")
    private List<Channel> channels;

    public List<TelegramMessage> getResults() {
        return data;
    }

    public void setResults(List<TelegramMessage> results) {
        this.data = results;
    }

    public List<Channel> getChannels() {
        return channels;
    }

    public void setChannels(List<Channel> channels) {
        this.channels = channels;
    }
}
