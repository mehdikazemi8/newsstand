package com.nebeek.newsstand.data.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Topic extends BaseModel {
    @SerializedName("_id")
    private String id;
    private List<String> names;
    private Integer subscribes;
    private String deleteId;

    String text;
    String photoURL;
    Boolean receiveNotification;
    Boolean inLibrary = false;

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public Integer getSubscribes() {
        return subscribes;
    }

    public void setSubscribes(Integer subscribes) {
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
