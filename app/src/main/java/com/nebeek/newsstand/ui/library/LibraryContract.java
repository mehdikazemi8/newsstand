package com.nebeek.newsstand.ui.library;

import com.nebeek.newsstand.controller.base.BasePresenter;
import com.nebeek.newsstand.controller.base.BaseView;
import com.nebeek.newsstand.data.models.Keyword;

import java.util.List;

public class LibraryContract {

    public interface View extends BaseView<Presenter> {

        void showLoading();

        void hideLoading();

        void showKeywords(List<Keyword> keywordList);

        void showSearchUI(Keyword keyword);
    }

    public interface Presenter extends BasePresenter {

        void onKeywordSelected(Keyword keyword);
    }
}
