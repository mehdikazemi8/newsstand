package com.nebeek.newsstand.data.remote.response;

import com.nebeek.newsstand.data.models.Snippet;

import java.util.List;

public class SearchResponse {

    private List<Snippet> results = null;

    public List<Snippet> getResults() {
        return results;
    }

    public void setResults(List<Snippet> results) {
        this.results = results;
    }
}
