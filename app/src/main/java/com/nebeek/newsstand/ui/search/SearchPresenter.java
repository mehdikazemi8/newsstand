package com.nebeek.newsstand.ui.search;

import android.util.Log;

import com.nebeek.newsstand.data.DataRepository;
import com.nebeek.newsstand.data.DataSource;
import com.nebeek.newsstand.data.models.Snippet;
import com.nebeek.newsstand.data.models.Topic;

import java.util.List;

public class SearchPresenter implements SearchContract.Presenter {

    private String keyword;
    private Topic topicObject = null;
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
    public void subscribeToTopic() {
        dataRepository.subscribeToTopic(topicObject.getId(), new DataSource.SubscribeCallback() {
            @Override
            public void onSuccess() {
                if (!searchView.isActive()) {
                    return;
                }

//                topicObject = topic;
                searchView.changePlusToCheck();
            }

            @Override
            public void onFailure() {
                if (!searchView.isActive()) {
                    return;
                }

                searchView.showAddToLibraryError();
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
        if (topicObject == null) {
            return;
        }

        dataRepository.removeKeyword(topicObject.getId(), new DataSource.RemoveKeywordCallback() {
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

    @Override
    public void setTopicObject(Topic topicObject) {
        this.topicObject = topicObject;
    }
}
