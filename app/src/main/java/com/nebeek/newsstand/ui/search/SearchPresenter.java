package com.nebeek.newsstand.ui.search;

import android.util.Log;

import com.nebeek.newsstand.data.DataRepository;
import com.nebeek.newsstand.data.DataSource;
import com.nebeek.newsstand.data.models.Snippet;

import java.util.List;

public class SearchPresenter implements SearchContract.Presenter {

    private SearchContract.View searchView;
    private DataRepository dataRepository;

    public SearchPresenter(SearchContract.View searchView, DataRepository dataRepository) {
        this.searchView = searchView;
        this.dataRepository = dataRepository;
    }

    @Override
    public void start() {
        searchView.focusOnSearchView();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchView.showLoading();

        dataRepository.searchKeyword(query, new DataSource.SearchKeywordCallback() {
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
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.d("TAG", "newTExt " + newText);
        return false;
    }
}
