package com.nebeek.newsstand.ui.onboarding;

import com.annimon.stream.Stream;
import com.nebeek.newsstand.data.DataRepository;
import com.nebeek.newsstand.data.DataSource;
import com.nebeek.newsstand.data.models.Topic;

import java.util.List;

public class OnboardingPresenter implements OnboardingContract.Presenter {

    private OnboardingContract.View onboardingTopicsView;
    private DataRepository dataRepository;
    private int currentPage = 0;
    private List<List<Topic>> pages;

    public OnboardingPresenter(OnboardingContract.View onboardingTopicsView,
                               DataRepository dataRepository) {
        this.onboardingTopicsView = onboardingTopicsView;
        this.dataRepository = dataRepository;
    }

    @Override
    public void start() {

        dataRepository.getOnboardingTopics(new DataSource.TopicsResponseCallback() {
            @Override
            public void onResponse(List<Topic> topicList) {
                pages = Stream.of(topicList).chunkBy(Topic::getOnboarding).toList();
                onboardingTopicsView.setPageNumber(currentPage + 1, pages.size());
                onboardingTopicsView.showOnboardingTopics(pages.get(currentPage));
            }

            @Override
            public void onFailure() {

            }

            @Override
            public void onNetworkFailure() {

            }
        });
    }


    @Override
    public void fetchNextPage(List<String> selectedTopicsIDs) {
        registerTopics(selectedTopicsIDs);

        ++currentPage;

        if (currentPage == pages.size()) {
            onboardingTopicsView.showMainPageUI();
        } else {
            onboardingTopicsView.setPageNumber(currentPage + 1, pages.size());
            onboardingTopicsView.showOnboardingTopics(pages.get(currentPage));
        }
    }

    private void registerTopics(List<String> selectedTopicsIDs) {
        for (String topicID : selectedTopicsIDs) {
            dataRepository.subscribeToTopic(topicID, new DataSource.SubscribeCallback() {
                @Override
                public void onSuccess() {

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
}
