package com.nebeek.newsstand.ui.topic;

import android.util.Log;

import com.nebeek.newsstand.controller.base.MessageListPresenter;
import com.nebeek.newsstand.data.DataRepository;
import com.nebeek.newsstand.data.DataSource;
import com.nebeek.newsstand.data.models.Topic;
import com.nebeek.newsstand.data.remote.response.MessagesResponse;
import com.nebeek.newsstand.event.NewSubscription;
import com.nebeek.newsstand.event.RxBus;

import java.util.Collections;
import java.util.List;

public class TopicPresenter extends MessageListPresenter implements TopicContract.Presenter {

    private Topic topicObject = null;
    private TopicContract.View topicView;
    private int offset = 0;
    private boolean isLoading = false;

    public TopicPresenter(Topic topicObject, TopicContract.View topicView, DataRepository dataRepository) {
        super(dataRepository);
        this.topicObject = topicObject;
        this.topicView = topicView;
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

        dataRepository.getMessages(offset, topicObject.getId(), new DataSource.GetMessagesCallback() {
            @Override
            public void onResponse(MessagesResponse response) {
                isLoading = false;

                topicView.hideLoading();
                Collections.reverse(response.getResults());
                messageList.addAll(0, response.getResults());
                topicView.refreshMessagesList(response.getResults().size(), false);

                offset += 10;
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
        Log.d("TAG", "removeFromLibrary " + (topicObject == null));
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

    @Override
    public void fetchRelatedTopics(List<List<Object>> request) {
        dataRepository.fetchRelatedTopics(request, new DataSource.TopicsResponseCallback() {
            @Override
            public void onResponse(List<Topic> topicList) {
                topicView.refreshRelatedTopics(topicList);
            }

            @Override
            public void onFailure() {

            }

            @Override
            public void onNetworkFailure() {

            }
        });
    }
}
