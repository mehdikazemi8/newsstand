package com.nebeek.newsstand.ui.explore;

import com.nebeek.newsstand.controller.base.BaseMessageListPresenter;
import com.nebeek.newsstand.data.DataRepository;
import com.nebeek.newsstand.data.DataSource;
import com.nebeek.newsstand.data.models.LikeRequest;
import com.nebeek.newsstand.data.models.TelegramMessage;
import com.nebeek.newsstand.data.remote.ApiService;
import com.nebeek.newsstand.data.remote.response.MessagesResponse;
import com.nebeek.newsstand.ui.topic.MessageRowView;
import com.nebeek.newsstand.util.DateManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExplorePresenter implements ExploreContract.Presenter, BaseMessageListPresenter {

    private DataRepository dataRepository;
    private ExploreContract.View exploreView;
    private int currentPage = -1;
    private boolean isLoading = false;
    private final List<TelegramMessage> messageList = new ArrayList<>();

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
                messageList.addAll(0, response.getResults());
                exploreView.refreshMessagesList(response.getResults().size(), (currentPage == 0));
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

    @Override
    public int getMessagesCount() {
        return messageList.size();
    }

    @Override
    public void onBindRowViewAtPosition(int position, MessageRowView view) {
        if (messageList.get(position).getSource() != null) {
            view.setSourcePhoto(ApiService.BASE_URL + messageList.get(position).getSource().getImageSets().get(0).getImages().get(0).getData());
            view.setSource(messageList.get(position).getSource().getNames().get(0).getFa());
        }

        if (messageList.get(position).getImageSets() != null && messageList.get(position).getImageSets().size() > 0) {
            view.makePhotoVisible();
            view.setPhoto(ApiService.BASE_URL + messageList.get(position).getImageSets().get(0).getImages().get(0).getData());
        } else {
            view.makePhotoInvisible();
        }

        if (messageList.get(position).getLiked() != null && messageList.get(position).getLiked()) {
            view.showLikeIcon();
        } else {
            view.hideLikeIcon();
        }

        view.setDate(DateManager.convertStringToDate(messageList.get(position).getDateCreated()));

        view.setDescription(messageList.get(position).getFullText().getFa());
    }

    @Override
    public void likeMessage(int position) {
        messageList.get(position).setLiked(true);
        dataRepository.likeMessage(new LikeRequest(messageList.get(position).getId()),
                new DataSource.LikeMessageCallback() {
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
