package com.nebeek.newsstand.ui.topic;

import com.nebeek.newsstand.controller.base.BasePresenter;
import com.nebeek.newsstand.controller.base.BaseView;
import com.nebeek.newsstand.data.models.Topic;

import java.util.List;

public class TopicContract {

    public interface View extends BaseView<Presenter> {

        void showLoading();

        void hideLoading();

        void refreshMessagesList(int messagesCount, boolean pushedAtEnd);

        void changePlusToCheck();

        void changeCheckToPlus();

        void showAddToLibraryError();

        void refreshRelatedTopics(List<Topic> topicList);
    }

    public interface Presenter extends BasePresenter {

        void subscribeToTopic();

        void removeFromLibrary();

        void setTopicObject(Topic topicObject);

        void loadOlderMessages();

        void fetchRelatedTopics(List<List<Object>> request);
    }
}
