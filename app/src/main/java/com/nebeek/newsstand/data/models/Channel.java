package com.nebeek.newsstand.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Channel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("dateCreated")
    @Expose
    private String dateCreated;
    @SerializedName("names")
    @Expose
    private List<Object> names = null;
    @SerializedName("owner")
    @Expose
    private String owner;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("payload")
    @Expose
    private Payload payload;
    @SerializedName("_cls")
    @Expose
    private String cls;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public List<Object> getNames() {
        return names;
    }

    public void setNames(List<Object> names) {
        this.names = names;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }

    public String getCls() {
        return cls;
    }

    public void setCls(String cls) {
        this.cls = cls;
    }

    public class Payload {

        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("photo")
        @Expose
        private Photo photo;
        @SerializedName("version")
        @Expose
        private Integer version;
        @SerializedName("flags")
        @Expose
        private Integer flags;
        @SerializedName("date")
        @Expose
        private Integer date;
        @SerializedName("id")
        @Expose
        private Integer id;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Photo getPhoto() {
            return photo;
        }

        public void setPhoto(Photo photo) {
            this.photo = photo;
        }

        public Integer getVersion() {
            return version;
        }

        public void setVersion(Integer version) {
            this.version = version;
        }

        public Integer getFlags() {
            return flags;
        }

        public void setFlags(Integer flags) {
            this.flags = flags;
        }

        public Integer getDate() {
            return date;
        }

        public void setDate(Integer date) {
            this.date = date;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

    }

}


