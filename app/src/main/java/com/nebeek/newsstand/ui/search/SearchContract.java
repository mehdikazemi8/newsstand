package com.nebeek.newsstand.ui.search;

import android.support.v7.widget.SearchView;

import com.nebeek.newsstand.controller.base.BasePresenter;
import com.nebeek.newsstand.controller.base.BaseView;
import com.nebeek.newsstand.data.models.Snippet;

import java.util.List;

public class SearchContract {

    public interface View extends BaseView<Presenter> {

        void focusOnSearchView();

        void showLoading();

        void hideLoading();

        void showSearchResults(List<Snippet> snippetList);
    }

    public interface Presenter extends BasePresenter, SearchView.OnQueryTextListener {

    }
}
