package com.nebeek.newsstand.ui.library;

import com.nebeek.newsstand.controller.base.BasePresenter;
import com.nebeek.newsstand.controller.base.BaseView;
import com.nebeek.newsstand.data.models.Topic;

import java.util.List;

public class LibraryContract {

    public interface View extends BaseView<Presenter> {

        void showLoading();

        void hideLoading();

        void showKeywords(List<Topic> topicList);

        void showSearchUI(Topic topic);
    }

    public interface Presenter extends BasePresenter {

        void onKeywordSelected(Topic topic);
    }
}
