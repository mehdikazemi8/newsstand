package com.nebeek.newsstand.ui.subscribes;

import android.util.Log;

import com.nebeek.newsstand.data.DataRepository;
import com.nebeek.newsstand.data.DataSource;
import com.nebeek.newsstand.data.models.Topic;
import com.nebeek.newsstand.event.NewSubscription;
import com.nebeek.newsstand.event.RxBus;

import java.util.List;

public class SubscribesPresenter implements SubscribesContract.Presenter {

    private DataRepository dataRepository;
    private SubscribesContract.View libraryView;

    public SubscribesPresenter(DataRepository dataRepository, SubscribesContract.View libraryView) {
        this.dataRepository = dataRepository;
        this.libraryView = libraryView;

        RxBus.getInstance().toObservable().subscribe(object -> {
            if (object instanceof NewSubscription) {
                start();
            }
        });
    }

    @Override
    public void start() {

        libraryView.showLoading();

        dataRepository.getSubscribes(new DataSource.GetSubscribesCallback() {
            @Override
            public void onResponse(List<Topic> topicList) {

                Log.d("TAG", "onResponse 111");

                if (!libraryView.isActive()) {
                    return;
                }
                libraryView.hideLoading();

                libraryView.showSubscribes(topicList);
            }

            @Override
            public void onFailure() {
                Log.d("TAG", "onFailure 111");

                if (!libraryView.isActive()) {
                    return;
                }
                libraryView.hideLoading();

            }

            @Override
            public void onNetworkFailure() {
                Log.d("TAG", "onNetworkFailure 111");

                if (!libraryView.isActive()) {
                    return;
                }
                libraryView.hideLoading();

            }
        });
    }

    @Override
    public void onKeywordSelected(Topic topic) {
        topic.setInLibrary(true);
        libraryView.showSearchUI(topic);
    }
}
