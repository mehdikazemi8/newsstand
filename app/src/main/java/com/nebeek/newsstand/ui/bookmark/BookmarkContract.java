package com.nebeek.newsstand.ui.bookmark;

import com.nebeek.newsstand.controller.BaseMessageView;
import com.nebeek.newsstand.controller.base.BasePresenter;

public class BookmarkContract {

    public interface View extends BaseMessageView {

        void showLoading();

        void hideLoading();

        void refreshMessagesList();

        void hideEmptyListMessage();

        void showEmptyListMessage();
    }

    public interface Presenter extends BasePresenter {

    }
}
