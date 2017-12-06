package com.nebeek.newsstand.ui.explore;

import com.nebeek.newsstand.data.DataRepository;
import com.nebeek.newsstand.data.DataSource;
import com.nebeek.newsstand.data.remote.response.MessagesResponse;

public class ExplorePresenter implements ExploreContract.Presenter {

    private DataRepository dataRepository;
    private ExploreContract.View exploreView;

    public ExplorePresenter(ExploreContract.View exploreView, DataRepository dataRepository) {
        this.exploreView = exploreView;
        this.dataRepository = dataRepository;
    }

    @Override
    public void start() {

        dataRepository.getMessages(null, null, new DataSource.GetMessagesCallback() {
            @Override
            public void onResponse(MessagesResponse response) {
                exploreView.showMessages(response.getResults());
            }

            @Override
            public void onFailure() {

            }

            @Override
            public void onNetworkFailure() {

            }
        });
    }
}
