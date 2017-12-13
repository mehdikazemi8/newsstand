package com.nebeek.newsstand.ui.topic;

import com.nebeek.newsstand.data.DataRepository;
import com.nebeek.newsstand.data.DataSource;
import com.nebeek.newsstand.data.models.Topic;
import com.nebeek.newsstand.data.remote.response.MessagesResponse;
import com.nebeek.newsstand.event.NewSubscription;
import com.nebeek.newsstand.event.RxBus;

public class TopicPresenter implements TopicContract.Presenter {

    private Topic topicObject = null;
    private TopicContract.View topicView;
    private DataRepository dataRepository;
    private int currentPage = -1;
    private boolean loading = false;

    public TopicPresenter(Topic topicObject, TopicContract.View topicView, DataRepository dataRepository) {
        this.topicObject = topicObject;
        this.topicView = topicView;
        this.dataRepository = dataRepository;
    }

    @Override
    public void start() {
        topicView.showLoading();

        loadMessages();
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
    public void loadMessages() {
        if (loading) {
            return;
        }

        currentPage++;

        loading = true;
        dataRepository.getMessages(currentPage, topicObject.getId(), new DataSource.GetMessagesCallback() {
            @Override
            public void onResponse(MessagesResponse response) {
                loading = false;

                topicView.hideLoading();
                topicView.showGetMessagesResults(response.getResults());
            }

            @Override
            public void onFailure() {
                loading = false;
            }

            @Override
            public void onNetworkFailure() {
                loading = false;
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
