package com.nebeek.newsstand.ui.explore;

import com.nebeek.newsstand.controller.base.BasePresenter;
import com.nebeek.newsstand.controller.base.BaseView;
import com.nebeek.newsstand.data.models.Snippet;

import java.util.List;

public class ExploreContract {

    public interface View extends BaseView<Presenter> {

        void refreshMessagesList(int messagesCount, boolean scrollToEnd);
    }

    public interface Presenter extends BasePresenter {

        void loadOlderMessages();
    }
}
