package com.nebeek.newsstand.ui.explore;

import com.nebeek.newsstand.controller.BaseMessageView;
import com.nebeek.newsstand.controller.base.BasePresenter;
import com.nebeek.newsstand.controller.base.BaseView;

public class ExploreContract {

    public interface View extends BaseMessageView {

        void refreshMessagesList(int messagesCount, boolean pushedAtEnd);
    }

    public interface Presenter extends BasePresenter {

        void loadOlderMessages();
    }
}
