package com.nebeek.newsstand.ui.main;


import com.nebeek.newsstand.data.DataRepository;
import com.nebeek.newsstand.data.local.PreferenceManager;

public class MainPresenter implements MainContract.Presenter {
    private PreferenceManager preferenceManager;
    private DataRepository dataRepository;
    private MainContract.View mainView;

    public MainPresenter(PreferenceManager preferenceManager, DataRepository dataRepository, MainContract.View mainView) {
        this.preferenceManager = preferenceManager;
        this.dataRepository = dataRepository;
        this.mainView = mainView;
    }

    @Override
    public void start() {

    }

    @Override
    public boolean onQueryTextSubmit(String keyword) {
        mainView.showSearchUI(keyword);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
