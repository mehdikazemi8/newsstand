package com.nebeek.newsstand.ui.search;

import android.util.Log;

import com.nebeek.newsstand.data.DataRepository;
import com.nebeek.newsstand.data.DataSource;
import com.nebeek.newsstand.data.models.Keyword;
import com.nebeek.newsstand.data.models.Snippet;

import java.util.List;

public class SearchPresenter implements SearchContract.Presenter {

    private String keyword;
    private Keyword keywordObject = null;
    private SearchContract.View searchView;
    private DataRepository dataRepository;

    public SearchPresenter(String keyword, SearchContract.View searchView, DataRepository dataRepository) {
        this.keyword = keyword;
        this.searchView = searchView;
        this.dataRepository = dataRepository;
    }

    @Override
    public void start() {
        searchView.showLoading();

        dataRepository.searchKeyword(keyword, new DataSource.SearchKeywordCallback() {
            @Override
            public void onResponse(List<Snippet> snippetList) {
                Log.d("TAG", "onResponse " + snippetList);
                if (!searchView.isActive()) {
                    return;
                }
                searchView.hideLoading();

                searchView.showSearchResults(snippetList);
            }

            @Override
            public void onFailure() {
                Log.d("TAG", "onFailure");
                if (!searchView.isActive()) {
                    return;
                }
                searchView.hideLoading();
            }

            @Override
            public void onNetworkFailure() {
                Log.d("TAG", "onNetworkFailure");
                if (!searchView.isActive()) {
                    return;
                }
                searchView.hideLoading();
            }
        });
    }

    @Override
    public void addToLibrary() {
        dataRepository.addKeyword(keyword, new DataSource.AddKeywordCallback() {
            @Override
            public void onResponse(Keyword keyword) {
                if (!searchView.isActive()) {
                    return;
                }

                keywordObject = keyword;
                searchView.changePlusToCheck();
            }

            @Override
            public void onFailure() {
                if (!searchView.isActive()) {
                    return;
                }
            }

            @Override
            public void onNetworkFailure() {
                if (!searchView.isActive()) {
                    return;
                }
            }
        });
    }

    @Override
    public void removeFromLibrary() {
        if (keywordObject == null) {
            return;
        }

        dataRepository.removeKeyword(keywordObject.getId(), new DataSource.RemoveKeywordCallback() {
            @Override
            public void onSuccess() {
                if (!searchView.isActive()) {
                    return;
                }

                searchView.changeCheckToPlus();
            }

            @Override
            public void onFailure() {
                if (!searchView.isActive()) {
                    return;
                }
            }

            @Override
            public void onNetworkFailure() {
                if (!searchView.isActive()) {
                    return;
                }
            }
        });
    }
}
