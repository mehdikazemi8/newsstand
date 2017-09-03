package com.nebeek.newsstand.data;

import com.nebeek.newsstand.data.models.Keyword;
import com.nebeek.newsstand.data.models.Snippet;

import java.util.List;

public abstract class DataSource {

    public interface SearchKeywordCallback {

        void onResponse(List<Snippet> snippetList);

        void onFailure();

        void onNetworkFailure();
    }

    public interface GetKeywordsCallback {

        void onResponse(List<Keyword> keywordList);

        void onFailure();

        void onNetworkFailure();
    }

    public interface AddKeywordCallback {

        void onResponse(Keyword keyword);

        void onFailure();

        void onNetworkFailure();
    }

    public abstract void searchKeyword(String keyword, SearchKeywordCallback callback);

    public abstract void getKeywords(GetKeywordsCallback callback);

    public abstract void addKeyword(String keyword, AddKeywordCallback callback);
}
