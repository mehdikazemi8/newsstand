package com.nebeek.newsstand.ui.explore;

import com.nebeek.newsstand.data.DataRepository;
import com.nebeek.newsstand.data.DataSource;
import com.nebeek.newsstand.data.remote.response.MessagesResponse;

public class ExplorePresenter implements ExploreContract.Presenter {

    private DataRepository dataRepository;
    private ExploreContract.View exploreView;
    private int currentPage = -1;

    public ExplorePresenter(ExploreContract.View exploreView, DataRepository dataRepository) {
        this.exploreView = exploreView;
        this.dataRepository = dataRepository;
    }

    @Override
    public void start() {

        loadOlderMessages();
    }

    @Override
    public void loadOlderMessages() {
        currentPage++;

        dataRepository.getMessages(currentPage, null, new DataSource.GetMessagesCallback() {
            @Override
            public void onResponse(MessagesResponse response) {

                exploreView.showMessages(response.getResults(), (currentPage == 0));
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
