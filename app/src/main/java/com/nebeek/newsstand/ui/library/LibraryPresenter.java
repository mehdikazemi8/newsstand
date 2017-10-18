package com.nebeek.newsstand.ui.library;

import android.util.Log;

import com.nebeek.newsstand.data.DataRepository;
import com.nebeek.newsstand.data.DataSource;
import com.nebeek.newsstand.data.models.Topic;

import java.util.List;

public class LibraryPresenter implements LibraryContract.Presenter {

    private DataRepository dataRepository;
    private LibraryContract.View libraryView;

    public LibraryPresenter(DataRepository dataRepository, LibraryContract.View libraryView) {
        this.dataRepository = dataRepository;
        this.libraryView = libraryView;
    }

    @Override
    public void start() {

        libraryView.showLoading();

        dataRepository.getKeywords(new DataSource.GetKeywordsCallback() {
            @Override
            public void onResponse(List<Topic> topicList) {

                Log.d("TAG", "onResponse 111");

                if (!libraryView.isActive()) {
                    return;
                }
                libraryView.hideLoading();

                libraryView.showKeywords(topicList);
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
