package com.nebeek.newsstand.ui.splash;


import com.nebeek.newsstand.controller.base.BasePresenter;
import com.nebeek.newsstand.controller.base.BaseView;

public class SplashContract {

    interface View extends BaseView<Presenter> {

        void showMainPageUI();
    }

    interface Presenter extends BasePresenter {

    }
}
