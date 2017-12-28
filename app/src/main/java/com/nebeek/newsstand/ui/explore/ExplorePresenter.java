package com.nebeek.newsstand.ui.explore;

import com.nebeek.newsstand.data.DataRepository;
import com.nebeek.newsstand.data.DataSource;
import com.nebeek.newsstand.data.remote.response.MessagesResponse;

import java.util.Collections;

public class ExplorePresenter implements ExploreContract.Presenter {

    private DataRepository dataRepository;
    private ExploreContract.View exploreView;
    private int currentPage = -1;
    private boolean isLoading = false;

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
        if (isLoading) {
            return;
        }
        isLoading = true;
        currentPage++;

        dataRepository.getMessages(currentPage, null, new DataSource.GetMessagesCallback() {
            @Override
            public void onResponse(MessagesResponse response) {
                isLoading = false;
                Collections.reverse(response.getResults());
                exploreView.showMessages(response.getResults(), (currentPage == 0));
            }

            @Override
            public void onFailure() {
                isLoading = false;
            }

            @Override
            public void onNetworkFailure() {
                isLoading = false;
            }
        });
    }
}
