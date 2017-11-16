package com.nebeek.newsstand.data.remote.response;

import com.nebeek.newsstand.data.models.Snippet;

import java.util.List;

public class MessagesResponse {

    private Integer count;
    private String next;
    private String previous;
    private List<Snippet> data = null;

    public List<Snippet> getResults() {
        return data;
    }

    public void setResults(List<Snippet> results) {
        this.data = results;
    }
}
