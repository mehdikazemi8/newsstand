package com.nebeek.newsstand.data.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.nebeek.newsstand.data.local.converter.ListAppTextConverter;

import java.util.List;

@Entity
@TypeConverters({ListAppTextConverter.class})
public class Topic extends BaseModel {

    @PrimaryKey
    @NonNull
    private String id;
    private List<AppText> names;
    @Ignore
    private AppSize subscribes;
    private Integer likes;
    private String deleteId;

    private String text;
    private String photoURL;
    private Boolean receiveNotification;
    private Boolean inLibrary = false;

    public List<AppText> getNames() {
        return names;
    }

    public void setNames(List<AppText> names) {
        this.names = names;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public AppSize getSubscribes() {
        return subscribes;
    }

    public void setSubscribes(AppSize subscribes) {
        this.subscribes = subscribes;
    }

    public Boolean getInLibrary() {
        return inLibrary;
    }

    public void setInLibrary(Boolean inLibrary) {
        this.inLibrary = inLibrary;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public Boolean getReceiveNotification() {
        return receiveNotification;
    }

    public void setReceiveNotification(Boolean receiveNotification) {
        this.receiveNotification = receiveNotification;
    }

    public String getDeleteId() {
        return deleteId;
    }

    public void setDeleteId(String deleteId) {
        this.deleteId = deleteId;
    }
}
