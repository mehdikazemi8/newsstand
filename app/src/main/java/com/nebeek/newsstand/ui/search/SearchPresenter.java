package com.nebeek.newsstand.ui.search;

import android.util.Log;

import com.nebeek.newsstand.data.DataRepository;
import com.nebeek.newsstand.data.DataSource;
import com.nebeek.newsstand.data.models.Topic;

import java.util.List;

public class SearchPresenter implements SearchContract.Presenter {

    private SearchContract.View searchView;
    private DataRepository dataRepository;

    public SearchPresenter(SearchContract.View searchView, DataRepository dataRepository) {
        this.searchView = searchView;
        this.dataRepository = dataRepository;
    }

    @Override
    public void start() {
        searchView.focusOnSearchInput();
    }

    @Override
    public void getTopics(String query) {
        if (query == null || query.length() < 2) {
            searchView.clearSuggestions();
            return;
        }

        Log.d("TAG", "abcdzzzzzz " + query);

        dataRepository.getAllTopics(query, new DataSource.TopicsResponseCallback() {
            @Override
            public void onResponse(List<Topic> myList) {
//                topicList.clear();
//                topicList.addAll(myList);
//
//                List<String> topicNames = new ArrayList<>();
//                for (Topic topic : topicList) {
//                    if (topic.getNames().size() > 0) {
//                        topicNames.add(topic.getNames().get(0).getFa());
//                        Log.d("TAG", "abcdhhhhhh ddddd " + topic.getNames().get(0).getFa());
//                    }
//                }
                searchView.showSuggestions(myList);
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
    public void onSuggestionClicked(Topic topic) {
        searchView.showTopicUI(topic);
        // todo must match the id not the text
//        for (Topic topic : topicList) {
//            if (topic.getNames().size() > 0 && topic.getNames().get(0).getFa().equals(suggestion)) {
//                mainView.showTopicUI(topic);
//                break;
//            }
//        }

    }
}
