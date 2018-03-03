package com.nebeek.newsstand.ui.trendingtopics;

import com.nebeek.newsstand.data.DataRepository;

public class TrendingTopicsPresenter implements TrendingTopicsContract.Presenter {

    private TrendingTopicsContract.View trendingTopicsView;
    private DataRepository dataRepository;

    public TrendingTopicsPresenter(TrendingTopicsContract.View trendingTopicsView,
                                   DataRepository dataRepository) {
        this.trendingTopicsView = trendingTopicsView;
    }

    @Override
    public void start() {



    }
}
