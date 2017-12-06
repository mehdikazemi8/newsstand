package com.nebeek.newsstand.ui.topic;

import com.nebeek.newsstand.controller.base.BasePresenter;
import com.nebeek.newsstand.controller.base.BaseView;
import com.nebeek.newsstand.data.models.Topic;
import com.nebeek.newsstand.data.models.Snippet;

import java.util.List;

public class TopicContract {

    public interface View extends BaseView<Presenter> {

        void showLoading();

        void hideLoading();

        void showGetMessagesResults(List<Snippet> snippetList);

        void changePlusToCheck();

        void changeCheckToPlus();

        void showAddToLibraryError();
    }

    public interface Presenter extends BasePresenter {

        void subscribeToTopic();

        void removeFromLibrary();

        void setTopicObject(Topic topicObject);

        void loadMessages();
    }
}
