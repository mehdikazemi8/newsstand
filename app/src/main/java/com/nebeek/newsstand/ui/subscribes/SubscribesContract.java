package com.nebeek.newsstand.ui.subscribes;

import com.nebeek.newsstand.controller.base.BasePresenter;
import com.nebeek.newsstand.controller.base.BaseView;
import com.nebeek.newsstand.data.models.Topic;

import java.util.List;

public class SubscribesContract {

    public interface View extends BaseView<Presenter> {

        void showLoading();

        void hideLoading();

        void showSubscribes(List<Topic> topicList);

        void showSearchUI(Topic topic);
    }

    public interface Presenter extends BasePresenter {

        void onKeywordSelected(Topic topic);
    }
}
