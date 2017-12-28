package com.nebeek.newsstand.ui.topic;

import com.nebeek.newsstand.data.DataRepository;
import com.nebeek.newsstand.data.DataSource;
import com.nebeek.newsstand.data.models.Topic;
import com.nebeek.newsstand.data.remote.response.MessagesResponse;
import com.nebeek.newsstand.event.NewSubscription;
import com.nebeek.newsstand.event.RxBus;

import java.util.Collections;

public class TopicPresenter implements TopicContract.Presenter {

    private Topic topicObject = null;
    private TopicContract.View topicView;
    private DataRepository dataRepository;
    private int currentPage = -1;
    private boolean isLoading = false;

    public TopicPresenter(Topic topicObject, TopicContract.View topicView, DataRepository dataRepository) {
        this.topicObject = topicObject;
        this.topicView = topicView;
        this.dataRepository = dataRepository;
    }

    @Override
    public void start() {
        topicView.showLoading();

        loadOlderMessages();
    }

    @Override
    public void loadOlderMessages() {
        if (isLoading) {
            return;
        }
        isLoading = true;
        currentPage++;

        dataRepository.getMessages(currentPage, topicObject.getId(), new DataSource.GetMessagesCallback() {
            @Override
            public void onResponse(MessagesResponse response) {
                isLoading = false;

                topicView.hideLoading();
                Collections.reverse(response.getResults());
                topicView.showMessages(response.getResults(), (currentPage == 0));
            }

            @Override
            public void onFailure() {
                isLoading = false;
            }

            @Override
            public void onNetworkFailure() {
                isLoading = false;
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
                RxBus.getInstance().send(new NewSubscription());
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
                RxBus.getInstance().send(new NewSubscription());
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
