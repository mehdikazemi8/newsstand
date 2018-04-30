package com.nebeek.newsstand.ui.onboarding;

import com.nebeek.newsstand.controller.base.BasePresenter;
import com.nebeek.newsstand.controller.base.BaseView;
import com.nebeek.newsstand.data.models.Topic;

import java.util.List;

public class OnboardingContract {

    interface View extends BaseView<Presenter> {

        void showOnboardingTopics(List<Topic> topics);

        void setPageNumber(int pageNumber, int allPagesCount);

        void showMainPageUI();
    }

    interface Presenter extends BasePresenter {

        void fetchNextPage();

    }
}
