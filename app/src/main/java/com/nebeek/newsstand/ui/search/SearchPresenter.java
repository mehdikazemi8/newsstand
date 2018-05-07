package com.nebeek.newsstand.ui.search;

import com.nebeek.newsstand.data.DataRepository;
import com.nebeek.newsstand.data.DataSource;
import com.nebeek.newsstand.data.models.Topic;

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
        searchView.focusOnSearchInput();
    }

    @Override
    public void getTopics(String query) {
        if (query == null || query.length() < 2) {
            searchView.clearSuggestions();
            return;
        }

        dataRepository.getAllTopics(query, new DataSource.TopicsResponseCallback() {
            @Override
            public void onResponse(List<Topic> myList) {
                if (myList.size() == 0) {
                    searchView.clearSuggestions();
                    searchView.showNoResultMessage();
                } else {
                    searchView.hideNoResultMessage();
                    searchView.showSuggestions(myList);
                }
            }

            @Override
            public void onFailure() {

            }

            @Override
            public void onNetworkFailure() {

            }
        });
    }

    @Override
    public void onSuggestionClicked(Topic topic) {
        searchView.showTopicUI(topic);
    }
}
