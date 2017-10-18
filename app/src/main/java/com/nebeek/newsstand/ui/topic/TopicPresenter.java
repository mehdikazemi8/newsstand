package com.nebeek.newsstand.ui.topic;

import android.util.Log;

import com.nebeek.newsstand.data.DataRepository;
import com.nebeek.newsstand.data.DataSource;
import com.nebeek.newsstand.data.models.Snippet;
import com.nebeek.newsstand.data.models.Topic;

import java.util.List;

public class TopicPresenter implements TopicContract.Presenter {

    private String keyword;
    private Topic topicObject = null;
    private TopicContract.View topicView;
    private DataRepository dataRepository;

    public TopicPresenter(String keyword, TopicContract.View topicView, DataRepository dataRepository) {
        this.keyword = keyword;
        this.topicView = topicView;
        this.dataRepository = dataRepository;
    }

    @Override
    public void start() {
        topicView.showLoading();

        dataRepository.searchKeyword(keyword, new DataSource.SearchKeywordCallback() {
            @Override
            public void onResponse(List<Snippet> snippetList) {
                Log.d("TAG", "onResponse " + snippetList);
                if (!topicView.isActive()) {
                    return;
                }
                topicView.hideLoading();

                topicView.showSearchResults(snippetList);
            }

            @Override
            public void onFailure() {
                Log.d("TAG", "onFailure");
                if (!topicView.isActive()) {
                    return;
                }
                topicView.hideLoading();
            }

            @Override
            public void onNetworkFailure() {
                Log.d("TAG", "onNetworkFailure");
                if (!topicView.isActive()) {
                    return;
                }
                topicView.hideLoading();
            }
        });
    }

    @Override
    public void subscribeToTopic() {
        dataRepository.subscribeToTopic(topicObject.getId(), new DataSource.SubscribeCallback() {
            @Override
            public void onSuccess() {
                if (!topicView.isActive()) {
                    return;
                }

//                topicObject = topic;
                topicView.changePlusToCheck();
            }

            @Override
            public void onFailure() {
                if (!topicView.isActive()) {
                    return;
                }

                topicView.showAddToLibraryError();
            }

            @Override
            public void onNetworkFailure() {
                if (!topicView.isActive()) {
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
                if (!topicView.isActive()) {
                    return;
                }

                topicView.changeCheckToPlus();
            }

            @Override
            public void onFailure() {
                if (!topicView.isActive()) {
                    return;
                }
            }

            @Override
            public void onNetworkFailure() {
                if (!topicView.isActive()) {
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