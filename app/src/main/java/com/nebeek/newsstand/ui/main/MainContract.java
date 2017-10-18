package com.nebeek.newsstand.ui.main;


import android.support.v7.widget.SearchView;

import com.nebeek.newsstand.controller.base.BasePresenter;
import com.nebeek.newsstand.controller.base.BaseView;
import com.nebeek.newsstand.data.models.Topic;

import java.util.List;

public class MainContract {

    interface View extends BaseView<Presenter> {

        void showSearchUI(Topic topic);

        void showSuggestions(List<String> suggestions);
    }

    interface Presenter extends BasePresenter, SearchView.OnQueryTextListener {

        void getTopics(String query);

        void onSuggestionClicked(String suggestion);
    }
}
