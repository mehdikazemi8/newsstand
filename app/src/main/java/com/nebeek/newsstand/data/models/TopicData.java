package com.nebeek.newsstand.data.models;

import com.google.gson.annotations.SerializedName;

public class TopicData {

    @SerializedName("id")
    private String id;
    @SerializedName("argument")
    private String argument;

    public String getArgument() {
        return argument;
    }

    public void setArgument(String argument) {
        this.argument = argument;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
