package com.nebeek.newsstand.ui.main;


import android.support.v7.widget.SearchView;

import com.nebeek.newsstand.controller.base.BasePresenter;
import com.nebeek.newsstand.controller.base.BaseView;

public class MainContract {

    interface View extends BaseView<Presenter> {

        void showSearchUI(String keyword);
    }

    interface Presenter extends BasePresenter, SearchView.OnQueryTextListener {

    }
}
