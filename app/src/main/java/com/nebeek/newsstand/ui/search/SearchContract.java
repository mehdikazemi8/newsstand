package com.nebeek.newsstand.ui.search;

import com.nebeek.newsstand.controller.base.BasePresenter;
import com.nebeek.newsstand.controller.base.BaseView;
import com.nebeek.newsstand.data.models.Snippet;

import java.util.List;

public class SearchContract {

    public interface View extends BaseView<Presenter> {

        void showLoading();

        void hideLoading();

        void showSearchResults(List<Snippet> snippetList);

        void changePlusToCheck();

        void changeCheckToPlus();
    }

    public interface Presenter extends BasePresenter {

        void addToLibrary();

        void removeFromLibrary();
    }
}
