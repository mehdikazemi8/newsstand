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
    private SubscribesContract.View subscribesView;

    public SubscribesPresenter(DataRepository dataRepository, SubscribesContract.View subscribesView) {
        this.dataRepository = dataRepository;
        this.subscribesView = subscribesView;

        RxBus.getInstance().toObservable().subscribe(object -> {
            if (object instanceof NewSubscription) {
                start();
            }
        });
    }

    @Override
    public void start() {

        subscribesView.showLoading();

        dataRepository.getSubscribes(new DataSource.GetSubscribesCallback() {
            @Override
            public void onResponse(List<Topic> topicList) {

                Log.d("TAG", "onResponse 111");

                if (!subscribesView.isActive()) {
                    return;
                }
                subscribesView.hideLoading();

                subscribesView.showSubscribes(topicList);
            }

            @Override
            public void onFailure() {
                Log.d("TAG", "onFailure 111");

                if (!subscribesView.isActive()) {
                    return;
                }
                subscribesView.hideLoading();

            }

            @Override
            public void onNetworkFailure() {
                Log.d("TAG", "onNetworkFailure 111");

                if (!subscribesView.isActive()) {
                    return;
                }
                subscribesView.hideLoading();

            }
        });
    }

    @Override
    public void onKeywordSelected(Topic topic) {
        dataRepository.updateTopicReadCount(topic.getContents(), topic.getId());

        topic.setInLibrary(true);
        subscribesView.showSearchUI(topic);
    }
}
