package com.nebeek.newsstand.ui.topic;

import com.nebeek.newsstand.data.DataRepository;
import com.nebeek.newsstand.data.DataSource;
import com.nebeek.newsstand.data.models.Topic;
import com.nebeek.newsstand.data.remote.response.MessagesResponse;

public class TopicPresenter implements TopicContract.Presenter {

    private Topic topicObject = null;
    private TopicContract.View topicView;
    private DataRepository dataRepository;

    public TopicPresenter(Topic topicObject, TopicContract.View topicView, DataRepository dataRepository) {
        this.topicObject = topicObject;
        this.topicView = topicView;
        this.dataRepository = dataRepository;
    }

    @Override
    public void start() {
        topicView.showLoading();

        dataRepository.getMessages(topicObject.getId(), new DataSource.GetMessagesCallback() {
            @Override
            public void onResponse(MessagesResponse response) {

                topicView.hideLoading();
                topicView.showGetMessagesResults(response.getResults());
            }

            @Override
            public void onFailure() {

            }

            @Override
            public void onNetworkFailure() {

            }
        });

        /*
        dataRepository.searchKeyword(keyword, new DataSource.SearchKeywordCallback() {
            @Override
            public void onResponse(List<Snippet> snippetList) {
                Log.d("TAG", "onResponse " + snippetList);
                if (!topicView.isActive()) {
                    return;
                }
                topicView.hideLoading();

                topicView.showGetMessagesResults(snippetList);
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
        */
    }

    @Override
    public void subscribeToTopic() {
        dataRepository.subscribeToTopic(topicObject.getDeleteId(), new DataSource.SubscribeCallback() {
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

        dataRepository.removeKeyword(topicObject.getDeleteId(), new DataSource.RemoveKeywordCallback() {
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
