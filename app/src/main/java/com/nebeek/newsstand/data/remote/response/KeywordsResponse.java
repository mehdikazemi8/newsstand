package com.nebeek.newsstand.data.remote.response;

import com.nebeek.newsstand.data.models.Keyword;

import java.util.List;

public class KeywordsResponse {
    List<Keyword> keywords;

    public List<Keyword> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<Keyword> keywords) {
        this.keywords = keywords;
    }
}
