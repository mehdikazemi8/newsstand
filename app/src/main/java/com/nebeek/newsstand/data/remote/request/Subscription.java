package com.nebeek.newsstand.data.remote.request;

public class Subscription {
    private String argument;

    public Subscription(String argument) {
        this.argument = argument;
    }

    public String getArgument() {
        return argument;
    }

    public void setArgument(String argument) {
        this.argument = argument;
    }
}
