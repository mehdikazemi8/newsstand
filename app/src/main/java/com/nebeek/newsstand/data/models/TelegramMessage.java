package com.nebeek.newsstand.data.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TelegramMessage {

    private List<AppImageSet> imageSets = null;
    private AppText fullText;
    private String dateCreated;
    private Integer likes;
    private String type;
    private String id;
    @SerializedName("channel")
    private String channelId;
    private Channel source;

    public List<AppImageSet> getImageSets() {
        return imageSets;
    }

    public void setImageSets(List<AppImageSet> imageSets) {
        this.imageSets = imageSets;
    }

    public AppText getFullText() {
        return fullText;
    }

    public void setFullText(AppText fullText) {
        this.fullText = fullText;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public Channel getSource() {
        return source;
    }

    public void setSource(Channel source) {
        this.source = source;
    }
}