package com.nebeek.newsstand.ui.bookmark;

import com.nebeek.newsstand.controller.base.BasePresenter;
import com.nebeek.newsstand.controller.base.BaseView;

public class BookmarkContract {

    public interface View extends BaseView<Presenter> {
        void showLoading();

        void hideLoading();

        void refreshMessagesList();
    }

    public interface Presenter extends BasePresenter {

    }
}
