package com.nebeek.newsstand.data;

import com.nebeek.newsstand.data.models.Snippet;

import java.util.List;

public abstract class DataSource {

    public interface SearchKeywordCallback {

        void onResponse(List<Snippet> snippetList);

        void onFailure();

        void onNetworkFailure();
    }

    public abstract void searchKeyword(String keyword, SearchKeywordCallback callback);
}
