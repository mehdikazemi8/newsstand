package com.nebeek.newsstand.ui.main;


import android.util.Log;

import com.nebeek.newsstand.data.DataRepository;
import com.nebeek.newsstand.data.DataSource;
import com.nebeek.newsstand.data.local.PreferenceManager;
import com.nebeek.newsstand.data.models.Topic;

import java.util.ArrayList;
import java.util.List;

public class MainPresenter implements MainContract.Presenter {
    private PreferenceManager preferenceManager;
    private DataRepository dataRepository;
    private MainContract.View mainView;

    private List<Topic> topicList = new ArrayList<>();

    public MainPresenter(PreferenceManager preferenceManager, DataRepository dataRepository, MainContract.View mainView) {
        this.preferenceManager = preferenceManager;
        this.dataRepository = dataRepository;
        this.mainView = mainView;
    }

    @Override
    public void start() {
        sendFCMToServer();
    }

    private void sendFCMToServer() {
        if (preferenceManager.getFcmID() == null) {
            return;
        }

        String token = preferenceManager.getFcmID().instanceId();
        DataRepository.getInstance().sendFcmIDToServer(token, new DataSource.SendFcmIDCallback() {
            @Override
            public void onSuccess() {
                Log.d("TAG", "Refreshed token hhhh: " + token);
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
    public boolean onQueryTextSubmit(String keyword) {
//        mainView.showSearchUI(keyword);
        return false;
    }


    @Override
    public boolean onQueryTextChange(String newText) {
        Log.d("TAG", "abcd " + newText);

        getTopics(newText);

        return false;
    }

    @Override
    public void getTopics(String query) {

        dataRepository.getAllTopics(query, new DataSource.TopicsResponseCallback() {
            @Override
            public void onResponse(List<Topic> myList) {
                topicList.clear();
                topicList.addAll(myList);

                for (Topic topic : topicList) {
                    Log.d("TAG", "topic " + topic.serialize());
                }

                List<String> topicNames = new ArrayList<>();
                for (Topic topic : topicList) {
                    // todo
                    if (topicNames.size() > 10) {
                        break;
                    }
                    if (topic.getNames().size() > 0) {
                        topicNames.add(topic.getNames().get(0));
                    }
                }

                mainView.showSuggestions(topicNames);
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
    public void onSuggestionClicked(String suggestion) {
        for (Topic topic : topicList) {
            if (topic.getNames().size() > 0 && topic.getNames().get(0).equals(suggestion)) {
                Log.d("TAG", "abcd onSuggestionClicked " + topic.serialize());
                mainView.showSearchUI(topic);
            }
        }
    }
}
