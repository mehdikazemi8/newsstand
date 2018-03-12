package com.nebeek.newsstand.data.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.nebeek.newsstand.data.local.converter.AppTextConverter;
import com.nebeek.newsstand.data.local.converter.ChannelConverter;
import com.nebeek.newsstand.data.local.converter.ListAppImageSetConverter;

import java.util.List;

@Entity
@TypeConverters({AppTextConverter.class, ChannelConverter.class, ListAppImageSetConverter.class})
public class TelegramMessage {

    @PrimaryKey
    @NonNull
    private String id;

    @Ignore
    private Attachment attachment = null;
    private String link;

    private List<AppImageSet> images = null;
    @SerializedName("text")
    private AppText fullText;
    private String dateCreated;
    private Integer likes;
    private String type;
    @SerializedName("channel")
    private String channelId;
    private Channel source;
    private Boolean liked;
    private Boolean bookmarked;
    private Long bookmarkTime;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Attachment getAttachment() {
        return attachment;
    }

    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;
    }

    public Long getBookmarkTime() {
        return bookmarkTime;
    }

    public void setBookmarkTime(Long bookmarkTime) {
        this.bookmarkTime = bookmarkTime;
    }

    public Boolean getBookmarked() {
        return bookmarked;
    }

    public void setBookmarked(Boolean bookmarked) {
        this.bookmarked = bookmarked;
    }

    public Boolean getLiked() {
        return liked;
    }

    public void setLiked(Boolean liked) {
        this.liked = liked;
    }

    public List<AppImageSet> getImages() {
        return images;
    }

    public void setImages(List<AppImageSet> images) {
        this.images = images;
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