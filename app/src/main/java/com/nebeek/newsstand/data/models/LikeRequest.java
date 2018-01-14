package com.nebeek.newsstand.data.models;

public class LikeRequest {
    private String type = "Like";
    private String argument;

    public LikeRequest(String argument) {
        this.argument = argument;
    }

    public LikeRequest() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getArgument() {
        return argument;
    }

    public void setArgument(String argument) {
        this.argument = argument;
    }
}
