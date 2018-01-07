package com.nebeek.newsstand.ui.topic;

import android.util.Log;

import com.nebeek.newsstand.controller.base.BaseMessageListPresenter;
import com.nebeek.newsstand.data.DataRepository;
import com.nebeek.newsstand.data.DataSource;
import com.nebeek.newsstand.data.models.Snippet;
import com.nebeek.newsstand.data.models.Topic;
import com.nebeek.newsstand.data.remote.ApiService;
import com.nebeek.newsstand.data.remote.response.MessagesResponse;
import com.nebeek.newsstand.event.NewSubscription;
import com.nebeek.newsstand.event.RxBus;
import com.nebeek.newsstand.util.DateManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TopicPresenter implements TopicContract.Presenter, BaseMessageListPresenter {

    private Topic topicObject = null;
    private TopicContract.View topicView;
    private DataRepository dataRepository;
    private int currentPage = -1;
    private boolean isLoading = false;
    private final List<Snippet> messageList = new ArrayList<>();

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
                messageList.addAll(0, response.getResults());
                topicView.refreshMessagesList(response.getResults().size(), (currentPage == 0));
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
    public int getMessagesCount() {
        return messageList.size();
    }

    @Override
    public void onBindRowViewAtPosition(int position, SnippetRowView view) {
        if (messageList.get(position).getSource() != null) {
            view.setSourcePhoto(ApiService.BASE_URL + messageList.get(position).getSource().getImageSets().get(0).getImages().get(0).getData() + "/data");
            view.setSource(messageList.get(position).getSource().getNames().get(0).getFa());
        }

        if (messageList.get(position).getImageSets() != null && messageList.get(position).getImageSets().size() > 0) {
            view.makePhotoVisible();
            view.setPhoto(ApiService.BASE_URL + messageList.get(position).getImageSets().get(0).getImages().get(0).getData() + "/data");
        } else {
            view.makePhotoInvisible();
        }

        view.setDate(DateManager.convertStringToDate(messageList.get(position).getDateCreated()));

        view.setDescription(messageList.get(position).getFullText().getFa());
    }
}
