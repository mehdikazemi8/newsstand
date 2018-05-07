package com.nebeek.newsstand.ui.search;

import com.nebeek.newsstand.controller.base.BasePresenter;
import com.nebeek.newsstand.controller.base.BaseView;
import com.nebeek.newsstand.data.models.Topic;

import java.util.List;

public class SearchContract {

    interface View extends BaseView<Presenter> {

        void focusOnSearchInput();

        void showSuggestions(List<Topic> suggestions);

        void clearSuggestions();

        void showTopicUI(Topic topic);
    }

    interface Presenter extends BasePresenter {

        void getTopics(String query);

        void onSuggestionClicked(Topic topic);
    }
}
